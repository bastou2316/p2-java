
package ch.hearc.p2.java.view.jpanel.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.hearc.p2.java.model.Equation;
import ch.hearc.p2.java.view.dialog.JDialogSetEquation;

public class JPanelSetEquation extends JPanelDialog
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelSetEquation(Equation equation, int numbVar, int numbEqu)
		{
		super();

		this.equation = equation;
		this.numbVar = numbVar;
		this.numbEqu = numbEqu;

		// Composition du panel
		geometry();
		control();
		appearance();

		//Update du bouton
		updateButtonText();
		}

	public JPanelSetEquation(Equation equation)
		{
		this(equation, equation.getMatrixNumberVariable(), equation.getMatrixNumberEquation());
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/
	 private void changeFont(Component component, int fontSize) {
	    Font f = component.getFont();
	    component.setFont(new Font(f.getName(),f.getStyle(),f.getSize() + fontSize));
	    if (component instanceof Container) {
	        for (Component child : ((Container) component).getComponents()) {
	            changeFont(child, fontSize);
	        }
	    }
	}

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
		spinNumbEqu = new JSpinner();
		spinNumbEqu.setValue(equation.getMatrixNumberEquation());

		labelNumberVar = new JLabel("Nombre d'inconnues : ");
		spinNumbVar = new JSpinner();
		spinNumbVar.setValue(equation.getMatrixNumberVariable());

		labelVarStyle = new JLabel("Style des variables : ");
		comboVarStyle = new JComboBox<String>();
		comboVarStyle.addItem("x, y, z (max 3)");
		comboVarStyle.addItem("a, b, c, d, e, ...");
		comboVarStyle.addItem("x1, x2, x3, x4, ...");
		comboVarStyle.setSelectedIndex(equation.getVariableStyle());

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

		jpanelGauche.add(labelName);
		jpanelDroite.add(textFieldName);

		jpanelGauche.add(labelNumberEquation);
		jpanelDroite.add(spinNumbEqu);

		jpanelGauche.add(labelNumberVar);
		jpanelDroite.add(spinNumbVar);

		jpanelGauche.add(labelVarStyle);
		jpanelDroite.add(comboVarStyle);

		jpanelMatrix.add(jpanelGauche, BorderLayout.WEST);
		jpanelMatrix.add(jpanelDroite, BorderLayout.CENTER);

		jpanelResolution.add(methodStep);
		jpanelResolution.add(methodDirect);

		JPanel jpanelResolution2 = new JPanel();
		jpanelResolution2.setLayout(new BorderLayout());

		jpanelResolution2.add(labelSpeed, BorderLayout.WEST);
		jpanelResolution2.add(spinSpeed, BorderLayout.CENTER);

		JPanel jpanelResoFinal = new JPanel();
		jpanelResoFinal.setLayout(new GridLayout(2, 1));
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
		spinNumbVar.addChangeListener(new ChangeListener()
			{

				@Override
				public void stateChanged(ChangeEvent e)
					{
					updateButtonText();
					}
			});

		spinNumbEqu.addChangeListener(new ChangeListener()
			{

				@Override
				public void stateChanged(ChangeEvent e)
					{
					updateButtonText();
					}
			});

		nextButton.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					equation.setName(textFieldName.getText());
					equation.setSpeed((Integer)spinSpeed.getValue());
					equation.setNumberVar((Integer)spinNumbVar.getValue());
					equation.setNumberEquation((Integer)spinNumbEqu.getValue());
					equation.setModeStep(methodStep.isSelected());
					equation.setVarNameMethod(comboVarStyle.getSelectedIndex());

					choice = 1;

					JDialogSetEquation jdialog = (JDialogSetEquation)getRootPane().getParent();
					jdialog.close();
					}
			});

		this.addMouseWheelListener(new MouseWheelListener()
			{

				@Override
				public void mouseWheelMoved(MouseWheelEvent e)
					{
					// TODO Auto-generated method stub
					changeFont(JPanelSetEquation.this, e.getWheelRotation());

					}
			});
		}

	private void appearance()
		{
		// rien
		}

	private void updateButtonText()
		{
		//Adapte le texte du bouton en fonction du changement de matrice nécessaire
		if (equation.isSolved())
			{
			String newText;
			if (numbVar != (Integer)spinNumbVar.getValue() || numbEqu != (Integer)spinNumbEqu.getValue())//if (equation.getMatrixNumberEquation() != (Integer)spinNumbVar.getValue() || equation.getMatrixNumberVariable() != (Integer)spinNumbEqu.getValue())
				{
				newText = "Suivant";
				}
			else
				{
				newText = "Confirmer";
				}
			nextButton.setText(newText);
			}
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private Equation equation;
	private int numbVar;
	private int numbEqu;

	// Tools
	private JLabel labelName, labelNumberEquation, labelNumberVar, labelVarStyle, labelSpeed;
	private JRadioButton methodStep, methodDirect;
	private JComboBox<String> comboVarStyle;
	private JSpinner spinNumbEqu, spinNumbVar, spinSpeed;
	private JTextField textFieldName;
	private JButton nextButton;

	}
