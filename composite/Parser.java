package ecv.composite;

import static ecv.composite.ExpressionBuilder.OperatorType.*;
import static ecv.composite.StreamTokenizerAdapter.TokenType.*;

import java.io.IOException;

import ecv.composite.StreamTokenizerAdapter.TokenType;

public class Parser
{
	private StreamTokenizerAdapter sta;
	private ExpressionBuilder builder;

	public Parser (StreamTokenizerAdapter sta, ExpressionBuilder builder)
	{
		this.sta = sta;
		this.builder = builder;
	}

	public void parse () throws IOException
	{
		sta.nextTokenType ();
		cond ();
		if (sta.lastToken () != TOKEN_EOF)
			throw new InvalidExpressionError ("Expected end of expression. Got '"+sta.lastToken()+"' instead.");
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
		switch (sta.lastToken ())
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
			throw new InvalidExpressionError ("Expected not (!), (, [ +, -, a number or an identifier. Got '"+sta.lastToken()+"' instead.");
		}
	}

	private void cond1 () throws IOException
	{
		if (sta.lastToken() == TOKEN_OR)
		{
			builder.buildOperator (OP_OR);
			sta.nextTokenType ();
			termb ();
			builder.endOperator ();
			cond1 ();
		}
	}

	private void termb () throws IOException
	{
		switch (sta.lastToken ())
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
			throw new InvalidExpressionError ("Expected not (!), (, [ +, -, a number or an identifier. Got '"+sta.lastToken()+"' instead.");
		}
	}

	private void termb1 () throws IOException
	{
		if (sta.lastToken() == TOKEN_AND)
		{
			builder.buildOperator (OP_AND);
			sta.nextTokenType ();
			factb ();
			builder.endOperator ();
			termb1 ();
		}
	}

	private void factb () throws IOException
	{
		switch (sta.lastToken())
		{
		case TOKEN_NOT:
			sta.nextTokenType ();
			factb ();
			builder.buildOperator (OP_NOT);
			break;
		case TOKEN_OPAR:
			sta.nextTokenType ();
			cond ();
			if (sta.lastToken () != TOKEN_CPAR)
				throw new InvalidExpressionError ("Expected ). Got '"+sta.lastToken()+"' instead.");
			sta.nextTokenType ();
			break;
		case TOKEN_ADD:
		case TOKEN_SUB:
		case TOKEN_ID:
		case TOKEN_NUM:
		case TOKEN_OSB:
			expr ();
			switch (sta.lastToken ())
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
				throw new InvalidExpressionError ("Expected relational operator (==, !=, >, >=, <, <=). Got '"+sta.lastToken()+"' instead.");
			}
			sta.nextTokenType ();
			expr ();
			builder.endOperator ();
			break;
		default:
			throw new InvalidExpressionError ("Expected not (!), (, [, +, -, a number or an identifier. Got '"+sta.lastToken()+"' instead.");
		}
	}

	private void expr () throws IOException
	{
		switch (sta.lastToken ())
		{
		case TOKEN_SUB:
			// 0 - term
			builder.buildOperand (0);
			builder.buildOperator (OP_SUB);
			sta.nextTokenType ();
			term ();
			builder.endOperator ();
			expr1 ();
			break;
		case TOKEN_ADD:
			sta.nextTokenType ();
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
			throw new InvalidExpressionError ("Expected [, a number with sign or an identifier. Got '"+sta.lastToken()+"' instead.");
		}
	}

	private void expr1 () throws IOException
	{
		if (sta.lastToken() == TOKEN_ADD)
			builder.buildOperator (OP_ADD);
		else if (sta.lastToken() == TOKEN_SUB)
			builder.buildOperator (OP_SUB);
		else
			return;
		sta.nextTokenType ();
		term ();
		builder.endOperator ();
		expr1 ();
	}

	private void term () throws IOException
	{
		switch (sta.lastToken())
		{
		case TOKEN_ID:
		case TOKEN_NUM:
		case TOKEN_OSB:
			termp ();
			term1 ();
			break;
		default:
			throw new InvalidExpressionError ("Expected [, a number or an identifier. Got '"+sta.lastToken()+"' instead.");
		}
	}

	private void term1 () throws IOException
	{
		if (sta.lastToken() == TOKEN_MUL)
			builder.buildOperator (OP_MUL);
		else if (sta.lastToken() == TOKEN_DIV)
			builder.buildOperator (OP_DIV);
		else if (sta.lastToken() == TOKEN_MOD)
			builder.buildOperator (OP_MOD);
		else
			return;
		sta.nextTokenType ();
		termp ();
		builder.endOperator ();
		term1 ();
	}

	private void termp () throws IOException
	{
		switch (sta.lastToken ())
		{
		case TOKEN_ID:
		case TOKEN_NUM:
		case TOKEN_OSB:
			fact ();
			termp1 ();
			break;
		default:
			throw new InvalidExpressionError ("Expected [, a number or an identifier. Got '"+sta.lastToken()+"' instead.");
		}
	}

	private void termp1 () throws IOException
	{
		if (sta.lastToken() == TOKEN_POW)
		{
			sta.nextTokenType ();
			builder.buildOperator (OP_POW);
			fact ();
			builder.endOperator ();
			termp1 ();
		}
	}

	private void fact () throws IOException
	{
		if (sta.lastToken() == TOKEN_ID)
		{
			builder.buildOperand (sta.getString ());
			sta.nextTokenType ();
		}
		else if (sta.lastToken() == TOKEN_NUM)
		{
			builder.buildOperand (sta.getInt ());
			sta.nextTokenType ();
		}
		else if (sta.lastToken() == TOKEN_OSB)
		{
			sta.nextTokenType ();
			expr ();
			if (sta.lastToken () != TOKEN_CSB)
				throw new InvalidExpressionError ("Expected ]. Got '"+sta.lastToken()+"' instead.");
			sta.nextTokenType ();
		}
		else
			throw new InvalidExpressionError ("Expected [, a number or an identifier. Got '"+sta.lastToken()+"' instead.");
	}
}