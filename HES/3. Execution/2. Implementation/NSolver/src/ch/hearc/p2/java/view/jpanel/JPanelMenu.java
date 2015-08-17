
package ch.hearc.p2.java.view.jpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ch.hearc.p2.java.controller.ControllerMain;
import ch.hearc.p2.java.controller.ControllerMain.DIALOG;
import ch.hearc.p2.java.view.strings.Strings;

public class JPanelMenu extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelMenu(ControllerMain controllerMain)
		{
		this.controllerMain = controllerMain;

		geometry();
		control();
		appearance();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void geometry()
		{
//		// JComponent : Instanciation
//		labelWelcome = new JLabel(Strings.WELCOME);
//		labelNew = new JLabel(Strings.LABEL_NEW);
//		labelLoad = new JLabel(Strings.LABEL_LOAD);
//		buttonNew = new JButton(Strings.BUTTON_NEW);
//		buttonLoad = new JButton(Strings.BUTTON_LOAD);
//
//			// Layout : Specification
//			{
//			FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
//			setLayout(flowlayout);
//
//			// flowlayout.setHgap(20);
//			// flowlayout.setVgap(20);
//			}
//
//		// JComponent : add
//		add(labelWelcome);
//		add(labelNew);
//		add(buttonNew);
//		add(labelLoad);
//		add(buttonLoad);
		
		JPanel panelGeneral = new JPanel();
		add(panelGeneral, BorderLayout.CENTER);
		panelGeneral.setLayout(new BorderLayout(0, 0));

		JPanel panelToolBar = new JPanel();
		panelGeneral.add(panelToolBar, BorderLayout.NORTH);
		panelToolBar.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panelContent = new JPanel();
		panelGeneral.add(panelContent, BorderLayout.CENTER);
		panelContent.setLayout(new GridLayout(3, 0, 0, 0));

		JPanel panelWelcome = new JPanel();
		panelWelcome.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panelWelcome.setBackground(Color.WHITE);
		panelContent.add(panelWelcome);
		panelWelcome.setLayout(new GridLayout(4, 0, 0, 0));

		JLabel lblBonjour = new JLabel("Bienvenue sur NSolver !");
		lblBonjour.setForeground(Color.BLACK);
		lblBonjour.setFont(new Font("Courier New", Font.BOLD, 14));
		panelWelcome.add(lblBonjour);

		JLabel lblNewLabel = new JLabel("");
		panelWelcome.add(lblNewLabel);

		JLabel lblLblexplication = new JLabel("\tVous avez la possibilit\u00E9e de cr\u00E9er une nouvelle \u00E9quation.");
		lblLblexplication.setFont(new Font("Courier New", Font.PLAIN, 11));
		lblLblexplication.setForeground(Color.BLACK);
		panelWelcome.add(lblLblexplication);

		JLabel lblVousPouvezgallement = new JLabel("Vous pouvez \u00E9gallement charger une \u00E9quation de l'historique.");
		lblVousPouvezgallement.setFont(new Font("Courier New", Font.PLAIN, 11));
		lblVousPouvezgallement.setForeground(Color.BLACK);
		panelWelcome.add(lblVousPouvezgallement);

		JPanel panelNewEquation = new JPanel();
		panelNewEquation.setBorder(new TitledBorder(null, "Equation", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		panelContent.add(panelNewEquation);
		panelNewEquation.setLayout(new GridLayout(3, 0, 0, 0));

		JLabel lblEmpty = new JLabel("");
		panelNewEquation.add(lblEmpty);

		JPanel panelShortCreateEquation = new JPanel();
		panelNewEquation.add(panelShortCreateEquation);
		panelShortCreateEquation.setLayout(new BoxLayout(panelShortCreateEquation, BoxLayout.X_AXIS));

		JPanel panelLblNewEqua = new JPanel();
		panelShortCreateEquation.add(panelLblNewEqua);

		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		panelLblNewEqua.add(horizontalStrut_2);

		JLabel lblNouvellequation = new JLabel("Nouvelle \u00E9quation :");
		lblNouvellequation.setForeground(Color.BLACK);
		lblNouvellequation.setFont(new Font("Courier New", Font.PLAIN, 11));
		panelLblNewEqua.add(lblNouvellequation);

		JPanel panelCreateEqu = new JPanel();
		panelShortCreateEquation.add(panelCreateEqu);
		panelCreateEqu.setLayout(new BorderLayout(0, 0));

		JPanel panelCreate = new JPanel();
		panelCreateEqu.add(panelCreate, BorderLayout.WEST);
		panelCreate.setLayout(new BorderLayout(0, 0));

		buttonNew = new JButton("Cr\u00E9er");
		panelCreate.add(buttonNew);
		buttonNew.setFont(new Font("Courier New", Font.PLAIN, 11));
		buttonNew.setForeground(Color.BLACK);

		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		panelShortCreateEquation.add(horizontalStrut_3);

		JPanel panelHistorique = new JPanel();
		panelHistorique.setForeground(Color.BLACK);
		panelHistorique.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Logs", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		panelContent.add(panelHistorique);
		panelHistorique.setLayout(new BoxLayout(panelHistorique, BoxLayout.X_AXIS));

		JPanel panelLblHistorique = new JPanel();
		panelHistorique.add(panelLblHistorique);
		panelLblHistorique.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		panelLblHistorique.add(horizontalStrut_1);

		JLabel lblHistorique = new JLabel("Historique :");
		lblHistorique.setForeground(Color.BLACK);
		lblHistorique.setFont(new Font("Courier New", Font.PLAIN, 11));
		panelLblHistorique.add(lblHistorique);

		JPanel panelCbHistorique = new JPanel();
		panelHistorique.add(panelCbHistorique);
		panelCbHistorique.setLayout(new BorderLayout(0, 0));

		JComboBox comboBox = new JComboBox();
		panelCbHistorique.add(comboBox, BorderLayout.NORTH);

		JPanel panelLoad = new JPanel();
		panelCbHistorique.add(panelLoad, BorderLayout.SOUTH);
		panelLoad.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		buttonLoad = new JButton("Charger");
		buttonLoad.setFont(new Font("Courier New", Font.PLAIN, 11));
		panelLoad.add(buttonLoad);
		

		Component horizontalStrut = Box.createHorizontalStrut(20);
		panelHistorique.add(horizontalStrut);

		Component horizontalGlue = Box.createHorizontalGlue();
		panelHistorique.add(horizontalGlue);
			add(panelGeneral);

		}

	private void control()
		{
		buttonNew.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					controllerMain.showDialog(DIALOG.SET_EQUATION);
					}
			});

		buttonLoad.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					controllerMain.load();
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
	ControllerMain controllerMain;

	// Tools
	JLabel labelWelcome;
	JLabel labelNew;
	JLabel labelLoad;
	JButton buttonNew;
	JButton buttonLoad;
	}
