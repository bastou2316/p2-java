
package ch.hearc.p2.java.view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import ch.hearc.p2.java.controller.ControllerMain;

public class JDialogMain extends JDialog
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JDialogMain(ControllerMain controllerMain)
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

	public void close()
		{
		setVisible(false);
		dispose();
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	public void setPanelAndShow(String title, JPanel jpanel)
		{
		setPanel(title, jpanel, 600, 400);
		}

	public void setPanel(String title, JPanel jpanel, int sizeX, int sizeY)
		{
		setSize(sizeX, sizeY);
		setLocationRelativeTo(null);

		setPanel(title, jpanel);
		}

	public void setPanel(String title, JPanel jpanel)
		{
		setTitle(title);

		setContentPane(jpanel);

		revalidate();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void geometry()
		{
		setModal(true);

		//setMinimumSize(new Dimension(300, 300));
		//setResizable(true);

		// Layout : Specification
		BorderLayout borderLayout = new BorderLayout();
		setLayout(borderLayout);
		}

	private void control()
		{
		//Changement de comportement du bouton fermé des dialogues
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter()
			{

				@Override
				public void windowClosing(WindowEvent e)
					{
//					if (controllerMain.isEquationSetted())
//						{
						//controllerMain.avoidNewEquation();
						controllerMain.stopCreating();
						controllerMain.closeDialog();
//						}
//					else
//						{
//						JOptionPane.showMessageDialog(JDialogMain.this, "Veuillez définir l'équation et la matrice.");
//						}
					}
			});
		}

	private void appearance()
		{
		//setSize(600, 400);
		//setLocationRelativeTo(null);
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	private ControllerMain controllerMain;

	}
