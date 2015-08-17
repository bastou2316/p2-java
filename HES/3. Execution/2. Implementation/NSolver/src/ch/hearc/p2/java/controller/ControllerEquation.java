
package ch.hearc.p2.java.controller;

import ch.hearc.p2.java.model.Equation;
import ch.hearc.p2.java.model.Matrix;

public class ControllerEquation
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public ControllerEquation()
		{
		//Matrice par défaut
		this.equation = new Equation();//A enlever

		//Utilisation equation temporaire
		this.tempEquation = equation;
		this.isCreating = true;//creation la 1ère fois
		this.isFinalStep=false;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public void reInitMatrix()
		{
		setMatrix(equation.getMatrix(Equation.ORIGIN));
		}

	public void solveEquation()
		{
		actualStep = Equation.ORIGIN;
		equation.solve();
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	public void setVariableName(String name)
		{
		equation.variableName=name;
		}

	public void setEquation(Equation equation)
		{
		if (isCreating)
			{
			tempEquation = equation;
			}
		else
			{
			this.equation = equation;
			}
		}

	public void setMatrix(Matrix matrix)
		{
		if (isCreating)
			{
			tempEquation.setMatrix(matrix);
			}
		else
			{
			equation.setMatrix(matrix);
			}
		}

	public void applyTempEquation()
		{
		if (isCreating)
			{
			equation = tempEquation;
			isCreating = false;
			}
		}

	public void setCreating(boolean isCreating)
		{
		this.isCreating = isCreating;
		}

	public void setIsFinalStep(boolean isFinalStep)
	{
	this.isFinalStep=isFinalStep;
	}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/
	public String getVariableName()
		{
		return equation.getVariableName();
		}
	public Equation getEquation()
		{
		if (isCreating())
			{
			tempEquation = (tempEquation == equation) ?  new Equation() : tempEquation;
			return tempEquation;
			}
		else
			{
			return equation;
			}
		}

	public Matrix getMatrix(int pos)
		{
		actualStep = pos;
		return equation.getMatrix(pos);
		}

	public Matrix getCurrentMatrix()
		{
		return equation.getMatrix(actualStep);
		}

	public int getCurrentStep()
		{
		return actualStep;
		}

	public Matrix getNextMatrix()
		{
		++actualStep;
		return equation.getMatrix(actualStep);
		}

	public Matrix getPreviousMatrix()
		{
		--actualStep;
		return equation.getMatrix(actualStep);
		}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	public String getName()
		{
		return equation.getName();
		}

	public int getNumberVar()
		{
		if (isCreating)
			{
			return tempEquation.getMatrixNumberVariable();
			}
		else
			{
			return equation.getMatrixNumberVariable();
			}
		}

	public int getNumberEquation()
		{
		if (isCreating)
			{
			return tempEquation.getMatrixNumberEquation();
			}
		else
			{
			return equation.getMatrixNumberEquation();
			}
		}

	public boolean getStepMode()
		{
		return equation.isStepMode();
		}

	public long getSpeed()
		{
		return equation.getSpeedMs();
		}

	/*------------------------------*\
	|*				Is				*|
	\*------------------------------*/

	public boolean hasNextMatrix()
		{
		return equation.hasMatrixIndex(actualStep + 1);
		}

	public boolean isEquationSolved()
		{
		return equation.isSolved();
		}

	public boolean isCreating()
		{
		return isCreating;
		}

	public boolean isFinalStep()
		{
		return this.isFinalStep;
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	//Tools
	private Equation equation;
	private int actualStep;
	private boolean isFinalStep;
	private Equation tempEquation;
	private boolean isCreating;


	}
