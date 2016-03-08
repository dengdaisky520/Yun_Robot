package net.ianying.www.robot.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatMessage {

    /**
     * 消息类型
     */
    private Type type;
    /**
     * 消息内容
     */
    private String msg;
    private String url;
    private int typeCode;

    public int getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }

    public List<?> getDataList() {
        return dataList;
    }

    public void setDataList(List<?> dataList) {
        this.dataList = dataList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private List<?> dataList;
    //新闻实体类
    private List<News> newsList;
    //蔬菜类
    private List<Recipes> recipesList;

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public List<Recipes> getRecipesList() {
        return recipesList;
    }

    public void setRecipesList(List<Recipes> recipesList) {
        this.recipesList = recipesList;
    }

    /**
     * 日期
     */
    private Date date;

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    /**
     * 日期的字符串格式
     */

    private String dateStr;
    /**
     * 发送人
     */
    private String name;
    /**
     * 发送人头像
     */
    private String image;

    public enum Type {
        INPUT, OUTPUT
    }

    public ChatMessage() {
    }

    public ChatMessage(Type type, String msg, String name, String image) {
        super();
        this.type = type;
        this.msg = msg;
        this.name = name;
        this.image = image;
        setDate(new Date());
    }

    public String getDateStr() {
        return dateStr;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateStr = df.format(date);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
