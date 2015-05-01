
package ch.hearc.p2.java.view.jpanel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;

import ch.hearc.p2.java.controller.ControllerEquation;
import ch.hearc.p2.java.model.Matrix;
import ch.hearc.p2.java.view.JFrameMain;

public class JPanelResultDirect extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelResultDirect(JFrameMain jFrameMain, ControllerEquation controllerEquation)
		{
		this.jFrameMain = jFrameMain;
		this.controllerEquation = controllerEquation;

		//Composition du panel
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
		Dimension dim = controllerEquation.getMatrixDimension();
		System.out.println(dim);
		tableRes = new JTable(dim.width, dim.height);
		interpretationTextArea = new JTextArea("test");
		interpretationTextArea.setEditable(false);
		
		panelDefaultControl = new JPanelDefaultControl(jFrameMain, controllerEquation);

			// Layout : Specification
			{
			FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
			setLayout(flowlayout);

			// flowlayout.setHgap(20);
			// flowlayout.setVgap(20);
			}

		// JComponent : add
		add(tableRes);
		add(interpretationTextArea);
		add(panelDefaultControl);
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
		
		}
	
	

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/
	
	//Inputs
	private JFrameMain jFrameMain;
	private ControllerEquation controllerEquation;
	
	// Tools

	private JTable tableRes;
	private JTextArea interpretationTextArea;
	
	private JPanelDefaultControl panelDefaultControl;
	

	}
