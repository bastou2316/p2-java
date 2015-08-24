package ch.hearc.p2.java.view.jpanel3d.actions;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ch.hearc.p2.java.view.jpanel3d.JPanel3D;

public class JPanelSelection extends JPanel {

	public JPanelSelection(Font font, Color titleColor, double[][] matrixValues, JPanel3D jPanel3d) {
		initCheckBoxes(matrixValues);
		
		
		this.setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));

		TitledBorder tBorderSelection = BorderFactory
				.createTitledBorder(" Fonctions affichées");
		tBorderSelection.setTitleFont(font);
		tBorderSelection.setTitleColor(titleColor);
		// tBorderProjection.setBorder(BorderFactory.createLineBorder(bgColor));

		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderSelection));

		ActionListener selectionListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JCheckBox src = (JCheckBox) event.getSource();
				int index = 3;
				if (matrixValues.length >= 3 && src == jCheckFct[2])
					index = 2;
				else if (matrixValues.length >= 2 && src == jCheckFct[1])
					index = 1;
				else if (src == jCheckFct[0])
					index = 0;

				jPanel3d.displayPlan(src.isSelected(), index);
			}
		};

		JPanel jPanelSelection = new JPanel();
		jPanelSelection.setLayout(new BoxLayout(jPanelSelection,
				BoxLayout.Y_AXIS));
		// jPanelSelection.setAlignmentY(Component.TOP_ALIGNMENT);
		
		
		jPanelSelection.add(Box.createVerticalStrut(5));

		for (int i = 0; i < jCheckFct.length; i++) {
			jCheckFct[i].setFont(font);
			jCheckFct[i].addActionListener(selectionListener);
			jCheckFct[i].setForeground(jPanel3d.getPlanColor(i));

			jPanelSelection.add(jCheckFct[i]);
			jPanelSelection.add(Box.createVerticalStrut(5));
			
		}
		// jCheckFct[].setForeground(bgColor);
		// jCheckFct[].setAlignmentX(Component.LEFT_ALIGNMENT);

		this.add(jPanelSelection);
	}
	
	public boolean isCheckBoxSelected(int index){
		return jCheckFct[index].isSelected();
	}
	
	private void initCheckBoxes(double[][] matrixValues) {
		int nbEqu = matrixValues.length;
		int nbVar = matrixValues[0].length-1;
		String[] fcts_str = new String[nbEqu];
		jCheckFct = new JCheckBox[nbEqu];
		
		for (int i = 0; i < nbEqu; i++) {
			fcts_str[i] = "";
			for (int j = 0; j < nbVar; j++) {
				if (matrixValues[i][j] > 0)
					fcts_str[i] += " + " + matrixValues[i][j] + ""
							+ (char) (120 + j);
				else if (matrixValues[i][j] < 0)
					fcts_str[i] += " - " + (-matrixValues[i][j]) + ""
							+ (char) (120 + j);

			}
			fcts_str[i] = " " + fcts_str[i].substring(2) + " = "
					+ matrixValues[i][nbVar];
			jCheckFct[i] = new JCheckBox(fcts_str[i], true);
		}
		
	}

	private JCheckBox[] jCheckFct;
}
