
package ch.hearc.p2.java.model;

import java.util.ArrayList;

import org.junit.Test;

public final class TestUnitMatrix
	{

	//private static Random random = new Random();
	//
	//		@Test
	//		public void test3x3_Infinit_Line_V2()
	//			{
	//			double[][] in = { { 0, 1, 0, 1 }, { 0, 0, 1, 2 }, { 0, 0, 0, 0 } };
	//			double[][] out = { { 0, 1, 0, 1 }, { 0, 0, 1, 2 }, { 0, 0, 0, 0 } };
	//			testReduceMatrix(in, out);
	//			//testDecompositionQR(in, out);
	//			}
	//
	//		@Test
	//		public void test3x3_Unique()
	//			{
	//			double[][] in = { { 0, 1, 1, 4 }, { 2, 4, -2, 2 }, { 0, 3, 15, 36 } };
	//			double[][] out = { { 1, 0, 0, -1 }, { 0, 1, 0, 2 }, { 0, 0, 1, 2 } };
	//			testReduceMatrix(in, out);
	//			//testDecompositionQR(in, out);
	//			}
	//
	//		@Test
	//		public void test3x3_Infinit_Line()
	//			{
	//			double[][] in = { { 0, 1, 1, 4 }, { 2, 4, -2, 2 }, { 0, 1, 1, 4 } };
	//			double[][] out = { { 1, 0, -3, -7 }, { 0, 1, 1, 4 }, { 0, 0, 0, 0 } };
	//			testReduceMatrix(in, out);
	//			//testDecompositionQR(in, out);
	//			}
	//
	//		@Test
	//		public void test3x4_Impossible()
	//			{
	//			double[][] in = { { 0, 1, 1, 1, 4 }, { 2, 4, -2, 2, 2 }, { 0, 1, 1, 1, 5 } };
	//			double[][] out = { { 1, 2, -1, 1, 1 }, { 0, 1, 1, 1, 4 }, { 0, 0, 0, 0, 1 } };
	//			testReduceMatrix(in, out);
	//			//testDecompositionQR(in, out);
	//			}
	//
	//		@Test
	//		public void test3x4_Infinit_Line()
	//			{
	//			double[][] in = { { 0, 1, 1, 1, 4 }, { 2, 4, -2, 2, 2 }, { 0, 1, 1, 1, 4 } };
	//			double[][] out = { { 1, 0, -3, -1, -7 }, { 0, 1, 1, 1, 4 }, { 0, 0, 0, 0, 0 } };
	//			testReduceMatrix(in, out);
	//			//testDecompositionQR(in, out);
	//			}
	//
	//		@Test
	//		public void test4x3_Impossible()
	//			{
	//			double[][] in = { { 0, 1, 1, 1 }, { 2, 4, -2, 2 }, { 0, 1, 1, 2 }, { 0, 1, 1, 2 } };
	//			double[][] out = { { 1, 2, -1, 1 }, { 0, 1, 1, 1 }, { 0, 0, 0, 1 }, { 0, 0, 0, 0 } };
	//			testReduceMatrix(in, out);
	//			//testDecompositionQR(in, out);
	//			}
	//
	//		@Test
	//		public void test4x3_Unique()
	//			{
	//			double[][] in = { { 0, 1, 1, 1 }, { 2, 4, -2, 2 }, { 0, 1, 2, 2 }, { 0, 1, 2, 2 } };
	//			double[][] out = { { 1, 0, 0, 2 }, { 0, 1, 0, 0 }, { 0, 0, 1, 1 }, { 0, 0, 0, 0 } };
	//			testReduceMatrix(in, out);
	//			//testDecompositionQR(in, out);
	//			}
	//
	//		@Test
	//		public void test4x3_Infinit_Line()
	//			{
	//			double[][] in = { { 0, 1, 1, 1 }, { 2, 4, -2, 2 }, { 2, 4, -2, 2 }, { 2, 4, -2, 2 } };
	//			double[][] out = { { 1, 0, -3, -1 }, { 0, 1, 1, 1 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
	//			testReduceMatrix(in, out);
	//			//testDecompositionQR(in, out);
	//			}
	//
	//		@Test
	//		public void test3x4_Infinit_Line_2()
	//			{
	//			double[][] in = { { 1, 0,0,0,0 }, { 0, 1, 0, 0,0 }, { 0, 0, 0, 0,0 } };
	//			double[][] out = { { 1, 0, -3, -1 }, { 0, 1, 1, 1 }, { 0, 0, 0, 0 }};
	//			testReduceMatrix(in, out);
	//			//testDecompositionQR(in, out);
	//			}
	//
	//	@Test
	//	public void test3x6_Infinit_Line_()
	//		{
	//		double[][] in = { { 1, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 } };
	//		double[][] out = { { 1, 0, -3, -1 }, { 0, 1, 1, 1 }, { 0, 0, 0, 0 } };
	//		testReduceMatrix(in, out);
	//		//testDecompositionQR(in, out);
	//		}
	//
	//	@Test
	//	public void big_number_2()
	//		{
	//		double[][] in = { { 65412355, 0, 10,400},{44,1,1,10},{50,1,1,1} };
	//		double[][] out = { { 1, 0, -3,1}};
	//		testReduceMatrix(in, out);
	//		//testDecompositionQR(in, out);
	//		}

	@Test
	public void test()
		{
		double[][] in = { { 2, 4, 5, 544 }, { 3, 1.5, 4, 3 }, { 2.4, 3, -1, 4 } };
		double[][] out = { { 1, 0, -3, 1 } };
		testReduceMatrix(in, out);
		//testDecompositionQR(in, out);
		}

	private static void testReduceMatrix(double[][] in, double[][] out)
		{
		Matrix matIn = new Matrix(in.length, in[0].length);
		for(int i = 0; i < in.length; i++)
			{
			for(int j = 0; j < in[i].length; ++j)
				{
				matIn.set(i, j, in[i][j]);
				}
			}
		Log log = new Log(matIn, true, 0);
		//		System.out.println("Initial matrix");
		//		stepToString(log.getMatrix(0), log.getRows(), log.getCols());
		//		String[][] tabMatEmpirical = log.getMatrix(log.getNbStep() - 1);
		for(int i = 0; i < out.length; i++)
			{
			for(int j = 0; j < out[i].length; j++)
				{
				//Assert.assertTrue(MathTools.isEquals(out[i][j], tabMatEmpirical[i][j]);
				}
			}
		//		System.out.println("Solution matrix");
		//		stepToString(log.getMatrix(log.getNbStep() - 1), log.getRows(), log.getCols());
		if (log.getNbStep() != 0)
			{
			//System.out.println("Details des operations\n");
			ArrayList<String> listOperation = new ArrayList<String>();
			listOperation = (ArrayList<String>)log.getListOperation();
			for(int i = 0; i < listOperation.size() - 1; i++)
				{
				if (i > 0)
					{
					System.out.println("----------");
					}
				System.out.println("Step=" + i + ", Operation: " + listOperation.get(i));
				System.out.println("----------");
				stepToString(log.getMatrix(i));
				}
			System.out.println("Step=" + (listOperation.size() - 1) + ", Operation: " + listOperation.get(listOperation.size() - 1));
			}
		System.out.println(log.getSolution());
		double[][] temp = log.getParametricEquations();
		System.out.println("State of tabCoeficient:");
		stepToString(temp);
		System.out.println("-------------------------------------------");
		}

	//	private static void testDecompositionQR(double[][] in, double[][] out)
	//		{
	//		Matrix mat = new Matrix(in.length, in[0].length - 1);
	//		for(int i = 0; i < in.length; i++)
	//			{
	//			for(int j = 0; j < in[i].length - 1; j++)
	//				{
	//				mat.set(i, j, in[i][j]);
	//				}
	//			}
	//		Matrix b = new Matrix(in.length, 1);
	//		for(int i = 0; i < in.length; i++)
	//			{
	//			b.set(i, 0, in[i][in.length]);
	//			}
	//		System.out.println("------QR decompositon--------");
	//		System.out.println();
	//		System.out.println("Initial matrix");
	//		System.out.println(mat.toString());
	//		System.out.println("Initial vector");
	//		System.out.println(b.toString());
	//
	//		QRDecomposition matQR = new QRDecomposition(mat);
	//		try
	//			{
	//			Matrix matSolved = matQR.solve(b);
	//			for(int i = 0; i < out.length; i++)
	//				{
	//
	//				//Assert.assertTrue(MathTools.isEquals(out[i][out.length], matSolved.get(i, 0)));
	//				}
	//
	//			System.out.println("Solution matrix");
	//			System.out.println(mat.toString());
	//			}
	//		catch (RuntimeException e)
	//			{
	//			System.out.println();
	//			System.out.println("La matrice n'a pas de solution unique.");
	//			System.out.println();
	//			System.out.println("-------------------------------------------");
	//			System.out.println();
	//			}
	//
	//		}

	public static void stepToString(String[][] tabMatrix)
		{

		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < tabMatrix.length; i++)
			{
			for(int j = 0; j < tabMatrix[0].length - 1; j++)
				{
				builder.append(tabMatrix[i][j]);
				builder.append(" ");
				}
			builder.append("= ");
			builder.append(tabMatrix[i][tabMatrix[0].length - 1]);
			builder.append(System.getProperty("line.separator"));
			}
		System.out.print(builder.toString());
		}

	public static void stepToString(double[][] tabMatrix)
		{
		if (tabMatrix != null)
			{
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < tabMatrix.length; i++)
				{
				for(int j = 0; j < tabMatrix[0].length; j++)
					{
					builder.append(tabMatrix[i][j]);
					builder.append(" ");
					}
				builder.append(System.getProperty("line.separator"));
				}
			System.out.print(builder.toString());
			}
		else
			{
			System.out.println("the tab is null");
			}
		}
	}
