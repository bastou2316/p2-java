
package ch.hearc.p2.java.view.jpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import ch.hearc.p2.java.controller.ControllerEquation;
import ch.hearc.p2.java.tools.ListAction;

public class JPanelResultStep extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelResultStep(ControllerEquation controllerEquation)
		{
		this.controllerEquation = controllerEquation;
		List<String> listHistory = controllerEquation.getEquation().getOperations();
		//Collections.reverse(listHistory);
		tabString = new String[listHistory.size()];
		tabString = listHistory.toArray(tabString);

		//Composition du panel
		geometry();
		control();
		appearance();

		//Instanciation des variables threads
		isFini = false;
		isRunning = false;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public synchronized void start()
		{
		if (!isRunning)
			{
			isFini = false;
			isRunning = true;
			updateDisplay();
			thread = new Thread(new Runnable()
				{

					@Override
					public void run()
						{
//						Matrix matrix = null;
						while(!isFini && controllerEquation.hasNextMatrix())
							{
							controllerEquation.getNextMatrix();
							updateDisplay();
//							textMatrix.setText(matrix.toString());//
//							graphicListHistory.setSelectedIndex(controllerEquation.getCurrentStep());
							sleep(controllerEquation.getSpeed());
							}
						isRunning = false;
						stop();
						}
				});
			thread.start();
			}
		}

	public synchronized void stop()
		{
		updateDisplay();
		isFini = true;
		thread = null;
		}

	public void next()
		{
		controllerEquation.getNextMatrix();
		updateDisplay();
		}

	public void previous()
		{
		controllerEquation.getPreviousMatrix();
		updateDisplay();
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

	private void updateDisplay()
		{
		int currentStep = controllerEquation.getCurrentStep();

		textMatrix.setText(controllerEquation.getCurrentMatrix().toString());
		graphicListHistory.setSelectedIndex(currentStep);

		graphicListHistory.setEnabled(!isRunning);
		buttonStart.setEnabled(currentStep < tabString.length-1 && !isRunning);
		buttonStop.setEnabled(isRunning);
		buttonNext.setEnabled(currentStep < tabString.length-1 && !isRunning);
		buttonPrevious.setEnabled(currentStep > 0 && !isRunning);
		}

	private void sleep(long delayMS)
		{
		try
			{
			Thread.sleep(delayMS);
			}
		catch (InterruptedException e)
			{
			e.printStackTrace();
			}
		}

	private void geometry()
		{
		// JComponent : Instanciation
		JPanel jPanelCenter = new JPanel();

		JPanel jPanelOperation = new JPanel();
		TitledBorder titlesborder = BorderFactory.createTitledBorder("Opérations");
		titlesborder.setTitleColor(Color.blue);
		jPanelOperation.setBorder(titlesborder);
		jPanelOperation.setForeground(Color.blue);

		JPanel jPanelMatrix = new JPanel();
		TitledBorder titlematrix = BorderFactory.createTitledBorder("Matrice");
		titlematrix.setTitleColor(Color.blue);
		jPanelMatrix.setBorder(titlematrix);
		jPanelMatrix.setForeground(Color.blue);

		JPanel jPanelButtons = new JPanel();

		buttonStart = new JButton("Start");
		buttonStop = new JButton("Stop");
		buttonNext = new JButton("Suivant");
		buttonPrevious = new JButton("Précédent");

		buttonStart.setEnabled(true);
		buttonStop.setEnabled(false);
		buttonNext.setEnabled(true);
		buttonPrevious.setEnabled(true);

		textMatrix = new JTextArea();
		textMatrix.setLineWrap(true);
		textMatrix.setEditable(false);
		textMatrix.setText(controllerEquation.getMatrix(0).toString());

		graphicListHistory = new JList<String>(tabString);
		graphicListHistory.setVisibleRowCount(5);
		graphicListHistory.setSelectedIndex(0);
		scrollPaneList = new JScrollPane();
		scrollPaneList.setViewportView(graphicListHistory);

			// Layout : Specification
			{
			setLayout(new BorderLayout());
			jPanelCenter.setLayout(new GridLayout(1, 0, 0, 0));
			jPanelOperation.setLayout(new BorderLayout(0, 0));
			jPanelMatrix.setLayout(new BorderLayout(0, 0));
			jPanelButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
			}

		// JComponent : add
		jPanelMatrix.add(textMatrix, BorderLayout.CENTER);
		jPanelOperation.add(scrollPaneList);

		jPanelCenter.add(jPanelOperation, BorderLayout.WEST);
		jPanelCenter.add(jPanelMatrix, BorderLayout.EAST);

		jPanelButtons.add(buttonStart);
		jPanelButtons.add(buttonStop);
		jPanelButtons.add(buttonPrevious);
		jPanelButtons.add(buttonNext);

		add(jPanelCenter, BorderLayout.CENTER);
		add(jPanelButtons, BorderLayout.SOUTH);
		}

	private void control()
		{
		Action changeAction = new AbstractAction()
			{

				@SuppressWarnings("unchecked")
				@Override
				public void actionPerformed(ActionEvent e)
					{
					JList<String> list = (JList<String>)e.getSource();
					controllerEquation.getMatrix(list.getSelectedIndex());
					//System.out.println(idSelected);
					updateDisplay();
					}
			};
		new ListAction(graphicListHistory, changeAction);

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
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	private Thread thread;
	private boolean isRunning;
	private boolean isFini;

	ControllerEquation controllerEquation;
	private String[] tabString;

	private JButton buttonStart;
	private JButton buttonStop;
	private JButton buttonNext;
	private JButton buttonPrevious;
	private JScrollPane scrollPaneList;
	private JList<String> graphicListHistory;
	private JTextArea textMatrix;
	}
