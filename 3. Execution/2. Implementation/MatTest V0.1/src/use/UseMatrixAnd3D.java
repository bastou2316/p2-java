package use;

import ch.hearc.p2.java.model.TestUnitMatrix;

public class UseMatrixAnd3D {

	public static void main(String[] args) {
		testUnit = new TestUnitMatrix();
		test2();

	}

	private static void test1() {
		testUnit.test3x3_Infinit_Line();
	}

	private static void test2() {
		testUnit.test3x3_Unique();
	}
	

	private static TestUnitMatrix testUnit;
}
