
package ch.hearc.p2.java.view.jpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.hearc.p2.java.view.IndependentVar;

public class JPanelMatrix extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/
	public JPanelMatrix(int rows, int varStyle)
		{
		this.rows = rows;
		this.varStyle = varStyle;

		variableDisplay = true;
		space = " ";
		symbol = "+";
		labels = new LinkedList<JLabel>();

		geometry();
		appareance();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public void updateLabels(String[][] matrix, Set<Integer> setDifference)
		{
		//Update
		updateGeometry(matrix.length);

		//Recuperation des variables et affichage
		String[] tabVar = IndependentVar.getTabVar(matrix[0].length, varStyle);//voir dans le constr

		if (variableDisplay)
			{
			equationDisplay(matrix, setDifference, tabVar);
			}
		else
			{
			matrixDisplay(matrix, setDifference);
			}
		}

	public void updateSolution(String solution)
		{
		if (boxSolution != getComponent(0))
			{
			Font font = getComponent(0).getFont();
			removeAll();

			labelSolution.setText(solution);
			labelSolution.setFont(font);
			add(boxSolution, BorderLayout.CENTER);
			}

		revalidate();
		repaint();
		}

	private void equationDisplay(String[][] matrix, Set<Integer> setDifference, String[] tabVar)
		{
		int nbVar = 0;//Pour le blocage d'affichage du "+"
		for(int i = 0; i < matrix.length; i++)//rows; i++)
			{
			nbVar = 0;
			StringBuilder builder = new StringBuilder();
			for(int j = 0; j < matrix[0].length - 1; j++) //verifier -1
				{
				String value = matrix[i][j];
				if (!value.equals("0")) //0 => Rien a afficher
					{
					if (value.substring(0, 1).equals("-"))
						{
						symbol = "-";
						}
					else
						{
						symbol = "+";
						}

					if (nbVar != 0) //sauf première variable
						{
						builder.append(space + symbol + space);
						}

					if (!value.equals("1")) //1 => On affiche uniquement la variable
						{
						if (symbol.equals("-"))
							{
							builder.append(value.substring(1, value.length() - 1));//Autres => on affiche le coefficient
							}
						else
							{
							builder.append(value);
							}
						}

					builder.append(tabVar[j]);
					nbVar++;
					}
				}

			if (nbVar == 0)//Pas de var
				{
				builder.append("0");
				}

			//On sort de la boucle 1 avant pour placer le =
			builder.append(space + "=" + space);
			builder.append(matrix[i][matrix[0].length - 1]);

			//Application du textes au labels
			labels.get(i).setText(builder.toString());

			//Mise en evidence
			if (setDifference != null && setDifference.contains(i))
				{
				labels.get(i).setForeground(Color.RED);
				}
			else
				{
				labels.get(i).setForeground(Color.BLACK);
				}
			}
		}

	public void matrixDisplay(String[][] matrix, Set<Integer> setDifference)
		{
		for(int i = 0; i < matrix.length; i++)
			{
			StringBuilder builder = new StringBuilder();

			for(int j = 0; j < matrix[0].length - 1; j++)
				{
				builder.append(matrix[i][j]);
				builder.append(space);
				}

			//On sort de la boucle 1 avant pour placer le =
			builder.append(space + "=" + space);
			builder.append(matrix[i][matrix[0].length - 1]);

			//Application du textes au labels
			labels.get(i).setText(builder.toString());

			//Mise en evidence
			if (setDifference != null && setDifference.contains(i))
				{
				labels.get(i).setForeground(Color.RED);
				}
			else
				{
				labels.get(i).setForeground(Color.BLACK);
				}
			}
		}

	public void setVariableDisplay(boolean variableDisplay)
		{
		this.variableDisplay = variableDisplay;
		}

	public void setSpaceDisplay(boolean spaceDisplay)
		{
		if (spaceDisplay)
			{
			space = "   ";
			}
		else
			{
			space = " ";
			}
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void updateGeometry(int rows)
		{
		if (boxV != getComponent(0))
			{
			Font font = getComponent(0).getFont();
			removeAll();

			for(JLabel label:labels)
				{
				label.setFont(font);
				}
			add(boxV, BorderLayout.CENTER);

			revalidate();
			repaint();
			}

		while(labels.size() < rows)
			{
			JLabel label = new JLabel("N/A");
			label.setForeground(Color.RED);
			label.setFont(labels.get(0).getFont());
			labels.add(label);
			boxV.add(label);
			}
		while(labels.size() > rows)
			{
			JLabel label = labels.remove(labels.size() - 1);
			boxV.remove(label);
			label = null;
			}

		repaint();
		}

	private void geometry()
		{
		//Declaration
		for(int i = 0; i < rows; i++)
			{
			labels.add(new JLabel("N/A"));
			}
		labelSolution = new Label("N/A");//utilisé plus tard

		//Layout
		boxV = Box.createVerticalBox();
		boxSolution = Box.createHorizontalBox();//utilisé plus tard
		setLayout(new BorderLayout());

		//Ajout
		for(int i = 0; i < rows; i++)//a voir -1
			{
			boxV.add(labels.get(i));
			}
		boxSolution.add(labelSolution);
		add(boxV, BorderLayout.CENTER);
		}

	private void appareance()
		{
		setBackground(Color.WHITE);
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	//Inputs
	private int rows;
	private int varStyle;

	//Tools
	private List<JLabel> labels;
	private Label labelSolution;
	private Box boxV;
	private Box boxSolution;
	private String space;
	private String symbol;
	private boolean variableDisplay;

	}
