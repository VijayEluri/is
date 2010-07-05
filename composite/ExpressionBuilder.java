package ecv.composite;

public class ExpressionBuilder
{
	public static enum OperatorType
	{
		OP_EQ,
		OP_NE,
		OP_GT,
		OP_GE,
		OP_LT,
		OP_LE,
		OP_POW,
		OP_DIV,
		OP_MOD,
		OP_MUL,
		OP_SUB,
		OP_ADD,
		OP_AND,
		OP_OR,
		OP_NOT,
	}

	private Expression root;

	public Expression getResult ()
	{
		assert root != null && root instanceof Expression;
		return (Expression)root;
	}

	private Operator createOperator (OperatorType operatorType)
	{
		switch (operatorType)
		{
		case OP_EQ:
			return new RelationalOperator.Eq ();
		case OP_NE:
			return new RelationalOperator.Neq ();
		case OP_GT:
			return new RelationalOperator.Gt ();
		case OP_GE:
			return new RelationalOperator.Ge ();
		case OP_LT:
			return new RelationalOperator.Lt ();
		case OP_LE:
			return new RelationalOperator.Le ();
		case OP_POW:
			return new ArithmeticOperator.Power ();
		case OP_DIV:
			return new ArithmeticOperator.Div ();
		case OP_MOD:
			return new ArithmeticOperator.Rem ();
		case OP_MUL:
			return new ArithmeticOperator.Mult ();
		case OP_ADD:
			return new ArithmeticOperator.Plus ();
		case OP_SUB:
			return new ArithmeticOperator.Minus ();
		case OP_AND:
			return new LogicalOperator.And ();
		case OP_OR:
			return new LogicalOperator.Or ();
		case OP_NOT:
			return new LogicalOperator.Not ();
		default:
			assert false;
			return null;
		}   
	}

	public void buildOperator (OperatorType operatorType)
	{
		assert root != null;
		Operator operator = createOperator (operatorType);
		operator.setParent (root.getParent ());
		operator.setLeft (root);
		root.setParent (operator);
		root = operator;
	}

	public void endOperator ()
	{
		assert root != null;
		Operator parent = (Operator)root.getParent ();
		assert parent.getRight () == null;
		parent.setRight (root);
		root = parent;
	}

	public void buildOperand (int number)
	{
		assert number >= 0 && (root == null || root instanceof Operator);
		Expression operand = new Number (number);
		operand.setParent (root);
		root = operand;
	}

	public void buildOperand (String identifier)
	{
		assert identifier != null && (root == null || root instanceof Operator);
		Expression operand = new Identifier (identifier);
		operand.setParent (root);
		root = operand;
	}
}