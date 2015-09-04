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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ch.hearc.p2.java.tools.MathTools;
import ch.hearc.p2.java.view.IndependentVar;
import ch.hearc.p2.java.view.jpanel3d.JPanel3D;

public class JPanelSolutionHighlight extends JPanel{

	private JCheckBox cb;
	private JLabel[] labels;

	public JPanelSolutionHighlight(Font font, Color titleColor, double[][] solution, int varStyle, final JPanel3D panel3d) {
		this.setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));

		TitledBorder tBorderSolution = BorderFactory
				.createTitledBorder("Domaine de solution");
		tBorderSolution.setTitleFont(font);
		tBorderSolution.setTitleColor(titleColor);

		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderSolution));

		JPanel jPanelSolution = new JPanel();
		jPanelSolution.setLayout(new BoxLayout(jPanelSolution,
				BoxLayout.Y_AXIS));

		cb = new JCheckBox();
		cb.setSelected(true);
		cb.setFont(font);
		cb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel3d.displaySolution(cb.isSelected());
			}
		});
		
		labels = new JLabel[3];
		for (int i = 0; i <labels.length; i++) {
			labels[i] = new JLabel();
			labels[i].setFont(font);
		}

		if(solution == null){ //pas de solution
			cb.setText("Pas de solution");
			cb.setEnabled(false);
		}
		else if(solution[0].length == 1){ //unique
			String text = "{ ";
			for(int i = 0; i < solution.length; i++){
				text += solution[i][0] + " ; ";
			}
			text = text.substring(0, text.length()-2) + "}";
			cb.setText(text);
		}
		else{ //			cb.setText("Infinité de solutions");						
			String text;
			for(int i = 0; i < solution.length; i++){
				text = " ";
				text += IndependentVar.getTabVar(3, varStyle)[i] + " = ";
				int indexFirstNumber = 0;
				for (int j = 0; j < solution[0].length; j++) {
					if(!MathTools.isEquals(solution[i][j], 0.0,1E-7)){
						if(j == 0){
							 text += solution[i][j];
						}
						else if(j == 1){
							if(indexFirstNumber == 1)
								text += solution[i][j];
							else if(solution[i][j] >= 0)
								text += " + "+ solution[i][j];
							else
								text += " - "+ -solution[i][j];
							text += "u1";
						}
						else if(j == 2){
							if(indexFirstNumber == 2)
								text += solution[i][j];
							else if(solution[i][j] >= 0)
								text += " + "+ solution[i][j];
							else
								text += " - "+ -solution[i][j];
							text += "u2";
						}
					}
					else{
						indexFirstNumber++;
					}
				}
				labels[i].setText(text);
			}
		}

		jPanelSolution.add(cb);
		for (JLabel label : labels) {
			jPanelSolution.add(label);
		}


		this.add(Box.createVerticalGlue());
		this.add(jPanelSolution);
		this.add(Box.createVerticalGlue());

		Dimension size = this.getPreferredSize();
		if(size.width < 140){
			size.width = 140;
			this.setMinimumSize(size);
			this.setPreferredSize(size);
		}
	}

	public boolean isCheckBoxSelected() {
		return cb.isSelected();
	}
}
