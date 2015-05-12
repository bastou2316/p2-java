
package view.jpanel;

import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import view.jframe.Control_I;
import view.jframe.ResolutionControl_I;
import controleur.ControleurProblem;

public class JPanelResolution extends JPanel implements Control_I, ResolutionControl_I
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
//		if (!isRunning)
//			{
//			isRunning = true;
//			thread = new Thread(new Runnable()
//				{
//
//					@Override
//					public void run()
//						{
						String text;
//						while(!isFini)
//							{

							controleurProblem.start();
							text = nextStep();
//							textArea.append(text+"\n");
							textArea.setText(text);
//							sleep(controleurProblem.getSpeed());
//							}
//						isFini = false;
//						isRunning = false;
//						}
//				});
//			thread.start();
//			}
		}

	@Override
	public synchronized void stop()
		{
		isFini = true;
		thread = null;
		textArea.setText("");
		controleurProblem.stop();

		}

	@Override
	public void suivant()
		{
		String text;
		text = nextStep();
		textArea.setText(text);
		}

	@Override
	public void precedent()
		{
		String text;
		text = previousStep();
		textArea.setText(text);

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

	private String previousStep()
		{
		return controleurProblem.previousStep();
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
					//Quand modif de la fen�tre
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