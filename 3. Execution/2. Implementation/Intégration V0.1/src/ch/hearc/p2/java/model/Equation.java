
package ch.hearc.p2.java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Equation implements Serializable
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public Equation(String name, int numberVar, int numberEquation, int speed, boolean modeStep)
		{
		listMatrix = new ArrayList<Matrix>();

		this.name = name;
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
		//tcheker methode solve
		}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	public String getName()
		{
		return name;
		}

	public int getMatrixNumberEquation()
		{
		return numberEquation;
		}

	public int getMatrixNumberVariable()
		{
		return numberVar;
		}

	public long getSpeed()
		{
		return speed;
		}

	public Matrix getMatrix(int pos)
		{
		if (!listMatrix.isEmpty())
			{
			return listMatrix.get(pos);
			}
		else
			{
			return null;
			}
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	public void setMatrix(Matrix matrix)
		{
		listMatrix.add(matrix);
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
	String name;
	List<Matrix> listMatrix;
	int numberVar;
	int numberEquation;
	int speed;
	boolean modeStep = false;

	final static int ORIGIN = 0;

	}
