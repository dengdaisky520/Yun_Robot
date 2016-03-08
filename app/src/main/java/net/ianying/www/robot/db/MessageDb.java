package net.ianying.www.robot.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 聊天记录保存
 * Created by anying on 2016/3/3.
 */
public class MessageDb extends SQLiteOpenHelper {

    public MessageDb(Context context) {
        super(context, "message.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table message(_id integer primary key autoincrement,_mtext text,_mdate varchar(50),_misComMeg varchar(20),_murl text,_typeCode integer)");
    }

    /**
     * 数据库更新
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("drop table message");
            onCreate(db);
        }
    }
}
