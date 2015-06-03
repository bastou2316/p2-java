
package ch.hearc.coursjava.gui.p2.menu;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;



public class JFrameMenu extends JFrame
	{

	public JFrameMenu()
	{
	geometry();
	control();
	apparance();
	}

	private void apparance()
		{
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
		panMatrice = new JPanel();
		panMatrice.setLayout(new GridLayout(2, 2));
		//panMatrice.setBackground(Color.white);
		TitledBorder titlesborder = BorderFactory.createTitledBorder("Menu principal");
		titlesborder.setTitleColor(Color.blue);


		panMatrice.setBorder(titlesborder);
		panMatrice.setForeground(Color.blue);

		btnNewMatrix = new JButton("Nouveau");
		btnNewMatrix.setForeground(Color.BLACK);

		name = new JTextField();
		lblNewMatrix = new JLabel("Créer une nouvelle matrice :");
		lblNewMatrix.setForeground(Color.BLACK);
		panMatrice.add(lblNewMatrix);
		panMatrice.add(btnNewMatrix);



		// Résolution
		JPanel panReso = new JPanel();
		//panReso.setBackground(Color.LIGHT_GRAY);

		TitledBorder panNew = BorderFactory.createTitledBorder("Logs / Historiques");
		panNew.setTitleColor(Color.blue);
		panReso.setBorder(panNew);


		//methodStep.setForeground(Color.blue);
		//methodDirect.setForeground(Color.blue);
		panReso.setLayout(new GridLayout(2,2));

		//txVitesse = new JTextField();

		list = new JList();
		list.setSize(400, 400);

		//txVitesse.setColumns(10);

		JPanel panCharger = new JPanel(new FlowLayout());
		panCharger.setBackground(UIManager.getColor("Button.background"));

		btnCharger = new JButton("Charger");
		btnCharger.setForeground(Color.BLACK);


		panCharger.add(btnCharger);

		Box controlBox = Box.createVerticalBox();

		controlBox.add(Box.createVerticalGlue());
		controlBox.add(panCharger);

		leftContainerBox = Box.createVerticalBox();

		leftContainerBox.add(Box.createVerticalGlue());
		leftContainerBox.add(Box.createVerticalGlue());
		leftContainerBox.add(panMatrice);
		leftContainerBox.add(Box.createVerticalGlue());
		leftContainerBox.add(panReso);

		comboBox = new JComboBox();
		//Historique ici


		panReso.add(comboBox);
		leftContainerBox.add(Box.createVerticalGlue());
		leftContainerBox.add(controlBox);
		leftContainerBox.add(Box.createVerticalGlue());
		getContentPane().add(leftContainerBox);

		}


	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	private JLabel lblNewMatrix, equationNumberLabel, varNumberLabel, varStyleLabel;
	private JComboBox<String> varStyle;
	private JSpinner equationNumber, varNumber;
	private JTextField name;
	private JButton   btnNewMatrix,btnCharger;
	private Box leftContainerBox;
	private JPanel panMatrice;
	private JList list;
	private JComboBox comboBox;



	}
