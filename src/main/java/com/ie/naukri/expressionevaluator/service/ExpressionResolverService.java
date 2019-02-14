package com.ie.naukri.expressionevaluator.service;

import com.ie.naukri.expressionevaluator.exceptions.ExpressionResolverException;
import com.ie.naukri.expressionevaluator.model.DataHolder;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Read the input data and evaluate it against the expression
 *
 * @author Neha Lohia
 */
@Service
public class ExpressionResolverService {

    private ExpressionParser parser = new SpelExpressionParser();


    /**
     * @param String
     * @param Map
     * @param String
     * @return boolean
     * @throws Exception
     */
    public boolean resolveExpession(String expressions, Map<String, Object> dataFromService, String tenantId) {
        Boolean result = false;

        DataHolder dataHolder = new DataHolder();
        dataHolder.setData(dataFromService);
        Expression expression = parser.parseExpression(expressions);
        try {
            result = expression.getValue(dataHolder, Boolean.class);
        } catch (Exception e) {
            throw new ExpressionResolverException("Error Parsing Expression", e);
        }
        return result;
    }

    /**
     * @param String
     * @return boolean
     */
    public boolean validate(String expression) {
        try {
            parser.parseExpression(expression);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
