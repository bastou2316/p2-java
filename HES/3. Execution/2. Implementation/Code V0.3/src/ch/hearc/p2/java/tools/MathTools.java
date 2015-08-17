
package ch.hearc.p2.java.tools;

public class MathTools
	{

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public static boolean isEquals(double a, double b)
		{
		if (a == 0 || b == 0)
			{
			return Math.abs(a - b) <= 1E-6;
			}
		else
			{
			return Math.abs((a - b) / a) <= 1E-6;
			}
		}

	/**
	 * Exemple: <b>epsilon</b> = 1E-6
	 */
	public static boolean isEquals(double a, double b, double epsilon)
		{
		if (a == 0 || b == 0)
			{
			return Math.abs(a - b) <= epsilon;
			}
		else
			{
			return Math.abs((a - b) / a) <= epsilon;
			}
		}

	/**
	 * Exemple: <b>epsilon</b> = 1E-6
	 */
	public static boolean isEquals(long a, long b, long epsilon)
		{
		return Math.abs(a-b) <= epsilon;
		}


	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	}
