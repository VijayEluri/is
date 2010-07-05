package ecv.composite;

public class Identifier extends Expression
{
	private String name;

	public Identifier (String name)
	{
		this.name = name;
	}

	public String getName ()
	{
		return name;
	}

	public boolean equals (Object expression)
	{
		if (!(expression instanceof Identifier))
			return false;
		return ((Identifier)expression).name.equals (name);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit (this);
	}
}