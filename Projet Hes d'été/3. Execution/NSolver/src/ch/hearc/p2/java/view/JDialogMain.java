
package ch.hearc.p2.java.view;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import ch.hearc.p2.java.view.jpanel.dialog.JPanelDialog;

public class JDialogMain extends JDialog
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JDialogMain(JPanelDialog jPanel)
		{
		super();

		this.jPanel = jPanel;

		geometry();
		control();
		appearance();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public int showDialog()
		{
		setVisible(true);
		return jPanel.getChoice();
		}

	public void close()
		{
		setVisible(false);
		dispose();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	protected void geometry()
		{
		setModal(true);

		//setMinimumSize(new Dimension(300, 300));
		//setResizable(true);

		BorderLayout borderLayout = new BorderLayout();
		setLayout(borderLayout);

		add(jPanel);
		}

	protected void control()
		{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		}

	protected void appearance()
		{
		setLocationRelativeTo(null);
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	//Inputs
	protected JPanelDialog jPanel;

	}
