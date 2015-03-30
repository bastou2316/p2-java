
package model;
import math.Matrix;

public class Problem
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	//Creation avec assistant
	public Problem(int speed)
		{
		this.speed = speed;
		}

	//Chargement d'un probleme complet
	public Problem(Matrix matrix, int speed)
		{
		this(speed);
		this.matrix = matrix;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/
	public String nextStep()
		{
		if (matrix != null)
			{
			matrix.solveNextStep();
			return matrix.toString();
			}
		else
			{
			return "Error. No matrix founded";
			}
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	public void setParameters(int speed)
		{
		this.speed = speed;
		}

	public void setMatrix(Matrix matrix)
		{
		this.matrix = matrix;
		}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	public long getSpeed()
		{
		return speed;
		}

	public Matrix getMatrix()
		{
		return matrix;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	//Tools
	Matrix matrix;
	int speed;
	boolean instanciated = false;

	}
