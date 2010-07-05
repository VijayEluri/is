package ecv.composite;

public abstract class ArithmeticOperator extends Operator
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

	public boolean equals (Object expression)
	{
		if (!getClass().isInstance (expression))
			return false;
		ArithmeticOperator op = (ArithmeticOperator)expression;
		return left.equals (op.left) && right.equals (op.right);
	}

	public static class Power extends ArithmeticOperator {
		@Override
		public void accept(Visitor visitor) {
			visitor.visit (this);
		}
	}

	public static class Div extends ArithmeticOperator {
		@Override
		public void accept(Visitor visitor) {
			visitor.visit (this);
		}
	}

	public static class Rem extends ArithmeticOperator {
		@Override
		public void accept(Visitor visitor) {
			visitor.visit (this);
		}
	}

	public static class Mult extends ArithmeticOperator {
		@Override
		public void accept(Visitor visitor) {
			visitor.visit (this);
		}
	}

	public static class Minus extends ArithmeticOperator {
		@Override
		public void accept(Visitor visitor) {
			visitor.visit (this);
		}
	}

	public static class Plus extends ArithmeticOperator {
		@Override
		public void accept(Visitor visitor) {
			visitor.visit (this);
		}
	}
}