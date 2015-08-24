
package ch.hearc.p2.java.view.jpanel.dialog;

import javax.swing.JPanel;

public class JPanelDialog extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelDialog()
		{
		super();

		this.choice = 0;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public int getChoice()
		{
		return choice;
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/
	// Tools
	protected int choice;

	}
