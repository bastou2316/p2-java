package ch.hearc.p2.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Node;
import javax.media.j3d.PickInfo;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.View;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import ch.hearc.p2.java.model.Matrix;

import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.pickfast.PickCanvas;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * Mateli program based on the following sample program:
 * 
 * http://interactivemesh.org/testspace/orbitbehavior.html
 *
 * Version: 2.0 Date: 2009/04/08
 *
 * Copyright (c) 2009 August Lammersdorf, InteractiveMesh e.K. Kolomanstrasse
 * 2a, 85737 Ismaning Germany / Munich Area www.InteractiveMesh.com/org *
 * 
 */
final public class SimpleUniverseNavigation2 {

	private JFrame jFrame = null;

	private BoundingSphere globalBounds = null;

	private SimpleUniverse su = null;
	private View view = null;
	private Canvas3D canvas3D = null;

	private OrbitBehaviorInterim orbitBehInterim = null;
	private boolean isHomeRotCenter = true;
	private boolean isLookAtRotCenter = true;
	private boolean isPickVertex = true;

	private BranchGroup enviBranch = null;
	private BranchGroup sceneBranch = null;

	private BranchGroup plansBranch = null;
	private BranchGroup labelsBranch = null;

	private TransformGroup TG1;

	private PickCanvas pickCanvas = null;

	private Font font = null;
	private Color bgColor = Color.WHITE;
	private Color titleColor = Color.BLUE.darker();

	private float scaleFactor;
	private double[][] matrixValues;

	public float getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(float scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public static void main(String[] args) {
		// new SimpleUniverseNavigation2();
	}

	public SimpleUniverseNavigation2(Matrix mat) {
		matrixValues = mat.getValuesCopy();
		scaleFactor = 10f; // (car la solution est : (0.8, 1.6, 2.6) (x,y,z)

		createUniverse();

		su.addBranchGraph(sceneBranch);
		su.addBranchGraph(enviBranch);

		showCanvas3D();

		// Setup 'orbitBehInterim'
		Bounds sphereBounds = sceneBranch.getBounds();
		orbitBehInterim.setClippingBounds(sphereBounds);
		orbitBehInterim.setProjectionMode(View.PERSPECTIVE_PROJECTION);
		orbitBehInterim.setPureParallelEnabled(true);
		orbitBehInterim.goHome(true);

	}

	private void changeRotationCenter(MouseEvent event) {

		pickCanvas.setShapeLocation(event.getX(), event.getY());
		PickInfo pickInfo = pickCanvas.pickClosest();

		if (pickInfo != null) {

			Node pickedNode = pickInfo.getNode();

			Transform3D locToVWord = new Transform3D();
			pickedNode.getLocalToVworld(locToVWord);

			Point3d rotationPoint = new Point3d();

			if (isPickVertex) {
				rotationPoint.set(pickInfo.getClosestIntersectionPoint());
			} else {

				Bounds bounds = pickedNode.getBounds();
				if (bounds == null || bounds.isEmpty()) {
					JOptionPane.showMessageDialog(jFrame,
							"Selected Shape3D doesn't provide a not empty Bounds object! \n"
									+ "Can't set center of rotation ! \n \n",
							"Bounds missing", JOptionPane.ERROR_MESSAGE);
				}

				if (bounds instanceof BoundingBox) {
					BoundingBox pickedBox = (BoundingBox) bounds;

					Point3d lower = new Point3d();
					Point3d upper = new Point3d();
					pickedBox.getLower(lower);
					pickedBox.getUpper(upper);

					rotationPoint.set(lower.x + (upper.x - lower.x) / 2,
							lower.y + (upper.y - lower.y) / 2, lower.z
									+ (upper.z - lower.z) / 2);
				} else {
					BoundingSphere pickedSphere = null;
					if (bounds instanceof BoundingSphere)
						pickedSphere = (BoundingSphere) bounds;
					else
						pickedSphere = new BoundingSphere(bounds);

					pickedSphere.getCenter(rotationPoint);
				}

			}

			locToVWord.transform(rotationPoint);
			orbitBehInterim.setRotationCenter(rotationPoint, isLookAtRotCenter);
		}
	}

	// Base world
	private void createUniverse() {
		// Bounds
		globalBounds = new BoundingSphere();
		globalBounds.setRadius(Double.MAX_VALUE);

		// Canvas3D
		GraphicsConfiguration gcfg = SimpleUniverse.getPreferredConfiguration();
		try {
			canvas3D = new Canvas3D(gcfg);

			canvas3D.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent event) {
					int clicks = event.getClickCount();
					if (clicks == 2 && SwingUtilities.isLeftMouseButton(event)) {
						changeRotationCenter(event);
					}
				}
			});

