
package ch.hearc.p2.java.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.hearc.p2.java.tools.MathTools;
import ch.hearc.p2.java.view.IndependentVar;

public class Log implements Serializable
	{

	private static final long serialVersionUID = 6447407441073945152L;

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/
	public Log(Matrix matrix, boolean isGauss, int varStyle)
		{
		this.varStyle = varStyle;
		this.matrix = matrix.cloneOf();
		listMapNameToCoeficient = new ArrayList<Map<String, Matrix>>();
		listMapNameToCoeficient.add(new HashMap<String, Matrix>());
		listTabMatrix = new ArrayList<String[][]>();
		listMatrix = new ArrayList<Matrix>();
		listMatrix.add(matrix);
		rows = matrix.rowCount();
		cols = matrix.columnCount();
		listOperation = new ArrayList<String>();
		listOperation.add("Origine");

		if (isGauss)
			{
			reducedRowEchelonForm(); //fill listOperation
			listOperation.add("Résultat final");
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

	public String getSolution()
		{
		return this.solution;
		}

	public double[][] getParametricEquations()
		{
		double[][] tabCoeficient = null;
		if (solution.equals("Ce système d'équations possède une solution unique."))
			{
			tabCoeficient = new double[cols - 1][1];
			for(int i = 0; i < cols - 1; ++i)
				{
				tabCoeficient[i][0] = listMatrix.get(listMatrix.size() - 1).get(i, cols - 1);
				}
			}
		else if (solution.equals("Ce système d'équations possède une infinité de solutions."))
			{

			tabCoeficient = new double[cols - 1][cols - 1];
			for(int i = 0; i < cols - 1; ++i)
				{
				tabCoeficient[i][0] = listMatrix.get(listMatrix.size() - 1).get(i, cols - 1);
				for(int j = 1; j < cols - 1; ++j)
					{
					if (listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 1).containsKey(("u" + j)))
						{
						tabCoeficient[i][j] = listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 1).get("u" + j).get(i, cols - 1);
						}
					}
				}
			}
		return tabCoeficient;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	/**
	 * Echelonne et réduit la matrice.
	 */
	private void reducedRowEchelonForm()
		{
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
				updateListMapNameToCoefiecient();
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
				updateListMapNameToCoefiecient();
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
					updateListMapNameToCoefiecient();
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
							stringBuilder.append(pivotCol + 1);
							}
						else
							{
							stringBuilder.append(" - ");
							if (!MathTools.isEquals(factorBackup, -1))
								{
								stringBuilder.append(formatter.format(-factorBackup));
								}
							stringBuilder.append("L");
							stringBuilder.append(pivotCol + 1);
							}
						listOperation.add(stringBuilder.toString());
						stringBuilder = new StringBuilder();
						listMatrix.add(matrix.cloneOf());
						updateListMapNameToCoefiecient();
						}
					}
				}

			if (findDependentVariables())
				{
				algebricBackSubstitution();
				solution = "Ce système d'équations possède une infinité de solutions.";
				}
			else
				{
				solution = "Ce système d'équations possède une solution unique.";
				}
			}
		else
			{
			solution = "Ce système d'équations n'a pas de solution réelle.";
			}
		}

	private void algebricBackSubstitution()
		{
		//substitution arrière algébrique
		double factorBackup;
		StringBuilder stringBuilder = new StringBuilder();
		DecimalFormat formatter = new DecimalFormat("0.##");
		// Compute reduced row echelon form (RREF)
		for(int i = cols - 2; i >= 0; i--)
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
				{
				for(int j = i - 1; j >= 0; j--)
					{
					factorBackup = -matrix.get(j, pivotCol);
					matrix.addRows(i, j, -matrix.get(j, pivotCol));

					if (!isEqual(listMatrix.get(listMatrix.size() - 1), matrix))
						{
						updateListMapNameToCoefiecient();
						addRowsCoeficientMatrix(i, j, factorBackup);
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
							stringBuilder.append(pivotCol + 1);
							}
						else
							{
							stringBuilder.append(" - ");
							if (!MathTools.isEquals(factorBackup, -1))
								{
								stringBuilder.append(formatter.format(-factorBackup));
								}
							stringBuilder.append("L");
							stringBuilder.append(pivotCol + 1);
							}
						listOperation.add(stringBuilder.toString());
						stringBuilder = new StringBuilder();
						listMatrix.add(matrix.cloneOf());
						}
					}
				}
			}
		}

	private boolean findDependentVariables()
		{
		int freeVariableIndex = 1;
		boolean isPivotFound = false;
		int currentCol = 0;
		int currentRow;
		StringBuilder stringBuilder = new StringBuilder();
		Boolean[] flagPivot = new Boolean[cols - 1];//cols-1 because we don't look at the augmented part of the matrix
		String[] tabVarStyle = IndependentVar.getTabVar(cols - 1, varStyle);

		//rempli le tableau de boolean
		for(int i = 0; i < cols - 1; i++)
			{
			flagPivot[i] = false;
			}

		//Recherche des variables dépendantes
		for(currentRow = 0; currentRow < rows; ++currentRow)//parcours les lignes
			{
			isPivotFound = false;
			currentCol = 0;
			do//Parcours les colonnes
				{
				//vérifie qu'il y ait un pivot dans la colonne
				if (MathTools.isEquals(matrix.get(currentRow, currentCol), 1))
					{
					//pivot trouvé
					flagPivot[currentCol] = true;//marquage du pivot
					isPivotFound = true;
					}
				currentCol++;
				} while(currentCol < cols - 1 && !isPivotFound);//cols-2 because we don't look at the augmented part of the matrix

			//traitement de la variable libre
			if (isPivotFound == false)
				{
				currentCol = 0;
				do
					{
					if (flagPivot[currentCol] == false)
						{
						updateListMapNameToCoefiecient(freeVariableIndex, currentRow, cols - 1);//add and save
						isPivotFound = true; //permet de sortir de la boucle
						flagPivot[currentCol] = true;//marquage de l'injection d'une variable libre
						//create a new step
						listOperation.add(tabVarStyle[currentCol] + " est libre donc on a " + tabVarStyle[currentCol] + " = u" + freeVariableIndex);
						matrix.set(currentRow, currentCol, 1); //set the free variable in the matrice
						listMatrix.add(matrix.cloneOf());//save

						//Gestion de la place du pivot
						if (currentRow != currentCol)
							{
							for(int j = 0; j < cols - 1; j++)//parcours les colonnes
								{
								int i = j;
								while(i < rows && matrix.get(i, j) != 1)
									{
									//Cherche le pivot
									i++;
									}
								i--;
								if (i != j) //si le pivot n'est pas à sa place il faut le swap
									{
									System.out.println("("+i+","+j+")");
									matrix.swapRows(i, j);
									swapRowsCoeficientMatrix(currentRow, j);
									stringBuilder.append("L");
									stringBuilder.append(i + 1);
									stringBuilder.append(" <=> L");
									stringBuilder.append(j + 1);
									listOperation.add(stringBuilder.toString());
									stringBuilder = new StringBuilder();
									listMatrix.add(matrix.cloneOf());
									updateListMapNameToCoefiecient();
									}
								}

							}

						freeVariableIndex++;
						}
					currentCol++;
					} while(currentCol < cols - 1 && !isPivotFound);
				}
			}

		//manage the case where there is more variable than equation

		//we scale the matrix of the coeficient to their final size
		int m = 1;
		Matrix matrixTemp = null;
		while(m < cols - rows)
			{
			if (listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 1).containsKey("u" + m))
				{
				matrixTemp = listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 1).get("u" + m);
				matrix = new Matrix(cols - 1, cols);
				//we copy the values of the last matrix into the new one

				for(int j = 0; j < rows; j++)
					{
					for(int k = 0; k < cols; k++)
						{
						matrix.set(j, k, matrixTemp.get(j, k));
						}
					}
				listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 1).replace("u" + m, matrix.cloneOf());
				}
			m++;
			}

		//we then go over each colomn
		for(int i = 0; i < cols - rows - 1; ++i)
			{
			//we create a square matrix
			matrix = new Matrix(rows + i + 1, cols);
			//we copy the values of the last matrix into the new one
			for(int j = 0; j < rows + i; j++)
				{
				for(int k = 0; k < cols - 1; k++)
					{
					matrix.set(j, k, listMatrix.get(listMatrix.size() - 1).get(j, k));
					}
				}

			//we set the value of the new free variable
			listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 1).put("u" + freeVariableIndex, new Matrix(cols - 1, cols));
			listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 1).get("u" + freeVariableIndex).set(rows + i, cols - 1, 1);
			updateListMapNameToCoefiecient();//save

			//create a new step
			currentCol = 0;
			while(flagPivot[currentCol] == true)
				{
				currentCol++;
				}
			flagPivot[currentCol] = true;//marquage de l'injection d'une variable libre
			matrix.set(rows + i, currentCol, 1); //set the free variable in the matrice
			listMatrix.add(matrix.cloneOf());//save
			listOperation.add(tabVarStyle[rows + i] + " est libre donc on a " + tabVarStyle[rows + i] + " = u" + freeVariableIndex);

			freeVariableIndex++;
			}
		//fin du traitement des variables libres.

		if (freeVariableIndex == 1)
			{
			return false;
			}
		else
			{
			return true;
			}
		}

	/*------------------------------------------------------------------*\
	|*							Tools Private Functions					*|
	\*------------------------------------------------------------------*/

	//return vrai si le sytème est consistant
	private boolean hasSolution()
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

	//used during the reduction algorithme. It makes a copy of the previous map and add it to the list of map.
	private void updateListMapNameToCoefiecient()
		{
		listMapNameToCoeficient.add(new HashMap<String, Matrix>());
		int k = 1;
		while(k < cols)
			{
			if (listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 2).containsKey("u" + k))
				{
				listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 1).put("u" + k, listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 2).get("u" + k).cloneOf());
				}
			k++;
			}

		}

	//used during the back substitution. the index represents the index of the free variable stored in the map.
	private void updateListMapNameToCoefiecient(int freeVariableIndex, int row, int col)
		{
		listMapNameToCoeficient.add(new HashMap<String, Matrix>());
		int k = 1;
		while(k < cols)
			{
			if (listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 2).containsKey("u" + k))
				{
				listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 1).put("u" + k, listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 2).get("u" + k).cloneOf());
				}
			k++;
			}
		listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 1).put("u" + freeVariableIndex, new Matrix(rows, cols));
		listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 1).get("u" + freeVariableIndex).set(row, col, 1);
		}

	//used when we need to add a new row to the matrix
	private void addRowsCoeficientMatrix(int srcRow, int destRow, double factor)
		{
		int k = 1;
		while(k < cols)
			{
			if (listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 1).containsKey("u" + k))
				{
				listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 1).get("u" + k).addRows(srcRow, destRow, factor);
				}
			k++;
			}
		}

	private void swapRowsCoeficientMatrix(int currentRow, int currentCol)
		{
		int k = 1;
		while(k < cols)
			{
			if (listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 1).containsKey("u" + k))
				{
				listMapNameToCoeficient.get(listMapNameToCoeficient.size() - 1).get("u" + k).swapRows(currentRow, currentCol);
				}
			k++;
			}
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
		double coeficient;

		for(int step = 0; step < listMatrix.size(); step++)
			{
			listTabMatrix.add(new String[listMatrix.get(step).rowCount()][cols]);
			matrix = listMatrix.get(step);
			for(int i = 0; i < listMatrix.get(step).rowCount(); i++)
				{
				for(int j = 0; j < cols; j++)
					{
					stringBuilder = new StringBuilder();
					stringBuilder.append(formatter.format(matrix.get(i, j)));
					boolean isFirst = true;
					int k = 1;
					//manage the free variable
					while(k < cols)
						{
						if (listMapNameToCoeficient.get(step).containsKey("u" + k))
							{
							coeficient = listMapNameToCoeficient.get(step).get("u" + k).get(i, j);
							if (!MathTools.isEquals(coeficient, 0))
								{
								//efface le 0
								if (MathTools.isEquals(matrix.get(i, j), 0) && isFirst)
									{
									stringBuilder.deleteCharAt(stringBuilder.length() - 1);
									isFirst = false;
									}
								if (coeficient > 0)
									{
									if (!MathTools.isEquals(coeficient, 1))
										{
										//gère le +
										if (isFirst)
											{
											stringBuilder.append("+");
											}
										stringBuilder.append(formatter.format(coeficient));
										isFirst = false;
										}
									else if (isFirst)
										{
										stringBuilder.append("+");
										}
									stringBuilder.append("u" + k);
									isFirst = false;
									}
								else
									{
									if (!MathTools.isEquals(coeficient, -1))
										{
										stringBuilder.append(formatter.format(coeficient));
										isFirst = false;
										}
									else
										{
										stringBuilder.append("-");
										}
									stringBuilder.append("u" + k);
									isFirst = false;
									}
								}
							}
						k++;
						}
					listTabMatrix.get(step)[i][j] = stringBuilder.toString();
					}
				}
			}
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	//output
	private List<String[][]> listTabMatrix;
	private List<String> listOperation;
	private String solution;//Donne une information sur le nombre de solution

	// tools
	private List<Matrix> listMatrix;
	private List<Map<String, Matrix>> listMapNameToCoeficient;
	private int rows;
	private int cols;

	//input
	private int varStyle;
	private Matrix matrix;

	}
