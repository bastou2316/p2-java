
package tools;

public class MathTools
	{
	public static final double EPSILON=0.00001;
	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	/**
	 * Exemple: <b>epsilon</b> = 1E-6
	 */
	public static boolean isEquals(double a, double b)
		{
		if (a == 0 || b == 0)
			{
			return Math.abs(a - b) <= EPSILON;
			}
		else
			{
			return Math.abs((a - b) / a) <= EPSILON;
			}
		}
	}
