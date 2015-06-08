
package ch.hearc.p2.java.view.jpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		method = JPanelSetEquation.getNoMethod();
		prepareTabNameVar(m, n, method);

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
			for(int j = 1; j <= m + 1; j++)
				{
				JPanel panMatrice = new JPanel();
				panMatrice.setLayout(new GridLayout(0, 2));

				String string;

				if (j == m)
					{
					string = tabString[j - 1] +  " =";
					}
				else
					{
					if (j == m + 1)
						{
						string = "";
						}
					else
						{
						string = tabString[j - 1] + " +";
						}
					}
				JLabel label = new JLabel(string);
				label.setHorizontalAlignment(SwingConstants.CENTER);
				JTextField textfield = new JTextField();
				tabTextField[i - 1][j - 1] = textfield;
				textfield.setPreferredSize(new Dimension(50, 30));

				if (controllerEquation.isEquationSolved() && !controllerEquation.isCreating())
					{
					textfield.setText(String.valueOf(controllerEquation.getEquation().getMatrix(0).get(i - 1, j - 1)));
					}

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

	private void prepareTabNameVar(int m, int n, int noMethod)
		{

		switch(noMethod)
			{
			case 2:
				if (n > 3)
					{
					prepareTabNameVar(m, n, 1);
					
					}
				else
					{
					tabString = new String[3];
					tabString[0] = "x";
					tabString[1] = "y";
					tabString[2] = "z";
					}
				break;
			case 1:
				tabString = new String[27];
				tabString[0] = "a";
				tabString[1] = "b";
				tabString[2] = "c";
				tabString[3] = "d";
				tabString[4] = "e";
				tabString[5] = "f";
				tabString[6] = "g";
				tabString[7] = "h";
				tabString[8] = "i";
				tabString[9] = "j";
				tabString[10] = "k";
				tabString[11] = "l";
				tabString[12] = "m";
				tabString[13] = "n";
				tabString[14] = "o";
				tabString[15] = "p";
				tabString[16] = "q";
				tabString[17] = "r";
				tabString[18] = "s";
				tabString[19] = "z";
				tabString[20] = "u";
				tabString[21] = "v";
				tabString[22] = "w";
				tabString[23] = "x";
				tabString[24] = "y";
				tabString[25] = "z";
				break;
			case 0:
				tabString = new String[n + 1];

				for(int i = 1; i <= n; i++)
					{
					tabString[i - 1] = "x"+i;
					}

				break;

			default:
				throw new IllegalStateException("Not valide variable display");
			}
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
	private static float[][] tabFloat;
	private int n;
	private int m;
	private int method;
	private static String[] tabString;

	}
