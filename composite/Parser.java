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
		case TOKEN_NEQ:
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
		return token == TOKEN_CPAR || token == TOKEN_CPAR1 || token == TOKEN_EOF;
	}

	@SuppressWarnings("unused")
	private static boolean isPlusMinus (TokenType token)
	{
		return token == TOKEN_PLUS || token == TOKEN_MINUS;
	}

	private void cond () throws IOException
	{
		switch (lexer.lastToken ())
		{
		case TOKEN_NOT:
		case TOKEN_OPAR:
		case TOKEN_PLUS:
		case TOKEN_MINUS:
		case TOKEN_ID:
		case TOKEN_NUM:
		case TOKEN_OPAR1:
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
			builder.buildOperator (OPERATOR_OR);
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
		case TOKEN_PLUS:
		case TOKEN_MINUS:
		case TOKEN_ID:
		case TOKEN_NUM:
		case TOKEN_OPAR1:
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
			builder.buildOperator (OPERATOR_AND);
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
			builder.buildOperator (OPERATOR_NOT);
			break;
		case TOKEN_OPAR:
			lexer.nextToken ();
			cond ();
			if (lexer.lastToken () != TOKEN_CPAR)
				throw new InvalidExpressionError ("Expected ). Got '"+lexer.lastToken()+"' instead.");
			lexer.nextToken ();
			break;
		case TOKEN_PLUS:
		case TOKEN_MINUS:
		case TOKEN_ID:
		case TOKEN_NUM:
		case TOKEN_OPAR1:
			expr ();
			switch (lexer.lastToken ())
			{
			case TOKEN_EQ:
				builder.buildOperator (OPERATOR_EQ);
				break;
			case TOKEN_NEQ:
				builder.buildOperator (OPERATOR_NEQ);
				break;
			case TOKEN_GT:
				builder.buildOperator (OPERATOR_GT);
				break;
			case TOKEN_GE:
				builder.buildOperator (OPERATOR_GE);
				break;
			case TOKEN_LT:
				builder.buildOperator (OPERATOR_LT);
				break;
			case TOKEN_LE:
				builder.buildOperator (OPERATOR_LE);
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
		case TOKEN_MINUS:
			// 0 - term
			builder.buildOperand (0);
			builder.buildOperator (OPERATOR_MINUS);
			lexer.nextToken ();
			term ();
			builder.endOperator ();
			expr1 ();
			break;
		case TOKEN_PLUS:
			lexer.nextToken ();
			term ();
			expr1 ();
			break;
		case TOKEN_ID:
		case TOKEN_NUM:
		case TOKEN_OPAR1:
			term ();
			expr1 ();
			break;
		default:
			throw new InvalidExpressionError ("Expected [, a number with sign or an identifier. Got '"+lexer.lastToken()+"' instead.");
		}
	}

	private void expr1 () throws IOException
	{
		if (lexer.lastToken() == TOKEN_PLUS)
			builder.buildOperator (OPERATOR_PLUS);
		else if (lexer.lastToken() == TOKEN_MINUS)
			builder.buildOperator (OPERATOR_MINUS);
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
		case TOKEN_OPAR1:
			termp ();
			term1 ();
			break;
		default:
			throw new InvalidExpressionError ("Expected [, a number or an identifier. Got '"+lexer.lastToken()+"' instead.");
		}
	}

	private void term1 () throws IOException
	{
		if (lexer.lastToken() == TOKEN_MULT)
			builder.buildOperator (OPERATOR_MULT);
		else if (lexer.lastToken() == TOKEN_DIV)
			builder.buildOperator (OPERATOR_DIV);
		else if (lexer.lastToken() == TOKEN_REM)
			builder.buildOperator (OPERATOR_REM);
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
		case TOKEN_OPAR1:
			fact ();
			termp1 ();
			break;
		default:
			throw new InvalidExpressionError ("Expected [, a number or an identifier. Got '"+lexer.lastToken()+"' instead.");
		}
	}

	private void termp1 () throws IOException
	{
		if (lexer.lastToken() == TOKEN_POWER)
		{
			lexer.nextToken ();
			builder.buildOperator (OPERATOR_POWER);
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
		else if (lexer.lastToken() == TOKEN_OPAR1)
		{
			lexer.nextToken ();
			expr ();
			if (lexer.lastToken () != TOKEN_CPAR1)
				throw new InvalidExpressionError ("Expected ]. Got '"+lexer.lastToken()+"' instead.");
			lexer.nextToken ();
		}
		else
			throw new InvalidExpressionError ("Expected [, a number or an identifier. Got '"+lexer.lastToken()+"' instead.");
	}
}