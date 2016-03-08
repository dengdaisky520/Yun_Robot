package net.ianying.www.robot.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.ianying.www.robot.db.MessageDb;
import net.ianying.www.robot.entity.ChatMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 聊天记录封装业务类
 * Created by anying on 2016/3/3.
 */
public class MessageDao {
    private MessageDb messageDb;

    public MessageDao(Context context) {
        this.messageDb = new MessageDb(context);
    }

    /**
     * 获取所有聊天记录
     *
     * @return
     */
    public List<ChatMessage> getAllData() {
        List<ChatMessage> list = new ArrayList<ChatMessage>();
        SQLiteDatabase database = messageDb.getWritableDatabase();
        Cursor cursor = database.rawQuery("select _id,_mtext,_mdate,_misComMeg,_murl,_typeCode from message", null);
        ChatMessage chatMessage;
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (cursor.moveToNext()) {
            chatMessage = new ChatMessage();
            // TODO: 2016/3/3
            //消息信息
            chatMessage.setMsg(cursor.getString(1));
            //状态码
            chatMessage.setTypeCode(cursor.getInt(5));
            if (chatMessage.getTypeCode() == 200000) {
                chatMessage.setUrl(cursor.getString(4));
            }
            // input代表小云，output代表自己

            if (cursor.getString(3).equals("INPUT")) {
                chatMessage.setType(ChatMessage.Type.INPUT);
            } else {
                chatMessage.setType(ChatMessage.Type.OUTPUT);
            }
            //时间
            String getDate = cursor.getString(2);
            try {
                Date date = sdformat.parse(getDate);
                chatMessage.setDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            list.add(chatMessage);

        }
        cursor.close();
        database.close();
        return list;
    }


    /**
     * 添加聊天记录
     *
     * @param chatMessages
     */
    public void add(ChatMessage chatMessages) {
        SQLiteDatabase sqLiteDatabase = messageDb.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

            contentValues.put("_mtext", chatMessages.getMsg());
            contentValues.put("_mdate", chatMessages.getDateStr());
            if (chatMessages.getTypeCode() == 200000) {
                contentValues.put("_murl", chatMessages.getUrl());
            }
            if (chatMessages.getType().toString().equals("INPUT")){
                contentValues.put("_misComMeg","INPUT");
            }else{
                contentValues.put("_misComMeg","OUTPUT");
            }
             contentValues.put("_typeCode",chatMessages.getTypeCode());
            sqLiteDatabase.insert("message", null, contentValues);

        sqLiteDatabase.close();
    }

    /**
     * 清空所有聊天记录
     */
    public void deleteAllData(){
        SQLiteDatabase sqLiteDatabase = messageDb.getWritableDatabase();
        //sqLiteDatabase.delete("message",null,null);
        sqLiteDatabase.execSQL("delete from message");
        sqLiteDatabase.close();
    }
}
