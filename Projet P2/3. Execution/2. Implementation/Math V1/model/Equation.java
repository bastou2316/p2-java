
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
		listOperation = new ArrayList<String>();
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
		Matrix matrix = listMatrix.get(0);
		if (modeStep)
			{
			matrix.reducedRowEchelonForm();
			for(int i = 0; i < matrix.getHistLength(); ++i)
				{
				listMatrix.set(i, new Matrix(matrix.getStep(i)));
				listOperation.set(i, matrix.getOperation(i));
				}
			}
		else
			{
			try{
			listMatrix.set(1, new QRDecomposition(matrix.getMatrix(0, matrix.rowCount() - 1, 0, matrix.columnCount() - 2)).solve(matrix.getMatrix(0, matrix.rowCount() - 1, matrix.columnCount() - 1, matrix.columnCount() - 1)));
			}catch (RuntimeException e)
			{
			System.out.println("nosol");
			}
			}
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

	public String getOperation(int pos)
		{
		if (!listOperation.isEmpty())
			{
			return listOperation.get(pos);
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
	List<String> listOperation;
	int numberVar;
	int numberEquation;
	int speed;
	boolean modeStep = false;

	final static int ORIGIN = 0;

	}
