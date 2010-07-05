package ecv.composite;

import static ecv.composite.ExpressionBuilder.OperatorType.*;
import static ecv.composite.Lexer.TokenType.*;

import java.io.IOException;

import ecv.composite.Lexer.TokenType;

public class Parser
{
	private Lexer lexer;
	private ExpressionBuilder builder;

	public Parser (Lexer lexer, ExpressionBuilder builder)
	{
		this.lexer = lexer;
		this.builder = builder;
	}

	public void parse () throws IOException
	{
		lexer.nextToken ();
		cond ();
		if (lexer.lastToken () != TOKEN_EOF)
			throw new InvalidExpressionError ("Expected end of expression. Got '"+lexer.lastToken()+"' instead.");
	}

	@SuppressWarnings("unused")
	private static boolean isRelop (TokenType token)
	{
		switch (token)
		{
		case TOKEN_EQ:
		case TOKEN_NE:
		case TOKEN_LT:
		case TOKEN_LE:
		case TOKEN_GT:
		case TOKEN_GE:
			return true;
		default:
			return false;
		}
	}

	@SuppressWarnings("unused")
	private static boolean isAndOr (TokenType token)
	{
		return token == TOKEN_AND || token == TOKEN_OR;
	}

	@SuppressWarnings("unused")
	private static boolean isClosing (TokenType token)
	{
		return token == TOKEN_CPAR || token == TOKEN_CSB || token == TOKEN_EOF;
	}

	@SuppressWarnings("unused")
	private static boolean isPlusMinus (TokenType token)
	{
		return token == TOKEN_ADD || token == TOKEN_SUB;
	}

	private void cond () throws IOException
	{
		switch (lexer.lastToken ())
		{
		case TOKEN_NOT:
		case TOKEN_OPAR:
		case TOKEN_ADD:
		case TOKEN_SUB:
		case TOKEN_ID:
		case TOKEN_NUM:
		case TOKEN_OSB:
			termb ();
			cond1 ();
			break;
		default:
			throw new InvalidExpressionError ("Expected not (!), (, [ +, -, a number or an identifier. Got '"+lexer.lastToken()+"' instead.");
		}
	}

	private void cond1 () throws IOException
	{
		if (lexer.lastToken() == TOKEN_OR)
		{
			builder.buildOperator (OP_OR);
			lexer.nextToken ();
			termb ();
			builder.endOperator ();
			cond1 ();
		}
	}

	private void termb () throws IOException
	{
		switch (lexer.lastToken ())
		{
		case TOKEN_NOT:
		case TOKEN_OPAR:
		case TOKEN_ADD:
		case TOKEN_SUB:
		case TOKEN_ID:
		case TOKEN_NUM:
		case TOKEN_OSB:
			factb ();
			termb1 ();
			break;
		default:
			throw new InvalidExpressionError ("Expected not (!), (, [ +, -, a number or an identifier. Got '"+lexer.lastToken()+"' instead.");
		}
	}

	private void termb1 () throws IOException
	{
		if (lexer.lastToken() == TOKEN_AND)
		{
			builder.buildOperator (OP_AND);
			lexer.nextToken ();
			factb ();
			builder.endOperator ();
			termb1 ();
		}
	}

	private void factb () throws IOException
	{
		switch (lexer.lastToken())
		{
		case TOKEN_NOT:
			lexer.nextToken ();
			factb ();
			builder.buildOperator (OP_NOT);
			break;
		case TOKEN_OPAR:
			lexer.nextToken ();
			cond ();
			if (lexer.lastToken () != TOKEN_CPAR)
				throw new InvalidExpressionError ("Expected ). Got '"+lexer.lastToken()+"' instead.");
			lexer.nextToken ();
			break;
		case TOKEN_ADD:
		case TOKEN_SUB:
		case TOKEN_ID:
		case TOKEN_NUM:
		case TOKEN_OSB:
			expr ();
			switch (lexer.lastToken ())
			{
			case TOKEN_EQ:
				builder.buildOperator (OP_EQ);
				break;
			case TOKEN_NE:
				builder.buildOperator (OP_NE);
				break;
			case TOKEN_GT:
				builder.buildOperator (OP_GT);
				break;
			case TOKEN_GE:
				builder.buildOperator (OP_GE);
				break;
			case TOKEN_LT:
				builder.buildOperator (OP_LT);
				break;
			case TOKEN_LE:
				builder.buildOperator (OP_LE);
				break;
			default:
				throw new InvalidExpressionError ("Expected relational operator (==, !=, >, >=, <, <=). Got '"+lexer.lastToken()+"' instead.");
			}
			lexer.nextToken ();
			expr ();
			builder.endOperator ();
			break;
		default:
			throw new InvalidExpressionError ("Expected not (!), (, [, +, -, a number or an identifier. Got '"+lexer.lastToken()+"' instead.");
		}
	}

