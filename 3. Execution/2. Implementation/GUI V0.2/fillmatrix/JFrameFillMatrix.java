
package ch.hearc.coursjava.gui.p2.fillmatrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

public class JFrameFillMatrix extends JFrame
	{

	public JFrameFillMatrix(int n, int m)
		{
		JFrameFillMatrix.m = m;
		JFrameFillMatrix.n = n;
		geometry(n, m);
		control();
		apparance();
		}

	private void apparance()
		{
		setLocation(200, 200);
		setResizable(true);
		setSize(500, 400);
		pack();
		setResizable(false);
		setVisible(true);

		}

	private void control()
		{
		btnSolve.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{

					tabFloat = new float[n][];
					for(int i = 1; i <= n; i++)
						{
						tabFloat[i - 1] = new float[m + 1];
						for(int j = 1; j <= m + 1; j++)
							{
							try
								{
								tabFloat[i - 1][j - 1] = Float.parseFloat(tabTextField[i - 1][j - 1].getText());
								}
							catch (Exception e2)
								{

								}

							}
						}

					display(tabFloat);
					}
			});

		}

	public static float[][] getTabFloat()
		{
		return tabFloat;
		}

	public static void methodSolve(int noMethod)
		{

		switch(noMethod)
			{
			case 0:
				tabString = new String[3];
				tabString[0] = "X";
				tabString[1] = "Y";
				tabString[2] = "Z";
				break;
			case 1:
				tabString = new String[27];
				tabString[0] = "A";
				tabString[1] = "B";
				tabString[2] = "C";
				tabString[3] = "D";
				tabString[4] = "E";
				tabString[5] = "F";
				tabString[6] = "G";
				tabString[7] = "H";
				tabString[8] = "I";
				tabString[9] = "J";
				tabString[10] = "K";
				tabString[11] = "L";
				tabString[12] = "M";
				tabString[13] = "N";
				tabString[14] = "O";
				tabString[15] = "P";
				tabString[16] = "Q";
				tabString[17] = "R";
				tabString[18] = "S";
				tabString[19] = "T";
				tabString[20] = "U";
				tabString[21] = "V";
				tabString[22] = "W";
				tabString[23] = "X";
				tabString[24] = "Y";
				tabString[25] = "Z";
				break;
			case 2:
				tabString = new String[n+1];

				for(int i = 1; i <= n; i++)
					{
					tabString[i-1] = "X";
					}

				break;

			default:
				break;
			}
		}

	private void geometry(int n, int m)
		{
		methodSolve(1);
		panel = new JPanel();
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
		panel.setLayout(new GridLayout(n, m, 0, 0));
		panel.setBorder(new TitledBorder(null, "Remplissage de la matrice", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		tabTextField = new JTextField[n][];

		for(int i = 1; i <= n; i++)
			{
			tabTextField[i - 1] = new JTextField[m + 1];
			for(int j = 1; j <= m + 1; j++)
				{
				JPanel panMatrice = new JPanel();
				panMatrice.setLayout(new GridLayout(0, 2));

				String string;

				if (j == m)
					{
					string = tabString[j - 1] + j + "=";
					}
				else
					{
					if (j == m + 1)
						{
						string = "";
						}
					else
						{
						string = tabString[j - 1] + j + "+";
						}
					}
				JLabel label = new JLabel(string);
				label.setHorizontalAlignment(SwingConstants.CENTER);
				JTextField textfield = new JTextField();
				tabTextField[i - 1][j - 1] = textfield;
				textfield.setPreferredSize(new Dimension(50, 30));
				panMatrice.add(textfield);
				panMatrice.add(label);
				panel.add(panMatrice);
				}
			}
		JPanel panelAll = new JPanel();
		panelAll.setLayout(new BorderLayout());
		panelAll.add(panel, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		btnSolve = new JButton("R\u00E9soudre");
		panel_2.add(btnSolve);
		panelAll.add(panel_2, BorderLayout.SOUTH);

		getContentPane().add(panelAll);

		}

	private static void display(float[][] tabHeterogen)
		{
		for(int i = 0; i < tabHeterogen.length; i++)
			{
			for(int j = 0; j < tabHeterogen[i].length; j++)
				{
				System.out.print(tabHeterogen[i][j]);
				System.out.print("\t");
				}
			System.out.println();
			}
		}

	private JButton btnSolve;
	private JPanel panel;
	private JTextField[][] tabTextField;
	private static float[][] tabFloat;
	private static int n;
	private static int m;
	private static String[] tabString;

	}
