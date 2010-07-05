package ecv.composite;

public abstract class Operator extends Expression
{
	public abstract Expression getLeft ();
	public abstract Expression getRight ();
	public abstract void setLeft (Expression expression);
	public abstract void setRight (Expression expression);
}