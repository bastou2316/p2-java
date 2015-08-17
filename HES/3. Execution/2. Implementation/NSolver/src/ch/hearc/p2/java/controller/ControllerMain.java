
package ch.hearc.p2.java.controller;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import ch.hearc.p2.java.model.Equation;
import ch.hearc.p2.java.view.JDialogMain;
import ch.hearc.p2.java.view.JFrameMain;
import ch.hearc.p2.java.view.jpanel.JPanelMenu;
import ch.hearc.p2.java.view.jpanel.JPanelResultStep;
import ch.hearc.p2.java.view.jpanel.JPanelSetEquation;
import ch.hearc.p2.java.view.jpanel.JPanelSetMatrix;
import ch.hearc.p2.java.view.jpanel3d.JPanel3D;

public class ControllerMain
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public ControllerMain()
		{
		super();
		this.controllerEquation = new ControllerEquation();
		this.controllerIO = new ControllerIO();

		this.jFrameMain = new JFrameMain(this);
		this.jDialogMain = new JDialogMain(this);

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
			lastFrame = panel;

			currentView = panel;
			JPanel jpanel;

			if (jDialogMain.isShowing())
				{
				jDialogMain.close();
				}

			switch(panel)
				{
				case MENU:
					jpanel = new JPanelMenu(this);
					jFrameMain.setPanel("Menu", jpanel);
					jFrameMain.enableAlternativeMenu(false);
					break;

				case RESULT:
					jpanel = new JPanelResultStep(controllerEquation);

					jFrameMain.setPanel("Résolution directe", jpanel, 600, 600);
					jFrameMain.enableAlternativeMenu(true);

					break;

				case RESULT_STEP:
					jpanel = new JPanelResultStep(controllerEquation);

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
		if (currentView != dialog)
			{
			currentView = dialog;
			JPanel jpanel;

			switch(dialog)
				{
				case SET_EQUATION:
					jpanel = new JPanelSetEquation(this, controllerEquation);
					jDialogMain.setPanel("Création de problème", jpanel, 300, 300);
					break;

				case SET_MATRIX:
					jpanel = new JPanelSetMatrix(this, controllerEquation);
					jDialogMain.setPanel("Remplissage de la matrice", jpanel, 500, 300);
					break;

				case RESULT_3D:
					if (controllerEquation.getNumberEquation() == 3 && controllerEquation.getNumberVar() == 3)
						{
						jpanel = new JPanel3D(controllerEquation.getMatrix(0));
						jDialogMain.setPanel("Vue 3D du système", jpanel);
						jDialogMain.setLocation(0, 0);
						jDialogMain.pack();
						}
					else
						{
						JOptionPane.showMessageDialog(jFrameMain, "Seules les matrices 3x3 (3x4 avec les réponses) peuvent être affichés graphiquement.", "", JOptionPane.WARNING_MESSAGE);
						}
				}

			if (!jDialogMain.isShowing())
				{
				jDialogMain.setVisible(true);
				}
			}
		}

	public void closeDialog()
		{
		changeView(lastFrame);
		}

	public void createNewEquation()
		{
		//Sauvegarde de l'équation actuelle
		//TODO: JBox demande de sauvegarde
		//save();

		//Création de l'équation
		controllerEquation.setCreating(true);
		showDialog(DIALOG.SET_EQUATION);
		}

	public void stopCreating()
		{
		//Equation temporaire non appliquée
		controllerEquation.setCreating(false);
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
				controllerIO.save(controllerEquation.getEquation(), jfilechooser.getSelectedFile());
				controllerEquation.getEquation().setSaved();
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
				Equation equation = controllerIO.load(jfilechooser.getSelectedFile());
				controllerEquation.setEquation(equation);
				controllerEquation.applyTempEquation();
				controllerEquation.reInitMatrix();
				controllerEquation.solveEquation();
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
	private ControllerEquation controllerEquation;
	private ControllerIO controllerIO;

	private JFrameMain jFrameMain;
	private JDialogMain jDialogMain;

	private VIEW currentView;
	private PANEL lastFrame;

	private interface VIEW
		{
		}

	public enum PANEL implements VIEW
		{
		MENU, RESULT, RESULT_STEP
		}

	public enum DIALOG implements VIEW
		{
		SET_EQUATION, SET_MATRIX, RESULT_3D
		}

	}
