package ch.hearc.p2.java.view.jpanel3d.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.hearc.p2.java.view.jpanel3d.JPanel3D;

public class JPanelScale extends JPanel {

	public final static double[] scaleValues = { 0.001, 0.0013, 0.0016, 0.002,
			0.0025, 0.0032, 0.004, 0.005, 0.0063, 0.008, 0.01, 0.013, 0.016,
			0.02, 0.025, 0.032, 0.04, 0.05, 0.063, 0.08, 0.1, 0.13, 0.16, 0.2,
			0.25, 0.32, 0.4, 0.5, 0.63, 0.8, 1.0, 1.3, 1.6, 2.0, 2.5, 3.2, 4.0,
			5.0, 6.3, 8.0, 10.0, 13.0, 16.0, 20.0, 25, 32, 40, 50, 63, 80, 100,
			130, 160, 200, 250, 320, 400, 500, 630, 800, 1000 };

	public JPanelScale(Font font, Color titleColor, float scaleFactor,
			JPanel3D jPanel3d) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		TitledBorder tBorderScale = BorderFactory
				.createTitledBorder("Echelle de la boîte");
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
		jSliderScale.setMinorTickSpacing(2);
		jSliderScale.setPaintTicks(true);

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
					newScaleFactor = (float) Math.round(Math.pow(10,
							jSliderScale.getValue() / 10.0));
				} else {
					newScaleFactor = (float) Math.pow(10,
							jSliderScale.getValue() / 10.0);
				}
				jPanel3d.setScaleFactor(newScaleFactor);
				
				float space = 100f;
				while(space >= newScaleFactor)
					space /= 10f;
				
				jPanel3d.setSpace(space);

				jPanel3d.rescale();

				// }
			}
		});

		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		jPanel.add(Box.createVerticalGlue());
		jPanel.add(jSliderScale);
		jPanel.add(Box.createVerticalGlue());

		// Pour agrandir le slider, pour plus de lisibilité
		Dimension scaleSliderDim = jPanel.getPreferredSize();
		scaleSliderDim.width = 280;
		jPanel.setPreferredSize(scaleSliderDim);
		jPanel.setMinimumSize(scaleSliderDim);
		//

		add(jPanel);
	}
}
