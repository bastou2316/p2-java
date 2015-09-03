package ch.hearc.p2.java.view.jpanel3d;

import ch.hearc.p2.java.model.Matrix;

public class TestUnit {
	
	
	public static void main(String[] args) {
		test1();
	}
	
	static void test1(){
		Matrix m = new Matrix(new double[][]{{1,2,-1,1},{2,1,2,2},{1-4,7,3}});
		JPanel3D panel = new JPanel3D(m);
		panel.setVisible(true);
	}
}
