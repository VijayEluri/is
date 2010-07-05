package ecv.composite;

public abstract class Expression
{
	private Expression parent;

	public abstract void accept (Visitor visitor);
/*	public void accept (Visitor visitor)
	{
		visitor.visit (this);
	}*/

	public Expression getParent ()
	{
		return parent;
	}

	public void setParent (Expression expression)
	{
		parent = expression;
	}
}