package ecv;

import java.io.Reader;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

import ecv.StreamTokenizerAdapter;

public class PropertiesContext implements Context
{
	private Hashtable<String,Integer> context = new Hashtable<String,Integer>();

	/*
	 * If duplicate entries are detected, old values are replaced with new values
	 */
	@SuppressWarnings("unchecked")
	public void load (Properties properties)
	{
		for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements();)
		{
			// Key validation
			String key = (String)e.nextElement();
			Reader reader = new StringReader (key);
			StreamTokenizerAdapter sta = new StreamTokenizerAdapter (reader);
			try
			{
				if (sta.nextTokenType () != StreamTokenizerAdapter.TokenType.TOKEN_ID)
					throw new RuntimeException ("Invalid property name '"+key+"' is not a valid identifier");
			}
			catch (Exception ex)
			{
				throw new RuntimeException ("An error occurred while parsing property name '"+key+"'");
			}

			// Property value
			String property = properties.getProperty (key, "0");
			int value = 0;
			try
			{
				value = Integer.parseInt (property);
			}
			catch (NumberFormatException ex)
			{
				throw new RuntimeException ("Invalid property '"+key+"="+property+"'; value is not a valid positive decimal integer number.");
			}

			if (value < 0)
				throw new RuntimeException ("Invalid property '"+key+"="+property+"'; number can't be negative");

			context.put (key, value);
		}
	}

	public int getValue (String identifier)
	{
		Integer number = context.get (identifier);
		return number != null ? number.intValue () : 0;
	}

	public Set<String> identifiersSet ()
	{
		return context.keySet ();
	}
}