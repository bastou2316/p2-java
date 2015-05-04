
package view.jframe;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import model.Equation;
import view.jpanel.JPanelControl;
import view.jpanel.JPanelResolution;
import view.jpanel.JPanelResolutionControl;
import controleur.ControleurProblem;

public class JFrameResolution extends JFrame
	{
	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JFrameResolution(Equation problem)
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
		jPanelResolutionControl = new JPanelResolutionControl(jPanelResolution);
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
		add(jPanelResolutionControl, BorderLayout.SOUTH);
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
	JPanelResolutionControl jPanelResolutionControl;
	ControleurProblem controleurProblem;


	}
