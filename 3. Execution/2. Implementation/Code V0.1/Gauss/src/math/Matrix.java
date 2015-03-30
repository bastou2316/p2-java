
package math;

import tools.MathTools;

public final class Matrix
	{

	// The values of the matrix stored in row-major order, with each element initially null
	private double[][] values;
	private int step;

	/**
	 * Constructs a blank matrix with the specified number of rows and columns. All the elements are initially {@code null}.
	 * @param rows the number of rows in this matrix
	 * @param cols the number of columns in this matrix
	 * @throws IllegalArgumentException if {@code rows} &le; 0 or {@code cols} &le; 0
	 * @throws NullPointerException if {@code f} is {@code null}
	 */
	public Matrix(int rows, int cols)
		{
		if (rows <= 0 || cols <= 0) { throw new IllegalArgumentException("Invalid number of rows or columns"); }

		values = new double[rows][cols];
		step = 0;
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
	 * Check if two double are equals.
	 * @param a the first double
	 * @param b the second double
	 * @return true if a and b are equals.
	 */
	public boolean isEqual(double a, double b)
		{
		return MathTools.isEquals(a, b);
		}

	/**
	 * Converts this matrix to reduced row echelon form (RREF) using Gauss-Jordan elimination.
	 */
	public void reducedRowEchelonForm()
		{
		int rows = rowCount();
		int cols = columnCount();

		// Compute row echelon form (REF)
		int numPivots = 0;
		for(int j = 0; j < cols; j++)
			{ // For each column
			// Find a pivot row for this column
			int pivotRow = numPivots;
			while(pivotRow < rows && isEqual(get(pivotRow, j), 0))
				{
				pivotRow++;
				}
			if (pivotRow == rows)
				{
				continue; // Cannot eliminate on this column
				}
			swapRows(numPivots, pivotRow);
			pivotRow = numPivots;
			numPivots++;

			// Simplify the pivot row using the reciprocal
			multiplyRow(pivotRow, 1 / get(pivotRow, j));

			// Eliminate rows below by substraction
			for(int i = pivotRow + 1; i < rows; i++)
				{
				addRows(pivotRow, i, -get(i, j));
				}
			}

		// Compute reduced row echelon form (RREF)
		for(int i = rows - 1; i >= 0; i--)
			{
			// Find pivot
			int pivotCol = 0;
			while(pivotCol < cols && isEqual(get(i, pivotCol), 0))
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
				addRows(i, j, -get(j, pivotCol));
				}
			}
		}

	public void solveNextStep()
		{
		//Modification de la matrice jusqua la prochaine etape
		++step;
		//Todo
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

		for(int i = 0; i < rows; i++)
			{
			for(int j = 0; j < cols; j++)
				{
				System.out.print(get(i, j));
				System.out.print("\t");
				}
			System.out.println();
			}
		System.out.println();
		}
	}
