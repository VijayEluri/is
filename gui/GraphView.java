package ecv.gui;


import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class GraphView extends JPanel implements Observer
{
	private JLabel graphLabel;

	public GraphView ()
	{
		super ();

		setLayout (new BorderLayout ());
		setupUI ();
	}

	private void setupUI ()
	{
		graphLabel = new JLabel ();
		add (new JScrollPane (graphLabel), BorderLayout.CENTER);
	}

	public void update (Observable o, Object arg)
	{
		if (arg == Model.Change.CHANGE_PARSED)
		{
			Model model = (Model)o;
			Icon icon = new ImageIcon (model.paint ());
			graphLabel.setIcon (icon);
		}
	}
}
