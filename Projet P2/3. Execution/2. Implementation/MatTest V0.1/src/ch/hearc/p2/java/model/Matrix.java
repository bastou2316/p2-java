
package ch.hearc.p2.java.model;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

import ch.hearc.p2.java.tools.MathTools;

public final class Matrix
	{

	// The values of the matrix stored in row-major order, with each element initially null
	private double[][] values;
	private int currentStep;
	private double[][][] hist;
	private String[] tabOperations;
	private int histLength;

	/**
	 * Construit une matrice vide avec le nombre de ligne et de colonne sp�cifi�.
	 * @param rows le nombre de ligne
	 * @param cols le nombre de colonne
	 * @throws IllegalArgumentException if {@code rows} &le; 0 or {@code cols} &le; 0
	 * @throws NullPointerException if {@code f} is {@code null}
	 */
	public Matrix(int rows, int cols)
		{
		if (rows <= 0 || cols <= 0) { throw new IllegalArgumentException("Invalid number of rows or columns"); }

		values = new double[rows][cols];
		currentStep = 0;
		hist = new double[50][rows][cols];
		tabOperations = new String[50];
		histLength = 0;
		}

	/** Construct a matrix quickly without checking arguments.
	   @param A    Two-dimensional array of doubles.
	   @param m    Number of rows.
	   @param n    Number of colums.
	   */

	public Matrix(double[][] values)
		{
		this.values = values;
		currentStep = 0;
		hist = new double[20][values.length][values[0].length];
		tabOperations = new String[20];
		histLength = 0;
		}

	/**
	 * Returns the number of rows in this matrix, which is positive.
	 * @return the number of rows in this matrix
	 */
	public int rowCount()
		{
		return values.length;
		}

	/**
	 * Returns the number of columns in this matrix, which is positive.
	 * @return the number of columns in this matrix
	 */
	public int columnCount()
		{
		return values[0].length;
		}

	/**
	 * Returns the element at the specified location in this matrix.
	 * @param row the row to read from (0-based indexing)
	 * @param col the column to read from (0-based indexing)
	 * @return the element at the specified location in this matrix
	 * @throws IndexOutOfBoundsException if the specified row or column exceeds the bounds of the matrix
	 */
	public double get(int row, int col)
		{
		if (row < 0 || row >= values.length || col < 0 || col >= values[row].length) { throw new IndexOutOfBoundsException("Row or column index out of bounds"); }
		return values[row][col];
		}

	/** Get a submatrix.
	   @param i0   Initial row index
	   @param i1   Final row index
	   @param j0   Initial column index
	   @param j1   Final column index
	   @return     A(i0:i1,j0:j1)
	   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	   */

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

	/** Access the internal two-dimensional array.
	   @return     Pointer to the two-dimensional array of matrix elements.
	   */
	public double[][] getValues()
		{
		return values;
		}

	/** Copy the internal two-dimensional array.
	@return     Two-dimensional array copy of matrix elements.
	*/

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

	/**
	 * Stores the specified element at the specified location in this matrix.
	 * @param row the row to write to (0-based indexing)
	 * @param col the column to write to (0-based indexing)
	 * @param val the element value to write
	 * @throws IndexOutOfBoundsException if the specified row or column exceeds the bounds of the matrix
	 */
	public void set(int row, int col, double val)
		{
		if (row < 0 || row >= values.length || col < 0 || col >= values[0].length) { throw new IndexOutOfBoundsException("Row or column index out of bounds"); }
		values[row][col] = val;
		}

	/**
	 * Returns a clone of this matrix. The field and underlying values are shallow-copied because they are assumed to be immutable.
	 * @return a clone of this matrix
	 */

	public Matrix cloneOf()
		{
		return new Matrix(this);
		}

	/**
	 * Construct a copy of the specified matrix.
	 * @param matrix to copy
	 */
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

	/**
	 * Swaps the two specified rows of this matrix.
	 * @param row0 one row to swap (0-based indexing)
	 * @param row1 the other row to swap (0-based indexing)
	 * @throws IndexOutOfBoundsException if a specified row exceeds the bounds of the matrix
	 */
	public void swapRows(int row0, int row1)
		{
		if (row0 < 0 || row0 >= values.length || row1 < 0 || row1 >= values.length) { throw new IndexOutOfBoundsException("Row index out of bounds"); }
		double[] temp = values[row0];
		values[row0] = values[row1];
		values[row1] = temp;
		}

	/**
	 * Multiplies the specified row in this matrix by the specified factor. In other words, row *= factor.
	 * @param row the row index to operate on (0-based indexing)
	 * @param factor the factor to multiply by
	 * @throws IndexOutOfBoundsException if the specified row exceeds the bounds of the matrix
	 */
	public void multiplyRow(int row, double factor)
		{
		for(int j = 0, cols = columnCount(); j < cols; j++)
			{
			set(row, j, get(row, j) * factor);
			}
		}

	/**
	 * Adds the first specified row in this matrix multiplied by the specified factor to the second specified row.
	 * In other words, destRow += srcRow * factor.
	 * @param srcRow the index of the row to read and multiply (0-based indexing)
	 * @param destRow the index of the row to accumulate to (0-based indexing)
	 * @param factor the factor to multiply by
	 * @throws IndexOutOfBoundsException if a specified row exceeds the bounds of the matrix
	 */
	public void addRows(int srcRow, int destRow, double factor)
		{
		for(int j = 0, cols = columnCount(); j < cols; j++)
			{
			set(destRow, j, get(destRow, j) + get(srcRow, j) * factor);
			}
		}

	/**
	 * Echelonne et r�duit la matrice.
	 */
	public void reducedRowEchelonForm()
		{
		double factorBackup;
		StringBuilder stringBuilder = new StringBuilder();
		int rows = rowCount();
		int cols = columnCount();
		currentStep = 0;
		hist[currentStep] = valuesClone();
		currentStep++;
		DecimalFormat formatter = new DecimalFormat("0.##");

		// Compute row echelon form (REF)
		int numPivots = 0;
		for(int j = 0; j < cols; j++)
			{ // For each column
				// Find a pivot row for this column
			int pivotRow = numPivots;
			while(pivotRow < rows && MathTools.isEquals(get(pivotRow, j), 0))
				{
				pivotRow++;
				}
			if (pivotRow == rows)
				{
				continue; // Cannot eliminate on this column
				}
			swapRows(numPivots, pivotRow);

			if (!isEqual(hist[currentStep - 1], values))
				{
				stringBuilder.append("L");
				stringBuilder.append(numPivots + 1);
				stringBuilder.append(" <=> L");
				stringBuilder.append(pivotRow + 1);
				tabOperations[currentStep] = stringBuilder.toString();
				stringBuilder = new StringBuilder();
				hist[currentStep] = valuesClone();
				currentStep++;
				}
			pivotRow = numPivots;
			numPivots++;
			// Simplify the pivot row using the reciprocal
			factorBackup = 1 / get(pivotRow, j);
			multiplyRow(pivotRow, 1 / get(pivotRow, j));
			if (!isEqual(hist[currentStep - 1], values))
				{
				stringBuilder.append("L");
				stringBuilder.append(pivotRow + 1);
				stringBuilder.append(" = L");
				stringBuilder.append(pivotRow + 1);
				if (factorBackup >= 1)
					{
					stringBuilder.append(" * ");
					stringBuilder.append(formatter.format(factorBackup));
					}
				else
					{
					stringBuilder.append(" / ");
					stringBuilder.append(formatter.format(1 / factorBackup));
					}
				tabOperations[currentStep] = stringBuilder.toString();
				stringBuilder = new StringBuilder();
				hist[currentStep] = valuesClone();
				currentStep++;
				}
			// Eliminate rows below by substraction
			for(int i = pivotRow + 1; i < rows; i++)
				{
				factorBackup = -get(i, j);
				addRows(pivotRow, i, -get(i, j));
				if (!isEqual(hist[currentStep - 1], values))
					{
					stringBuilder.append("L");
					stringBuilder.append(i + 1);
					stringBuilder.append(" = L");
					stringBuilder.append(i + 1);
					if (factorBackup > 0)
						{
						stringBuilder.append(" + ");
						if (!MathTools.isEquals(factorBackup, 1))
							{
							stringBuilder.append(formatter.format(factorBackup));
							}
						stringBuilder.append("L");
						stringBuilder.append(pivotRow + 1);
						}
					else
						{
						stringBuilder.append(" - ");
						if (!MathTools.isEquals(factorBackup, -1))
							{
							stringBuilder.append(formatter.format(-factorBackup));
							}
						stringBuilder.append("L");
						stringBuilder.append(pivotRow + 1);
						}
					tabOperations[currentStep] = stringBuilder.toString();
					stringBuilder = new StringBuilder();
					hist[currentStep] = valuesClone();
					currentStep++;
					}
				}
			}
		if (hasSolution())
			{

			// Compute reduced row echelon form (RREF)
			for(int i = rows - 1; i >= 0; i--)
				{
				// Find pivot
				int pivotCol = 0;
				while(pivotCol < cols && MathTools.isEquals(get(i, pivotCol), 0))
					{
					pivotCol++;
					}
				if (pivotCol == cols)
					{
					continue; // Skip this all-zero row
					}

				// Eliminate rows above
				for(int j = i - 1; j >= 0; j--)
					{
					factorBackup = -get(j, pivotCol);
					addRows(i, j, -get(j, pivotCol));
					if (!isEqual(hist[currentStep - 1], values))
						{
						stringBuilder.append("L");
						stringBuilder.append(j + 1);
						stringBuilder.append(" = L");
						stringBuilder.append(j + 1);
						if (factorBackup > 0)
							{
							stringBuilder.append(" + ");
							if (!MathTools.isEquals(factorBackup, 1))
								{
								stringBuilder.append(formatter.format(factorBackup));
								}
							stringBuilder.append("L");
							stringBuilder.append(j + 1);
							}
						else
							{
							stringBuilder.append(" - ");
							if (!MathTools.isEquals(factorBackup, -1))
								{
								stringBuilder.append(formatter.format(-factorBackup));
								}
							stringBuilder.append("L");
							stringBuilder.append(j + 1);
							}
						tabOperations[currentStep] = stringBuilder.toString();
						stringBuilder = new StringBuilder();
						hist[currentStep] = valuesClone();
						currentStep++;
						}
					}
				}

			histLength = currentStep + 1;
			currentStep = 0;
			}
		}

	public String stepToString(int step)
		{
		StringBuilder builder = new StringBuilder();
		int rows = rowCount();
		int cols = columnCount();
		double[][] tabStep = getStep(step);
		for(int i = 0; i < rows; i++)
			{
			for(int j = 0; j < cols; j++)
				{
				builder.append(tabStep[i][j]);
				builder.append("  ");
				}
			builder.append(System.getProperty("line.separator"));
			}
		builder.append(System.getProperty("line.separator"));
		return builder.toString();
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

	@Override
	public String toString()
		{
		StringBuilder builder = new StringBuilder();
		int rows = rowCount();
		int cols = columnCount();

		for(int i = 0; i < rows; i++)
			{
			for(int j = 0; j < cols; j++)
				{
				builder.append(get(i, j));
				builder.append("  ");
				}
			builder.append(System.getProperty("line.separator"));
			}
		builder.append(System.getProperty("line.separator"));
		return builder.toString();
		}

	/**
	 * displays the matrix in the console
	 */
	public void print()
		{
		int rows = rowCount();
		int cols = columnCount();
		DecimalFormat formatter = new DecimalFormat("0.##");
		for(int i = 0; i < rows; i++)
			{
			for(int j = 0; j < cols; j++)
				{
				System.out.print(formatter.format(get(i, j)));
				System.out.print("\t");
				}
			System.out.println();
			}
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

	//Format the resulting matrix
	public String showResult()
		{
		StringBuilder stringBuilder = new StringBuilder();
		int rows = rowCount();
		int cols = columnCount() - 1;
		Map<Integer, String> mapIndexVariableName = new TreeMap<Integer, String>();
		int currentCol;
		int currentRow = 0;
		int currentVariableCol;
		boolean isFirst;
		DecimalFormat formatter = new DecimalFormat("0.##");

		int tokenStartIndex = 97;

		if (hasSolution())
			{
			findDependentVariables(mapIndexVariableName, tokenStartIndex);
			//System.out.println(mapIndexVariableName);
			//vulgarisation de la matrice

			for(currentVariableCol = 0; currentVariableCol < cols; ++currentVariableCol)//commence en haut de la matrice
				{
				stringBuilder.append((char)(tokenStartIndex + currentVariableCol));
				stringBuilder.append(" = ");
				isFirst = true; //permet de savoir si le prochain nombre affich� n�c�ssite l'ajout du caract�re '+';
				if (mapIndexVariableName.get(currentVariableCol).equals(Integer.toString(currentVariableCol + tokenStartIndex))) //si c'est une collonne pivot...
					{
					for(int i = 0; i < rows; ++i)
						{
						if (MathTools.isEquals(values[i][currentVariableCol], 1))
							{
							currentRow = i;
							}
						}
					for(currentCol = currentRow + 1; currentCol < cols; ++currentCol) //parcours la ligne de la matrice augment�e depuis le pivot jusqu'� la fin
						{
						//soustraction des variables d�pendante � la variable ind�pendante actuelle.
						if (values[currentRow][currentCol] > 0)
							{
							if (isFirst == false)
								{
								stringBuilder.append(" ");
								}
							stringBuilder.append(formatter.format(-values[currentRow][currentCol]));
							stringBuilder.append(mapIndexVariableName.get(currentCol));
							isFirst = false;
							}
						else if (values[currentRow][currentCol] < 0)
							{
							if (isFirst == false)
								{
								stringBuilder.append(" + ");
								}
							if (!MathTools.isEquals(values[currentRow][currentCol], -1))
								{
								stringBuilder.append(-(int)values[currentRow][currentCol]);
								}
							stringBuilder.append(mapIndexVariableName.get(currentCol));
							isFirst = false;
							}
						}
					//addition de la valeur du vecteur
					if ((int)values[currentRow][cols] > 0)
						{
						if (isFirst == false)
							{
							stringBuilder.append(" +");
							}
						stringBuilder.append(formatter.format((int)values[currentRow][cols]));
						}
					else if (values[currentRow][cols] < 0)
						{
						if (isFirst == false)
							{
							stringBuilder.append(" ");
							}
						stringBuilder.append(formatter.format(values[currentRow][cols]));
						}
					if (MathTools.isEquals(values[currentRow][cols], 0) && isFirst)
						{
						stringBuilder.append('0');
						}
					}
				else
					//si c'est pas une collone pivot...
					{
					stringBuilder.append(mapIndexVariableName.get(currentVariableCol));
					}
				stringBuilder.append("\n");
				}
			}
		else
			{
			stringBuilder.append("Le syst�me n'a pas de solution.\n");
			}
		return stringBuilder.toString();
		}

	private void findDependentVariables(Map<Integer, String> mapIndexVariableName, int tokenStartIndex)
		{
		int freeVariableNumber = 0;
		boolean isOne;
		boolean isPivot;
		int rows = rowCount();
		int cols = columnCount() - 1;
		int currentCol;
		int currentRow;

		//Fill the list of name for the variable. For exemple a,b,c
		for(int i = 0; i < cols; ++i)
			{
			mapIndexVariableName.put(i, Integer.toString(tokenStartIndex + i));
			}

		//Recherche des variables d�pendantes
		for(currentCol = cols - 1; currentCol > 0; --currentCol)//commence � droite de la matrice. Parcours les colonnes.
			{
			isOne = false;
			isPivot = false;
			for(currentRow = rows - 1; currentRow > 0; --currentRow)//parcours les lignes
				{
				if ((int)values[currentRow][currentCol] == 1)
					{
					//v�rifie qu'il y ait un pivot dans la colonne
					if (isOne == false)
						{
						isOne = true;
						isPivot = true;
						}
					//si il y deux pivots alors ce n'est pas une colonne pivot.
					else
						{
						isPivot = false;
						break;
						}
					}
				}
			//v�rifie si il n'y a pas de pivot � gauche du pivot candidat
			if (isPivot == true)
				{
				for(int i = currentCol; i > 0; --i)
					{
					if ((int)values[currentRow][i] != 0)
						{
						isPivot = false;
						}
					}
				}
			if (isPivot == false)
				{
				mapIndexVariableName.replace(currentCol, "u" + freeVariableNumber);
				freeVariableNumber++;
				}

			}
		}

	//Affiche les op�rations dans la console pour le d�bug
	public void showOperations()
		{
		new Matrix(hist[0]).print();
		System.out.println();
		for(int i = 1; i < histLength - 1; ++i)
			{
			System.out.println(tabOperations[i]);
			System.out.println();
			new Matrix(hist[i]).print();
			System.out.println();
			}

		}

	//return vrai si le syt�me est consistant
	private boolean hasSolution()
		{
		int zeroCount = 0;
		for(int i = 0; i < rowCount(); ++i)// pour chaque ligne

			{
			if (!MathTools.isEquals(values[i][columnCount() - 1], 0))//Si la valeur du vecteur b � la ligne actuelle est non-null
				{
				zeroCount = 0;
				for(int j = 0; j < columnCount() - 1; ++j) //parcours la ligne de la matrice
					{
					if (!MathTools.isEquals(values[i][j], 0)) //si on trouve une valeur non-null alors la ligne est consistante.
						{
						break;
						}
					zeroCount++;
					}
				if (zeroCount == columnCount() - 1) { return false; } //si la ligne ne contient pas de valeur non-nul, la ligne est inconsistante.
				}
			}
		return true;
		}
	}
