
package ch.hearc.p2.java.view;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;

import ch.hearc.p2.java.controller.ControllerEquation;
import ch.hearc.p2.java.view.jpanel.JPanelControl;
import ch.hearc.p2.java.view.jpanel.JPanelMenu;
import ch.hearc.p2.java.view.jpanel.JPanelResult;

public class JFrameMain extends JFrame
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JFrameMain()
		{
		super();
		controllerEquation = new ControllerEquation();

		geometry();
		control();
		appearance();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	protected void geometry()
		{
		// Layout : Specification
		BorderLayout borderLayout = new BorderLayout();
		setLayout(borderLayout);

		// JComponent : Instanciation
		//showView(PANEL.MENU);
		showView(PANEL.RESULT);
		}

	protected void control()
		{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		}

	protected void appearance()
		{
		setSize(600, 400);
		setLocationRelativeTo(null); // frame centrer
		setVisible(true); // last!
		}

	//Affichage de la
	public void showView(PANEL panel)
		{

		if(actualPanel != panel || actualPanel == null) {
			actualPanel = panel;
			switch(panel)
				{
				case MENU:
					add(new JPanelMenu(this), BorderLayout.CENTER);
					break;
				case RESULT:
					jPanelResolution = new JPanelResult(controllerEquation);
					jPanelControl = new JPanelControl(jPanelResolution);

					add(jPanelResolution, BorderLayout.CENTER);
					add(jPanelControl, BorderLayout.SOUTH);

					JDialog dialog = new JDialog(this, "Hello");
					dialog.setLocationRelativeTo(this);
					dialog.add(new JPanelControl(jPanelResolution));
					dialog.pack();
					dialog.setVisible(true);

					break;
				case RESULT_STEP:
					break;

				default:
					System.out.println("Error, panel does not exist. Try : PANEL.*");
					break;
				}
			}
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	JPanelResult jPanelResolution;
	JPanelControl jPanelControl;
	ControllerEquation controllerEquation;

	PANEL actualPanel;

	enum PANEL
		{
		MENU, RESULT, RESULT_STEP, SET_EQUATION, SET_MATRIX
		}

	}