			canvas3D.setBackground(bgColor);
		} catch (Exception e) {
			System.out.println("SimpleUniverseNavigation2: Canvas3D failed !!");
			e.printStackTrace();
			System.exit(0);
		}

		//
		// SimpleUniverse
		//
		su = new SimpleUniverse(canvas3D);

		//
		// View
		//
		view = su.getViewer().getView();

		//
		// BranchGraphs
		//
		sceneBranch = new BranchGroup();
		enviBranch = new BranchGroup();

		// OrbitBehaviorInterim
		TransformGroup viewTG = su.getViewingPlatform()
				.getViewPlatformTransform();

		orbitBehInterim = new OrbitBehaviorInterim(canvas3D, viewTG, view,
				OrbitBehaviorInterim.REVERSE_ALL);
		orbitBehInterim.setReverseZoom(false);
		orbitBehInterim.setSchedulingBounds(globalBounds);
		orbitBehInterim.setClippingEnabled(true);

		// Setting home view
		Transform3D homeTransform = new Transform3D();
		homeTransform.setRotation(new AxisAngle4d(-10.0f, 10.0f, 10.0f, Math
				.toRadians(50)));
		Transform3D t = new Transform3D();
		t.setTranslation(new Vector3f(0f, 0f, 20f));
		homeTransform.mul(t);
		orbitBehInterim.setHomeTransform(homeTransform);
		orbitBehInterim.setHomeRotationCenter(new Point3d(0.0, 0.0, 0.0));

		// Headlight
		DirectionalLight headLight = new DirectionalLight();
		headLight.setInfluencingBounds(globalBounds);

		// Scene is already live !!
		BranchGroup lightBG = new BranchGroup();
		lightBG.addChild(headLight);
		viewTG.addChild(lightBG);

		enviBranch.addChild(orbitBehInterim);
		// enviBranch.addChild(vpExecutor);

		// PickCanvas
		pickCanvas = new PickCanvas(canvas3D, sceneBranch);
		pickCanvas.setMode(PickInfo.PICK_GEOMETRY);
		pickCanvas
				.setFlags(PickInfo.NODE | PickInfo.CLOSEST_INTERSECTION_POINT);
		pickCanvas.setTolerance(4.0f);

		// EnviBranch

		Background bg = new Background();
		bg.setApplicationBounds(globalBounds);
		bg.setColor(new Color3f(bgColor));

		enviBranch.addChild(bg);

		// SceneBranch
		// on crée un groupe de transformation principal TG1

		// float transparancy = 0.65f;
		drawScene();
	}

	private void drawScene() {

		TG1 = new TransformGroup();
		TG1.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		TG1.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		// ------------ début de création des axes ------------

		LineAttributes lineAttr = new LineAttributes(4f, 0, true);
		Appearance lineApp = new Appearance();
		lineApp.setLineAttributes(lineAttr);

		// axe des X
		LineArray axisX = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisX.setCoordinate(0, new Point3f(0f, 0f, 0f));
		axisX.setCoordinate(1, new Point3f(0f, 0f, 4f));
		axisX.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		axisX.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));

		// axe des Y
		LineArray axisY = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisY.setCoordinate(0, new Point3f(0f, 0f, 0f));
		axisY.setCoordinate(1, new Point3f(4f, 0f, 0f));
		axisY.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		axisY.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));

		// axe des Z
		LineArray axisZ = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisZ.setCoordinate(0, new Point3f(0f, 0f, 0f));
		axisZ.setCoordinate(1, new Point3f(0f, 4f, 0f));
		axisZ.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		axisZ.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));

		TG1.addChild(new Shape3D(axisX, lineApp));
		TG1.addChild(new Shape3D(axisY, lineApp));
		TG1.addChild(new Shape3D(axisZ, lineApp));

		// création des labels des axes
		// Axe X
		Text2D textObject = new Text2D("X", new Color3f(0.2f, 0.2f, 0.2f),
				"Serif", 90, Font.BOLD);
		Transform3D textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(-0.3f, 0f, 2.5f));
		TransformGroup textTranslationGroup = new TransformGroup(
				textTranslation);
		textTranslationGroup.addChild(textObject);
		TG1.addChild(textTranslationGroup);

		// Apparence (visible des 2 côtés)
		Appearance app = textObject.getAppearance();
		PolygonAttributes pa = app.getPolygonAttributes();
		if (pa == null)
			pa = new PolygonAttributes();
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		if (app.getPolygonAttributes() == null)
			app.setPolygonAttributes(pa);

		/* Axe Y */
		textObject = new Text2D("Y", new Color3f(0.2f, 0.2f, 0.2f), "Serif",
				90, Font.BOLD);
		textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(2.2f, 0f, 0f));
		textTranslationGroup = new TransformGroup(textTranslation);
		textTranslationGroup.addChild(textObject);
		TG1.addChild(textTranslationGroup);

		// Apparence (visible des 2 côtés)
		app = textObject.getAppearance();
		pa = app.getPolygonAttributes();
		if (pa == null)
			pa = new PolygonAttributes();
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		if (app.getPolygonAttributes() == null)
			app.setPolygonAttributes(pa);

		/* Axe Z */
		textObject = new Text2D("Z", new Color3f(0.2f, 0.2f, 0.2f), "Serif",
				90, Font.BOLD);
		textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(-0.3f, 1.8f, 0f));
		textTranslationGroup = new TransformGroup(textTranslation);
		textTranslationGroup.addChild(textObject);
		TG1.addChild(textTranslationGroup);

		// Apparence (visible des 2 côtés)
		app = textObject.getAppearance();
		pa = app.getPolygonAttributes();
		if (pa == null)
			pa = new PolygonAttributes();
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		if (app.getPolygonAttributes() == null)
			app.setPolygonAttributes(pa);

		// création de 3 plans
		drawPlans();

		// plan1
		// Appearance app1 = new Appearance();
		// app1.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
		// 0.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
		// app1.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app1.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest = createPlan(-2.0f, 3.0f, 4.0f, 2.7f);
		// TG1.addChild(new Shape3D(quadArrTest, app1));
		//
		// // plan2
		// Appearance app2 = new Appearance();
		// app2.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
		// 1.0f, 0.0f), ColoringAttributes.SHADE_FLAT));
		// app2.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app2.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest2 = createPlan(2.0f, -3.0f, 1.3f, 3.7f);
		// TG1.addChild(new Shape3D(quadArrTest2, app2));
		//
		// // plan3
		// Appearance app3 = new Appearance();
		// app3.setColoringAttributes(new ColoringAttributes(new Color3f(1.0f,
		// 0.3f, 0.0f), ColoringAttributes.SHADE_GOURAUD));
		// app3.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app3.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest3 = createPlan(2.0f, 2.0f, 1.3f, 1.1f);
		// TG1.addChild(new Shape3D(quadArrTest3, app3));
		//
		// // plan4
		// Appearance app4 = new Appearance();
		// app4.setColoringAttributes(new ColoringAttributes(new Color3f(1.0f,
		// 0.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
		// app4.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app4.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest4 = createPlan(2.0f, 0f, 0f, 1.1f);
		// TG1.addChild(new Shape3D(quadArrTest4, app4));
		//
		// // plan5
		// Appearance app5 = new Appearance();
		// app5.setColoringAttributes(new ColoringAttributes(new Color3f(0f,
		// 1.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
		// app5.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app5.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest5 = createPlan(2.0f, 2f, 0f, 1.1f);
		// TG1.addChild(new Shape3D(quadArrTest5, app5));

		// Test1

		// scaleFactor = 10f; // (car la solution est : (0.8, 1.6, 2.6) (x,y,z)
		// plan1 (demo)
		// Appearance app1 = new Appearance();
		// app1.setColoringAttributes(new ColoringAttributes(new Color3f(0.1f,
		// 0.15f, 0.9f), ColoringAttributes.SHADE_GOURAUD));
		// app1.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app1.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest1 = createPlan(1.0f, -1.0f, 3.0f, 5f);
		// TG1.addChild(new Shape3D(quadArrTest1, app1));

		// Appearance app1bis = new Appearance();
		// app1bis.setColoringAttributes(new ColoringAttributes(new
		// Color3f(0.0f,
		// 0.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
		// app1bis.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NICEST, transparancy));
		// app1bis.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest1bis = createPlan(1.0f, -1.0f, 3.0f, 5f, 10f);
		// TG1.addChild(new Shape3D(quadArrTest1bis, app1bis));

		// plan2
		// Appearance app2 = new Appearance();
		// app2.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
		// 1.0f, 0.0f), ColoringAttributes.SHADE_FLAT));
		// app2.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app2.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest2 = createPlan(-4.0f, 3.0f, 1.0f, 5.0f);
		// TG1.addChild(new Shape3D(quadArrTest2, app2));

		// plan3 (demo)
		// Appearance app3 = new Appearance();
		// app3.setColoringAttributes(new ColoringAttributes(new Color3f(0.95f,
		// 0.05f, 0.05f), ColoringAttributes.SHADE_GOURAUD));
		// app3.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app3.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest3 = createPlan(1.0f, 1.0f, 1.0f, 5.0f);
		// TG1.addChild(new Shape3D(quadArrTest3, app3));

		// Appearance app3bis = new Appearance();
		// app3bis.setColoringAttributes(new ColoringAttributes(new
		// Color3f(1.0f,
		// 0.0f, 0.0f), ColoringAttributes.SHADE_GOURAUD));
		// app3bis.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NICEST, transparancy));
		// app3bis.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest3bis = createPlan(1.0f, 1.0f, 1.0f, 5.0f, 10f);
		// TG1.addChild(new Shape3D(quadArrTest3bis, app3bis));

		// plan4 (demo)
		// Appearance app4 = new Appearance();
		// app4.setColoringAttributes(new ColoringAttributes(new Color3f(0.1f,
		// 1.0f, 0.1f), ColoringAttributes.SHADE_GOURAUD));
		// app4.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app4.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest4 = createPlan(-0.5f, 0f, 1.f, 0f);
		// TG1.addChild(new Shape3D(quadArrTest4, app4));

		// Appearance app4bis = new Appearance();
		// app4bis.setColoringAttributes(new ColoringAttributes(new
		// Color3f(0.0f,
		// 1.0f, 0.0f), ColoringAttributes.SHADE_GOURAUD));
		// app4bis.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NICEST, transparancy));
		// app4bis.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest4bis = createPlan(-0.5f, 0.0f, 1f, 0f, 10f);
		// TG1.addChild(new Shape3D(quadArrTest4bis, app4bis));

		// //Test2
		// scaleFactor = 2f; // pas de solutions, on le voit bien avec cette
		// échelle
		// // plan1
		// Appearance app1 = new Appearance();
		// app1.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
		// 0.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
		// app1.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app1.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest = createPlan(1.0f, 2.0f, -1.0f, 1.0f);
		// TG1.addChild(new Shape3D(quadArrTest, app1));
		//
		// // plan2
		// Appearance app2 = new Appearance();
		// app2.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
		// 1.0f, 0.0f), ColoringAttributes.SHADE_FLAT));
		// app2.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app2.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest2 = createPlan(2.0f, 1.0f, 2.0f, 2.0f);
		// TG1.addChild(new Shape3D(quadArrTest2, app2));
		//
		// // plan3
		// Appearance app3 = new Appearance();
		// app3.setColoringAttributes(new ColoringAttributes(new Color3f(1.0f,
		// 0.3f, 0.0f), ColoringAttributes.SHADE_GOURAUD));
		// app3.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app3.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest3 = createPlan(1.0f, -4.0f, 7.0f, 3.0f);
		// TG1.addChild(new Shape3D(quadArrTest3, app3));

		// // Test3
		// scaleFactor = 1f; // pas de solutions
		//
		// // plan1
		// Appearance app1 = new Appearance();
		// app1.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
		// 0.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
		// app1.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app1.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest = createPlan(1.0f, -1.0f, 0.0f, 0.0f);
		// TG1.addChild(new Shape3D(quadArrTest, app1));
		//
		// // plan2
		// Appearance app2 = new Appearance();
		// app2.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
		// 1.0f, 0.0f), ColoringAttributes.SHADE_FLAT));
		// app2.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app2.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest2 = createPlan(0.0f, 1.0f, -1.0f, 1.0f);
		// TG1.addChild(new Shape3D(quadArrTest2, app2));
		//
		// // plan3
		// Appearance app3 = new Appearance();
		// app3.setColoringAttributes(new ColoringAttributes(new Color3f(1.0f,
		// 0.3f, 0.0f), ColoringAttributes.SHADE_GOURAUD));
		// app3.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app3.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest3 = createPlan(-1.0f, 0.0f, 1.0f, 0.0f);
		// TG1.addChild(new Shape3D(quadArrTest3, app3));

		// Test4
		// scaleFactor = 1f; // infinité de solutions
		//
		// // plan1
		// Appearance app1 = new Appearance();
		// app1.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
		// 0.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
		// app1.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app1.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest = createPlan(1.0f, 0.0f, -1.0f, 1.0f);
		// TG1.addChild(new Shape3D(quadArrTest, app1));
		//
		// // plan2
		// Appearance app2 = new Appearance();
		// app2.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
		// 1.0f, 0.0f), ColoringAttributes.SHADE_FLAT));
		// app2.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app2.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest2 = createPlan(0.0f, -1.0f, 1.0f, 0.0f);
		// TG1.addChild(new Shape3D(quadArrTest2, app2));
		//
		// // plan3
		// Appearance app3 = new Appearance();
		// app3.setColoringAttributes(new ColoringAttributes(new Color3f(1.0f,
		// 0.3f, 0.0f), ColoringAttributes.SHADE_GOURAUD));
		// app3.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app3.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest3 = createPlan(1.0f, 1.0f, -2.0f, 1.0f);
		// TG1.addChild(new Shape3D(quadArrTest3, app3));

		// // Test5 (S4)
		// scaleFactor = 10f; // car solution unique { x = -5, y = 3, z = 3 }.
		// // si solution unique, prendre max(|x| , |y|) * 2
		//
		// // plan1
		// Appearance app1 = new Appearance();
		// app1.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
		// 0.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
		// app1.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app1.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest = createPlan(2.0f, 3.0f, -5.0f, -16.0f);
		// TG1.addChild(new Shape3D(quadArrTest, app1));
		//
		// // plan2
		// Appearance app2 = new Appearance();
		// app2.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
		// 1.0f, 0.0f), ColoringAttributes.SHADE_FLAT));
		// app2.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app2.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest2 = createPlan(4.0f, 13.0f, -8.0f, -5.0f);
		// TG1.addChild(new Shape3D(quadArrTest2, app2));
		//
		// // plan3
		// Appearance app3 = new Appearance();
		// app3.setColoringAttributes(new ColoringAttributes(new Color3f(1.0f,
		// 0.3f, 0.0f), ColoringAttributes.SHADE_GOURAUD));
		// app3.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app3.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest3 = createPlan(1.0f, 2.0f, 7.0f, 22.0f);
		// TG1.addChild(new Shape3D(quadArrTest3, app3));

		// Test6 (pour poster)
		// scaleFactor = 4f; // pas de solutions
		//
		// // plan1
		// Appearance app1 = new Appearance();
		// app1.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
		// 0.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
		// app1.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app1.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest = createPlan(1.0f, 1.0f, 1.0f, 4.0f);
		// TG1.addChild(new Shape3D(quadArrTest, app1));
		//
		// // plan2
		// Appearance app2 = new Appearance();
		// app2.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
		// 1.0f, 0.0f), ColoringAttributes.SHADE_FLAT));
		// app2.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app2.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest2 = createPlan(1.0f, -2.5f, 1.0f, -0.6f);
		// TG1.addChild(new Shape3D(quadArrTest2, app2));
		//
		// // plan3
		// Appearance app3 = new Appearance();
		// app3.setColoringAttributes(new ColoringAttributes(new Color3f(1.0f,
		// 0.3f, 0.0f), ColoringAttributes.SHADE_GOURAUD));
		// app3.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app3.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest3 = createPlan(0.0f, 1.0f, -0.5f, 0.0f);
		// TG1.addChild(new Shape3D(quadArrTest3, app3));

		// Ticks pour l'échelle
		drawTicks();
		drawAxisLabels();

		// Bounding cube
		LineAttributes cubeAttr = new LineAttributes(2f, 0, true);
		Appearance cubeApp = new Appearance();
		cubeApp.setLineAttributes(cubeAttr);

		LineArray cube = new LineArray(24, LineArray.COORDINATES
				| LineArray.COLOR_3);
		cube.setCoordinate(0, new Point3f(1f, 1f, 1f));
		cube.setCoordinate(1, new Point3f(1f, 1f, -1f));

		cube.setCoordinate(2, new Point3f(1f, 1f, 1f));
		cube.setCoordinate(3, new Point3f(1f, -1f, 1f));

		cube.setCoordinate(4, new Point3f(1f, 1f, 1f));
		cube.setCoordinate(5, new Point3f(-1f, 1f, 1f));

		cube.setCoordinate(6, new Point3f(1f, 1f, -1f));
		cube.setCoordinate(7, new Point3f(1f, -1f, -1f));

		cube.setCoordinate(8, new Point3f(1f, 1f, -1f));
		cube.setCoordinate(9, new Point3f(-1f, 1f, -1f));

		cube.setCoordinate(10, new Point3f(1f, -1f, 1f));
		cube.setCoordinate(11, new Point3f(1f, -1f, -1f));

		cube.setCoordinate(12, new Point3f(1f, -1f, 1f));
		cube.setCoordinate(13, new Point3f(-1f, -1f, 1f));

		cube.setCoordinate(14, new Point3f(-1f, 1f, 1f));
		cube.setCoordinate(15, new Point3f(-1f, -1f, 1f));

		cube.setCoordinate(16, new Point3f(-1f, 1f, 1f));
		cube.setCoordinate(17, new Point3f(-1f, 1f, -1f));

		cube.setCoordinate(18, new Point3f(-1f, -1f, 1f));
		cube.setCoordinate(19, new Point3f(-1f, -1f, -1f));

		cube.setCoordinate(20, new Point3f(-1f, 1f, -1f));
		cube.setCoordinate(21, new Point3f(-1f, -1f, -1f));

		cube.setCoordinate(22, new Point3f(1f, -1f, -1f));
		cube.setCoordinate(23, new Point3f(-1f, -1f, -1f));

		TG1.addChild(new Shape3D(cube, cubeApp));

		sceneBranch = new BranchGroup();
		sceneBranch.addChild(TG1);

		// TG1.removeChild(shape);
		// cube.setCoordinate(23, new Point3f(-10f, -1f, -1f));
		// TG1.addChild(shape);
	}

	private void drawTicks() {
		LineAttributes lineAttr = new LineAttributes(4f, 0, true);
		Appearance lineApp = new Appearance();
		lineApp.setLineAttributes(lineAttr);

		// Axe X
		LineArray tickX = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		tickX.setCoordinate(0, new Point3f(1.0f, 0f, -0.08f));
		tickX.setCoordinate(1, new Point3f(1.0f, 0f, 0.08f));
		tickX.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		tickX.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));

		TG1.addChild(new Shape3D(tickX, lineApp));

		/* Axe Y */
		LineArray tickY = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		tickY.setCoordinate(0, new Point3f(-0.08f, 0f, 1f));
		tickY.setCoordinate(1, new Point3f(0.08f, 0f, 1f));
		tickY.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		tickY.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		TG1.addChild(new Shape3D(tickY, lineApp));
		// tabTG1Shapes.add(new Shape3D(tickY, lineApp));

		/* Axe Z */
		LineArray tickZ = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		tickZ.setCoordinate(0, new Point3f(0f, 1f, -0.08f));
		tickZ.setCoordinate(1, new Point3f(0f, 1f, 0.08f));
		tickZ.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		tickZ.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		TG1.addChild(new Shape3D(tickZ, lineApp));
		// tabTG1Shapes.add(new Shape3D(tickZ, lineApp));
		//
		// for(Shape3D el : tabTG1Shapes){
		// TG1.addChild(el);
		// }
	}

	private void drawAxisLabels() {
		labelsBranch = new BranchGroup();
		labelsBranch.setCapability(BranchGroup.ALLOW_DETACH);

		// Axe X
		Text2D tickValue = new Text2D(scaleFactor + "", new Color3f(0.2f, 0.2f,
				0.2f), "Serif", 70, Font.ITALIC);
		Transform3D textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(1f, 0f, 0f));

		TransformGroup textTranslationGroup = new TransformGroup(
				textTranslation);
		textTranslationGroup = new TransformGroup(textTranslation);
		textTranslationGroup.addChild(tickValue);
		labelsBranch.addChild(textTranslationGroup);

		// Axe Y
		tickValue = new Text2D(scaleFactor + "", new Color3f(0.2f, 0.2f, 0.2f),
				"Serif", 70, Font.ITALIC);
		textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(0f, 0f, 1f));
		textTranslationGroup = new TransformGroup(textTranslation);
		textTranslationGroup.addChild(tickValue);
		labelsBranch.addChild(textTranslationGroup);

		// Axe Z
		tickValue = new Text2D(scaleFactor + "", new Color3f(0.2f, 0.2f, 0.2f),
				"Serif", 70, Font.ITALIC);
		textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(0f, 1f, 0f));
		textTranslationGroup = new TransformGroup(textTranslation);
		textTranslationGroup.addChild(tickValue);
		labelsBranch.addChild(textTranslationGroup);

		// Apparence (visible des 2 côtés)
		Appearance appTick = tickValue.getAppearance();
		PolygonAttributes paTick = appTick.getPolygonAttributes();
		if (paTick == null)
			paTick = new PolygonAttributes();
		paTick.setCullFace(PolygonAttributes.CULL_NONE);
		if (appTick.getPolygonAttributes() == null)
			appTick.setPolygonAttributes(paTick);

		TG1.addChild(labelsBranch);
	}

	private void drawPlans() {
		PolygonAttributes polyAttr = new PolygonAttributes();
		polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
		polyAttr.setBackFaceNormalFlip(true);

		double[] equ1 = matrixValues[0];
		double[] equ2 = matrixValues[1];
		double[] equ3 = matrixValues[2];

		plansBranch = new BranchGroup();
		plansBranch.setCapability(BranchGroup.ALLOW_DETACH);

		// plan1
		Appearance app1 = new Appearance();
		app1.setColoringAttributes(new ColoringAttributes(new Color3f(0.1f,
				0.15f, 0.9f), ColoringAttributes.SHADE_GOURAUD));
		app1.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.NONE, 0.5f));
		app1.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest1 = createPlan(1.0f, -1.0f, 3.0f, 5f);
		QuadArray quadArrTest1 = createPlan((float) equ1[0], (float) equ1[1],
				(float) equ1[2], (float) equ1[3]);
		plansBranch.addChild(new Shape3D(quadArrTest1, app1));

		// Appearance app1bis = new Appearance();
		// app1bis.setColoringAttributes(new ColoringAttributes(new
		// Color3f(0.0f,
		// 0.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
		// app1bis.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NICEST, transparancy));
		// app1bis.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest1bis = createPlan(1.0f, -1.0f, 3.0f, 5f, 10f);
		// TG1.addChild(new Shape3D(quadArrTest1bis, app1bis));

		// plan2
		Appearance app2 = new Appearance();
		app2.setColoringAttributes(new ColoringAttributes(new Color3f(0.95f,
				0.05f, 0.05f), ColoringAttributes.SHADE_GOURAUD));
		app2.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.NONE, 0.5f));
		app2.setPolygonAttributes(polyAttr);
		QuadArray quadArrTest2 = createPlan((float) equ2[0], (float) equ2[1],
				(float) equ2[2], (float) equ2[3]);
		plansBranch.addChild(new Shape3D(quadArrTest2, app2));

		// Appearance app2bis = new Appearance();
		// app2bis.setColoringAttributes(new ColoringAttributes(new
		// Color3f(1.0f,
		// 0.0f, 0.0f), ColoringAttributes.SHADE_GOURAUD));
		// app2bis.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NICEST, transparancy));
		// app2bis.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest2bis = createPlan(1.0f, 1.0f, 1.0f, 5.0f, 10f);
		// TG1.addChild(new Shape3D(quadArrTest2bis, app2bis));

		// plan3
		Appearance app3 = new Appearance();
		app3.setColoringAttributes(new ColoringAttributes(new Color3f(0.1f,
				1.0f, 0.1f), ColoringAttributes.SHADE_GOURAUD));
		app3.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.NONE, 0.5f));
		app3.setPolygonAttributes(polyAttr);
		QuadArray quadArrTest3 = createPlan((float) equ3[0], (float) equ3[1],
				(float) equ3[2], (float) equ3[3]);
		plansBranch.addChild(new Shape3D(quadArrTest3, app3));

		TG1.addChild(plansBranch);

	}

	private QuadArray createPlan(float a, float b, float c, float d) {
		return createPlan(a, b, c, d, 1f);
	}

	private QuadArray createPlan(float a, float b, float c, float d,
			float _scale) {
		QuadArray quadArray = new QuadArray(4, GeometryArray.COORDINATES);
		Point3f[] coords = new Point3f[4];

		float scale = _scale;
		// Point3f(y,z,x)
		if (c != 0) {
			// On a : ax + by + cz = d
			// Donc : z = (d - ax - by) / c
			coords[0] = new Point3f(-scale, (d - a * -scaleFactor * scale - b
					* -scaleFactor * scale)
					/ (scaleFactor * c), -scale);
			coords[1] = new Point3f(-scale, (d - a * scaleFactor * scale - b
					* -scaleFactor * scale)
					/ (scaleFactor * c), scale);
			coords[2] = new Point3f(scale, (d - a * scaleFactor * scale - b
					* scaleFactor * scale)
					/ (scaleFactor * c), scale);
			coords[3] = new Point3f(scale, (d - a * -scaleFactor * scale - b
					* scaleFactor * scale)
					/ (scaleFactor * c), -scale);
		} else if (b != 0) { // plan vertical
			// On a : ax + by = d
			// Donc : y = (d - ax) / b
			// hauteur 10 (axe z) => (peut être à changer)
			coords[0] = new Point3f((d - a * -scaleFactor * scale)
					/ (scaleFactor * b), -scale, -scale);
			coords[1] = new Point3f((d - a * -scaleFactor * scale)
					/ (scaleFactor * b), scale, -scale);
			coords[2] = new Point3f((d - a * scaleFactor * scale)
					/ (scaleFactor * b), scale, scale);
			coords[3] = new Point3f((d - a * scaleFactor * scale)
					/ (scaleFactor * b), -scale, scale);
		} else if (a != 0) { // plan vertical
			// On a : ax = d
			// Donc : x = d/a
			// hauteur 10 (axe z) => (peut être à changer)
			coords[0] = new Point3f(-scale, -scale, d / (a * scaleFactor));
			coords[1] = new Point3f(scale, -scale, d / (a * scaleFactor));
			coords[2] = new Point3f(scale, scale, d / (a * scaleFactor));
			coords[3] = new Point3f(-scale, scale, d / (a * scaleFactor));
		} else {
			System.err
					.println("equation error : cannot display this plan (0x + 0y + 0z = "
							+ d + ")");
		}

		quadArray.setCoordinates(0, coords);

		return quadArray;
	}

	// Create GUI
	private void showCanvas3D() {

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenDim = toolkit.getScreenSize();

		//
		// GUI / User actions
		//

		int fontSize = 14;
		if (screenDim.height < 1024)
			fontSize = 11;
		else if (screenDim.height < 1200)
			fontSize = 12;

		font = new Font("SansSerif", Font.BOLD, fontSize);

		JPanel jPanelLine = new JPanel(new BorderLayout());
		jPanelLine.setBackground(new Color(0.05f, 0.05f, 0.5f));
		jPanelLine.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

		JPanel jPanelBaseX = new JPanel();
		jPanelBaseX.setLayout(new BoxLayout(jPanelBaseX, BoxLayout.X_AXIS));
		// jPanelBaseX.setBackground(bgColor);
		jPanelBaseX.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

		jPanelLine.add(jPanelBaseX, BorderLayout.CENTER);

		JPanel jPanelBaseYBorder = new JPanel();
		jPanelBaseYBorder.setLayout(new BoxLayout(jPanelBaseYBorder,
				BoxLayout.X_AXIS));
		jPanelBaseYBorder.setBackground(bgColor);
		jPanelBaseYBorder.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLoweredBevelBorder(),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		JPanel jPanelBaseY = new JPanel();
		jPanelBaseY.setLayout(new BoxLayout(jPanelBaseY, BoxLayout.Y_AXIS));
		jPanelBaseY.setBackground(bgColor);
		jPanelBaseY.setBorder(BorderFactory.createEmptyBorder());

		jPanelBaseYBorder.add(jPanelBaseY);

		//
		// Actions
		//
		JPanel jPanelActions = new JPanel();
		jPanelActions.setLayout(new BoxLayout(jPanelActions, BoxLayout.X_AXIS));
		jPanelActions.setBackground(bgColor);
		jPanelActions.setBorder(BorderFactory.createEmptyBorder());
		jPanelActions.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Home Transform
		JPanel jPanelActionHome = new JPanel();
		BoxLayout boxLayoutHome = new BoxLayout(jPanelActionHome,
				BoxLayout.Y_AXIS);
		jPanelActionHome.setLayout(boxLayoutHome);

		TitledBorder tBorderHome = BorderFactory
				.createTitledBorder(" Réinitialisation et centrage");
		tBorderHome.setTitleFont(font);
		tBorderHome.setTitleColor(titleColor);
		// tBorderHome.setBorder(BorderFactory.createLineBorder(bgColor));

		jPanelActionHome.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderHome));

		JButton jButtonHomeTransform = new JButton("Retour à la vue de départ");
		jButtonHomeTransform.setFont(font);
		// jButtonHomeTransform.setForeground(bgColor);
		jButtonHomeTransform.setAlignmentX(Component.CENTER_ALIGNMENT);

		jButtonHomeTransform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// heavy-weight !!
				view.stopView();
				orbitBehInterim.goHome(isHomeRotCenter);
				view.startView();

			}
		});

		JButton jButtonLookAt = new JButton("Centrer");
		jButtonLookAt.setFont(font);
		// jButtonLookAt.setForeground(bgColor);
		jButtonLookAt.setAlignmentX(Component.CENTER_ALIGNMENT);

		jButtonLookAt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				orbitBehInterim.lookAtRotationCenter();

			}
		});

		jPanelActionHome.add(Box.createVerticalStrut(5));
		jPanelActionHome.add(jButtonHomeTransform);
		jPanelActionHome.add(Box.createVerticalStrut(10));
		jPanelActionHome.add(jButtonLookAt);

		jPanelActions.add(jPanelActionHome);
		jPanelActions.add(Box.createHorizontalStrut(10));

		// Projection
		JPanel jPanelActionProjection = new JPanel();
		jPanelActionProjection.setLayout(new BoxLayout(jPanelActionProjection,
				BoxLayout.Y_AXIS));

		TitledBorder tBorderProjection = BorderFactory
				.createTitledBorder(" Projection ");
		tBorderProjection.setTitleFont(font);
		tBorderProjection.setTitleColor(titleColor);
		// tBorderProjection.setBorder(BorderFactory.createLineBorder(bgColor));

		jPanelActionProjection.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderProjection));

		final JRadioButton jRadioParallel = new JRadioButton("Parallèle");
		final JRadioButton jRadioPerspective = new JRadioButton("Perspective");
		jRadioPerspective.setSelected(true);

		jRadioParallel.setFont(font);
		jRadioPerspective.setFont(font);
		// jRadioParallel.setForeground(bgColor);
		// jRadioPerspective.setForeground(bgColor);

		jRadioPerspective.setAlignmentX(Component.LEFT_ALIGNMENT);

		ActionListener projectionListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				int projMode = View.PERSPECTIVE_PROJECTION;
				if (jRadioParallel.isSelected())
					projMode = View.PARALLEL_PROJECTION;

				orbitBehInterim.setProjectionMode(projMode);

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

		jPanelActionProjection.add(jPanelParProj);
		jPanelActionProjection.add(jRadioPerspective);

		jPanelActions.add(jPanelActionProjection);
		jPanelActions.add(Box.createHorizontalStrut(10));

		// Scale
		JPanel jPanelActionScale = new JPanel();
		jPanelActionScale.setLayout(new BoxLayout(jPanelActionScale,
				BoxLayout.Y_AXIS));

		TitledBorder tBorderScale = BorderFactory
				.createTitledBorder("Box Scale");
		tBorderScale.setTitleFont(font);
		tBorderScale.setTitleColor(titleColor);
		// tBorderScale.setBorder(BorderFactory.createLineBorder(bgColor));

		jPanelActionScale.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderScale));

		final JSlider jSliderScale = new JSlider(1, 100, (int) scaleFactor);
		jSliderScale.setFont(font);
		jSliderScale.setAlignmentX(Component.LEFT_ALIGNMENT);
		jSliderScale.setMajorTickSpacing(10);
		jSliderScale.setPaintTicks(true);

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(1), new JLabel("1"));
		labelTable.put(new Integer(10), new JLabel("10"));
		labelTable.put(new Integer(100), new JLabel("100"));

		jSliderScale.setLabelTable(labelTable);
		jSliderScale.setPaintLabels(true);

		jSliderScale.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// if (!jSliderScale.getValueIsAdjusting()) {
				scaleFactor = (float) jSliderScale.getValue();

				TG1.removeChild(plansBranch);
				TG1.removeChild(labelsBranch);
				
				drawPlans();
				drawAxisLabels();
				
				//si on veut juste cacher puis réafficher (exemple avec les plans):
