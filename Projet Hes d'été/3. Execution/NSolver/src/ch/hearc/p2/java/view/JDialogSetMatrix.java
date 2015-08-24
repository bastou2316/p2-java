
package ch.hearc.p2.java.view;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import ch.hearc.p2.java.view.jpanel.JPanelSetMatrix;

public class JDialogSetMatrix extends JDialog
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JDialogSetMatrix(JPanelSetMatrix jPanelSetMatrix)
		{
		super();

		this.jPanel = jPanelSetMatrix;

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

		//Changement de comportement du bouton fermé des dialogues
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//		addWindowListener(new WindowAdapter()
//			{
//
//				@Override
//				public void windowClosing(WindowEvent e)
//					{
////					if (controllerMain.isEquationSetted())
////						{
//						//controllerMain.avoidNewEquation();
////						controllerMain.stopCreating();
////						controllerMain.closeDialog();
////						}
////					else
////						{
////						JOptionPane.showMessageDialog(JDialogMain.this, "Veuillez définir l'équation et la matrice.");
////						}
//					}
//			});
		}

	private void appearance()
		{
		setTitle("Remplissage de la matrice");
		setSize(500, 300);
		setLocationRelativeTo(null);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public int showDialog()
	{
		setVisible(true);
		return jPanel.getChoice();//jPanel.getMatrix();
	}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	private JPanelSetMatrix jPanel;

	}
