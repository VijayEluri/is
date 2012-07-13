package ecv.operator;

import ecv.visitor.Visitor;

public abstract class Expression
{
	private Expression parent;

	public void accept (Visitor visitor)
	{
		visitor.visit (this);
	}

	public Expression getParent ()
	{
		return parent;
	}

	public void setParent (Expression expression)
	{
		parent = expression;
	}
}
