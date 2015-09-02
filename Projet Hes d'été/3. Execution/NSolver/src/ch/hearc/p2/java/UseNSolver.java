
package ch.hearc.p2.java;

import javax.swing.UIManager;

import ch.hearc.p2.java.controller.ControllerMain;

public class UseNSolver
	{

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public static void main(String[] args)
		{
		/*try
			{
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			}
		catch (Exception e)
			{
			e.printStackTrace();
			}*/

		main();
		}

	public static void main()
		{
		try
			{
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			}
		catch (Exception e)
			{
			e.printStackTrace();
			}

		new ControllerMain();
		}
	}
