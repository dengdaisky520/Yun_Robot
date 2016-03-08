package net.ianying.www.robot.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 字符过滤工具类
 */
public class StringFilterUtil {
	/**
	 * 清除掉所有特殊字符 
	 * @param str
	 * @return 
	 * @throws PatternSyntaxException
	 */
	public static String StringFilter(String str) throws PatternSyntaxException {                               
      String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：\"”“’。，、？-]";     
      Pattern p = Pattern.compile(regEx);        
      Matcher m = p.matcher(str);        
      return m.replaceAll("").trim();        
    }
}
