
package ch.hearc.p2.java.view.dialog;

import ch.hearc.p2.java.view.JDialogMain;
import ch.hearc.p2.java.view.jpanel.dialog.JPanelDialog;

public class JDialogSetEquation extends JDialogMain
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JDialogSetEquation(JPanelDialog jPanel)
		{
		super(jPanel);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	@Override
	protected void appearance()
		{
		setTitle("Création de problème");
		setSize(450, 500);
		setLocationRelativeTo(null);
		}

	}
