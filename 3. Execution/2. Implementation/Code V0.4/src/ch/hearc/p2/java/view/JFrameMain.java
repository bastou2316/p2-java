
package ch.hearc.p2.java.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class JFrameMain extends JFrame
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JFrameMain()
		{
		super();

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

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	protected void geometry()
		{
		//Objects init

		//setMaximumSize(new Dimension(1900,680));
		setMinimumSize(new Dimension(300, 300));
		setResizable(true);

		// Layout : Specification
		BorderLayout borderLayout = new BorderLayout();
		setLayout(borderLayout);

		// JComponent : Instanciation
		//add(jPanelMenu, BorderLayout.CENTER);

		}

	protected void control()
		{
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		}

	protected void appearance()
		{
		setSize(600, 400);
		setLocationRelativeTo(null); // frame centrer
		setVisible(true); // last!
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	}
