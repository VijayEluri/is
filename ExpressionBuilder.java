package ecv;

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
		return root;
	}

	private Operator createOperator (OperatorType operatorType)
	{
		switch (operatorType)
		{
		case OP_EQ:
		case OP_NE:
		case OP_GT:
		case OP_GE:
		case OP_LT:
		case OP_LE:
			return new Operator.RelationalOperator(operatorType);
		case OP_POW:
		case OP_DIV:
		case OP_MOD:
		case OP_MUL:
		case OP_ADD:
		case OP_SUB:
			return new Operator.ArithmeticOperator(operatorType);
		case OP_AND:
		case OP_OR:
		case OP_NOT:
			return new Operator.LogicalOperator(operatorType);
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