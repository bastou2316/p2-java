
package ch.hearc.coursjava.gui.p2.menu;

import javax.swing.UIManager;

public class useJFrameMenu
	{

	/*------------------------------------------------------------------*\
	 |*							Methodes Public							*|
	 \*------------------------------------------------------------------*/

	public static void main(String[] args)
		{
		main();
		}

	public static void main()
		{
		try
			{
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			}
		catch (Throwable e)
			{
			e.printStackTrace();
			}

		new JFrameMenu();

		}

	/*------------------------------------------------------------------*\
	 |*							Methodes Private						*|
	 \*------------------------------------------------------------------*/

	}
