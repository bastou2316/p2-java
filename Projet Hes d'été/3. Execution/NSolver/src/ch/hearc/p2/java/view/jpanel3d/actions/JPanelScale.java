
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

public class JPanelScale extends JPanel
	{

	public final static float[] scaleValues = { 0.001f, 0.0013f, 0.0016f, 0.002f,
			0.0025f, 0.0032f, 0.004f, 0.005f, 0.0063f, 0.008f, 0.01f, 0.013f, 0.016f,
			0.02f, 0.025f, 0.032f, 0.04f, 0.05f, 0.063f, 0.08f, 0.1f, 0.13f, 0.16f, 0.2f,
			0.25f, 0.32f, 0.4f, 0.5f, 0.63f, 0.8f, 1.0f, 1.3f, 1.6f, 2.0f, 2.5f, 3.2f, 4.0f,
			5.0f, 6.3f, 8.0f, 10f, 13f, 16f, 20f, 25f, 32f, 40f, 50f, 63f, 80f, 100f,
			130f, 160f, 200f, 250f, 320f, 400f, 500f, 630f, 800f, 1000f };

	public JPanelScale(Font font, Color titleColor, float scaleFactor, final JPanel3D jPanel3d)
		{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		TitledBorder tBorderScale = BorderFactory.createTitledBorder("Echelle de la boîte");
		tBorderScale.setTitleFont(font);
		tBorderScale.setTitleColor(titleColor);
		// tBorderScale.setBorder(BorderFactory.createLineBorder(bgColor));

		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), tBorderScale));

		final JSlider jSliderScale = new JSlider(-30, 30, (int)(10 * Math.log10(scaleFactor)));
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

		jSliderScale.addChangeListener(new ChangeListener()
			{

			@Override
			public void stateChanged(ChangeEvent e) {
				// if (!jSliderScale.getValueIsAdjusting()) {
				int index = jSliderScale.getValue() +30;
				float newScaleFactor = JPanelScale.scaleValues[index];
//				if (jSliderScale.getValue() >= 0) {
//					newScaleFactor = (float) Math.round(Math.pow(10,
//							jSliderScale.getValue() / 10.0));
//				} else {
//					newScaleFactor = (float) Math.pow(10,
//							jSliderScale.getValue() / 10.0);
//				}
				jPanel3d.setScaleFactor(newScaleFactor);
				
				float space = 100f;
				while(space >= newScaleFactor)
					space /= 10f;				
				jPanel3d.setSpace(space);

				
				jPanel3d.rescale();
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
