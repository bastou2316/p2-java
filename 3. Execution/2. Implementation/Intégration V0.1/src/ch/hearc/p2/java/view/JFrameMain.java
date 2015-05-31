
package ch.hearc.p2.java.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ch.hearc.p2.java.controller.ControllerMain;
import ch.hearc.p2.java.controller.ControllerMain.DIALOG;

public class JFrameMain extends JFrame
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JFrameMain(ControllerMain controllerMain)
		{
		super();

		this.controllerMain = controllerMain;

		geometry();
		control();
		appearance();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	@Override
	public void paint(Graphics g)
		{
		Dimension d = getSize();
		Dimension m = getMaximumSize();
		boolean resize = d.width > m.width || d.height > m.height;
		d.width = Math.min(m.width, d.width);
		d.height = Math.min(m.height, d.height);

		if (resize)
			{
			Point p = getLocation();
			setVisible(false);
			setSize(d);
			setLocation(p);
			setVisible(true);
			}
		super.paint(g);
		}

	public void enableAlternativeMenu(boolean enabled)
		{
		itemSave.setEnabled(enabled);
		menuOption.setEnabled(enabled);
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	public void setPanel(String title, JPanel jpanel)
		{
		setPanel(title, jpanel, 600, 400);
		}

	public void setPanel(String title, JPanel jpanel, int sizeX, int sizeY)
		{
		setTitle(title);
		setSize(sizeX, sizeY);
		setLocationRelativeTo(null);

		setContentPane(jpanel);

		revalidate();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	protected void geometry()
		{
		initMenu();

		//setMaximumSize(new Dimension(1900,680));
		setMinimumSize(new Dimension(300, 300));
		setResizable(true);

		// Layout : Specification
		BorderLayout borderLayout = new BorderLayout();
		setLayout(borderLayout);
		}

	protected void control()
		{
		itemNew.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					controllerMain.newEquation();
					}
			});

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		itemLoad.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					controllerMain.load();
					}
			});


		itemSave.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					controllerMain.save();
					}
			});

		itemSetEquation.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					controllerMain.showDialog(DIALOG.SET_EQUATION);
					}
			});

		itemHelp.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					JOptionPane.showMessageDialog(JFrameMain.this, "Explications : en image");
					}
			});
		}

	protected void appearance()
		{
		//setSize(600, 400);
		setLocationRelativeTo(null);
		setVisible(true);
		}

	private void initMenu()
		{
		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("Fichier");

		itemNew = menu.add(new JMenuItem("Nouveau", KeyEvent.VK_N));//verif syntaxe
		itemLoad = menu.add(new JMenuItem("Charger", KeyEvent.VK_C));
		itemSave = menu.add(new JMenuItem("Sauver", KeyEvent.VK_S));
		itemSave.setEnabled(false);

		menuBar.add(menu);

		menuOption = new JMenu("Options");
		menuOption.setEnabled(false);

		itemSetEquation = menuOption.add(new JMenuItem("Modifier l'équation"));
		itemSetMatrix = menuOption.add(new JMenuItem("Modifier la matrice"));

		menuBar.add(menuOption);

		menu = new JMenu("Aide");

		itemHelp = menu.add("Comment utiliser NSolver");

		menuBar.add(menu);

		setJMenuBar(menuBar);
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/
	//Input
	ControllerMain controllerMain;

	//Tools
	JMenu menuOption;
	JMenuItem itemNew;
	JMenuItem itemSave;
	JMenuItem itemLoad;
	JMenuItem itemSetEquation;
	JMenuItem itemSetMatrix;
	JMenuItem itemHelp;

	}
