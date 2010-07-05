package ecv.gui;


import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings({ "serial" })
public class EvaluationView extends JPanel implements Observer
{
	private JTextArea resultArea;

	public EvaluationView ()
	{
		super ();

		setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
		setupUI ();
	}

	private void setupUI ()
	{
		resultArea = new JTextArea ();
		resultArea.setEditable (false);
		add (resultArea);
	}

	public void update (Observable o, Object arg)
	{
		if (arg == Model.Change.CHANGE_PARSED)
		{
			Model model = (Model)o;
			resultArea.setText (""+model.evaluate ());
		}
	}
}