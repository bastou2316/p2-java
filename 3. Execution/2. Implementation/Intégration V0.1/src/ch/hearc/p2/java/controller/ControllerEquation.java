
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
		this.tempEquation = null;
		this.isCreating = true;//creation la 1ère fois
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public void solveEquation()
		{
		actualStep = 0;
		equation.solve();
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

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

	public void applyTempEquation()//Reload previous equation
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

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	public Equation getEquation()
		{
		if(isCreating())
			{
			return new Equation();//par défaut
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
		return equation.getSpeed();
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

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	//Tools
	private Equation equation;
	private int actualStep;

	private Equation tempEquation;
	private boolean isCreating;

	}
