package ecv.test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.Assert;

import org.junit.Test;

import ecv.Parser;
import ecv.StreamTokenizerAdapter;
import ecv.operator.Expression;
import ecv.operator.ExpressionBuilder;
import ecv.visitor.PostfixPrinterVisitor;


public class PostfixPrinterVisitorTest {

	private String evaluate(String expr) throws IOException {
		ExpressionBuilder builder = new ExpressionBuilder();
		new Parser(new StreamTokenizerAdapter(new StringReader(expr)), builder).parse();
		Expression expression = builder.getResult();
		StringWriter writer = new StringWriter();
		new PostfixPrinterVisitor(writer).print(expression);
		return writer.toString();
	}

	@Test
	public void testTrue() throws IOException {
		Assert.assertEquals("6 2 2 ^ + 25 5 / > 24 100 25 / / 3 10 + 6 - != and", evaluate("6 + 2^2 > 25 / 5 && 24 / [100 / 25] != 3 + 10 - 6"));
	}
}
