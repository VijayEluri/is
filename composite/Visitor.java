package ecv.composite;

public interface Visitor
{
//	public void visit (Expression expression);
	public void visit (Number number);
	public void visit (Identifier identifier);
	public void visit (ArithmeticOperator arithmeticOperator);
	public void visit (LogicalOperator logicalOperator);
	public void visit (RelationalOperator relationalOperator);
}