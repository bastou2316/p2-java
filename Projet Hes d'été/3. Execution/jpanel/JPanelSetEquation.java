
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


		JPanel jpanelControl = new JPanel();

		labelName = new JLabel("Nom : ");
		textFieldName = new JTextField(equation.getName());
		textFieldName.setMinimumSize(new Dimension(100, 5));

		labelNumberEquation = new JLabel("Nombre d'équations : ");
		numberEquation = new JSpinner();
		numberEquation.setValue(equation.getMatrixNumberEquation());

		labelNumberVar = new JLabel("Nombre d'inconnues : ");
		numberVar = new JSpinner();
		numberVar.setValue(equation.getMatrixNumberVariable());

		labelVarStyle = new JLabel("Style des variables : ");
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

		labelSpeed = new JLabel("Vitesse (en seconde) : ");
		spinSpeed = new JSpinner();
		spinSpeed.setValue(equation.getSpeedSec());

		nextButton = new JButton("Suivant");

			// Layout : Specification
			{
			setLayout(new BorderLayout());

			jpanelCenter.setLayout(new GridLayout(2, 0));

			jpanelMatrix.setLayout(new BorderLayout());
			jpanelResolution.setLayout(new GridLayout(1, 2));

			jpanelControl.setLayout(new FlowLayout(FlowLayout.CENTER));
			}

		// JComponent : add
			
		JPanel jpanelGauche = new JPanel();
		JPanel jpanelDroite = new JPanel();
		jpanelGauche.setLayout(new GridLayout(4, 1));
		jpanelDroite.setLayout(new GridLayout(4, 1));
			
//		JPanel jPanelName = new JPanel();
//		jPanelName.setLayout(new BorderLayout());
//		jPanelName.add(labelName, BorderLayout.WEST);
//		jPanelName.add(textFieldName,BorderLayout.CENTER);
//		jpanelMatrix.add(jPanelName);
		
		jpanelGauche.add(labelName);
		jpanelDroite.add(textFieldName);
		
		
//		JPanel jPanelNumberEquation = new JPanel();
//		jPanelNumberEquation.setLayout(new BorderLayout());
//		jPanelNumberEquation.add(labelNumberEquation, BorderLayout.WEST);
//		jPanelNumberEquation.add(numberEquation,BorderLayout.CENTER);
//		jpanelMatrix.add(jPanelNumberEquation);

		jpanelGauche.add(labelNumberEquation);
		jpanelDroite.add(numberEquation);
		
		
//		JPanel jPanelNumberVar = new JPanel();
//		jPanelNumberVar.setLayout(new BorderLayout());
//		jPanelNumberVar.add(labelNumberVar, BorderLayout.WEST);
//		jPanelNumberVar.add(numberVar,BorderLayout.CENTER);
//		jpanelMatrix.add(jPanelNumberVar);

		jpanelGauche.add(labelNumberVar);
		jpanelDroite.add(numberVar);
		
		
		
		
//		JPanel jPanelVarStyle = new JPanel();
//		jPanelVarStyle.setLayout(new BorderLayout());
//		jPanelVarStyle.add(labelVarStyle, BorderLayout.WEST);
//		jPanelVarStyle.add(varStyle,BorderLayout.CENTER);
//		jpanelMatrix.add(jPanelVarStyle);
		
		jpanelGauche.add(labelVarStyle);
		jpanelDroite.add(varStyle);

		
		jpanelMatrix.add(jpanelGauche,BorderLayout.WEST);
		jpanelMatrix.add(jpanelDroite,BorderLayout.CENTER);
		
		jpanelResolution.add(methodStep);
		jpanelResolution.add(methodDirect);
		
		JPanel jpanelResolution2 = new JPanel();
		jpanelResolution2.setLayout(new BorderLayout());

		jpanelResolution2.add(labelSpeed,BorderLayout.WEST);
		jpanelResolution2.add(spinSpeed,BorderLayout.CENTER);
		
		
		JPanel jpanelResoFinal = new JPanel();
		jpanelResoFinal.setLayout(new GridLayout(2,1));
		jpanelResoFinal.add(jpanelResolution);
		jpanelResoFinal.add(jpanelResolution2);
		TitledBorder panreso = BorderFactory.createTitledBorder("Résolution");
		jpanelResoFinal.setBorder(panreso);
		

		jpanelCenter.add(jpanelMatrix);
		jpanelCenter.add(jpanelResoFinal);

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
