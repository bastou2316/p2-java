
package ch.hearc.p2.java.view.dialog;

import ch.hearc.p2.java.view.JDialogMain;
import ch.hearc.p2.java.view.jpanel.dialog.JPanelDialog;

public class JDialog3D extends JDialogMain
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JDialog3D(JPanelDialog jPanel)
		{
		super(jPanel);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/


	@Override
	protected void appearance()
		{
		setTitle("Vue 3D du système");
		setLocation(0, 0);
		pack();
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	}
