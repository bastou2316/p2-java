package ch.hearc.p2.java.plot;


import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicArrowButton;

import ch.hearc.p2.java.plot.surfaceplotter.JSurfacePanel;
import ch.hearc.p2.java.plot.surfaceplotter.Mapper;
import ch.hearc.p2.java.plot.surfaceplotter.AbstractSurfaceModel;
import ch.hearc.p2.java.plot.surfaceplotter.surface.ArraySurfaceModel;
import ch.hearc.p2.java.plot.surfaceplotter.surface.SurfaceVertex;

public class UseTest3d {

        public void testSomething() {
        		
                JSurfacePanel jsp = new JSurfacePanel();
                jsp.setTitleText("Test");

                JFrame jf = new JFrame("test");
                jf.setSize(1000, 1000);
                jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jf.getContentPane().add(jsp, BorderLayout.CENTER);
                jf.setVisible(true);

                List<float[][]> list = new ArrayList<float[][]>(3);
//                Random rand = new Random();
                int max = 40;
                float[][] z1 = new float[max][max];
                float[][] z2 = new float[max][max];
                float[][] z3 = new float[max][max];
                
                for (int i = 0; i < max; i++) {
                        for (int j = 0; j < max; j++) {
                                //z1[i][j] = rand.nextFloat() * 20 - 10f;
                        		z1[i][j] = f1(i,j);
                                z2[i][j] = f2(i,j);
                                z3[i][j] = f3(i,j);
                        }
                }
                list.add(z1);
                list.add(z2);
                list.add(z3);
                
                ArraySurfaceModel sm = new ArraySurfaceModel();
                sm.setValues(0f,10f,0f,10f,max, list);
                jsp.setModel(sm);
                
                // sm.doRotate();

                // canvas.doPrint();
                // sm.doCompute();
        }

        public static float f1(float x, float y) {
                //return (float) (Math.sin(x * x + y * y) / (x * x + y * y));
                // return (float)(10*x*x+5*y*y+8*x*y -5*x+3*y);
        		return 2*x - 3*y + 7;
        }

        public static float f2(float x, float y) {
                //return (float) (Math.sin(x * x - y * y) / (x * x + y * y));
                //return (float)(10*x*x+5*y*y+15*x*y-2*x-y);
        		return 10*x + 10*y - 350;
        }
        
        public static float f3(float x, float y){
        		return -2*x + 3*y + 25;
        }

        public static void main(String[] args) {
                SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                                new UseTest3d().testSomething();
                        }
                });

        }

}