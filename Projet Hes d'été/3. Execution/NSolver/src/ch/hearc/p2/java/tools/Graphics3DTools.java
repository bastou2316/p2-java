package ch.hearc.p2.java.tools;

import javax.media.j3d.GeometryArray;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.geometry.GeometryInfo;

public class Graphics3DTools {

	private static final float[][] coordXY = new float[][] { { -1, -1 },
			{ -1, 1 }, { 1, 1 }, { 1, -1 } };

	
	/**
	 * create plan : ax + by + cz = d
	 * fiting in the box defined by scaleFactor
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param scaleFactor : facteur de zoom
	 * @return
	 */
	public static GeometryArray createPlanBoxed(float a, float b, float c,
			float d, float scaleFactor) {

		GeometryInfo gi = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);

		Point3f[] pts = null;
		int[] stripCounts = new int[1];
		

		// x,y
		float[] z = new float[4];
		for(int i = 0; i<4; i++){
			z[i] = (d - a * scaleFactor * coordXY[i][0] - b * scaleFactor
					* coordXY[i][1]) / (scaleFactor * c);
		}

		// Point3f => (y,z,x)
		if (c != 0) {
			// On a : ax + by + cz = d
			// Donc : z = (d - ax - by) / c

			boolean[] insideBox = new boolean[4];

			int cas = 0;
			for (int i = 0; i < 4; i++) {
				if (Math.abs(z[i]) <= 1) {
					insideBox[i] = true;
					++cas;
				}
			}

			switch (cas) {
			case 0:

				int state = 0;
				for (int i = 0; i < 4; i++) {
					if (z[i] > 1f)
						++state;
				}
				// state :
				// 4 : 4 pts en haut => rien à faire
				// 3 : 3 pts en haut 1 en bas
				// 2 : 2 pts en haut 2 en bas
				// 1 : 1 pt en haut 3 en bas
				// 0 : 0 pt en haut => rien à faire

				int index1;
				int index2;
				if (state <= 3 && state >= 1) {
					pts = new Point3f[4];
					stripCounts[0] = 4;
				}

				switch (state) {
				case 1:
					index1 = -1;
					while (z[++index1] < 1f);
					planBoxed_case0a(a, b, c, d, scaleFactor, pts, index1);
					break;

				case 2:
					index1 = -1;
					while (z[++index1] < 1f);

					index2 = index1 + 1;
					if (z[index2] < 1f) {
						int tempIndex = index1;
						index1 = (index1 + 3) % 4;
						index2 = tempIndex;
					}

					if (coordXY[index1][1] == coordXY[index1][0]) {
						pts[0] = new Point3f(coordXY[index1][1], 1, (d - b
								* scaleFactor * coordXY[index1][1] - c
								* scaleFactor)
								/ (a * scaleFactor));
						pts[1] = new Point3f(coordXY[index2][1], 1, (d - b
								* scaleFactor * coordXY[index2][1] - c
								* scaleFactor)
								/ (a * scaleFactor));
						pts[2] = new Point3f(coordXY[index2][1], -1, (d - b
								* scaleFactor * coordXY[index2][1] - c
								* -scaleFactor)
								/ (a * scaleFactor));
						pts[3] = new Point3f(coordXY[index1][1], -1, (d - b
								* scaleFactor * coordXY[index1][1] - c
								* -scaleFactor)
								/ (a * scaleFactor));
					} else {
						pts[0] = new Point3f((d - a * scaleFactor
								* coordXY[index1][0] - c * scaleFactor)
								/ (b * scaleFactor), 1, coordXY[index1][0]);
						pts[1] = new Point3f((d - a * scaleFactor
								* coordXY[index2][0] - c * scaleFactor)
								/ (b * scaleFactor), 1, coordXY[index2][0]);
						pts[2] = new Point3f((d - a * scaleFactor
								* coordXY[index2][0] - c * -scaleFactor)
								/ (b * scaleFactor), -1, coordXY[index2][0]);
						pts[3] = new Point3f((d - a * scaleFactor
								* coordXY[index1][0] - c * -scaleFactor)
								/ (b * scaleFactor), -1, coordXY[index1][0]);
					}
					break;

				case 3:
					index1 = -1;
					while (z[++index1] > 1f);
					planBoxed_case0a(a, b, c, d, scaleFactor, pts, index1);
					break;

				}
				break;

			case 1:
				state = 0;
				for (int i = 0; i < 4; i++) {
					if (z[i] > 1f)
						++state;
				}
				
				if(state == 0 || state == 3){
					pts = new Point3f[3];
					stripCounts[0] = 3;
				}
				else{
					pts = new Point3f[5];
					stripCounts[0] = 5;
				}					

				for (int i = 0; i < 4; i++) {
					if (insideBox[i] == true) {
						switch (state) {
						case 0:
							planBoxed_case1a(a, b, c, d, scaleFactor, pts, i, true);
							break;
						case 1:
							planBoxed_case1b(a, b, c, d, scaleFactor, pts, i, true);
							break;
						case 2:
							planBoxed_case1b(a, b, c, d, scaleFactor, pts, i, false);
							break;							
						case 3:
							planBoxed_case1a(a, b, c, d, scaleFactor, pts, i, false);
							break;
						}
						pts[0] = new Point3f(coordXY[i][1], z[i], coordXY[i][0]);
					}
				}
				break;

			case 2:
				int j = -1;
				while (insideBox[++j]);

				// points hors cube côte à côte (j et j+1)
				if (!insideBox[j + 1]) {
					stripCounts[0] = 4;
					pts = new Point3f[4];
					planBoxed_case2a(a, b, c, d, scaleFactor, pts, z, j, true);

				// points hors cube l'une en face de l'autre, forcément de 
				// signes opposés
				} else if (!insideBox[(j + 2) % 4]) {
					stripCounts[0] = 6;
					pts = new Point3f[6];
					if (z[j] > 1)
						planBoxed_case2b(a, b, c, d, scaleFactor, pts, z, j,
								true);
					else 
						planBoxed_case2b(a, b, c, d, scaleFactor, pts, z, j,
								false);
							
				// côte à côte : j et (j+3)%4 sont en dehors du cube
				} else {
					stripCounts[0] = 4;
					pts = new Point3f[4];
					planBoxed_case2a(a, b, c, d, scaleFactor, pts, z, j, false);
				}
				break;

			case 3:
				pts = new Point3f[5];
				stripCounts[0] = 5;

				int biais = 0; // astuce
				for (int i = 0; i < 4; i++) {
					if (biais == 0 && insideBox[i] == false) {
						int k = i + 1;
						int l = i;
						if (coordXY[i][0] == coordXY[i][1]) {
							k = i;
							l = i + 1;
						}
						float z_fixe;
						if (z[i] > 1)
							z_fixe = 1f;
						else
							z_fixe = -1f;

						pts[k] = new Point3f(coordXY[i][1], z_fixe, (d - b
								* scaleFactor * coordXY[i][1] - c * scaleFactor
								* z_fixe)
								/ (a * scaleFactor));
						pts[l] = new Point3f((d - a * scaleFactor
								* coordXY[i][0] - c * scaleFactor * z_fixe)
								/ (b * scaleFactor), z_fixe, coordXY[i][0]);

						biais = 1;
					} else {
						pts[i + biais] = new Point3f(coordXY[i][1], z[i],
								coordXY[i][0]);
					}
				}
				break;

			case 4:
				pts = new Point3f[4];
				stripCounts[0] = 4;
				for (int i = 0; i < 4; i++) {
					pts[i] = new Point3f(coordXY[i][1], z[i], coordXY[i][0]);
				}
				break;
			}

		} else if (b != 0) { // plan vertical
			// On a : ax + by = d
			// Donc : y = (d - ax) / b

			float coordY1 = (d - a * -scaleFactor) / (scaleFactor * b);
			float coordY2 = (d - a * scaleFactor) / (scaleFactor * b);
			if (Math.abs(coordY1) <= 1 && Math.abs(coordY2) <= 1) {
				pts = new Point3f[4];
				stripCounts[0] = 4;

				pts[0] = new Point3f(coordY1, -1, -1);
				pts[1] = new Point3f(coordY1, 1, -1);
				pts[2] = new Point3f(coordY2, 1, 1);
				pts[3] = new Point3f(coordY2, -1, 1);
			} else if (Math.abs(coordY1) > 1 && Math.abs(coordY2) <= 1) {

				pts = new Point3f[4];
				stripCounts[0] = 4;

				if (coordY1 > 1)
					coordY1 = 1;
				else
					coordY1 = -1;
				pts[0] = new Point3f(coordY1, -1, (d - b * scaleFactor
						* coordY1)
						/ (scaleFactor * a));
				pts[1] = new Point3f(coordY1, 1,
						(d - b * scaleFactor * coordY1) / (scaleFactor * a));
				pts[2] = new Point3f(coordY2, 1, 1);
				pts[3] = new Point3f(coordY2, -1, 1);
			} else if (Math.abs(coordY1) <= 1 && Math.abs(coordY2) > 1) {
				pts = new Point3f[4];
				stripCounts[0] = 4;
				if (coordY2 > 1)
					coordY2 = 1;
				else
					coordY2 = -1;
				pts[0] = new Point3f(coordY1, -1, -1);
				pts[1] = new Point3f(coordY1, 1, -1);
				pts[2] = new Point3f(coordY2, 1,
						(d - b * scaleFactor * coordY2) / (scaleFactor * a));
				pts[3] = new Point3f(coordY2, -1, (d - b * scaleFactor
						* coordY2)
						/ (scaleFactor * a));
			} else {
				float coordX1 = (d - b * scaleFactor * 1) / (scaleFactor * a);
				float coordX2 = (d - b * scaleFactor * -1) / (scaleFactor * a);
				
				if (Math.abs(coordX1) <= 1 && Math.abs(coordX2) <= 1) {
					pts = new Point3f[4];
					stripCounts[0] = 4;
					pts[0] = new Point3f(1, -1, coordX1);
					pts[1] = new Point3f(1, 1, coordX1);
					pts[2] = new Point3f(-1, 1, coordX2);
					pts[3] = new Point3f(-1, -1, coordX2);
				} else {
//					 rien car plan totalement en dehors du cube
				}	
			}

		} else if (a != 0) { // plan vertical
			// On a : ax = d
			// Donc : x = d/a

			float coordX = d / (a * scaleFactor);
			if (Math.abs(coordX) <= 1) {
				pts = new Point3f[4];
				stripCounts[0] = 4;

				pts[0] = new Point3f(-1, -1, coordX);
				pts[1] = new Point3f(1, -1, coordX);
				pts[2] = new Point3f(1, 1, coordX);
				pts[3] = new Point3f(-1, 1, coordX);
			} else {
				// rien car plan totalement en dehors du cube
			}
			
		} else {
			System.err
					.println("equation error : cannot display this plan (0x + 0y + 0z = "
							+ d + ")");
		}

