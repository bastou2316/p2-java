
package ch.hearc.p2.java.view.jpanel3d.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ch.hearc.p2.java.view.jpanel3d.JPanel3D;

public class JPanelHomeTransform extends JPanel
	{

	public JPanelHomeTransform(Font font, Color titleColor, final JPanel3D panel3d)
		{
		BoxLayout boxLayoutHome = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(boxLayoutHome);

		TitledBorder tBorderHome = BorderFactory.createTitledBorder(" Réinitialisation et centrage");
		tBorderHome.setTitleFont(font);
		tBorderHome.setTitleColor(titleColor);
		// tBorderHome.setBorder(BorderFactory.createLineBorder(bgColor));

		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), tBorderHome));

		JButton jButtonHomeTransform = new JButton("Retour à la vue de départ");
		jButtonHomeTransform.setFont(font);
		// jButtonHomeTransform.setForeground(bgColor);
		jButtonHomeTransform.setAlignmentX(Component.CENTER_ALIGNMENT);

		jButtonHomeTransform.addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent event)
					{
					panel3d.goHomeView();
					}
			});

		JButton jButtonLookAt = new JButton("Centrer");
		jButtonLookAt.setFont(font);
		// jButtonLookAt.setForeground(bgColor);
		jButtonLookAt.setAlignmentX(Component.CENTER_ALIGNMENT);

		jButtonLookAt.addActionListener(new ActionListener()
			{

				public void actionPerformed(ActionEvent event)
					{
					panel3d.centerView();

					}
			});

		this.add(Box.createVerticalStrut(10));
		this.add(jButtonHomeTransform);
		this.add(Box.createVerticalStrut(10));
		this.add(jButtonLookAt);
		this.add(Box.createVerticalStrut(10));

		}
	}
