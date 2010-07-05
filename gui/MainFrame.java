package ecv.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener
{
	private Model model;
	private JMenuItem openContextItem, openExpressionItem, quitItem;
	private File contextFile, expressionFile;
	private EditorPanel editorPanel;

	public static void main (String[] args)
	{
		new MainFrame().setVisible(true);
	}

	public MainFrame ()
	{
		super ();
		this.model = new Model();

		setTitle ("ECV");
		setDefaultCloseOperation (WindowConstants.EXIT_ON_CLOSE);

		setupUI ();
		setSize (new Dimension (700, 300));
	}

	public void actionPerformed (ActionEvent event)
	{
		if (event.getSource () == openContextItem)
			openContext ();
		else if (event.getSource () == openExpressionItem)
			openExpression ();
		else if (event.getSource () == quitItem)
			quit ();
	}

	private void setupUI ()
	{
		setJMenuBar (createMenuBar ());
		ContextView contextView = createContextView ();
		editorPanel = createEditorPanel ();
		JSplitPane upPane = new JSplitPane (JSplitPane.VERTICAL_SPLIT, contextView, editorPanel);

		JTabbedPane tabbedPane = createTabbedPane ();
		JSplitPane lowPane = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT, upPane, tabbedPane);

		upPane.setDividerLocation (0);
		lowPane.setDividerLocation (350);
		add (lowPane);
	}

	private JMenuBar createMenuBar ()
	{
		JMenuBar menuBar = new JMenuBar ();
		JMenu menu = new JMenu ("File");
		openContextItem = menu.add("Open context");
		openContextItem.addActionListener (this);
		openExpressionItem = menu.add("Open expression");
		openExpressionItem.addActionListener (this);
		menu.addSeparator ();
		quitItem = menu.add("Quit");
		quitItem.addActionListener (this);
		menuBar.add (menu);
		return menuBar;
	}

	private EditorPanel createEditorPanel ()
	{
		return new EditorPanel (model);
	}

	private ContextView createContextView ()
	{
		ContextView view = new ContextView ();
		model.addObserver (view);
		return view;
	}

	private JTabbedPane createTabbedPane ()
	{
		JTabbedPane tabbedPane = new JTabbedPane ();
		tabbedPane.addTab ("Evaluation", createEvaluationView ());
		tabbedPane.addTab ("Infix Print", createInfixView ());
		tabbedPane.addTab ("Postfix print", createPostfixView ());
		tabbedPane.addTab ("Graph", createGraphView ());
		return tabbedPane;
	}

	private EvaluationView createEvaluationView ()
	{
		EvaluationView view = new EvaluationView ();
		model.addObserver (view);
		return view;
	}

	private InfixView createInfixView ()
	{
		InfixView view = new InfixView ();
		model.addObserver (view);
		return view;
	}

	private PostfixView createPostfixView ()
	{
		PostfixView view = new PostfixView ();
		model.addObserver (view);
		return view;
	}

	private GraphView createGraphView ()
	{
		GraphView view = new GraphView ();
		model.addObserver (view);
		return view;
	}

	private File openFile (String filterName, String ext, File selected, boolean acceptAllFile)
	{
		JFileChooser chooser = new JFileChooser ();
		FileNameExtensionFilter filter = new FileNameExtensionFilter (filterName, ext);
		chooser.setAcceptAllFileFilterUsed (acceptAllFile);
		chooser.setFileFilter (filter);
		if (selected != null)
			chooser.setSelectedFile (selected);
		int ret = chooser.showOpenDialog (this);
		if (ret == JFileChooser.APPROVE_OPTION)
			return chooser.getSelectedFile ();
		return null;
	}

	private void openContext ()
	{
		File file = openFile ("Properties file - *.properties", "properties", contextFile, false);
		if (file == null)
			return;
		try
		{
			model.loadContext (file);
			contextFile = file;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog (this, "An error occurred while loading context from file '"+file+"':\n\n"+e, "Error", JOptionPane.ERROR_MESSAGE); 
		}
	}

	private void openExpression ()
	{
		File file = openFile ("Expression file - *.txt", "txt", expressionFile, true);
		if (file == null)
			return;
		try
		{
			String content = new Scanner(file).useDelimiter("\\z").next ();
			if (editorPanel.parseExpression (content))
				expressionFile = file;
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog (this, "An error occurred while loading expression from file '"+file+"':\n\n"+e, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void quit ()
	{
		System.exit (0);
	}
}