
package ch.hearc.p2.java.view.jpanel;

import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

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
		this.varStyle = varStyle;

		labels = new LinkedList<JLabel>();

		geometry();
		appareance();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public void updateLabels(String[][] matrix)
		{
		String[] tabVar = IndependentVar.getTabVar(cols, varStyle);//voir dans le constr

		for(int i = 0; i < rows; i++)
			{
			StringBuilder builder = new StringBuilder();
			for(int j = 0; j < matrix.length - 1; j++)
				{
				if (!matrix[i][j].equals("0")) //0 => Rien a afficher
					{
					if (!matrix[i][j].equals("1")) //1 => On affiche uniquement la variable
						{
						builder.append(matrix[i][j]);//Autres => on affiche le coefficient
						}
					builder.append(tabVar[j]);
					builder.append("\t");
					}
				}

			//On sort de la boucle 1 avant pour placer le =
			builder.append("= ");
			builder.append(matrix[i][matrix.length - 1]);

			//Application du textes au labels
			labels.get(i).setText(builder.toString());

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
		for(int i = 0; i < rows; i++)
			{
			labels.add(new JLabel("N/A"));
			}

		//Layout
		setLayout(new GridLayout(rows, 1));

		//Ajout
		for(int i = 0; i < rows; i++)//a voir -1
			{
			add(labels.get(i));
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
	private List<JLabel> labels;

	}
