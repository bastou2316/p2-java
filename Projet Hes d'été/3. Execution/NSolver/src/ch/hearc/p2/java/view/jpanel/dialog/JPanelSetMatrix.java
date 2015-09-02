
package ch.hearc.p2.java.view.jpanel.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import ch.hearc.p2.java.model.Matrix;
import ch.hearc.p2.java.view.IndependentVar;
import ch.hearc.p2.java.view.dialog.JDialogSetMatrix;

public class JPanelSetMatrix extends JPanelDialog
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelSetMatrix(Matrix matrix, int method, boolean solved)
		{
		super();

		this.matrix = matrix;
		this.n = matrix.rowCount();//attention inversion ptetre
		this.m = matrix.columnCount();
		this.isMatrixSolved = solved;

		IndependentVar.create(n, m, method);//remplacer 2 par methodVar

		// Composition du panel
		geometry();
		control();
		appearance();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void geometry()
		{
		// JComponent : Instanciation
		JPanel panelVar = new JPanel();
		panelVar.setLayout(new GridLayout(n, m, 0, 0));
		panelVar.setBorder(new TitledBorder(null, "Remplissage de la matrice", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));

		JPanel panelButton = new JPanel();

		solveButton = new JButton("R\u00E9soudre");
		previousButton = new JButton("Précédent");

		tabTextField = new JTextField[n][];
		String[][] tabString = IndependentVar.getTabVar();

		for(int i = 1; i <= n; i++)
			{
			tabTextField[i - 1] = new JTextField[m];
			for(int j = 1; j <= m ; j++)// + 1; j++)
				{
				Box panMatrice = Box.createHorizontalBox();

				String string;

				if (j == m-1)
					{
					string = tabString[i-1][j-1] + j + "=";
					}
				else
					{
					if (j == m)
						{
						string = "";
						}
					else
						{
						string = tabString[i-1][j-1] + j + "+";
						}
					}
				JLabel label = new JLabel(string);
				label.setHorizontalAlignment(SwingConstants.CENTER);
				JTextField textfield = new JTextField();
				tabTextField[i - 1][j - 1] = textfield;
				textfield.setPreferredSize(new Dimension(50, 30));

				if (isMatrixSolved)
					{
					textfield.setText(String.valueOf(matrix.get(i - 1, j - 1)));
					}

				Box box = Box.createVerticalBox();
				box.add(Box.createVerticalGlue());
				box.add(textfield);
				box.add(Box.createVerticalGlue());

				panMatrice.add(box);
				panMatrice.add(label);
				panelVar.add(panMatrice);
				}
			}

			// Layout : Specification
			{
			setLayout(new BorderLayout());
			}

		// JComponent : add
		panelButton.add(previousButton);
		panelButton.add(solveButton);

		add(panelVar, BorderLayout.CENTER);
		add(panelButton, BorderLayout.SOUTH);
		}

	private void control()
		{
		solveButton.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					//Remplissage de la matrice
					//matrix = new Matrix(n, m + 1);

					for(int i = 0; i < n; i++)
						{
						for(int j = 0; j < m; j++) // m + 1; j++)
							{
							//System.out.println(Float.parseFloat(tabTextField[i][j].getText()));
							float value = (tabTextField[i][j].getText().isEmpty()) ? 0 : Float.parseFloat(tabTextField[i][j].getText());
							matrix.set(i, j, value);
							}
						}

					choice = 1;
					JDialogSetMatrix jdialog = (JDialogSetMatrix) getRootPane().getParent();
					jdialog.close();
					}
			});

		previousButton.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					choice = 2;
					JDialogSetMatrix jdialog = (JDialogSetMatrix) getRootPane().getParent();
					jdialog.close();
//					controllerMain.showDialog(DIALOG.SET_EQUATION);
					}
			});
		}

	private void appearance()
		{
		// rien
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private Matrix matrix;
	private boolean isMatrixSolved;

	// Tools
	private JButton solveButton, previousButton;
	private JTextField[][] tabTextField;
	private int n;
	private int m;

	}
