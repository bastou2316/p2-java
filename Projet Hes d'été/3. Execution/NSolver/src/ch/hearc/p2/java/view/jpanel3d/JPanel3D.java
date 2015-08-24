package ch.hearc.p2.java.view.jpanel3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
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
import javax.swing.JPanel;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.pickfast.PickCanvas;
import com.sun.j3d.utils.universe.SimpleUniverse;

import ch.hearc.p2.java.model.Matrix;
import ch.hearc.p2.java.view.jpanel.dialog.JPanelDialog;
import ch.hearc.p2.java.view.jpanel3d.actions.JPanelHomeTransform;
import ch.hearc.p2.java.view.jpanel3d.actions.JPanelProjection;
import ch.hearc.p2.java.view.jpanel3d.actions.JPanelScale;
import ch.hearc.p2.java.view.jpanel3d.actions.JPanelSelection;
import ch.hearc.p2.java.view.jpanel3d.actions.JPanelSolutionHighlight;

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
final public class JPanel3D extends JPanelDialog {

	private BoundingSphere globalBounds = null;

	private SimpleUniverse su = null;
	private View view = null;
	private Canvas3D canvas3D = null;

	private OrbitBehaviorInterim orbitBehInterim = null;
	private boolean isHomeRotCenter = true;

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

	private float scaleFactor;
	private double[][] matrixValues;

	private JPanelSelection jPanelSelection;

