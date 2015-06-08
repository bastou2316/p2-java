
package ch.hearc.p2.java.view.jpanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import ch.hearc.p2.java.controller.ControllerEquation;
import ch.hearc.p2.java.controller.ControllerMain;
import ch.hearc.p2.java.controller.ControllerMain.DIALOG;
import ch.hearc.p2.java.model.Equation;

public class JPanelSetEquation extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelSetEquation(ControllerMain controllerMain, ControllerEquation controllerEquation)
		{
		super();

		this.controllerMain = controllerMain;
		this.controllerEquation = controllerEquation;

		equation = controllerEquation.getEquation();

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
		JPanel jpanelCenter = new JPanel();

		JPanel jpanelMatrix = new JPanel();
		TitledBorder titlesborder = BorderFactory.createTitledBorder("Matrice");
		jpanelMatrix.setBorder(titlesborder);

		JPanel jpanelResolution = new JPanel();
		TitledBorder panreso = BorderFactory.createTitledBorder("Résolution");
		jpanelResolution.setBorder(panreso);

		JPanel jpanelControl = new JPanel();

		labelName = new JLabel("Nom :");
		textFieldName = new JTextField(equation.getName());
		textFieldName.setMinimumSize(new Dimension(100, 5));

		labelNumberEquation = new JLabel("Nombre d'équations :");
		numberEquation = new JSpinner();
		numberEquation.setValue(equation.getMatrixNumberEquation());

		labelNumberVar = new JLabel("Nombre d'inconnues :");
		numberVar = new JSpinner();
		numberVar.setValue(equation.getMatrixNumberVariable());

		labelVarStyle = new JLabel("Style des variables :");
		varStyle = new JComboBox<String>();
		varStyle.addItem("x1, x2, x3, x4, ...");
		varStyle.addItem("a, b, c, d, e, ...");
		varStyle.addItem("x, y, z (max 3)");
		varStyle.setSelectedItem(0);

		methodStep = new JRadioButton("étape par étape");
		methodStep.setSelected(equation.isStepMode());
		methodDirect = new JRadioButton("directe");
		methodDirect.setSelected(!equation.isStepMode());
		ButtonGroup bg = new ButtonGroup();
		bg.add(methodStep);
		bg.add(methodDirect);

		labelSpeed = new JLabel("Vitesse (en seconde) :");
		spinSpeed = new JSpinner();
		spinSpeed.setValue(equation.getSpeedSec());

		nextButton = new JButton("Suivant");

			// Layout : Specification
			{
			setLayout(new BorderLayout());

			jpanelCenter.setLayout(new GridLayout(2, 0));

			jpanelMatrix.setLayout(new GridLayout(4, 2));
			jpanelResolution.setLayout(new GridLayout(2, 2));

			jpanelControl.setLayout(new FlowLayout(FlowLayout.CENTER));
			}

		// JComponent : add
		jpanelMatrix.add(labelName);
		jpanelMatrix.add(textFieldName);

		jpanelMatrix.add(labelNumberEquation);
		jpanelMatrix.add(numberEquation);

		jpanelMatrix.add(labelNumberVar);
		jpanelMatrix.add(numberVar);

		jpanelMatrix.add(labelVarStyle);
		jpanelMatrix.add(varStyle);

		jpanelResolution.add(methodStep);
		jpanelResolution.add(methodDirect);

		jpanelResolution.add(labelSpeed);
		jpanelResolution.add(spinSpeed);

		jpanelCenter.add(jpanelMatrix);
		jpanelCenter.add(jpanelResolution);

		jpanelControl.add(nextButton);

		add(jpanelCenter, BorderLayout.CENTER);
		add(jpanelControl, BorderLayout.SOUTH);
		}

	private void control()
		{
		nextButton.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					equation.setName(textFieldName.getText());
					equation.setSpeed((Integer)spinSpeed.getValue());

					int newNumberEqu = (Integer)numberEquation.getValue();
					int newNumberVar = (Integer)numberVar.getValue();

					if (controllerEquation.isCreating() || !equation.isSolved() || newNumberVar != equation.getMatrixNumberVariable() || newNumberEqu != equation.getMatrixNumberEquation())
						{
						//Changement de matrix
						equation.setNumberVar(newNumberVar);
						equation.setNumberEquation(newNumberEqu);
						equation.setModeStep(methodStep.isSelected());

						controllerEquation.setEquation(equation);
						controllerMain.showDialog(DIALOG.SET_MATRIX);
						}
					else if(equation.isStepMode() != methodStep.isSelected())
						{
						//Changement de mode
						equation.setModeStep(methodStep.isSelected());
						controllerEquation.reInitMatrix();//On reinitialise la matrix
						controllerEquation.solveEquation();//On recalcule avec l'autre méthode
						controllerMain.closeDialog();//On ferme aussi directement le dialogue
						}
					else
						{
						controllerEquation.setEquation(equation);
						controllerMain.closeDialog();//On ferme aussi directement le dialogue
						}
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
	Equation equation;

	private JLabel labelName, labelNumberEquation, labelNumberVar, labelVarStyle, labelSpeed;
	private JRadioButton methodStep, methodDirect;
	private JComboBox<String> varStyle;
	private JSpinner numberEquation, numberVar, spinSpeed;
	private JTextField textFieldName;
	private JButton nextButton;

	}
