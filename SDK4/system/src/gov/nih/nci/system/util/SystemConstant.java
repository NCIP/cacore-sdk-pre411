package gov.nih.nci.system.util;

/**
 * Constant assigns values to static fields 
 * @author caCORE SDK Team 
 * @version 1.0
 */
public class SystemConstant {

	public static final char 	AMPERSAND='&';
	public static final char 	AT='@';	
	public static final char 	COMMA=',';
	public static final char 	DOT='.';
	public static final char 	EQUAL='=';	
	public static final char 	FORWARD_SLASH='/';
	public static final char 	GREATER_THAN='>';	
	public static final char 	QUESTION_MARK='?';	
	public static final char 	LEFT_BRACKET='[';
	public static final char 	RIGHT_BRACKET=']';	
	public static final char 	SPACE=' ';		
	
	public static final String 	AMPERSAND_STR="&";
	public static final String  BACK_SLASH="\\";		
	public static final String 	COMMA_STR=",";
	public static final String 	FORWARD_SLASH_STR="/";
	
	public int MAX_RESULT_COUNT_PER_QUERY = 5000;
	public int RESULT_COUNT_PER_QUERY = 1000;	
	
    public static final String 	XLINK_URL = "http://www.w3.org/1999/xlink";

	public void setMAX_RESULT_COUNT_PER_QUERY(int max_result_count_per_query) {
		MAX_RESULT_COUNT_PER_QUERY = max_result_count_per_query;
	}

	public void setRESULT_COUNT_PER_QUERY(int result_count_per_query) {
		RESULT_COUNT_PER_QUERY = result_count_per_query;
	}	
}