	public JPanel3D(Matrix mat) {
		matrixValues = mat.getValuesCopy();
		scaleFactor = 10f;
		jPanelSelection = null;

		plansColor = new Color[4];
		plansColor[0] = new Color(0.1f, 0.15f, 0.9f);
		plansColor[1] = new Color(0.95f, 0.05f, 0.05f);
		plansColor[2] = new Color(0.1f, 1.0f, 0.1f);
		plansColor[3] = new Color(0.9f,0.06f, 0.78f);

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

	// Base world
	private void createUniverse() {
		// Bounds
		globalBounds = new BoundingSphere();
		globalBounds.setRadius(Double.MAX_VALUE);

		// Canvas3D
		GraphicsConfiguration gcfg = SimpleUniverse.getPreferredConfiguration();
		try {
			canvas3D = new Canvas3D(gcfg);

			canvas3D.setBackground(bgColor);
		} catch (Exception e) {
			System.out.println("JPanel3D : Canvas3D failed !!");
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
		TG1.setCapability(Group.ALLOW_CHILDREN_WRITE);
		TG1.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		// ------------ début de création des axes ------------

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
		TG1.addChild(new Shape3D(axisX, lineApp));

		LineArray arrowLeftX = new LineArray(2, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3);
		arrowLeftX.setCoordinate(0, new Point3f(0f, 0f, 4f));
		arrowLeftX.setCoordinate(1, new Point3f(-0.1f, 0f, 3.8f));
		arrowLeftX.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		arrowLeftX.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		TG1.addChild(new Shape3D(arrowLeftX, lineApp));

		LineArray arrowRightX = new LineArray(2, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3);
		arrowRightX.setCoordinate(0, new Point3f(0f, 0f, 4f));
		arrowRightX.setCoordinate(1, new Point3f(0.1f, 0f, 3.8f));
		arrowRightX.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		arrowRightX.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		TG1.addChild(new Shape3D(arrowRightX, lineApp));

		// axe des Y
		LineArray axisY = new LineArray(2, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3);
		axisY.setCoordinate(0, new Point3f(0f, 0f, 0f));
		axisY.setCoordinate(1, new Point3f(4f, 0f, 0f));
		axisY.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		axisY.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		TG1.addChild(new Shape3D(axisY, lineApp));

		LineArray arrowLeftY = new LineArray(2, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3);
		arrowLeftY.setCoordinate(0, new Point3f(4f, 0f, 0f));
		arrowLeftY.setCoordinate(1, new Point3f(3.8f, 0f, -0.1f));
		arrowLeftY.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		arrowLeftY.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		TG1.addChild(new Shape3D(arrowLeftY, lineApp));

		LineArray arrowRightY = new LineArray(2, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3);
		arrowRightY.setCoordinate(0, new Point3f(4f, 0f, 0f));
		arrowRightY.setCoordinate(1, new Point3f(3.8f, 0f, 0.1f));
		arrowRightY.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		arrowRightY.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		TG1.addChild(new Shape3D(arrowRightY, lineApp));


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

		// Apparence (visible des 2 côtés)
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

		if (matrixValues[0].length == 3) {			//2D
			// dessin de 2 lignes
			drawLines();

			//dessin du carré
			drawSquare();

			// dessin de repères pour l'échelle
			drawTicks2D();
			drawAxisScaleLabels2D();
		}
		else {										//3D lenght == 4
			LineArray axisZ = new LineArray(2, GeometryArray.COORDINATES
					| GeometryArray.COLOR_3);
			axisZ.setCoordinate(0, new Point3f(0f, 0f, 0f));
			axisZ.setCoordinate(1, new Point3f(0f, 4f, 0f));
			axisZ.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
			axisZ.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
			TG1.addChild(new Shape3D(axisZ, lineApp));

			LineArray arrowLeftZ = new LineArray(2, GeometryArray.COORDINATES
					| GeometryArray.COLOR_3);
			arrowLeftZ.setCoordinate(0, new Point3f(0f, 4f, 0f));
			arrowLeftZ.setCoordinate(1, new Point3f(-0.07f, 3.8f, 0.07f));
			arrowLeftZ.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
			arrowLeftZ.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
			TG1.addChild(new Shape3D(arrowLeftZ, lineApp));

			LineArray arrowRightZ = new LineArray(2, GeometryArray.COORDINATES
					| GeometryArray.COLOR_3);
			arrowRightZ.setCoordinate(0, new Point3f(0f, 4f, 0f));
			arrowRightZ.setCoordinate(1, new Point3f(0.07f, 3.8f, -0.07f));
			arrowRightZ.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
			arrowRightZ.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
			TG1.addChild(new Shape3D(arrowRightZ, lineApp));

			textObject = new Text2D("Z", new Color3f(0.2f, 0.2f, 0.2f),
					"Serif", 90, Font.BOLD);
			textTranslation = new Transform3D();
			textTranslation.setTranslation(new Vector3f(-0.3f, 1.8f, 0f));
			textTranslationGroup = new TransformGroup(textTranslation);
			textTranslationGroup.addChild(textObject);
			TG1.addChild(textTranslationGroup);

			// Apparence (visible des 2 côtés)
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

			// dessin de 3 plans
			drawPlans();

			//dessin du cube
			drawCube();

			// dessin de repères pour l'échelle
			drawTicks3D();
			drawAxisScaleLabels3D();
		}

		sceneBranch = new BranchGroup();
		sceneBranch.addChild(TG1);
	}

	private void drawTicks2D() {
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

	}

	private void drawAxisScaleLabels2D() {
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

		// Apparence (visible des 2 côtés)
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

	private void drawTicks3D() {
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

		/* Axe Z */
		LineArray tickZ = new LineArray(2, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3);
		tickZ.setCoordinate(0, new Point3f(0f, 1f, -0.08f));
		tickZ.setCoordinate(1, new Point3f(0f, 1f, 0.08f));
		tickZ.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		tickZ.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		TG1.addChild(new Shape3D(tickZ, lineApp));
	}

	private void drawAxisScaleLabels3D() {
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

	private void drawLines() {
		PolygonAttributes polyAttr = new PolygonAttributes();
		polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
		polyAttr.setBackFaceNormalFlip(true);


		LineAttributes lineAttr = new LineAttributes(2f, 0, true);
		Appearance app1 = new Appearance();
		app1.setLineAttributes(lineAttr);
		app1.setColoringAttributes(new ColoringAttributes(new Color3f(
				plansColor[0]), ColoringAttributes.SHADE_GOURAUD));
		app1.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.NONE, 0.5f));
		app1.setPolygonAttributes(polyAttr);


		plansTogetherBranch = new BranchGroup();
		plansTogetherBranch.setCapability(BranchGroup.ALLOW_DETACH);
		plansTogetherBranch.setCapability(Group.ALLOW_CHILDREN_WRITE);
		plansTogetherBranch.setCapability(Group.ALLOW_CHILDREN_EXTEND);

		plansBranch = new BranchGroup[matrixValues.length];


		LineArray temp;

		for (int i = 0; i < matrixValues.length; i++){
			temp = createLine((float) matrixValues[i][0], (float) matrixValues[i][1],
					(float) matrixValues[i][2]);
			temp.setColor(0, new Color3f(
					plansColor[i]));
			temp.setColor(1, new Color3f(
					plansColor[i]));

			plansBranch[i] = new BranchGroup();
			plansBranch[i].setCapability(BranchGroup.ALLOW_DETACH);
			plansBranch[i].addChild(new Shape3D(temp, app1));
			plansTogetherBranch.addChild(plansBranch[i]);
		}


		// construction mais pas d'affichage si checkbox non selectionnee
		if (jPanelSelection != null) {
			for (int i = 0; i < matrixValues.length; i++) {
				if (!jPanelSelection.isCheckBoxSelected(i))
					{
						plansTogetherBranch.removeChild(plansBranch[i]);
						}
			}
		}

		TG1.addChild(plansTogetherBranch);
	}

	/**
	 * create line that fits inside the "square"
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	private LineArray createLine(float a, float b, float c) {
		return createLine(a, b, c, 1f);
	}

	/**
	 * create plan : ax + by = d
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param scale
	 *            == 1 : line fits the square
	 *             < 1 : line smaller than the square
	 *             > 1 : line bigger than the square
	 * @return
	 */
	private LineArray createLine(float a, float b, float c, float scale) {
		LineArray lineArray = new LineArray(2, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3);

		if (b != 0) {
			// On a : ax + by = c
			// Donc : y = (c - ax) / b
			lineArray.setCoordinate(0, new Point3f((c - a * -scaleFactor * scale)
					/ (scaleFactor * b), 0, -scale));
			lineArray.setCoordinate(1, new Point3f((c - a * scaleFactor * scale)
					/ (scaleFactor * b), 0, scale));

		} else if (a != 0) { // plan vertical
			// On a : ax = c
			// Donc : x = c/a
			lineArray.setCoordinate(0, new Point3f(-scale, 0, c / (a * scaleFactor)));
			lineArray.setCoordinate(1, new Point3f(scale, 0, c / (a * scaleFactor)));
		} else {
			System.err
					.println("equation error : cannot display this line (0x + 0y "
							+ c + ")");
		}



		return lineArray;
	}

	private void drawSquare() {
		// Bounding square
		LineAttributes squareAttr = new LineAttributes(2f, 0, true);
		Appearance squareApp = new Appearance();
		squareApp.setLineAttributes(squareAttr);

		LineArray square = new LineArray(8, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3);
		square.setCoordinate(0, new Point3f(1f, 0f, 1f));
		square.setCoordinate(1, new Point3f(1f, 0f, -1f));

		square.setCoordinate(2, new Point3f(1f, 0f, -1f));
		square.setCoordinate(3, new Point3f(-1f, 0f, -1f));

		square.setCoordinate(4, new Point3f(-1f, 0f, -1f));
		square.setCoordinate(5, new Point3f(-1f, 0f, 1f));

		square.setCoordinate(6, new Point3f(-1f, 0f, 1f));
		square.setCoordinate(7, new Point3f(1f, 0f, 1f));

		TG1.addChild(new Shape3D(square, squareApp));
	}

	private void drawPlans() {
		PolygonAttributes polyAttr = new PolygonAttributes();
		polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
		polyAttr.setBackFaceNormalFlip(true);

		Appearance apparence = new Appearance();
//		app1.setColoringAttributes(new ColoringAttributes(new Color3f(
//				plansColor[0]), ColoringAttributes.SHADE_GOURAUD));
		apparence.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.NONE, 0.5f));
		apparence.setPolygonAttributes(polyAttr);

		plansTogetherBranch = new BranchGroup();
		plansTogetherBranch.setCapability(BranchGroup.ALLOW_DETACH);
		plansTogetherBranch.setCapability(Group.ALLOW_CHILDREN_WRITE);
		plansTogetherBranch.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		plansBranch = new BranchGroup[matrixValues.length];

		QuadArray quadArrayTemp;

		for(int i = 0; i < matrixValues.length; i++){
			quadArrayTemp = createPlan((float) matrixValues[i][0], (float) matrixValues[i][1],
					(float) matrixValues[i][2], (float) matrixValues[i][3]);
			//pour la couleur de chaque extrémité (4)
			for(int j = 0; j < 4; j++){
				quadArrayTemp.setColor(j,new Color3f(plansColor[i]));
			}

			plansBranch[i] = new BranchGroup();
			plansBranch[i].setCapability(BranchGroup.ALLOW_DETACH);
			plansBranch[i].addChild(new Shape3D(quadArrayTemp, apparence));
			plansTogetherBranch.addChild(plansBranch[i]);
		}

		// construction mais pas d'affichage si checkbox non selectionnee
		if (jPanelSelection != null) {
			for (int i = 0; i < matrixValues.length; i++) {
				if (!jPanelSelection.isCheckBoxSelected(i))
					{
						plansTogetherBranch.removeChild(plansBranch[i]);
						}
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
		QuadArray quadArray = new QuadArray(4, GeometryArray.COORDINATES | GeometryArray.COLOR_3);

		Point3f[] coords = new Point3f[4];

		// Point3f => (y,z,x)
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

	private void drawCube() {
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
		JPanelHomeTransform jPanelHomeTransform = new JPanelHomeTransform(font,
				titleColor, this);
		jPanelActions.add(jPanelHomeTransform);
		jPanelActions.add(Box.createHorizontalStrut(10));

		// Projection
		JPanelProjection jPanelProjection = new JPanelProjection(font,
				titleColor, this);
		jPanelActions.add(jPanelProjection);
		jPanelActions.add(Box.createHorizontalStrut(10));

		// Scale
		JPanel jPanelScale = new JPanelScale(font, titleColor, scaleFactor,
				this);
		jPanelActions.add(jPanelScale);
		jPanelActions.add(Box.createHorizontalStrut(10));

		// Functions Selection
		jPanelSelection = new JPanelSelection(font, titleColor, matrixValues,
				this);
		jPanelActions.add(jPanelSelection);
		jPanelActions.add(Box.createHorizontalStrut(10));

		// Solution Highlight
		JPanel jPanelSolution = new JPanelSolutionHighlight(font, titleColor);
		jPanelActions.add(jPanelSolution);

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

	public void goHomeView() {
		view.stopView();
		orbitBehInterim.goHome(isHomeRotCenter);
		view.startView();
	}

	public void centerView() {
		orbitBehInterim.lookAtRotationCenter();
	}

	/**
	 * true : perspective false: parallele
	 *
	 * @param perspective
	 */
	public void setPerspective(boolean perspective) {
		int projMode = View.PARALLEL_PROJECTION;
		if (perspective)
			{
				projMode = View.PERSPECTIVE_PROJECTION;
				}

		orbitBehInterim.setProjectionMode(projMode);
	}

	public void rescale() {
		TG1.removeChild(plansTogetherBranch);
		TG1.removeChild(labelsBranch);

		if(matrixValues[0].length == 4){	// soit 3 inconnues
			drawPlans();
			drawAxisScaleLabels3D();
		}
		else{
			drawLines();
			drawAxisScaleLabels2D();
		}

	}

	public void displayPlan(boolean isDisplayed, int index) {
		if (isDisplayed) {
			plansTogetherBranch.addChild(plansBranch[index]);
		} else {
			plansTogetherBranch.removeChild(plansBranch[index]);
		}
	}

	//
	// GETTERS & SETTERS
	//

	public float getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(float scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public Color getPlanColor(int index) {
		return plansColor[index];
	}
}
