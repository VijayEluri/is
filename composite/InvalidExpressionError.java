package ecv.composite;

@SuppressWarnings ("serial")
public class InvalidExpressionError extends RuntimeException
{
	public InvalidExpressionError (String message)
	{
		super (message);
	}
}