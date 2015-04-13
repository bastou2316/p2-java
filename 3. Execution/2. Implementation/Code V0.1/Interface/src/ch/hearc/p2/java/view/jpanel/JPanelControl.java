
package ch.hearc.p2.java.view.jpanel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import ch.hearc.p2.java.view.jframe.Control_I;

public class JPanelControl extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelControl(Control_I controlable)
		{
		this.controlable = controlable;
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
		boutonStart = new JButton("Start");
		boutonStop = new JButton("Stop");

		boutonStart.setSize(100, 40);
		boutonStop.setSize(100, 40);

		boutonStart.setLocation(10, 100);
		boutonStop.setLocation(10, 300);

		boutonStart.setEnabled(true);
		boutonStop.setEnabled(false);

			// Layout : Specification
			{
			FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
			setLayout(flowlayout);

			flowlayout.setHgap(20);
			// flowlayout.setVgap(20);
			}

		// JComponent : add
		add(boutonStart);
		add(boutonStop);

		}

	private void control()
		{
		ActionListener actionListener = new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					switchEtatBouton();
					}
			};

		boutonStart.addActionListener(actionListener);
		boutonStop.addActionListener(actionListener);

		boutonStart.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					start();
					}
			});

		boutonStop.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					stop();
					}
			});

		}

	private void appearance()
		{
		// rien
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void switchEtatBouton()
		{
		switchEtatBouton(boutonStart);
		switchEtatBouton(boutonStop);
		}

	private void start()
		{
		controlable.start();
		}

	private void stop()
		{
		controlable.stop();
		}

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	private static void switchEtatBouton(JButton bouton)
		{
		bouton.setEnabled(!bouton.isEnabled());
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	private JButton boutonStart;
	private JButton boutonStop;

	// Input
	private Control_I controlable;

	}
