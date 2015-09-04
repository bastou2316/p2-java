package ch.hearc.p2.java.view.jpanel3d;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.PickInfo;
import javax.media.j3d.PolygonAttributes;
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

import ch.hearc.p2.java.model.Matrix;
import ch.hearc.p2.java.tools.Graphics3DTools;
import ch.hearc.p2.java.view.IndependentVar;
import ch.hearc.p2.java.view.jpanel.dialog.JPanelDialog;
import ch.hearc.p2.java.view.jpanel3d.actions.JPanelHelp;
import ch.hearc.p2.java.view.jpanel3d.actions.JPanelHomeTransform;
import ch.hearc.p2.java.view.jpanel3d.actions.JPanelProjection;
import ch.hearc.p2.java.view.jpanel3d.actions.JPanelScale;
import ch.hearc.p2.java.view.jpanel3d.actions.JPanelSelection;
import ch.hearc.p2.java.view.jpanel3d.actions.JPanelSolutionHighlight;
import ch.hearc.p2.java.view.jpanel3d.canvasControl.OrbitBehaviorInterim;

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
final public class JPanel3D extends JPanelDialog {

	private BoundingSphere globalBounds = null;

	private SimpleUniverse su = null;
	private View view = null;
	private Canvas3D canvas3D = null;
	private JPanel jPanelBaseY;
	private JPanel jPanelActions;

	private OrbitBehaviorInterim orbitBehInterim = null;
	private boolean isHomeRotCenter = true;

	private BranchGroup enviBranch = null;
	private BranchGroup sceneBranch = null;

	private BranchGroup plansTogetherBranch = null;
	private BranchGroup[] plansBranch = null;
	private BranchGroup labelsBranch = null;
	private BranchGroup ticksBranch = null;
	private BranchGroup solutionBranch = null;

	private TransformGroup TG1;

	private PickCanvas pickCanvas = null;

	private Font font = null;
	private int fontsize;
	private Color bgColor = Color.WHITE;
	private Color titleColor = Color.BLUE.darker();
	private Color solutionColor;
	private Color[] plansColor;

	
	private double[][] matrixValues;
	private double[][] solution;
	private int varStyle;
	
	private float scaleFactor;

	private JPanelSelection jPanelSelection;
	private JPanelSolutionHighlight jPanelSolution;

	private float space;	

