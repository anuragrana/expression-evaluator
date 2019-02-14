package com.ie.naukri.expressionevaluator;

import com.ie.naukri.expressionevaluator.service.ExpressionResolverService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ExpressionResolverServiceTest {

    @InjectMocks
    ExpressionResolverService expressionResolverService;


    @Test
    public void testResolver() {
        String expressions = "(data['profile']['loc'] == 'DELHI' )";
        Map<String, Object> dataFromService = new HashMap<>();

        Map<String, String> map = new HashMap<>();
        map.put("loc", "DELHI");

        dataFromService.put("profile", map);
        expressionResolverService.resolveExpession(expressions, dataFromService, "1");
    }


    @Test
    public void testValidationWhenValid() {
        boolean res = expressionResolverService.validate("(data['profile']['loc'] == 'DELHI')");
        assertTrue(res);
    }

    @Test
    public void testValidationWhenInvalid() {
        boolean res = expressionResolverService.validate("(data['profile']['loc'] == 'DELHI'))");
        assertFalse(res);
    }
}
