
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
		//Pas encore de matrice
		this.equation = null;
		this.actualStep = 0;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

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

	public void setEquation(Matrix matrix, int numberVar, int numberEquation, boolean modeStep, int speed)
		{
		equation = new Equation(matrix, numberVar, numberEquation, speed, modeStep);
		equation.solve();
		}

	public long getSpeed()
		{
		return equation.getSpeed();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	//Tools
	private Equation equation;
	private int actualStep;

	}
