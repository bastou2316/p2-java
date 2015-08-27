
package ch.hearc.p2.java.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.hearc.p2.java.tools.MathTools;

public class Log implements Serializable
	{

	private static final long serialVersionUID = 6447407441073945152L;
	
	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/
	public Log(Matrix matrix, boolean isGauss)
		{
		mapCoeficient = new HashMap<String, Matrix>();
		listTabMatrix = new ArrayList<String[][]>();
		listMatrix = new ArrayList<Matrix>();
		listMatrix.add(matrix);
		rows = matrix.rowCount();
		cols = matrix.columnCount();
		listOperation = new ArrayList<String>();
		if (isGauss)
			{
			reducedRowEchelonForm(); //fill listOperation
			serializeMatrix(); //fill listMatrix
			}
		else
			{
			listMatrix.add(new QRDecomposition(matrix.getMatrix(0, matrix.rowCount() - 1, 0, matrix.columnCount() - 2)).solve(matrix.getMatrix(0, matrix.rowCount() - 1, matrix.columnCount() - 1, matrix.columnCount() - 1)));
			}
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	public Matrix getOriginalMatrix()
		{
		return listMatrix.get(0);
		}

	public String[][] getMatrix(int step)
		{
		return listTabMatrix.get(step);
		}

	public List<String> getListOperation()
		{
		return listOperation;
		}

	public int getNbStep()
		{
		return listMatrix.size();
		}

	public int getRows()
		{
		return this.rows;
		}

	public int getCols()
		{
		return this.cols;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	/**
	 * Echelonne et réduit la matrice.
	 */
	private void reducedRowEchelonForm()
		{
		Matrix matrix = listMatrix.get(0).cloneOf();
		double factorBackup;
		StringBuilder stringBuilder = new StringBuilder();
		DecimalFormat formatter = new DecimalFormat("0.##");
		// Compute row echelon form (REF)
		int numPivots = 0;
		for(int j = 0; j < cols; j++)
			{ // For each column
				// Find a pivot row for this column
			int pivotRow = numPivots;
			while(pivotRow < rows && MathTools.isEquals(matrix.get(pivotRow, j), 0))
				{
				pivotRow++;
				}
			if (pivotRow == rows)
				{
				continue; // Cannot eliminate on this column
				}
			matrix.swapRows(numPivots, pivotRow);
			if (!isEqual(listMatrix.get(listMatrix.size() - 1), matrix))
				{
				stringBuilder.append("L");
				stringBuilder.append(numPivots + 1);
				stringBuilder.append(" <=> L");
				stringBuilder.append(pivotRow + 1);
				listOperation.add(stringBuilder.toString());
				stringBuilder = new StringBuilder();
				listMatrix.add(matrix.cloneOf());
				}
			pivotRow = numPivots;
			numPivots++;
			// Simplify the pivot row using the reciprocal
			factorBackup = 1 / matrix.get(pivotRow, j);
			matrix.multiplyRow(pivotRow, 1 / matrix.get(pivotRow, j));
			if (!isEqual(listMatrix.get(listMatrix.size() - 1), matrix))
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
				listOperation.add(stringBuilder.toString());
				stringBuilder = new StringBuilder();
				listMatrix.add(matrix.cloneOf());
				}
			// Eliminate rows below by substraction
			for(int i = pivotRow + 1; i < rows; i++)
				{
				factorBackup = -matrix.get(i, j);
				matrix.addRows(pivotRow, i, -matrix.get(i, j));
				if (!isEqual(listMatrix.get(listMatrix.size() - 1), matrix))
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
					listOperation.add(stringBuilder.toString());
					stringBuilder = new StringBuilder();
					listMatrix.add(matrix.cloneOf());
					}
				}
			}
		if (hasSolution(matrix))
			{

			// Compute reduced row echelon form (RREF)
			for(int i = rows - 1; i >= 0; i--)
				{
				// Find pivot
				int pivotCol = 0;
				while(pivotCol < cols && MathTools.isEquals(matrix.get(i, pivotCol), 0))
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
					factorBackup = -matrix.get(j, pivotCol);
					matrix.addRows(i, j, -matrix.get(j, pivotCol));
					if (!isEqual(listMatrix.get(listMatrix.size() - 1), matrix))
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
						listOperation.add(stringBuilder.toString());
						stringBuilder = new StringBuilder();
						listMatrix.add(matrix.cloneOf());
						}
					}
				}
			//TODO
			if (hasOneSolution(matrix))
				{

				}
			}
		else
			//if the solution has no solution
			{
			System.out.println("Log: the equation has no solution");
			}
		//substitution arrière algébrique

		}

	private boolean isEqual(Matrix matrix1, Matrix matrix2)
		{
		//If array shape is equals, then check the elements inside it
		for(int i = 0; i < matrix1.rowCount(); i++)
			{
			for(int j = 0; j < matrix2.columnCount(); j++)
				{
				if (!MathTools.isEquals(matrix1.get(i, j), matrix2.get(i, j))) { return false; }
				}
			}
		//If each test has passed, then the arrays are equals
		return true;
		}

	private void serializeMatrix()
		{
		StringBuilder stringBuilder;
		Matrix matrix;
		DecimalFormat formatter = new DecimalFormat("0.##");
		for(int step = 0; step < listMatrix.size(); step++)
			{
			listTabMatrix.add(new String[rows][cols]);
			matrix = listMatrix.get(step);
			for(int i = 0; i < rows; i++)
				{
				for(int j = 0; j < cols; j++)
					{
					stringBuilder = new StringBuilder();
					stringBuilder.append(formatter.format(matrix.get(i, j)));
					int k = 0;
					while(k != mapCoeficient.size())
						{
						k++;
						double coeficient = mapCoeficient.get("u" + k).get(i, j);
						if (!MathTools.isEquals(coeficient, 0))
							{
							if (coeficient > 0)
								{
								stringBuilder.append("+");
								stringBuilder.append(formatter.format(coeficient));
								stringBuilder.append("u" + k);
								}
							else
								{
								stringBuilder.append(formatter.format(coeficient));
								stringBuilder.append("u" + k);
								}
							}
						}
					listTabMatrix.get(step)[i][j] = stringBuilder.toString();
					}
				}
			}
		}

	private void findDependentVariables()
		{
		Matrix matrix = getOriginalMatrix();
		int freeVariableNumber = 0;
		boolean isOne;
		boolean isPivot;
		int currentCol;
		int currentRow, pivotRow;

		//Recherche des variables dépendantes
		for(currentCol = cols - 1; currentCol >= 0; --currentCol)//commence à droite de la matrice. Parcours les colonnes.
			{
			isOne = false;
			isPivot = false;
			pivotRow = 0;
			for(currentRow = rows - 1; currentRow >= 0; --currentRow)//parcours les lignes
				{
				if (MathTools.isEquals(matrix.get(currentRow, currentCol), 1))
					{
					//vérifie qu'il y ait un pivot dans la colonne
					if (isOne == false)
						{
						isOne = true;
						isPivot = true;
						pivotRow = currentRow;
						}
					//si il y deux pivots alors ce n'est pas une colonne pivot.
					else
						{
						isPivot = false;
						break;
						}
					}
				}
			//vérifie si il n'y a pas de pivot à gauche du pivot candidat
			if (isPivot == true)
				{
				for(int i = currentCol - 1; i > 0; --i)
					{
					if (!MathTools.isEquals(matrix.get(pivotRow, i), 0))
						{
						isPivot = false;
						}
					}
				}
			//traitement de la variable libre
			if (isPivot == false)
				{
				//listMatrix
				//freeVariableNumber++;
				}

			}
		}

	private boolean hasOneSolution(Matrix matrix)
		{
		if (rows != cols)
			{

			}
		for(int i = 0; i < rows; i++)
			{
			if (!MathTools.isEquals(matrix.get(i, i), 1)) { return false; }
			}
		return true;
		}

	//return vrai si le sytème est consistant
	private boolean hasSolution(Matrix matrix)
		{
		int zeroCount = 0;
		for(int i = 0; i < rows; ++i)// pour chaque ligne

			{
			if (!MathTools.isEquals(matrix.get(i, cols - 1), 0))//Si la valeur du vecteur b à la ligne actuelle est non-null
				{
				zeroCount = 0;
				for(int j = 0; j < cols - 1; ++j) //parcours la ligne de la matrice
					{
					if (!MathTools.isEquals(matrix.get(i, j), 0)) //si on trouve une valeur non-null alors la ligne est consistante.
						{
						break;
						}
					zeroCount++;
					}
				if (zeroCount == cols - 1) { return false; } //si la ligne ne contient pas de valeur non-nul, la ligne est inconsistante.
				}
			}
		return true;
		}

	//	//Format the resulting matrix
	//	public String showResult()
	//		{
	//		StringBuilder stringBuilder = new StringBuilder();
	//		int rows = rowCount();
	//		int cols = columnCount() - 1;
	//		int currentCol;
	//		int currentRow = 0;
	//		int currentVariableCol;
	//		boolean isFirst;
	//		DecimalFormat formatter = new DecimalFormat("0.##");
	//
	//		if (hasSolution())
	//			{
	//			findDependentVariables();
	//
	//			//vulgarisation de la matrice
	//			for(currentVariableCol = 0; currentVariableCol < cols; ++currentVariableCol)//commence en haut de la matrice
	//				{
	//				stringBuilder.append(listVariableName.get(currentVariableCol));
	//				stringBuilder.append(" = ");
	//				isFirst = true; //permet de savoir si le prochain nombre affiché nécéssite l'ajout du caractère '+';
	//				if (mapIndexVariableName.get(currentVariableCol).equals(listVariableName.get(currentVariableCol))) //si c'est une collonne pivot...
	//					{
	//					for(int i = 0; i < rows; ++i)
	//						{
	//						if (MathTools.isEquals(values[i][currentVariableCol], 1))
	//							{
	//							currentRow = i;
	//							}
	//						}
	//					for(currentCol = currentVariableCol + 1; currentCol < cols; ++currentCol) //parcours la ligne de la matrice augmentée depuis le pivot jusqu'à la fin
	//						{
	//						//soustraction des variables dépendante à la variable indépendante actuelle.
	//						if (values[currentRow][currentCol] > 0)
	//							{
	//							if (isFirst == false)
	//								{
	//								stringBuilder.append(" ");
	//								}
	//							stringBuilder.append(formatter.format(-values[currentRow][currentCol]));
	//							stringBuilder.append(mapIndexVariableName.get(currentCol));
	//							isFirst = false;
	//							}
	//						else if (values[currentRow][currentCol] < 0)
	//							{
	//							if (isFirst == false)
	//								{
	//								stringBuilder.append(" + ");
	//								}
	//							if (!MathTools.isEquals(values[currentRow][currentCol], -1))
	//								{
	//								stringBuilder.append(formatter.format(-values[currentRow][currentCol]));
	//								}
	//							stringBuilder.append(mapIndexVariableName.get(currentCol));
	//							isFirst = false;
	//							}
	//						}
	//					//addition de la valeur du vecteur
	//					if (values[currentRow][cols] > 0)
	//						{
	//						if (isFirst == false)
	//							{
	//							stringBuilder.append(" +");
	//							}
	//						stringBuilder.append(formatter.format(values[currentRow][cols]));
	//						}
	//					else if (values[currentRow][cols] < 0)
	//						{
	//						if (isFirst == false)
	//							{
	//							stringBuilder.append(" ");
	//							}
	//						stringBuilder.append(formatter.format(values[currentRow][cols]));
	//						}
	//
	//					if (MathTools.isEquals(values[currentRow][cols], 0) && isFirst)
	//						{
	//						stringBuilder.append('0');
	//						}
	//					}
	//				else
	//					//si c'est pas une collone pivot...
	//					{
	//					stringBuilder.append(mapIndexVariableName.get(currentVariableCol));
	//					}
	//				stringBuilder.append("\n");
	//				}
	//			}
	//		else
	//			{
	//			stringBuilder.append("Le système n'a pas de solution.\n");
	//			}
	//		return stringBuilder.toString();
	//		}
	//		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	//output
	private List<String[][]> listTabMatrix;
	private List<String> listOperation;

	// tools
	private List<Matrix> listMatrix;
	private Map<String, Matrix> mapCoeficient;
	private int rows;
	private int cols;

	}
