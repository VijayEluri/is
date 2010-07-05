package ecv.visitor;

import ecv.composite.ArithmeticOperator;
import ecv.composite.Expression;
import ecv.composite.Identifier;
import ecv.composite.LogicalOperator;
import ecv.composite.RelationalOperator;
import ecv.composite.Visitor;



public class EvaluatingVisitor implements Visitor
{
	private Context context;
	private boolean boolValue;
	private int intValue;

	public EvaluatingVisitor (Context context)
	{
		this.context = context;
	}

	public void visit (ecv.composite.Number number)
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
		if (logicalOperator instanceof LogicalOperator.Not)
			boolValue = !leftValue;
		else if (logicalOperator instanceof LogicalOperator.And)
			if (!leftValue) // Corto circuito and
				boolValue = false;
			else
				logicalOperator.getRight().accept (this);
		else if (logicalOperator instanceof LogicalOperator.Or)
			if (leftValue) // Corto circuito or
				boolValue = true;
			else
				logicalOperator.getRight().accept (this);
		else
			assert false;
	}

	public void visit (RelationalOperator relationalOperator)
	{
		relationalOperator.getLeft().accept (this);
		int leftValue = intValue;
		relationalOperator.getRight().accept (this);
		int rightValue = intValue;
		if (relationalOperator instanceof RelationalOperator.Eq)
			boolValue = leftValue == rightValue;
		else if (relationalOperator instanceof RelationalOperator.Neq)
			boolValue = leftValue != rightValue;
		else if (relationalOperator instanceof RelationalOperator.Gt)
			boolValue = leftValue > rightValue;
		else if (relationalOperator instanceof RelationalOperator.Ge)
			boolValue = leftValue >= rightValue;
		else if (relationalOperator instanceof RelationalOperator.Lt)
			boolValue = leftValue < rightValue;
		else if (relationalOperator instanceof RelationalOperator.Le)
			boolValue = leftValue <= rightValue;
		else
			assert false;
	}

	public void visit (ArithmeticOperator arithmeticOperator)
	{
		arithmeticOperator.getLeft().accept (this);
		int leftValue = intValue;
		arithmeticOperator.getRight().accept (this);
		int rightValue = intValue;
		if (arithmeticOperator instanceof ArithmeticOperator.Power)
			intValue = (int)Math.pow (leftValue, rightValue);
		else if (arithmeticOperator instanceof ArithmeticOperator.Div)
			intValue = leftValue / rightValue;
		else if (arithmeticOperator instanceof ArithmeticOperator.Rem)
			intValue = leftValue % rightValue;
		else if (arithmeticOperator instanceof ArithmeticOperator.Mult)
			intValue = leftValue * rightValue;
		else if (arithmeticOperator instanceof ArithmeticOperator.Minus)
			intValue = leftValue - rightValue;
		else if (arithmeticOperator instanceof ArithmeticOperator.Plus)
			intValue = leftValue + rightValue;
		else
			assert false;
	}

	public boolean valueOf (Expression expression)
	{
		expression.accept (this);
		return boolValue;
	}
}