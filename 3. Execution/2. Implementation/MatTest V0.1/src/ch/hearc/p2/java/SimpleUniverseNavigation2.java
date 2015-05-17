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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.Behavior;
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
import javax.media.j3d.WakeupOnBehaviorPost;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.pickfast.PickCanvas;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * Mateli program based on the following sample program:
 * 
 * 'SimpleUniverseNavigation2' is built on a SimpleUniverse, renders into a
 * Canvas3D, provides navigation by OrbitBehaviorInterim (2.1) and a (simple)
 * viewpoint implementation as inner classes (VantagePoint and
 * VantagePointBehavior), and supports center of rotation picking by a double
 * clicked left mouse button based on the utility PickCanvas.
 *
 * Version: 2.0 Date: 2009/04/08
 *
 * Copyright (c) 2009 August Lammersdorf, InteractiveMesh e.K. Kolomanstrasse
 * 2a, 85737 Ismaning Germany / Munich Area www.InteractiveMesh.com/org
 * 
 * Please create your own implementation. This source code is provided "AS IS",
 * without warranty of any kind. You are allowed to copy and use all lines you
 * like of this source code without any copyright notice, but you may not
 * modify, compile, or distribute this 'SimpleUniverseNavigation2'.
 * 
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

	private BranchGroup sceneBranch = null;
	private BranchGroup enviBranch = null;

	private PickCanvas pickCanvas = null;

	private Font font = null;
	private Color bgColor = new Color(0.05f, 0.05f, 0.5f);

	private double defaultFoV = Math.PI / 4;
	private JSlider jSliderFoV = null;
	private ChangeListener fovSliderListener = null;
	private JTextField jTextFoVDegree = null;
	private JTextField jTextFoVRadians = null;

	private VantagePointBehavior vpExecutor = null;

	private LinkedHashMap<String, VantagePoint> vantagepointHM = new LinkedHashMap<String, VantagePoint>();

	private float scaleFactor;

	public float getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(float scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public static void main(String[] args) {
		new SimpleUniverseNavigation2();
	}

	SimpleUniverseNavigation2() {

		createVantagePoints();

		createUniverse();

		su.addBranchGraph(sceneBranch);
		su.addBranchGraph(enviBranch);

		showCanvas3D();

		// Setup 'orbitBehInterim'
		Bounds sphereBounds = sceneBranch.getBounds();
		orbitBehInterim.setClippingBounds(sphereBounds);
		orbitBehInterim.setProjectionMode(View.PARALLEL_PROJECTION);
		orbitBehInterim.setPureParallelEnabled(true);
		orbitBehInterim.goHome(true);
		orbitBehInterim.setReverseZoom(false);

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
		defaultFoV = view.getFieldOfView();

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
		orbitBehInterim.setSchedulingBounds(globalBounds);
		orbitBehInterim.setClippingEnabled(true);

		Transform3D homeTransform = new Transform3D();
		homeTransform.setTranslation(new Vector3d(0.0, 0.75, 15.0));
		orbitBehInterim.setHomeTransform(homeTransform);
		orbitBehInterim.setHomeRotationCenter(new Point3d(0.0, 0.0, 1.0));

		// VantagePointBehavior
		vpExecutor = new VantagePointBehavior(orbitBehInterim);
		vpExecutor.setSchedulingBounds(globalBounds);

		// Headlight
		DirectionalLight headLight = new DirectionalLight();
		headLight.setInfluencingBounds(globalBounds);

		// Scene is already live !!
		BranchGroup lightBG = new BranchGroup();
		lightBG.addChild(headLight);
		viewTG.addChild(lightBG);

		enviBranch.addChild(orbitBehInterim);
		enviBranch.addChild(vpExecutor);

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
		TransformGroup TG1 = new TransformGroup();

		// ------------ début de création des axes ------------

		LineAttributes lineAttr = new LineAttributes(3f, 0, true);
		Appearance lineApp = new Appearance();
		lineApp.setLineAttributes(lineAttr);

		// axe des X
		LineArray axisX = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisX.setCoordinate(0, new Point3f(-2f, 0f, 0f));
		axisX.setCoordinate(1, new Point3f(2f, 0f, 0f));
		axisX.setColor(0, new Color3f(0.5f, 0.5f, 0.5f));
		axisX.setColor(1, new Color3f(0f, 0f, 0f));

		// axe des Y
		LineArray axisY = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisY.setCoordinate(0, new Point3f(0f, 0f, -2f));
		axisY.setCoordinate(1, new Point3f(0f, 0f, 2f));
		axisY.setColor(0, new Color3f(0.5f, 0.5f, 0.5f));
		axisY.setColor(1, new Color3f(0f, 0f, 0f));

		// axe des Z
		LineArray axisZ = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisZ.setCoordinate(0, new Point3f(0f, -2f, 0f));
		axisZ.setCoordinate(1, new Point3f(0f, 2f, 0f));
		axisZ.setColor(0, new Color3f(0.5f, 0.5f, 0.5f));
		axisZ.setColor(1, new Color3f(0f, 0f, 0f));

		TG1.addChild(new Shape3D(axisX, lineApp));
		TG1.addChild(new Shape3D(axisY, lineApp));
		TG1.addChild(new Shape3D(axisZ, lineApp));

		// création des labels des axes
		// Axe X
		Text2D textObject = new Text2D("X", new Color3f(1f, 0f, 0f), "Serif",
				70, Font.ITALIC);
		Transform3D textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(2f, 0f, 0f));
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
		textObject = new Text2D("Y", new Color3f(1f, 0f, 0f), "Serif", 70,
				Font.ITALIC);
		textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(0f, 0f, 2f));
		textTranslationGroup = new TransformGroup(textTranslation);
		textTranslationGroup.addChild(textObject);
		TG1.addChild(textTranslationGroup);

		/* Axe Z */
		textObject = new Text2D("Z", new Color3f(1f, 0f, 0f), "Serif", 70,
				Font.ITALIC);
		textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(0f, 2f, 0f));
		textTranslationGroup = new TransformGroup(textTranslation);
		textTranslationGroup.addChild(textObject);
		TG1.addChild(textTranslationGroup);

		

		// création de 3 plans

		PolygonAttributes polyAttr = new PolygonAttributes();
		polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
		polyAttr.setBackFaceNormalFlip(true);

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

		// scaleFactor = 3f; // (car la solution est : (1,2,3) (x,y,z)
		// // plan1
		// Appearance app1 = new Appearance();
		// app1.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
		// 0.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
		// app1.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app1.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest = createPlan(3.0f, 1.0f, -2.0f, -1.0f);
		// TG1.addChild(new Shape3D(quadArrTest, app1));
		//
		// // plan2
		// Appearance app2 = new Appearance();
		// app2.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
		// 1.0f, 0.0f), ColoringAttributes.SHADE_FLAT));
		// app2.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app2.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest2 = createPlan(-4.0f, 3.0f, 1.0f, 5.0f);
		// TG1.addChild(new Shape3D(quadArrTest2, app2));
		//
		// // plan3
		// Appearance app3 = new Appearance();
		// app3.setColoringAttributes(new ColoringAttributes(new Color3f(1.0f,
		// 0.3f, 0.0f), ColoringAttributes.SHADE_GOURAUD));
		// app3.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app3.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest3 = createPlan(2.0f, -2.0f, 3.0f, 7.0f);
		// TG1.addChild(new Shape3D(quadArrTest3, app3));
		//
		// // plan4
		// Appearance app4 = new Appearance();
		// app4.setColoringAttributes(new ColoringAttributes(new Color3f(1.0f,
		// 0.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
		// app4.setTransparencyAttributes(new TransparencyAttributes(
		// TransparencyAttributes.NONE, 0.5f));
		// app4.setPolygonAttributes(polyAttr);
		// QuadArray quadArrTest4 = createPlan(1.0f, 0f, 0f, 1.0f);
		// TG1.addChild(new Shape3D(quadArrTest4, app4));

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
//		scaleFactor = 1f; // infinité de solutions
//
//		// plan1
//		Appearance app1 = new Appearance();
//		app1.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
//				0.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
//		app1.setTransparencyAttributes(new TransparencyAttributes(
//				TransparencyAttributes.NONE, 0.5f));
//		app1.setPolygonAttributes(polyAttr);
//		QuadArray quadArrTest = createPlan(1.0f, 0.0f, -1.0f, 1.0f);
//		TG1.addChild(new Shape3D(quadArrTest, app1));
//
//		// plan2
//		Appearance app2 = new Appearance();
//		app2.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
//				1.0f, 0.0f), ColoringAttributes.SHADE_FLAT));
//		app2.setTransparencyAttributes(new TransparencyAttributes(
//				TransparencyAttributes.NONE, 0.5f));
//		app2.setPolygonAttributes(polyAttr);
//		QuadArray quadArrTest2 = createPlan(0.0f, -1.0f, 1.0f, 0.0f);
//		TG1.addChild(new Shape3D(quadArrTest2, app2));
//
//		// plan3
//		Appearance app3 = new Appearance();
//		app3.setColoringAttributes(new ColoringAttributes(new Color3f(1.0f,
//				0.3f, 0.0f), ColoringAttributes.SHADE_GOURAUD));
//		app3.setTransparencyAttributes(new TransparencyAttributes(
//				TransparencyAttributes.NONE, 0.5f));
//		app3.setPolygonAttributes(polyAttr);
//		QuadArray quadArrTest3 = createPlan(1.0f, 1.0f, -2.0f, 1.0f);
//		TG1.addChild(new Shape3D(quadArrTest3, app3));

		// Test5 (S4)
		scaleFactor = 10f; // car solution unique { x = -5, y = 3, z = 3 }. 
		// si solution unique, prendre max(|x| , |y|) * 2  

		// plan1
		Appearance app1 = new Appearance();
		app1.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
				0.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
		app1.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.NONE, 0.5f));
		app1.setPolygonAttributes(polyAttr);
		QuadArray quadArrTest = createPlan(2.0f, 3.0f, -5.0f, -16.0f);
		TG1.addChild(new Shape3D(quadArrTest, app1));

		// plan2
		Appearance app2 = new Appearance();
		app2.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
				1.0f, 0.0f), ColoringAttributes.SHADE_FLAT));
		app2.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.NONE, 0.5f));
		app2.setPolygonAttributes(polyAttr);
		QuadArray quadArrTest2 = createPlan(4.0f, 13.0f, -8.0f, -5.0f);
		TG1.addChild(new Shape3D(quadArrTest2, app2));

		// plan3
		Appearance app3 = new Appearance();
		app3.setColoringAttributes(new ColoringAttributes(new Color3f(1.0f,
				0.3f, 0.0f), ColoringAttributes.SHADE_GOURAUD));
		app3.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.NONE, 0.5f));
		app3.setPolygonAttributes(polyAttr);
		QuadArray quadArrTest3 = createPlan(1.0f, 2.0f, 7.0f, 22.0f);
		TG1.addChild(new Shape3D(quadArrTest3, app3));

		// Test6 à faire (S5)
