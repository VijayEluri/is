package ecv.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import ecv.composite.InvalidExpressionError;

@SuppressWarnings("serial")
public class EditorPanel extends JPanel implements ActionListener
{
	private Model model;
	private JTextPane textPane, errorPane;
	private JButton parseButton;

	public EditorPanel (Model model)
	{
		super ();
		this.model = model;

		setupUI ();
	}

	public void actionPerformed (ActionEvent e)
	{
		parseExpression (textPane.getText ());
	}

	public boolean parseExpression (String expression)
	{
		textPane.setText (expression);
		try
		{
			model.parse (expression);
			errorPane.setText ("Espressione corretta");
		}
		catch (InvalidExpressionError e)
		{
			errorPane.setText (e.getMessage ());
			return false;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog (this, "L' Espressione inserita è errata'"+expression+"':\n\n"+e, "Error", JOptionPane.ERROR_MESSAGE); 
		}
		return true;
	}

	private void setupUI ()
	{
		setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
		textPane = createTextPane ();
		textPane.setText ("(Inserisci qui l' espressione)");
		textPane.addMouseListener (new MouseAdapter ()
		{
			public void mousePressed (MouseEvent e)
			{
				textPane.setText ("");
				textPane.removeMouseListener (this);
			}
		});
		add (new JScrollPane (textPane));
		parseButton = createParseButton ();
		add (parseButton);
		errorPane = createErrorPane ();
		add (new JScrollPane (errorPane));
	}

	private JTextPane createTextPane ()
	{
		JTextPane textPane = new JTextPane ();
		return textPane;
	}

	private JTextPane createErrorPane ()
	{
		JTextPane errorPane = new JTextPane ();
		errorPane.setEditable (false);
		return errorPane;
	}

	private JButton createParseButton ()
	{
		JButton button = new JButton ("Parse");
		button.addActionListener (this);
		return button;
	}
}