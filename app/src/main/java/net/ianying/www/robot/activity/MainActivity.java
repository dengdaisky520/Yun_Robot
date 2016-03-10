package net.ianying.www.robot.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;
import com.socks.library.KLog;

import net.ianying.www.robot.R;
import net.ianying.www.robot.adapter.ChatMessageAdapter;
import net.ianying.www.robot.dao.MessageDao;
import net.ianying.www.robot.entity.ChatMessage;
import net.ianying.www.robot.utils.HttpUtils;
import net.ianying.www.robot.utils.SpTools;
import net.ianying.www.robot.utils.SpeechUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    /**
     * 展示消息的listview
     */
    private ListView mChatView;
    /**
     * 文本域
     */
    private EditText mMsg;
    String API_KEY = "aGn468si1bWuK6usOg2Gvej6";
    String SECRET_KEY = "e5e80eb9975bd69832ffec27cdadc628";
    /**
     * 语音按钮
     */
    private TextView tv_yuying;
    private ImageButton btn_yuying;
    private Button setButton;
    //百度语音识别对话框
    BaiduASRDigitalDialog mDialog = null;
    DialogRecognitionListener mDialogListener = null;
    private SensorManager mSensorManager;
    /**
     * 存储聊天消息
     */
    private List<ChatMessage> mDatas = new ArrayList<ChatMessage>();
    /**
     * 适配器
     */
    private ChatMessageAdapter mAdapter;

    //聊天记录业务类
    private MessageDao messageDao;
    //获取数据
    // private List<ChatMessage> mDatas = messageDao.getAllData();
    boolean isyuying = true;
    private SpeechUtil speechUtil;

    int ret = 0;// 函数调用返回值
    private Context mContext;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            ChatMessage from = (ChatMessage) msg.obj;
            mDatas.add(from);
            messageDao.add(from);
            mAdapter.notifyDataSetChanged();
            mChatView.setSelection(mDatas.size() - 1);
        }

        ;
    };

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mContext = this;
        messageDao = new MessageDao(getApplicationContext());
        //初始化语音朗读
        speechUtil = new SpeechUtil(this);
        //初始化界面
        initView();
        //初始化聊天记录业务类
        mAdapter = new ChatMessageAdapter(this, mDatas);
        mChatView.setAdapter(mAdapter);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Videos();
        //设置广告条
        setupBannerAd();

    }

    /**
     * 设置广告条广告
     */
    private void setupBannerAd() {
//		/**
        //		 * 普通布局
        //		 */
        //		//　实例化广告条
      /*  AdView adView = new AdView(mContext, AdSize.FIT_SCREEN);
        RelativeLayout bannerLayout = (RelativeLayout) findViewById(R.id.adrelative);
        bannerLayout.addView(adView);
        // 监听广告条接口
        adView.setAdListener(new AdViewListener() {

            @Override
            public void onSwitchedAd(AdView adView) {
                Log.i(TAG, "广告条切换");
            }

            @Override
            public void onReceivedAd(AdView adView) {
                Log.i(TAG, "请求广告条成功");
            }

            @Override
            public void onFailedToReceivedAd(AdView adView) {
                Log.i(TAG, "请求广告条失败");
            }
        });
        // 调用Activity的addContentView函数
        bannerLayout.addView(adView);*/

    }

    private void initView() {

        mChatView = (ListView) findViewById(R.id.message_list);
        mMsg = (EditText) findViewById(R.id.et_message);
        tv_yuying = (TextView) findViewById(R.id.tv_yuying);
        btn_yuying = (ImageButton) findViewById(R.id.btn_yuying);
        //初始化朗读
        mChatView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getText = mDatas.get(position).getMsg();
                speechUtil.speak(getText);
            }
        });


        //获取数据
        List<ChatMessage> allData = messageDao.getAllData();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        if (allData.size() != 0) {
            for (int i = 0; i < allData.size(); i++) {
                if (allData.get(i).getDateStr() != null) {
                    allData.get(i).setDateStr(allData.get(i).getDateStr());
                }
                if (allData.get(i).getType().toString().equals("INPUT")) {
                    allData.get(i).setType(ChatMessage.Type.INPUT);
                } else {
                    allData.get(i).setType(ChatMessage.Type.OUTPUT);
                }
                if (allData.get(i).getTypeCode() == 200000) {
                    // TODO: 2016/3/4  加入链接
                }
                mDatas.add(allData.get(i));
            }
        } else {

                mDatas.add(new ChatMessage(ChatMessage.Type.INPUT, "你好，我是小云，很高兴为您服务", "", ""));
            //保存第一条对话
            messageDao.add(mDatas.get(mDatas.size() - 1));
        }
    }

    /**
     * 切换
     *
     * @param view
     */
    public void imbtnYuyin(View view) {
        if (isyuying) {
            mMsg.setVisibility(View.GONE);
            tv_yuying.setVisibility(View.VISIBLE);
            btn_yuying.setImageResource(R.mipmap.keyboard);
            isyuying = false;
        } else {
            mMsg.setVisibility(View.VISIBLE);
            tv_yuying.setVisibility(View.GONE);
            btn_yuying.setImageResource(R.mipmap.yuying);
            isyuying = true;
        }

    }

    /**
     * 发送按钮
     *
     * @param view
     */
    public void sendMessage(View view) {
        final String msg = mMsg.getText().toString();
        sendMessage(msg);

    }

    private void sendMessage(final String msg) {
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(this, "空消息不能发送...", Toast.LENGTH_SHORT).show();
            return;
        }

        ChatMessage to = new ChatMessage(ChatMessage.Type.OUTPUT, msg,
                SpTools.getString(this,"nichen",""),
                SpTools.getString(this, "path", ""));
        KLog.d(SpTools.getString(this, "path", ""));
        to.setDate(new Date());
        mDatas.add(to);
        messageDao.add(to);
        mAdapter.notifyDataSetChanged();
        mChatView.setSelection(mDatas.size() - 1);

        mMsg.setText("");

        // 关闭软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }

        new Thread() {
            public void run() {
                ChatMessage from = null;
                try {
                    from = HttpUtils.sendMsg(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    from = new ChatMessage(ChatMessage.Type.INPUT, "服务器连接中断...", "", "");
                }
                Message message = Message.obtain();
                message.obj = from;
                mHandler.sendMessage(message);
            }

            ;
        }.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
        }
        mDialogListener = null;
        speechUtil.cancle();


        System.exit(0);
    }

    /**
     * 语音识别方法
     */
    private void Videos() {
        if (mDialog == null) {
            if (mDialog != null) {
                mDialog.dismiss();
            }
            Bundle params = new Bundle();
            //设置API_KEY, SECRET_KEY
            params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, API_KEY);
            params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, SECRET_KEY);
            //默认输入模式
            params.putInt(BaiduASRDigitalDialog.PARAM_PROP, VoiceRecognitionConfig.PROP_INPUT);
            //设置语音识别对话框为蓝色高亮主题
            params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_BLUE_LIGHTBG);
            //默认语音类型中文普通话
            params.putString(BaiduASRDigitalDialog.PARAM_LANGUAGE, VoiceRecognitionConfig.LANGUAGE_CHINESE);
            //实例化百度语音识别对话框
            mDialog = new BaiduASRDigitalDialog(this, params);
            //设置百度语音识别回调接口
            mDialogListener = new DialogRecognitionListener() {
                @Override
                public void onResults(Bundle mResults) {
                    ArrayList<String> rs = mResults != null ? mResults.getStringArrayList(RESULTS_RECOGNITION) : null;
                    if (rs != null && rs.size() > 0) {
                        final String msg = rs.get(0);
                        ChatMessage too = new ChatMessage(ChatMessage.Type.OUTPUT, msg,  SpTools.getString(MainActivity.this,"nichen",""),
                                SpTools.getString(MainActivity.this, "path", ""));
                        too.setDate(new Date());
                        mDatas.add(too);
                        messageDao.add(too);
                        mAdapter.notifyDataSetChanged();
                        mChatView.setSelection(mDatas.size() - 1);
                        new Thread() {
                            public void run() {
                                ChatMessage from = null;
                                try {
                                    from = HttpUtils.sendMsg(msg);
                                } catch (Exception e) {
                                    from = new ChatMessage(ChatMessage.Type.INPUT, "服务器正在做俯卧撑，估计累趴了~", "", "");
                                }
                                Message message = Message.obtain();
                                message.obj = from;
                                mHandler.sendMessage(message);
                            }

                            ;
                        }.start();
                    }
                }

            };
            mDialog.setDialogRecognitionListener(mDialogListener);
        }
        //设置语音识别模式为输入模式
        //  mDialog.setSpeechMode(BaiduASRDigitalDialog.SPEECH_MODE_INPUT);
        //禁用语义识别
        mDialog.getParams().putBoolean(BaiduASRDigitalDialog.PARAM_NLU_ENABLE, false);
        //界面元素
        tv_yuying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
            }
        });

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        int sensorType = event.sensor.getType();

        // values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        float[] values = event.values;
        int sum = 0;
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            /*
             * 因为一般正常情况下，任意轴数值最大就在9.8~10之间，只有在你突然摇动手机的时候，瞬时加速度才会突然增大或减少。
			 * 所以，经过实际测试，只需监听任一轴的加速度大于14的时候，改变你需要的设置就OK了~~~
			 */
            if ((Math.abs(values[0]) > 14 || Math.abs(values[1]) > 14 || Math
                    .abs(values[2]) > 14)) {
                //手机摇的次数必须大于三次然后显示动画
                sum++;
                // TODO: 2016/3/1
                if (sum % 3 == 0) {
                    mChatView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                    mDatas.add(new ChatMessage(ChatMessage.Type.INPUT, "不要摇了，小云要晕了~", "", ""));
                    messageDao.add(mDatas.get(mDatas.size() - 1));
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 加速度传感器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                // 还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，
                // 根据不同应用，需要的反应速率不同，具体根据实际情况设定
                SensorManager.SENSOR_DELAY_NORMAL);
    }


    /**
     * 执行退出提示操作
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == event.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("确认要退出吗?");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                  /*  List<ChatMessage> allData = messageDao.getAllData();
                    //如果没有输入就退出，直接关闭自己
                    if (allData.size() == mDatas.size()) {
                        finish();
                    }
                    if (allData.size() != 0) {
                        for (int i = allData.size(); i < mDatas.size(); i++) {
                            messageDao.add(mDatas.get(i));
                        }
                    } else {
                        for (int i = 0; i < mDatas.size(); i++) {
                            messageDao.add(mDatas.get(i));
                        }
                    }*/

                    finish();

                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    return;
                }
            });
            builder.create().show();

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 进入设置界面
     *
     * @param view
     */
    public void chatMenu(View view) {
        Intent startSeting = new Intent();
        startSeting.setClass(this, SetingActivity.class);
        startActivityForResult(startSeting, 100);
    }

    /**
     * 清除聊天记录
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            if (requestCode == 100) {
                mDatas = new ArrayList<ChatMessage>();
                mAdapter = new ChatMessageAdapter(this, mDatas);
                mAdapter.notifyDataSetChanged();
                mChatView.setAdapter(mAdapter);
            }
        }
    }
}
