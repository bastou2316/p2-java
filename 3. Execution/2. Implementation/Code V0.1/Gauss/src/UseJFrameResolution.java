
import math.Matrix;
import model.Problem;
import view.jframe.JFrameResolution;






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
		Matrix matrix = new Matrix(3,4);
		double[][] in = {
					{ 0, 1,  1, 4},
                	{ 2, 4, -2 , 2},
                	{ 0, 3, 15 , 36}
              		};
		matrix.set(in);
		Problem problem = new Problem(matrix, 1);
		new JFrameResolution(problem);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	}

