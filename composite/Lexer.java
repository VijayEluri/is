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
		TOKEN_OPAR1 ("["), // "["
		TOKEN_CPAR1 ("]"), // "]"
		TOKEN_EQ ("=="), // "=="
		TOKEN_NEQ ("!="), // "!="
		TOKEN_GT (">"), // ">"
		TOKEN_GE (">="), // ">="
		TOKEN_LT ("<"), // "<"
		TOKEN_LE ("<="), // "<="
		TOKEN_POWER ("^"), // "^"
		TOKEN_DIV ("/"), // "/"
		TOKEN_REM ("%"), // "%"
		TOKEN_MULT ("*"), // "*"
		TOKEN_MINUS ("-"), // "-"
		TOKEN_PLUS ("+"), // "+"
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