//		scaleFactor = 1f; // pas de solutions
//
//		// plan1
//		Appearance app1 = new Appearance();
//		app1.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
//				0.0f, 1.0f), ColoringAttributes.SHADE_GOURAUD));
//		app1.setTransparencyAttributes(new TransparencyAttributes(
//				TransparencyAttributes.NONE, 0.5f));
//		app1.setPolygonAttributes(polyAttr);
//		QuadArray quadArrTest = createPlan(1.0f, -1.0f, 0.0f, 0.0f);
//		TG1.addChild(new Shape3D(quadArrTest, app1));
//
//		// plan2
//		Appearance app2 = new Appearance();
//		app2.setColoringAttributes(new ColoringAttributes(new Color3f(0.0f,
//				1.0f, 0.0f), ColoringAttributes.SHADE_FLAT));
//		app2.setTransparencyAttributes(new TransparencyAttributes(
//				TransparencyAttributes.NONE, 0.5f));
//		app2.setPolygonAttributes(polyAttr);
//		QuadArray quadArrTest2 = createPlan(0.0f, 1.0f, -1.0f, 1.0f);
//		TG1.addChild(new Shape3D(quadArrTest2, app2));
//
//		// plan3
//		Appearance app3 = new Appearance();
//		app3.setColoringAttributes(new ColoringAttributes(new Color3f(1.0f,
//				0.3f, 0.0f), ColoringAttributes.SHADE_GOURAUD));
//		app3.setTransparencyAttributes(new TransparencyAttributes(
//				TransparencyAttributes.NONE, 0.5f));
//		app3.setPolygonAttributes(polyAttr);
//		QuadArray quadArrTest3 = createPlan(-1.0f, 0.0f, 1.0f, 0.0f);
//		TG1.addChild(new Shape3D(quadArrTest3, app3));
		
		// Ticks pour l'échelle

		// Axe X
		LineArray tickX = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		tickX.setCoordinate(0, new Point3f(1.0f, 0f, -0.05f));
		tickX.setCoordinate(1, new Point3f(1.0f, 0f, 0.05f));
		tickX.setColor(0, new Color3f(0f, 0f, 0f));
		tickX.setColor(1, new Color3f(0f, 1f, 0f));
		TG1.addChild(new Shape3D(tickX));
		
		Text2D tickValue = new Text2D(scaleFactor+"", new Color3f(0f, 0f, 0f), "Serif",
				70, Font.ITALIC);
		textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(1f, 0f, 0f));
		textTranslationGroup = new TransformGroup(
				textTranslation);
		textTranslationGroup.addChild(tickValue);
		TG1.addChild(textTranslationGroup);

		// Apparence (visible des 2 côtés)
		Appearance appTick = tickValue.getAppearance();
		PolygonAttributes paTick = appTick.getPolygonAttributes();
		if (paTick == null)
			paTick = new PolygonAttributes();
		paTick.setCullFace(PolygonAttributes.CULL_NONE);
		if (appTick.getPolygonAttributes() == null)
			appTick.setPolygonAttributes(paTick);

		
		/* Axe Y */
		LineArray tickY = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		tickY.setCoordinate(0, new Point3f(-0.05f, 0f, 1f));
		tickY.setCoordinate(1, new Point3f(0.05f, 0f, 1f));
		tickY.setColor(0, new Color3f(0f, 0f, 0f));
		tickY.setColor(1, new Color3f(0f, 1f, 0f));
		TG1.addChild(new Shape3D(tickY));
		
		tickValue = new Text2D(scaleFactor+"", new Color3f(0f, 0f, 0f), "Serif", 70,
				Font.ITALIC);
		textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(0f, 0f, 1f));
		textTranslationGroup = new TransformGroup(textTranslation);
		textTranslationGroup.addChild(tickValue);
		TG1.addChild(textTranslationGroup);
		
		
		/* Axe Z */
		tickValue = new Text2D(scaleFactor+"", new Color3f(0f, 0f, 0f), "Serif", 70,
				Font.ITALIC);
		textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(0f, 1f, 0f));
		textTranslationGroup = new TransformGroup(textTranslation);
		textTranslationGroup.addChild(tickValue);
		TG1.addChild(textTranslationGroup);			

		LineArray tickZ = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		tickZ.setCoordinate(0, new Point3f(0f, 1f, -0.05f));
		tickZ.setCoordinate(1, new Point3f(0f, 1f, 0.05f));
		tickZ.setColor(0, new Color3f(0f, 0f, 0f));
		tickZ.setColor(1, new Color3f(0f, 1f, 0f));
		TG1.addChild(new Shape3D(tickZ));

		sceneBranch.addChild(TG1);
	}

	private QuadArray createPlan(float a, float b, float c, float d) {
		QuadArray quadArray = new QuadArray(4, GeometryArray.COORDINATES);
		Point3f[] coords = new Point3f[4];

		if (c != 0) {
			// On a : ax + by + cz = d
			// Donc : z = (d - ax - by) / c
			coords[0] = new Point3f(-1.0f, (d - a * -scaleFactor - b
					* -scaleFactor)
					/ (scaleFactor * c), -1.0f);
			coords[1] = new Point3f(-1.0f, (d - a * -scaleFactor - b
					* scaleFactor)
					/ (scaleFactor * c), 1.0f);
			coords[2] = new Point3f(1.0f, (d - a * scaleFactor - b
					* scaleFactor)
					/ (scaleFactor * c), 1.0f);
			coords[3] = new Point3f(1.0f, (d - a * scaleFactor - b
					* -scaleFactor)
					/ (scaleFactor * c), -1.0f);
		} else if (b != 0) { // plan vertical
			// On a : ax + by = d
			// Donc : y = (d - ax) / b
			// hauteur 10 (axe z) => (peut être à changer)
			coords[0] = new Point3f(-1.0f, -10f, (d - a * -scaleFactor)
					/ (scaleFactor * b));
			coords[1] = new Point3f(-1.0f, 10f, (d - a * -scaleFactor)
					/ (scaleFactor * b));
			coords[2] = new Point3f(1.0f, 10f, (d - a * scaleFactor)
					/ (scaleFactor * b));
			coords[3] = new Point3f(1.0f, -10f, (d - a * scaleFactor)
					/ (scaleFactor * b));
		} else if (a != 0) { // plan vertical
			// On a : ax = d
			// Donc : x = d/a
			// hauteur 10 (axe z) => (peut être à changer)
			coords[0] = new Point3f(d / (a * scaleFactor), -10f, -1f);
			coords[1] = new Point3f(d / (a * scaleFactor), -10f, 1f);
			coords[2] = new Point3f(d / (a * scaleFactor), 10f, 1f);
			coords[3] = new Point3f(d / (a * scaleFactor), 10f, -1f);
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

		// GUI / User actions

		int fontSize = 14;
		if (screenDim.height < 1024)
			fontSize = 11;
		else if (screenDim.height < 1200)
			fontSize = 12;

		font = new Font("SansSerif", Font.BOLD, fontSize);

		JPanel jPanelLine = new JPanel(new BorderLayout());
		jPanelLine.setBackground(new Color(0.05f, 0.05f, 0.8f));
		jPanelLine.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));

		JPanel jPanelBaseX = new JPanel();
		jPanelBaseX.setLayout(new BoxLayout(jPanelBaseX, BoxLayout.X_AXIS));
		jPanelBaseX.setBackground(bgColor);
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

		final JRadioButton jRadioNull = new JRadioButton();

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
				.createTitledBorder(" Home Transform ");
		tBorderHome.setTitleFont(font);
		tBorderHome.setTitleColor(bgColor);
		tBorderHome.setBorder(BorderFactory.createLineBorder(bgColor));

		jPanelActionHome.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderHome));

		JButton jButtonHomeTransform = new JButton("Go Home");
		jButtonHomeTransform.setFont(font);
		jButtonHomeTransform.setForeground(bgColor);
		jButtonHomeTransform.setAlignmentX(Component.CENTER_ALIGNMENT);

		jButtonHomeTransform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// heavy-weight !!
				view.stopView();
				orbitBehInterim.goHome(isHomeRotCenter);
				orbitBehInterim.setFieldOfView(defaultFoV);
				view.startView();

				setFoVSlider(defaultFoV);

				// No vantage point selected
				jRadioNull.setSelected(true);
			}
		});

		final JCheckBox jChecHomeRotCenter = new JCheckBox(
				"Home Rotation Center");
		jChecHomeRotCenter.setFont(font);
		jChecHomeRotCenter.setForeground(bgColor);
		jChecHomeRotCenter.setAlignmentX(Component.CENTER_ALIGNMENT);
		jChecHomeRotCenter.setSelected(true);

		jChecHomeRotCenter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				isHomeRotCenter = jChecHomeRotCenter.isSelected();

			}
		});

		jPanelActionHome.add(jButtonHomeTransform);
		jPanelActionHome.add(jChecHomeRotCenter);

		jPanelActions.add(jPanelActionHome);
		jPanelActions.add(Box.createHorizontalStrut(10));

		// Center picking
		JPanel jPanelActionCenter = new JPanel();
		jPanelActionCenter.setLayout(new BoxLayout(jPanelActionCenter,
				BoxLayout.Y_AXIS));

		TitledBorder tBorderCenter = BorderFactory
				.createTitledBorder(" Center of Rotation Picking ");
		tBorderCenter.setTitleFont(font);
		tBorderCenter.setTitleColor(bgColor);
		tBorderCenter.setBorder(BorderFactory.createLineBorder(bgColor));

		jPanelActionCenter.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderCenter));

		JPanel jPanelPickMode = new JPanel();
		jPanelPickMode
				.setLayout(new BoxLayout(jPanelPickMode, BoxLayout.X_AXIS));
		jPanelPickMode.setAlignmentX(Component.CENTER_ALIGNMENT);

		final JRadioButton jRadioPickVertex = new JRadioButton("Pick Vertex");
		final JRadioButton jRadioPickShape = new JRadioButton("Pick Shape");

		jRadioPickVertex.setFont(font);
		jRadioPickShape.setFont(font);

		jRadioPickVertex.setForeground(bgColor);
		jRadioPickShape.setForeground(bgColor);

		ButtonGroup bGroup = new ButtonGroup();
		bGroup.add(jRadioPickVertex);
		bGroup.add(jRadioPickShape);

		jRadioPickVertex.setSelected(true);

		ItemListener pickModeListener = new ItemListener() {
			public void itemStateChanged(ItemEvent event) {

				isPickVertex = jRadioPickVertex.isSelected();

			}
		};
		jRadioPickVertex.addItemListener(pickModeListener);
		jRadioPickShape.addItemListener(pickModeListener);

		jPanelPickMode.add(jRadioPickVertex);
		jPanelPickMode.add(Box.createHorizontalStrut(5));
		jPanelPickMode.add(jRadioPickShape);

		final JCheckBox jCheckLookAtCenter = new JCheckBox("Look at Center");
		jCheckLookAtCenter.setFont(font);
		jCheckLookAtCenter.setForeground(bgColor);
		jCheckLookAtCenter.setAlignmentX(Component.CENTER_ALIGNMENT);
		jCheckLookAtCenter.setSelected(true);

		jCheckLookAtCenter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				isLookAtRotCenter = jCheckLookAtCenter.isSelected();

			}
		});

		jPanelActionCenter.add(jPanelPickMode);
		jPanelActionCenter.add(jCheckLookAtCenter);

		jPanelActions.add(jPanelActionCenter);
		jPanelActions.add(Box.createHorizontalStrut(10));

		// Look At Current Center
		JPanel jPanelActionLookAt = new JPanel();
		jPanelActionLookAt.setLayout(new BoxLayout(jPanelActionLookAt,
				BoxLayout.Y_AXIS));

		TitledBorder tBorderLookAt = BorderFactory
				.createTitledBorder(" Center Transform ");
		tBorderLookAt.setTitleFont(font);
		tBorderLookAt.setTitleColor(bgColor);
		tBorderLookAt.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(bgColor),
				BorderFactory.createEmptyBorder(0, 4, 0, 4)));

		jPanelActionLookAt.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderLookAt));

		JButton jButtonLookAt = new JButton("Look at Center");
		jButtonLookAt.setFont(font);
		jButtonLookAt.setForeground(bgColor);
		jButtonLookAt.setAlignmentX(Component.CENTER_ALIGNMENT);

		jButtonLookAt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				orbitBehInterim.lookAtRotationCenter();

			}
		});

		JPanel spacePanel = new JPanel();
		spacePanel.setMaximumSize(jButtonLookAt.getPreferredSize());
		spacePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		jPanelActionLookAt.add(jButtonLookAt);
		jPanelActionLookAt.add(spacePanel);

		jPanelActions.add(jPanelActionLookAt);
		jPanelActions.add(Box.createHorizontalStrut(10));

		// Projection
		JPanel jPanelActionProjection = new JPanel();
		jPanelActionProjection.setLayout(new BoxLayout(jPanelActionProjection,
				BoxLayout.Y_AXIS));

		TitledBorder tBorderProjection = BorderFactory
				.createTitledBorder(" Projection ");
		tBorderProjection.setTitleFont(font);
		tBorderProjection.setTitleColor(bgColor);
		tBorderProjection.setBorder(BorderFactory.createLineBorder(bgColor));

		jPanelActionProjection.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderProjection));

		final JRadioButton jRadioParallel = new JRadioButton("Parallel");
		final JRadioButton jRadioPerspective = new JRadioButton("Perspective");
		jRadioParallel.setSelected(true);

		jRadioParallel.setFont(font);
		jRadioPerspective.setFont(font);
		jRadioParallel.setForeground(bgColor);
		jRadioPerspective.setForeground(bgColor);

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

		final JCheckBox jCheckPureParallel = new JCheckBox("Pure");
		jCheckPureParallel.setFont(font);
		jCheckPureParallel.setForeground(bgColor);
		jCheckPureParallel.setSelected(true);
		jCheckPureParallel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				orbitBehInterim.setPureParallelEnabled(jCheckPureParallel
						.isSelected());

			}
		});

		JPanel jPanelParProj = new JPanel();
		jPanelParProj.setLayout(new BoxLayout(jPanelParProj, BoxLayout.X_AXIS));
		jPanelParProj.setAlignmentX(Component.LEFT_ALIGNMENT);

		jPanelParProj.add(jRadioParallel);
		jPanelParProj.add(jCheckPureParallel);

		jPanelActionProjection.add(jPanelParProj);
		jPanelActionProjection.add(jRadioPerspective);

		jPanelActions.add(jPanelActionProjection);
		jPanelActions.add(Box.createHorizontalStrut(10));

		// Field of View
		JPanel jPanelActionFoV = new JPanel();
		jPanelActionFoV.setLayout(new BoxLayout(jPanelActionFoV,
				BoxLayout.Y_AXIS));

		TitledBorder tBorderFoV = BorderFactory
				.createTitledBorder(" Field of View ");
		tBorderFoV.setTitleFont(font);
		tBorderFoV.setTitleColor(bgColor);
		tBorderFoV.setBorder(BorderFactory.createLineBorder(bgColor));

		jPanelActionFoV.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderFoV));

		JPanel jPanelFoVvalue = new JPanel();
		jPanelFoVvalue
				.setLayout(new BoxLayout(jPanelFoVvalue, BoxLayout.X_AXIS));
		jPanelFoVvalue.setAlignmentX(Component.LEFT_ALIGNMENT);

		jTextFoVDegree = new JTextField();
		jTextFoVDegree.setBorder(BorderFactory.createEmptyBorder());
		jTextFoVDegree.setHorizontalAlignment(JTextField.RIGHT);
		jTextFoVDegree.setFont(font);
		jTextFoVDegree.setForeground(bgColor);
		jTextFoVDegree.setSelectionColor(jPanelActionFoV.getBackground());
		jTextFoVDegree.setEditable(false);
		jTextFoVDegree.setText(Integer.toString(
				(int) Math.round(view.getFieldOfView() / 3.141592 * 180.0))
				.toString()
				+ "°");

		jTextFoVRadians = new JTextField();
		jTextFoVRadians.setBorder(BorderFactory.createEmptyBorder());
		jTextFoVRadians.setHorizontalAlignment(JTextField.LEFT);
		jTextFoVRadians.setFont(font);
		jTextFoVRadians.setForeground(bgColor);
		jTextFoVRadians.setSelectionColor(jPanelActionFoV.getBackground());
		jTextFoVRadians.setEditable(false);
		jTextFoVRadians.setText(Float.toString((float) view.getFieldOfView()));

		JLabel jLabelSlash = new JLabel(" /  ");
		jLabelSlash.setFont(font);
		jLabelSlash.setForeground(bgColor);

		jPanelFoVvalue.add(jTextFoVDegree);
		jPanelFoVvalue.add(jLabelSlash);
		jPanelFoVvalue.add(jTextFoVRadians);

		jPanelActionFoV.add(jPanelFoVvalue);

		// Slider
		Hashtable<Integer, JLabel> labelTableDegree = new Hashtable<Integer, JLabel>();
		labelTableDegree.put(1, new JLabel("1°"));
		labelTableDegree.put(45, new JLabel("45°"));
		labelTableDegree.put(120, new JLabel("120°"));

		jSliderFoV = new JSlider(1, 120);
		jSliderFoV.setLabelTable(labelTableDegree);
		jSliderFoV.setPaintTicks(false);
		jSliderFoV.setPaintLabels(true);
		jSliderFoV.setSnapToTicks(false);
		jSliderFoV.setPaintTrack(true);
		jSliderFoV
				.setValue((int) Math.round(view.getFieldOfView() / 3.141592 * 180.0));
		jSliderFoV.setAlignmentX(Component.LEFT_ALIGNMENT);

		fovSliderListener = new ChangeListener() {
			public void stateChanged(ChangeEvent sliderEvent) {
				int angleDegree = jSliderFoV.getValue();
				double angleRadians = (angleDegree * 3.141592 / 180);

				jTextFoVDegree.setText(Integer.toString(angleDegree) + "°");
				jTextFoVRadians.setText(Float.toString((float) angleRadians));

				orbitBehInterim.setFieldOfView(angleRadians);
			}
		};

		jSliderFoV.addChangeListener(fovSliderListener);

		jPanelActionFoV.add(jSliderFoV);

		Dimension dimSl = jPanelActionHome.getPreferredSize();
		jPanelActionFoV.setPreferredSize(dimSl);
		jPanelActionFoV.setMaximumSize(dimSl);

		jPanelActions.add(jPanelActionFoV);

		jPanelBaseY.add(jPanelActions);
		jPanelBaseY.add(Box.createVerticalStrut(5));

		//
		// VantagePoints
		//
		JPanel jPanelVantagePoints = new JPanel();
		jPanelVantagePoints.setLayout(new BoxLayout(jPanelVantagePoints,
				BoxLayout.X_AXIS));
		jPanelVantagePoints.setAlignmentX(Component.CENTER_ALIGNMENT);
		TitledBorder tBorderVantage = BorderFactory
				.createTitledBorder(" Vantage Points ");
		tBorderVantage.setTitleFont(font);
		tBorderVantage.setTitleColor(bgColor);
		tBorderVantage.setBorder(BorderFactory.createLineBorder(bgColor));
		jPanelVantagePoints.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderVantage));

		final ButtonGroup vpGroup = new ButtonGroup();

		ActionListener vpListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Enumeration<AbstractButton> abstractButtons = vpGroup
						.getElements();
				while (abstractButtons.hasMoreElements()) {
					JRadioButton jRadioButton = (JRadioButton) abstractButtons
							.nextElement();
					if (jRadioButton.isSelected()) {

						vpExecutor.setVP(vantagepointHM.get(jRadioButton
								.getText()));
						vpExecutor.postId(VantagePointBehavior.APPLY_VP);

						break;
					}
				}
			}
		};

		jPanelVantagePoints.add(Box.createHorizontalGlue());
		jPanelVantagePoints.add(Box.createHorizontalStrut(5));

		Set<String> vpNames = vantagepointHM.keySet();
		Iterator<String> vpIterator = vpNames.iterator();

		String name = null;
		while (vpIterator.hasNext()) {
			name = vpIterator.next();
			final JRadioButton jRadioVp = new JRadioButton(name);
			jRadioVp.setFont(font);
			jRadioVp.setForeground(bgColor);
			jRadioVp.addActionListener(vpListener);

			vpGroup.add(jRadioVp);
			jPanelVantagePoints.add(jRadioVp);
			jPanelVantagePoints.add(Box.createHorizontalStrut(5));
		}

		vpGroup.add(jRadioNull);
		jRadioNull.setSelected(true);

		jPanelVantagePoints.add(Box.createHorizontalGlue());

		Dimension dimVps = jPanelActions.getPreferredSize();
		dimVps.height = jPanelVantagePoints.getPreferredSize().height;
		jPanelVantagePoints.setPreferredSize(dimVps);
		jPanelVantagePoints.setMaximumSize(dimVps);

		jPanelBaseY.add(jPanelVantagePoints);
		dimVps.height = jPanelBaseY.getPreferredSize().height;
		jPanelBaseY.setPreferredSize(dimVps);
		jPanelBaseY.setMaximumSize(dimVps);

		jPanelBaseX.add(Box.createHorizontalGlue());
		jPanelBaseX.add(jPanelBaseYBorder);
		jPanelBaseX.add(Box.createHorizontalGlue());

		Dimension dim = new Dimension(screenDim.width - 20, screenDim.width / 3);
		canvas3D.setPreferredSize(dim);
		canvas3D.setSize(dim);

		JPanel jPanel = new JPanel(new BorderLayout());
		jPanel.add(canvas3D, BorderLayout.CENTER);
		jPanel.add(jPanelLine, BorderLayout.SOUTH);

		// JFrame
		jFrame = new JFrame();
		jFrame.setTitle("InteractiveMesh : SimpleUniverse Navigation 2");
		jFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		jFrame.add(jPanel);
		jFrame.pack();

		Dimension jframeDim = jFrame.getSize();
		jFrame.setLocation((screenDim.width - jframeDim.width) / 2,
				(screenDim.height - jframeDim.height) / 2);

		jFrame.setVisible(true);
	}

	private void setFoVSlider(double angleRadians) {
		jSliderFoV.removeChangeListener(fovSliderListener);
		int angleDegree = (int) Math.round(angleRadians / 3.141592 * 180.0);
		jSliderFoV.setValue(angleDegree);
		jTextFoVDegree.setText(Integer.toString(angleDegree) + "°");
		jTextFoVRadians.setText(Float.toString((float) angleRadians));
		jSliderFoV.addChangeListener(fovSliderListener);
	}

	private void createVantagePoints() {
		// name - ViewPlatform's position - look at - up vector - center of
		// rotation - fov angle
		new VantagePoint("Home", new Point3d(0.0, 0.75, 15.0), new Point3d(0.0,
				0.75, 0.0), new Vector3d(0.0, 1.0, 0.0), new Point3d(0.0, 0.0,
				1.0), Math.toRadians(45));
		new VantagePoint("Front", new Point3d(0.0, 3.0, 8.5), new Point3d(0.0,
				0.0, 0.0), new Vector3d(0.0, 1.0, 0.0), new Point3d(0.0, 0.0,
				2.5), Math.toRadians(65));
		new VantagePoint("Back", new Point3d(0.0, 0.75, -13), new Point3d(0.0,
				0.75, 0.0), new Vector3d(0.0, 1.0, 0.0), new Point3d(0.0, 0.0,
				-0.5), Math.toRadians(40));
		new VantagePoint("Top", new Point3d(0.0, 50.0, 1.0), new Point3d(0.0,
				0.0, 1.0), new Vector3d(0.0, 0.0, -1.0), new Point3d(0.0, 0.0,
				0.0), Math.toRadians(10));
		new VantagePoint("Bottom", new Point3d(0.0, -5.0, 1.0), new Point3d(
				0.0, 0.0, 1.0), new Vector3d(0.0, 0.0, 1.0), new Point3d(0.0,
				0.0, 0.0), Math.toRadians(90));
		new VantagePoint("Left", new Point3d(-14.0, 0.0, 1.0), new Point3d(0.0,
				0.0, 1.0), new Vector3d(0.0, 0.0, -1.0), new Point3d(0.0, 0.0,
				1.0), Math.toRadians(30));
		new VantagePoint("Right", new Point3d(12.0, 0.0, 1.0), new Point3d(0.0,
				0.0, 1.0), new Vector3d(0.0, 0.0, -1.0), new Point3d(0.0, 0.0,
				1.0), Math.toRadians(30));
	}

	private final class VantagePoint {

		private String name = "";

		private Point3d eye = new Point3d();
		private Point3d viewCenter = new Point3d();
		private Vector3d up = new Vector3d();
		private Point3d rotCenter = new Point3d();
		private double fov = Math.PI / 4;

		VantagePoint(String name, Point3d eye, Point3d viewCenter, Vector3d up,
				Point3d rotationCenter, double fov) {

			this.name = name;

			this.eye.set(eye);
			this.viewCenter.set(viewCenter);
			this.up.set(up);
			this.rotCenter.set(rotationCenter);
			this.fov = fov;

			vantagepointHM.put(name, this);
		}

		@Override
		public String toString() {
			return name;
		}

		void applyTo(OrbitBehaviorInterim navigator) {

			navigator.setViewingTransform(eye, viewCenter, up, rotCenter);
			navigator.setFieldOfView(fov);

			setFoVSlider(fov);
		}
	}

	// Sets vantage point in behavior scheduler
	private final class VantagePointBehavior extends Behavior {

		static final int APPLY_VP = 1;
		private WakeupOnBehaviorPost post = new WakeupOnBehaviorPost(this,
				APPLY_VP);

		private OrbitBehaviorInterim orbitBeh = null;
		private VantagePoint vantagePoint = null;

		VantagePointBehavior(OrbitBehaviorInterim orbitBeh) {
			this.orbitBeh = orbitBeh;
		}

		void setVP(VantagePoint vp) {
			vantagePoint = vp;
		}

		@Override
		public void initialize() {
			wakeupOn(post);
		}

		@Override
		public void processStimulus(Enumeration criteria) {
			if (vantagePoint != null)
				vantagePoint.applyTo(orbitBeh);
			vantagePoint = null;

			wakeupOn(post);
		}
	}
}
