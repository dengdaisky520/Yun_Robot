package net.ianying.www.robot.entity;

/**
 * 新闻和菜谱类实体 302000
 * Created by anying on 2016/2/28.
 */
public class News {
    //标题内容
    private String article;
    //来源
    private String source;
    //图标
    private String icon;
    //地址
    private String detailurl;
    private ChatMessage chatMessage;

    public News() {
        super();
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public News(String article, String source, String icon, String detailurl) {
        this.article = article;
        this.source = source;
        this.icon = icon;
        this.detailurl = detailurl;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDetailurl() {
        return detailurl;
    }

    public void setDetailurl(String detailurl) {
        this.detailurl = detailurl;
    }
}
