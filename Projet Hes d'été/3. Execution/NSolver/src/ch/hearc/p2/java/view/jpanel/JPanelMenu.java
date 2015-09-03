
package ch.hearc.p2.java.view.jpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ch.hearc.p2.java.controller.ControllerMain;
import ch.hearc.p2.java.controller.ControllerMain.DIALOG;

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
		changeFont(this, 1);

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
		setLayout(new BorderLayout());
		JPanel panelContent = new JPanel();

		JPanel panelWelcome = new JPanel();
		panelWelcome.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panelWelcome.setBackground(Color.WHITE);
		JLabel lblBonjour = new JLabel("Bienvenue sur NSolver !");
		lblBonjour.setForeground(Color.BLACK);
		lblBonjour.setFont(new Font("Courier New", Font.BOLD, 14));
		JLabel lblexplication = new JLabel("\t Vous avez la possibilit\u00E9e de cr\u00E9er une nouvelle \u00E9quation.");
		lblexplication.setFont(new Font("Courier New", Font.PLAIN, 11));
		lblexplication.setForeground(Color.BLACK);
		JLabel lblexplication2 = new JLabel("\t Vous pouvez \u00E9gallement charger une \u00E9quation de l'historique.");
		lblexplication2.setFont(new Font("Courier New", Font.PLAIN, 11));
		lblexplication2.setForeground(Color.BLACK);
		Box boxExplain = Box.createVerticalBox();
		boxExplain.add(lblBonjour);
		boxExplain.add(new JLabel("\n"));
		boxExplain.add(lblexplication);
		boxExplain.add(lblexplication2);
		panelWelcome.add(boxExplain);

		JPanel panelHistory = new JPanel();
		JLabel lblNouvellequation = new JLabel("Nouvelle \u00E9quation :");
		lblNouvellequation.setForeground(Color.BLACK);
		lblNouvellequation.setFont(new Font("Courier New", Font.PLAIN, 11));

		panelHistory.setForeground(Color.BLACK);
		TitledBorder border = new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Logs", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK);
		//		border.setTitleFont( border.getTitleFont().deriveFont(Font.BOLD + Font.ITALIC) );
		panelHistory.setBorder(border);

		buttonNew = new JButton("Cr\u00E9er");
		buttonNew.setFont(new Font("Courier New", Font.PLAIN, 11));
		buttonNew.setForeground(Color.BLACK);

		panelHistory.add(lblNouvellequation);
		panelHistory.add(buttonNew);

		JPanel panelFile = new JPanel();

		JLabel lblNewFile = new JLabel("Charger un fichier :");
		lblNewFile.setForeground(Color.BLACK);
		lblNewFile.setFont(new Font("Courier New", Font.PLAIN, 11));

		buttonLoad = new JButton("Parcourir");
		buttonLoad.setFont(new Font("Courier New", Font.PLAIN, 11));
		buttonLoad.setForeground(Color.BLACK);

		panelFile.add(lblNewFile);
		panelFile.add(buttonLoad);

		panelFile.setForeground(Color.BLACK);
		panelFile.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Fichier", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));

		Box boxV = Box.createVerticalBox();
		boxV.add(panelWelcome);
		boxV.add(panelFile);
		boxV.add(panelHistory);
		panelContent.add(boxV);
		add(boxV, BorderLayout.CENTER);
		}

	private void control()
		{
		buttonNew.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					controllerMain.showDialog(DIALOG.NEW_EQUATION);
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

		this.addMouseWheelListener(new MouseWheelListener()
			{

				@Override
				public void mouseWheelMoved(MouseWheelEvent e)
					{
					// TODO Auto-generated method stub
					changeFont(JPanelMenu.this, e.getWheelRotation());

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
