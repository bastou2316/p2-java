package ch.hearc.p2.java;
import ch.hearc.p2.java.model.Matrix;
import ch.hearc.p2.java.model.Problem;
import ch.hearc.p2.java.view.jframe.JFrameResolution;






public class UseJFrameResolution
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
		Matrix matrix = new Matrix();
		Problem problem = new Problem(matrix, 1);
		new JFrameResolution(problem);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	}

