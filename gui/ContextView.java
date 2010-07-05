package ecv.gui;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import ecv.visitor.Context;

@SuppressWarnings("serial")
public class ContextView extends JPanel implements Observer
{
	private JTable table;

	public ContextView ()
	{
		super ();

		setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
		setupUI ();
	}

	private void setupUI ()
	{
		table = new JTable ();
		add (new JScrollPane (table));
	}

	public void update (Observable o, Object arg)
	{
		if (arg == Model.Change.CHANGE_CONTEXT)
		{
			Model model = (Model)o;
			Context context = model.getContext ();
			Object[] identifiers = context.identifiersSet().toArray ();
			Arrays.sort (identifiers);
			Object[] columnNames = { "Id", "Value" };
			Object[][] data = new Object[identifiers.length][2];
			int i=0;
			for (Object identifier: identifiers)
			{
				data[i][0] = identifier;
				data[i][1] = context.getValue ((String)identifier);
			}
			table.setModel (new DefaultTableModel (data, columnNames));
		}
	}
}