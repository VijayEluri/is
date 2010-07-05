package ecv.composite;

public abstract class RelationalOperator extends Operator
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
		RelationalOperator op = (RelationalOperator)expression;
		return left.equals (op.left) && right.equals (op.right);
	}

	public static class Eq extends RelationalOperator
	{
		public void accept (Visitor visitor)
		{
			visitor.visit (this);
		}
	}

	public static class Neq extends RelationalOperator
	{
		public void accept (Visitor visitor)
		{
			visitor.visit (this);
		}
	}

	public static class Gt extends RelationalOperator
	{
		public void accept (Visitor visitor)
		{
			visitor.visit (this);
		}
	}

	public static class Ge extends RelationalOperator
	{
		public void accept (Visitor visitor)
		{
			visitor.visit (this);
		}
	}

	public static class Lt extends RelationalOperator
	{
		public void accept (Visitor visitor)
		{
			visitor.visit (this);
		}
	}

	public static class Le extends RelationalOperator
	{
		public void accept (Visitor visitor)
		{
			visitor.visit (this);
		}
	}
}