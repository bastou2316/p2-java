
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
		String[] tabString = new String[cols];

		switch(varStyle)
			{

			case X:
				if (cols > 3)
					{
					return getTabVar(cols, VARSTYLE.X1);
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
				if (cols > 27)
					{
					return getTabVar(cols, VARSTYLE.X1);
					}
				else
					{
					tabString = new String[26];
					tabString[0] = "a";
					tabString[1] = "b";
					tabString[2] = "c";
					tabString[3] = "d";
					tabString[4] = "e";
					tabString[5] = "f";
					tabString[6] = "g";
					tabString[7] = "h";
					tabString[8] = "i";
					tabString[9] = "j";
					tabString[10] = "k";
					tabString[11] = "l";
					tabString[12] = "m";
					tabString[13] = "n";
					tabString[14] = "o";
					tabString[15] = "p";
					tabString[16] = "q";
					tabString[17] = "r";
					tabString[18] = "s";
					tabString[19] = "t";
					tabString[20] = "u";
					tabString[21] = "v";
					tabString[22] = "w";
					tabString[23] = "x";
					tabString[24] = "y";
					tabString[25] = "z";
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
