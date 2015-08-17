
package ch.hearc.p2.java.view.jframe;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import ch.hearc.p2.java.controleur.ControleurProblem;
import ch.hearc.p2.java.model.Problem;
import ch.hearc.p2.java.view.jpanel.JPanelControl;
import ch.hearc.p2.java.view.jpanel.JPanelResolution;

public class JFrameResolution extends JFrame
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JFrameResolution(Problem problem)
		{
		controleurProblem = new ControleurProblem(problem);

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

	private void geometry()
		{
		// JComponent : Instanciation
		jPanelResolution = new JPanelResolution(controleurProblem);
		jPanelControl = new JPanelControl(jPanelResolution);

			// Layout : Specification
			{
			BorderLayout borderLayout = new BorderLayout();
			setLayout(borderLayout);

			// borderLayout.setHgap(20);
			// borderLayout.setVgap(20);
			}

		// JComponent : add
		add(jPanelResolution, BorderLayout.CENTER);
		add(jPanelControl, BorderLayout.NORTH);
		}

	private void control()
		{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		}

	private void appearance()
		{
		setSize(600, 400);
		setLocationRelativeTo(null); // frame centrer
		setVisible(true); // last!
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	JPanelResolution jPanelResolution;
	JPanelControl jPanelControl;
	ControleurProblem controleurProblem;

	}
