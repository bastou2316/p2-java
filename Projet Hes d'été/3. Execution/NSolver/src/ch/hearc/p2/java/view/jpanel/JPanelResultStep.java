
package ch.hearc.p2.java.view.jpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import ch.hearc.p2.java.model.Equation;
import ch.hearc.p2.java.model.Matrix;
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

	private void changeFont(Component component, int fontSize)
		{
		Font f = component.getFont();
		component.setFont(new Font(f.getName(), f.getStyle(), f.getSize() + fontSize));
		if (component instanceof Container)
			{
			for(Component child:((Container)component).getComponents())
				{
				changeFont(child, fontSize);
				}
			}
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
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		JPanel jPanelOperation = new JPanel();
		TitledBorder titlesborder = BorderFactory.createTitledBorder("Opérations");
		jPanelOperation.setBorder(titlesborder);
		jPanelOperation.setMinimumSize(new Dimension(100, 0));

		JPanel jPanelMatrix = new JPanel();
		TitledBorder titlematrix = BorderFactory.createTitledBorder("Matrice");
		jPanelMatrix.setBorder(titlematrix);

		jPanelPreviousStep = new JPanel();
		TitledBorder titlePreviousStep = BorderFactory.createTitledBorder("Previous step");
		jPanelPreviousStep.setBorder(titlePreviousStep);

		JPanel jPanelButtons = new JPanel();

		buttonStart = new JButton("Start");
		buttonStop = new JButton("Stop");
		buttonNext = new JButton("Suivant");
		buttonPrevious = new JButton("Précédent");

		buttonStart.setEnabled(true);
		buttonStop.setEnabled(false);
		buttonNext.setEnabled(true);
		buttonPrevious.setEnabled(true);

		panelMatrix = new JPanelMatrix(equation.getMatrixNumberEquation(), equation.getVariableStyle());
		panelMatrixPrev = new JPanelMatrix(equation.getMatrixNumberEquation(), equation.getVariableStyle());

		JScrollPane scrollPaneMatrix = new JScrollPane(panelMatrix, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneMatrix.setMinimumSize(new Dimension(10, 10));

		JScrollPane scrollPanePrevious = new JScrollPane(panelMatrixPrev, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPanePrevious.setMinimumSize(new Dimension(10, 10));
		//scrollPaneMatrix.getHorizontalScrollBar().adda
		//scrollPaneMatrix.setEnabled(true);

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
			jPanelPreviousStep.setLayout(new BorderLayout(0, 0));
			jPanelButtons.setLayout(new FlowLayout(FlowLayout.CENTER));
			}

		// JComponent : add
		jPanelMatrix.add(scrollPaneMatrix, BorderLayout.CENTER);
		jPanelPreviousStep.add(scrollPanePrevious, BorderLayout.CENTER);
		jPanelOperation.add(scrollPaneList);

		Box boxV = Box.createVerticalBox();
		boxV.add(jPanelPreviousStep);
		boxV.add(jPanelMatrix);

		JPanel panelLeft = new JPanel();
		panelLeft.setLayout(new BorderLayout());
		panelLeft.add(jPanelOperation, BorderLayout.CENTER);
		Box boxcomb = Box.createVerticalBox();
		cbxVar = new JCheckBox("Affichage des variables");
		cbxVar.setSelected(true);
		cbxSpace = new JCheckBox("Alignement");
		cbxSpace.setSelected(false);
		boxcomb.add(cbxVar);
		boxcomb.add(cbxSpace);
		panelLeft.add(boxcomb, BorderLayout.SOUTH);

		splitPane.add(panelLeft, JSplitPane.LEFT);
		splitPane.add(boxV, JSplitPane.RIGHT);

		jPanelButtons.add(buttonStart);
		jPanelButtons.add(buttonStop);
		jPanelButtons.add(buttonPrevious);
		jPanelButtons.add(buttonNext);

		add(splitPane, BorderLayout.CENTER);
		add(jPanelButtons, BorderLayout.SOUTH);

		updateDisplayedMatrix();
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
					JList<String> list = (JList<String>)e.getSource();
					actualStep = list.getSelectedIndex();

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


		cbxSpace.addItemListener(new ItemListener()
			{

				@Override
				public void itemStateChanged(ItemEvent e)
					{
					panelMatrix.setSpaceDisplay(cbxSpace.isSelected());
					panelMatrixPrev.setSpaceDisplay(cbxSpace.isSelected());
					updateDisplayedMatrix();
					}
			});


		cbxVar.addItemListener(new ItemListener()
			{

				@Override
				public void itemStateChanged(ItemEvent e)
					{
					panelMatrix.setVariableDisplay(cbxVar.isSelected());
					panelMatrixPrev.setVariableDisplay(cbxVar.isSelected());
					updateDisplayedMatrix();
					}
			});

		//Zoom
		panelMatrix.addMouseWheelListener(new MouseWheelListener()
			{

				@Override
				public void mouseWheelMoved(MouseWheelEvent e)
					{
					updateZoom(e);
					}
			});

		panelMatrixPrev.addMouseWheelListener(new MouseWheelListener()
			{

				@Override
				public void mouseWheelMoved(MouseWheelEvent e)
					{
					updateZoom(e);
					}
			});

		MouseWheelListener zoom = new MouseWheelListener()
			{

				@Override
				public void mouseWheelMoved(MouseWheelEvent e)
					{
					changeFont(panelMatrix, e.getWheelRotation());
					changeFont(panelMatrixPrev, e.getWheelRotation());

					}
			};

		panelMatrix.addMouseWheelListener(zoom);
		panelMatrixPrev.addMouseWheelListener(zoom);
		scrollPaneList.addMouseWheelListener(new MouseWheelListener()
			{

				@Override
				public void mouseWheelMoved(MouseWheelEvent e)
					{
					changeFont(scrollPaneList, e.getWheelRotation());

					}
			});
		}

	private void appearance()
		{
		// rien
		}

	private void updateZoom(MouseWheelEvent e)
		{
		actualTextSize += e.getWheelRotation();
		panelMatrix.setFont(new Font("Sans-Serif", Font.PLAIN, actualTextSize));
		panelMatrixPrev.setFont(new Font("Sans-Serif", Font.PLAIN, actualTextSize));
		}

	private void updateDisplayedMatrix()
		{
		//Affichage de la matrix en fonction de l'étape
		Set<Integer> setDifference = null;
		boolean notFirst = actualStep > 0;
		boolean notLast = actualStep < tabHistory.length - 1;

		//Maj des labels en fonc de la matrix et de l'étape
		if (notFirst)
			{
			if (notLast)//pas de difference vers texte de solution
				{
				setDifference = Matrix.isEquals(equation.getMatrix(actualStep), equation.getMatrix(actualStep - 1));//Comparaison des lignes de la matrix
				}
			panelMatrixPrev.updateLabels(equation.getMatrix(actualStep - 1), null);//matrix précédente affichage slmt a partir de étape "2"
			}

		if (notLast)
			{
			panelMatrix.updateLabels(equation.getMatrix(actualStep), setDifference);
			}
		else
			{
			panelMatrix.updateSolution(equation.getSolution());
			}
		jPanelPreviousStep.setVisible(notFirst);//&& notLast

		graphicListHistory.setSelectedIndex(actualStep);

		//Blocage et libération des boutons
		graphicListHistory.setEnabled(!isRunning);
		buttonStart.setEnabled(actualStep < tabHistory.length - 1 && !isRunning);
		buttonStop.setEnabled(isRunning);
		buttonNext.setEnabled(actualStep < tabHistory.length - 1 && !isRunning);
		buttonPrevious.setEnabled(actualStep > 0 && !isRunning);
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
	private JPanelMatrix panelMatrix, panelMatrixPrev;
	private JPanel jPanelPreviousStep;
	private JCheckBox cbxVar;
	private JCheckBox cbxSpace;

	private int actualTextSize;
	}