	private void expr () throws IOException
	{
		switch (lexer.lastToken ())
		{
		case TOKEN_SUB:
			// 0 - term
			builder.buildOperand (0);
			builder.buildOperator (OP_SUB);
			lexer.nextToken ();
			term ();
			builder.endOperator ();
			expr1 ();
			break;
		case TOKEN_ADD:
			lexer.nextToken ();
			term ();
			expr1 ();
			break;
		case TOKEN_ID:
		case TOKEN_NUM:
		case TOKEN_OSB:
			term ();
			expr1 ();
			break;
		default:
			throw new InvalidExpressionError ("Expected [, a number with sign or an identifier. Got '"+lexer.lastToken()+"' instead.");
		}
	}

	private void expr1 () throws IOException
	{
		if (lexer.lastToken() == TOKEN_ADD)
			builder.buildOperator (OP_ADD);
		else if (lexer.lastToken() == TOKEN_SUB)
			builder.buildOperator (OP_SUB);
		else
			return;
		lexer.nextToken ();
		term ();
		builder.endOperator ();
		expr1 ();
	}

	private void term () throws IOException
	{
		switch (lexer.lastToken())
		{
		case TOKEN_ID:
		case TOKEN_NUM:
		case TOKEN_OSB:
			termp ();
			term1 ();
			break;
		default:
			throw new InvalidExpressionError ("Expected [, a number or an identifier. Got '"+lexer.lastToken()+"' instead.");
		}
	}

	private void term1 () throws IOException
	{
		if (lexer.lastToken() == TOKEN_MUL)
			builder.buildOperator (OP_MUL);
		else if (lexer.lastToken() == TOKEN_DIV)
			builder.buildOperator (OP_DIV);
		else if (lexer.lastToken() == TOKEN_MOD)
			builder.buildOperator (OP_MOD);
		else
			return;
		lexer.nextToken ();
		termp ();
		builder.endOperator ();
		term1 ();
	}

	private void termp () throws IOException
	{
		switch (lexer.lastToken ())
		{
		case TOKEN_ID:
		case TOKEN_NUM:
		case TOKEN_OSB:
			fact ();
			termp1 ();
			break;
		default:
			throw new InvalidExpressionError ("Expected [, a number or an identifier. Got '"+lexer.lastToken()+"' instead.");
		}
	}

	private void termp1 () throws IOException
	{
		if (lexer.lastToken() == TOKEN_POW)
		{
			lexer.nextToken ();
			builder.buildOperator (OP_POW);
			fact ();
			builder.endOperator ();
			termp1 ();
		}
	}

	private void fact () throws IOException
	{
		if (lexer.lastToken() == TOKEN_ID)
		{
			builder.buildOperand (lexer.getString ());
			lexer.nextToken ();
		}
		else if (lexer.lastToken() == TOKEN_NUM)
		{
			builder.buildOperand (lexer.getInt ());
			lexer.nextToken ();
		}
		else if (lexer.lastToken() == TOKEN_OSB)
		{
			lexer.nextToken ();
			expr ();
			if (lexer.lastToken () != TOKEN_CSB)
				throw new InvalidExpressionError ("Expected ]. Got '"+lexer.lastToken()+"' instead.");
			lexer.nextToken ();
		}
		else
			throw new InvalidExpressionError ("Expected [, a number or an identifier. Got '"+lexer.lastToken()+"' instead.");
	}
}