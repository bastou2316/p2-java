package ch.hearc.p2.java.view.jpanel3d.actions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class JPanelSolutionHighlight extends JPanel{
	
	public JPanelSolutionHighlight(Font font, Color titleColor) {
		this.setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));

		TitledBorder tBorderSolution = BorderFactory
				.createTitledBorder("Domaine de solution");
		tBorderSolution.setTitleFont(font);
		tBorderSolution.setTitleColor(titleColor);
		// tBorderProjection.setBorder(BorderFactory.createLineBorder(bgColor));

		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderSolution));
		
		JPanel jPanelSolution = new JPanel();
		jPanelSolution.setLayout(new BoxLayout(jPanelSolution,
				BoxLayout.Y_AXIS));
		// jPanelSelection.setAlignmentY(Component.TOP_ALIGNMENT);	
	
		JCheckBox cb = new JCheckBox("blablablalllla",true);
		cb.setFont(font);
//		cb.addActionListener(selectionListener);
		
//		jPanelSelection.add(Box.createVerticalStrut(5));
		jPanelSolution.add(cb);
//		jPanelSelection.add(Box.createVerticalStrut(5));
		
		
		
		// jCheckFct[].setForeground(bgColor);
		// jCheckFct[].setAlignmentX(Component.LEFT_ALIGNMENT);

		this.add(jPanelSolution);
	}
}
