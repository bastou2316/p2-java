package ch.hearc.p2.java.view.jpanel3d.actions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ch.hearc.p2.java.view.jpanel3d.JPanel3D;

public class JPanelHelp extends JPanel {
	
	public JPanelHelp(Font font, Color titleColor, JPanel3D jPanel3d) {
		
		this.jPanel3d = jPanel3d;
		this.setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));

		TitledBorder tBorderHelp = BorderFactory
				.createTitledBorder("Aide");
		tBorderHelp.setTitleFont(font);
		tBorderHelp.setTitleColor(titleColor);
		// tBorderProjection.setBorder(BorderFactory.createLineBorder(bgColor));

		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderHelp));
		
		jPanelHelp = new JPanel();
		jPanelHelp.setLayout(new BoxLayout(jPanelHelp,
				BoxLayout.Y_AXIS));
		
		
		BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(new File("./ressources/computer_mouse_final.png"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		icon = new ImageIcon(myPicture);
		
		Image zoom1 = null;
		zoom1 = scaleImage(icon.getImage(), 150);//taille en pixels

		Icon iconScaled = new ImageIcon(zoom1);
		
		
		image = new JLabel("");
		image.setIcon(iconScaled);
		
		jPanelHelp.add(image);
		
		jPanelHelp.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseExited(MouseEvent e) {
				JPanelHelp.this.mouseExited();				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				JPanelHelp.this.mouseEntered();
			}
			
		});

		this.add(jPanelHelp);
	}
	
	protected void mouseExited() {
		Image zoom1 = scaleImage(icon.getImage(), 150);//taille en pixels
		Icon iconScaled1 = new ImageIcon(zoom1);
		
		image.setIcon(iconScaled1);
		jPanelHelp.add(image);
		jPanel3d.update();			
	}
	
	
	protected void mouseEntered() {
		Image zoom2 = scaleImage(icon.getImage(), 450);//taille en pixels
		Icon iconScaled2 = new ImageIcon(zoom2);
		
		image.setIcon(iconScaled2);
		jPanelHelp.add(image);
		jPanel3d.update();			
	}



	public static Image scaleImage(Image source, int size) {
	    int width = source.getWidth(null);
	    int height = source.getHeight(null);
	    double f = 0;
	    if (width < height) {//portrait
	        f = (double) height / (double) width;
	        width = (int) (size / f);
	        height = size;
	    } else {//paysage
	        f = (double) width / (double) height;
	        width = size;
	        height = (int) (size / f);
	    }
	    return scaleImage(source, width, height);
	}
	
	public static Image scaleImage(Image source, int width, int height) {
	    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = (Graphics2D) img.getGraphics();
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g.drawImage(source, 0, 0, width, height, null);
	    g.dispose();
	    return img;
	}
	
	private JPanel3D jPanel3d;	
	private JLabel image;
	private ImageIcon icon;
	private JPanel jPanelHelp;
	
}
