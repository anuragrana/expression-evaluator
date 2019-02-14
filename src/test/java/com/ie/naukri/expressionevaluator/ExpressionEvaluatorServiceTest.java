package com.ie.naukri.expressionevaluator;

import com.ie.naukri.expressionevaluator.resolver.SourceResolverServiceInterface;
import com.ie.naukri.expressionevaluator.service.ExpressionEvaluatorService;
import com.ie.naukri.expressionevaluator.service.ExpressionResolverService;
import com.ie.naukri.expressionevaluator.service.ExpressionTokenizerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;


@RunWith(MockitoJUnitRunner.class)
public class ExpressionEvaluatorServiceTest {
    @InjectMocks
    ExpressionEvaluatorService expressionEvaluatorService;

    @Mock
    SourceResolverServiceInterface sourceResolverServiceInterface;

    @Mock
    ExpressionTokenizerService expressionTokenizer;

    @Mock
    ExpressionResolverService expressionResolver;

    @Test
    public void testExpressionEvaluation() {
        boolean result = false;
        String ruleExpression = "data['profile']['loc'] == 'Delhi' and data['profile']['education'] != 'BTECH'";

        Map<Object, List<String>> requestMap = new HashMap<>();
        requestMap.put("profile", Arrays.asList("loc", "education"));
        Mockito.when(expressionTokenizer.tokenize(ruleExpression)).thenReturn(requestMap);

        Map<String, Object> responseMap = new HashMap<>();
        Map<String, String> innerMap = new HashMap<>();
        innerMap.put("loc", "Delhi");
        innerMap.put("education", "BCA");
        responseMap.put("profile", innerMap);

        Mockito.when(sourceResolverServiceInterface.resolveExpressionValueFromService(any(), any())).thenReturn(responseMap);
        Mockito.when(expressionResolver.resolveExpession(any(), any(), any())).thenReturn(true);
        Map<String, String> map = new HashMap<>();
        map.put("userId", "123");
        result = expressionEvaluatorService.evaluateExpression(ruleExpression, "1", map);

        assertTrue(result);
    }

    @Test
    public void testExpressionValidationWhenValid() {
        String expression = "(data['profile']['loc'] == 'Delhi')";
        Mockito.when(expressionResolver.validate(expression)).thenReturn(true);
        boolean result = expressionEvaluatorService.validateExpression(expression);
        assertTrue(result);
    }

    @Test
    public void testExpressionValidationWhenInvalid() {
        String expression = "data{'profile'}{'loc'} == 'delhi'";
        Mockito.when(expressionResolver.validate(expression)).thenReturn(false);
        boolean result = expressionEvaluatorService.validateExpression(expression);
        assertFalse(result);
    }
}
