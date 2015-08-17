package ch.hearc.p2.java;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.lang.Class;
import java.lang.reflect.Method;
import java.lang.NoSuchMethodException;
import java.lang.SecurityException;
import java.lang.Exception;
/**
 * Classe permettant de créer une fenêtre représentant un plan.<br>
 * Le plan affiché s'étend des coordonnées [-6,6] sur les axes x et y.<br>
 * Il est possible de dessiner des points, des flêches et des segments sur un plan.<br>
 * 
 * 
 * @author Benjamin Perret
 * @version 13/09/2012
 */
public class Plan 
{
	public static void main(String[] args) {
		Plan monPlan = new Plan();
		
		monPlan.dessinerSegmentEn2d(0, 0, 3, 3);
	}
    // min et max du repère monde
    private double xMax=6;
    private double yMax=6;
    
    // largeur et hauteur de la zone de dessin en pixel
    private int largeurPix=600;
    private int hauteurPix=600;

    // buffer de dessin et graphics sur ce buffer
    private BufferedImage buff;
    private Graphics2D gBuff;
    
    // faut-il dessiner le buffer
    private boolean repere=true;
    
    // quelques tests basiques
    private boolean debug=false;
   
    //zone de dessin
    private MonJPanel panel;
  
    
    /**
     * Créé un plan et l'affiche dans une fenêtre
     */
    public Plan()
    {
        JFrame frame = new JFrame("Plan2D");
        frame.setSize(largeurPix,hauteurPix+20);
        frame.setResizable(false);
        
        panel=new MonJPanel();
        panel.setSize(largeurPix,hauteurPix);
        frame.add(panel);
        
        buff = new BufferedImage(largeurPix,hauteurPix,BufferedImage.TYPE_INT_ARGB);
        gBuff = buff.createGraphics();
        effacer();
        frame.setVisible(true);
        
        if(debug)
        {
            dessinerPointEn2d(1,2,Color.CYAN);
            dessinerPointEn2d(1,-2);
            dessinerPointEn2d(-1,2);
            dessinerPointEn2d(-1,-2);
            dessinerFlecheEn2d(3,5);
            dessinerFlecheEn2d(-3,5);
            dessinerFlecheEn2d(3,-5);
            dessinerFlecheEn2d(-3,-5);
            dessinerSegmentEn2d(3,5,-3,5);
            dessinerSegmentEn2d(3,5,3,-5,Color.ORANGE);
            dessinerSegmentEn2d(3,-5,-3,-5);
            dessinerSegmentEn2d(-3,-5,-3,5);
            
            dessinerFlecheEn2d(1,3,Color.ORANGE);
            dessinerFlecheEn2d(1,3,3,5,Color.CYAN);
            
        }
    }

    
   
    
    /**
     * Conversion coordonnée en x dans le repère monde vers le repère écran
     * @param x coordonnée en x dans le repère monde
     * @return coordonnée dans le repère écran
     */
    private int repereVersEcranX(double x)
    {
        return (int)((x/xMax)*largeurPix/2)+largeurPix/2;
    }
    
    /**
     * Conversion coordonnée en y dans le repère monde vers le repère écran
     * @param y coordonnée en y dans le repère monde
     * @return coordonnée dans le repère écran
     */
    private int repereVersEcranY(double y)
    {
        return (int)((-y/yMax)*hauteurPix/2)+hauteurPix/2;
    }
    
