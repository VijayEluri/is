package ecv.composite;


public class Number extends Expression
{
	private int value;

	public Number (int value)
	{
		if (value < 0)
			throw new RuntimeException ("Not a positive number '"+value+"'");
		this.value = value;
	}

	public int getValue ()
	{
		return value;
	}

	public void accept (Visitor visitor)
	{
		visitor.visit (this);
	}

	public boolean equals (Object expression)
	{
		if (!(expression instanceof Number))
			return false;
		return ((Number)expression).value == value;
	}
}