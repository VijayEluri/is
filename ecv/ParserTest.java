package ecv;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import ecv.operator.ExpressionBuilder;

public class ParserTest {

	public Parser parse(String expr) {
		ExpressionBuilder builder = new ExpressionBuilder();
		return new Parser(new StreamTokenizerAdapter(new StringReader(expr)), builder);
	}

	@Test
	public void testValid() throws IOException {
		parse("6 + 2^2 > 25 / 5 && 24 / [100 / 25] != 3 + 10 - 6").parse();
	}

	@Test(expected=InvalidExpressionError.class)
	public void testInvalid() throws IOException {
		parse("6 + 2^2) > 25 / 5 && 24 / [100 / 25] != 3 + 10 - 6").parse();
	}
}
