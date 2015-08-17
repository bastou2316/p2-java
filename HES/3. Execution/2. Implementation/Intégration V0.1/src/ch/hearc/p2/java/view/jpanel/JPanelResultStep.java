
package ch.hearc.p2.java.view.jpanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
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

		updateDisplay();
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
						while(!isFini && controllerEquation.hasNextMatrix())
							{
							controllerEquation.getNextMatrix();
							updateDisplay();
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
		int currentStep = controllerEquation.getCurrentStep();
		if (currentStep < tabString.length-2)
			{
			controllerEquation.getNextMatrix();
			controllerEquation.setIsFinalStep(false);
			}
		else
			{
			controllerEquation.setIsFinalStep(true);
			}
		updateDisplay();
		}

	public void previous()
		{
		if(controllerEquation.isFinalStep())
			{
				controllerEquation.getCurrentMatrix();
				}
		else
			{
				controllerEquation.getPreviousMatrix();
				}

		controllerEquation.setIsFinalStep(false);
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
		if (!controllerEquation.isFinalStep())
			{
			textMatrix.setText(controllerEquation.getCurrentMatrix().toString());
			graphicListHistory.setSelectedIndex(currentStep);
			}
		else
			{
			controllerEquation.getCurrentMatrix().setVariableName(controllerEquation.getEquation().getVariableName());
			textMatrix.setText(controllerEquation.getCurrentMatrix().showResult());
			graphicListHistory.setSelectedIndex(currentStep+1);
			}
		graphicListHistory.setEnabled(!isRunning);
		buttonStart.setEnabled(currentStep < tabString.length-1 && !isRunning && !controllerEquation.isFinalStep());
		buttonStop.setEnabled(isRunning);
		buttonNext.setEnabled(currentStep < tabString.length-1 && !isRunning && !controllerEquation.isFinalStep());
		buttonPrevious.setEnabled(currentStep > 0 && !isRunning);
		if(controllerEquation.isFinalStep())
			buttonPrevious.setEnabled(true);
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
		//		JPanel jPanelCenter = new JPanel();

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		JPanel jPanelOperation = new JPanel();
		TitledBorder titlesborder = BorderFactory.createTitledBorder("Opérations");
		jPanelOperation.setBorder(titlesborder);

		JPanel jPanelMatrix = new JPanel();
		TitledBorder titlematrix = BorderFactory.createTitledBorder("Matrice");
		jPanelMatrix.setBorder(titlematrix);

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
		textMatrix.setEditable(false);
		textMatrix.setText(controllerEquation.getMatrix(0).toString());
		//textMatrix.setColumns(controllerEquation.getEquation().getMatrixNumberVariable());
		textMatrix.setRows(controllerEquation.getEquation().getMatrixNumberEquation());

		actualTextSize = textMatrix.getFont().getSize();

		//textMatrix.setMinimumSize(new Dimension(10, 10));

		JScrollPane scrollPaneMatrix = new JScrollPane(textMatrix, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneMatrix.setMinimumSize(new Dimension(10, 10));
		//scrollPaneMatrix.getHorizontalScrollBar().adda
		//scrollPaneMatrix.setEnabled(true);

		//		textPaneMatrix = new JTextPane();
		//		textPaneMatrix.setEditable(false);
		//		textPaneMatrix.setMinimumSize(new Dimension(10, 10));
		//		StyledDocument doc = textPaneMatrix.getStyledDocument();
		//		MutableAttributeSet center = new SimpleAttributeSet();
		//		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		//		doc.setParagraphAttributes(0, 0, center, true);
		//		textPaneMatrix.setText(controllerEquation.getMatrix(0).toString());

		graphicListHistory = new JList<String>(tabString);
		graphicListHistory.setVisibleRowCount(5);
		graphicListHistory.setSelectedIndex(0);
		scrollPaneList = new JScrollPane();
		scrollPaneList.setViewportView(graphicListHistory);

			// Layout : Specification
			{
			setLayout(new BorderLayout());
			jPanelOperation.setLayout(new BorderLayout(0, 0));
			jPanelMatrix.setLayout(new BorderLayout(0, 0));
			jPanelButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
			}

		// JComponent : add
		//scrollPaneMatrix.setViewportView(textMatrix);
		jPanelMatrix.add(scrollPaneMatrix, BorderLayout.CENTER);
		jPanelOperation.add(scrollPaneList);

		splitPane.add(jPanelOperation, JSplitPane.LEFT);
		splitPane.add(jPanelMatrix, JSplitPane.RIGHT);

		jPanelButtons.add(buttonStart);
		jPanelButtons.add(buttonStop);
		jPanelButtons.add(buttonPrevious);
		jPanelButtons.add(buttonNext);

		add(splitPane, BorderLayout.CENTER);
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
				if (list.getSelectedIndex() < tabString.length-1)
					{
				controllerEquation.getMatrix(list.getSelectedIndex());
				controllerEquation.setIsFinalStep(false);
					}
				if(list.getSelectedIndex()==tabString.length-1)
					{
						controllerEquation.setIsFinalStep(true);
						}
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

	textMatrix.addMouseWheelListener(new MouseWheelListener()
		{

			@Override
			public void mouseWheelMoved(MouseWheelEvent e)
				{
				if (actualTextSize > 0)
					{
					actualTextSize += e.getWheelRotation();
					//System.out.println(actualTextSize);
					textMatrix.setFont(new Font("Sans-Serif", Font.PLAIN, actualTextSize));
					}
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

private int actualTextSize;
//	private JTextPane textPaneMatrix;
}

