package ecv.visitor;

import java.io.IOException;
import java.io.Writer;

import ecv.composite.Expression;
import ecv.composite.Identifier;
import ecv.composite.Visitor;
import ecv.composite.Operator.ArithmeticOperator;
import ecv.composite.Operator.LogicalOperator;
import ecv.composite.Operator.RelationalOperator;

public class InfixPrinterVisitor extends Visitor
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
		switch(logicalOperator.getType()) {
		case OP_AND:
			builder.append ("(");
			logicalOperator.getLeft().accept (this);
			builder.append (" and ");
			break;
		case OP_OR:
			builder.append ("(");
			logicalOperator.getLeft().accept (this);
			builder.append (" or ");
			break;
		case OP_NOT:
			builder.append ("not ");
			logicalOperator.getLeft().accept (this);
			return;
		default: assert false;
		}
		logicalOperator.getRight().accept (this);
		builder.append (")");
	}

	public void visit (RelationalOperator relationalOperator)
	{
		builder.append ("(");
		relationalOperator.getLeft().accept (this);
		switch(relationalOperator.getType()) {
		case OP_EQ:	builder.append (" == "); break;
		case OP_NE:	builder.append (" != "); break;
		case OP_GT:	builder.append (" > "); break;
		case OP_GE:	builder.append (" >= "); break;
		case OP_LT:	builder.append (" < "); break;
		case OP_LE:	builder.append (" <= "); break;
		default:		assert false;
		}
		relationalOperator.getRight().accept (this);
		builder.append (")");
	}

	public void visit (ArithmeticOperator arithmeticOperator)
	{
		builder.append ("[");
		arithmeticOperator.getLeft().accept (this);
		switch(arithmeticOperator.getType()) {
		case OP_POW:	builder.append (" ^ "); break;
		case OP_DIV:	builder.append (" / "); break;
		case OP_MOD:	builder.append (" % "); break;
		case OP_MUL:	builder.append (" * "); break;
		case OP_ADD:	builder.append (" + "); break;
		case OP_SUB:	builder.append (" - "); break;
		default: 		assert false;
		}
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