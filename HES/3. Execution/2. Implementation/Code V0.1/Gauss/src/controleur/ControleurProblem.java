
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
		return problem.nextStep();
		}
	public String previousStep()
		{
		//A chaque pas de l'animation
		return problem.previousStep();
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
