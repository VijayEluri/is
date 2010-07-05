package ecv.gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class PostfixView extends JPanel implements Observer
{
	private JTextArea resultArea;

	public PostfixView ()
	{
		super ();

		setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
		setupUI ();
	}

	private void setupUI ()
	{
		resultArea = new JTextArea ();
		resultArea.setEditable (false);
		add (new JScrollPane (resultArea));
	}

	public void update (Observable o, Object arg)
	{
		if (arg == Model.Change.CHANGE_PARSED)
		{
			Model model = (Model)o;
			try
			{
				resultArea.setText (model.postfixPrint ());
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog (this, "An error occurred while postfix printing the expression:\n\n"+e, "Error", JOptionPane.ERROR_MESSAGE);            
			}
		}
	}
}