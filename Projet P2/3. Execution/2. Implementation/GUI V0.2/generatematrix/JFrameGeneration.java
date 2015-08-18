
package ch.hearc.coursjava.gui.p2.generatematrix;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class JFrameGeneration extends JFrame
	{

	public JFrameGeneration()
		{
		geometry();
		control();
		apparance();
		}

	private void apparance()
		{
		// TODO Auto-generated method stub
		setLocation(200, 200);
		setResizable(true);
		setSize(500, 400);
		setVisible(true);

		}

	private void control()
		{
		// TODO Auto-generated method stub

		}

	private void geometry()
		{
		// TODO Auto-generated method stub
		panMatrice = new JPanel();
		panMatrice.setLayout(new GridLayout(4, 2));
		//panMatrice.setBackground(Color.white);
		TitledBorder titlesborder = BorderFactory.createTitledBorder("Matrice");
		titlesborder.setTitleColor(Color.blue);


		panMatrice.setBorder(titlesborder);
		panMatrice.setForeground(Color.blue);

		name = new JTextField();
		nameLabel = new JLabel("Nom :");
		//nameLabel.setForeground(Color.blue);
		panMatrice.add(nameLabel);
		panMatrice.add(name);

		equationNumber = new JSpinner();
		equationNumber.setValue(3);
		equationNumberLabel = new JLabel("Nombre d'équations :");

		//equationNumber.setForeground(Color.blue);
	//	equationNumberLabel.setForeground(Color.blue);
		panMatrice.add(equationNumberLabel);
		panMatrice.add(equationNumber);

		varNumber = new JSpinner();
		varNumber.setValue(3);
		varNumberLabel = new JLabel("Nombre d'inconnues :");
		//varNumberLabel.setForeground(Color.blue);
		//varNumber.setForeground(Color.blue);
		varNumber.setBackground(Color.LIGHT_GRAY);
		panMatrice.add(varNumberLabel);
		panMatrice.add(varNumber);

		varStyle = new JComboBox<String>();
		varStyle.setForeground(Color.BLACK);
		varStyle.addItem("x1, x2, x3, x4, ...");
		varStyle.addItem("a, b, c, d, e, ...");
		varStyle.addItem("x, y, z (max 3)");
		varStyleLabel = new JLabel("Style des variables :");
		//varStyleLabel.setForeground(Color.blue);
		//varStyle.setForeground(Color.blue);
		varStyle.setBackground(Color.white);
		panMatrice.add(varStyleLabel);
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
		okBouton = new JButton("Générer");
		okBouton.setForeground(Color.BLACK);

		panControl.add(backButton);
		panControl.add(okBouton);

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


		getContentPane().add(leftContainerBox);

		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	private JLabel nameLabel, equationNumberLabel, varNumberLabel, varStyleLabel;
	private JRadioButton methodStep, methodDirect;
	private JComboBox<String> varStyle;
	private JSpinner equationNumber, varNumber;
	private JTextField name;
	private JButton okBouton,  backButton;
	private Box leftContainerBox;
	private JPanel panMatrice;
	private JTextField txVitesse;

	}
