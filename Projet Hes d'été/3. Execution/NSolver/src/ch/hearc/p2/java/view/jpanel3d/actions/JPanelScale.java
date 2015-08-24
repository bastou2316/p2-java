package ch.hearc.p2.java.view.jpanel3d.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.hearc.p2.java.view.jpanel3d.JPanel3D;

public class JPanelScale extends JPanel{

	public JPanelScale(Font font, Color titleColor, float scaleFactor, final JPanel3D jPanel3d) {
		this.setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));

		TitledBorder tBorderScale = BorderFactory.createTitledBorder("Echelle de la boîte");
		tBorderScale.setTitleFont(font);
		tBorderScale.setTitleColor(titleColor);
		// tBorderScale.setBorder(BorderFactory.createLineBorder(bgColor));

		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderScale));

		final JSlider jSliderScale = new JSlider(-30, 30,
				(int) (10 * Math.log10(scaleFactor)));
		jSliderScale.setFont(font);
		jSliderScale.setAlignmentX(Component.LEFT_ALIGNMENT);
		jSliderScale.setMajorTickSpacing(10);
		jSliderScale.setPaintTicks(true);
		Dimension scaleSliderDim = new Dimension(280, 60);
		jSliderScale.setPreferredSize(scaleSliderDim);

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();

		labelTable.put(new Integer(-30), new JLabel("0.001"));
		labelTable.put(new Integer(-20), new JLabel("0.01"));
		labelTable.put(new Integer(-10), new JLabel("0.1"));
		labelTable.put(new Integer(0), new JLabel("1"));
		labelTable.put(new Integer(10), new JLabel("10"));
		labelTable.put(new Integer(20), new JLabel("100"));
		labelTable.put(new Integer(30), new JLabel("1000"));

		jSliderScale.setLabelTable(labelTable);
		jSliderScale.setPaintLabels(true);

		jSliderScale.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// if (!jSliderScale.getValueIsAdjusting()) {
				float newScaleFactor = 0f;
				if (jSliderScale.getValue() >= 0) {
					newScaleFactor = Math.round(Math.pow(10,
							jSliderScale.getValue() / 10.0));
				} else {
					newScaleFactor = (float) Math.pow(10,
							jSliderScale.getValue() / 10.0);
				}
				jPanel3d.setScaleFactor(newScaleFactor);

				jPanel3d.rescale();

				// }
			}
		});

		JPanel jPanel = new JPanel();
		jPanel.setPreferredSize(scaleSliderDim);
		jPanel.setMinimumSize(scaleSliderDim);

		jPanel.add(jSliderScale);

		add(jPanel);
	}
}
