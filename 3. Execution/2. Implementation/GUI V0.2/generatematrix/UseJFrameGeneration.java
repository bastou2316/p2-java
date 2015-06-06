
package ch.hearc.coursjava.gui.p2.generatematrix;

import javax.swing.UIManager;



public class UseJFrameGeneration
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
	new JFrameGeneration();
	}

/*------------------------------------------------------------------*\
|*							Methodes Private						*|
\*------------------------------------------------------------------*/

	}

