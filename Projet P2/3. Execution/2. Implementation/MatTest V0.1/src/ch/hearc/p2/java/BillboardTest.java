package ch.hearc.p2.java;

 import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsConfigTemplate;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.URL;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.AudioDevice;
import javax.media.j3d.Background;
import javax.media.j3d.Billboard;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Group;
import javax.media.j3d.LineArray;
import javax.media.j3d.Locale;
import javax.media.j3d.Material;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

import com.sun.j3d.audioengines.javasound.JavaSoundMixer;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.ColorCube;

/**
 * Creates a simple rotating scene that includes two text billboards, one
 * created to ROTATE_ABOUT_AXIS the other ROTATE_ABOUT_POINT.
 */
public class BillboardTest extends Java3dApplet {
  private static int m_kWidth = 400;

  private static int m_kHeight = 400;

  public BillboardTest() {
    initJava3d();
  }

  protected double getScale() {
    return 0.08;
  }

  protected BranchGroup createSceneBranchGroup() {
    BranchGroup objRoot = super.createSceneBranchGroup();

    TransformGroup objTrans = new TransformGroup();
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
        100.0);

    Transform3D incliner = new Transform3D();
    incliner.rotX(Math.PI/3.0d);
    
    Transform3D yAxis = new Transform3D();
    Alpha rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0,
        4000, 0, 0, 0, 0, 0);
    yAxis.mul(incliner);
    RotationInterpolator rotator = new RotationInterpolator(rotationAlpha,
        objTrans, yAxis, 0.0f, (float) Math.PI * 2.0f);
    rotator.setSchedulingBounds(bounds);
    objTrans.addChild(rotator);

    objTrans.addChild(createBillboard("AXIS - 0,1,0", new Point3f(-40.0f,
        40.0f, 0.0f), Billboard.ROTATE_ABOUT_AXIS, new Point3f(0.0f,
        1.0f, 0.0f), bounds));

    objTrans.addChild(createBillboard("POINT - 10,0,0", new Point3f(40.0f,
        00.0f, 0.0f), Billboard.ROTATE_ABOUT_POINT, new Point3f(10.0f,
        0.0f, 0.0f), bounds));

//    objTrans.addChild(new ColorCube(20.0));
    
  //ligne des X
  	LineArray axisX=new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
  	axisX.setCoordinate(0,new Point3f(-100f,0f,0f));
  	axisX.setCoordinate(1,new Point3f(100f,0f,0f));
  	axisX.setColor(0,new Color3f(1f,0f,0f));
  	axisX.setColor(1,new Color3f(0f,0f,1f));
  	
  	//ligne des Y
  	LineArray axisY=new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
  	axisY.setCoordinate(0,new Point3f(0f,-100f,0f));
  	axisY.setCoordinate(1,new Point3f(0f,100f,0f));
  	axisY.setColor(0,new Color3f(0f,1f,0f));
  	axisY.setColor(1,new Color3f(1f,0f,0f));
  	
  	//ligne des Z
  	LineArray axisZ=new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
  	axisZ.setCoordinate(0,new Point3f(0f,0f,-100f));
  	axisZ.setCoordinate(1,new Point3f(0f,0f,100f));
  	axisZ.setColor(0,new Color3f(0f,1f,0f));
  	axisZ.setColor(1,new Color3f(1f,0f,0f));
  	
  	objTrans.addChild(new Shape3D(axisX));
  	objTrans.addChild(new Shape3D(axisY));
  	objTrans.addChild(new Shape3D(axisZ));

    objRoot.addChild(objTrans);

    return objRoot;
  }

  private TransformGroup createBillboard(String szText,
      Point3f locationPoint, int nMode, Point3f billboardPoint,
      BoundingSphere bounds) {
    TransformGroup subTg = new TransformGroup();
    subTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

    Font3D f3d = new Font3D(new Font("SansSerif", Font.PLAIN, 10),
        new FontExtrusion());
    Text3D label3D = new Text3D(f3d, szText, locationPoint);

    Appearance app = new Appearance();

    Color3f black = new Color3f(0.1f, 0.1f, 0.1f);
    Color3f objColor = new Color3f(0.2f, 0.2f, 0.2f);

    app.setMaterial(new Material(objColor, black, objColor, black, 90.0f));
    Shape3D sh = new Shape3D(label3D, app);

    subTg.addChild(sh);
    

    Billboard billboard = new Billboard(subTg, nMode, billboardPoint);
    billboard.setSchedulingBounds(bounds);
    subTg.addChild(billboard);
    
    return subTg;
  }

  public static void main(String[] args) {
    BillboardTest billTest = new BillboardTest();
    billTest.saveCommandLineArguments(args);

    new MainFrame(billTest, m_kWidth, m_kHeight);
  }
}

