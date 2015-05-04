
package controleur;

import model.Equation;

public class ControleurProblem
	{

	public ControleurProblem(Equation problem)
		{
		//Recup. de la matrice
		this.problem = problem;
		currentStep = 0;
		}

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public void stop()
		{
		problem.clear();
		}

	public void start()
		{
		problem.solve();

		}

	public String nextStep()
		{
		//A chaque pas de l'animation
		currentStep++;
		return problem.getMatrix(currentStep).toString();
		}

	public String previousStep()
		{
		//A chaque pas de l'animation
		currentStep--;
		return problem.getMatrix(currentStep).toString();
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
	private Equation problem;
	private int currentStep;

	}
