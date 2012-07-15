package ecv;
import static ecv.StreamTokenizerAdapter.TokenType.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;

public class StreamTokenizerAdapter extends StreamTokenizer
{
	private TokenType lastToken;
	private String tokenContent;

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

	/*
	 * The constructor will adapt the settings of the given StreamTokenizer to
	 * fit the ECV specification.
	 */
	public StreamTokenizerAdapter(Reader r)
	{
		super(r);
		resetSyntax ();
		eolIsSignificant (false);
		lowerCaseMode (false);
		parseNumbers ();
		ordinaryChar ('(');
		ordinaryChar (')');
		ordinaryChar ('[');
		ordinaryChar (']');
		ordinaryChar ('!');
		ordinaryChar ('=');
		ordinaryChar ('<');
		ordinaryChar ('>');
		ordinaryChar ('^');
		ordinaryChar ('*');
		ordinaryChar ('/');
		ordinaryChar ('%');
		ordinaryChar ('-');
		ordinaryChar ('+');
		wordChars ('a', 'z');
		wordChars ('A', 'Z');
		whitespaceChars ('\0', ' ');
	}

	public TokenType nextTokenType () throws IOException
	{
		switch (nextToken ())
		{
		case TT_EOF:
			lastToken = TOKEN_EOF;
			break;
		case TT_NUMBER:
			lastToken = TOKEN_NUM;
			break;
		case TT_WORD:
			String s = sval;
			if (s.equals ("and"))
				lastToken = TOKEN_AND;
			else if (s.equals ("or"))
				lastToken = TOKEN_OR;
			else if (s.equals ("not"))
				lastToken = TOKEN_NOT;
			else
				lastToken = TOKEN_ID;
			break;

		case '(':
			lastToken = TOKEN_OPAR;
			break;
		case ')':
			lastToken = TOKEN_CPAR;
			break;
		case '[':
			lastToken = TOKEN_OSB;
			break;
		case ']':
			lastToken = TOKEN_CSB;
			break;
		case '=':
			if (nextToken () != '=')
				throw new InvalidExpressionError ("Invalid character =. Expected ==");
			lastToken = TOKEN_EQ;
			break;
		case '!':
			if (nextToken () != '=')
			{
				pushBack ();
				lastToken = TOKEN_NOT;
			}
			else
				lastToken = TOKEN_NE;
			break;
		case '>':
			if (nextToken () != '=')
			{
				pushBack ();
				lastToken = TOKEN_GT;
			}
			else
				lastToken = TOKEN_GE;
			break;
		case '<':
			if (nextToken () != '=')
			{
				pushBack ();
				lastToken = TOKEN_LT;
			}
			else
				lastToken = TOKEN_LE;
			break;
		case '^':
			lastToken = TOKEN_POW;
			break;
		case '/':
			lastToken = TOKEN_DIV;
			break;
		case '%':
			lastToken = TOKEN_MOD;
			break;
		case '*':
			lastToken = TOKEN_MUL;
			break;
		case '-':
			lastToken = TOKEN_SUB;
			break;
		case '+':
			lastToken = TOKEN_ADD;
			break;
		case '&':
			if (nextToken () != '&')
				throw new InvalidExpressionError ("Invalid character &. Expected &&");
			lastToken = TOKEN_AND;
			break;
		case '|':
			if (nextToken () != '|')
				throw new InvalidExpressionError ("Invalid character |. Expected ||");
			lastToken = TOKEN_OR;
			break;
		default:
			throw new InvalidExpressionError ("Unexpected character: "+(char)ttype);
		}
		return lastToken;
	}

	/*
	 * Caller must ensure to call nextToken() at least one time before calling this function.
	 */
	public TokenType lastToken ()
	{
		assert ttype != -4;
		return lastToken;
	}

	/*
	 * Caller must ensure the last token is TOKEN_ID.
	 */
	public String getString ()
	{
		assert lastToken == TOKEN_ID;
		return sval;
	}

	/*
	 * Caller must ensure the last token is TOKEN_NUM.
	 */
	public int getInt ()
	{
		assert lastToken == TOKEN_NUM;
		return (int)nval;
	}

	/**
	 * @param tokenContent the tokenContent to set
	 */
	public void setTokenContent(String tokenContent) {
		this.tokenContent = tokenContent;
	}

	/**
	 * @return the tokenContent
	 */
	public String getTokenContent() {
		return tokenContent;
	}
}
