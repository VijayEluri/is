package ecv.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Observable;
import java.util.Properties;

import ecv.composite.Expression;
import ecv.composite.ExpressionBuilder;
import ecv.composite.Parser;
import ecv.composite.StreamTokenizerAdapter;
import ecv.visitor.Context;
import ecv.visitor.EvaluatingVisitor;
import ecv.visitor.InfixPrinterVisitor;
import ecv.visitor.PaintVisitor;
import ecv.visitor.PostfixPrinterVisitor;
import ecv.visitor.PropertiesContext;

public class Model extends Observable
{
	private PropertiesContext context = new PropertiesContext ();
	private Expression expression;

	public Context getContext ()
	{
		return context;
	}

	public boolean evaluate ()
	{
		if (expression == null)
			throw new IllegalStateException ("No expression specified");
		EvaluatingVisitor visitor = new EvaluatingVisitor (context);
		return visitor.valueOf (expression);
	}

	public String infixPrint () throws IOException
	{
		if (expression == null)
			throw new IllegalStateException ("No expression specified");
		StringWriter writer = new StringWriter ();
		InfixPrinterVisitor visitor = new InfixPrinterVisitor (writer);
		visitor.print (expression);
		return writer.toString ();
	}

	public String postfixPrint () throws IOException
	{
		if (expression == null)
			throw new IllegalStateException ("No expression specified");
		StringWriter writer = new StringWriter ();
		PostfixPrinterVisitor visitor = new PostfixPrinterVisitor (writer);
		visitor.print (expression);
		return writer.toString ();
	}

	public BufferedImage paint ()
	{
		if (expression == null)
			throw new IllegalStateException ("No expression specified");
		PaintVisitor visitor = new PaintVisitor ();
		return visitor.createImage (expression);
	}

	public static enum Change
	{
		CHANGE_CONTEXT,
		CHANGE_PARSED;
	}

	public void loadContext (File file) throws IOException
	{
		FileInputStream is = new FileInputStream (file);
		Properties properties = new Properties ();
		properties.load (is);
		loadContext (properties);
		setChanged ();
		notifyObservers (Change.CHANGE_CONTEXT);
	}

	public void loadContext (Properties properties)
	{
		PropertiesContext context = new PropertiesContext ();
		context.load (properties);
		this.context = context;
		setChanged ();
		notifyObservers (Change.CHANGE_CONTEXT);
	}

	public void parse (String expression) throws IOException
	{
		StringReader reader = new StringReader (expression);
		StreamTokenizerAdapter sta = new StreamTokenizerAdapter (reader);
		ExpressionBuilder builder = new ExpressionBuilder ();
		Parser parser = new Parser (sta, builder);
		parser.parse ();
		this.expression = builder.getResult ();
		setChanged ();
		notifyObservers (Change.CHANGE_PARSED);
	}
}