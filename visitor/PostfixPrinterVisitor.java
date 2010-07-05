package ecv.visitor;

import java.io.IOException;
import java.io.Writer;

import ecv.composite.ArithmeticOperator;
import ecv.composite.Expression;
import ecv.composite.Identifier;
import ecv.composite.LogicalOperator;
import ecv.composite.RelationalOperator;
import ecv.composite.Visitor;

public class PostfixPrinterVisitor implements Visitor
{
	private Writer writer;
	private StringBuilder builder;

	public PostfixPrinterVisitor (Writer writer)
	{
		this.writer = writer;
	}

	public void visit (ecv.composite.Number number)
	{
		builder.append (number.getValue()+" ");
	}

	public void visit (Identifier identifier)
	{
		builder.append (identifier.getName()+" ");
	}

	public void visit (LogicalOperator logicalOperator)
	{
		logicalOperator.getLeft().accept (this);
		if (logicalOperator.getRight() != null)
			logicalOperator.getRight().accept (this);
		if (logicalOperator instanceof LogicalOperator.And)
			builder.append ("and ");
		else if (logicalOperator instanceof LogicalOperator.Or)
			builder.append ("or ");
		else if (logicalOperator instanceof LogicalOperator.Not)
			builder.append ("not ");
		else
			assert false;
	}

	public void visit (RelationalOperator relationalOperator)
	{
		relationalOperator.getLeft().accept (this);
		relationalOperator.getRight().accept (this);
		if (relationalOperator instanceof RelationalOperator.Eq)
			builder.append ("== ");
		else if (relationalOperator instanceof RelationalOperator.Neq)
			builder.append ("!= ");
		else if (relationalOperator instanceof RelationalOperator.Gt)
			builder.append ("> ");
		else if (relationalOperator instanceof RelationalOperator.Ge)
			builder.append (">= ");
		else if (relationalOperator instanceof RelationalOperator.Lt)
			builder.append ("< ");
		else if (relationalOperator instanceof RelationalOperator.Le)
			builder.append ("<= ");
		else
			assert false;
	}

	public void visit (ArithmeticOperator arithmeticOperator)
	{
		arithmeticOperator.getLeft().accept (this);
		arithmeticOperator.getRight().accept (this);
		if (arithmeticOperator instanceof ArithmeticOperator.Power)
			builder.append ("^ ");
		else if (arithmeticOperator instanceof ArithmeticOperator.Div)
			builder.append ("/ ");
		else if (arithmeticOperator instanceof ArithmeticOperator.Rem)
			builder.append ("% ");
		else if (arithmeticOperator instanceof ArithmeticOperator.Mult)
			builder.append ("* ");
		else if (arithmeticOperator instanceof ArithmeticOperator.Minus)
			builder.append ("- ");
		else if (arithmeticOperator instanceof ArithmeticOperator.Plus)
			builder.append ("+ ");
		else
			assert false;
	}

	public void print (Expression expression) throws IOException
	{
		builder = new StringBuilder ();
		expression.accept (this);
		// trim trailing whitespace
		writer.write (builder.toString().trim ());
	}
}