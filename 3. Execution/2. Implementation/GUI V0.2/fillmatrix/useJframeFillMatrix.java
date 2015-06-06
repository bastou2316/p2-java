
package ch.hearc.coursjava.gui.p2.fillmatrix;

import javax.swing.UIManager;


public class useJframeFillMatrix
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
		useJframeFillMatrix();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private static  void useJframeFillMatrix()
		{
		try
			{
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			}
		catch (Throwable e)
			{
			e.printStackTrace();
			}
		new JFrameFillMatrix(3, 3);
		}




	}
