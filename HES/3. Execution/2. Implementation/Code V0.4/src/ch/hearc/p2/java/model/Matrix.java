
package ch.hearc.p2.java.model;

import java.io.Serializable;

public class Matrix implements Serializable
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public Matrix()
		{
		step = 0;
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

	}
