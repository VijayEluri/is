package ecv.composite;
import static ecv.composite.Lexer.TokenType.TOKEN_AND;
import static ecv.composite.Lexer.TokenType.TOKEN_CPAR;
import static ecv.composite.Lexer.TokenType.TOKEN_CSB;
import static ecv.composite.Lexer.TokenType.TOKEN_DIV;
import static ecv.composite.Lexer.TokenType.TOKEN_EOF;
import static ecv.composite.Lexer.TokenType.TOKEN_EQ;
import static ecv.composite.Lexer.TokenType.TOKEN_GE;
import static ecv.composite.Lexer.TokenType.TOKEN_GT;
import static ecv.composite.Lexer.TokenType.TOKEN_ID;
import static ecv.composite.Lexer.TokenType.TOKEN_LE;
import static ecv.composite.Lexer.TokenType.TOKEN_LT;
import static ecv.composite.Lexer.TokenType.TOKEN_SUB;
import static ecv.composite.Lexer.TokenType.TOKEN_MUL;
import static ecv.composite.Lexer.TokenType.TOKEN_NE;
import static ecv.composite.Lexer.TokenType.TOKEN_NOT;
import static ecv.composite.Lexer.TokenType.TOKEN_NUM;
import static ecv.composite.Lexer.TokenType.TOKEN_OPAR;
import static ecv.composite.Lexer.TokenType.TOKEN_OSB;
import static ecv.composite.Lexer.TokenType.TOKEN_OR;
import static ecv.composite.Lexer.TokenType.TOKEN_ADD;
import static ecv.composite.Lexer.TokenType.TOKEN_POW;
import static ecv.composite.Lexer.TokenType.TOKEN_MOD;

import java.io.IOException;
import java.io.StreamTokenizer;

public class StreamTokenizerAdapter implements Lexer
{
	private StreamTokenizer streamTokenizer;
	private TokenType lastToken;
	private String tokenContent;

	/*
	 * The constructor will adapt the settings of the given StreamTokenizer to
	 * fit the ECV specification.
	 */
	public StreamTokenizerAdapter (StreamTokenizer streamTokenizer)
	{
		this.streamTokenizer = streamTokenizer;
		streamTokenizer.resetSyntax ();
		streamTokenizer.eolIsSignificant (false);
		streamTokenizer.lowerCaseMode (false);
		streamTokenizer.parseNumbers ();
		streamTokenizer.ordinaryChar ('(');
		streamTokenizer.ordinaryChar (')');
		streamTokenizer.ordinaryChar ('[');
		streamTokenizer.ordinaryChar (']');
		streamTokenizer.ordinaryChar ('!');
		streamTokenizer.ordinaryChar ('=');
		streamTokenizer.ordinaryChar ('<');
		streamTokenizer.ordinaryChar ('>');
		streamTokenizer.ordinaryChar ('^');
		streamTokenizer.ordinaryChar ('*');
		streamTokenizer.ordinaryChar ('/');
		streamTokenizer.ordinaryChar ('%');
		streamTokenizer.ordinaryChar ('-');
		streamTokenizer.ordinaryChar ('+');
		streamTokenizer.wordChars ('a', 'z');
		streamTokenizer.wordChars ('A', 'Z');
		streamTokenizer.whitespaceChars ('\0', ' ');
	}

	public TokenType nextToken () throws IOException
	{
		switch (streamTokenizer.nextToken ())
		{
		case StreamTokenizer.TT_EOF:
			lastToken = TOKEN_EOF;
			break;
		case StreamTokenizer.TT_NUMBER:
			lastToken = TOKEN_NUM;
			break;
		case StreamTokenizer.TT_WORD:
			String s = streamTokenizer.sval;
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
			if (streamTokenizer.nextToken () != '=')
				throw new InvalidExpressionError ("Invalid character =. Expected ==");
			lastToken = TOKEN_EQ;
			break;
		case '!':
			if (streamTokenizer.nextToken () != '=')
			{
				streamTokenizer.pushBack ();
				lastToken = TOKEN_NOT;
			}
			else
				lastToken = TOKEN_NE;
			break;
		case '>':
			if (streamTokenizer.nextToken () != '=')
			{
				streamTokenizer.pushBack ();
				lastToken = TOKEN_GT;
			}
			else
				lastToken = TOKEN_GE;
			break;
		case '<':
			if (streamTokenizer.nextToken () != '=')
			{
				streamTokenizer.pushBack ();
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
			if (streamTokenizer.nextToken () != '&')
				throw new InvalidExpressionError ("Invalid character &. Expected &&");
			lastToken = TOKEN_AND;
			break;
		case '|':
			if (streamTokenizer.nextToken () != '|')
				throw new InvalidExpressionError ("Invalid character |. Expected ||");
			lastToken = TOKEN_OR;
			break;
		default:
			throw new InvalidExpressionError ("Unexpected character: "+(char)streamTokenizer.ttype);
		}
		return lastToken;
	}

	/*
	 * Caller must ensure to call nextToken() at least one time before calling this function.
	 */
	public TokenType lastToken ()
	{
		assert streamTokenizer.ttype != -4;
		return lastToken;
	}

	/*
	 * Caller must ensure the last token is TOKEN_ID.
	 */
	public String getString ()
	{
		assert lastToken == TOKEN_ID;
		return streamTokenizer.sval;
	}

	/*
	 * Caller must ensure the last token is TOKEN_NUM.
	 */
	public int getInt ()
	{
		assert lastToken == TOKEN_NUM;
		return (int)streamTokenizer.nval;
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