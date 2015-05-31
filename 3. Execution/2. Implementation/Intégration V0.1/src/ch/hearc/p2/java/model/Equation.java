
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
		Matrix matrix = listMatrix.get(ORIGIN);
		if (modeStep)
			{
			matrix.reducedRowEchelonForm();
			for(int i = 0; i < matrix.getHistLength(); ++i)
				{
				listMatrix.add(i, new Matrix(matrix.getStep(i)));
				listOperation.add(i, matrix.getOperation(i));
				}
			}
		else
			{
			listMatrix.set(1, new QRDecomposition(matrix.getMatrix(0, matrix.rowCount() - 1, 0, matrix.columnCount() - 2)).solve(matrix.getMatrix(0, matrix.rowCount() - 1, matrix.columnCount() - 1, matrix.columnCount() - 1)));
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
		if (listMatrix.isEmpty()) { return null; }

		return listMatrix.get(pos);

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
		listMatrix.clear();
		listMatrix.add(ORIGIN, matrix);
		}

	/*------------------------------*\
	|*				Is				*|
	\*------------------------------*/

	public boolean hasMatrixIndex(int index)
		{
		return index < listMatrix.size();
		}

	public boolean isStepMode()
		{
		return modeStep;
		}

	public boolean isSolved()
		{
		return solved;
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	private String name;
	private List<Matrix> listMatrix;
	private List<String> listOperation;
	private int numberVar;
	private int numberEquation;
	private int speed;
	private boolean modeStep;
	private boolean solved;

	private final static int ORIGIN = 0;

	}
