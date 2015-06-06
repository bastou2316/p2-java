
package ch.hearc.p2.java.view.jpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import ch.hearc.p2.java.controller.ControllerEquation;
import ch.hearc.p2.java.model.Matrix;

public class JPanelResultStep extends JPanel implements Control_I
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelResultStep(ControllerEquation controllerEquation)
		{
		this.controllerEquation = controllerEquation;
		this.listString = controllerEquation.getEquation().getOperations();

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
						Matrix matrix = null;
						while(!isFini)
							{
							if (controllerEquation.hasNextMatrix())
								{
								matrix = controllerEquation.getNextMatrix();
								textArea.setText(matrix.toString());
								sleep(controllerEquation.getSpeed());
								}
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
		String[] tabString = new String[listString.size()];
		tabString=listString.toArray(tabString);
		rowList = new JList<String>(tabString);
		rowList.setVisibleRowCount(5);

		listScrollPane = new JScrollPane();
		listScrollPane.setViewportView(rowList);

		setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));

		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));

		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		panel_4.add(textArea, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.SOUTH);

		TitledBorder titlesborder = BorderFactory.createTitledBorder("Opérations");
		titlesborder.setTitleColor(Color.blue);
		panel_3.setBorder(titlesborder);
		panel_3.setForeground(Color.blue);

		TitledBorder titlematrix = BorderFactory.createTitledBorder("Matrice");
		titlematrix.setTitleColor(Color.blue);
		panel_4.setBorder(titlematrix);
		panel_4.setForeground(Color.blue);

		panel_3.add(listScrollPane);

//		textArea = new JTextArea(controllerEquation.getCurrentMatrix().toString());
//		//textArea.setBounds(new Rectangle(getBounds()));
//		textArea.setLineWrap(true);
//
//			// Layout : Specification
//			{
//			FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
//			setLayout(flowlayout);
//
//			// flowlayout.setHgap(20);
//			// flowlayout.setVgap(20);
//			}
//
//		// JComponent : add
//		add(textArea);
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

	//JTextArea textArea;
	ControllerEquation controllerEquation;

	private List<String> listString;
	private JScrollPane listScrollPane;
	private JList<String> rowList;
	private JTextArea textArea;
	}
