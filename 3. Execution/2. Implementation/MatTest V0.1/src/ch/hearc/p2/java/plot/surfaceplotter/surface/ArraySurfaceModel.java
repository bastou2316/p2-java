package ch.hearc.p2.java.plot.surfaceplotter.surface;

import java.util.List;

public class ArraySurfaceModel extends AbstractSurfaceModel {

	SurfaceVertex[][] surfaceVertex;

	/** Creates two surfaces using data from the array.
	 * 
	 * @param xmin lower bound of x values
	 * @param xmax upper bound of x values
	 * @param ymin lower bound of y values
	 * @param ymax upper bound of y values
	 * @param size number of items in each dimensions (ie z1 = float[size][size] )
	 * @param z1 value matrix (null supported)
	 * @param z2 secondary function value matrix (null supported)
	 */
	public void setValues(float xmin, float xmax, float ymin, float ymax, int size, List<float[][]> list) {
		setDataAvailable(false); // clean space
		setXMin(xmin);
		setXMax(xmax);
		setYMin(ymin);
		setYMax(ymax);
		setCalcDivisions(size-1);
		
		final float stepx = (xMax - xMin) / calcDivisions;
		final float stepy = (yMax - yMin) / calcDivisions;
		final float xfactor = 20 / (xMax - xMin); // 20 aint magic: surface vertex requires a value in [-10 ; 10]
		final float yfactor = 20 / (yMax - yMin);
		
		final int total = (calcDivisions + 1) * (calcDivisions + 1); // compute total size
		surfaceVertex = new SurfaceVertex[list.size()][total];
		
		
		for (int i = 0; i <= calcDivisions; i++)
			for (int j = 0; j <= calcDivisions; j++) {
				int k = i * (calcDivisions + 1) + j;

				float xv = xMin + i * stepx;
				float yv = yMin + j * stepy;
					
				float[] v = new float[list.size()];
					for(int l = 0; l < list.size(); l++){
						v[l] = list.get(l)!=null?list.get(l)[i][j]:Float.NaN;
						if (Float.isInfinite(v[l]))
							v[l] = Float.NaN;
						if (!Float.isNaN(v[l])) {
							if (Float.isNaN(zMax) || (v[l] > zMax))
								zMax = v[l];
							else if (Float.isNaN(zMin) || (v[l] < zMin))
								zMin = v[l];
						}

						surfaceVertex[l][k] = new SurfaceVertex((xv - xMin) * xfactor - 10, (yv - yMin) * yfactor - 10, v[l]);
					}
			}
		
		
		autoScale();
		setDataAvailable(true); 
		fireStateChanged();
	}

	public SurfaceVertex[][] getSurfaceVertex() {
		return surfaceVertex;
	}

}
