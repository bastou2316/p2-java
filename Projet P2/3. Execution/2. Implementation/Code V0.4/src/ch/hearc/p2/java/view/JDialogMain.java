
package ch.hearc.p2.java.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class JDialogMain extends JDialog
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JDialogMain()
		{
		super();

		geometry();
		control();
		appearance();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	public void setPanelAndShow(String title, JPanel jpanel)
		{
		setPanelAndShow(title, jpanel, 600, 400);
		}

	public void setPanelAndShow(String title, JPanel jpanel, int sizeX, int sizeY)
		{
		setTitle(title);
		setSize(sizeX, sizeY);
		setLocationRelativeTo(null);

		setContentPane(jpanel);

		revalidate();
		setVisible(true);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void geometry()
		{
		setModal(true);

		setMinimumSize(new Dimension(300, 300));
		setResizable(true);

		// Layout : Specification
		BorderLayout borderLayout = new BorderLayout();
		setLayout(borderLayout);
		}

	private void control()
		{
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		}

	private void appearance()
		{
		//setSize(600, 400);
		setLocationRelativeTo(null);
		}
	}
