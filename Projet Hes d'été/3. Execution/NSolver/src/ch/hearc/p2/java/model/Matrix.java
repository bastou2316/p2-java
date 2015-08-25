
package ch.hearc.p2.java.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ch.hearc.p2.java.tools.MathTools;

public final class Matrix implements Serializable
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public Matrix(int rows, int cols)
		{
		if (rows <= 0 || cols <= 0) { throw new IllegalArgumentException("Invalid number of rows or columns"); }

		values = new double[rows][cols];
		currentStep = 0;
		hist = new double[50][rows][cols];
		tabOperations = new String[50];
		histLength = 0;
		mapIndexVariableName = new TreeMap<Integer, String>();
		this.variableName = "a";
		listVariableName = new ArrayList<String>();
		}

	public Matrix(double[][] values, String variableToken)
		{
		mapIndexVariableName = new TreeMap<Integer, String>();
		this.variableName = variableToken;

		this.values = values;
		currentStep = 0;
		hist = new double[20][values.length][values[0].length];
		tabOperations = new String[20];
		histLength = 0;
		listVariableName = new ArrayList<String>();
		}

	public Matrix(double[][] values)
		{
		mapIndexVariableName = new TreeMap<Integer, String>();
		listVariableName = new ArrayList<String>();
		this.variableName = "a";
		this.values = values;
		currentStep = 0;
		hist = new double[20][values.length][values[0].length];
		tabOperations = new String[20];
		histLength = 0;
		}

	public Matrix cloneOf()
		{
		return new Matrix(this);
		}

	public Matrix(Matrix src)
		{
		this(src.rowCount(), src.columnCount());

		int rows = rowCount();
		int cols = columnCount();

		for(int i = 0; i < rows; i++)
			{
			for(int j = 0; j < cols; j++)
				{
				set(i, j, src.get(i, j));
				}
			}
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public int rowCount()
		{
		return values.length;
		}

	public int columnCount()
		{
		return values[0].length;
		}



	/*------------------------------------------------------------------*\
	|*							Elementary Operations				    *|
	\*------------------------------------------------------------------*/

	public void swapRows(int row0, int row1)
		{
		if (row0 < 0 || row0 >= values.length || row1 < 0 || row1 >= values.length) { throw new IndexOutOfBoundsException("Row index out of bounds"); }
		double[] temp = values[row0];
		values[row0] = values[row1];
		values[row1] = temp;
		}

	public void multiplyRow(int row, double factor)
		{
		for(int j = 0, cols = columnCount(); j < cols; j++)
			{
			set(row, j, get(row, j) * factor);
			}
		}

	public void addRows(int srcRow, int destRow, double factor)
		{
		for(int j = 0, cols = columnCount(); j < cols; j++)
			{
			set(destRow, j, get(destRow, j) + get(srcRow, j) * factor);
			}
		}

	/*------------------------------------------------------------------*\
	|*					Methode Public Static				            *|
	\*------------------------------------------------------------------*/

	public static boolean isEqual(double[][] tab1, double[][] tab2)
		{
		//If array shape is equals, then check the elements inside it
		for(int i = 0; i < tab1.length; i++)
			{
			for(int j = 0; j < tab1[i].length; j++)
				{
				if (!MathTools.isEquals(tab1[i][j], tab2[i][j])) { return false; }
				}
			}
		//If each test has passed, then the arrays are equals
		return true;
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	public void set(int row, int col, double val)
		{
		if (row < 0 || row >= values.length || col < 0 || col >= values[0].length) { throw new IndexOutOfBoundsException("Row or column index out of bounds"); }
		values[row][col] = val;
		}

	public void set(double[][] src)
		{

		if (src.length < 0 || src.length > values.length || src[0].length < 0 || src[0].length > values[0].length) { throw new IndexOutOfBoundsException("Row or column index out of bounds"); }
		int rows = rowCount();
		int cols = columnCount();

		for(int i = 0; i < rows; i++)
			{
			for(int j = 0; j < cols; j++)
				{
				values[i][j] = src[i][j];
				}
			}
		}

	public void setVariableName(String name){
		variableName=name;
	}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/
	@Override
	public String toString()
		{
		DecimalFormat formatter = new DecimalFormat("0.##");
		StringBuilder builder = new StringBuilder();
		int rows = rowCount();
		int cols = columnCount();

		for(int i = 0; i < rows; i++)
			{
			for(int j = 0; j < cols-1; j++)
				{
				builder.append(formatter.format(get(i, j)));
				builder.append(" ");
				}
			builder.append("= ");
			builder.append(formatter.format(get(i,cols-1)));
			builder.append(System.getProperty("line.separator"));
			}
		builder.append(System.getProperty("line.separator"));
		return builder.toString();
		}

	public String stepToString(int step)
		{
		DecimalFormat formatter = new DecimalFormat("0.##");
		StringBuilder builder = new StringBuilder();
		int rows = rowCount();
		int cols = columnCount();
		double[][] tabStep = getStep(step);
		for(int i = 0; i < rows; i++)
			{
			for(int j = 0; j < cols-1; j++)
				{
				builder.append(formatter.format(tabStep[i][j]));
				builder.append(" ");
				}
			builder.append("= ");
			builder.append(formatter.format(tabStep[i][cols-1]));
			builder.append(System.getProperty("line.separator"));
			}
		System.out.println(new Matrix(hist[0], variableName).toString());
		return builder.toString();
		}

	public double get(int row, int col)
		{
		if (row < 0 || row >= values.length || col < 0 || col >= values[row].length) { throw new IndexOutOfBoundsException("Row or column index out of bounds"); }
		return values[row][col];
		}

	public Matrix getMatrix(int i0, int i1, int j0, int j1)
		{
		Matrix X = new Matrix(i1 - i0 + 1, j1 - j0 + 1);
		double[][] B = X.getValues();
		try
			{
			for(int i = i0; i <= i1; i++)
				{
				for(int j = j0; j <= j1; j++)
					{
					B[i - i0][j - j0] = values[i][j];
					}
				}
			}
		catch (ArrayIndexOutOfBoundsException e)
			{
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
			}
		return X;
		}

	public double[][] getValues()
		{
		return values;
		}

	public double[][] getValuesCopy()
		{
		int rows = rowCount();
		int cols = columnCount();
		double[][] C = new double[rows][cols];
		for(int i = 0; i < rows; i++)
			{
			for(int j = 0; j < cols; j++)
				{
				C[i][j] = this.values[i][j];
				}
			}
		return C;
		}

	public double[][] valuesClone()
		{
		//Create similar structure
		double[][] tabCopie = new double[values.length][];
		for(int i = 1; i <= values.length; i++)
			{
			int mi = values[i - 1].length;
			tabCopie[i - 1] = new double[mi];
			}

		//fill the structure
		for(int i = 1; i <= values.length; i++)
			{
			int mi = values[i - 1].length;
			for(int j = 1; j <= mi; j++)
				{
				tabCopie[i - 1][j - 1] = values[i - 1][j - 1];
				}
			}
		return tabCopie;
		}

	public String getOperation(int step)
		{
		return tabOperations[step];
		}

	public double[][] getStep(int step)
		{
		return hist[step];
		}

	public int getHistLength()
		{
		return histLength;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/



	/*------------------------------------------------------------------*\
	|*							Methode Private	Static					*|
	\*------------------------------------------------------------------*/

	private void fillVariableNameStructures()
		{
		boolean isVariableCount = false;
		int tokenStartIndex = 97;
//		//Fill the list of name for the variable.
//		if (variableName.equals("a"))
//			{
//			tokenStartIndex = 97; //a,b,c
//			}
//		else if (variableName.equals("x"))
//			{
//			tokenStartIndex = 120; //x,y,z
//			}
//		else
//			{
//			tokenStartIndex = 120; //x1,x2,x3
//			isVariableCount = true;
//			}
		for(int i = 0; i < columnCount(); ++i)
			{
			if (isVariableCount)
				{
				mapIndexVariableName.put(i, String.valueOf(Character.toChars(tokenStartIndex))+(i+1));
				listVariableName.add(String.valueOf(Character.toChars(tokenStartIndex))+(i+1));
				}
			else
				{
				mapIndexVariableName.put(i, String.valueOf(Character.toChars(tokenStartIndex + i)));
				listVariableName.add(String.valueOf(Character.toChars(tokenStartIndex + i)));
				}
			}
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// The values of the matrix stored in row-major order, with each element initially null
	private double[][] values;
	private int currentStep;
	private double[][][] hist;
	private String[] tabOperations;
	private int histLength;
	private String variableName;
	Map<Integer, String> mapIndexVariableName;//avec variables dépendantes
	List<String> listVariableName; //sans variable dépendante
	}
