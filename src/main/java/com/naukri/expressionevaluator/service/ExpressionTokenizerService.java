package com.naukri.expressionevaluator.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tokenize the rule expression into attributes to be fetched from backend service
 */
@Service
public class ExpressionTokenizerService {

    public static final Logger logger = LoggerFactory.getLogger(ExpressionTokenizerService.class);
    private static Map<String, Map<Object, List<String>>> cacheMap = new HashMap<>();


    public Map<Object, List<String>> tokenizeAndCacheExpression(String expression) {
        return checkExpressionInCache(expression);
    }

    @SuppressWarnings("unchecked")
    public Map<Object, List<String>> checkExpressionInCache(String expression) {
        if (!(cacheMap.containsKey(expression))) {
            cacheMap.put(expression, tokenize(expression));
        }
        return cacheMap.get(expression);
    }

    /**
     * @param String
     * @return Map of attributes
     */
    public Map<Object, List<String>> tokenize(String expressionToTokenize) {
        String delimit = "==|!=|>|>=|<|<=| ";
        StringTokenizer stringTokens = new StringTokenizer(expressionToTokenize, delimit);
        Map<Object, List<String>> serviceTokens = new HashMap<Object, List<String>>();
        List<String> serviceNames = new ArrayList<String>();
        while (true) {
            if (!stringTokens.hasMoreElements()) {
                break;
            }
            String next = stringTokens.nextToken(delimit);
            Matcher m = Pattern.compile("\\['(.*?)\\']").matcher(next);
            if (m.find()) {
                String service = m.group(1);
                List<String> tokens = new ArrayList<String>();
                if (!serviceNames.contains(service)) {
                    serviceNames.add(service);
                } else {
                    tokens = serviceTokens.get(service);
                }

                if (m.find()) {
                    String attribute = m.group(1);
                    tokens.add(attribute);
                    serviceTokens.put(service, tokens);
                }
            }
        }
        return serviceTokens;
    }

    /**
     * @param Map
     * @return JSONObject
     */
    public JSONObject parseToJson(Map<String, String> stringFormatted) {
        JSONObject json = null;
        try {
            json = (JSONObject) new JSONParser().parse(JSONObject.toJSONString(stringFormatted));
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return json;
    }

}
