
package ch.hearc.p2.java.controller;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import ch.hearc.p2.java.model.Equation;
import ch.hearc.p2.java.model.Matrix;
import ch.hearc.p2.java.view.JDialogSetEquation;
import ch.hearc.p2.java.view.JDialogSetMatrix;
import ch.hearc.p2.java.view.JFrameMain;
import ch.hearc.p2.java.view.jpanel.JPanelMenu;
import ch.hearc.p2.java.view.jpanel.JPanelResultStep;
import ch.hearc.p2.java.view.jpanel.JPanelSetEquation;
import ch.hearc.p2.java.view.jpanel.JPanelSetMatrix;

public class ControllerMain
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public ControllerMain()
		{
		super();

		jFrameMain = new JFrameMain(this);
		equation = equationTemp = null;
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
			JPanel jpanel;

			switch(panel)
				{
				case MENU:
					jpanel = new JPanelMenu(this);
					jFrameMain.setPanel("Menu", jpanel);
					jFrameMain.enableAlternativeMenu(false);
					break;

				case RESULT:
					jpanel = new JPanelResultStep(equation);
					jFrameMain.setPanel("Résolution directe", jpanel, 600, 600);
					jFrameMain.enableAlternativeMenu(true);
					break;

				case RESULT_STEP:
					jpanel = new JPanelResultStep(equation);
					jFrameMain.setPanel("Résolution étape par étape", jpanel, 600, 600);
					jFrameMain.pack();
					jFrameMain.enableAlternativeMenu(true);
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

		int result;
		JDialogSetEquation jDialog;
		JDialogSetMatrix jDialogMatrix;

		switch(dialog)
			{
			case NEW_EQUATION:
				if (equationTemp == null)
					{
					equationTemp = new Equation();
					}

				jDialog = new JDialogSetEquation(new JPanelSetEquation(equationTemp));
				//jDialog.setPanel("Création de problème", , 300, 300);//a combiner avec constr

				result = jDialog.showDialog();
				if (result == 1)//si pas annulé
					{
					showDialog(DIALOG.SET_MATRIX);
					}

				break;

			case SET_EQUATION:
				equationTemp = new Equation(equation);//copie de l'equ
				jDialog = new JDialogSetEquation(new JPanelSetEquation(equationTemp));

				result = jDialog.showDialog();

				if (result == 1)//si pas annulé
					{
					if (equationTemp.getMatrixNumberEquation() == equation.getMatrixNumberEquation() && equationTemp.getMatrixNumberVariable() == equation.getMatrixNumberVariable())
						{
						//Matrice de meme taille, on applique les changements
						equation = equationTemp;
						equationTemp = null;
						}
					else
						{
						//On doit entrer une nouvelle matrice
						showDialog(DIALOG.SET_MATRIX);
						}
					}
				break;

			case SET_MATRIX:
				Matrix matrixTemp;

				if (equationTemp == null)//Equation déjà résolue
					{
					matrixTemp = equation.getMatrix(0);
					}
				else					//Matrice en création
					{
					matrixTemp = new Matrix(equationTemp.getMatrixNumberEquation(), equationTemp.getMatrixNumberVariable() + 1);
					}


				jDialogMatrix = new JDialogSetMatrix(new JPanelSetMatrix(matrixTemp));//passer en temporaire

				result = jDialogMatrix.showDialog();
				if (result == 1)//Appliqué (annulé renvoi 0)
					{
					equationTemp.setMatrix(matrixTemp);
					equation = equationTemp;
					equationTemp = null;//termine la création

					if (equation.isStepMode())
						{
						equation.solve();
						changeView(PANEL.RESULT_STEP);
						}
					else
						{
						//equation.solve() //attention pas bonne méthode de res. directe
						changeView(PANEL.RESULT);
						}
					}
				else if (result == 2)//Précédent
					{
					showDialog(DIALOG.NEW_EQUATION);//showDialog(DIALOG.SET_EQUATION);
					}
				break;

			//			case RESULT_3D:
//							if ( controllerEquation.getNumberEquation() >= 1  && controllerEquation.getNumberEquation() <= 4
//							&& (controllerEquation.getNumberVar() == 2 || controllerEquation.getNumberVar() == 3) )
			//					{
			//					jpanel = new JPanel3D(equation.getMatrix(0));
			//					jDialogMain.setPanel("Vue 3D du système", jpanel);
			//					jDialogMain.setLocation(0, 0);
			//					jDialogMain.pack();
			//					}
			//				else
			//					{
			//					JOptionPane.showMessageDialog(jFrameMain, "Seuls les systèmes de 1-4 équation(s) comprenant 2 à 3 inconnues, peuvent être affichés graphiquement.", "", JOptionPane.WARNING_MESSAGE);
			//					}
			}
		}

	public void save()
		{
		JFileChooser jfilechooser = new JFileChooser();
		jfilechooser.setDialogTitle("Choisissez un emplacement de sauvegarde");
		jfilechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		//jfilechooser.setSelectedFile(controller)
		jfilechooser.setFileFilter(new FileNameExtensionFilter("NSolver extension", "nso"));
		int returnOption = jfilechooser.showSaveDialog(jFrameMain);

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
			JOptionPane.showMessageDialog(jFrameMain, "Error while saving file : " + jfilechooser.getSelectedFile().getAbsolutePath(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			}
		}

	public void load()
		{
		JFileChooser jfilechooser = new JFileChooser();
		jfilechooser.setDialogTitle("Choisissez un fichier à charger");
		jfilechooser.setFileFilter(new FileNameExtensionFilter("NSolver extension", "nso"));
		int returnOption = jfilechooser.showOpenDialog(jFrameMain);

		if (returnOption == JFileChooser.APPROVE_OPTION)
			{
			try
				{
				equation = controllerIO.load(jfilechooser.getSelectedFile());
				equation.solve();
				//				controllerEquation.setEquation(equation);
				//				controllerEquation.applyTempEquation();
				//				controllerEquation.reInitMatrix();
				//				controllerEquation.solveEquation();
				changeView(PANEL.RESULT);
				}
			catch (ClassNotFoundException e)
				{
				JOptionPane.showMessageDialog(jFrameMain, "Didn't found \"Equation.class\".", e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				}
			catch (IOException e)
				{
				JOptionPane.showMessageDialog(jFrameMain, "Error while loading file : " + jfilechooser.getSelectedFile().getAbsolutePath(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				}
			}
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/
	//Tools
	private Equation equation;
	private Equation equationTemp;

	private ControllerIO controllerIO;

	//	private boolean isCreating;

	//Vues
	private JFrameMain jFrameMain;
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
		NEW_EQUATION, SET_EQUATION, SET_MATRIX, RESULT_3D
		}

	}
