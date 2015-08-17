
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
		
		JLabel lblLblexplication = new JLabel("  Vous avez la possibilit\u00E9e de cr\u00E9er une nouvelle \u00E9quation.");
		lblLblexplication.setFont(new Font("Courier New", Font.PLAIN, 11));
		lblLblexplication.setForeground(Color.BLACK);
		panelWelcome.add(lblLblexplication);
		
		JLabel lblVousPouvezgallement = new JLabel("  Vous pouvez \u00E9gallement charger une ancienne sauvegarde.");
		lblVousPouvezgallement.setFont(new Font("Courier New", Font.PLAIN, 11));
		lblVousPouvezgallement.setForeground(Color.BLACK);
		panelWelcome.add(lblVousPouvezgallement);
		
		//
		JPanel panelNewE = new JPanel();
		panelNewE.setForeground(Color.BLACK);
		panelNewE.setBorder(new TitledBorder(null, "Equation", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelContent.add(panelNewE);
		panelNewE.setLayout(new BoxLayout(panelNewE, BoxLayout.X_AXIS));
		
		JLabel labelv = new JLabel("");
		panelNewE.add(labelv);
		
		JPanel panelShortE = new JPanel();
		panelNewE.add(panelShortE);
		
		JPanel panelLblNewE = new JPanel();
		panelShortE.add(panelLblNewE);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		panelLblNewE.add(horizontalStrut);
		
		JLabel lblLoad = new JLabel("Nouvelle \u00E9quation");
		lblLoad.setForeground(Color.BLACK);
		lblLoad.setFont(new Font("Courier New", Font.PLAIN, 11));
		panelLblNewE.add(lblLoad);
		
		JPanel panelCreateE = new JPanel();
		panelShortE.add(panelCreateE);
		panelCreateE.setLayout(new BorderLayout(0, 0));
		
		JPanel panelE = new JPanel();
		panelCreateE.add(panelE, BorderLayout.WEST);
		panelE.setLayout(new BorderLayout(0, 0));
		
		buttonNew = new JButton("Cr\u00E9er");
		buttonNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buttonNew.setForeground(Color.BLACK);
		buttonNew.setFont(new Font("Courier New", Font.PLAIN, 11));
		panelE.add(buttonNew, BorderLayout.NORTH);
		//
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		panelShortE.add(horizontalStrut_3);
		
		JPanel panelHistorique = new JPanel();
		panelHistorique.setForeground(Color.BLACK);
		panelHistorique.setBorder(new TitledBorder(null, "Fichier", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelContent.add(panelHistorique);
		panelHistorique.setLayout(new BoxLayout(panelHistorique, BoxLayout.X_AXIS));
		
		JLabel label = new JLabel("");
		panelHistorique.add(label);
		
		JPanel panelShortFile = new JPanel();
		panelHistorique.add(panelShortFile);
		
		JPanel panelLblNewFile = new JPanel();
		panelShortFile.add(panelLblNewFile);
		
		Component horizontalStrut1 = Box.createHorizontalStrut(20);
		panelLblNewFile.add(horizontalStrut1);
		
		JLabel lblParcourir = new JLabel("Charger un fichier :");
		lblParcourir.setForeground(Color.BLACK);
		lblParcourir.setFont(new Font("Courier New", Font.PLAIN, 11));
		panelLblNewFile.add(lblParcourir);
		
		JPanel panelCreateFIle = new JPanel();
		panelShortFile.add(panelCreateFIle);
		panelCreateFIle.setLayout(new BorderLayout(0, 0));
		
		JPanel panelFile = new JPanel();
		panelCreateFIle.add(panelFile, BorderLayout.WEST);
		panelFile.setLayout(new BorderLayout(0, 0));
		

		buttonLoad = new JButton("Parcourir");
		buttonLoad.setForeground(Color.BLACK);
		buttonLoad.setFont(new Font("Courier New", Font.PLAIN, 11));
		panelFile.add(buttonLoad, BorderLayout.NORTH);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		panelShortFile.add(horizontalStrut_1);

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
