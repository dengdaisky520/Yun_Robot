package net.ianying.www.robot.utils;

import net.ianying.www.robot.entity.ChatMessage;
import net.ianying.www.robot.entity.CommonException;
import net.ianying.www.robot.entity.News;
import net.ianying.www.robot.entity.Recipes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

public class HttpUtils {
    private static String API_KEY= Constans.TL_API_KEY;
    private static String URL = "http://www.tuling123.com/openapi/api";

    /**
     * 发送一个消息，并得到返回的消息
     *
     * @param msg
     * @return
     */
    public static ChatMessage sendMsg(String msg) {
        ChatMessage message = new ChatMessage();
        String url = setParams(msg);
        String res = doGet(url);
        try {
            JSONObject jsonObject = new JSONObject(res);
            int code = jsonObject.getInt("code");
            String text = jsonObject.getString("text");
            if (code > 400000 || text == null
                    || text.trim().equals("")) {
                message.setMsg("该功能等待开发...");
                //新闻类
            } else if (code == 302000) {
                message.setTypeCode(code);
                message.setMsg(text);
                System.out.println(jsonObject.getString("list"));
                List<News> newsList = ConvertJsonUtils.jsonToJavaList(jsonObject.getString("list"), News.class);
                message.setNewsList(newsList);
                //菜谱类
            } else if (code == 308000) {
                message.setTypeCode(code);
                message.setMsg(text);
                List<Recipes> recipesList = ConvertJsonUtils.jsonToJavaList(jsonObject.getString("list"), Recipes.class);
                message.setRecipesList(recipesList);
            } else if (code == 200000) {
                message.setTypeCode(code);
                message.setUrl(jsonObject.getString("url"));
                message.setMsg(text);
            } else {
                //标识码为1000000
                message.setTypeCode(code);
                message.setMsg(text);
            }
            message.setType(ChatMessage.Type.INPUT);
            message.setDate(new Date());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 拼接Url
     *
     * @param msg
     * @return
     */
    private static String setParams(String msg) {
        try {
            msg = URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return URL + "?key=" + API_KEY + "&info=" + msg + "&userid=520000";
    }

    /**
     * Get请求，获得返回数据
     *
     * @param urlStr
     * @return
     */
    private static String doGet(String urlStr) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else {
                throw new CommonException("服务器连接错误！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException("服务器连接错误！");
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
        }

    }

}
