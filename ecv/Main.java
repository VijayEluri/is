package ecv;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ecv.operator.Expression;
import ecv.operator.ExpressionBuilder;
import ecv.visitor.EvaluatingVisitor;
import ecv.visitor.InfixPrinterVisitor;
import ecv.visitor.PaintVisitor;
import ecv.visitor.PostfixPrinterVisitor;

@SuppressWarnings("serial")
public class Main extends JFrame implements DocumentListener {
	private JLabel err = new JLabel();
	private ImageIcon ok = new ImageIcon("ok.png");
	private ImageIcon fail = new ImageIcon("fail.png");
	private JLabel valid = new JLabel(ok);
	private JTextArea expr = new JTextArea("6 + 2^2 > 25 / 5 && 24 / [100 / 25] != 3 + 10 - 6", 5, 30);

	// visitors
	private JLabel evaluating = new JLabel();
	private JLabel infix = new JLabel();
	private JLabel postfix = new JLabel();
	private JLabel graph = new JLabel();

	public static void main(String args[]) {
		new Main();
	}

	private Main() {
		super("ECV");

		GridBagConstraints c = new GridBagConstraints();

		setLayout(new GridBagLayout());
		expr.getDocument().addDocumentListener(this);

		// Input and status controls
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridheight = 2;
		add(expr, c);

		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		add(valid, c);

		add(err, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JSeparator(), c);

		// Evaluating visitor
		c.ipady = 5;
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = 1;
		add(new JLabel("Evaluating visitor:"), c);

		c.gridwidth = GridBagConstraints.REMAINDER;
		add(evaluating, c);

		c.gridwidth = GridBagConstraints.REMAINDER;
		add(evaluating, c);

		add(new JSeparator(), c);

		// Infix visitor
		c.gridwidth = 1;
		add(new JLabel("Infix visitor:"), c);

		c.gridwidth = GridBagConstraints.REMAINDER;
		add(infix, c);

		add(new JSeparator(), c);

		// Postfix visitor
		c.gridwidth = 1;
		add(new JLabel("Postfix visitor:"), c);

		c.gridwidth = GridBagConstraints.REMAINDER;
		add(postfix, c);

		add(new JSeparator(), c);

		// Graph visitor
		c.gridwidth = 1;
		add(new JLabel("Graph visitor:"), c);

		c.gridwidth = GridBagConstraints.REMAINDER;
		add(graph, c);

		pack();
		setVisible(true);
		expr.requestFocusInWindow();
		expr.setCaretPosition(expr.getDocument().getLength());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void parse() {
		if(expr.getText().length() == 0) {
			valid.setIcon(ok);
			err.setText(null);

			evaluating.setText(null);
			infix.setText(null);
			postfix.setText(null);
			graph.setIcon(null);
		} else try {
			PropertiesContext context = new PropertiesContext ();
			ExpressionBuilder builder = new ExpressionBuilder();
			new Parser(new StreamTokenizerAdapter(new StringReader(expr.getText())), builder).parse();
			Expression expression = builder.getResult();

			evaluating.setText("" + new EvaluatingVisitor(context).valueOf(expression));

			StringWriter writer = new StringWriter();
			new InfixPrinterVisitor(writer).print(expression);
			infix.setText(writer.toString());

			writer = new StringWriter();
			new PostfixPrinterVisitor(writer).print(expression);
			postfix.setText("" + writer.toString());

			graph.setIcon(new ImageIcon(new PaintVisitor().createImage(expression)));

			valid.setIcon(ok);
			err.setText("Valid expression");
		} catch (InvalidExpressionError iee) {
			valid.setIcon(fail);
			err.setText(iee.getMessage());

			evaluating.setText(null);
			infix.setText(null);
			postfix.setText(null);
			graph.setIcon(null);

		} catch (IOException e) {
			e.printStackTrace();
		}
		pack();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		parse();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		parse();
	}

	@Override
	public void changedUpdate(DocumentEvent e) { }
}
