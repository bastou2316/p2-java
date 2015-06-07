
package ch.hearc.p2.java.view.jpanel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

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
	|*							Methodes Jframe 						*|
	\*------------------------------------------------------------------*/

	private void geometry()
		{
		// JComponent : Instanciation
		buttonStart = new JButton("Start");
		buttonStop = new JButton("Stop");
		buttonNext = new JButton("Suivant");
		buttonPrevious = new JButton("Précédent");

		//boutonStart.setSize(100, 40);
		//boutonStop.setSize(100, 40);

		//boutonStart.setLocation(10, 100);
		//boutonStop.setLocation(10, 300);

		buttonStart.setEnabled(true);
		buttonStop.setEnabled(false);
		buttonNext.setEnabled(true);
		buttonPrevious.setEnabled(true);

			// Layout : Specification
			{
			FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
			setLayout(flowlayout);

			flowlayout.setHgap(20);
			// flowlayout.setVgap(20);
			}

		// JComponent : add
		add(buttonStart);
		add(buttonStop);
		add(buttonNext);
		add(buttonPrevious);

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

		buttonStart.addActionListener(actionListener);
		buttonStop.addActionListener(actionListener);

		buttonStart.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					start();
					}
			});

		buttonStop.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					stop();
					}
			});

		buttonNext.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					next();
					}
			});

		buttonPrevious.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					previous();
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
		switchEtatBouton(buttonStart);
		switchEtatBouton(buttonStop);
		switchEtatBouton(buttonNext);
		switchEtatBouton(buttonPrevious);
		}

	private void start()
		{
		controlable.start();
		}

	private void stop()
		{
		controlable.stop();
		}

	private void next()
		{
		controlable.next();
		}

	private void previous()
		{
		controlable.previous();
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
	private JButton buttonStart;
	private JButton buttonStop;
	private JButton buttonNext;
	private JButton buttonPrevious;

	// Input
	private Control_I controlable;

	}
