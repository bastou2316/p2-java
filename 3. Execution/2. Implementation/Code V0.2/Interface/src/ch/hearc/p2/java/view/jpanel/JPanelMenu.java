package ch.hearc.p2.java.view.jpanel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.hearc.p2.java.view.JFrameMain;
import ch.hearc.p2.java.view.strings.Strings;

public class JPanelMenu extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelMenu(JFrameMain jFrameMain)
		{
		this.jFrameMain = jFrameMain;

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

	private void control()
		{
		buttonLoad.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					//jFrameMain.showView(JFrameMain.PANEL.RESULT);
					}
			});
		}

	private void appearance()
		{
		// rien
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/
	//Inputs
	JFrameMain jFrameMain;

	// Tools
	JLabel labelWelcome;
	JLabel labelNew;
	JLabel labelLoad;
	JButton buttonNew;
	JButton buttonLoad;
	}
