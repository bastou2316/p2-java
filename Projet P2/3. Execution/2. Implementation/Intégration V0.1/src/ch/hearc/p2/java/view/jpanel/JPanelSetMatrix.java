
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
		previousButton = new JButton("Pr�c�dent");

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
					string = tabString[j - 1] + j + "=";
					}
				else
					{
					if (j == m + 1)
						{
						string = "";
						}
					else
						{
						string = tabString[j - 1] + j + "+";
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

					//Changement de fen�tre
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
			case 0:
				if (n > 3)
					{
					prepareTabNameVar(m, n, 2);
					}
				else
					{
					tabString = new String[3];
					tabString[0] = "X";
					tabString[1] = "Y";
					tabString[2] = "Z";
					}
				break;
			case 1:
				tabString = new String[27];
				tabString[0] = "A";
				tabString[1] = "B";
				tabString[2] = "C";
				tabString[3] = "D";
				tabString[4] = "E";
				tabString[5] = "F";
				tabString[6] = "G";
				tabString[7] = "H";
				tabString[8] = "I";
				tabString[9] = "J";
				tabString[10] = "K";
				tabString[11] = "L";
				tabString[12] = "M";
				tabString[13] = "N";
				tabString[14] = "O";
				tabString[15] = "P";
				tabString[16] = "Q";
				tabString[17] = "R";
				tabString[18] = "S";
				tabString[19] = "T";
				tabString[20] = "U";
				tabString[21] = "V";
				tabString[22] = "W";
				tabString[23] = "X";
				tabString[24] = "Y";
				tabString[25] = "Z";
				break;
			case 2:
				tabString = new String[n + 1];

				for(int i = 1; i <= n; i++)
					{
					tabString[i - 1] = "X";
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
