
package ch.hearc.p2.java.view.jpanel;

import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import ch.hearc.p2.java.controleur.ControleurProblem;
import ch.hearc.p2.java.view.jframe.Control_I;

public class JPanelResolution extends JPanel implements Control_I
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelResolution(ControleurProblem controleurProblem)
		{
		this.controleurProblem = controleurProblem;

		//Composition du panel
		geometry();
		control();
		appearance();

		//Instanciation des threads
		isFini = false;
		isRunning = false;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	@Override
	public synchronized void start()
		{
		if (!isRunning)
			{
			isRunning = true;
			thread = new Thread(new Runnable()
				{

					@Override
					public void run()
						{
						String text;
						while(!isFini)
							{
							text = nextStep();
							textArea.append(text+"\n");
							sleep(controleurProblem.getSpeed());
							}
						isFini = false;
						isRunning = false;
						}
				});
			thread.start();
			}
		}

	@Override
	public synchronized void stop()
		{
		isFini = true;
		thread = null;
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private String nextStep()
		{
		return controleurProblem.nextStep();
		}

	private void sleep(long delayMS)
		{
		try
			{
			Thread.sleep(delayMS);
			}
		catch (InterruptedException e)
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}

	private void geometry()
		{
		// JComponent : Instanciation
		textArea = new JTextArea("Debut\n");
		//textArea.setBounds(new Rectangle(getBounds()));
		textArea.setLineWrap(true);

			// Layout : Specification
			{
			FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
			setLayout(flowlayout);

			// flowlayout.setHgap(20);
			// flowlayout.setVgap(20);
			}

		// JComponent : add
		add(textArea);
		}

	private void control()
		{
		addComponentListener(new ComponentAdapter()
			{

				@Override
				public void componentResized(ComponentEvent e)
					{
					//Quand modif de la fenêtre
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

	// Tools
	private Thread thread;
	private boolean isRunning;
	private boolean isFini;

	JTextArea textArea;
	ControleurProblem controleurProblem;

	}
