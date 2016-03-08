package net.ianying.www.robot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.ianying.www.robot.R;

/**
 * 欢迎界面
 * Created by anying on 2016/3/3.
 */
// TODO: 2016/3/7  暂时不支持6.0 
public class SplashActivity extends AppCompatActivity {
   // private static final int REQUEST_CODE = 500;
    private ImageView splash_imageView;
    private LinearLayout splash_linear;
  //  private PermissionHelper mPermissionHelper;
    private static final String TAG = "Splash";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化界面
        initView();
        initAnimation();
      /*  mPermissionHelper = new PermissionHelper(this);
        mPermissionHelper.setOnApplyPermissionListener(new PermissionHelper.OnApplyPermissionListener() {
            @Override
            public void onAfterApplyAllPermission() {
                Log.i(TAG, "All of requested permissions has been granted, so run app logic.");
                loadMain();
            }
        });
        if (Build.VERSION.SDK_INT < 23) {
            //如果系统版本低于23，直接跑应用的逻辑
            Log.d(TAG, "The api level of system is lower than 23, so run app logic directly.");
            initAnimation();
        } else {
            //如果权限全部申请了，那就直接跑应用逻辑
            if (mPermissionHelper.isAllRequestedPermissionGranted()) {
                Log.d(TAG, "All of requested permissions has been granted, so run app logic directly.");
                initAnimation();
            } else {
                //如果还有权限为申请，而且系统版本大于23，执行申请权限逻辑
                Log.i(TAG, "Some of requested permissions hasn't been granted, so apply permissions first.");
                mPermissionHelper.applyPermissions();
            }
        }*/


    }

    /**
     * 初始化渐变动画
     */
    private void initAnimation() {
        //0表示完全透明，1表示完全显示
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        //持续时间3
        alphaAnimation.setDuration(3000);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loadMain();
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splash_linear.setAnimation(alphaAnimation);
    }

    /**
     * 加载主界面
     */
    private void loadMain() {
       /* if(!Settings.System.canWrite(getApplicationContext())&&Build.VERSION.SDK_INT==23){
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE);
        }*/
        Intent startMain=new Intent();
        startMain.setClass(this, MainActivity.class);
        startActivity(startMain);
    }



    /**
     * 初始化界面
     */
    private void initView() {
        setContentView(R.layout.activity_splash);
        splash_imageView= (ImageView) findViewById(R.id.splash_id);
        splash_linear= (LinearLayout) findViewById(R.id.splash_linear);
    }


   /* @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_CODE==requestCode){
            if (Settings.System.canWrite(this)) {
                //检查返回结果
                Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
            }
        }
        mPermissionHelper.onActivityResult(requestCode, resultCode, data);

    }*/
}
