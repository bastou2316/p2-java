
package ch.hearc.p2.java.controller;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ch.hearc.p2.java.model.Equation;
import ch.hearc.p2.java.view.JDialogMain;
import ch.hearc.p2.java.view.JFrameMain;
import ch.hearc.p2.java.view.jpanel.JPanelControl;
import ch.hearc.p2.java.view.jpanel.JPanelMenu;
import ch.hearc.p2.java.view.jpanel.JPanelResultDirect;
import ch.hearc.p2.java.view.jpanel.JPanelResultStep;
import ch.hearc.p2.java.view.jpanel.JPanelSetEquation;

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

		this.jFrameMain = new JFrameMain();
		this.jDialogMain = new JDialogMain();

		changeView(PANEL.MENU);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	//Affichage de la vue appropriée
	public void changeView(PANEL panel)
		{

		if (currentPanel != panel || currentPanel == null)
			{
			currentPanel = panel;
			JPanel jpanel;

			switch(panel)
				{
				case MENU:
					jpanel = new JPanelMenu(this);
					jFrameMain.setPanel("Menu", jpanel);
					break;

				case SET_EQUATION:
					jpanel = new JPanelSetEquation(this, controllerEquation);
					jDialogMain.setPanelAndShow("Création de problème", jpanel, 300, 300);

					if (controllerEquation.getEquation() != null)
						{
						changeView(PANEL.SET_MATRIX);
						}
					break;

				case SET_MATRIX:
					//TODO panel
					//jpanel = new JPanelSetMatrix(this, controllerEquation);
					//jDialogMain.setPanelAndShow("Remplissage de la matrice", jpanel, 300, 300);

					if (controllerEquation.getStepMode())
						{
						changeView(PANEL.RESULT_STEP);
						}
					else
						{
						changeView(PANEL.RESULT);
						}

				case RESULT:
					jpanel = new JPanelResultDirect(this, controllerEquation);
					jFrameMain.setPanel("Résolution directe", jpanel, 600, 600);
					break;

				case RESULT_STEP:
					jpanel = new JPanel(new BorderLayout());
					JPanelResultStep jpanelResultStep = new JPanelResultStep(controllerEquation);
					JPanelControl jpanelControl = new JPanelControl(jpanelResultStep);

					jpanel.add(jpanelResultStep, BorderLayout.CENTER);
					jpanel.add(jpanelControl, BorderLayout.SOUTH);
					jFrameMain.setPanel("Résolution étape par étape", jpanel, 600, 600);
					break;

				default:
					System.out.println("Error, panel does not exist. Try : PANEL.*");
					break;
				}
			}
		}

	public void save()
		{
		JFileChooser jfilechooser = new JFileChooser();
		jfilechooser.setVisible(true);

		try
			{
			controllerIO.save(controllerEquation.getEquation(), jfilechooser.getSelectedFile());
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
		int returnOption = jfilechooser.showOpenDialog(jFrameMain);

		if (returnOption == JFileChooser.APPROVE_OPTION)
			{
			try
				{
				Equation equation = controllerIO.load(jfilechooser.getSelectedFile());
				controllerEquation.setEquation(equation);
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

	private PANEL currentPanel;

	public enum PANEL
		{
		MENU, RESULT, RESULT_STEP, SET_EQUATION, SET_MATRIX
		}

	}
