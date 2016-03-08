package net.ianying.www.robot.entity;

/**菜谱类API
 * Created by anying on 2016/2/28.
 */
public class Recipes {
    //菜名
    private  String name;
    //菜谱信息
    private String icon;
    //信息图标
    private String info;
    //详情链接
    private String detailurl;
    private ChatMessage chatMessage;

    public Recipes() {
        super();
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public Recipes(String name, String icon, String info, String detailurl) {
        this.name = name;
        this.icon = icon;
        this.info = info;
        this.detailurl = detailurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDetailurl() {
        return detailurl;
    }

    public void setDetailurl(String detailurl) {
        this.detailurl = detailurl;
    }
}
