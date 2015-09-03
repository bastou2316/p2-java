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

import ch.hearc.p2.java.view.jpanel3d.JPanel3D;

public class JPanelSolutionHighlight extends JPanel{

	private JCheckBox cb;

	public JPanelSolutionHighlight(Font font, Color titleColor, double[][] solution, final JPanel3D panel3d) {
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
		else{ //solution multiple
			cb.setText("Solutions multiples");
			cb.setEnabled(false);
		}

		jPanelSolution.add(cb);


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