	public JPanel3D(Matrix mat, double[][] solution, int varStyle) {
		super();
		matrixValues = mat.getValuesCopy();
		this.solution = solution;
		this.varStyle = varStyle;
		scaleFactor = 10f;
		space = 1f;
		fontsize = 40;
		jPanelSelection = null;

		solutionColor = new Color(1f, 1f, 0f);
		plansColor = new Color[4];
		plansColor[0] = new Color(0.0f, 0.5f, 1f); // bleu
		plansColor[1] = new Color(0.95f, 0.05f, 0.05f); // rouge
		plansColor[2] = new Color(0.0f, 0.7f, 0.2f); // vert
		plansColor[3] = new Color(0.9f, 0.06f, 0.78f); // violet
		

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
		homeTransform.setRotation(new AxisAngle4d(-1.0f, 1.5f, 0.0f, Math
				.toRadians(30)));
		Transform3D t = new Transform3D();
		t.setTranslation(new Vector3f(0f, 1.2f, 17f));
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
		axisX.setCoordinate(1, new Point3f(0f, 0f, 3f));
		axisX.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		axisX.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		TG1.addChild(new Shape3D(axisX, lineApp));

		LineArray arrowLeftX = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		arrowLeftX.setCoordinate(0, new Point3f(0f, 0f, 3f));
		arrowLeftX.setCoordinate(1, new Point3f(-0.1f, 0f, 2.8f));
		arrowLeftX.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		arrowLeftX.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		TG1.addChild(new Shape3D(arrowLeftX, lineApp));

		LineArray arrowRightX = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		arrowRightX.setCoordinate(0, new Point3f(0f, 0f, 3f));
		arrowRightX.setCoordinate(1, new Point3f(0.1f, 0f, 2.8f));
		arrowRightX.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		arrowRightX.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		TG1.addChild(new Shape3D(arrowRightX, lineApp));

		// axe des Y
		LineArray axisY = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		axisY.setCoordinate(0, new Point3f(0f, 0f, 0f));
		axisY.setCoordinate(1, new Point3f(3f, 0f, 0f));
		axisY.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		axisY.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		TG1.addChild(new Shape3D(axisY, lineApp));

		LineArray arrowLeftY = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		arrowLeftY.setCoordinate(0, new Point3f(3f, 0f, 0f));
		arrowLeftY.setCoordinate(1, new Point3f(2.8f, 0f, -0.1f));
		arrowLeftY.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		arrowLeftY.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		TG1.addChild(new Shape3D(arrowLeftY, lineApp));

		LineArray arrowRightY = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		arrowRightY.setCoordinate(0, new Point3f(3f, 0f, 0f));
		arrowRightY.setCoordinate(1, new Point3f(2.8f, 0f, 0.1f));
		arrowRightY.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		arrowRightY.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		TG1.addChild(new Shape3D(arrowRightY, lineApp));

		// création des labels des axes
		// Axe X
		Text2D textObject = new Text2D(IndependentVar.getTabVar(3, varStyle)[0], new Color3f(0.2f, 0.2f, 0.2f),
				"Serif", 90, Font.BOLD);
		Transform3D textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(-0.3f, 0f, 3.0f));
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
		textObject = new Text2D(IndependentVar.getTabVar(3, varStyle)[1], new Color3f(0.2f, 0.2f, 0.2f), "Serif",
				90, Font.BOLD);
		textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(3.0f, 0f, 0f));
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

		if (matrixValues[0].length == 3) { // 2D
			// dessin de 2 lignes
			drawLines();

			// dessin du carré
			drawSquare();

			// dessin de repères pour l'échelle
			drawTicks2D();
			drawAxisScaleLabels2D();

			// dessin de la solution
			drawSolution2D();

		} else { // 3D lenght == 4
			LineArray axisZ = new LineArray(2, LineArray.COORDINATES
					| LineArray.COLOR_3);
			axisZ.setCoordinate(0, new Point3f(0f, 0f, 0f));
			axisZ.setCoordinate(1, new Point3f(0f, 3f, 0f));
			axisZ.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
			axisZ.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
			TG1.addChild(new Shape3D(axisZ, lineApp));

			LineArray arrowLeftZ = new LineArray(2, LineArray.COORDINATES
					| LineArray.COLOR_3);
			arrowLeftZ.setCoordinate(0, new Point3f(0f, 3f, 0f));
			arrowLeftZ.setCoordinate(1, new Point3f(-0.07f, 2.8f, 0.07f));
			arrowLeftZ.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
			arrowLeftZ.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
			TG1.addChild(new Shape3D(arrowLeftZ, lineApp));

			LineArray arrowRightZ = new LineArray(2, LineArray.COORDINATES
					| LineArray.COLOR_3);
			arrowRightZ.setCoordinate(0, new Point3f(0f, 3f, 0f));
			arrowRightZ.setCoordinate(1, new Point3f(0.07f, 2.8f, -0.07f));
			arrowRightZ.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
			arrowRightZ.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
			TG1.addChild(new Shape3D(arrowRightZ, lineApp));

			textObject = new Text2D(IndependentVar.getTabVar(3, varStyle)[2], new Color3f(0.2f, 0.2f, 0.2f),
					"Serif", 90, Font.BOLD);
			textTranslation = new Transform3D();
			textTranslation.setTranslation(new Vector3f(-0.3f, 2.8f, 0f));
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

			// dessin de 3 plans
			drawPlans();

			// dessin du cube
			drawCube();

			// dessin de repères pour l'échelle
			drawTicks3D();
			drawAxisScaleLabels3D();

			// dessin de la solution
			drawSolution3D();
		}

		sceneBranch = new BranchGroup();
		sceneBranch.addChild(TG1);
	}

	/**
	 * 2D
	 */

	private void drawTicks2D() {
		ticksBranch = new BranchGroup();
		ticksBranch.setCapability(BranchGroup.ALLOW_DETACH);

		LineAttributes lineAttr = new LineAttributes(2f, 0, true);
		Appearance lineApp = new Appearance();
		lineApp.setLineAttributes(lineAttr);
		lineApp.setColoringAttributes(new ColoringAttributes(new Color3f(0.5f,
				0.3f, 0.3f), ColoringAttributes.SHADE_GOURAUD));
		lineApp.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.NONE, 0.0f));

		float totalSpace = space;
		float tickSize = 0.05f;

		drawTicks2DZeros(tickSize * 3, lineApp);
		while (totalSpace < scaleFactor) {
			drawTicks2D(totalSpace, tickSize, lineApp);
			totalSpace += space;
		}

		TG1.addChild(ticksBranch);

		// // Axe X
		// LineArray tickX = new LineArray(2, LineArray.COORDINATES
		// | LineArray.COLOR_3);
		// tickX.setCoordinate(0, new Point3f(1.0f, 0f, -0.08f));
		// tickX.setCoordinate(1, new Point3f(1.0f, 0f, 0.08f));
		// tickX.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		// tickX.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		// TG1.addChild(new Shape3D(tickX, lineApp));
		//
		// /* Axe Y */
		// LineArray tickY = new LineArray(2, LineArray.COORDINATES
		// | LineArray.COLOR_3);
		// tickY.setCoordinate(0, new Point3f(-0.08f, 0f, 1f));
		// tickY.setCoordinate(1, new Point3f(0.08f, 0f, 1f));
		// tickY.setColor(0, new Color3f(0.2f, 0.2f, 0.2f));
		// tickY.setColor(1, new Color3f(0.2f, 0.2f, 0.2f));
		// TG1.addChild(new Shape3D(tickY, lineApp));

	}

	private void drawTicks2DZeros(float tickSize, Appearance lineApp) {
		for (int i = -1; i < 2; i += 2) {
			// Axe X
			LineArray tickX = new LineArray(4, LineArray.COORDINATES);
			tickX.setCoordinate(0, new Point3f(0f, 0f, i * (1f - tickSize)));
			tickX.setCoordinate(3, new Point3f(0f, 0f, i * (1f + tickSize)));
			ticksBranch.addChild(new Shape3D(tickX, lineApp));

			// Axe Y
			LineArray tickY = new LineArray(4, LineArray.COORDINATES);
			tickY.setCoordinate(0, new Point3f(i * (1f + tickSize), 0f, 0f));
			tickY.setCoordinate(3, new Point3f(i * (1f - tickSize), 0f, 0f));
			ticksBranch.addChild(new Shape3D(tickY, lineApp));

		}
	}

	private void drawTicks2D(float totalSpace, float tickSize,
			Appearance lineApp) {
		for (int i = -1; i < 2; i += 2) {
			for (int j = -1; j < 2; j += 2) {

				// Axe X
				LineArray tickX = new LineArray(4, LineArray.COORDINATES);
				tickX.setCoordinate(0, new Point3f(
						i * totalSpace / scaleFactor, 0, j * (1f + tickSize)));
				tickX.setCoordinate(1, new Point3f(
						i * totalSpace / scaleFactor, 0, j * (1f - tickSize)));
				ticksBranch.addChild(new Shape3D(tickX, lineApp));

				// Axe Y
				LineArray tickY = new LineArray(4, LineArray.COORDINATES);
				tickY.setCoordinate(0, new Point3f(j * (1f + tickSize), 0, i
						* totalSpace / scaleFactor));
				tickY.setCoordinate(1, new Point3f(j * (1f - tickSize), 0, i
						* totalSpace / scaleFactor));
				ticksBranch.addChild(new Shape3D(tickY, lineApp));

			}
		}

	}

	private void drawAxisScaleLabels2D() {
		labelsBranch = new BranchGroup();
		labelsBranch.setCapability(BranchGroup.ALLOW_DETACH);

		// Axe X
		Text2D tickValue = new Text2D(scaleFactor + "", new Color3f(0.2f, 0.2f,
				0.2f), "Serif", fontsize, Font.ITALIC);
		Transform3D textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(1.05f, 0f, 0f));

		TransformGroup textTranslationGroup = new TransformGroup(
				textTranslation);
		textTranslationGroup = new TransformGroup(textTranslation);
		textTranslationGroup.addChild(tickValue);
		labelsBranch.addChild(textTranslationGroup);

		// Axe Y
		tickValue = new Text2D(scaleFactor + "", new Color3f(0.2f, 0.2f, 0.2f),
				"Serif", fontsize, Font.ITALIC);
		textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(0f, 0f, 1.05f));
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
		plansTogetherBranch.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		plansTogetherBranch.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

		plansBranch = new BranchGroup[matrixValues.length];

		LineArray temp;

		for (int i = 0; i < matrixValues.length; i++) {
			temp = createLine((float) matrixValues[i][0],
					(float) matrixValues[i][1], (float) matrixValues[i][2]);
			temp.setColor(0, new Color3f(plansColor[i]));
			temp.setColor(1, new Color3f(plansColor[i]));

			plansBranch[i] = new BranchGroup();
			plansBranch[i].setCapability(BranchGroup.ALLOW_DETACH);
			plansBranch[i].addChild(new Shape3D(temp, app1));
			plansTogetherBranch.addChild(plansBranch[i]);
		}

		// construction mais pas d'affichage si checkbox non selectionnee
		if (jPanelSelection != null) {
			for (int i = 0; i < matrixValues.length; i++) {
				if (!jPanelSelection.isCheckBoxSelected(i))
					plansTogetherBranch.removeChild(plansBranch[i]);
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
	 *            == 1 : line fits the square < 1 : line smaller than the square
	 *            > 1 : line bigger than the square
	 * @return
	 */
	private LineArray createLine(float a, float b, float c, float scale) {
		LineArray lineArray = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);

		if (b != 0) {
			// On a : ax + by = c
			// Donc : y = (c - ax) / b
			lineArray.setCoordinate(0, new Point3f((c - a * -scaleFactor
					* scale)
					/ (scaleFactor * b), 0, -scale));
			lineArray.setCoordinate(1,
					new Point3f((c - a * scaleFactor * scale)
							/ (scaleFactor * b), 0, scale));

		} else if (a != 0) { // plan vertical
			// On a : ax = c
			// Donc : x = c/a
			lineArray.setCoordinate(0, new Point3f(-scale, 0, c
					/ (a * scaleFactor)));
			lineArray.setCoordinate(1, new Point3f(scale, 0, c
					/ (a * scaleFactor)));
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

		LineArray square = new LineArray(8, LineArray.COORDINATES
				| LineArray.COLOR_3);
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

	private void drawSolution2D() {
		solutionBranch = new BranchGroup();
		solutionBranch.setCapability(BranchGroup.ALLOW_DETACH);
		if(solution != null){
			for (int i = 0; i < solution.length; i++) {
				System.out.println();
				for (int j = 0; j < solution[0].length; j++) {
					System.out.print(solution[i][j]+"  ");
				}
			}
		}
		if (solution == null) { // pas de solution

		} else if (solution[0].length == 1) { // unique

			if (solutionInSquare()) {
				float tickSize = 0.15f;

				LineAttributes lineAttr = new LineAttributes(6f, 0, true);
				Appearance lineApp = new Appearance();
				lineApp.setLineAttributes(lineAttr);
				lineApp.setTransparencyAttributes(new TransparencyAttributes(
						TransparencyAttributes.NONE, 1.0f));
				// Axe X
				LineArray tickX = new LineArray(4, LineArray.COORDINATES
						| LineArray.COLOR_3);
				tickX.setCoordinate(0, new Point3f((float) solution[1][0]
						/ scaleFactor, 0, (float) solution[0][0] / scaleFactor
						- tickSize));
				tickX.setCoordinate(1, new Point3f((float) solution[1][0]
						/ scaleFactor, 0, (float) solution[0][0] / scaleFactor));
				tickX.setCoordinate(2, new Point3f((float) solution[1][0]
						/ scaleFactor, 0, (float) solution[0][0] / scaleFactor));
				tickX.setCoordinate(3, new Point3f((float) solution[1][0]
						/ scaleFactor, 0, (float) solution[0][0] / scaleFactor
						+ tickSize));
				tickX.setColor(0, new Color3f(solutionColor));
				tickX.setColor(1, new Color3f(0f, 0f, 0f));
				tickX.setColor(2, new Color3f(0f, 0f, 0f));
				tickX.setColor(3, new Color3f(solutionColor));
				solutionBranch.addChild(new Shape3D(tickX, lineApp));

				// Axe Y
				LineArray tickY = new LineArray(4, LineArray.COORDINATES
						| LineArray.COLOR_3);
				tickY.setCoordinate(0, new Point3f((float) solution[1][0]
						/ scaleFactor - tickSize, 0, (float) solution[0][0]
						/ scaleFactor));
				tickY.setCoordinate(1, new Point3f((float) solution[1][0]
						/ scaleFactor, 0, (float) solution[0][0] / scaleFactor));
				tickY.setCoordinate(2, new Point3f((float) solution[1][0]
						/ scaleFactor, 0, (float) solution[0][0] / scaleFactor));
				tickY.setCoordinate(3, new Point3f((float) solution[1][0]
						/ scaleFactor + tickSize, 0, (float) solution[0][0]
						/ scaleFactor));
				tickY.setColor(0, new Color3f(solutionColor));
				tickY.setColor(1, new Color3f(0f, 0f, 0f));
				tickY.setColor(2, new Color3f(0f, 0f, 0f));
				tickY.setColor(3, new Color3f(solutionColor));
				solutionBranch.addChild(new Shape3D(tickY, lineApp));
			}

		} else {
			PolygonAttributes polyAttr = new PolygonAttributes();
			polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
			polyAttr.setBackFaceNormalFlip(true);
	
			LineAttributes lineAttr = new LineAttributes(2f, 0, true);
			Appearance app = new Appearance();
			app.setLineAttributes(lineAttr);
			app.setTransparencyAttributes(new TransparencyAttributes(
					TransparencyAttributes.NONE, 0.5f));
			app.setPolygonAttributes(polyAttr);
	
			LineArray line;	
			line = createLine((float) matrixValues[0][0],
						(float) matrixValues[0][1], (float) matrixValues[0][2]);
			line.setColor(0, new Color3f(solutionColor));
			line.setColor(1, new Color3f(solutionColor));
	
			solutionBranch.addChild(new Shape3D(line, app));
		}	

		TG1.addChild(solutionBranch);
		if (jPanelSolution != null) {
			if (!jPanelSolution.isCheckBoxSelected())
				TG1.removeChild(solutionBranch);
		}
	}

	private boolean solutionInSquare() {
		double max = 0;
		for (int i = 0; i < solution.length; i++) {
			max = Math.max(max, solution[i][0]);
		}
		return max <= scaleFactor;
	}

	
	/*
	 * 3D
	 */

	/**
	 * Dessin des ticks dans l'espace 3D
	 */
	private void drawTicks3D() {
		ticksBranch = new BranchGroup();
		ticksBranch.setCapability(BranchGroup.ALLOW_DETACH);

		LineAttributes lineAttr = new LineAttributes(2f, 0, true);
		Appearance lineApp = new Appearance();
		lineApp.setLineAttributes(lineAttr);
		lineApp.setColoringAttributes(new ColoringAttributes(new Color3f(0.5f,
				0.3f, 0.3f), ColoringAttributes.SHADE_GOURAUD));
		lineApp.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.NONE, 0.0f));

		float totalSpace = space;
		float tickSize = 0.05f;

		drawTicks3D(tickSize * 3, lineApp);
		drawTicks3DZeros(tickSize * 3, lineApp);
		while (totalSpace < scaleFactor) {
			drawTicks3D(totalSpace, tickSize, lineApp);
			totalSpace += space;
		}

		TG1.addChild(ticksBranch);
	}

	/**
	 * Draw ticks on the axes
	 * 
	 * @param tickSize
	 * @param lineApp
	 */
	private void drawTicks3D(float tickSize, Appearance lineApp) {

		// Axe X
		LineArray tickX = new LineArray(4, LineArray.COORDINATES);
		tickX.setCoordinate(0, new Point3f(1.0f, 0f, -tickSize));
		tickX.setCoordinate(1, new Point3f(1.0f, 0f, tickSize));
		tickX.setCoordinate(2, new Point3f(1.0f, tickSize, 0f));
		tickX.setCoordinate(3, new Point3f(1.0f, -tickSize, 0f));
		ticksBranch.addChild(new Shape3D(tickX, lineApp));

		// Axe Y
		LineArray tickY = new LineArray(4, LineArray.COORDINATES);
		tickY.setCoordinate(0, new Point3f(-tickSize, 0f, 1f));
		tickY.setCoordinate(1, new Point3f(tickSize, 0f, 1f));
		tickY.setCoordinate(2, new Point3f(0f, tickSize, 1f));
		tickY.setCoordinate(3, new Point3f(0f, -tickSize, 1f));
		ticksBranch.addChild(new Shape3D(tickY, lineApp));

		// Axe Z
		LineArray tickZ = new LineArray(4, LineArray.COORDINATES);
		tickZ.setCoordinate(0, new Point3f(0f, 1f, -tickSize));
		tickZ.setCoordinate(1, new Point3f(0f, 1f, tickSize));
		tickZ.setCoordinate(2, new Point3f(tickSize, 1f, 0f));
		tickZ.setCoordinate(3, new Point3f(-tickSize, 1f, 0f));
		ticksBranch.addChild(new Shape3D(tickZ, lineApp));

	}

	/**
	 * Draw the 0 ticks on the cube
	 * 
	 * @param tickSize
	 * @param app
	 */
	private void drawTicks3DZeros(float tickSize, Appearance app) {

		for (int i = -1; i < 2; i += 2) {
			for (int j = -1; j < 2; j += 2) {
				// Axe X
				LineArray tickX = new LineArray(4, LineArray.COORDINATES);
				tickX.setCoordinate(0, new Point3f(0f, i, j * (1f - tickSize)));
				tickX.setCoordinate(1, new Point3f(0f, i, j));
				tickX.setCoordinate(2, new Point3f(0f, i, j));
				tickX.setCoordinate(3, new Point3f(0f, i * (1f - tickSize), j));
				ticksBranch.addChild(new Shape3D(tickX, app));

				// Axe Y
				LineArray tickY = new LineArray(4, LineArray.COORDINATES);
				tickY.setCoordinate(0, new Point3f(j * (1f - tickSize), i, 0f));
				tickY.setCoordinate(1, new Point3f(j, i, 0f));
				tickY.setCoordinate(2, new Point3f(j, i, 0f));
				tickY.setCoordinate(3, new Point3f(j, i * (1f - tickSize), 0f));
				ticksBranch.addChild(new Shape3D(tickY, app));

				// Axe Z
				LineArray tickZ = new LineArray(4, LineArray.COORDINATES);
				tickZ.setCoordinate(0, new Point3f(i, 0f, j * (1f - tickSize)));
				tickZ.setCoordinate(1, new Point3f(i, 0f, j));
				tickZ.setCoordinate(2, new Point3f(i, 0f, j));
				tickZ.setCoordinate(3, new Point3f(i * (1f - tickSize), 0f, j));
				ticksBranch.addChild(new Shape3D(tickZ, app));

			}
		}
	}

	/**
	 * Draw all the ticks on the cube except the "0 ticks"
	 * 
	 * @param totalSpace
	 * @param tickSize
	 * @param app
	 */
	private void drawTicks3D(float totalSpace, float tickSize, Appearance app) {

		for (int i = -1; i < 2; i += 2) {
			for (int j = -1; j < 2; j += 2) {
				for (int k = -1; k < 2; k += 2) {
					// Axe X
					LineArray tickX = new LineArray(4, LineArray.COORDINATES);
					tickX.setCoordinate(0, new Point3f(i * totalSpace
							/ scaleFactor, j, k));
					tickX.setCoordinate(1, new Point3f(i * totalSpace
							/ scaleFactor, j * (1f - tickSize), k));
					tickX.setCoordinate(2, new Point3f(i * totalSpace
							/ scaleFactor, j, k));
					tickX.setCoordinate(3, new Point3f(i * totalSpace
							/ scaleFactor, j, k * (1f - tickSize)));
					ticksBranch.addChild(new Shape3D(tickX, app));

					// Axe Y
					LineArray tickY = new LineArray(4, LineArray.COORDINATES);
					tickY.setCoordinate(0, new Point3f(k, j, i * totalSpace
							/ scaleFactor));
					tickY.setCoordinate(1, new Point3f(k, j * (1f - tickSize),
							i * totalSpace / scaleFactor));
					tickY.setCoordinate(2, new Point3f(k, j, i * totalSpace
							/ scaleFactor));
					tickY.setCoordinate(3, new Point3f(k * (1f - tickSize), j,
							i * totalSpace / scaleFactor));
					ticksBranch.addChild(new Shape3D(tickY, app));

					// Axe Z
					LineArray tickZ = new LineArray(4, LineArray.COORDINATES);
					tickZ.setCoordinate(0, new Point3f(j, i * totalSpace
							/ scaleFactor, k));
					tickZ.setCoordinate(1, new Point3f(j * (1f - tickSize), i
							* totalSpace / scaleFactor, k));
					tickZ.setCoordinate(2, new Point3f(j, i * totalSpace
							/ scaleFactor, k));
					tickZ.setCoordinate(3, new Point3f(j, i * totalSpace
							/ scaleFactor, k * (1f - tickSize)));
					ticksBranch.addChild(new Shape3D(tickZ, app));
				}
			}
		}

	}

	private void drawAxisScaleLabels3D() {
		labelsBranch = new BranchGroup();
		labelsBranch.setCapability(BranchGroup.ALLOW_DETACH);

		// Axe X
		Text2D tickValue = new Text2D(scaleFactor + "", new Color3f(0.2f, 0.2f,
				0.2f), "Serif", fontsize, Font.ITALIC);
		Transform3D textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(1.05f, 0f, 0f));

		TransformGroup textTranslationGroup = new TransformGroup(
				textTranslation);
		textTranslationGroup = new TransformGroup(textTranslation);
		textTranslationGroup.addChild(tickValue);
		labelsBranch.addChild(textTranslationGroup);

		// Axe Y
		tickValue = new Text2D(scaleFactor + "", new Color3f(0.2f, 0.2f, 0.2f),
				"Serif", fontsize, Font.ITALIC);
		textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(0f, 0f, 1.05f));
		textTranslationGroup = new TransformGroup(textTranslation);
		textTranslationGroup.addChild(tickValue);
		labelsBranch.addChild(textTranslationGroup);

		// Axe Z
		tickValue = new Text2D(scaleFactor + "", new Color3f(0.2f, 0.2f, 0.2f),
				"Serif", fontsize, Font.ITALIC);
		textTranslation = new Transform3D();
		textTranslation.setTranslation(new Vector3f(0f, 1.05f, 0f));
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

	/**
	 * Dessine les plans dans l'espace en 3D
	 */
	private void drawPlans() {
		PolygonAttributes polyAttr = new PolygonAttributes();
		polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
		polyAttr.setBackFaceNormalFlip(true);

		Appearance appBase = new Appearance();
		appBase.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.NONE, 0.5f));
		appBase.setPolygonAttributes(polyAttr);

		plansTogetherBranch = new BranchGroup();
		plansTogetherBranch.setCapability(BranchGroup.ALLOW_DETACH);
		plansTogetherBranch.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		plansTogetherBranch.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		plansBranch = new BranchGroup[matrixValues.length];

		GeometryArray geoArrayTemp;
		Appearance appTemp = null;

		for (int i = 0; i < matrixValues.length; i++) {
			geoArrayTemp = createPlan((float) matrixValues[i][0],
					(float) matrixValues[i][1], (float) matrixValues[i][2],
					(float) matrixValues[i][3]);

			appTemp = new Appearance();
			appTemp = (Appearance) appBase.cloneNodeComponent(true);
			appTemp.setColoringAttributes(new ColoringAttributes(new Color3f(
					plansColor[i]), ColoringAttributes.SHADE_GOURAUD));
			plansBranch[i] = new BranchGroup();
			plansBranch[i].setCapability(BranchGroup.ALLOW_DETACH);
			plansBranch[i].addChild(new Shape3D(geoArrayTemp, appTemp));
			plansTogetherBranch.addChild(plansBranch[i]);
		}

		// construction mais pas d'affichage si checkbox non selectionnee
		if (jPanelSelection != null) {
			for (int i = 0; i < matrixValues.length; i++) {
				if (!jPanelSelection.isCheckBoxSelected(i))
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
	private GeometryArray createPlan(float a, float b, float c, float d) {
		return Graphics3DTools.createPlanBoxed(a, b, c, d, scaleFactor);
		// return Graphics3DTools.createPlan(a, b, c, d, 1f, scaleFactor);
	}

	private void drawCube() {
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
	}

	private void drawSolution3D() {
		solutionBranch = new BranchGroup();
		solutionBranch.setCapability(BranchGroup.ALLOW_DETACH);
		
		if(solution == null){ //pas de solution
			
		}
		else if(solution[0].length == 1){ //unique
			
			
			if(solutionInBox()){
				float tickSize = 0.15f;
				
				LineAttributes lineAttr = new LineAttributes(6f, 0, true);
				Appearance lineApp = new Appearance();
				lineApp.setLineAttributes(lineAttr);
				lineApp.setTransparencyAttributes(new TransparencyAttributes(
						TransparencyAttributes.NONE, 1.0f));
				// Axe X
				LineArray tickX = new LineArray(4, LineArray.COORDINATES | LineArray.COLOR_3);
				tickX.setCoordinate(0, new Point3f((float)solution[1][0]/scaleFactor, (float)solution[2][0]/scaleFactor, (float)solution[0][0]/scaleFactor - tickSize));
				tickX.setCoordinate(1, new Point3f((float)solution[1][0]/scaleFactor, (float)solution[2][0]/scaleFactor, (float)solution[0][0]/scaleFactor));
				tickX.setCoordinate(2, new Point3f((float)solution[1][0]/scaleFactor, (float)solution[2][0]/scaleFactor, (float)solution[0][0]/scaleFactor));
				tickX.setCoordinate(3, new Point3f((float)solution[1][0]/scaleFactor, (float)solution[2][0]/scaleFactor, (float)solution[0][0]/scaleFactor + tickSize));
				tickX.setColor(0, new Color3f(solutionColor));
				tickX.setColor(1, new Color3f(0f, 0f, 0f));
				tickX.setColor(2, new Color3f(0f, 0f, 0f));
				tickX.setColor(3, new Color3f(solutionColor));
				solutionBranch.addChild(new Shape3D(tickX, lineApp));
				
				// Axe Y
				LineArray tickY = new LineArray(4, LineArray.COORDINATES | LineArray.COLOR_3);
				tickY.setCoordinate(0, new Point3f((float)solution[1][0]/scaleFactor - tickSize, (float)solution[2][0]/scaleFactor, (float)solution[0][0]/scaleFactor));
				tickY.setCoordinate(1, new Point3f((float)solution[1][0]/scaleFactor, (float)solution[2][0]/scaleFactor, (float)solution[0][0]/scaleFactor));
				tickY.setCoordinate(2, new Point3f((float)solution[1][0]/scaleFactor, (float)solution[2][0]/scaleFactor, (float)solution[0][0]/scaleFactor));
				tickY.setCoordinate(3, new Point3f((float)solution[1][0]/scaleFactor + tickSize, (float)solution[2][0]/scaleFactor, (float)solution[0][0]/scaleFactor));
				tickY.setColor(0, new Color3f(solutionColor));
				tickY.setColor(1, new Color3f(0f, 0f, 0f));
				tickY.setColor(2, new Color3f(0f, 0f, 0f));
				tickY.setColor(3, new Color3f(solutionColor));
				solutionBranch.addChild(new Shape3D(tickY, lineApp));
				
				// Axe Z
				LineArray tickZ = new LineArray(4, LineArray.COORDINATES | LineArray.COLOR_3);
				tickZ.setCoordinate(0, new Point3f((float)solution[1][0]/scaleFactor, (float)solution[2][0]/scaleFactor - tickSize, (float)solution[0][0]/scaleFactor));
				tickZ.setCoordinate(1, new Point3f((float)solution[1][0]/scaleFactor, (float)solution[2][0]/scaleFactor, (float)solution[0][0]/scaleFactor));
				tickZ.setCoordinate(2, new Point3f((float)solution[1][0]/scaleFactor, (float)solution[2][0]/scaleFactor, (float)solution[0][0]/scaleFactor));
				tickZ.setCoordinate(3, new Point3f((float)solution[1][0]/scaleFactor, (float)solution[2][0]/scaleFactor + tickSize, (float)solution[0][0]/scaleFactor));
				tickZ.setColor(0, new Color3f(solutionColor));
				tickZ.setColor(1, new Color3f(0f, 0f, 0f));
				tickZ.setColor(2, new Color3f(0f, 0f, 0f));
				tickZ.setColor(3, new Color3f(solutionColor));
				solutionBranch.addChild(new Shape3D(tickZ, lineApp));
				}
								
		}
		else if(solution[0].length == 3) { // infinité de solutions  (variables indépendantes)
			if(solution[0][2] == 0 && solution[1][2] == 0 && solution[2][2] == 0){
				int i = 0;
				Point3f[] pts = new Point3f[2];
				//On a :
				// x = a1 + b1*t
				// y = a2 + b2*t
				// z = a3 + b3*t				
				
				for(int j = 0; j<3; j++){
					if(solution[j][1] != 0){
						//Posons x/y/z = 1 pour trouver t et en déduire les 2 autres inconnues
						float t = (float) ((1-solution[j][0]/scaleFactor)/(solution[j][1]/scaleFactor));
						float var2 = (float) (solution[(j+1)%3][0]/scaleFactor + (solution[(j+1)%3][1]/scaleFactor)*t);
						float var3 = (float) (solution[(j+2)%3][0]/scaleFactor + (solution[(j+2)%3][1]/scaleFactor)*t);				
						if(Math.abs(var2) <= 1 && Math.abs(var3) <= 1){
							if(j == 0){
								if(i<2){
									pts[i] = new Point3f(var2,var3,1);
									i++;
								}
							}
							else if(j == 1){
								if(i<2){
									pts[i] = new Point3f(1,var2,var3);
									i++;
								}
							}
							else{
								if(i<2){
									pts[i] = new Point3f(var3,1,var2);
									i++;
								}
							}
						}
						
						//Posons x/y/z = -1 pour trouver t et en déduire les 2 autres inconnues
						t = (float) ((-1-solution[j][0]/scaleFactor)/(solution[j][1]/scaleFactor));
						var2 = (float) (solution[(j+1)%3][0]/scaleFactor + (solution[(j+1)%3][1]/scaleFactor)*t);
						var3 = (float) (solution[(j+2)%3][0]/scaleFactor + (solution[(j+2)%3][1]/scaleFactor)*t);				
						if(Math.abs(var2) <= 1 && Math.abs(var3) <= 1){
							if(j == 0){
								if(i<2){
									pts[i] = new Point3f(var2,var3,-1);
									i++;
								}							
							}
							else if(j == 1){
								if(i<2){
									pts[i] = new Point3f(-1,var2,var3);
									i++;
								}
							}
							else{
								if(i<2){
									pts[i] = new Point3f(var3,-1,var2);
									i++;
								}
							}
						}
					}
				}
				if(pts[1] != null){
					PolygonAttributes polyAttr = new PolygonAttributes();
					polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
					polyAttr.setBackFaceNormalFlip(true);
					LineAttributes lineAttr = new LineAttributes(8f, 0, true);
					Appearance lineApp = new Appearance();
					lineApp.setLineAttributes(lineAttr);
					lineApp.setPolygonAttributes(polyAttr);
					lineApp.setTransparencyAttributes(new TransparencyAttributes(
							TransparencyAttributes.NONE, 0.0f));
					
					// Droite
					LineArray line = new LineArray(4, LineArray.COORDINATES | LineArray.COLOR_3);
					line.setCoordinate(0, pts[0]);
					line.setCoordinate(1, new Point3f((pts[0].x + pts[1].x)/2, (pts[0].y + pts[1].y)/2, (pts[0].z + pts[1].z)/2 ));
					line.setCoordinate(2, new Point3f((pts[0].x + pts[1].x)/2, (pts[0].y + pts[1].y)/2, (pts[0].z + pts[1].z)/2 ));
					line.setCoordinate(3, pts[1]);
					
					
					line.setColor(0, new Color3f(solutionColor));
					line.setColor(1, new Color3f(0f, 0f, 0f));
					line.setColor(2, new Color3f(0f, 0f, 0f));
					line.setColor(3, new Color3f(solutionColor));
					solutionBranch.addChild(new Shape3D(line, lineApp));
					System.out.println();
				}
			}
			else{
				PolygonAttributes polyAttr = new PolygonAttributes();
				polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
				polyAttr.setBackFaceNormalFlip(true);
				Appearance app = new Appearance();
				app.setTransparencyAttributes(new TransparencyAttributes(
						TransparencyAttributes.NONE, 0.5f));
				app.setPolygonAttributes(polyAttr);
				app.setColoringAttributes(new ColoringAttributes(new Color3f(
						solutionColor), ColoringAttributes.SHADE_GOURAUD));
				
				GeometryArray geoArray = createPlan((float) matrixValues[0][0],
						(float) matrixValues[0][1], (float) matrixValues[0][2],
						(float) matrixValues[0][3]);
			
				solutionBranch.addChild(new Shape3D(geoArray, app));
			}
			
			
		}
		
		TG1.addChild(solutionBranch);
		if (jPanelSolution != null) {
			if (!jPanelSolution.isCheckBoxSelected())
				TG1.removeChild(solutionBranch);
		}
		
	}

	private boolean solutionInBox() {
		double max = 0;
		for (int i = 0; i < solution.length; i++) {
			max = Math.max(max, solution[i][0]);
		}
		return max <= scaleFactor;
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

		jPanelBaseY = new JPanel();
		jPanelBaseY.setLayout(new BoxLayout(jPanelBaseY, BoxLayout.Y_AXIS));
		jPanelBaseY.setBackground(bgColor);
		jPanelBaseY.setBorder(BorderFactory.createEmptyBorder());

		jPanelBaseYBorder.add(jPanelBaseY);

		//
		// Actions
		//
		jPanelActions = new JPanel();
		jPanelActions.setLayout(new BoxLayout(jPanelActions, BoxLayout.X_AXIS));
		jPanelActions.setBackground(bgColor);
		jPanelActions.setBorder(BorderFactory.createEmptyBorder());
		jPanelActions
				.setAlignmentX(TOP_ALIGNMENT/* Component.CENTER_ALIGNMENT */);
		jPanelActions.setAlignmentY(TOP_ALIGNMENT);

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
		jPanelSelection = new JPanelSelection(font, titleColor, matrixValues, varStyle,
				this);
		jPanelActions.add(jPanelSelection);
		jPanelActions.add(Box.createHorizontalStrut(10));

		// Solution Highlight
		jPanelSolution = new JPanelSolutionHighlight(font, titleColor,
				solution, varStyle, this);
		jPanelActions.add(jPanelSolution);
		jPanelActions.add(Box.createHorizontalStrut(10));

		// Help
		JPanel jPanelHelp = new JPanelHelp(font, titleColor, this);
		jPanelActions.add(jPanelHelp);

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
			projMode = View.PERSPECTIVE_PROJECTION;

		orbitBehInterim.setProjectionMode(projMode);
	}

	public void rescale() {
		TG1.removeChild(plansTogetherBranch);
		TG1.removeChild(labelsBranch);
		TG1.removeChild(ticksBranch);
		TG1.removeChild(solutionBranch);

		if (matrixValues[0].length == 4) { // soit 3 inconnues
			drawPlans();
			drawAxisScaleLabels3D();
			drawTicks3D();
			drawSolution3D();
		} else {
			drawLines();
			drawAxisScaleLabels2D();
			drawTicks2D();
			drawSolution2D();
		}

	}

	public void displayPlan(boolean isDisplayed, int index) {
		if (isDisplayed) {
			plansTogetherBranch.addChild(plansBranch[index]);
		} else {
			plansTogetherBranch.removeChild(plansBranch[index]);
		}
	}

	public void displaySolution(boolean isDisplayed) {
		if (isDisplayed) {
			TG1.addChild(solutionBranch);
		} else {
			TG1.removeChild(solutionBranch);
		}
	}

	public void update() {

		// this.repaint();
		// this.revalidate();

		Dimension dimVps = jPanelActions.getPreferredSize();
		jPanelBaseY.setPreferredSize(dimVps);
		jPanelBaseY.setMaximumSize(dimVps);

		// this.revalidate();
		// this.repaint();
		// canvas3D.revalidate();
		// canvas3D.repaint();
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

	public void setSpace(float space) {
		this.space = space;
	}
}
