
package controleur;

import model.Problem;

public class ControleurProblem
	{

	public ControleurProblem(Problem problem)
		{
		//Recup. de la matrice
		this.problem = problem;
		}

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/


	public String nextStep()
		{
		//A chaque pas de l'animation
		problem.nextStep();
		return problem.getMatrix().toString();
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	public long getSpeed()
		{
		return problem.getSpeed();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	//Tools
	private Problem problem;

	}
