package ch.hearc.p2.java;

/**
 * @author Mateli
 */

// classes Java standart
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Group;
import javax.media.j3d.LineArray;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.JFrame;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.exp.swing.JCanvas3D;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
// classes Java 3D
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class cube3d extends JFrame implements WindowListener {

	
	private static final long serialVersionUID = 1L;

	// Tools
	private JCanvas3D canvas3D;
	private PolygonAttributes polyAttr;

	private OrbitBehavior orbit;

	public cube3d() {
		super("- 2 TG pour placer 2 cubes -");
		setSize(1000, 1000);
		setLocationRelativeTo(null);
		addWindowListener(this);
		setLayout(new BorderLayout());

		// // 1ere étape création du JCanvas3d qui vas afficher votre univers
		// // virtuel avec une config pré etablie
		canvas3D = new JCanvas3D(new GraphicsConfigTemplate3D());
		canvas3D.setSize(1000, 1000);
		add(canvas3D);

		// 2eme étape on crée notre scene (regroupement d'objet)
		BranchGroup scene = createSceneGraph();
		// on les compile pour optimiser les calcules
		// scene.compile();

		// 3eme étape on creer l'univer qui va contenir notre scene 3d
		// utilise simpleUniverse qui simplifie le code (il crée un
		// environemment minimal simple)
		SimpleUniverse simpleU = new SimpleUniverse(
				canvas3D.getOffscreenCanvas3D());

		// on déplace le point de vue de départ (rotation et translation)
		Transform3D r = new Transform3D();
		r.setRotation(new AxisAngle4d(-1.0f, 1.0f, 0.00f, Math.toRadians(10)));
		Transform3D t = new Transform3D();
		t.setTranslation(new Vector3f(0f, 0f, 4f));
		r.mul(t);
		simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(r);

		
		

        // add orbit behavior to ViewingPlatform
        
		
		// on place la scene dans l'univers simpleU
		simpleU.addBranchGraph(scene);
	}

	// crée un regroupement d'objet contenant un objet cube
	public BranchGroup createSceneGraph() {
		// on crée le Bg principal
		BranchGroup objRoot = new BranchGroup();

		// ------------ début de création des axes ------------

		// on crée un groupe de transformation TG1
		TransformGroup TG1 = new TransformGroup();

		// axe des X
		LineArray axisX = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisX.setCoordinate(0, new Point3f(-1f, 0f, 0f));
		axisX.setCoordinate(1, new Point3f(1f, 0f, 0f));
		axisX.setColor(0, new Color3f(0f, 1f, 0f));
		axisX.setColor(1, new Color3f(1f, 0f, 0f));

		// axe des Y
		LineArray axisY = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisY.setCoordinate(0, new Point3f(0f, -1f, 0f));
		axisY.setCoordinate(1, new Point3f(0f, 1f, 0f));
		axisY.setColor(0, new Color3f(0f, 1f, 0f));
		axisY.setColor(1, new Color3f(1f, 0f, 0f));

		// axe des Z
		LineArray axisZ = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisZ.setCoordinate(0, new Point3f(0f, 0f, -1f));
		axisZ.setCoordinate(1, new Point3f(0f, 0f, 1f));
		axisZ.setColor(0, new Color3f(0f, 1f, 0f));
		axisZ.setColor(1, new Color3f(1f, 0f, 0f));

		TG1.addChild(new Shape3D(axisX));
		TG1.addChild(new Shape3D(axisY));
		TG1.addChild(new Shape3D(axisZ));

		// création de 3 plans

		polyAttr = new PolygonAttributes();
		polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
		polyAttr.setBackFaceNormalFlip(true);

		// plan1
		Appearance app1 = new Appearance();
		app1.setColoringAttributes(new ColoringAttributes(new Color3f(0.3f,
				0.2f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
		app1.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.NONE, 0.1f));
		app1.setPolygonAttributes(polyAttr);

		QuadArray quadArrTest = createPlan(-0.1f, 0.5f, 1.0f, 0.7f);

		TG1.addChild(new Shape3D(quadArrTest, app1));

		// float w = 1.0f;
		// float h = 0.0f;
		// float z = 1.0f;
		// QuadArray quadArray1 = new QuadArray(4, GeometryArray.COORDINATES);
		// Point3f[] coords1 = new Point3f[4];
		// coords1[0] = new Point3f(w, h, z);
		// coords1[1] = new Point3f(-w, h, z);
		// coords1[2] = new Point3f(-w, h, -z);
		// coords1[3] = new Point3f(w, h, -z);
		// quadArray1.setCoordinates(0, coords1);
		//
		// //Plan 2
		// Appearance app2 = new Appearance();
		// app2.setColoringAttributes(new ColoringAttributes(new Color3f(0.4f,
		// 0.8f, 0.3f), ColoringAttributes.SHADE_GOURAUD));
		// app2.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.1f));
		// app2.setPolygonAttributes(polyAttr);
		// w = 0.0f;
		// h = 1.0f;
		// z = 1.0f;
		// QuadArray quadArray2 = new QuadArray(4, GeometryArray.COORDINATES);
		// Point3f[] coords2 = new Point3f[4];
		// coords2[0] = new Point3f(w, h, z);
		// coords2[1] = new Point3f(w, h, -z);
		// coords2[2] = new Point3f(w, -h, -z);
		// coords2[3] = new Point3f(w, -h, z);
		// quadArray2.setCoordinates(0, coords2);
		//
		// // Plan3
		// Appearance app3 = new Appearance();
		// app3.setColoringAttributes(new ColoringAttributes(new Color3f(1.0f,
		// 0.2f, 0.1f), ColoringAttributes.SHADE_GOURAUD));
		// app3.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.1f));
		// app3.setPolygonAttributes(polyAttr);
		// w = 1.0f;
		// h = 1.0f;
		// z = 0.0f;
		// QuadArray quadArray3 = new QuadArray(4, GeometryArray.COORDINATES);
		// Point3f[] coords3 = new Point3f[4];
		// coords3[0] = new Point3f(w, h, z);
		// coords3[1] = new Point3f(-w, h, z);
		// coords3[2] = new Point3f(-w, -h, z);
		// coords3[3] = new Point3f(w, -h, z);
		// quadArray3.setCoordinates(0, coords3);

		// TG1.addChild(new Shape3D(quadArray1, app1));
		// TG1.addChild(new Shape3D(quadArray2, app2));
		// TG1.addChild(new Shape3D(quadArray3, app3));

		// Rotation du tranformGroup avec la souris possible
		BoundingSphere bounds = new BoundingSphere(new Point3d(), 1000.0);

		objRoot.setCapability(BranchGroup.ALLOW_DETACH);
		objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		objRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
		TG1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		TG1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		TG1.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		TG1.setCapability(Group.ALLOW_CHILDREN_WRITE);

		MouseRotate mr = new MouseRotate(canvas3D, TG1);
		MouseWheelZoom mz = new MouseWheelZoom(canvas3D,
				MouseWheelZoom.INVERT_INPUT);
		mz.setTransformGroup(TG1);
		MouseTranslate mt = new MouseTranslate(canvas3D, TG1);

		mr.setSchedulingBounds(bounds);
		mz.setSchedulingBounds(bounds);
		mt.setSchedulingBounds(bounds);

		TG1.addChild(mr);
		TG1.addChild(mz);
		TG1.addChild(mt);

		objRoot.addChild(TG1);

		return objRoot;
	}

	public QuadArray createPlan(float a, float b, float c, float d) {
		QuadArray quadArray = new QuadArray(4, GeometryArray.COORDINATES);
		Point3f[] coords = new Point3f[4];

		// On a : ax + by + cz = d
		// Donc : z = (d - ax - by) / c
		if (c != 0) {

			coords[0] = new Point3f(-1.0f, (d - a * -1.0f - b * -1.0f) / c,
					-1.0f);
			coords[1] = new Point3f(-1.0f, (d - a * -1.0f - b * 1.0f) / c, 1.0f);
			coords[2] = new Point3f(1.0f, (d - a * 1.0f - b * 1.0f) / c, 1.0f);
			coords[3] = new Point3f(1.0f, (d - a * 1.0f - b * -1.0f) / c, -1.0f);
		} else {

		}

		quadArray.setCoordinates(0, coords);

		return quadArray;
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		System.exit(1);
	}

	public static void main(String args[]) {
		cube3d myApp = new cube3d();

		myApp.setVisible(true);

	}

}