		if(pts != null){
			gi.setCoordinates(pts);
			gi.setStripCounts(stripCounts);
			int[] contourCount = new int[1];
			contourCount[0] = 1;
			gi.setContourCounts(contourCount);
			return gi.getGeometryArray();
		}
		else
			return null;
	}

	private static void planBoxed_case0a(float a, float b, float c, float d,
			float scaleFactor, Point3f[] pts, int index) {
		
		pts[0] = new Point3f((d - a * scaleFactor * coordXY[index][0] - c
				* -scaleFactor)
				/ (b * scaleFactor), -1, coordXY[index][0]);
		pts[1] = new Point3f((d - a * scaleFactor * coordXY[index][0] - c
				* scaleFactor)
				/ (b * scaleFactor), 1, coordXY[index][0]);
		pts[2] = new Point3f(coordXY[index][1], 1, (d - b * scaleFactor
				* coordXY[index][1] - c * scaleFactor)
				/ (a * scaleFactor));
		pts[3] = new Point3f(coordXY[index][1], -1, (d - b * scaleFactor
				* coordXY[index][1] - c * -scaleFactor)
				/ (a * scaleFactor));
	}
	
	private static void planBoxed_case1a(float a, float b, float c, float d,
			float scaleFactor, Point3f[] pts, int i, boolean isCase0) {
		float z_fixe = 1;
		if(isCase0)
			z_fixe = -1;
		pts[1] = new Point3f(coordXY[i][1], z_fixe, (d - b
				* scaleFactor * coordXY[i][1] - c
				* scaleFactor* z_fixe)
				/ (a * scaleFactor));
		pts[2] = new Point3f((d - a * scaleFactor
				* coordXY[i][0] - c * scaleFactor * z_fixe)
				/ (b * scaleFactor), z_fixe, coordXY[i][0]);	
	}
	
	private static void planBoxed_case1b(float a, float b, float c, float d,
			float scaleFactor, Point3f[] pts, int i, boolean isCase1) {		
		
		float z_fixe = 1;
		if (coordXY[i][0] == coordXY[i][1]) {
			if(isCase1)
				z_fixe = -1;		
			pts[1] = new Point3f(coordXY[i][1], z_fixe, (d
					- b * scaleFactor * coordXY[i][1] - c
					* scaleFactor * z_fixe)
					/ (a * scaleFactor));
			pts[2] = new Point3f(-coordXY[i][1], z_fixe, (d
					- b * scaleFactor * -coordXY[i][1] - c
					* scaleFactor * z_fixe)
					/ (a * scaleFactor));

			z_fixe = -z_fixe;			
			pts[3] = new Point3f(-coordXY[i][1], z_fixe, (d
					- b * scaleFactor * -coordXY[i][1] - c
					* scaleFactor * z_fixe)
					/ (a * scaleFactor));
			pts[4] = new Point3f((d - a * scaleFactor
					* coordXY[i][0] - c * scaleFactor
					* z_fixe)
					/ (b * scaleFactor), z_fixe,
					coordXY[i][0]);
		} else {
			if(isCase1)
				z_fixe = -1;
			pts[1] = new Point3f((d - a * scaleFactor
					* coordXY[i][0] - c * scaleFactor
					* z_fixe)
					/ (b * scaleFactor), z_fixe,
					coordXY[i][0]);
			pts[2] = new Point3f((d - a * scaleFactor
					* -coordXY[i][0] - c * scaleFactor
					* z_fixe)
					/ (b * scaleFactor), z_fixe,
					-coordXY[i][0]);

			z_fixe = -z_fixe;
			pts[3] = new Point3f((d - a * scaleFactor
					* -coordXY[i][0] - c * scaleFactor
					* z_fixe)
					/ (b * scaleFactor), z_fixe,
					-coordXY[i][0]);
			pts[4] = new Point3f(coordXY[i][1], z_fixe, (d
					- b * scaleFactor * coordXY[i][1] - c
					* scaleFactor * z_fixe)
					/ (a * scaleFactor));
		}		
	}

	private static void planBoxed_case2a(float a, float b, float c, float d,
			float scaleFactor, Point3f[] pts, float[] z, int j, boolean isAfterJ) {

		int z_fixe = -1;
		if (z[j] > 1)
			z_fixe = 1;

		int indexIn1 = (j + 3) % 4;
		int indexIn2 = j;
		int indexIn3 = j + 1;
		int indexIn4 = (j + 2) % 4;
		if (isAfterJ) {
			indexIn1 = j;
			indexIn2 = j + 1;
			indexIn3 = (j + 2) % 4;
			indexIn4 = (j + 3) % 4;
		}

		if (coordXY[indexIn1][0] == coordXY[indexIn1][1]) {
			pts[0] = new Point3f(coordXY[indexIn1][1], z_fixe, (d - b
					* scaleFactor * coordXY[indexIn1][1] - c * scaleFactor
					* z_fixe)
					/ (a * scaleFactor));
			pts[1] = new Point3f(coordXY[indexIn2][1], z_fixe, (d - b
					* scaleFactor * coordXY[indexIn2][1] - c * scaleFactor
					* z_fixe)
					/ (a * scaleFactor));
		} else {
			pts[0] = new Point3f(
					(d - a * scaleFactor * coordXY[indexIn1][0] - c
							* scaleFactor * z_fixe)
							/ (b * scaleFactor), z_fixe, coordXY[indexIn1][0]);
			pts[1] = new Point3f(
					(d - a * scaleFactor * coordXY[indexIn2][0] - c
							* scaleFactor * z_fixe)
							/ (b * scaleFactor), z_fixe, coordXY[indexIn2][0]);
		}
		pts[2] = new Point3f(coordXY[indexIn3][1], z[indexIn3],
				coordXY[indexIn3][0]);
		pts[3] = new Point3f(coordXY[indexIn4][1], z[indexIn4],
				coordXY[indexIn4][0]);

	}

	private static void planBoxed_case2b(float a, float b, float c, float d,
			float scaleFactor, Point3f[] pts, float[] z, int j,
			boolean isHighAtJ) {
		

		int l = 1;
		int m = 0;
		if (coordXY[j][0] == coordXY[j][1]) {
			l = 0;
			m = 1;
		}

		int z_fixe = -1;
		if (isHighAtJ)
			z_fixe = 1;

		int indexIn1 = j + 1;
		int indexIn2 = (j + 3) % 4;
		pts[l] = new Point3f(coordXY[j][1], z_fixe, (d - b * scaleFactor
				* coordXY[j][1] - c * scaleFactor * z_fixe)
				/ (a * scaleFactor));

		pts[m] = new Point3f((d - a * scaleFactor * coordXY[j][0] - c
				* scaleFactor * z_fixe)
				/ (b * scaleFactor), z_fixe, coordXY[j][0]);

		pts[2] = new Point3f(coordXY[indexIn1][1], z[indexIn1],
				coordXY[indexIn1][0]);

		if (isHighAtJ)
			z_fixe = -1;
		else
			z_fixe = 1;
		pts[l + 3] = new Point3f(-coordXY[j][1], z_fixe, (d - b * scaleFactor
				* -coordXY[j][1] - c * scaleFactor * z_fixe)
				/ (a * scaleFactor));

		pts[m + 3] = new Point3f((d - a * scaleFactor * -coordXY[j][0] - c
				* scaleFactor * z_fixe)
				/ (b * scaleFactor), z_fixe, -coordXY[j][0]);

		pts[5] = new Point3f(coordXY[indexIn2][1], z[indexIn2],
				coordXY[indexIn2][0]);
	}
	
	
	/**
	 * create plan : ax + by + cz = d
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param scale
	 *            == 1 : plan fits the box on x and y 
	 *            < 1 : plan smaller than the box on x and y 
	 *            > 1 : plan bigger than the box on x and y
	 * @param scaleFactor 
	 * 			  facteur de zoom
	 * @return
	 */
	public static GeometryArray createPlan(float a, float b, float c, float d,
			float scale, float scaleFactor) {

		GeometryInfo gi = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);

		Point3f[] pts = new Point3f[4];
		pts[0] = new Point3f(0, 0, 0);
		pts[1] = new Point3f(0, 0, 0);
		pts[2] = new Point3f(0, 0, 0);
		pts[3] = new Point3f(0, 0, 0);
		int[] stripCounts = new int[1];
		stripCounts[0] = 4;
		int[] contourCount = new int[1];
		contourCount[0] = 1;

		float[] z = new float[4];
		for(int i = 0; i<4; i++){
			z[i] = (d - a * scaleFactor * coordXY[i][0] - b * scaleFactor
					* coordXY[i][1]) // -1 -1
					/ (scaleFactor * c);
		}

		// Point3f => (y,z,x)
		if (c != 0) {
			// On a : ax + by + cz = d
			// Donc : z = (d - ax - by) / c
			for(int i = 0; i < 4; i++){
				pts[i] = new Point3f(scale * coordXY[i][1], z[0], scale
						* coordXY[i][0]);
			}
		} else if (b != 0) { // plan vertical
			// On a : ax + by = d
			// Donc : y = (d - ax) / b
			pts[0] = new Point3f((d - a * -scaleFactor * scale)
					/ (scaleFactor * b), -scale, -scale);
			pts[1] = new Point3f((d - a * -scaleFactor * scale)
					/ (scaleFactor * b), scale, -scale);
			pts[2] = new Point3f((d - a * scaleFactor * scale)
					/ (scaleFactor * b), scale, scale);
			pts[3] = new Point3f((d - a * scaleFactor * scale)
					/ (scaleFactor * b), -scale, scale);
		} else if (a != 0) { // plan vertical
			// On a : ax = d
			// Donc : x = d/a
			pts[0] = new Point3f(-scale, -scale, d / (a * scaleFactor));
			pts[1] = new Point3f(scale, -scale, d / (a * scaleFactor));
			pts[2] = new Point3f(scale, scale, d / (a * scaleFactor));
			pts[3] = new Point3f(-scale, scale, d / (a * scaleFactor));
		} else {
			System.err
					.println("equation error : cannot display this plan (0x + 0y + 0z = "
							+ d + ")");
		}

		gi.setCoordinates(pts);
		gi.setStripCounts(stripCounts);
		gi.setContourCounts(contourCount);

		return gi.getGeometryArray();
	}

}
