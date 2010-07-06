package ecv;

import java.io.IOException;
import java.io.Writer;

import ecv.Operator.ArithmeticOperator;
import ecv.Operator.LogicalOperator;
import ecv.Operator.RelationalOperator;

public class PostfixPrinterVisitor extends Visitor
{
	private Writer writer;
	private StringBuilder builder;

	public PostfixPrinterVisitor (Writer writer)
	{
		this.writer = writer;
	}

	public void visit (ecv.Number number)
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
		switch(logicalOperator.getType()) {
		case OP_AND:
			builder.append ("and ");
			break;
		case OP_OR:
			builder.append ("or ");
			break;
		case OP_NOT:
			builder.append ("not ");
		default: assert false;
		}
	}

	public void visit (RelationalOperator relationalOperator)
	{
		relationalOperator.getLeft().accept (this);
		relationalOperator.getRight().accept (this);
		switch(relationalOperator.getType()) {
		case OP_EQ:	builder.append ("== "); break;
		case OP_NE:	builder.append ("!= "); break;
		case OP_GT:	builder.append ("> "); break;
		case OP_GE:	builder.append (">= "); break;
		case OP_LT:	builder.append ("< "); break;
		case OP_LE:	builder.append ("<= "); break;
		default:		assert false;
		}
	}

	public void visit (ArithmeticOperator arithmeticOperator)
	{
		arithmeticOperator.getLeft().accept (this);
		arithmeticOperator.getRight().accept (this);
		switch(arithmeticOperator.getType()) {
		case OP_POW:	builder.append ("^ "); break;
		case OP_DIV:	builder.append ("/ "); break;
		case OP_MOD:	builder.append ("% "); break;
		case OP_MUL:	builder.append ("* "); break;
		case OP_ADD:	builder.append ("+ "); break;
		case OP_SUB:	builder.append ("- "); break;
		default: 		assert false;
		}
	}

	public void print (Expression expression) throws IOException
	{
		builder = new StringBuilder ();
		expression.accept (this);
		// trim trailing whitespace
		writer.write (builder.toString().trim ());
	}
}