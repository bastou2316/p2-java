
package ch.hearc.p2.java.model;

//import org.junit.Assert;
//import org.junit.Test;

import javax.swing.JDialog;
import javax.swing.JPanel;

import ch.hearc.p2.java.JPanel3D;

public final class TestUnitMatrix
	{

	//private static Random random = new Random();

//	@Test
	public void test3x3_Unique()
		{
		double[][] in = { { 0, 1, 1, 4 }, { 2, 4, -2, 2 }, { 0, 3, 15, 36 } };
		double[][] out = { { 1, 0, 0, -1 }, { 0, 1, 0, 2 }, { 0, 0, 1, 2 } };
		testReduceMatrix(in, out);
		//testDecompositionQR(in, out);
		}

//	@Test
	public void test3x3_Infinit_Line()
		{
		double[][] in = { { 0, 1, 1, 4 }, { 2, 4, -2, 2 }, { 0, 1, 1, 4 } };
		double[][] out = { { 1, 0, -3, -7 }, { 0, 1, 1, 4 }, { 0, 0, 0, 0 } };
		testReduceMatrix(in, out);
		//testDecompositionQR(in, out);
		}
	
//	@Test
	public void test3x3_Infinit_Line2() {
		double[][] in = { { 0, 1, 1, 4 }, { 2, 4, -2, 2 }, { 2, 5, -1, 6 } };
		double[][] out = { { 1, 0, -3, -7 }, { 0, 1, 1, 4 }, { 0, 0, 0, 0 } };
		testReduceMatrix(in, out);		
	}
	
//	@Test
	public void test3x3_A() {
		double[][] in = {{ 1, 1, 1, 4 }, { 1, -2.5, 1, -0.6 }, { 0, 1, -0.5, 0 }  };
		double[][] out = { { 1, 0, -3, -7 }, { 0, 1, 1, 4 }, { 0, 0, 0, 0 } };//faux
		testReduceMatrix(in, out);		
	}
	
//	@Test
	public void test3x3_B() {
		double[][] in = { { 2, 3, -5, -16 }, { 4, 13, -8, -5 }, { 1, 2, 7, 22 } };
		double[][] out = { { 1, 0, -3, -7 }, { 0, 1, 1, 4 }, { 0, 0, 0, 0 } };//faux
		testReduceMatrix(in, out);		
	}
	
//	@Test	//Exemple présentation	//scale idéal : 5
	public void test3x3_C() {
		double[][] in = { { 1, 0, -1, 1 }, { 0, -1, 1, 0 }, { 1, 1, -2, 1 } };
		double[][] out = { { 1, 0, -3, -7 }, { 0, 1, 1, 4 }, { 0, 0, 0, 0 } };//faux
		testReduceMatrix(in, out);		
	}
	
//	@Test	//aucune solution	//scale idéal : 
	public void test3x3_D() {
		double[][] in = { { 1, -1, 0, 0 }, { 0, 1, -1, 1 }, { -1, 0, 1, 0 } };
		double[][] out = { { 1, 0, -3, -7 }, { 0, 1, 1, 4 }, { 0, 0, 0, 0 } };//faux
		testReduceMatrix(in, out);		
	}
	
//	@Test	//aucune solution	//scale idéal : 2
	public void test3x3_E() {
		double[][] in = { { 1, 2, -1, 1 }, { 2, 1, 2, 2 }, { 1, -4, 7, 3 } };
		double[][] out = { { 1, 0, -3, -7 }, { 0, 1, 1, 4 }, { 0, 0, 0, 0 } };//faux
		testReduceMatrix(in, out);		
	}
	
//	@Test	//demo
	public void test3x3_F() {
		double[][] in = { { 1, -1, 3, 5 }, { 1, 1, 1, 5 }, { -0.5, 0, 1, 0 } };
		double[][] out = { { 1, 0, -3, -7 }, { 0, 1, 1, 4 }, { 0, 0, 0, 0 } };//faux
		testReduceMatrix(in, out);		
	}
	
//	@Test
	public void test3x3_G() {
		double[][] in = { { -2, 3, 4, 2.7 }, { 2, -3, 1.3, 3.7 }, { 2, 0, 0, 1.1 } };
		double[][] out = { { 1, 0, -3, -7 }, { 0, 1, 1, 4 }, { 0, 0, 0, 0 } };//faux
		testReduceMatrix(in, out);		
	}

//	@Test
	public void test3x4_Impossible()
		{
		double[][] in = { { 0, 1, 1, 1, 4 }, { 2, 4, -2, 2, 2 }, { 0, 1, 1, 1, 5 } };
		double[][] out = { { 1, 2, -1, 1, 1 }, { 0, 1, 1, 1, 4 }, { 0, 0, 0, 0, 1 } };
		testReduceMatrix(in, out);
		//testDecompositionQR(in, out);
		}

//	@Test
	public void test3x4_Infinit_Line()
		{
		double[][] in = { { 0, 1, 1, 1, 4 }, { 2, 4, -2, 2, 2 }, { 0, 1, 1, 1, 4 } };
		double[][] out = { { 1, 0, -3, -1, -7 }, { 0, 1, 1, 1, 4 }, { 0, 0, 0, 0, 0 } };
		testReduceMatrix(in, out);
		//testDecompositionQR(in, out);
		}

//	@Test
	public void test4x3_Impossible()
		{
		double[][] in = { { 0, 1, 1, 1 }, { 2, 4, -2, 2 }, { 0, 1, 1, 2 }, { 0, 1, 1, 2 } };
		double[][] out = { { 1, 2, -1, 1 }, { 0, 1, 1, 1 }, { 0, 0, 0, 1 }, { 0, 0, 0, 0 } };
		testReduceMatrix(in, out);
		//testDecompositionQR(in, out);
		}

//	@Test
	public void test4x3_Unique()
		{
		double[][] in = { { 0, 1, 1, 1 }, { 2, 4, -2, 2 }, { 0, 1, 2, 2 }, { 0, 1, 2, 2 } };
		double[][] out = { { 1, 0, 0, 2 }, { 0, 1, 0, 0 }, { 0, 0, 1, 1 }, { 0, 0, 0, 0 } };
		testReduceMatrix(in, out);
		//testDecompositionQR(in, out);
		}

//	@Test
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
		
		
		// intégration de mon JPanel3D
		
		JDialog jDial = new JDialog();
		JPanel panel = new JPanel3D(mat);
//		jFrame.setTitle("Vue 3D du système d'équation linéaire à 3 inconnues");
//		jDial.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
//		jFrame.add(panel);
//		jFrame.pack();
		jDial.setTitle("Vue 3D du système d'équation linéaire à 3 inconnues");
//		jDial.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		jDial.add(panel);
		jDial.pack();
		

//		Dimension jframeDim = jFrame.getSize();
//		Toolkit toolkit = Toolkit.getDefaultToolkit();
//		Dimension screenDim = toolkit.getScreenSize();
//		jFrame.setLocation((screenDim .width - jframeDim.width) / 2,
//				(screenDim.height - jframeDim.height) / 2);
		jDial.setVisible(true);
		
		//fin intégration
		

		mat.reducedRowEchelonForm();
		for(int i = 0; i < out.length; i++)
			{
			for(int j = 0; j < out[i].length; j++)
				{
//				Assert.assertTrue(MathTools.isEquals(out[i][j], mat.get(i, j)));
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
//				Assert.assertTrue(MathTools.isEquals(out[i][out.length], matSolved.get(i, 0)));
			}

		System.out.println("Solution matrix");
		matSolved.print();
		}

	}
