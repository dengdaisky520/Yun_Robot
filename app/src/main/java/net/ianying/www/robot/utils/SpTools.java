package net.ianying.www.robot.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpTools {
	public static void putString(Context context,String key,String value){
		SharedPreferences sp = context.getSharedPreferences(Constans.SPFILE, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();//保存数据
	}

	public static String getString(Context context,String key,String defValue){
		SharedPreferences sp = context.getSharedPreferences(Constans.SPFILE, Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}

	public static void putBoolean(Context context,String key,Boolean value){
		SharedPreferences sp = context.getSharedPreferences(Constans.SPFILE, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();//保存数据
	}

	public static boolean getBoolean(Context context,String key,boolean defValue){
		SharedPreferences sp = context.getSharedPreferences(Constans.SPFILE, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}
}
