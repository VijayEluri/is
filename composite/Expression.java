package ecv.composite;

/* Expression is our Composite type
 * it can be composed by more expressions
 */

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