    /**
     * Dessine le repère du monde dans le système de coordonnées écran
     * @param g objet graphics sur la zone de dessin
     */
    private void dessineRepere(Graphics g)
    {
        int nbStep=6;
        g.setColor(Color.GRAY);
        g.drawLine(0, hauteurPix/2,largeurPix,hauteurPix/2);
        g.drawLine(largeurPix/2,0,largeurPix/2,hauteurPix);
        for(int i=0;i<nbStep;i++)
        {
            int xx=repereVersEcranX(i*xMax/nbStep);
            g.drawLine(xx, hauteurPix/2,xx,hauteurPix/2+5);
            int xx2=repereVersEcranX(-i*xMax/nbStep);
            g.drawLine(xx2, hauteurPix/2,xx2,hauteurPix/2+5);
            
            int yy=repereVersEcranX(i*yMax/nbStep);
            g.drawLine(largeurPix/2,yy, largeurPix/2+5,yy);
            int yy2=repereVersEcranX(-i*yMax/nbStep);
            g.drawLine(largeurPix/2, yy2,largeurPix/2+5,yy2);
        }
        
        g.drawString("(0,0)",largeurPix/2+3,hauteurPix/2+15);
        g.drawString(""+(nbStep-1)*xMax/nbStep,repereVersEcranX((nbStep-1.0)*xMax/nbStep)-8,hauteurPix/2+18);
        g.drawString(""+ (-1*(nbStep-1)*xMax/nbStep),repereVersEcranX(-(nbStep-1.0)*xMax/nbStep)-8,hauteurPix/2+18);
        g.drawString(""+(nbStep-1)*yMax/nbStep,largeurPix/2+8,repereVersEcranY((nbStep-1.0)*yMax/nbStep)+5);
        g.drawString(""+(-(nbStep-1)*yMax/nbStep),largeurPix/2+8,repereVersEcranY(-(nbStep-1.0)*yMax/nbStep)+5);
    }
    

    
    /**
     * Effacer le contenu du plan
     */
    public void effacer()
    {     
        for(int x = 0; x < buff.getWidth(); x++)
        {
            for(int y = 0; y < buff.getHeight(); y++)
            {
                buff.setRGB(x, y, (0xFF));
            }
        }
        panel.repaint();
    }
    
    /**
     * Dessine un point rouge
     * @param o : tout objet possédant deux fonctions publiques getX() et getY() retournant respectivement les coordonnées en x et y de l'objet au format double
     */
    public void dessinerPointEn2d(Object o)
    {
        Double ox=getValueFromObject(o,"getX");
        Double oy=getValueFromObject(o,"getY");
        if(ox!=null && oy!=null)
        {
            dessinerPointEn2d(ox,oy);
        }
    }
    
    /**
     * Dessiner un point rouge aux coordonnées (px,py)
     * @param px Coordonnée en x
     * @param py Coordonnée en y
     */
    private void dessinerPointEn2d(double px, double py)
    {
        dessinerPointEn2d(px,py,Color.RED);
    }
    
    /**
     * Dessiner un point aux coordonnées (px,py)
     * @param px Coordonnée en x
     * @param py Coordonnée en y
     */
    private void dessinerPointEn2d(double px, double py, Color couleur)
    {
        gBuff.setColor(couleur);
        int x=repereVersEcranX(px);
        int y=repereVersEcranY(py);
        gBuff.fillRect(x-2,y-2,5,5);
        panel.repaint();
    }
    
    /**
     * Dessiner une flèche bleue partant de l'origine et allant aux coordonnées (px,py)
     * @param px Coordonnée en x de la pointe de la flêche
     * @param py Coordonnée en y de la pointe de la flêche
     */
    private void dessinerFlecheEn2d(double px, double py)
    {
        dessinerFlecheEn2d(0,0,px,py,Color.BLUE);
    }
    
    /**
     * Dessine un vecteur
     * @param o : tout objet possédant deux fonctions publiques getX() et getY() retournant respectivement les coordonnées en x et y de l'objet au format double
     */
    public void dessinerVecteurEn2d(Object o)
    {
        Double ox=getValueFromObject(o,"getX");
        Double oy=getValueFromObject(o,"getY");
        if(ox!=null && oy!=null)
        {
            dessinerFlecheEn2d(ox,oy);
        }
    }
    
    // "To be or not to be" un temps pour l'introspection !
    private Double getValueFromObject(Object o, String method)
    {
        Class<?> c=o.getClass();
        Method m;
        try{
            m=c.getMethod(method,new Class<?>[]{});
            
        } catch (NoSuchMethodException e) 
        {
            System.err.println("Plan: dessinerVecteurEn2d: Impossible de trouver la méthode "+method+" dans la classe " +c );
            return null;
        } catch (SecurityException e)
        {
            System.err.println("Plan: dessinerVecteurEn2d: La méthode " +method +" doit être publique dans la classe " +c);
            return null;
        }
        
        Object or;
        try{
            or=m.invoke(o,new Object []{});
        }catch(Exception e) {
            System.err.println("Plan: dessinerVecteurEn2d: Erreur lors de l'appel à " +method+ " : " + e);
            return null;
        }
        
         
        if(or==null)
        {
            System.err.println("Plan: dessinerVecteurEn2d: Erreur la méthode " +method+ " n'a rien retourné");
            return null;
        }
        
        if(!(or instanceof Double))
        {
            System.err.println("Plan: dessinerVecteurEn2d: Erreur la méthode " +method+ " doit retourner un double");
            return null;
        }
        
        return (Double)or;
    }
 
