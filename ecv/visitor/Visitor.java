package ecv.visitor;

import ecv.operator.Expression;
import ecv.operator.Identifier;
import ecv.operator.Number;
import ecv.operator.Operator;
import ecv.operator.Operator.ArithmeticOperator;
import ecv.operator.Operator.LogicalOperator;
import ecv.operator.Operator.RelationalOperator;

public abstract class Visitor
{
	public abstract void visit (Number number);
	public abstract void visit (Identifier identifier);
	public abstract void visit (ArithmeticOperator arithmeticOperator);
	public abstract void visit (LogicalOperator logicalOperator);
	public abstract void visit (RelationalOperator relationalOperator);

	public void visit (Expression expression) {
		if(expression instanceof Number)
			visit((Number)expression);
		else if(expression instanceof Identifier)
			visit((Identifier)expression);
		else if(expression instanceof ArithmeticOperator)
			visit((ArithmeticOperator)expression);
		else if(expression instanceof LogicalOperator)
			visit((LogicalOperator)expression);
		else if(expression instanceof Operator.RelationalOperator)
			visit((RelationalOperator)expression);
		else
			assert false;
	};
}
