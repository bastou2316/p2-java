
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
		this.equation = new Equation();

		//Utilisation matrice temporaire
		this.tempEquation = null;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public void solveEquation()
		{
		this.actualStep = 0;
		equation.solve();
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	public void setEquation(Equation equation)
		{
		this.equation = equation;
		}

	public void setMatrix(Matrix matrix)
		{
		equation.setMatrix(matrix);
		}

	public void useTemp()//Save current equation
		{
		tempEquation = equation;
		tempStep = actualStep;
		}

	public void avoidTemp()//Reload previous equation
		{
		if (tempEquation != null)
			{
			equation = tempEquation;
			actualStep = tempStep;
			tempEquation = null;
			}
		}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	public Equation getEquation()
		{
		return equation;
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
		return getMatrix(actualStep);
		}

	public Matrix getPreviousMatrix()
		{
		--actualStep;
		return getMatrix(actualStep);
		}

	public String getName()
		{
		return equation.getName();
		}

	public int getNumberVar()
		{
		return equation.getMatrixNumberVariable();
		}

	public int getNumberEquation()
		{
		return equation.getMatrixNumberEquation();
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

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	//Tools
	private Equation equation;
	private int actualStep;

	private Equation tempEquation;
	private int tempStep;

	}
