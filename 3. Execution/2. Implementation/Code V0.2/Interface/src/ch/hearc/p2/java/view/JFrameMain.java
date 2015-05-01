
package ch.hearc.p2.java.view;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ch.hearc.p2.java.controller.ControllerEquation;
import ch.hearc.p2.java.view.jpanel.JPanelControl;
import ch.hearc.p2.java.view.jpanel.JPanelMenu;
import ch.hearc.p2.java.view.jpanel.JPanelResultDirect;
import ch.hearc.p2.java.view.jpanel.JPanelResultStep;
import ch.hearc.p2.java.view.jpanel.JPanelSetEquation;

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
		jPanelMenu = new JPanelMenu(this);
		jPanelSetEquation = new JPanelSetEquation(this, controllerEquation);
		
		
	
		
		// Layout : Specification
		BorderLayout borderLayout = new BorderLayout();
		setLayout(borderLayout);

		// JComponent : Instanciation
		add(jPanelMenu, BorderLayout.CENTER);
		
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
					setSize(600, 400);
					setLocationRelativeTo(null);
					this.setContentPane(jPanelMenu);//					
					revalidate();
					break;
				case SET_EQUATION:					
					this.setSize(1100, 400);
					setLocationRelativeTo(null);
					this.setContentPane(jPanelSetEquation);					
					revalidate();
					break;
				case RESULT:
					this.setSize(600, 600);
					setLocationRelativeTo(null);
					this.setTitle("Résolution directe");
					jPanelResultDirect = new JPanelResultDirect(this, controllerEquation);
					
					this.setContentPane(jPanelResultDirect);					
					revalidate();
					

//					JDialog dialog = new JDialog(this, "Hello");
//					dialog.setLocationRelativeTo(this);
//					dialog.add(new JPanelControl(jPanelResolution));
//					dialog.pack();
//					dialog.setVisible(true);		
					break;
				case RESULT_STEP:
					this.setSize(600, 600);
					this.setTitle("Résolution étape par étape");
					jPanelResultStep = new JPanelResultStep(controllerEquation);
					jPanelControl = new JPanelControl(jPanelResultStep);
					
					JPanel mainPanel = new JPanel(new BorderLayout());
					mainPanel.add(jPanelResultStep, BorderLayout.CENTER);
					mainPanel.add(jPanelControl, BorderLayout.SOUTH);
					this.setContentPane(mainPanel);					
					revalidate();
					
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
	private JPanelMenu jPanelMenu;
	private JPanelSetEquation jPanelSetEquation;
	private JPanelResultDirect jPanelResultDirect;
	private JPanelControl jPanelControl;
	
	private JPanelResultStep jPanelResultStep;
	private ControllerEquation controllerEquation;

	private PANEL actualPanel;

	public enum PANEL
		{
		MENU, RESULT, RESULT_STEP, SET_EQUATION, SET_MATRIX
		}

	}
