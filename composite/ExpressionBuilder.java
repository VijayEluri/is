package ecv.composite;

public class ExpressionBuilder
{
	public static enum OperatorType
	{
		OPERATOR_EQ,
		OPERATOR_NEQ,
		OPERATOR_GT,
		OPERATOR_GE,
		OPERATOR_LT,
		OPERATOR_LE,
		OPERATOR_POWER,
		OPERATOR_DIV,
		OPERATOR_REM,
		OPERATOR_MULT,
		OPERATOR_MINUS,
		OPERATOR_PLUS,
		OPERATOR_AND,
		OPERATOR_OR,
		OPERATOR_NOT,
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
		case OPERATOR_EQ:
			return new RelationalOperator.Eq ();
		case OPERATOR_NEQ:
			return new RelationalOperator.Neq ();
		case OPERATOR_GT:
			return new RelationalOperator.Gt ();
		case OPERATOR_GE:
			return new RelationalOperator.Ge ();
		case OPERATOR_LT:
			return new RelationalOperator.Lt ();
		case OPERATOR_LE:
			return new RelationalOperator.Le ();
		case OPERATOR_POWER:
			return new ArithmeticOperator.Power ();
		case OPERATOR_DIV:
			return new ArithmeticOperator.Div ();
		case OPERATOR_REM:
			return new ArithmeticOperator.Rem ();
		case OPERATOR_MULT:
			return new ArithmeticOperator.Mult ();
		case OPERATOR_PLUS:
			return new ArithmeticOperator.Plus ();
		case OPERATOR_MINUS:
			return new ArithmeticOperator.Minus ();
		case OPERATOR_AND:
			return new LogicalOperator.And ();
		case OPERATOR_OR:
			return new LogicalOperator.Or ();
		case OPERATOR_NOT:
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