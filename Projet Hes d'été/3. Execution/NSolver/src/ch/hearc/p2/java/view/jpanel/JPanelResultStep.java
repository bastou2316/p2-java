
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

import ch.hearc.p2.java.model.Equation;
import ch.hearc.p2.java.tools.ListAction;

public class JPanelResultStep extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelResultStep(Equation equation)
		{
		this.equation = equation;
		this.actualStep = 0;

		List<String> listHistory = equation.getOperations();
		tabHistory = new String[listHistory.size()];//+2
		tabHistory = listHistory.toArray(tabHistory);

		//Composition du panel
		geometry();
		control();
		appearance();

		//Instanciation des variables threads
		isFini = false;
		isRunning = false;

		updateDisplayedMatrix();
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

			updateDisplayedMatrix();
			thread = new Thread(new Runnable()
				{
					@Override
					public void run()
						{
						while(!isFini && equation.hasMatrixIndex(actualStep))
							{
							equation.getMatrix(++actualStep);
							updateDisplayedMatrix();
							sleep(equation.getSpeedMs());
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
		updateDisplayedMatrix();
		isFini = true;
		thread = null;
		}

	public void next()
		{
		if (actualStep < tabHistory.length)// - 1)
			{
			++actualStep;
			}
		updateDisplayedMatrix();
		}

	public void previous()
		{
		if (actualStep > 0)
			{
			--actualStep;
			}
		updateDisplayedMatrix();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void updateDisplayedMatrix()
		{
		//Affichage de la matrix en fonction de l'étape
		textMatrix.setText(stepToString(equation.getMatrix(actualStep), equation.getMatrixNumberEquation(), equation.getMatrixNumberVariable()+1));
		graphicListHistory.setSelectedIndex(actualStep);

		//Blocage et libération des boutons
		graphicListHistory.setEnabled(!isRunning);
		buttonStart.setEnabled(actualStep < tabHistory.length - 1 && !isRunning);
		buttonStop.setEnabled(isRunning);
		buttonNext.setEnabled(actualStep < tabHistory.length - 1 && !isRunning);
		buttonPrevious.setEnabled(actualStep > 0 && !isRunning);
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
		// JPanel jPanelCenter = new JPanel();

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
		textMatrix.setText(equation.getMatrix(0).toString());
		//textMatrix.setColumns(controllerEquation.getEquation().getMatrixNumberVariable());
		textMatrix.setRows(equation.getMatrixNumberEquation());

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

		graphicListHistory = new JList<String>(tabHistory);
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
					//Récupération de l'étape sélectionnée
					JList<String> list = (JList<String>) e.getSource();
					actualStep = list.getSelectedIndex();
					//System.out.println(idSelected);

					//Maj de la page des résultats
					updateDisplayedMatrix();
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
//					if (actualTextSize > 0)
//						{
						actualTextSize += e.getWheelRotation();
						//System.out.println(actualTextSize);
						textMatrix.setFont(new Font("Sans-Serif", Font.PLAIN, actualTextSize));
//						}
					}
			});

		graphicListHistory.addMouseWheelListener(new MouseWheelListener()
			{

				@Override
				public void mouseWheelMoved(MouseWheelEvent e)
					{

					}
			});
		}

	private void appearance()
		{
		// rien
		}

	private static String stepToString(String[][] tabMatrix, int rowCount, int columnCount)
		{
		StringBuilder builder = new StringBuilder();
		int rows = rowCount;
		int cols = columnCount;
		for(int i = 0; i < rows; i++)
			{
			for(int j = 0; j < cols - 1; j++)
				{
				builder.append(tabMatrix[i][j]);
				builder.append("\t");
				}
			builder.append("= ");
			builder.append(tabMatrix[i][cols - 1]);
			builder.append(System.getProperty("line.separator"));
			}
		return builder.toString();
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private Equation equation;

	// Tools
	private Thread thread;
	private boolean isRunning;
	private boolean isFini;

	private int actualStep;
	private String[] tabHistory;

	// JComponent
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
