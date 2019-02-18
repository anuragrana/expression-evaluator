package com.naukri.expressionevaluator;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.naukri.expressionevaluator.service.ExpressionTokenizerService;

@RunWith(MockitoJUnitRunner.class)
public class ExpressionTokenizerTest {

	@InjectMocks
	ExpressionTokenizerService expressionTokenizer;
	
	@Test
	public void parseExpressionToSplitTest() {
		String exp = "(data['profile']['loc'] == 'DELHI' )";
		Map<Object, List<String>> map = expressionTokenizer.tokenize(exp);
		assertNotNull(map);
	}
}
