
package ch.hearc.p2.java.view.jpanel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import ch.hearc.p2.java.controller.ControllerEquation;
import ch.hearc.p2.java.controller.ControllerMain;
import ch.hearc.p2.java.controller.ControllerMain.DIALOG;
import ch.hearc.p2.java.model.Equation;
import ch.hearc.p2.java.model.Matrix;

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
		Equation equation;

//		if (controllerEquation.getEquation == null || controllerEquation.isCreating())
//			{
//			equation = new Equation();
//			}
//		else
//			{
			equation = controllerEquation.getEquation();
//			}

		// JComponent : Instanciation
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

		nextButton = new JButton("Suivant");

			// Layout : Specification
			{
			FlowLayout flowlayout = new FlowLayout(FlowLayout.LEFT);
			setLayout(flowlayout);
			}

		// JComponent : add
		add(labelName);
		add(textFieldName);

		add(labelNumberEquation);
		add(numberEquation);

		add(labelNumberVar);
		add(numberVar);

		add(labelVarStyle);
		add(varStyle);

		add(methodStep);
		add(methodDirect);

		add(nextButton);
		}

	private void control()
		{
		nextButton.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					//todo : setEquationTemp
					Equation equation = new Equation(textFieldName.getText(), (Integer)numberVar.getValue(), (Integer)numberEquation.getValue(), 1000, methodStep.isSelected());
					equation.setMatrix(new Matrix((Integer)numberVar.getValue(), (Integer)numberEquation.getValue()));
					controllerEquation.setEquation(equation);
					controllerMain.showDialog(DIALOG.SET_MATRIX);
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
	private JLabel labelName, labelNumberEquation, labelNumberVar, labelVarStyle;
	private JRadioButton methodStep, methodDirect;
	private JComboBox<String> varStyle;
	private JSpinner numberEquation, numberVar;
	private JTextField textFieldName;
	private JButton nextButton;

	}
