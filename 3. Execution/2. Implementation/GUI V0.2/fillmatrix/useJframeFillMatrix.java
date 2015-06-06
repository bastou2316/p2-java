
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
		//3ème paramètre, affichage des variables
		// 0 = X, Y , Z
		// 1 = A, B, C , D
		// 3 = X1, Y1, Z1
		new JFrameFillMatrix(4, 4, 0);
		}




	}
