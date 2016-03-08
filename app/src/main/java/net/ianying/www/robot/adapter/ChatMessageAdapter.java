package net.ianying.www.robot.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.ianying.www.robot.R;
import net.ianying.www.robot.entity.ChatMessage;
import net.ianying.www.robot.widget.RoundedCornerImageView;
import net.tsz.afinal.FinalBitmap;

import java.util.List;


public class ChatMessageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<ChatMessage> mDatas;
    Context context;

    private FinalBitmap fb;

    public ChatMessageAdapter(Context context, List<ChatMessage> datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        this.context = context;
        fb = FinalBitmap.create(context);// 初始化FinalBitmap模块
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 接受到消息为1，发送消息为0
     */
    @Override
    public int getItemViewType(int position) {
        ChatMessage msg = mDatas.get(position);
        return msg.getType() == ChatMessage.Type.INPUT ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage = mDatas.get(position);

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            if (chatMessage.getType() == ChatMessage.Type.INPUT) {
                convertView = mInflater.inflate(R.layout.main_chat_from_msg,
                        parent, false);
                viewHolder.createDate = (TextView) convertView
                        .findViewById(R.id.chat_from_createDate);
                viewHolder.content = (TextView) convertView
                        .findViewById(R.id.chat_from_content);
                viewHolder.imageView = (RoundedCornerImageView) convertView
                        .findViewById(R.id.chat_from_icon);
                convertView.setTag(viewHolder);
            } else if (chatMessage.getType() == ChatMessage.Type.OUTPUT) {
                convertView = mInflater.inflate(R.layout.main_chat_send_msg,
                        null);

                viewHolder.createDate = (TextView) convertView
                        .findViewById(R.id.chat_send_createDate);
                viewHolder.content = (TextView) convertView
                        .findViewById(R.id.chat_send_content);
                viewHolder.imageView = (RoundedCornerImageView) convertView
                        .findViewById(R.id.chat_send_icon);
                viewHolder.name = (TextView) convertView
                        .findViewById(R.id.chat_send_name);
                convertView.setTag(viewHolder);
            }
            // TODO: 2016/3/4 后续升级改进内容
              /*else if (chatMessage.getTypeCode() == 302000) {
               convertView = mInflater.inflate(R.layout.main_chat_send_msg,
						null);

				viewHolder.createDate = (TextView) convertView
						.findViewById(R.id.chat_send_createDate);
				viewHolder.imageView = (RoundedCornerImageView) convertView
						.findViewById(R.id.chat_send_icon);
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.chat_send_name);
				viewHolder.source= (TextView) convertView.findViewById(R.id.newsSource);
				viewHolder.icon= (ImageView) convertView.findViewById(R.id.newsImgIcon);
				viewHolder.Url= (TextView) convertView.findViewById(R.id.newsUrl);
				viewHolder.content.setVisibility(View.GONE);
				viewHolder.newsRelative= (RelativeLayout) convertView.findViewById(R.id.newsRelative);
				viewHolder.newsRelative.setVisibility(View.VISIBLE);
				convertView.setTag(viewHolder);
            }*/
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (chatMessage.getTypeCode() == 302000) {
           /* for (int i = 0; i < chatMessage.getNewsList().size(); i++) {
                System.out.println(chatMessage.getNewsList().get(i).getDetailurl());
                viewHolder.Url.setText(chatMessage.getNewsList().get(i).getDetailurl());
                viewHolder.source.setText(chatMessage.getNewsList().get(i).getSource());
                viewHolder.icon.setImageResource(R.mipmap.keai);
            }*/
            viewHolder.content.setText(chatMessage.getMsg());
        } else if (chatMessage.getTypeCode() == 200000) {
            String str = "<a href=" + chatMessage.getUrl() + ">打开页面</a>";
            Spanned localSpanned = Html.fromHtml(chatMessage.getMsg() + "：" + str);
            viewHolder.content.setText(localSpanned);
            viewHolder.content.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            viewHolder.content.setText(chatMessage.getMsg());
        }
        viewHolder.createDate.setText(chatMessage.getDateStr());
        String nameStr = chatMessage.getName();
        String imageStr = chatMessage.getImage();
        if (!TextUtils.isEmpty(nameStr)) {
            viewHolder.name.setText(nameStr);
        }
        if (!TextUtils.isEmpty(imageStr)) {
            fb.display(viewHolder.imageView, imageStr);
        }


        return convertView;
    }

    private class ViewHolder {
        public TextView createDate;
        public TextView name;
        public TextView content;
        //来源
        private TextView source;
        //图标
        private ImageView icon;
        //地址
        private TextView Url;
        private RelativeLayout newsRelative;
        RoundedCornerImageView imageView;
    }

}
