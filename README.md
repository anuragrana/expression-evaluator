Introduction:

This is a general purpose expression evaluator for spring boot applications. It accepts string expressions that define business rules and evaluates data objects against them.

Features:
1. Supports standard mathematical operators, relational operators (==, !=, <, >, <=, >=), conditional operators (and, or, not) and regular expressions.
2. Fast. Responds in single digit milliseconds.
3. Supports defining rules in terms of multiple data objects. 
4. Supports defining rules around request and system properties.

 
Pre-requisites: Jdk 8, Spring core dependencies

Integration Steps:

1. Add the following dependency in project's pom.xml,

		<dependency>
	   		<groupId>com.ie.naukri.msa.concerns</groupId>
	    		<artifactId>expression-evaluator</artifactId>
	    		<version>%version_number%</version>
	    		<type>pom</type>
		 </dependency>

2. Define Expression: Define an expression string that represents the rules to run against the input data. Format of the expression should be correct and error free. There is a specific format in which the expressions should be written. Sample expressions below,

    Rules format (case insensitive):
    
		data['profile']['location'] == '65'
	
		data['profile']['location'] == '65' and data['profile']['role'] != '8' and data['request']['useragent'] == 'android'
	
		data['profile']['location'] == '65' and data['system']['day'] == 'tuesday'
	
		(data['profile']['location'] == '65' or data['profile']['role'] == '6')

3. Validate Expression: It is quite possible that you need to define the expression at one place/application and evaluate it at the other. In such cases, you can validate the expression beforehand to check for syntax errors or non supported format errors. Sample code to validate the expression:
	
		public boolean validateExpression(String expressionRule) {
			return 	expressionEvaluatorService.validateExpression(expressionRule);
		}
		
4. Evaluate Expression: Expression evaluator works in steps like Resolve and Evaluate the expression. Main objective of the expression evaluator is to resolve the expression at run time ,but along with it the value of the fields that we need to compare with the expression can be fetched at run time.

	Suppose we want to evaluate an expression (data['PROFILE']['LOC_PREF'] =='65') for the attribute LOC_PREF of PROFILE but the attribute value is unknown. So here expression evaluator will help us first to get the value of the attribute "LOC_PREF" then will help to resolve this expression for the fetched value. Here the identitfier in the map format only having the keys will be passed in the resolver and resolver will fetch the values of the required input attributes and provided to the evaluator, then the evaluator will evaluate the expression for the fetched values of attributes.
	
4.1.Implement Resolver: Implement the resolveExpressionValueFromService of SourceResolverServiceInterface to resolve the values of field's attributes.
   
Argument 
   
    Map<Object, List<String>>: Json formatted fields and their attributes whose values need to be fetched at run time. 
    Map<String, String>: key identifier 
    The key identifier will be like customerNo:1221233 whose attribute like PINCODE,CITY of field ADDRESS need to be fetched.
	
Input format of the attributes in resolveExpressionValueFromService :
   
    {SYSTEM =[DATE],PROFILE=[LOC_PREF,EDUCATION]}
    {PROFILE=[LOC_PREF,EDUCATION]}
    {SYSTEM=[DATE],PROFILE=[LOC,EDUCATION],REQUEST=[user-agent]}
    {profile= [ctc,loc_pref ,education],system = [date],jobalert:[jobid]}	

Output data format provided by resolver to the expression evaluator(that need to evaluate):
   
    {SYSTEM={DATE=TODAY}, PROFILE={LOC=DELHI, EDUCATION=BTECH}, REQUEST={userAgent=asaa}}
    {SYSTEM={DATE=TODAY}, PROFILE={LOC=DELHI, EDUCATION=BTECH}}
	
Sample code to evaluate the expression:

	public boolean evaluateExpression(String expression, String userId) {
		Map<String, String> dataToRelay = new HashMap<>();
		dataToRelay.put(dataToRelayKey, userId);
		boolean result =expressionEvaluatorService.evaluateExpression(expression, "0",dataToRelay);
		return result;
	}
	
4.2. Invoke evaluator: Use the evaluateExpression method of Expression Evaluator Service to evaluate the expression.
Arguments

    String: expression that need to be evaulated.
    String: Parameter to differentiate the Source like appId in case of REST.
    Map<String, String>: unique identifier whose attributes values need to evaulate.
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

NOTE: Currently, only single level expressions are supported. Like, data['profile']['education'] is supported but not ['profile']['education']['course']. The support to be added on need basis.


