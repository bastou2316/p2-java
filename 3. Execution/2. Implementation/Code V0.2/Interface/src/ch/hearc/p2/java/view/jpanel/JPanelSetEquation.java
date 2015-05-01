package ch.hearc.p2.java.view.jpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.util.Enumeration;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import ch.hearc.p2.java.controller.ControllerEquation;
import ch.hearc.p2.java.model.Matrix;
import ch.hearc.p2.java.view.JFrameMain;
import ch.hearc.p2.java.view.JFrameMain.PANEL;

public class JPanelSetEquation extends JSplitPane {

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelSetEquation(JFrameMain jFrameMain,
			ControllerEquation controllerEquation) {
		super(JSplitPane.HORIZONTAL_SPLIT);

		this.jFrameMain = jFrameMain;
		this.controllerEquation = controllerEquation;

		// Composition du panel
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

	private void geometry() {
		
		// Ic�ne
		// icon = new JLabel(new ImageIcon("images/icone.jpg"));
		// JPanel panIcon = new JPanel();
		// panIcon.setBackground(Color.white);
		// panIcon.setLayout(new BorderLayout());
		// panIcon.add(icon);

		// Matrice
		JPanel panMatrice = new JPanel();
		panMatrice.setLayout(new GridLayout(4, 2));
		panMatrice.setBackground(Color.white);
		panMatrice.setBorder(BorderFactory.createTitledBorder("Matrice"));

		name = new JTextField();
		nameLabel = new JLabel("Nom :");
		panMatrice.add(nameLabel);
		panMatrice.add(name);

		equationNumber = new JSpinner();
		equationNumber.setValue(3);
		equationNumberLabel = new JLabel("Nombre d'�quations :");
		panMatrice.add(equationNumberLabel);
		panMatrice.add(equationNumber);

		varNumber = new JSpinner();
		varNumber.setValue(3);
		varNumberLabel = new JLabel("Nombre d'inconnues :");
		panMatrice.add(varNumberLabel);
		panMatrice.add(varNumber);

		varStyle = new JComboBox<String>();
		varStyle.addItem("x1, x2, x3, x4, ...");
		varStyle.addItem("a, b, c, d, e, ...");
		varStyle.addItem("x, y, z (max 3 variables)");
		varStyleLabel = new JLabel("Style des variables :");
		panMatrice.add(varStyleLabel);
		panMatrice.add(varStyle);

		// R�solution
		JPanel panReso = new JPanel();
		panReso.setBackground(Color.white);
		panReso.setBorder(BorderFactory.createTitledBorder("R�solution"));
		methodStep = new JRadioButton("�tape par �tape");
		methodStep.setSelected(true);
		methodDirect = new JRadioButton("directe");
		ButtonGroup bg = new ButtonGroup();
		bg.add(methodStep);
		bg.add(methodDirect);
		panReso.add(methodStep);
		panReso.add(methodDirect);

		JPanel control = new JPanel();

		backButton = new JButton("BACK");
		okBouton = new JButton("OK");

		control.add(backButton);
		control.add(okBouton);

		leftContainerBox = Box.createVerticalBox();

		// leftContainerBox.add(panIcon);
		leftContainerBox.add(Box.createVerticalGlue());
		leftContainerBox.add(panMatrice);
		leftContainerBox.add(Box.createVerticalStrut(10));
		leftContainerBox.add(panReso);
		leftContainerBox.add(Box.createVerticalStrut(10));
		leftContainerBox.add(control);
		leftContainerBox.add(Box.createVerticalGlue());

		this.setLeftComponent(leftContainerBox);

		// RIGHT component

		rightContainerPanel = new JPanel(new BorderLayout());

		createMatrixTables();

		rightContainerPanel.add(boxV, BorderLayout.CENTER);
		JScrollPane jScrollPane = new JScrollPane(rightContainerPanel);
		this.setRightComponent(jScrollPane);
	}

	private void control() {
		

		okBouton.addActionListener(new ActionListener() {

			

			// setter matrix en fonction de matrixTables

			@Override
			public void actionPerformed(ActionEvent e) {
				int nbVar = (int) varNumber.getValue();
				int nbEqu = (int) equationNumber.getValue();

				Matrix matrix = new Matrix();
				controllerEquation.setEquation(matrix, nbVar, nbEqu, true, 10);

				if (methodStep.isSelected()) {
					jFrameMain.showView(PANEL.RESULT_STEP);
				} else {
					jFrameMain.showView(PANEL.RESULT);
				}

			}
		});

		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jFrameMain.showView(PANEL.MENU);
			}
		});

		ChangeListener changeListener = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				updateTable();
			}
		};

		equationNumber.addChangeListener(changeListener);
		varNumber.addChangeListener(changeListener);

		varStyle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateTable();
			}
		});

	}

	private void appearance() {
		// rien
	}

	private void createMatrixTables() {
		int nbVar = (int) varNumber.getValue();
		int nbEqu = (int) equationNumber.getValue();

		matrixTables = new JTable[nbVar + 1];
		varsTables = new JTable[nbVar];

		for (int j = 0; j < nbVar; j++) {
			matrixTables[j] = new JTable(nbEqu, 1);
			matrixTables[j].setRowHeight(40);
			matrixTables[j].setBackground(Color.YELLOW);
			matrixTables[j].setCellSelectionEnabled(false);
			varsTables[j] = new JTable(nbEqu, 1);
			varsTables[j].setRowHeight(40);
			varsTables[j].setShowVerticalLines(false);
			varsTables[j].setEnabled(false);
		}
		matrixTables[nbVar] = new JTable(nbEqu, 1);
		matrixTables[nbVar].setRowHeight(40);
		matrixTables[nbVar].setRowSelectionAllowed(false);
		matrixTables[nbVar].setBackground(Color.YELLOW);

		char addOrEqual;
		switch (varStyle.getSelectedIndex()) {
		case 0:
			for (int i = 0; i < nbEqu; i++) {
				addOrEqual = '+';
				for (int j = 0; j < nbVar; j++) {
					if (j + 1 == nbVar)
						addOrEqual = '=';
					varsTables[j].setValueAt("  x" + j + "   "
							+ addOrEqual, i, 0);
				}
			}
			break;

		case 1:
			for (int i = 0; i < nbEqu; i++) {
				addOrEqual = '+';
				for (int j = 0; j < nbVar; j++) {
					if (j == nbVar - 1)
						addOrEqual = '=';
					varsTables[j].setValueAt("     " + (char) (97 + j)
							+ "      " + addOrEqual, i, 0);
				}
			}
			break;

		case 2:
			for (int i = 0; i < nbEqu; i++) {
				addOrEqual = '+';
				for (int j = 0; j < nbVar; j++) {
					if (j == nbVar - 1)
						addOrEqual = '=';
					varsTables[j].setValueAt("     " + (char) (120 + j)
							+ "      " + addOrEqual, i, 0);
				}
			}
			break;
		}

		boxV = Box.createVerticalBox();
		boxH = Box.createHorizontalBox();

		boxH.add(Box.createHorizontalGlue());
		for (int j = 0; j < nbVar; j++) {
			boxH.add(matrixTables[j]);
			boxH.add(Box.createHorizontalStrut(5));
			boxH.add(varsTables[j]);
			boxH.add(Box.createHorizontalStrut(5));
		}
		boxH.add(matrixTables[nbVar]);
		boxH.add(Box.createHorizontalGlue());

		boxV.add(Box.createVerticalGlue());
		boxV.add(boxH);
		boxV.add(Box.createVerticalGlue());

	}

	private void updateTable() {
		rightContainerPanel.remove(boxV);

		createMatrixTables();

		rightContainerPanel.add(boxV);
		rightContainerPanel.revalidate();
		rightContainerPanel.repaint();
	}
	
	
	/**
	 * Controler les inputs dans la table, qu'ils soient bien des double (ou int)
	 * @param val
	 * @return
	 */
	private boolean checkInput(Object val){
		if (val != null) {
			try {
				Double.valueOf((String) val);
			} catch (NumberFormatException e) {
				System.err
						.println("Erreur dans l'entr�e utilisateur (need double) ");
				return false;
			}
		}
		return true;
	}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private JFrameMain jFrameMain;
	private ControllerEquation controllerEquation;

	// Tools
	private Box leftContainerBox;
	private JPanel rightContainerPanel;

	// left (form)
	private JLabel nameLabel, equationNumberLabel, varNumberLabel,
			varStyleLabel, icon;
	private JRadioButton methodStep, methodDirect;
	private JComboBox<String> varStyle;
	private JSpinner equationNumber, varNumber;
	private JTextField name;
	private JButton okBouton, backButton;

	// right (table)
	private JTable[] matrixTables;
	private JTable[] varsTables;
	private Box boxV;
	private Box boxH;

}
