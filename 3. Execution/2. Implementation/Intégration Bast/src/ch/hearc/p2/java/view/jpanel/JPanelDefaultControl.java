package ch.hearc.p2.java.view.jpanel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import ch.hearc.p2.java.controller.ControllerEquation;
import ch.hearc.p2.java.controller.ControllerMain;
import ch.hearc.p2.java.controller.ControllerMain.DIALOG;

public class JPanelDefaultControl extends JPanel {
	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelDefaultControl(ControllerMain controllerMain, ControllerEquation controllerEquation) {
		this.controllerMain = controllerMain;
		this.controllerEquation = controllerEquation;

		// Composition du panel
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

	private void geometry() {
		// JComponent : Instanciation
		seeIn3D = new JButton("3D");
		seeIn3D.setToolTipText("Watch original matrix system in 3 Dimension representation");

		saveMatrix = new JButton("Save");
		saveMatrix.setToolTipText("Save matrix");

		backButton = new JButton("Back");
		backButton.setToolTipText("Return to previous window");

		// Layout : Specification
		{
			FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
			setLayout(flowlayout);

			// flowlayout.setHgap(20);
			// flowlayout.setVgap(20);
		}

		// JComponent : add
		add(seeIn3D);
		add(saveMatrix);
		add(backButton);
	}

	private void control() {
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				// Quand modif de la fenêtre
			}
		});
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controllerMain.showDialog(DIALOG.SET_EQUATION);
			}
		});
	}

	private void appearance() {

	}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private ControllerMain controllerMain;
	private ControllerEquation controllerEquation;

	// Tools

	private JButton seeIn3D, saveMatrix, backButton;

	private JPanelDefaultControl panelDefaultControl;

}
