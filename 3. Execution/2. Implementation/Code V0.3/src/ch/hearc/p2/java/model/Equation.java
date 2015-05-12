
package ch.hearc.p2.java.model;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Equation implements Serializable
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public Equation(Matrix matrix, int numberVar, int numberEquation, int speed, boolean modeStep)
		{
		listMatrix = new ArrayList<Matrix>();
		listMatrix.add(matrix);
		this.numberVar = numberVar;
		this.numberEquation = numberEquation;
		this.speed = speed;
		this.modeStep = modeStep;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/
	public void solve()
		{
		// Solve le toute
		}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	public long getSpeed()
		{
		return speed;
		}

	public Dimension getMatrixDimension()
		{
		return new Dimension(numberEquation, numberVar + 1);
		}

	public Matrix getMatrix(int pos)
		{
		return listMatrix.get(pos);
		}

	/*------------------------------*\
	|*				Is				*|
	\*------------------------------*/

	public boolean isStepMode()
		{
		return modeStep;
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	List<Matrix> listMatrix;
	int numberVar;
	int numberEquation;
	int speed;
	boolean modeStep = false;

	final static int ORIGIN = 0;

	}