    /**
     * Dessiner une flèche partant de l'origine et allant aux coordonnées (px,py)
     * @param px Coordonnée en x de la pointe de la flêche
     * @param py Coordonnée en y de la pointe de la flêche
     * @param couleur Couleur de la flêche (voir classe java.awt.Color)
     */
    private void dessinerFlecheEn2d(double px, double py, Color couleur)
    {
        dessinerFlecheEn2d(0,0,px,py,couleur);
    }
    
    /**
     * Dessiner une flèche bleue partant de (px1,py1) et allant aux coordonnées (px2,py2)
     * @param px1 Coordonnée en x de l'origine de la flêche
     * @param py1 Coordonnée en y de l'origine de la flêche
     * @param px2 Coordonnée en x de la pointe de la flêche
     * @param py2 Coordonnée en y de la pointe de la flêche
     * @param couleur Couleur de la flêche (voir classe java.awt.Color)
     */
    private void dessinerFlecheEn2d(double px1, double py1, double px2, double py2, Color couleur)
    {
        dessinerSegmentEn2d(px1,py1,px2,py2,couleur);
        int x=repereVersEcranX(px2);
        int y=repereVersEcranY(py2);
        gBuff.translate(x, y);
        double angle = Math.atan2(py2-py1,px2-px1);
        gBuff.rotate(-angle);
        gBuff.drawLine(0, 0, -10, 5);
        gBuff.drawLine(0, 0, -10, -5);
        gBuff.rotate( angle);
        gBuff.translate( -x, -y);
        panel.repaint();
    }
   
    /**
     * Dessine un segment vert
     * @param o1 : origine du segment : tout objet possédant deux fonctions publiques getX() et getY() retournant respectivement les coordonnées en x et y de l'objet au format double
     * @param o2 : extrémité du segment : tout objet possédant deux fonctions publiques getX() et getY() retournant respectivement les coordonnées en x et y de l'objet au format double
     */
    public void dessinerSegmentEn2d(Object o1, Object o2)
    {
        Double ox1=getValueFromObject(o1,"getX");
        Double oy1=getValueFromObject(o1,"getY");
        Double ox2=getValueFromObject(o2,"getX");
        Double oy2=getValueFromObject(o2,"getY");
        if(ox1!=null && oy1!=null && ox2!=null && oy2!=null)
        {
            dessinerSegmentEn2d(ox1,oy1,ox2,oy2);
        }
    }
    
    /**
     * Dessiner un segment vert allant du point  (px1,py1) au point (px2,py2)
     * @param px1 Coordonnée en x de l'origine du segment
     * @param py1 Coordonnée en y de l'origine du segment
     * @param px2 Coordonnée en x de la pointe du segment
     * @param py2 Coordonnée en y de la pointe du segment
     */
    private void dessinerSegmentEn2d(double px1, double py1, double px2, double py2)
    {
        dessinerSegmentEn2d(px1,py1,px2,py2,Color.GREEN);
    }
    
    /**
     * Dessiner un segment  allant du point  (px1,py1) au point (px2,py2)
     * @param px1 Coordonnée en x de l'origine du segment
     * @param py1 Coordonnée en y de l'origine du segment
     * @param px2 Coordonnée en x de la pointe du segment
     * @param py2 Coordonnée en y de la pointe du segment
     * @param couleur Couleur du segment (voir classe java.awt.Color)
     */
    private void dessinerSegmentEn2d(double px1, double py1, double px2, double py2, Color couleur)
    {
        int x1=repereVersEcranX(px1);
        int y1=repereVersEcranY(py1);
        int x2=repereVersEcranX(px2);
        int y2=repereVersEcranY(py2);
        gBuff.setColor(couleur);
        gBuff.drawLine(x1,y1,x2,y2);
        panel.repaint();
    }
    /**
     * Définit si il faut afficher le repère
     * @param f si vrai le repere est affiché, sinon il ne l'est pas.
     */
    public void afficheRepere(boolean f)
    {
        repere=f; 
        panel.repaint();
    }
    
    // "Cachez cette classe que je ne saurais voir !"
    private class MonJPanel extends JPanel
    {
         @Override
         public void paint(Graphics g) 
         {
             g.setColor(Color.WHITE);
             g.clearRect(0, 0, largeurPix,hauteurPix);
             if(repere) dessineRepere(g);
             g.drawImage(buff,0,0,null);
            }
    }
}
