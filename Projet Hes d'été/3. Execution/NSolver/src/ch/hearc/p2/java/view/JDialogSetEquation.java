
package ch.hearc.p2.java.view;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import ch.hearc.p2.java.view.jpanel.JPanelSetEquation;

public class JDialogSetEquation extends JDialog
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JDialogSetEquation(JPanelSetEquation jPanel)
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

	public void close()
		{
		setVisible(false);
		dispose();
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

//	public void setPanel(String title, JPanel jpanel, int sizeX, int sizeY)
//		{
//		setSize(sizeX, sizeY);
//		setLocationRelativeTo(null);
//
//		setPanel(title, jpanel);
//		}
//
//	public void setPanel(String title, JPanel jpanel)
//		{
//		setTitle(title);
//
//		setContentPane(jpanel);
//
//		revalidate();
//		}

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

		add(jPanel);
		}

	private void control()
		{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		//Changement de comportement du bouton ferm� des dialogues
//		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//		addWindowListener(new WindowAdapter()
//			{
//
//				@Override
//				public void windowClosing(WindowEvent e)
//					{
//////					if (controllerMain.isEquationSetted())
//////						{
////						//controllerMain.avoidNewEquation();
////						controllerMain.stopCreating();
////						controllerMain.closeDialog();
//////						}
//////					else
//////						{
//////						JOptionPane.showMessageDialog(JDialogMain.this, "Veuillez d�finir l'�quation et la matrice.");
//////						}
//					}
//			});
		}

	private void appearance()
		{
		setTitle("Cr�ation de probl�me");
		setSize(300, 300);
		setLocationRelativeTo(null);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public int showDialog()
	{
		setVisible(true);
		return jPanel.getChoice();
	}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/
	JPanelSetEquation jPanel;

	}
