
package model;

import java.util.ArrayList;
import java.util.List;

public class Equation
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	//Creation avec assistant
	public Equation(int speed, boolean isModeStep)
		{
		this.speed = speed;
		this.isModeStep = isModeStep;
		listMatrix = new ArrayList<Matrix>();
		}

	//Chargement d'un probleme complet
	public Equation(Matrix matrix, int speed, boolean isModeStep)
		{
		this(speed, isModeStep);
		listMatrix = new ArrayList<Matrix>();
		listMatrix.add(matrix);
		rowCount = matrix.rowCount();
		columCount = matrix.columnCount();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public void clear()
		{
		listMatrix.clear();
		}

	public void solve()
		{
		Matrix matrix = listMatrix.get(0);
		if (isModeStep)
			{
			matrix.reducedRowEchelonForm();
			for(int i = 1; i < matrix.getHistLength(); i++)
				{
				listMatrix.add(new Matrix(matrix.getStep(i)));
				}
			}
		else
			{
			listMatrix.add(new QRDecomposition(matrix.getMatrix(0, matrix.columnCount() - 1, 0, matrix.rowCount())).solve(matrix.getMatrix(matrix.columnCount() - 1, matrix.columnCount(), 0, matrix.rowCount())));
			}
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	public void setParameters(int speed)
		{
		this.speed = speed;
		}

	public void setMatrix(Matrix matrix)
		{
		listMatrix.clear();
		listMatrix.add(matrix);
		}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	public long getSpeed()
		{
		return speed;
		}

	public Matrix getMatrix(int n)
		{
		return listMatrix.get(n);
		}

	public int getRowCount()
		{
		return this.rowCount;
		}

	public int getColumCount()
		{
		return this.columCount;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/
	private int speed;
	private boolean isModeStep;
	private List<Matrix> listMatrix;
	//Tools

	int rowCount;
	int columCount;

	}
