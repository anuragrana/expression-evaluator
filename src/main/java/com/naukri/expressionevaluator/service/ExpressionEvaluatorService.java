package com.naukri.expressionevaluator.service;

import com.naukri.expressionevaluator.resolver.SourceResolverServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 *  Parse the expression and evaluate the data provided
 */

@Service
public class ExpressionEvaluatorService {

    public static final Logger logger = LoggerFactory.getLogger(ExpressionEvaluatorService.class);

    @Inject
    private SourceResolverServiceInterface sourceResolverServiceInterface;

    @Inject
    private ExpressionResolverService expressionResolver;

    @Inject
    private ExpressionTokenizerService expressionTokenizer;

    /**
     * Tokenize and resolve the expression
     *
     * @param expression the expression to resolve
     * @param tenantId the tenant id
     * @param dataToRelay the data that should be relayed to resolver
     * @return boolean output of evaluation
     */
    public boolean evaluateExpression(String expression, String tenantId, Map<String, String> dataToRelay) {
        Map<Object, List<String>> parsedExpression = expressionTokenizer.tokenizeAndCacheExpression(expression);
        Map<String, Object> serviceResponse = sourceResolverServiceInterface
                .resolveExpressionValueFromService(parsedExpression, dataToRelay);
        boolean result = expressionResolver.resolveExpession(expression, serviceResponse, tenantId);
        return result;
    }

    /**
     * Checks if the expression is as per the format supported by the engine
     * @param expression the rule expression
     * @return boolean output of validation
     */
    public boolean validateExpression(String expression) {
        return expressionResolver.validate(expression);
    }

}
