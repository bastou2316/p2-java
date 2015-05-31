
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
		this.equation = new Equation("Name", 3, 3, 10, true);
		this.actualStep = 0;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public void solveEquation()
		{
		equation.solve();
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	public void setEquation(String name, int numberVar, int numberEquation, boolean modeStep, int speed)
		{
		equation = new Equation(name, numberVar, numberEquation, speed, modeStep);

		//Default matrix
		Matrix matrix = new Matrix(numberVar, numberEquation);
		equation.setMatrix(matrix);
		}

	public void setEquation(Equation equation)
		{
		this.equation = equation;
		}

	public void setMatrix(Matrix matrix)
		{
		equation.setMatrix(matrix);
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
		return equation.hasMatrixIndex(actualStep+1);
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	//Tools
	private Equation equation;
	private int actualStep;

	}
