package com.naukri.expressionevaluator.resolver;

import java.util.List;
import java.util.Map;

import com.naukri.expressionevaluator.exceptions.ExpressionResolverException;
import org.springframework.stereotype.Service;


/* Interface to be implemented by the calling application to return the attribute values
 * @author Neha Lohia
 */
@Service
public interface SourceResolverServiceInterface {

	public Map<String,Object> resolveExpressionValueFromService(Map<Object, List<String>> attributesToFetch,  Map<String,String> dataToRelay) throws ExpressionResolverException;
}
