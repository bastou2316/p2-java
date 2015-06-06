package use;

import ch.hearc.p2.java.model.TestUnitMatrix;

public class UseMatrixAnd3D {

	public static void main(String[] args) {
		testUnit = new TestUnitMatrix();
//		test1();
//		test2();
//		test3();
		test('C');

	}

	private static void test1() {
		testUnit.test3x3_Infinit_Line();
	}

	private static void test2() {
		testUnit.test3x3_Unique();
	}
	
	private static void test3() {
		testUnit.test3x3_Infinit_Line2();
	}
	
	private static void test(char index) {
		switch(index){
		case 'A' :
			testUnit.test3x3_A();
			break;
		case 'B' :
			testUnit.test3x3_B();
			break;
		case 'C' :
			testUnit.test3x3_C();
			break;
		case 'D' :
			testUnit.test3x3_D();
			break;
		case 'E' :
			testUnit.test3x3_E();
			break;
		case 'F' :
			testUnit.test3x3_F();
			break;
		case 'G' :
			testUnit.test3x3_G();
			break;
		default:
			//rien
		
		}
		
	}
	

	private static TestUnitMatrix testUnit;
}
