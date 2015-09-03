
package ch.hearc.p2.java.view;

public class IndependentVar
	{

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public static void afficher(String[][] tab2d)
		{
		int n = tab2d.length;
		int cols = tab2d[0].length;

		for(int i = 1; i <= n; i++)
			{
			for(int j = 1; j <= cols; j++)
				{
				System.out.print(tab2d[i - 1][j - 1]);
				System.out.print("\t");
				}
			System.out.print("\n");
			}
		}

	public static String[] getTabVar(int cols, int varStyle)
	{
		return getTabVar(cols, IndependentVar.varStyleFromInt(varStyle));
	}

	public static String[] getTabVar(int cols, VARSTYLE varStyle)
		{
		cols = cols - 1;
		String[] tabString = new String[cols];

		switch(varStyle)
			{

			case X:
				if (cols > 3)
					{
					getTabVar(cols, VARSTYLE.X1);
					}
				else
					{
					tabString = new String[3];
					tabString[0] = "X";
					tabString[1] = "Y";
					tabString[2] = "Z";
					}
				break;

			case A:
				if (cols > 26)
					{
					getTabVar(cols, VARSTYLE.X1);
					}
				else
					{
					tabString = new String[26];
					tabString[0] = "A";
					tabString[1] = "B";
					tabString[2] = "C";
					tabString[3] = "D";
					tabString[4] = "E";
					tabString[5] = "F";
					tabString[6] = "G";
					tabString[7] = "H";
					tabString[8] = "I";
					tabString[9] = "J";
					tabString[10] = "K";
					tabString[11] = "L";
					tabString[12] = "M";
					tabString[13] = "N";
					tabString[14] = "O";
					tabString[15] = "P";
					tabString[16] = "Q";
					tabString[17] = "R";
					tabString[18] = "S";
					tabString[19] = "T";
					tabString[20] = "U";
					tabString[21] = "V";
					tabString[22] = "W";
					tabString[23] = "X";
					tabString[24] = "Y";
					tabString[25] = "Z";
					}
				break;

			case X1:
				tabString = new String[cols];
				for(int i1 = 1; i1 <= cols; i1++)
					{
					tabString[i1 - 1] = "X" + i1;
					}
				break;
			default:
				throw new IllegalStateException("Variable style does not exist");
			}
		return tabString;
		}

	public static VARSTYLE varStyleFromInt(int varStyle)
	{
	switch(varStyle)
		{
		case 0:
			return VARSTYLE.X;
		case 1:
			return VARSTYLE.A;
		case 2:
			return VARSTYLE.X1;
		default:
			throw new IndexOutOfBoundsException();
		}
	}

	public static enum VARSTYLE
		{
		X, A, X1
		}

	}
