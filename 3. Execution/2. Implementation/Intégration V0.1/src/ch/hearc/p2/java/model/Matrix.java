
package ch.hearc.p2.java.model;

import java.io.Serializable;
import java.util.Random;

public class Matrix implements Serializable
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public Matrix(int numberVar, int numberEquation)
		{

		step = 0;
		matrixes = new int[numberVar][numberEquation];
		Random rand = new Random();

		for(int i = 0; i < numberVar; i++)
			{
			for(int j = 0; j < numberEquation; j++)
				{
				matrixes[i][j] = rand.nextInt(20);
				}
			}

		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public void solveNextStep()
		{
		//Modification de la matrice jusqua la prochaine etape
		++step;
		//TODO
		}

	@Override
	public String toString()
		{
		StringBuilder builder = new StringBuilder();
		builder.append("Matrix [step=");
		builder.append(this.step);
		builder.append("]");
		return builder.toString();
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	//Tools
	int step;
	int[][] matrixes;

	}
