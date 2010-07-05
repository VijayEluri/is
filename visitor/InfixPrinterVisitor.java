package ecv.visitor;

import java.io.IOException;
import java.io.Writer;

import ecv.composite.ArithmeticOperator;
import ecv.composite.Expression;
import ecv.composite.Identifier;
import ecv.composite.LogicalOperator;
import ecv.composite.RelationalOperator;
import ecv.composite.Visitor;

public class InfixPrinterVisitor implements Visitor
{
	private Writer writer;
	private StringBuilder builder;
	private Expression expression;

	public InfixPrinterVisitor (Writer writer)
	{
		this.writer = writer;
	}

	public void visit (ecv.composite.Number number)
	{
		builder.append (number.getValue ());
	}

	public void visit (Identifier identifier)
	{
		builder.append (identifier.getName ());
	}

	public void visit (LogicalOperator logicalOperator)
	{
		if (logicalOperator instanceof LogicalOperator.Not)
		{
			builder.append ("not ");
			logicalOperator.getLeft().accept (this);
			return;
		}
		builder.append ("(");
		logicalOperator.getLeft().accept (this);
		if (logicalOperator instanceof LogicalOperator.And)
			builder.append (" and ");
		else if (logicalOperator instanceof LogicalOperator.Or)
			builder.append (" or ");
		else
			assert false;
		logicalOperator.getRight().accept (this);
		builder.append (")");
	}

	public void visit (RelationalOperator relationalOperator)
	{
		builder.append ("(");
		relationalOperator.getLeft().accept (this);
		if (relationalOperator instanceof RelationalOperator.Eq)
			builder.append (" == ");
		else if (relationalOperator instanceof RelationalOperator.Neq)
			builder.append (" != ");
		else if (relationalOperator instanceof RelationalOperator.Gt)
			builder.append (" > ");
		else if (relationalOperator instanceof RelationalOperator.Ge)
			builder.append (" >= ");
		else if (relationalOperator instanceof RelationalOperator.Lt)
			builder.append (" < ");
		else if (relationalOperator instanceof RelationalOperator.Le)
			builder.append (" <= ");
		else
			assert false;
		relationalOperator.getRight().accept (this);
		builder.append (")");
	}

	public void visit (ArithmeticOperator arithmeticOperator)
	{
		builder.append ("[");
		arithmeticOperator.getLeft().accept (this);
		if (arithmeticOperator instanceof ArithmeticOperator.Power)
			builder.append (" ^ ");
		else if (arithmeticOperator instanceof ArithmeticOperator.Div)
			builder.append (" / ");
		else if (arithmeticOperator instanceof ArithmeticOperator.Rem)
			builder.append (" % ");
		else if (arithmeticOperator instanceof ArithmeticOperator.Mult)
			builder.append (" * ");
		else if (arithmeticOperator instanceof ArithmeticOperator.Minus)
			builder.append (" - ");
		else if (arithmeticOperator instanceof ArithmeticOperator.Plus)
			builder.append (" + ");
		else
			assert false;
		arithmeticOperator.getRight().accept (this);
		builder.append ("]");
	}

	public void print (Expression expression) throws IOException
	{
		this.setExpression(expression);
		builder = new StringBuilder ();
		expression.accept (this);
		writer.write (builder.toString ());
	}

	/**
	 * @param expression the expression to set
	 */
	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	/**
	 * @return the expression
	 */
	public Expression getExpression() {
		return expression;
	}
}