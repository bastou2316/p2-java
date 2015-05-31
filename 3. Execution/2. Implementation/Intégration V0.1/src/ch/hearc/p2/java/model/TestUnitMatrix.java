
package ch.hearc.p2.java.model;

import org.junit.Assert;
import org.junit.Test;

import ch.hearc.p2.java.tools.MathTools;

public final class TestUnitMatrix
	{

	//private static Random random = new Random();

	@Test
	public void test3x3_Unique()
		{
		double[][] in = { { 0, 1, 1, 4 }, { 2, 4, -2, 2 }, { 0, 3, 15, 36 } };
		double[][] out = { { 1, 0, 0, -1 }, { 0, 1, 0, 2 }, { 0, 0, 1, 2 } };
		testReduceMatrix(in, out);
		//testDecompositionQR(in, out);
		}

	@Test
	public void test3x3_Infinit_Line()
		{
		double[][] in = { { 0, 1, 1, 4 }, { 2, 4, -2, 2 }, { 0, 1, 1, 4 } };
		double[][] out = { { 1, 0, -3, -7 }, { 0, 1, 1, 4 }, { 0, 0, 0, 0 } };
		testReduceMatrix(in, out);
		//testDecompositionQR(in, out);
		}

	@Test
	public void test3x4_Impossible()
		{
		double[][] in = { { 0, 1, 1, 1, 4 }, { 2, 4, -2, 2, 2 }, { 0, 1, 1, 1, 5 } };
		double[][] out = { { 1, 2, -1, 1, 1 }, { 0, 1, 1, 1, 4 }, { 0, 0, 0, 0, 1 } };
		testReduceMatrix(in, out);
		//testDecompositionQR(in, out);
		}

	@Test
	public void test3x4_Infinit_Line()
		{
		double[][] in = { { 0, 1, 1, 1, 4 }, { 2, 4, -2, 2, 2 }, { 0, 1, 1, 1, 4 } };
		double[][] out = { { 1, 0, -3, -1, -7 }, { 0, 1, 1, 1, 4 }, { 0, 0, 0, 0, 0 } };
		testReduceMatrix(in, out);
		//testDecompositionQR(in, out);
		}

	@Test
	public void test4x3_Impossible()
		{
		double[][] in = { { 0, 1, 1, 1 }, { 2, 4, -2, 2 }, { 0, 1, 1, 2 }, { 0, 1, 1, 2 } };
		double[][] out = { { 1, 2, -1, 1 }, { 0, 1, 1, 1 }, { 0, 0, 0, 1 }, { 0, 0, 0, 0 } };
		testReduceMatrix(in, out);
		//testDecompositionQR(in, out);
		}

	@Test
	public void test4x3_Unique()
		{
		double[][] in = { { 0, 1, 1, 1 }, { 2, 4, -2, 2 }, { 0, 1, 2, 2 }, { 0, 1, 2, 2 } };
		double[][] out = { { 1, 0, 0, 2 }, { 0, 1, 0, 0 }, { 0, 0, 1, 1 }, { 0, 0, 0, 0 } };
		testReduceMatrix(in, out);
		//testDecompositionQR(in, out);
		}

	@Test
	public void test4x3_Infinit_Line()
		{
		double[][] in = { { 0, 1, 1, 1 }, { 2, 4, -2, 2 }, { 2, 4, -2, 2 }, { 2, 4, -2, 2 } };
		double[][] out = { { 1, 0, -3, -1 }, { 0, 1, 1, 1 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
		testReduceMatrix(in, out);
		//testDecompositionQR(in, out);
		}

	private static void testReduceMatrix(double[][] in, double[][] out)
		{
		Matrix mat = new Matrix(in.length, in[0].length);
		for(int i = 0; i < out.length; i++)
			{
			for(int j = 0; j < out[i].length; j++)
				{
				mat.set(i, j, in[i][j]);
				}
			}
		System.out.println("Initial matrix");
		mat.print();

		mat.reducedRowEchelonForm();
		for(int i = 0; i < out.length; i++)
			{
			for(int j = 0; j < out[i].length; j++)
				{
				Assert.assertTrue(MathTools.isEquals(out[i][j], mat.get(i, j)));
				}
			}
		System.out.println("Solution matrix");
		mat.print();
		System.out.println("Solution de l'equation");
		System.out.println(mat.showResult());
		System.out.println("Details des operations");
		mat.showOperations();
		System.out.println("-------------------------------------------");
		}

	private static void testDecompositionQR(double[][] in, double[][] out)
		{
		Matrix mat = new Matrix(in.length, in[0].length - 1);
		for(int i = 0; i < in.length; i++)
			{
			for(int j = 0; j < in[i].length - 1; j++)
				{
				mat.set(i, j, in[i][j]);
				}
			}
		Matrix b = new Matrix(in.length, 1);
		for(int i = 0; i < in.length; i++)
			{
			b.set(i, 0, in[i][in.length]);
			}
		System.out.println("Initial matrix");
		mat.print();
		System.out.println("Initial vector");
		b.print();

		QRDecomposition matQR = new QRDecomposition(mat);

		Matrix matSolved = matQR.solve(b);

		for(int i = 0; i < out.length; i++)
			{

			Assert.assertTrue(MathTools.isEquals(out[i][out.length], matSolved.get(i, 0)));
			}

		System.out.println("Solution matrix");
		matSolved.print();
		}
	}
