package ecv.composite;

public abstract class LogicalOperator extends Operator
{
	private Expression left, right;

	public Expression getLeft ()
	{
		return left;
	}

	public Expression getRight ()
	{
		return right;
	}

	public void setLeft (Expression Expression)
	{
		left = Expression;
	}

	public void setRight (Expression Expression)
	{
		right = Expression;
	}

	public void accept (Visitor visitor)
	{
		visitor.visit (this);
	}

	public boolean equals (Object expression)
	{
		if (!getClass().isInstance (expression))
			return false;
		LogicalOperator op = (LogicalOperator)expression;
		return left.equals (op.left) && right.equals (op.right);
	}

	public static class Or extends LogicalOperator
	{
		public void accept (Visitor visitor)
		{
			visitor.visit (this);
		}
	}

	public static class And extends LogicalOperator
	{
		public void accept (Visitor visitor)
		{
			visitor.visit (this);
		}
	}

	public static class Not extends LogicalOperator
	{
		public void accept (Visitor visitor)
		{
			visitor.visit (this);
		}

		public void setRight (Expression Expression)
		{
			throw new RuntimeException ("Unary operator Not only have left operand");
		}
	}
}