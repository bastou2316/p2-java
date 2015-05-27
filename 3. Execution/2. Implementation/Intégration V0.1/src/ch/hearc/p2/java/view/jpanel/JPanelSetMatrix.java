
package ch.hearc.p2.java.view.jpanel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

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
		solveButton = new JButton("Résoudre");
		previousButton = new JButton("Précédent");

		createMatrixTables();

			// Layout : Specification
			{
			FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
			setLayout(flowlayout);
			}

		// JComponent : add
		for(int i = 0; i < matrixTables.length; i++)
			{
			add(matrixTables[i]);

			if (i != matrixTables.length - 1)//pas de derniere ligne
				{
				add(varsTables[i]);
				}
			}

		add(solveButton);
		add(previousButton);
		}

	private void control()
		{
		solveButton.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					//Remplissage de la matrice
					Matrix matrix = new Matrix(controllerEquation.getNumberVar(), controllerEquation.getNumberEquation());
					controllerEquation.setMatrix(matrix);

					//Changement de fenêtre
					if (controllerEquation.getStepMode())
						{
						controllerMain.changeView(PANEL.RESULT_STEP);
						}
					else
						{
						controllerMain.changeView(PANEL.RESULT);
						}

					//controllerMain.closeDialog();
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

	private void createMatrixTables()
		{
		int nbVar = controllerEquation.getNumberVar();
		int nbEqu = controllerEquation.getNumberEquation();

		System.out.println(nbVar);

		matrixTables = new JTable[nbVar + 1];
		varsTables = new JTable[nbVar];

		for(int j = 0; j < nbVar; j++)
			{
			matrixTables[j] = new JTable(nbEqu, 1);
			matrixTables[j].setRowHeight(40);
			matrixTables[j].setCellSelectionEnabled(false);
			varsTables[j] = new JTable(nbEqu, 1);
			varsTables[j].setRowHeight(40);
			varsTables[j].setShowVerticalLines(false);
			varsTables[j].setEnabled(false);
			}
		matrixTables[nbVar] = new JTable(nbEqu, 1);
		matrixTables[nbVar].setRowHeight(40);
		matrixTables[nbVar].setRowSelectionAllowed(false);
		matrixTables[nbVar].setBackground(Color.YELLOW);

		char addOrEqual;
		switch(0)
			//varStyle.getSelectedIndex())
			{
			case 0:
				for(int i = 0; i < nbEqu; i++)
					{
					addOrEqual = '+';
					for(int j = 0; j < nbVar; j++)
						{
						if (j + 1 == nbVar)
							{
							addOrEqual = '=';
							}
						varsTables[j].setValueAt("  x" + j + "   " + addOrEqual, i, 0);
						}
					}
				break;

			case 1:
				for(int i = 0; i < nbEqu; i++)
					{
					addOrEqual = '+';
					for(int j = 0; j < nbVar; j++)
						{
						if (j == nbVar - 1)
							{
							addOrEqual = '=';
							}
						varsTables[j].setValueAt("     " + (char)(97 + j) + "      " + addOrEqual, i, 0);
						}
					}
				break;

			case 2:
				for(int i = 0; i < nbEqu; i++)
					{
					addOrEqual = '+';
					for(int j = 0; j < nbVar; j++)
						{
						if (j == nbVar - 1)
							{
							addOrEqual = '=';
							}
						varsTables[j].setValueAt("     " + (char)(120 + j) + "      " + addOrEqual, i, 0);
						}
					}
				break;
			}

		boxV = Box.createVerticalBox();
		boxH = Box.createHorizontalBox();

		boxH.add(Box.createHorizontalGlue());
		for(int j = 0; j < nbVar; j++)
			{
			boxH.add(matrixTables[j]);
			boxH.add(Box.createHorizontalStrut(5));
			boxH.add(varsTables[j]);
			boxH.add(Box.createHorizontalStrut(5));
			}
		boxH.add(matrixTables[nbVar]);
		boxH.add(Box.createHorizontalGlue());

		boxV.add(Box.createVerticalGlue());
		boxV.add(boxH);
		boxV.add(Box.createVerticalGlue());

		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private ControllerMain controllerMain;
	private ControllerEquation controllerEquation;

	// Tools
	private JTable[] matrixTables;
	private JTable[] varsTables;
	private Box boxV;
	private Box boxH;

	private JButton solveButton, previousButton;

	}
