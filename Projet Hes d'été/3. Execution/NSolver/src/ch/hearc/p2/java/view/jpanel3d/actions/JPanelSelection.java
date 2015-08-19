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
				int index = 0;
				if (src == jCheckFct[1])
					index = 1;
				else if (src == jCheckFct[2])
					index = 2;

				jPanel3d.displayPlan(src.isSelected(), index);
			}
		};

		JPanel jPanelSelection = new JPanel();
		jPanelSelection.setLayout(new BoxLayout(jPanelSelection,
				BoxLayout.Y_AXIS));
		// jPanelSelection.setAlignmentY(Component.TOP_ALIGNMENT);
		
	

		jCheckFct[0].setForeground(jPanel3d.getPlanColor(0));
		jCheckFct[1].setForeground(jPanel3d.getPlanColor(1));
		jCheckFct[2].setForeground(jPanel3d.getPlanColor(2));
		
		jPanelSelection.add(Box.createVerticalStrut(5));

		for (JCheckBox jCheck : jCheckFct) {
			jCheck.setFont(font);
			jCheck.addActionListener(selectionListener);
			jPanelSelection.add(jCheck);
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
		String[] fcts_str = new String[3];
		for (int i = 0; i < 3; i++) {
			fcts_str[i] = "";
			for (int j = 0; j < 3; j++) {
				if (matrixValues[i][j] > 0)
					fcts_str[i] += " + " + matrixValues[i][j] + ""
							+ (char) (120 + j);
				else if (matrixValues[i][j] < 0)
					fcts_str[i] += " - " + (-matrixValues[i][j]) + ""
							+ (char) (120 + j);

			}
			fcts_str[i] = " " + fcts_str[i].substring(2) + " = "
					+ matrixValues[i][3];
		}
		jCheckFct = new JCheckBox[3];
		jCheckFct[0] = new JCheckBox(fcts_str[0], true);
		jCheckFct[1] = new JCheckBox(fcts_str[1], true);
		jCheckFct[2] = new JCheckBox(fcts_str[2], true);
		
	}

	private JCheckBox[] jCheckFct;
}
