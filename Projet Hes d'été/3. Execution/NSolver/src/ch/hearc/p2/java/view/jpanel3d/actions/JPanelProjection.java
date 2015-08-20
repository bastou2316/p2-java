
package ch.hearc.p2.java.view.jpanel3d.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import ch.hearc.p2.java.view.jpanel3d.JPanel3D;

public class JPanelProjection extends JPanel
	{

	public JPanelProjection(Font font, Color titleColor, final JPanel3D jPanel3d)
		{
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		TitledBorder tBorderProjection = BorderFactory.createTitledBorder(" Projection ");
		tBorderProjection.setTitleFont(font);
		tBorderProjection.setTitleColor(titleColor);
		// tBorderProjection.setBorder(BorderFactory.createLineBorder(bgColor));

		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), tBorderProjection));

		final JRadioButton jRadioParallel = new JRadioButton("Parallèle");
		final JRadioButton jRadioPerspective = new JRadioButton("Perspective");
		jRadioPerspective.setSelected(true);

		jRadioParallel.setFont(font);
		jRadioPerspective.setFont(font);
		// jRadioParallel.setForeground(bgColor);
		// jRadioPerspective.setForeground(bgColor);

		jRadioPerspective.setAlignmentX(Component.LEFT_ALIGNMENT);

		ActionListener projectionListener = new ActionListener()
			{

				public void actionPerformed(ActionEvent event)
					{

					if (jRadioPerspective.isSelected())
						{
						jPanel3d.setPerspective(true);
						}
					else
						{
						jPanel3d.setPerspective(false);
						}

					}
			};
		jRadioParallel.addActionListener(projectionListener);
		jRadioPerspective.addActionListener(projectionListener);

		ButtonGroup projGroup = new ButtonGroup();
		projGroup.add(jRadioParallel);
		projGroup.add(jRadioPerspective);

		JPanel jPanelParProj = new JPanel();
		jPanelParProj.setLayout(new BoxLayout(jPanelParProj, BoxLayout.X_AXIS));
		jPanelParProj.setAlignmentX(Component.LEFT_ALIGNMENT);

		jPanelParProj.add(jRadioParallel);

		this.add(Box.createVerticalStrut(5));
		this.add(jPanelParProj);
		this.add(Box.createVerticalStrut(5));
		this.add(jRadioPerspective);
		this.add(Box.createVerticalStrut(5));

		}
	}
