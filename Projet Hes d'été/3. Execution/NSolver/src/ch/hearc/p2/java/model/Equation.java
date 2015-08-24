
package ch.hearc.p2.java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Equation implements Serializable
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public Equation()
		{
		this("Name", 3, 3, 1, true);
		}

	public Equation(String name, int numberVar, int numberEquation, int speedSec, boolean modeStep)
		{
		listMatrix = new ArrayList<Matrix>();
		listOperation = new ArrayList<String>();
		this.name = name;
		this.numberVar = numberVar;
		this.numberEquation = numberEquation;
		this.speedMs = speedSec * 1000;
		this.modeStep = modeStep;
		this.saved = false;
		}

	public Equation(Equation src)
		{
		this(src.getName(), src.getMatrixNumberVariable(), src.getMatrixNumberEquation(), src.getSpeedSec(), src.isStepMode());
		this.setMatrix(src.getMatrix(0));
//		this.solve();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public void solve()
		{
		Matrix matrix = new Matrix(listMatrix.get(ORIGIN));
		if (modeStep)
			{
			matrix.reducedRowEchelonForm();
			for(int i = 1; i < matrix.getHistLength() - 1; ++i)
				{
				listMatrix.add(new Matrix(matrix.getStep(i)));
				listOperation.add(matrix.getOperation(i));
				}
			listOperation.add("Final");
			}
		else
			{
			listMatrix.add(new QRDecomposition(matrix.getMatrix(0, matrix.rowCount() - 1, 0, matrix.columnCount() - 2)).solve(matrix.getMatrix(0, matrix.rowCount() - 1, matrix.columnCount() - 1, matrix.columnCount() - 1)));
			listOperation.add("Final");
			}

		solved = true;
		}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	public String getVariableName()
		{
		return variableName;
		}

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

	public int getSpeedSec()
		{
		return speedMs / 1000;
		}

	public long getSpeedMs()
		{
		return speedMs;
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

	public List<String> getOperations()
		{
		return listOperation;
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	public void setVariableName(String variableName)
		{
		listMatrix.get(listMatrix.size() - 1).setVariableName(variableName);
		}

	public void setMatrix(Matrix matrix)
		{
		listMatrix.clear();
		listOperation.clear();

		listMatrix.add(ORIGIN, matrix);
		listOperation.add(ORIGIN, "Origine");
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public void setNumberVar(int numberVar)
		{
		this.numberVar = numberVar;
		}

	public void setNumberEquation(int numberEquation)
		{
		this.numberEquation = numberEquation;
		}

	public void setSpeed(int speedSec)
		{
		this.speedMs = speedSec * 1000;
		}

	public void setModeStep(boolean modeStep)
		{
		this.modeStep = modeStep;
		}

	public void setSaved()
		{
		saved = true;
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
	private int speedMs;
	private boolean modeStep;
	private boolean solved;
	private boolean saved;

	public String variableName;

	public final static int ORIGIN = 0;

	}
