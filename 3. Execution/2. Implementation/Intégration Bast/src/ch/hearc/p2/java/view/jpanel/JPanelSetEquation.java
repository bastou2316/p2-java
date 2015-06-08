
package ch.hearc.p2.java.view.jpanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

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

		
		panMatrice = new JPanel();
		panMatrice.setLayout(new GridLayout(4, 2));
		//panMatrice.setBackground(Color.white);
		TitledBorder titlesborder = BorderFactory.createTitledBorder("Matrice");
		titlesborder.setTitleColor(Color.blue);


		panMatrice.setBorder(titlesborder);
		panMatrice.setForeground(Color.blue);

		textFieldName = new JTextField();
		labelName = new JLabel("Nom :");
		//nameLabel.setForeground(Color.blue);
		panMatrice.add(labelName);
		panMatrice.add(textFieldName);

		numberEquation = new JSpinner();
		numberEquation.setValue(3);
		labelNumberEquation = new JLabel("Nombre d'équations :");

		//equationNumber.setForeground(Color.blue);
	//	equationNumberLabel.setForeground(Color.blue);
		panMatrice.add(labelNumberEquation);
		panMatrice.add(numberEquation);

		numberVar = new JSpinner();
		numberVar.setValue(3);
		labelNumberVar = new JLabel("Nombre d'inconnues :");
		//varNumberLabel.setForeground(Color.blue);
		//varNumber.setForeground(Color.blue);
		numberVar.setBackground(Color.LIGHT_GRAY);
		panMatrice.add(labelNumberVar);
		panMatrice.add(numberVar);

		varStyle = new JComboBox<String>();
		varStyle.setForeground(Color.BLACK);
		varStyle.addItem("x1, x2, x3, x4, ...");
		varStyle.addItem("a, b, c, d, e, ...");
		varStyle.addItem("x, y, z (max 3)");
		labelVarStyle = new JLabel("Style des variables :");
		//varStyleLabel.setForeground(Color.blue);
		//varStyle.setForeground(Color.blue);
		varStyle.setBackground(Color.white);
		panMatrice.add(labelVarStyle);
		panMatrice.add(varStyle);

		// Résolution
		JPanel panReso = new JPanel();
		//panReso.setBackground(Color.LIGHT_GRAY);

		TitledBorder panreso = BorderFactory.createTitledBorder("Résolution");
		panreso.setTitleColor(Color.blue);
		panReso.setBorder(panreso);

		methodStep = new JRadioButton("\u00E9tape par \u00E9tape, vitesse : ");
		methodStep.setSelected(true);
		methodDirect = new JRadioButton("directe");
		ButtonGroup bg = new ButtonGroup();
		//methodStep.setForeground(Color.blue);
		//methodDirect.setForeground(Color.blue);
		bg.add(methodStep);
		bg.add(methodDirect);
		panReso.setLayout(new GridLayout(2,2));
		panReso.add(methodStep);

		txVitesse = new JTextField();
		panReso.add(txVitesse);
		txVitesse.setColumns(10);
		panReso.add(methodDirect);

		JPanel panControl = new JPanel(new FlowLayout());
		panControl.setBackground(UIManager.getColor("Button.background"));

		backButton = new JButton("Précédent");
		backButton.setForeground(Color.BLACK);
		nextButton = new JButton("Générer");
		nextButton.setForeground(Color.BLACK);

		panControl.add(backButton);
		panControl.add(nextButton);

		Box controlBox = Box.createVerticalBox();

		controlBox.add(Box.createVerticalGlue());
		controlBox.add(panControl);
		controlBox.add(Box.createVerticalGlue());

		leftContainerBox = Box.createVerticalBox();

		leftContainerBox.add(Box.createVerticalGlue());
		leftContainerBox.add(Box.createVerticalGlue());
		leftContainerBox.add(panMatrice);
		leftContainerBox.add(Box.createVerticalGlue());
		leftContainerBox.add(panReso);
		leftContainerBox.add(Box.createVerticalGlue());
		leftContainerBox.add(controlBox);
		leftContainerBox.add(Box.createVerticalGlue());
		leftContainerBox.add(Box.createVerticalGlue());


		add(leftContainerBox);
		}

	private void control()
		{
		nextButton.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					//Settage de la l'equation courante
					equation.setName(textFieldName.getText());
					equation.setNumberVar((Integer)numberVar.getValue());
					equation.setNumberEquation((Integer)numberEquation.getValue());
					equation.setSpeed(1000);
					equation.setModeStep(methodStep.isSelected());
					equation.setMatrix(new Matrix((Integer)numberVar.getValue(), (Integer)numberEquation.getValue()));

					controllerEquation.setEquation(equation);
					controllerMain.showDialog(DIALOG.SET_MATRIX);
					}
			});
		}

	private void appearance()
		{
		//RIEN
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private ControllerMain controllerMain;
	private ControllerEquation controllerEquation;

	// Tools
	Equation equation;

	
	
	
	private JLabel labelName, labelNumberEquation, labelNumberVar, labelVarStyle;
	private JRadioButton methodStep, methodDirect;
	private JComboBox<String> varStyle;
	private JSpinner numberEquation, numberVar;
	private JTextField textFieldName,txVitesse;
	private JButton nextButton,  backButton;
	private Box leftContainerBox;
	private JPanel panMatrice;

	}
