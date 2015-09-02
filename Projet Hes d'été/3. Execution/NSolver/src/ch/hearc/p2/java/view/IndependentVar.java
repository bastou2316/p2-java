
package ch.hearc.p2.java.view;

public class IndependentVar
	{

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public static void afficher(String[][] tab2d)
		{
		int n = tab2d.length;
		int m = tab2d[0].length;

		for(int i = 1; i <= n; i++)
			{
			for(int j = 1; j <= m; j++)
				{
				System.out.print(tab2d[i - 1][j - 1]);
				System.out.print("\t");
				}
			System.out.print("\n");
			}
		}

	public static String[] getVarStyle(int noMethod, int n, int m)
		{
		m = m - 1; //Matrice non-augmentée
		String[] tabString = new String[n];

		switch(noMethod)
			{
			case 0:
				System.out.println(m + "");
				if (m > 3)
					{
					getVarStyle(2, n, m);
					}
				else
					{
					tabString = new String[3];
					tabString[0] = "X";
					tabString[1] = "Y";
					tabString[2] = "Z";
					}
				break;
			case 1:
				if (m > 26)
					{
					getVarStyle(2, n, m);
					}
				else
					{
					tabString = new String[27];
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

			case 2:
				tabString = new String[m + 1];
				for(int i1 = 1; i1 <= m; i1++)
					{
					tabString[i1 - 1] = "X" + i1;
					}
				break;
			default:
				throw new IllegalStateException("Variable style does not exist");
			}

		return tabString;
		}

	}
