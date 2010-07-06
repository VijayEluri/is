package ecv;

import java.util.Set;

public interface Context
{
	public int getValue (String identifier);
	public Set<String> identifiersSet ();
}