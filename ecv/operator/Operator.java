package ecv.operator;

import ecv.operator.ExpressionBuilder.OperatorType;

public abstract class Operator extends Expression
{
	protected Expression left, right;
	protected OperatorType type;

	public Operator(OperatorType t) {
		type = t;
	}

	public OperatorType getType () {
		return type;
	}

	public Expression getLeft () {
		return left;
	}

	public Expression getRight () {
		return right;
	}

	public void setLeft (Expression Expression) {
		left = Expression;
	}

	public void setRight (Expression Expression) {
		right = Expression;
	}

	public boolean equals (Object obj)
	{
		Operator op = (Operator)obj;
		return obj != null && obj instanceof Operator && left.equals(op.left) && right.equals(op.right);
	}

	public static class ArithmeticOperator extends Operator
	{
		public ArithmeticOperator(OperatorType t) {
			super(t);
		}
	}

	public static class LogicalOperator extends Operator
	{
		public LogicalOperator(OperatorType t) {
			super(t);
		}

		public void setRight (Expression expression)
		{
			if(type == OperatorType.OP_NOT)
				throw new RuntimeException ("Unary operator Not only have left operand");
			else
				super.setRight(expression);
		}

	}

	public static class RelationalOperator extends Operator
	{
		public RelationalOperator(OperatorType t) {
			super(t);
		}
	}
}
