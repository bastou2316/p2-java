
package ch.hearc.p2.java.controller;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import ch.hearc.p2.java.model.Equation;
import ch.hearc.p2.java.model.Matrix;
import ch.hearc.p2.java.view.JDialogMain;
import ch.hearc.p2.java.view.JFrameMain;
import ch.hearc.p2.java.view.dialog.JDialog3D;
import ch.hearc.p2.java.view.dialog.JDialogSetEquation;
import ch.hearc.p2.java.view.dialog.JDialogSetMatrix;
import ch.hearc.p2.java.view.jpanel.JPanelMenu;
import ch.hearc.p2.java.view.jpanel.JPanelResultStep;
import ch.hearc.p2.java.view.jpanel.dialog.JPanelSetEquation;
import ch.hearc.p2.java.view.jpanel.dialog.JPanelSetMatrix;
import ch.hearc.p2.java.view.jpanel3d.JPanel3D;

public class ControllerMain
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public ControllerMain()
		{
		super();

		jFrame = new JFrameMain(this);
		controllerIO = new ControllerIO();

		changeView(PANEL.MENU);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	//Affichage de la vue appropriée
	public void changeView(PANEL panel)
		{
		if (currentView != panel)//Empeche le double affichage
			{
			currentView = panel;

			switch(panel)
				{
				case MENU:
					currentPanel = new JPanelMenu(this);
					jFrame.setPanel("Menu", currentPanel, 1000, 600);
					jFrame.enableAlternativeMenu(false);
					break;

				case RESULT:
					currentPanel = new JPanelResultStep(equation);
					jFrame.setPanel("Résolution directe", currentPanel, 1100, 700);
					jFrame.enableAlternativeMenu(true);
					break;

				case RESULT_STEP:
					currentPanel = new JPanelResultStep(equation);
					jFrame.setPanel("Résolution étape par étape", currentPanel, 1100, 700);
					jFrame.enableAlternativeMenu(true);
					break;

				default:
					System.out.println("Error, panel does not exist. Try : PANEL.*");
					break;
				}
			}
		}

	public void showDialog(DIALOG dialog)
		{
		currentView = dialog;

		switch(dialog)
			{
			case NEW_EQUATION:
				showNewEquationDialog();
				break;

			case SET_EQUATION:
				showSetEquationDialog();
				break;

			case SET_MATRIX:
				showSetMatrixDialog();
				break;

			case RESULT_3D:
				if (equation.getMatrixNumberEquation() >= 1 && equation.getMatrixNumberEquation() <= 4 && (equation.getMatrixNumberVariable() == 2 || equation.getMatrixNumberVariable() == 3))
					{
					JDialog3D jDialogMain = new JDialog3D(new JPanel3D(equation.getMatrix(), equation.getParametricEquations(), equation.getVariableStyle()));
					jDialogMain.showDialog();
					}
				else
					{
					JOptionPane.showMessageDialog(jFrame, "Seuls les systèmes de 1-4 équation(s) comprenant 2 à 3 inconnues, peuvent être affichés graphiquement.", "", JOptionPane.WARNING_MESSAGE);
					}
			}
		}

	public void save()
		{
		JFileChooser jfilechooser = new JFileChooser();
		jfilechooser.setDialogTitle("Choisissez un emplacement de sauvegarde");
		jfilechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		jfilechooser.setFileFilter(new FileNameExtensionFilter("NSolver extension", "nso"));
		int returnOption = jfilechooser.showSaveDialog(jFrame);

		try
			{
			if (returnOption == JFileChooser.APPROVE_OPTION)
				{
				controllerIO.save(equation, jfilechooser.getSelectedFile());
				equation.setSaved();
				}
			}
		catch (IOException e)
			{
			JOptionPane.showMessageDialog(jFrame, "Error while saving file : " + jfilechooser.getSelectedFile().getAbsolutePath(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			}
		}

	public void load()
		{
		JFileChooser jfilechooser = new JFileChooser();
		jfilechooser.setDialogTitle("Choisissez un fichier à charger");
		jfilechooser.setFileFilter(new FileNameExtensionFilter("NSolver extension", "nso"));
		int returnOption = jfilechooser.showOpenDialog(jFrame);

		if (returnOption == JFileChooser.APPROVE_OPTION)
			{
			try
				{
				equation = controllerIO.load(jfilechooser.getSelectedFile());
				//equation.solve();//voir si deja solve
				changeView(PANEL.RESULT);
				}
			catch (ClassNotFoundException e)
				{
				JOptionPane.showMessageDialog(jFrame, "Didn't found \"Equation.class\".", e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				}
			catch (IOException e)
				{
				JOptionPane.showMessageDialog(jFrame, "Error while loading file : " + jfilechooser.getSelectedFile().getAbsolutePath(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				}
			}
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void showNewEquationDialog()
		{
		//Si l'equation n'a pas deja ete commencé (suivant puis précédent),
		//on en crée une nouvelle
		if (equationTemp == null)
			{
			equationTemp = new Equation();
			}

		//Affichage du dialogue
		JDialogMain jDialog = new JDialogSetEquation(new JPanelSetEquation(equationTemp));
		int result = jDialog.showDialog();

		//Traitement du résultat
		if (result == 1)//Suivant
			{
			showNewMatrixDialog(); //showDialog(DIALOG.NEW_MATRIX);
			}
		else
			{
			equationTemp = null;
			}
		}

	private void showNewMatrixDialog()
		{
		Matrix matrixTemp = new Matrix(equationTemp.getMatrixNumberEquation(), equationTemp.getMatrixNumberVariable() + 1);

		JDialogMain jDialog = new JDialogSetMatrix(new JPanelSetMatrix(matrixTemp, equationTemp.getVariableStyle(), false)); //equationTemp.isSolved()));

		int result = jDialog.showDialog();
		if (result == 1)//Appliqué
			{
			equationTemp.setMatrix(matrixTemp, equationTemp.isStepMode());
			equation = equationTemp;
			equationTemp = null;

			if (equation.isStepMode())
				{
				changeView(PANEL.RESULT_STEP);
				}
			else
				{
				changeView(PANEL.RESULT);
				}
			}
		else if (result == 2)//Précédent
			{
			if (equationTemp.isSolved())
				{
				showSetEquationDialog(); //A deja été résolu, donc modif
				}
			else
				{
				showNewEquationDialog(); //N'a pas été résolu, donc création
				}
			}
		else
			//Annulé
			{
			equationTemp = null;
			}
		}

	private void showSetEquationDialog()
		{
		//Copie de l'equation
		if (equationTemp == null)
			{
			equationTemp = new Equation(equation);
			}

		//Affichage
		JDialogMain jDialog = new JDialogSetEquation(new JPanelSetEquation(equationTemp, equation.getMatrixNumberVariable(), equation.getMatrixNumberEquation()));
		int result = jDialog.showDialog();

		//Traitement du resultat
		if (result == 1)//Suivant
			{
			if (equationTemp.getMatrixNumberEquation() == equation.getMatrixNumberEquation() && equationTemp.getMatrixNumberVariable() == equation.getMatrixNumberVariable())
				{
				//Matrice de meme taille, on applique les changements
				equation = equationTemp;
				equationTemp = null;

				//Maj de la fenetre principale
				JPanelResultStep panelResult = (JPanelResultStep)currentPanel;
				panelResult.updateVarStyle();

				if (equation.isStepMode())
					{
					changeView(PANEL.RESULT_STEP);
					}
				else
					{
					changeView(PANEL.RESULT);
					}

				}
			else
				{
				//On doit entrer une nouvelle matrice
				showNewMatrixDialog();//On reste dans le temporaire
				}
			}
		else
			//Annulé
			{
			equationTemp = null;
			}
		}

	private void showSetMatrixDialog()
		{
		Matrix matrixTemp = equation.getMatrix();
		JDialogMain jDialog = new JDialogSetMatrix(new JPanelSetMatrix(matrixTemp, equation.getVariableStyle(), true));

		int result = jDialog.showDialog();
		if (result == 1)//Appliqué (annulé renvoi 0)
			{
			if (equation.isStepMode())
				{
				equation.setMatrix(matrixTemp, equation.isStepMode());
				changeView(PANEL.RESULT_STEP);
				}
			else
				{
				equation.setMatrix(matrixTemp, true);
				changeView(PANEL.RESULT);
				}
			}
		else if (result == 2)//Précédent
			{
			//Rien dans ce cas
			}
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	//Tools
	private Equation equation;
	private Equation equationTemp;
	private ControllerIO controllerIO;

	//Vues
	private JFrameMain jFrame;
	private JPanel currentPanel;
	private VIEW currentView;

	private interface VIEW
		{
		}

	public enum PANEL implements VIEW
		{
		MENU, RESULT, RESULT_STEP
		}

	public enum DIALOG implements VIEW
		{
		NEW_EQUATION, SET_EQUATION, SET_MATRIX, RESULT_3D//, NEW_MATRIX
		}

	}
