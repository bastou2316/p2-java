
package ch.hearc.p2.java.model;

import java.io.Serializable;
import java.util.List;

public class Equation implements Serializable
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	/**
	 *
	 */
	private static final long serialVersionUID = 4044778703986450246L;

	public Equation()
		{
		this("Name", 3, 3, 1, true);
		}

	public Equation(String name, int numberVar, int numberEquation, int speedSec, boolean modeStep)
		{
		this.name = name;
		this.numberVar = numberVar;
		this.numberEquation = numberEquation;

		this.speedMs = speedSec * 1000;
		this.modeStep = modeStep;

		this.solved = false;
		this.saved = false;
		}

	public Equation(Equation source)
		{
		this(source.getName(), source.getMatrixNumberVariable(), source.getMatrixNumberEquation(), source.getSpeedSec(), source.isStepMode());
		setMatrix(source.getMatrix(), source.isStepMode());
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

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

	public Matrix getMatrix()
		{
		return log.getOriginalMatrix();
		}

	public String[][] getMatrix(int pos)
		{
		return log.getMatrix(pos);
		}

	public List<String> getOperations()
		{
		return log.getListOperation();
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	//	public void setVariableName(String variableName)
	//		{
	//		listMatrix.get(listMatrix.size() - 1).setVariableName(variableName);
	//		}

	public void setMatrix(Matrix matrix, boolean gaussResolution)
		{
		log = new Log(matrix, gaussResolution);
		solved = true;
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

	public void setUnsolved()
		{
		solved = false;
		}

	/*------------------------------*\
	|*				Is				*|
	\*------------------------------*/

	public boolean hasMatrixIndex(int index)
		{
		return index < log.getNbStep() - 1;
		}

	public boolean isStepMode()
		{
		return modeStep;
		}

	public boolean isSolved()
		{
		return solved;
		}

	public boolean isSaved()
		{
		return saved;
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	//Inputs
	private String name;
	public String variableName;
	private int numberVar;
	private int numberEquation;

	private int speedMs;
	private boolean modeStep;

	// Tools
	private Log log;
	private boolean solved;
	private boolean saved;

	public final static int ORIGIN = 0;

	}
