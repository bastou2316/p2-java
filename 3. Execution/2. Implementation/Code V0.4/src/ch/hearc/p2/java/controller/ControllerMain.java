
package ch.hearc.p2.java.controller;

import java.awt.BorderLayout;

import javax.swing.JPanel;

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
						jFrameMain.setPanel("Création de problème", jpanel, 1100, 550);
						//jframeMain.setMaximumSize(new Dimension(1900, 680));//pas d'accès

						//Plutot sous forme de dialogue normalement
						//					JDialog dialog = new JDialog(this, "Hello");
						//					dialog.setLocationRelativeTo(this);
						//					dialog.add(new JPanelControl(jPanelResolution));
						//					dialog.pack();
						//					dialog.setVisible(true);
						break;
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

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/
	//

	//Tools
	private ControllerEquation controllerEquation;

	private JFrameMain jFrameMain;
	private JDialogMain jDialogMain;

	private PANEL currentPanel;
	public enum PANEL
		{
		MENU, RESULT, RESULT_STEP, SET_EQUATION
		}

	}
