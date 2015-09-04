
package ch.hearc.p2.java.view.dialog;

import ch.hearc.p2.java.view.JDialogMain;
import ch.hearc.p2.java.view.jpanel.dialog.JPanelDialog;

public class JDialogSetMatrix extends JDialogMain
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JDialogSetMatrix(JPanelDialog jPanel)
		{
		super(jPanel);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	@Override
	protected void appearance()
		{
		setTitle("Remplissage de la matrice");
		setSize(500, 350);
		setLocationRelativeTo(null);
		}

	}
