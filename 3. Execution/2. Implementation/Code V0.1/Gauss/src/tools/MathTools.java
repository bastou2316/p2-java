
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
	/** sqrt(a^2 + b^2) without under/overflow. **/

	   public static double hypot(double a, double b) {
	      double r;
	      if (Math.abs(a) > Math.abs(b)) {
	         r = b/a;
	         r = Math.abs(a)*Math.sqrt(1+r*r);
	      } else if (b != 0) {
	         r = a/b;
	         r = Math.abs(b)*Math.sqrt(1+r*r);
	      } else {
	         r = 0.0;
	      }
	      return r;
	   }
	}
