package ch.hearc.p2.java.view.jpanel3d;

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
import java.util.Hashtable;

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
import javax.media.j3d.Group;
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
import javax.swing.JCheckBox;
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

import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.pickfast.PickCanvas;
import com.sun.j3d.utils.universe.SimpleUniverse;

import ch.hearc.p2.java.model.Matrix;

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
final public class JPanel3D extends JPanel {

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

	private BranchGroup plansTogetherBranch = null;
	private BranchGroup labelsBranch = null;
	private BranchGroup[] plansBranch = null;

	private TransformGroup TG1;

	private PickCanvas pickCanvas = null;

	private Font font = null;
	private Color bgColor = Color.WHITE;
	private Color titleColor = Color.BLUE.darker();
	private Color[] plansColor;

	private JCheckBox[] jCheckFct;

	private float scaleFactor;
	private double[][] matrixValues;

	public float getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(float scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public JPanel3D(Matrix mat) {
		matrixValues = mat.getValuesCopy();
		scaleFactor = 10f;

		plansColor = new Color[3];
		plansColor[0] = new Color(0.1f, 0.15f, 0.9f);
		plansColor[1] = new Color(0.95f, 0.05f, 0.05f);
		plansColor[2] = new Color(0.1f, 1.0f, 0.1f);

		String[] fcts_str = new String[3];
		for (int i = 0; i < 3; i++) {
			fcts_str[i] = "";
			for (int j = 0; j < 3; j++) {
				if (matrixValues[i][j] > 0)
					{
						fcts_str[i] += " + " + matrixValues[i][j] + ""
								+ (char) (120 + j);
						}
				else if (matrixValues[i][j] < 0)
					{
						fcts_str[i] += " - " + (-matrixValues[i][j]) + ""
								+ (char) (120 + j);
						}

			}
			fcts_str[i] = " " + fcts_str[i].substring(2) + " = "
					+ matrixValues[i][3];
		}
		jCheckFct = new JCheckBox[3];
		jCheckFct[0] = new JCheckBox(fcts_str[0], true);
		jCheckFct[1] = new JCheckBox(fcts_str[1], true);
		jCheckFct[2] = new JCheckBox(fcts_str[2], true);

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
					JOptionPane.showMessageDialog(this,
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
						{
							pickedSphere = (BoundingSphere) bounds;
							}
					else
						{
							pickedSphere = new BoundingSphere(bounds);
							}

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
				@Override
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
		// on cr�e un groupe de transformation principal TG1

		// float transparancy = 0.65f;
		drawScene();
	}

	private void drawScene() {

		TG1 = new TransformGroup();
		TG1.setCapability(Group.ALLOW_CHILDREN_WRITE);
		TG1.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		// ------------ d�but de cr�ation des axes ------------

		LineAttributes lineAttr = new LineAttributes(4f, 0, true);
		Appearance lineApp = new Appearance();
		lineApp.setLineAttributes(lineAttr);

		// axe des X
		LineArray axisX = new LineArray(2, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3);
		axisX.setCoordinate(0, new Point3f(0f, 0f, 0f));
		axisX.setCoordinate(1, new Point3f(0f, 0f, 4f));
		axisX.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		axisX.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));

		// axe des Y
		LineArray axisY = new LineArray(2, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3);
		axisY.setCoordinate(0, new Point3f(0f, 0f, 0f));
		axisY.setCoordinate(1, new Point3f(4f, 0f, 0f));
		axisY.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		axisY.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));

		// axe des Z
		LineArray axisZ = new LineArray(2, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3);
		axisZ.setCoordinate(0, new Point3f(0f, 0f, 0f));
		axisZ.setCoordinate(1, new Point3f(0f, 4f, 0f));
		axisZ.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		axisZ.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));

		TG1.addChild(new Shape3D(axisX, lineApp));
		TG1.addChild(new Shape3D(axisY, lineApp));
		TG1.addChild(new Shape3D(axisZ, lineApp));

		// cr�ation des labels des axes
		// Axe X
		Text2D textObject = new Text2D("X", new Color3f(0.2f, 0.2f, 0.2f),
				"Serif", 90, Font.BOLD);
		Transform3D textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(-0.3f, 0f, 2.5f));
		TransformGroup textTranslationGroup = new TransformGroup(
				textTranslation);
		textTranslationGroup.addChild(textObject);
		TG1.addChild(textTranslationGroup);

		// Apparence (visible des 2 c�t�s)
		Appearance app = textObject.getAppearance();
		PolygonAttributes pa = app.getPolygonAttributes();
		if (pa == null)
			{
				pa = new PolygonAttributes();
				}
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		if (app.getPolygonAttributes() == null)
			{
				app.setPolygonAttributes(pa);
				}

		/* Axe Y */
		textObject = new Text2D("Y", new Color3f(0.2f, 0.2f, 0.2f), "Serif",
				90, Font.BOLD);
		textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(2.2f, 0f, 0f));
		textTranslationGroup = new TransformGroup(textTranslation);
		textTranslationGroup.addChild(textObject);
		TG1.addChild(textTranslationGroup);

		// Apparence (visible des 2 c�t�s)
		app = textObject.getAppearance();
		pa = app.getPolygonAttributes();
		if (pa == null)
			{
				pa = new PolygonAttributes();
				}
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		if (app.getPolygonAttributes() == null)
			{
				app.setPolygonAttributes(pa);
				}

		/* Axe Z */
		textObject = new Text2D("Z", new Color3f(0.2f, 0.2f, 0.2f), "Serif",
				90, Font.BOLD);
		textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(-0.3f, 1.8f, 0f));
		textTranslationGroup = new TransformGroup(textTranslation);
		textTranslationGroup.addChild(textObject);
		TG1.addChild(textTranslationGroup);

		// Apparence (visible des 2 c�t�s)
		app = textObject.getAppearance();
		pa = app.getPolygonAttributes();
		if (pa == null)
			{
				pa = new PolygonAttributes();
				}
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		if (app.getPolygonAttributes() == null)
			{
				app.setPolygonAttributes(pa);
				}

		// cr�ation de 3 plans
		drawPlans();

		// Ticks pour l'�chelle
		drawTicks();
		drawAxisLabels();

		// Bounding cube
		LineAttributes cubeAttr = new LineAttributes(2f, 0, true);
		Appearance cubeApp = new Appearance();
		cubeApp.setLineAttributes(cubeAttr);

		LineArray cube = new LineArray(24, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3);
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
	}

	private void drawTicks() {
		LineAttributes lineAttr = new LineAttributes(4f, 0, true);
		Appearance lineApp = new Appearance();
		lineApp.setLineAttributes(lineAttr);

		// Axe X
		LineArray tickX = new LineArray(2, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3);
		tickX.setCoordinate(0, new Point3f(1.0f, 0f, -0.08f));
		tickX.setCoordinate(1, new Point3f(1.0f, 0f, 0.08f));
		tickX.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		tickX.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));

		TG1.addChild(new Shape3D(tickX, lineApp));

		/* Axe Y */
		LineArray tickY = new LineArray(2, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3);
		tickY.setCoordinate(0, new Point3f(-0.08f, 0f, 1f));
		tickY.setCoordinate(1, new Point3f(0.08f, 0f, 1f));
		tickY.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		tickY.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		TG1.addChild(new Shape3D(tickY, lineApp));
		// tabTG1Shapes.add(new Shape3D(tickY, lineApp));

		/* Axe Z */
		LineArray tickZ = new LineArray(2, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3);
		tickZ.setCoordinate(0, new Point3f(0f, 1f, -0.08f));
		tickZ.setCoordinate(1, new Point3f(0f, 1f, 0.08f));
		tickZ.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		tickZ.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		TG1.addChild(new Shape3D(tickZ, lineApp));
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

		// Apparence (visible des 2 c�t�s)
		Appearance appTick = tickValue.getAppearance();
		PolygonAttributes paTick = appTick.getPolygonAttributes();
		if (paTick == null)
			{
				paTick = new PolygonAttributes();
				}
		paTick.setCullFace(PolygonAttributes.CULL_NONE);
		if (appTick.getPolygonAttributes() == null)
			{
				appTick.setPolygonAttributes(paTick);
				}

		TG1.addChild(labelsBranch);
	}

	private void drawPlans() {
		PolygonAttributes polyAttr = new PolygonAttributes();
		polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
		polyAttr.setBackFaceNormalFlip(true);

		double[] equ1 = matrixValues[0];
		double[] equ2 = matrixValues[1];
		double[] equ3 = matrixValues[2];

		plansTogetherBranch = new BranchGroup();
		plansTogetherBranch.setCapability(BranchGroup.ALLOW_DETACH);
		plansTogetherBranch.setCapability(Group.ALLOW_CHILDREN_WRITE);
		plansTogetherBranch.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		plansBranch = new BranchGroup[3];
		plansBranch[0] = new BranchGroup();
		plansBranch[1] = new BranchGroup();
		plansBranch[2] = new BranchGroup();
		plansBranch[0].setCapability(BranchGroup.ALLOW_DETACH);
		plansBranch[1].setCapability(BranchGroup.ALLOW_DETACH);
		plansBranch[2].setCapability(BranchGroup.ALLOW_DETACH);

		// plan1
		Appearance app1 = new Appearance();
		app1.setColoringAttributes(new ColoringAttributes(new Color3f(
				plansColor[0]), ColoringAttributes.SHADE_GOURAUD));
		app1.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.NONE, 0.5f));
		app1.setPolygonAttributes(polyAttr);
		QuadArray quadArrTest1 = createPlan((float) equ1[0], (float) equ1[1],
				(float) equ1[2], (float) equ1[3]);
		plansBranch[0].addChild(new Shape3D(quadArrTest1, app1));
		plansTogetherBranch.addChild(plansBranch[0]);

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
		app2.setColoringAttributes(new ColoringAttributes(new Color3f(
				plansColor[1]), ColoringAttributes.SHADE_GOURAUD));
		app2.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.NONE, 0.5f));
		app2.setPolygonAttributes(polyAttr);
		QuadArray quadArrTest2 = createPlan((float) equ2[0], (float) equ2[1],
				(float) equ2[2], (float) equ2[3]);
		plansBranch[1].addChild(new Shape3D(quadArrTest2, app2));
		plansTogetherBranch.addChild(plansBranch[1]);

		// plan3
		Appearance app3 = new Appearance();
		app3.setColoringAttributes(new ColoringAttributes(new Color3f(
				plansColor[2]), ColoringAttributes.SHADE_GOURAUD));
		app3.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.NONE, 0.5f));
		app3.setPolygonAttributes(polyAttr);
		QuadArray quadArrTest3 = createPlan((float) equ3[0], (float) equ3[1],
				(float) equ3[2], (float) equ3[3]);
		plansBranch[2].addChild(new Shape3D(quadArrTest3, app3));
		plansTogetherBranch.addChild(plansBranch[2]);

		// construction mais pas d'affichage si checkbox non selectionnee
		for (int i = 0; i < 3; i++) {
			if (!jCheckFct[i].isSelected())
				{
					plansTogetherBranch.removeChild(plansBranch[i]);
					}
		}

		TG1.addChild(plansTogetherBranch);
	}

	/**
	 * create plan that fits that stays inside the "box" (x and y axis ok, but z
	 * axis might go out of the box)
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	private QuadArray createPlan(float a, float b, float c, float d) {
		return createPlan(a, b, c, d, 1f);
	}

	/**
	 * create plan : ax + by + cz = d
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param scale
	 *            == 1 : plan fits the box on x and y < 1 : plan smaller than
	 *            the box on x and y > 1 : plan bigger than the box on x and y
	 * @return
	 */
	private QuadArray createPlan(float a, float b, float c, float d, float scale) {
		QuadArray quadArray = new QuadArray(4, GeometryArray.COORDINATES);
		Point3f[] coords = new Point3f[4];

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
			// hauteur 10 (axe z) => (peut �tre � changer)
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
			// hauteur 10 (axe z) => (peut �tre � changer)
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
			{
				fontSize = 11;
				}
		else if (screenDim.height < 1200)
			{
				fontSize = 12;
				}

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
				.createTitledBorder(" R�initialisation et centrage");
		tBorderHome.setTitleFont(font);
		tBorderHome.setTitleColor(titleColor);
		// tBorderHome.setBorder(BorderFactory.createLineBorder(bgColor));

		jPanelActionHome.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderHome));

		JButton jButtonHomeTransform = new JButton("Retour � la vue de d�part");
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

		jPanelActionHome.add(Box.createVerticalStrut(10));
		jPanelActionHome.add(jButtonHomeTransform);
		jPanelActionHome.add(Box.createVerticalStrut(10));
		jPanelActionHome.add(jButtonLookAt);
		jPanelActionHome.add(Box.createVerticalStrut(10));

		jPanelActions.add(jPanelActionHome);
		jPanelActions.add(Box.createHorizontalStrut(10));

		//
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

		final JRadioButton jRadioParallel = new JRadioButton("Parall�le");
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
					{
						projMode = View.PARALLEL_PROJECTION;
						}

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

		jPanelActionProjection.add(Box.createVerticalStrut(5));
		jPanelActionProjection.add(jPanelParProj);
		jPanelActionProjection.add(Box.createVerticalStrut(5));
		jPanelActionProjection.add(jRadioPerspective);
		jPanelActionProjection.add(Box.createVerticalStrut(5));

		jPanelActions.add(jPanelActionProjection);
		jPanelActions.add(Box.createHorizontalStrut(10));

		// Scale
		JPanel jPanelActionScale = new JPanel();
		jPanelActionScale.setLayout(new BoxLayout(jPanelActionScale,
				BoxLayout.Y_AXIS));

		TitledBorder tBorderScale = BorderFactory.createTitledBorder("Echelle de la bo�te");
		tBorderScale.setTitleFont(font);
		tBorderScale.setTitleColor(titleColor);
		// tBorderScale.setBorder(BorderFactory.createLineBorder(bgColor));

		jPanelActionScale.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderScale));

		final JSlider jSliderScale = new JSlider(-30, 30,
				(int) (10 * Math.log10(scaleFactor)));
		jSliderScale.setFont(font);
		jSliderScale.setAlignmentX(Component.LEFT_ALIGNMENT);
		jSliderScale.setMajorTickSpacing(10);
		jSliderScale.setPaintTicks(true);
		Dimension scaleSliderDim = new Dimension(280, 60);
		jSliderScale.setPreferredSize(scaleSliderDim);

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
				if (jSliderScale.getValue() >= 0) {
					scaleFactor = Math.round(Math.pow(10,
							jSliderScale.getValue() / 10.0));
				} else {
					scaleFactor = (float) Math.pow(10,
							jSliderScale.getValue() / 10.0);
				}

				TG1.removeChild(plansTogetherBranch);
				TG1.removeChild(labelsBranch);

				drawPlans();
				drawAxisLabels();

				// }
			}
		});

		JPanel jPanelScale = new JPanel();
		jPanelScale.setPreferredSize(scaleSliderDim);
		jPanelScale.setMinimumSize(scaleSliderDim);

		jPanelScale.add(jSliderScale);

		jPanelActionScale.add(jPanelScale);
		jPanelActions.add(jPanelActionScale);
		jPanelActions.add(Box.createHorizontalStrut(10));

		//
		// Selection fonctions
		JPanel jPanelActionSelection = new JPanel();
		jPanelActionSelection.setLayout(new BoxLayout(jPanelActionSelection,
				BoxLayout.Y_AXIS));

		TitledBorder tBorderSelection = BorderFactory
				.createTitledBorder(" Fonctions affich�es");
		tBorderSelection.setTitleFont(font);
		tBorderSelection.setTitleColor(titleColor);
		// tBorderProjection.setBorder(BorderFactory.createLineBorder(bgColor));

		jPanelActionSelection.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), tBorderSelection));

		ActionListener selectionListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JCheckBox src = (JCheckBox) event.getSource();
				int index = 0;
				if (src == jCheckFct[1])
					{
						index = 1;
						}
				else if (src == jCheckFct[2])
					{
						index = 2;
						}

				if (src.isSelected()) {
					plansTogetherBranch.addChild(plansBranch[index]);
				} else {
					plansTogetherBranch.removeChild(plansBranch[index]);
				}
			}
		};

		JPanel jPanelSelection = new JPanel();
		jPanelSelection.setLayout(new BoxLayout(jPanelSelection,
				BoxLayout.Y_AXIS));
		// jPanelSelection.setAlignmentY(Component.TOP_ALIGNMENT);



		jCheckFct[0].setForeground(plansColor[0]);
		jCheckFct[1].setForeground(plansColor[1]);
		jCheckFct[2].setForeground(plansColor[2]);

		jPanelSelection.add(Box.createVerticalStrut(5));

		for (JCheckBox jCheck : jCheckFct) {
			jCheck.setFont(font);
			jCheck.addActionListener(selectionListener);
			jPanelSelection.add(jCheck);
			jPanelSelection.add(Box.createVerticalStrut(5));

		}
		// jCheckFct[].setForeground(bgColor);
		// jCheckFct[].setAlignmentX(Component.LEFT_ALIGNMENT);

		jPanelActionSelection.add(jPanelSelection);

		jPanelActions.add(jPanelActionSelection);

		//

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

		//
		this.setLayout(new BorderLayout());
		this.add(canvas3D, BorderLayout.CENTER);
		this.add(jPanelLine, BorderLayout.SOUTH);

	}
}
