
package math;

import org.junit.Test;


public final class TestUnitMatrix
	{

		//private static Random random = new Random();


		@Test
		public void test4x4() {
			double[][] in = {
							{ 0, 1,  1, 4},
                        	{ 2, 4, -2 , 2},
                        	{ 0, 3, 15 , 36}
                      		};
			double[][] out = {
							{ 1, 0, 0, -1},
							{ 0, 1, 0, 2},
							{ 0, 0, 1, 2}
							};
			testReduceMatrix(in, out);
			testDecompositionQR(in, out);
		}


		private static void testReduceMatrix(double[][] in, double[][] out) {
			Matrix mat = new Matrix (in.length, in[0].length);
			for (int i = 0; i < out.length; i++) {
				for (int j = 0; j < out[i].length; j++)
					{
						mat.set(i, j, in[i][j]);
						}
			}
			System.out.println("Initial matrix");
			mat.print();

			mat.reducedRowEchelonForm();

			for (int i = 0; i < out.length; i++) {
				for (int j = 0; j < out[i].length; j++)
					{
						//assertTrue(MathTools.isEquals(out[i][j], mat.get(i, j)));
					}
			}
			System.out.println("Solution matrix");
			mat.print();
		}
		private static void testDecompositionQR(double[][] in, double[][] out) {
		Matrix mat = new Matrix (in.length, in[0].length);
		for (int i = 0; i < out.length; i++) {
			for (int j = 0; j < out[i].length; j++)
				{
					mat.set(i, j, in[i][j]);
					}
		}
		System.out.println("Initial matrix");
		mat.print();

		QRDecomposition matQR=new QRDecomposition(mat);

		for (int i = 0; i < out.length; i++) {
			for (int j = 0; j < out[i].length; j++)
				{
					//assertTrue(MathTools.isEquals(out[i][j], matQR.get(i, j)));
				}
		}
		System.out.println("Solution matrix");
		matQR.print();
		}
	}

