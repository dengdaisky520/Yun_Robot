package net.ianying.www.robot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 转换json工具类
 * @author anying
 * create by 2015/12/24
 */
public class ConvertJsonUtils {
	
	/**
	 * 将java对象转换为json格式的字符串
	 * @param object  带转换的对象
	 * @return	json格式的字符串
	 */
	public static String toJsonString(Object object){
		return JSONObject.toJSONString(object);
	}
	
	/**
	 * 默认日期
	 * @param object
	 * @return
	 */
	public static String toJsonWithDefaultDateFormatString(Object object){
		return toJsonWithDateString(object, "yyyy-MM-DD hh:mm:ss");
	}
	
	/**
	 * 带有日期格式的json格式字符串转换
	 * @param object
	 * @param format
	 * @return
	 */
	public static String toJsonWithDateString(Object object,String format){
		return JSONObject.toJSONStringWithDateFormat(object, format, SerializerFeature.PrettyFormat);
	}
	
	/**
	 * 将对象转换为json格式字符串(包括子类的,与toJsonString具体区别可以参考自定义菜单)
	 * @param object
	 * @return
	 */
	public static String toJSONAndChildStr(Object object){
		return JSONObject.toJSON(object).toString();
	}
	
	/**
	 * 将json格式的数据转换为java对象(不包括数组,集合类型)
	 * @param <T>		
	 * @param jsonData	json格式的字符串
	 * @param t			待转换成的java对象
	 * @return
	 */
	public static <T> T jsonToJavaObject(String jsonData,Class<T> t){
		return JSONObject.parseObject(jsonData, t);
	}
	
	/**
	 * 将json格式的数据转换为java集合对象 ,List集合,map集合
	 * @param jsonData	待转换的json格式的字符串
	 * @param t			泛型集合对象
	 * @return
	 */
	public static <T> List<T> jsonToJavaList(String jsonData,Class<T> t){
		return JSONObject.parseArray(jsonData, t);
	}
	
	/**
	 * 获取json格式数据中的键,将其中的值作为转换为List中的数据
	 * @param jsonData	json格式的数据
	 * @param t			待转换的类型
	 * @param key		json格式中的指定的键
	 * @return			list集合
	 * 示列：
	 * {"kf_online_list":[{"kf_account":"test@hzsanyoxy","status":1,"kf_id":"1003","auto_accept":0,"accepted_case":0}]}
	 * 这里的key  kf_online_list
	 */
	public static <T> List<T> jsonToJavaListByKey(String jsonData,Class<T> t,String key){
		List<T> result = new ArrayList<T>();
		JSONObject obj = JSONObject.parseObject(jsonData);
		if(null!=obj){
					if(obj.containsKey(key)){
						jsonData = obj.getString(key);
						result = new ArrayList<T>();
						result = JSONObject.parseArray(jsonData,t);
			}
		}
		return result;
	}


	/**
	 * 将jsonString转化为hashmap
	 * @param jsonString json字符串
	 * @return
	 */
	public static HashMap<String, Object> fromJson2Map(String jsonString) {
		HashMap jsonMap = JSON.parseObject(jsonString, HashMap.class);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		for(Iterator iter = jsonMap.keySet().iterator(); iter.hasNext();){
			String key = (String)iter.next();
			if(jsonMap.get(key) instanceof JSONArray){
				JSONArray jsonArray = (JSONArray)jsonMap.get(key);
				List list = handleJSONArray(jsonArray);
				resultMap.put(key, list);
			}else{
				resultMap.put(key, jsonMap.get(key));
			}
		}
		return resultMap;
	}

	/**
	 * 将jsonString转化为list<map>
 	 * @param jsonArray JSON对象
	 * @return  List<HashMap<String, Object>>
	 */
	public static  List<HashMap<String, Object>> handleJSONArray(JSONArray jsonArray){
		List list = new ArrayList();
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;
			HashMap map = new HashMap<String, Object>();
			for (Map.Entry entry : jsonObject.entrySet()) {
				if(entry.getValue() instanceof  JSONArray){
					map.put((String)entry.getKey(), handleJSONArray((JSONArray)entry.getValue()));
				}else{
					map.put((String)entry.getKey(), entry.getValue());
				}
			}
			list.add(map);
		}
		return list;
	}
	

}