/*******************************************************************************
 * Copyright (C) 2001 Daniel Selman
 * 
 * First distributed with the book "Java 3D Programming" by Daniel Selman and
 * published by Manning Publications. http://manning.com/selman
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, version 2.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * The license can be found on the WWW at: http://www.fsf.org/copyleft/gpl.html
 * 
 * Or by writing to: Free Software Foundation, Inc., 59 Temple Place - Suite
 * 330, Boston, MA 02111-1307, USA.
 * 
 * Authors can be contacted at: Daniel Selman:  daniel@selman.org
 * 
 * If you make changes you think others would like, please contact one of the
 * authors or someone at the www.j3d.org web site.
 ******************************************************************************/

//*****************************************************************************
/**
 * Java3dApplet
 * 
 * Base class for defining a Java 3D applet. Contains some useful methods for
 * defining views and scenegraphs etc.
 * 
 * @author Daniel Selman
 * @version 1.0
 */
//*****************************************************************************

abstract class Java3dApplet extends Applet {
  public static int m_kWidth = 300;

  public static int m_kHeight = 300;

  protected String[] m_szCommandLineArray = null;

  protected VirtualUniverse m_Universe = null;

  protected BranchGroup m_SceneBranchGroup = null;

  protected Bounds m_ApplicationBounds = null;

  //  protected com.tornadolabs.j3dtree.Java3dTree m_Java3dTree = null;

  public Java3dApplet() {
  }

  public boolean isApplet() {
    try {
      System.getProperty("user.dir");
      System.out.println("Running as Application.");
      return false;
    } catch (Exception e) {
    }

    System.out.println("Running as Applet.");
    return true;
  }

  public URL getWorkingDirectory() throws java.net.MalformedURLException {
    URL url = null;

    try {
      File file = new File(System.getProperty("user.dir"));
      System.out.println("Running as Application:");
      System.out.println("   " + file.toURL());
      return file.toURL();
    } catch (Exception e) {
    }

    System.out.println("Running as Applet:");
    System.out.println("   " + getCodeBase());

    return getCodeBase();
  }

  public VirtualUniverse getVirtualUniverse() {
    return m_Universe;
  }

  //public com.tornadolabs.j3dtree.Java3dTree getJ3dTree() {
  //return m_Java3dTree;
  //  }

  public Locale getFirstLocale() {
    java.util.Enumeration e = m_Universe.getAllLocales();

    if (e.hasMoreElements() != false)
      return (Locale) e.nextElement();

    return null;
  }

  protected Bounds getApplicationBounds() {
    if (m_ApplicationBounds == null)
      m_ApplicationBounds = createApplicationBounds();

    return m_ApplicationBounds;
  }

