
package ch.hearc.coursjava.gui.p2.solvematrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class JFrameSolve extends JFrame
	{

	public JFrameSolve()
		{
		geometry();
		control();
		apparance();



		}

	private void apparance()
		{
		setLocation(200, 200);
		setResizable(true);
		setSize(500, 400);
		setVisible(true);

		}

	private void control()
		{
		// TODO Auto-generated method stub

		}

	private void geometry()
		{
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));

		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));

		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		panel_4.add(textArea, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("R\u00E9soudre");
		panel_2.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Stop");
		panel_2.add(btnNewButton_1);

		TitledBorder titlesborder = BorderFactory.createTitledBorder("Opérations");
		titlesborder.setTitleColor(Color.blue);
		panel_3.setBorder(titlesborder);
		panel_3.setForeground(Color.blue);

		TitledBorder titlematrix = BorderFactory.createTitledBorder("Matrice");
		titlematrix.setTitleColor(Color.blue);
		panel_4.setBorder(titlematrix);
		panel_4.setForeground(Color.blue);

		rowList.setVisibleRowCount(5);
		listScrollPane.setViewportView(rowList);
		panel_3.add(listScrollPane);

		}

	private JScrollPane listScrollPane = new JScrollPane();
	private String[] stringArray = { "Testing", "This", "Stuff",
			"ffffffffffffffggggggggggg" };
	private JList rowList = new JList(stringArray);
	}
