
package ch.hearc.p2.java.view.jpanel.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

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

	public JPanelSetMatrix(Matrix matrix, int varStyle, boolean solved)
		{
		super();

		this.matrix = matrix;
		this.rows = matrix.rowCount();//attention inversion ptetre
		this.cols = matrix.columnCount();
		this.isMatrixSolved = solved;
		this.varStyle = varStyle;

		// Composition du panel
		geometry();
		control();
		appearance();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/
	private void changeFont(Component component, int fontSize)
		{
		Font f = component.getFont();
		component.setFont(new Font(f.getName(), f.getStyle(), f.getSize() + fontSize));
		if (component instanceof Container)
			{
			for(Component child:((Container)component).getComponents())
				{
				changeFont(child, fontSize);
				}
			}
		}

	private void geometry()
		{
		// JComponent : Instanciation
		JPanel panelVar = new JPanel();
		panelVar.setLayout(new GridLayout(rows, cols, 0, 0));
		panelVar.setBorder(new TitledBorder(null, "Remplissage de la matrice", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));

		JPanel panelButton = new JPanel();

		solveButton = new JButton("R\u00E9soudre");
		previousButton = new JButton("Précédent");

		tabTextField = new JTextField[rows][];
		String[] tabString = IndependentVar.getTabVar(cols - 1, varStyle);

		for(int i = 1; i <= rows; i++)
			{
			tabTextField[i - 1] = new JTextField[cols];
			for(int j = 1; j <= cols; j++)
				{
				Box panMatrice = Box.createHorizontalBox();

				String string;

				if (j == cols - 1)
					{
					string = tabString[j - 1] + "=";
					}
				else
					{
					if (j == cols)
						{
						string = "";
						}
					else
						{
						string = tabString[j - 1] + "+";
						}
					}
				JLabel label = new JLabel(string);
				label.setHorizontalAlignment(SwingConstants.CENTER);
				JTextField textfield = new JTextField("0");
				textfield.getFont().deriveFont(Font.ITALIC);
				textfield.setForeground(Color.gray);

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

		for(JTextField[] line : tabTextField)
			{
			for(JTextField textField : line)
				{
				textField.addFocusListener(new FocusListener()
					{

						@Override
						public void focusLost(FocusEvent e)
							{
							JTextField texteField = ((JTextField)e.getSource());
							if(texteField.getText().isEmpty())
								{
									texteField.setText(textTemp);//"0");		//Affichage de l'ancienne valeure
									texteField.getFont().deriveFont(Font.ITALIC);
									texteField.setForeground(Color.gray);
								}
							}

						@Override
						public void focusGained(FocusEvent e)
							{
							JTextField texteField = ((JTextField)e.getSource());

							textTemp = texteField.getText();

							texteField.setText("");
							texteField.getFont().deriveFont(Font.PLAIN);
							texteField.setForeground(Color.black);

							texteField.repaint();
							}
					});
				}

			}

		solveButton.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					//Remplissage de la matrice
					//matrix = new Matrix(n, m + 1);

					for(int i = 0; i < rows; i++)
						{
						for(int j = 0; j < cols; j++) // m + 1; j++)
							{
							//System.out.println(Float.parseFloat(tabTextField[i][j].getText()));
							float value = (tabTextField[i][j].getText().isEmpty()) ? 0 : Float.parseFloat(tabTextField[i][j].getText());
							matrix.set(i, j, value);
							}
						}

					choice = 1;
					JDialogSetMatrix jdialog = (JDialogSetMatrix)getRootPane().getParent();
					jdialog.close();
					}
			});

		previousButton.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					choice = 2;
					JDialogSetMatrix jdialog = (JDialogSetMatrix)getRootPane().getParent();
					jdialog.close();
					//					controllerMain.showDialog(DIALOG.SET_EQUATION);
					}
			});

		this.addMouseWheelListener(new MouseWheelListener()
			{
				@Override
				public void mouseWheelMoved(MouseWheelEvent e)
					{
					// TODO Auto-generated method stub
					changeFont(JPanelSetMatrix.this, e.getWheelRotation());
					}
			});
		}

	private void appearance()
		{
		changeFont(this, 10);
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Inputs
	private Matrix matrix;
	private int varStyle;
	private boolean isMatrixSolved;

	// Tools
	private JButton solveButton, previousButton;
	private JTextField[][] tabTextField;
	private String textTemp;
	private int rows;
	private int cols;

	}
