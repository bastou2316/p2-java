
package ch.hearc.p2.java.view.jpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import ch.hearc.p2.java.controller.ControllerEquation;
import ch.hearc.p2.java.controller.ControllerMain;
import ch.hearc.p2.java.controller.ControllerMain.DIALOG;
import ch.hearc.p2.java.controller.ControllerMain.PANEL;
import ch.hearc.p2.java.model.Matrix;

public class JPanelSetMatrix extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelSetMatrix(ControllerMain controllerMain, ControllerEquation controllerEquation)
		{
		super();

		this.controllerMain = controllerMain;
		this.controllerEquation = controllerEquation;

		this.n = controllerEquation.getNumberEquation();
		this.m = controllerEquation.getNumberVar();

		tab2d = IndependentVar.create(n,m);
		IndependentVar.peupler(tab2d,0); //a changer ici pour le nom des variables

		// Composition du panel
		geometry();
		control();
		appearance();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void geometry()
		{
		// JComponent : Instanciation
		JPanel panelVar = new JPanel();
		panelVar.setLayout(new GridLayout(n, m, 0, 0));
		panelVar.setBorder(new TitledBorder(null, "Remplissage de la matrice", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));

		JPanel panelButton = new JPanel();

		solveButton = new JButton("R\u00E9soudre");
		previousButton = new JButton("Précédent");

		tabTextField = new JTextField[n][];
		for(int i = 1; i <= n; i++)
			{
			tabTextField[i - 1] = new JTextField[m + 1];
			for(int j = 1; j <= m +1; j++)
				{
//				JPanel panMatrice = new JPanel();
//				panMatrice.setLayout(new GridLayout(0, 2));
				Box panMatrice = Box.createHorizontalBox();

				String string;

				if (j == m)
					{
					string = tab2d[i-1][j - 1] + j + "=";
					}
				else
					{
					if (j == m + 1)
						{
						string = "";
						}
					else
						{
						string = tab2d[i-1][j - 1] + j + "+";
						}
					}
				JLabel label = new JLabel(string);


				label.setHorizontalAlignment(SwingConstants.CENTER);
				JTextField textfield = new JTextField();
				tabTextField[i - 1][j - 1] = textfield;
				textfield.setPreferredSize(new Dimension(50, 30));
				textfield.setSize(getPreferredSize());
				if (controllerEquation.isEquationSolved() && !controllerEquation.isCreating())
					{
					textfield.setText(String.valueOf(controllerEquation.getEquation().getMatrix(0).get(i - 1, j - 1)));
					}

			//	textfield.setMaximumSize(new Dimension(Integer.MAX_VALUE, textfield.getPreferredSize().height));

				Box box = Box.createVerticalBox();
				box.add(Box.createVerticalGlue());
				box.add(textfield);
				box.add(Box.createVerticalGlue());

				panMatrice.add(textfield);
				panMatrice.add(label);
				panelVar.add(panMatrice);
				}
			}

			// Layout : Specification
			{
			setLayout(new BorderLayout());
			}

		// JComponent : add
		panelButton.add(previousButton);
		panelButton.add(solveButton);

		add(panelVar, BorderLayout.CENTER);
		add(panelButton, BorderLayout.SOUTH);
		}

	private void control()
		{
		solveButton.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					//Remplissage de la matrice
					Matrix matrix = new Matrix(n, m + 1);

					for(int i = 0; i < n; i++)
						{
						for(int j = 0; j < m + 1; j++)
							{
							//System.out.println(Float.parseFloat(tabTextField[i][j].getText()));
							float value = (tabTextField[i][j].getText().isEmpty()) ? 0 : Float.parseFloat(tabTextField[i][j].getText());
							matrix.set(i, j, value);
							}
						}

					controllerEquation.setMatrix(matrix);
					controllerEquation.applyTempEquation();//Fin de la version provisoire
					controllerEquation.solveEquation();

					//Changement de fenêtre
					if (controllerEquation.getStepMode())
						{
						controllerMain.changeView(PANEL.RESULT_STEP);
						}
					else
						{
						controllerMain.changeView(PANEL.RESULT);
						}
					}
			});

		previousButton.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					controllerMain.showDialog(DIALOG.SET_EQUATION);
					}
			});
		}

	private void appearance()
		{
		// rien
		}


	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private ControllerMain controllerMain;
	private ControllerEquation controllerEquation;

	// Tools
	private JButton solveButton, previousButton;
	private JTextField[][] tabTextField;
	private int n;
	private int m;
	private int method;
	String[][] tab2d;

	}
