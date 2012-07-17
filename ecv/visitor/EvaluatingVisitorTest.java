package ecv.visitor;

import java.io.IOException;
import java.io.StringReader;

import junit.framework.Assert;

import org.junit.Test;
import ecv.Parser;
import ecv.PropertiesContext;
import ecv.StreamTokenizerAdapter;
import ecv.operator.Expression;
import ecv.operator.ExpressionBuilder;


public class EvaluatingVisitorTest {

	private boolean evaluate(String expr) throws IOException {
		ExpressionBuilder builder = new ExpressionBuilder();
		new Parser(new StreamTokenizerAdapter(new StringReader(expr)), builder).parse();
		Expression expression = builder.getResult();
		return new EvaluatingVisitor(new PropertiesContext ()).valueOf(expression);		
	}

	@Test
	public void testTrue() throws IOException {
		Assert.assertTrue(evaluate("6 + 2^2 > 25 / 5 && 24 / [100 / 25] != 3 + 10 - 6"));
	}

	@Test
	public void testFalse() throws IOException {
		Assert.assertFalse(evaluate("10 == 4"));
	}
}
