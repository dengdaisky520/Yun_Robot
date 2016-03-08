package net.ianying.www.robot.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import net.ianying.www.robot.R;
import net.ianying.www.robot.dao.MessageDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 设置界面
 * Created by anying on 2016/3/2.
 */
public class SetingActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView set_about;
   // private TextView set_nameAndImg;
    private TextView set_help;
    private TextView set_checkUpdate;
    private TextView set_clearMessage;
    private MessageDao messageDao;
    private static final String TAG ="SetingActivity" ;
    //下载进度条
    private ProgressBar download_progress;
    //版本号
    private int verCode;
    //版本名称
    private String verName;
    private String desc="";
    private String url="";
    //下载和安装路径
    private final  static String path= Environment.getExternalStorageDirectory()+"/xiaoyun/yun.apk";
    private ProgressDialog progressDialog;
    private ProgressDialog waitpressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化界面
        initView();
        set_about.setOnClickListener(this);
        // TODO: 2016/3/4 后续功能添加
       //set_nameAndImg.setOnClickListener(this);
        set_help.setOnClickListener(this);
        set_checkUpdate.setOnClickListener(this);
        set_clearMessage.setOnClickListener(this);
        //初始化数据库
        messageDao=new MessageDao(getApplicationContext());
        //设置广告条
       // setupBannerAd();
    }

    /**
     * 初始化界面
     */
    private void initView() {

       setContentView(R.layout.chat_seting);
        //关于
        set_about = (TextView) findViewById(R.id.set_about);
        //用户图片与姓名
      //  set_nameAndImg = (TextView) findViewById(R.id.set_nameAndImg);
        //帮助
        set_help = (TextView) findViewById(R.id.set_help);
        //检查更新
        set_checkUpdate = (TextView) findViewById(R.id.set_checkUpdate);
        //清空聊天记录
        set_clearMessage = (TextView) findViewById(R.id.set_clearMessage);
        //下载进度条
        download_progress= (ProgressBar) findViewById(R.id.download_progress);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
           /* case R.id.set_nameAndImg: {
                break;
            }*/
            //检查更新
            case R.id.set_checkUpdate: {
                //显示ProgressDialog
                waitpressDialog = ProgressDialog.show(this, "检查中", "正在检查中，请稍后", true, false);
                getVersion();
                RequestQueue mQueue = Volley.newRequestQueue(this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://www.ayblogs.com/yunVersion.txt", null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                   String getVersion= response.getString("version");
                                    desc=response.getString("desc");
                                    url=response.getString("url");
                                    if(!getVersion.equals(verName)){
                                        waitpressDialog.dismiss();
                                        showUpdateDialog();
                                    }else{
                                        Toast.makeText(getApplicationContext(),"当前是最新版本",Toast.LENGTH_SHORT).show();
                                        waitpressDialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d(TAG, response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                });
                mQueue.add(jsonObjectRequest);

                break;
            } case R.id.set_clearMessage: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("确认清空聊天记录吗?");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            messageDao.deleteAllData();
                            Toast.makeText(getApplicationContext(), "聊天记录清除成功", Toast.LENGTH_SHORT).show();
                            setResult(200);
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"系统错误!",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        return;
                    }
                });
                builder.create().show();
                break;
            } case R.id.set_help: {
                Intent intent=new Intent();
                intent.setClass(this,HelpActivity.class);
                startActivity(intent);
                break;
            } case R.id.set_about: {
                Intent intent=new Intent();
                intent.setClass(this, AboutActivity.class);
                startActivity(intent);
                break;
            }
        }
    }


    /**
     * 获取自己的版本
     */
    private void getVersion() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            verCode = packageInfo.versionCode;
            verName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     *更新对话框
     */
    private void showUpdateDialog() {
        String descs = null;
        try {
            descs = URLDecoder.decode(desc, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提醒")
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                })
                .setMessage("是否更新新版本？新版本的具有如下特性:" + descs)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //下载最新APK
                       downLoadApk();

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //进入主界面
                dialog.dismiss();
            }
        });
             builder.show();
    }

    /**
     * 下载最新APK
     */
    private void downLoadApk() {
        HttpUtils httpUtils = new HttpUtils();
        // 先删除掉xx.apk
        File file = new File(path);
        file.delete();// 删除文件
        progressDialog=new ProgressDialog(this);
        httpUtils.download(url, path, new RequestCallBack<File>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                progressDialog.setTitle("更新客户端。");
                progressDialog.setMessage("客户端总大小:" + formatSize(total) + "\n正在下载中，请稍后。。");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置进度条对话框//样式（水平，旋转）
                //进度最大值
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) current);// 设置当前进度
                //显示
                progressDialog.show();

            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Toast.makeText(getApplicationContext(), "新版本下载成功!", Toast.LENGTH_LONG).show();
                installApk();
                //安装隐藏进度条
                progressDialog.dismiss();
                download_progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(getApplicationContext(), "新版本下载失败!", Toast.LENGTH_LONG).show();
                download_progress.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 安装APK方法
     */
    private void installApk() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        String type = "application/vnd.android.package-archive";
        Uri data = Uri.fromFile(new File(path));
        intent.setDataAndType(data, type);
        startActivityForResult(intent, 0);

    }

    private String formatSize(long size)
    {
        return Formatter.formatFileSize(this, size);
    }
}
