package ch.hearc.p2.java;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class SimpleClip extends Frame {

	/**
	 * Fonction de cr�ation de la sc�ne 3D
	 */
	public BranchGroup createSceneGraph(SimpleUniverse u) {
		// Objet Root
		BranchGroup objRoot = new BranchGroup();

		// Param�trage de l'eclairage
		BoundingSphere bounds;
		bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

		Vector3f ldir = new Vector3f(1.0F, -1.0F, -1.0F);
		Color3f lcouldl = new Color3f(1.0F, 1.0F, 1.0F);

		DirectionalLight dl = new DirectionalLight(lcouldl, ldir);
		dl.setInfluencingBounds(bounds);

		objRoot.addChild(dl);

		Color3f lColor1 = new Color3f(0.5f, 0.0f, 0.5f);
		Vector3f lDir1 = new Vector3f(-1.0f, -1.0f, 1.0f);

		DirectionalLight lgt1 = new DirectionalLight(lColor1, lDir1);
		lgt1.setInfluencingBounds(bounds);

		objRoot.addChild(lgt1);

		Vector3f lDir2 = new Vector3f(1.0f, 1.0f, -1.0f);
		Color3f lColor2 = new Color3f(0.7f, 0.7f, 0.0f);

		DirectionalLight lgt2 = new DirectionalLight(lColor2, lDir2);
		lgt2.setInfluencingBounds(bounds);

		objRoot.addChild(lgt2);

		// Configuration des g�om�tries
		{ // Un cylindre
			TransformGroup objTrans = new TransformGroup();
			objRoot.addChild(objTrans);

			Transform3D t3D = new Transform3D();
			t3D.setRotation(new AxisAngle4d(-1.0, 2.0, -1.0, Math.PI / 5.0));
			objTrans.setTransform(t3D);

			Appearance a = new Appearance();
			Material m = new Material();
			m.setDiffuseColor(0.3f, 0.7f, 1.0f);

			PolygonAttributes attr = new PolygonAttributes();
			attr.setCullFace(PolygonAttributes.CULL_NONE);
			a.setPolygonAttributes(attr);
			a.setMaterial(m);
			objTrans.addChild(new Cylinder(0.4F, 0.9F, 1, 70, 70, a));
		}
		{ // Un cube/pav� ...
			TransformGroup objTrans = new TransformGroup();
			objRoot.addChild(objTrans);

			Transform3D t3D = new Transform3D();
			t3D.setRotation(new AxisAngle4d(1.0, 2.0, 1.0, Math.PI / 5.0));
			objTrans.setTransform(t3D);

			Appearance a = new Appearance();
			Material m = new Material();
			m.setDiffuseColor(1.0f, 0.6f, 0.4f);
			m.setAmbientColor(0.4f, 0.2f, 0.1f);

			PolygonAttributes attr = new PolygonAttributes();
			attr.setCullFace(PolygonAttributes.CULL_NONE);
			a.setPolygonAttributes(attr);
			a.setMaterial(m);

			objTrans.addChild(new Box(0.25F, 0.45F, 0.6F, a));
		}

		// Configuration des coupes et de leurs rotations (animations)
		{
			ModelClip mc = new ModelClip();

			boolean enables[] = { false, false, false, false, false, false };

			Vector4d eqn1 = new Vector4d(0.0, 1.0, 0.0, 0.0);
			Vector4d eqn2 = new Vector4d(1.0, 1.0, 0.0, 0.0);

			mc.setEnables(enables);

			mc.setPlane(1, eqn1);
			mc.setPlane(2, eqn2);

			mc.setEnable(1, true);
			mc.setEnable(2, true);

			mc.setInfluencingBounds(bounds);

			TransformGroup objTrans1 = new TransformGroup();

			objRoot.addChild(objTrans1);
			objTrans1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

			Transform3D yAxis = new Transform3D();

			Alpha rotationAlpha;
			rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0, 4000,
					0, 0, 0, 0, 0);

			RotationInterpolator rotator;
			rotator = new RotationInterpolator(rotationAlpha, objTrans1, yAxis,
					0.0f, (float) Math.PI * 2.0f);
			rotator.setSchedulingBounds(bounds);

			objTrans1.addChild(rotator);

			TransformGroup objTrans2 = new TransformGroup();
			objTrans1.addChild(objTrans2);

			Transform3D t3D = new Transform3D();
			t3D.setRotation(new AxisAngle4d(1.0, 1.0, 0.0, Math.PI / 5.0));
			objTrans2.setTransform(t3D);

			objTrans2.addChild(mc);
		}

		// Compilation de la sc�ne
		objRoot.compile();

		return objRoot;
	}

	/**
	 * Constructeur
	 */
	public SimpleClip() {
		setLayout(new BorderLayout());

		// Param�trage de Java3D, r�cup�ration de l'envirronement graphique
		GraphicsConfiguration config;
		config = SimpleUniverse.getPreferredConfiguration();

		// Cr�ation du canvas 3D
		Canvas3D c = new Canvas3D(config);
		add("Center", c);

		// Int�gration de la sc�ne 3D
		SimpleUniverse u = new SimpleUniverse(c);
		BranchGroup scene = createSceneGraph(u);
		u.getViewingPlatform().setNominalViewingTransform();
		u.addBranchGraph(scene);
	}

	/**
	 * Proc�dure d'�x�cution.
	 */
	public static void main(String[] args) {
		SimpleClip f = new SimpleClip();
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f.setSize(300, 250);
		f.setVisible(true);
		f.setLocationRelativeTo(null);
	}
}