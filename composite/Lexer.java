package ecv.composite;
import java.io.IOException;

public interface Lexer
{
	public static enum TokenType
	{
		TOKEN_EOF ("end of file"),
		TOKEN_ID ("identifier"), // ("A" | ... | "Z" | "a" | ...| "z") [("A"| ... | "Z" | "a" | ...| "z" | "0" | ... | "9")]*
		TOKEN_NUM ("number"), // ("0" | ... | "9") [("0" | ... | "9")]*
		TOKEN_OPAR ("("), // "("
		TOKEN_CPAR (")"), // ")"
		TOKEN_OSB ("["), // "["
		TOKEN_CSB ("]"), // "]"
		TOKEN_EQ ("=="), // "=="
		TOKEN_NE ("!="), // "!="
		TOKEN_GT (">"), // ">"
		TOKEN_GE (">="), // ">="
		TOKEN_LT ("<"), // "<"
		TOKEN_LE ("<="), // "<="
		TOKEN_POW ("^"), // "^"
		TOKEN_DIV ("/"), // "/"
		TOKEN_MOD ("%"), // "%"
		TOKEN_MUL ("*"), // "*"
		TOKEN_SUB ("-"), // "-"
		TOKEN_ADD ("+"), // "+"
		TOKEN_AND ("and (&&)"), // "and" o "&&"
		TOKEN_OR ("or (||)"), // "or" o "||"
		TOKEN_NOT ("not (!)"); // "not" o "!"

		private final String name;
		private TokenType (String name)
		{
			this.name = name;
		}

		public String toString ()
		{
			return name;
		}

	}

	public TokenType nextToken () throws IOException;
	public TokenType lastToken ();
	public String getString (); // for all tokens except NUM and EOF
	public int getInt (); // for NUM tokens
}