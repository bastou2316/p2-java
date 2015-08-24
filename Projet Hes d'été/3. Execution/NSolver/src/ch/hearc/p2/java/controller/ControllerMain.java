
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
			JPanel jpanel;

			switch(panel)
				{
				case MENU:
					jpanel = new JPanelMenu(this);
					jFrame.setPanel("Menu", jpanel);
					jFrame.enableAlternativeMenu(false);
					break;

				case RESULT:
					jpanel = new JPanelResultStep(equation);
					jFrame.setPanel("Résolution directe", jpanel, 600, 600);
					jFrame.enableAlternativeMenu(true);
					break;

				case RESULT_STEP:
					jpanel = new JPanelResultStep(equation);
					jFrame.setPanel("Résolution étape par étape", jpanel, 600, 600);
					jFrame.pack();
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

		JDialogMain jDialog;
		int result;

		Matrix matrixTemp;

		switch(dialog)
			{
			case NEW_EQUATION:
				//En cas d'annulation de remplissage, charge l'equation
				if (equationTemp == null)
					{
					equationTemp = new Equation();
					}

				jDialog = new JDialogSetEquation(new JPanelSetEquation(equationTemp));
				//jDialog.setPanel("Création de problème", , 300, 300);//a combiner avec constr

				//Récupération du résultat
				result = jDialog.showDialog();
				if (result == 1)//si pas annulé
					{
					showDialog(DIALOG.NEW_MATRIX);
					}

				break;

			case NEW_MATRIX:
				matrixTemp = new Matrix(equationTemp.getMatrixNumberEquation(), equationTemp.getMatrixNumberVariable() + 1);

				jDialog = new JDialogSetMatrix(new JPanelSetMatrix(matrixTemp));

				result = jDialog.showDialog();
				if (result == 1)//Appliqué (annulé renvoi 0)
					{
					equationTemp.setMatrix(matrixTemp);
					equation = equationTemp;
					equationTemp = null;

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
					showDialog(DIALOG.NEW_EQUATION);
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
				matrixTemp = equation.getMatrix(0);
				jDialog = new JDialogSetMatrix(new JPanelSetMatrix(matrixTemp));

				result = jDialog.showDialog();
				if (result == 1)//Appliqué (annulé renvoi 0)
					{
					equation.setMatrix(matrixTemp);

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
					//Rien dans ce cas
					}
				break;

			case RESULT_3D:
				if (equation.getMatrixNumberEquation() >= 1 && equation.getMatrixNumberEquation() <= 4 && (equation.getMatrixNumberVariable() == 2 || equation.getMatrixNumberVariable() == 3))
					{
					JDialog3D jDialogMain = new JDialog3D(new JPanel3D(equation.getMatrix(0)));
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

		//jfilechooser.setSelectedFile(controller)
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
				equation.solve();
				//				controllerEquation.setEquation(equation);
				//				controllerEquation.applyTempEquation();
				//				controllerEquation.reInitMatrix();
				//				controllerEquation.solveEquation();
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
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/
	//Tools
	private Equation equation;
	private Equation equationTemp;
	private ControllerIO controllerIO;

	//Vues
	private JFrameMain jFrame;
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
		NEW_EQUATION, SET_EQUATION, SET_MATRIX, RESULT_3D, NEW_MATRIX
		}

	}
