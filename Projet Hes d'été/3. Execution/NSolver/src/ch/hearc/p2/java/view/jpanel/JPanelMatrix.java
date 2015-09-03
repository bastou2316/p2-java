
package ch.hearc.p2.java.view.jpanel;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.hearc.p2.java.view.IndependentVar;

public class JPanelMatrix extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/
	public JPanelMatrix(int rows, int cols, int varStyle)
		{
		this.rows = rows;
		this.cols = cols;
		this.varStyle = varStyle;

		geometry();
		appareance();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public void updateLabels(String[][] matrix)
		{
		String[] tabVar = IndependentVar.getVarStyle(varStyle, rows, cols);//voir dans le constr

		for(int i = 0; i < rows; i++)
			{
			StringBuilder builder2 = new StringBuilder();
			for(int j = 0; j < cols - 1; j++)
				{
				if (!matrix[i][j].equals("0")) //0 => Rien a afficher
					{
					if (!matrix[i][j].equals("1")) //1 => On affiche uniquement la variable
						{
						builder2.append(matrix[i][j]);//Autres => on affiche le coefficient
						}
					builder2.append(tabVar[j]);
					builder2.append("\t");
					}
				}

			//On sort de la boucle 1 avant pour placer le =
			builder2.append("= ");
			builder2.append(matrix[i][cols - 1]);

			//Application du textes au labels
			labels[i].setText(builder2.toString());

			//	        if (i == 2)//A fixer
			//	            {
			//	            labels[i].setForeground(Color.RED);
			//	            }
			//	        else
			//	            {
			//	            labels[i].setForeground(Color.BLACK);
			//	            }

			}

		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void geometry()
		{
		//Declaration
		labels = new JLabel[rows];

		//Layout
		setLayout(new GridLayout(rows, 1));

		//Ajout
		for(int i = 0; i < rows; i++)//a voir -1
			{
			add(labels[i]);
			}
		}

	private void appareance()
		{
		//Rien
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	//Inputs
	private int rows;
	private int cols;
	private int varStyle;

	//Tools
	private JLabel[] labels;

	}