  protected Bounds createApplicationBounds() {
    m_ApplicationBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
        100.0);
    return m_ApplicationBounds;
  }

  protected Background createBackground() {
    Background back = new Background(new Color3f(0.9f, 0.9f, 0.9f));
    back.setApplicationBounds(createApplicationBounds());
    return back;
  }

  public void initJava3d() {
    //  m_Java3dTree = new com.tornadolabs.j3dtree.Java3dTree();
    m_Universe = createVirtualUniverse();

    Locale locale = createLocale(m_Universe);

    BranchGroup sceneBranchGroup = createSceneBranchGroup();

    ViewPlatform vp = createViewPlatform();
    BranchGroup viewBranchGroup = createViewBranchGroup(
        getViewTransformGroupArray(), vp);

    createView(vp);

    Background background = createBackground();

    if (background != null)
      sceneBranchGroup.addChild(background);

    //    m_Java3dTree.recursiveApplyCapability(sceneBranchGroup);
    //  m_Java3dTree.recursiveApplyCapability(viewBranchGroup);

    locale.addBranchGraph(sceneBranchGroup);
    addViewBranchGroup(locale, viewBranchGroup);

    onDoneInit();
  }

  protected void onDoneInit() {
    //  m_Java3dTree.updateNodes(m_Universe);
  }

  protected double getScale() {
    return 1.0;
  }

  public TransformGroup[] getViewTransformGroupArray() {
    TransformGroup[] tgArray = new TransformGroup[1];
    tgArray[0] = new TransformGroup();

    // move the camera BACK a little...
    // note that we have to invert the matrix as
    // we are moving the viewer
    Transform3D t3d = new Transform3D();
    t3d.setScale(getScale());
    t3d.setTranslation(new Vector3d(0.0, 0.0, -20.0));
    t3d.invert();
    tgArray[0].setTransform(t3d);

    return tgArray;
  }

  protected void addViewBranchGroup(Locale locale, BranchGroup bg) {
    locale.addBranchGraph(bg);
  }

  protected Locale createLocale(VirtualUniverse u) {
    return new Locale(u);
  }

  protected BranchGroup createSceneBranchGroup() {
    m_SceneBranchGroup = new BranchGroup();
    return m_SceneBranchGroup;
  }

  protected View createView(ViewPlatform vp) {
    View view = new View();

    PhysicalBody pb = createPhysicalBody();
    PhysicalEnvironment pe = createPhysicalEnvironment();

    AudioDevice audioDevice = createAudioDevice(pe);

    if (audioDevice != null) {
      pe.setAudioDevice(audioDevice);
      audioDevice.initialize();
    }

    view.setPhysicalEnvironment(pe);
    view.setPhysicalBody(pb);

    if (vp != null)
      view.attachViewPlatform(vp);

    view.setBackClipDistance(getBackClipDistance());
    view.setFrontClipDistance(getFrontClipDistance());

    Canvas3D c3d = createCanvas3D();
    view.addCanvas3D(c3d);
    addCanvas3D(c3d);

    return view;
  }

  protected PhysicalBody createPhysicalBody() {
    return new PhysicalBody();
  }

  protected AudioDevice createAudioDevice(PhysicalEnvironment pe) {
    JavaSoundMixer javaSoundMixer = new JavaSoundMixer(pe);

    if (javaSoundMixer == null)
      System.out.println("create of audiodevice failed");

    return javaSoundMixer;
  }

  protected PhysicalEnvironment createPhysicalEnvironment() {
    return new PhysicalEnvironment();
  }

  protected float getViewPlatformActivationRadius() {
    return 100;
  }

  protected ViewPlatform createViewPlatform() {
    ViewPlatform vp = new ViewPlatform();
    vp.setViewAttachPolicy(View.RELATIVE_TO_FIELD_OF_VIEW);
    vp.setActivationRadius(getViewPlatformActivationRadius());

    return vp;
  }

  protected Canvas3D createCanvas3D() {
    GraphicsConfigTemplate3D gc3D = new GraphicsConfigTemplate3D();
    gc3D.setSceneAntialiasing(GraphicsConfigTemplate.PREFERRED);
    GraphicsDevice gd[] = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getScreenDevices();

    Canvas3D c3d = new Canvas3D(gd[0].getBestConfiguration(gc3D));
    c3d.setSize(getCanvas3dWidth(c3d), getCanvas3dHeight(c3d));

    return c3d;
  }

  protected int getCanvas3dWidth(Canvas3D c3d) {
    return m_kWidth;
  }

  protected int getCanvas3dHeight(Canvas3D c3d) {
    return m_kHeight;
  }

  protected double getBackClipDistance() {
    return 100.0;
  }

  protected double getFrontClipDistance() {
    return 1.0;
  }

  protected BranchGroup createViewBranchGroup(TransformGroup[] tgArray,
      ViewPlatform vp) {
    BranchGroup vpBranchGroup = new BranchGroup();

    if (tgArray != null && tgArray.length > 0) {
      Group parentGroup = vpBranchGroup;
      TransformGroup curTg = null;

      for (int n = 0; n < tgArray.length; n++) {
        curTg = tgArray[n];
        parentGroup.addChild(curTg);
        parentGroup = curTg;
      }

      tgArray[tgArray.length - 1].addChild(vp);
    } else
      vpBranchGroup.addChild(vp);

    return vpBranchGroup;
  }

  protected void addCanvas3D(Canvas3D c3d) {
    setLayout(new BorderLayout());
    add(c3d, BorderLayout.CENTER);
    doLayout();
  }

  protected VirtualUniverse createVirtualUniverse() {
    return new VirtualUniverse();
  }

  protected void saveCommandLineArguments(String[] szArgs) {
    m_szCommandLineArray = szArgs;
  }

  protected String[] getCommandLineArguments() {
    return m_szCommandLineArray;
  }
}