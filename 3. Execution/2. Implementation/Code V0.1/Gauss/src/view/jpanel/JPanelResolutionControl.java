
package view.jpanel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import view.jframe.ResolutionControl_I;

public class JPanelResolutionControl extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelResolutionControl(ResolutionControl_I controlable)
		{
		this.controlable = controlable;
		geometry();
		control();
		appearance();
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

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void geometry()
		{
		// JComponent : Instanciation
		boutonSuivant = new JButton("Start");
		boutonPrecedent = new JButton("Stop");

		boutonSuivant.setSize(100, 40);
		boutonPrecedent.setSize(100, 40);

		boutonSuivant.setLocation(10, 100);
		boutonPrecedent.setLocation(10, 300);

		boutonSuivant.setEnabled(true);
		boutonPrecedent.setEnabled(false);

			// Layout : Specification
			{
			setLayout(new FlowLayout(FlowLayout.CENTER));

			//flowlayout.setHgap(20);
			// flowlayout.setVgap(20);
			}

		// JComponent : add
		add(boutonSuivant);
		add(boutonPrecedent);

		}

	private void control()
		{
		ActionListener actionListener = new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					switchEtatBouton();
					}
			};

		boutonSuivant.addActionListener(actionListener);
		boutonPrecedent.addActionListener(actionListener);

		boutonSuivant.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					suivant();
					}
			});

		boutonPrecedent.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent e)
					{
					precedent();
					}
			});

		}

	private void appearance()
		{
		setBorder(BorderFactory.createTitledBorder("Control"));
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void switchEtatBouton()
		{
		switchEtatBouton(boutonSuivant);
		switchEtatBouton(boutonPrecedent);
		}

	private void suivant()
		{
		controlable.suivant();
		}

	private void precedent()
		{
		controlable.precedent();
		}

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	private static void switchEtatBouton(JButton bouton)
		{
		bouton.setEnabled(!bouton.isEnabled());
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	private JButton boutonSuivant;
	private JButton boutonPrecedent;

	// Input
	private ResolutionControl_I controlable;

	}