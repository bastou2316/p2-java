
package ch.hearc.p2.java.view.jpanel;

import java.awt.BorderLayout;
import java.awt.Color;
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
	public JPanelMatrix(int rows, int cols, int varStyle)
		{
		this.rows = rows;
		this.cols = cols;
		this.varStyle = varStyle;

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
		int nbVar = 0;//Pour le blocage d'affichage du "+"

		for(int i = 0; i < matrix.length; i++)//rows; i++)
			{
			nbVar = 0;
			StringBuilder builder = new StringBuilder();
			for(int j = 0; j < matrix[0].length - 1; j++) //verifier -1
				{
				if (!matrix[i][j].equals("0")) //0 => Rien a afficher
					{
					if (nbVar != 0) //sauf dernière variable
						{
						builder.append(" + ");
						}

					if (!matrix[i][j].equals("1")) //1 => On affiche uniquement la variable
						{
						builder.append(matrix[i][j]);//Autres => on affiche le coefficient
						}
					builder.append(tabVar[j]);
					nbVar++;
					}
				}

			//On sort de la boucle 1 avant pour placer le =
			builder.append(" = ");
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

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void updateGeometry(int rows)//a verifier
		{
		while(labels.size() < rows)
			{
			JLabel label = new JLabel("N/A");
			label.setForeground(Color.RED);
			labels.add(label);
			boxV.add(label);
			}
		while(labels.size() > rows)
			{
			JLabel label = labels.remove(labels.size() - 1);
			boxV.remove(label);
			label = null;
			}

		repaint();//updateUI();
		}

	private void geometry()
		{
		//Declaration
		for(int i = 0; i < rows; i++)
			{
			labels.add(new JLabel("N/A"));
			}

		//Layout
		boxV = Box.createVerticalBox();
		setLayout(new BorderLayout());

		//Ajout
		for(int i = 0; i < rows; i++)//a voir -1
			{
			boxV.add(labels.get(i));
			}
		add(boxV, BorderLayout.CENTER);
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
	private Box boxV;

	}
