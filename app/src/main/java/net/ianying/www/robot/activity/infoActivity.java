package net.ianying.www.robot.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.socks.library.KLog;

import net.ianying.www.robot.R;
import net.ianying.www.robot.utils.ImageTools;
import net.ianying.www.robot.utils.SpTools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 设置我的图像和昵称
 * Created by anying on 2016/3/9.
 */
public class infoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private static final int CROP = 2;
    private static final int CROP_PICTURE = 3;
    private static final int SCALE = 5;//照片缩小比例
    private ImageView iv_image = null;
    private Button upImage;
    private Button upImages;
    private Button upInfo;
    private EditText nichen;
    private String spFileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化界面
        initView();
        upImage.setOnClickListener(this);
        upImages.setOnClickListener(this);
        upInfo.setOnClickListener(this);
    }

    /**
     *
     */
    private void initView() {
        setContentView(R.layout.chat_info);
        setTitle("anying");
        iv_image = (ImageView) this.findViewById(R.id.img);
        upImage = (Button) findViewById(R.id.btn);
        upImages = (Button) findViewById(R.id.btn2);
        upInfo = (Button) findViewById(R.id.upInfo);
        nichen = (EditText) findViewById(R.id.nichen);
    }

    /**
     * 选择照片处理完成后回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    KLog.d("TAKE_PICTURE");
                    //将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/image.png");
                    Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();

                    //将处理过的图片显示在界面上，并保存到本地
                    iv_image.setImageBitmap(newBitmap);
                    spFileName = String.valueOf(System.currentTimeMillis());
                    if (!TextUtils.isEmpty(SpTools.getString(this, "path", ""))) {
                        ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiaoyun", SpTools.getString(this, "path", ""));
                        ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiaoyun", spFileName);
                    } else {
                        ImageTools.savePhotoToSDCard(newBitmap, Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiaoyun", spFileName);
                    }
                    break;

                case CHOOSE_PICTURE:
                    KLog.d("CHOOSE_PICTURE");
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();
                    try {
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photo != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();
                            spFileName = String.valueOf(System.currentTimeMillis());
                            //保存之前判断是否存在以前的图片,有就删除
                            if (!TextUtils.isEmpty(SpTools.getString(this, "path", ""))) {
                                ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiaoyun", SpTools.getString(this, "path", ""));
                                ImageTools.savePhotoToSDCard(smallBitmap, Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiaoyun", spFileName);
                            } else {
                                ImageTools.savePhotoToSDCard(smallBitmap, Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiaoyun", spFileName);
                        }
                            iv_image.setImageBitmap(smallBitmap);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case CROP:
                    KLog.d("CROP");
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                        KLog.d("Date");
                    } else {
                        KLog.d("File");
                        String fileName = getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE).getString("tempName", "");
                        uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
                    }
                    cropImage(uri, 500, 500, CROP_PICTURE);
                    break;

                case CROP_PICTURE:
                    KLog.d("CROP_PICTURE");
                    Bitmap photo = null;
                    Uri photoUri = data.getData();
                    if (photoUri != null) {
                        photo = BitmapFactory.decodeFile(photoUri.getPath());
                    }
                    if (photo == null) {
                        Bundle extra = data.getExtras();
                        if (extra != null) {
                            photo = (Bitmap) extra.get("data");
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        }
                    }
                    iv_image.setImageBitmap(photo);
                    break;
                default:
                    break;
            }
        }
    }

    //截取图片
    public void cropImage(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, requestCode);
    }

    public void showPicturePicker(Context context, boolean isCrop) {
        final boolean crop = isCrop;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("个性图像来源");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照上传", "本地照片上传"}, new DialogInterface.OnClickListener() {
            //类型码
            int REQUEST_CODE;

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case TAKE_PICTURE:
                        Uri imageUri = null;
                        String fileName = null;
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (crop) {
                            REQUEST_CODE = CROP;
                            //删除上一次截图的临时文件
                            SharedPreferences sharedPreferences = getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE);
                            ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiaoyun", sharedPreferences.getString("tempName", ""));

                            //保存本次截图临时文件名字
                            fileName = String.valueOf(System.currentTimeMillis()) + ".png";
                            spFileName = fileName;
                            KLog.d("照片临时名称", fileName);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("tempName", fileName);
                            editor.commit();
                        } else {
                            REQUEST_CODE = TAKE_PICTURE;
                            fileName = "image.png";
                        }
                        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
                        //指定照片保存路径（SD卡），image.png为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(openCameraIntent, REQUEST_CODE);
                        break;

                    case CHOOSE_PICTURE:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        if (crop) {
                            REQUEST_CODE = CROP;
                        } else {
                            REQUEST_CODE = CHOOSE_PICTURE;
                        }
                        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(openAlbumIntent, REQUEST_CODE);
                        break;

                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //不裁剪图片上传
            case R.id.btn: {

                showPicturePicker(this, false);
                break;
            }
            //裁剪图片上传
            case R.id.btn2: {
                showPicturePicker(this, true);
                break;
            }
            //提交信息
            case R.id.upInfo: {
                //判断昵称
                String getNichen = nichen.getText().toString().trim();
                if (TextUtils.isEmpty(getNichen)) {
                    Toast.makeText(getApplicationContext(), "昵称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(spFileName)) {

                    Toast.makeText(getApplicationContext(), "图像不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //昵称
                SpTools.putString(this, "nichen", getNichen);
                //路径
                SpTools.putString(this, "path", Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiaoyun/" + spFileName + ".png");
                //文件名
                SpTools.putString(this, "filename", spFileName + ".png");
                Toast.makeText(getApplicationContext(), "图像和昵称设置成功", Toast.LENGTH_SHORT).show();


            }
        }
    }
}