//				TG1.removeChild(plansBranch);
//				TG1.addChild(plansBranch);
				
				// }
			}
		});

		JPanel jPanelScale = new JPanel();
		jPanelScale.setLayout(new BoxLayout(jPanelScale, BoxLayout.X_AXIS));
		jPanelScale.setAlignmentX(Component.LEFT_ALIGNMENT);

		jPanelScale.add(jSliderScale);
		jPanelActionScale.add(jPanelScale);
		jPanelActions.add(jPanelActionScale);

		jPanelBaseY.add(jPanelActions);
		jPanelBaseY.add(Box.createVerticalStrut(10));

		Dimension dimVps = jPanelActions.getPreferredSize();
		dimVps.height = jPanelBaseY.getPreferredSize().height;
		jPanelBaseY.setPreferredSize(dimVps);
		jPanelBaseY.setMaximumSize(dimVps);

		jPanelBaseX.add(Box.createHorizontalGlue());
		jPanelBaseX.add(jPanelBaseYBorder);
		jPanelBaseX.add(Box.createHorizontalGlue());

		Dimension dim = new Dimension(screenDim.width - 20,
				screenDim.height - 300);
		canvas3D.setPreferredSize(dim);
		canvas3D.setSize(dim);
		
		JPanel jPanel = new JPanel(new BorderLayout());
		jPanel.add(canvas3D, BorderLayout.CENTER);
		jPanel.add(jPanelLine, BorderLayout.SOUTH);

		// JFrame
		jFrame = new JFrame();
		jFrame.setTitle("Vue 3D du système d'équation linéaire à 3 inconnues");
		jFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		jFrame.add(jPanel);
		jFrame.pack();

		Dimension jframeDim = jFrame.getSize();
		jFrame.setLocation((screenDim.width - jframeDim.width) / 2,
				(screenDim.height - jframeDim.height) / 2);

		jFrame.setVisible(true);
	}
}
