package ecv;

import ecv.Operator.ArithmeticOperator;
import ecv.Operator.LogicalOperator;
import ecv.Operator.RelationalOperator;

public class EvaluatingVisitor extends Visitor
{
	private Context context;
	private boolean boolValue;
	private int intValue;

	public EvaluatingVisitor (Context context)
	{
		this.context = context;
	}

	public void visit (Number number)
	{
		intValue = number.getValue ();
	}

	public void visit (Identifier identifier)
	{
		intValue = context.getValue (identifier.getName ());
	}

	public void visit (LogicalOperator logicalOperator)
	{
		logicalOperator.getLeft().accept (this);
		boolean leftValue = boolValue;
		switch(logicalOperator.getType()) {
		case OP_AND:
			if (!leftValue) // Corto circuito and
				boolValue = false;
			else
				logicalOperator.getRight().accept (this);
			break;
		case OP_OR:
			if (leftValue) // Corto circuito or
				boolValue = true;
			else
				logicalOperator.getRight().accept (this);
			break;
		case OP_NOT:
			boolValue = !leftValue;
		default: assert false;
		}
	}

	public void visit (RelationalOperator relationalOperator)
	{
		relationalOperator.getLeft().accept (this);
		int leftValue = intValue;
		relationalOperator.getRight().accept (this);
		int rightValue = intValue;
		switch(relationalOperator.getType()) {
		case OP_EQ:	boolValue = leftValue == rightValue; break;
		case OP_NE:	boolValue = leftValue != rightValue; break;
		case OP_GT:	boolValue = leftValue > rightValue; break;
		case OP_GE:	boolValue = leftValue >= rightValue; break;
		case OP_LT:	boolValue = leftValue < rightValue; break;
		case OP_LE:	boolValue = leftValue <= rightValue; break;
		default:		assert false;
		}
	}

	public void visit (ArithmeticOperator arithmeticOperator)
	{
		arithmeticOperator.getLeft().accept (this);
		int leftValue = intValue;
		arithmeticOperator.getRight().accept (this);
		int rightValue = intValue;
		switch(arithmeticOperator.getType()) {
		case OP_POW:	intValue = (int)Math.pow (leftValue, rightValue); break;
		case OP_DIV:	intValue = leftValue / rightValue; break;
		case OP_MOD:	intValue = leftValue % rightValue; break;
		case OP_MUL:	intValue = leftValue * rightValue; break;
		case OP_ADD:	intValue = leftValue + rightValue; break;
		case OP_SUB:	intValue = leftValue - rightValue; break;
		default: 		assert false;
		}
	}

	public boolean valueOf (Expression expression)
	{
		expression.accept (this);
		return boolValue;
	}
}