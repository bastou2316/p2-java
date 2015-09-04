
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
			return Math.abs(a - b) <= 1E-8;
			}
		else
			{
			return Math.abs((a - b) / a) <= 1E-8;
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
		return Math.abs(a - b) <= epsilon;
		}

	/** sqrt(a^2 + b^2) without under/overflow. **/
	public static double hypot(double a, double b)
		{
		double r;
		if (Math.abs(a) > Math.abs(b))
			{
			r = b / a;
			r = Math.abs(a) * Math.sqrt(1 + r * r);
			}
		else if (b != 0)
			{
			r = a / b;
			r = Math.abs(b) * Math.sqrt(1 + r * r);
			}
		else
			{
			r = 0.0;
			}
		return r;
		}

	public static double round(double value, int places)
		{
		if (places < 0) {
			throw new IllegalArgumentException();
			}

		long factor = (long)Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double)tmp / factor;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	}
