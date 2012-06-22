package ecv;


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
}
