package ch.hearc.p2.java.view.jpanel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.hearc.p2.java.controller.ControllerMain;
import ch.hearc.p2.java.controller.ControllerMain.PANEL;
import ch.hearc.p2.java.view.strings.Strings;

public class JPanelMenu extends JPanel {

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelMenu(ControllerMain controllerMain) {
		this.controllerMain = controllerMain;

		geometry();
		control();
		appearance();
	}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void geometry() {
		// JComponent : Instanciation
		labelWelcome = new JLabel(Strings.WELCOME);
		labelNew = new JLabel(Strings.LABEL_NEW);
		labelLoad = new JLabel(Strings.LABEL_LOAD);
		buttonNew = new JButton(Strings.BUTTON_NEW);
		buttonLoad = new JButton(Strings.BUTTON_LOAD);

		// Layout : Specification
		{
			FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
			setLayout(flowlayout);

			// flowlayout.setHgap(20);
			// flowlayout.setVgap(20);
		}

		// JComponent : add
		add(labelWelcome);
		add(labelNew);
		add(buttonNew);
		add(labelLoad);
		add(buttonLoad);

	}

	private void control() {
		buttonNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controllerMain.changeView(PANEL.SET_EQUATION);
			}
		});

		buttonLoad.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();

				int returnVal = fileChooser.showOpenDialog(JPanelMenu.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("You chose to open this file: "
							+ fileChooser.getSelectedFile().getName());
				}

				//Utiliser une classe ProjectSettings (serealizable) pour sauver et charger les projets en format .bin

//				jFrameMain.showView(PANEL.SET_EQUATION, loadedProjectSettings);
//				redéfinir showView et faire un 2e constructeur dans JPanelSetEquation (qu'on pourrait appeler setProject)
				//KV : a faire dans le controlleur via le controlleurIO
			}
		});
	}

	private void appearance() {
		// rien
	}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/
	// Inputs
	ControllerMain controllerMain;

	// Tools
	JLabel labelWelcome;
	JLabel labelNew;
	JLabel labelLoad;
	JButton buttonNew;
	JButton buttonLoad;
}
