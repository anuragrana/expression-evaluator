Spring Expression Evaluator:

This is a general purpose expression evaluator in Java.Its intend is to evaluate an expression by resolving its values for the specific identifier at run time.
Expression evaluator supports standard mathematical operators, relational operators, logical operators, conditional operators, collections and regular expressions, etc. 
It can be used to inject a bean or a bean property into another bean. 

Features:

	1.The literal expression can be used in the evaluator. For example, "this is literal" is a String literal. If this literal is used as a expression,
	  the evaluated value will also be "this is literal".
	2.The relational operators equal (==), not equal (!=), less than (<), less than or equal (<=), greater than (>), and greater than or equal (>=) are
	  also supported in the expression.
	3.The logical operators, (&&) or (||) and not (!), are supported.
	 
Pre-requisites:

	Jdk 8
	Spring core dependencies

Integration:

1.POM dependency :
Expression evaluator can be used by adding the dependency in the project's pom.xml.

	 <dependency>
   		<groupId>com.ie.naukri.msa.concerns</groupId>
    	<artifactId>expression-evaluator</artifactId>
    	<version>%version_number%</version>
    	<type>pom</type>
	 </dependency>

2.Create the Rule:
Before starting to use the expression evaluator , Rules need to be decide that we want to evaluate.
Format of the expression should be correct and error free.

    Rules format (case insensitive):
	(data['PROFILE']['LOC_PREF'] == '65')
	(data['PROFILE']['LOC_PREF'] == '65' and data['PROFILE']['ROLE'] =='6')
	(data['PROFILE']['loc_pref'] == '65')
	(data['profile']['loc_pref'] == '65' OR data['profile']['role'] == '6')

3.Validation of expression:
	Expression evaluator also facilitate to validate the created rules, to 	avail this feature validateExpression is provided.
	It is possible that the created expression(rule) might have some syntax error or unexpected/non supported format , so this can be validated by 	using the validateExpression  method.
	Basically ,its more useful when the expression(rule) are created separately before evaluting them.
	
Sample code to validate the expression:
	
	public boolean validateExpression(String expressionRule) {
			return 	expressionEvaluatorService.validateExpression(expressionRule);
	}
		
4.Resolve and Evaluate the expression : 
	Expression evaluator works in steps like Resolve and Evaluate the 	expression.
	Main objective of the expression evaluator is to resolve the expression at 	run time ,but along with it the value of the fields that we need to 	compare 
	with the expression can be fetched at run time.
	Suppose we want to evaluate an expression (data['PROFILE']['LOC_PREF'] =='65') for the attribute LOC_PREF of field PROFILE but the attribute value 	is unknown.	So here expression evaluator will help us first to get the 	value of the attribute "LOC_PREF" then will help to resolve this 	expression for the fetched value.Here the identitfier in the map format 	only having the keys will be passed in the resolver and resolver will 	fetch the values of the required input attributes and provided to the 	evaluator , then the evaluator will evaluate the expression for the 	fetched values of attributes.
	
4.1.Implementation of Resolver: Implement the resolveExpressionValueFromService of SourceResolverServiceInterface to resolve the values of field's attributes.
   
Argument 
   
    Map<Object, List<String>> 	:Json formatted fields and their attributes whose values need to be fetched at run time. 
    Map<String, String>		    :key identifier 
    The key identifier will be like customerNo:1221233 whose attribute like PINCODE,CITY of field ADDRESS need to be fetched.
	
Input format of the attributes in resolveExpressionValueFromService :
   
    {SYSTEM =[DATE],PROFILE=[LOC_PREF,EDUCATION]}
    {PROFILE=[LOC_PREF,EDUCATION]}
    {SYSTEM=[DATE],PROFILE=[LOC,EDUCATION],REQUEST=[user-agent]}
    {profile= [ctc,loc_pref ,education],system = [date],jobalert:[jobid]}	

Output data format provided by resolver to the expression evaluator(that 	need to evaluate):
   
    {SYSTEM={DATE=TODAY}, PROFILE={LOC=DELHI, EDUCATION=BTECH}, REQUEST={userAgent=asaa}}
    {SYSTEM={DATE=TODAY}, PROFILE={LOC=DELHI, EDUCATION=BTECH}}
	
Sample code to evaluate the expression:

	public boolean evaluateExpression(String expression, String userId) {
		Map<String, String> dataToRelay = new HashMap<>();
		dataToRelay.put(dataToRelayKey, userId);
		boolean result =expressionEvaluatorService.evaluateExpression(expression, "0",dataToRelay);
		return result;
	}
	
4.2.Expression Evaluation: evaluateExpression method of Expression Evaluator Service to evaluate the expression.
Arguments

    String : expression that need to be evaulated.
    String : Parameter to differentiate the Source like appId in case of REST.
    Map<String, String> : unique identifier whose attributes values need to evaulate.
	The unique identifier will be like customerNo:1221233 
	
Sample of implementation to get the value that are needed to resolve:

	@Override
	public Map<String, Object> resolveExpressionValueFromService(Map<Object,List<String>> jsonFormattedServiceInput,Map<String, String> dataToRelay)hrows ExpressionResolverException {
		HashMap<String, Object> SpelAcceptedData = null;
		try {
	    	HashMap<Object, Object> data = callRestApiToGetData(requestBody,dataToRelayKey);
	    	SpelAcceptedData = new HashMap<>();
			for (Map.Entry<Object, Object> entry : data.entrySet()) {
		    	SpelAcceptedData.put(expressionProfile, entry.getValue());
			}
		} catch (RESTServiceIntegrationException e) {
			throw new ExpressionResolverException("Error in expression evaluator implementation ", e);
		}
		return SpelAcceptedData;
	}

NOTE: Single level expression will be handled like below:
PROFILE.EDUCATION

