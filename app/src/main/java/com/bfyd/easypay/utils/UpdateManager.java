package com.bfyd.easypay.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bfyd.easypay.R;
import com.bfyd.easypay.entity.UpdateEntity;
import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * 版本更新管理
 * Created by zyk on 2016/4/28.
 */
public class UpdateManager {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private Activity mActivity;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    //是否显示提示对话框(有更新提示，下载进度，安装提示等)
    private boolean isShowDialog = true;

    private UpdateEntity mUpdate;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                default:
                    break;
            }
        }
    };

    public UpdateManager(Activity context) {
        this.mActivity = context;
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate() {
        Runnable r = new Runnable(){
            @Override
            public void run() {
                if (isUpdate()) {
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    File file = new File(mSavePath, mUpdate.apkName);
                    if(file.exists()) {
                        installApk();
                    } else {
                        if (isShowDialog) {
                            // 显示提示对话框
                            mActivity.runOnUiThread(new Runnable(){
                                @Override
                                public void run() {
                                    showNoticeDialog();
                                }
                            });
                        } else {
                            //自动下载
                            downloadApk();
                        }
                    }
                }
            }
        };
        new Thread(r).start();
    }

    /**
     * 检查软件是否有更新版本
     * @return
     */
    private boolean isUpdate() {
        // 获取当前软件版本
        int versionCode = getVersionCode();
        mUpdate = requestByGet();
        // 版本判断
        if (mUpdate.versionCode > versionCode) {
            return true;
        }
        return false;
    }

    /**
     * 获取软件版本号
     */
    private int getVersionCode() {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("更新");
        builder.setMessage("发现新版本:"+mUpdate.versionName);
        // 更新
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("下载");
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mActivity);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 现在文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            FileOutputStream fos = null;
            InputStream is = null;
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(mUpdate.downloadURL);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, mUpdate.apkName);
                    fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                close(fos, is);
                if (isShowDialog) {
                    // 取消下载对话框显示
                    mDownloadDialog.dismiss();
                }
            } catch (IOException e) {
                e.printStackTrace();
                close(fos, is);
                if (isShowDialog) {
                    // 取消下载对话框显示
                    mDownloadDialog.dismiss();
                }
            }

            if (isShowDialog) {
                // 取消下载对话框显示
                mDownloadDialog.dismiss();
            }
        }
    }

    private void close(FileOutputStream fos, InputStream is){
        if(fos != null){
            try {
                fos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if( is != null) {
            try {
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 安装APK文件
     */
    private void installApk() {
            File apkfile = new File(mSavePath, mUpdate.apkName);
            if (!apkfile.exists()) {
                return;
            }
            // 通过Intent安装APK文件
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
            mActivity.startActivity(i);
    }

    private UpdateEntity requestByGet(){
        UpdateEntity update = new UpdateEntity();
        System.out.println("requestByGet");
        // Get方式请求
        try {
            // 新建一个URL对象
            URL url = new URL(NetworkUtils.UpdateURL);
            System.out.println("url:"+NetworkUtils.UpdateURL);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                byte[] data = readStream(urlConn.getInputStream());
                urlConn.disconnect();

                Gson gson = new Gson();
                String result = new String(data, "UTF-8");
                update = gson.fromJson(result, UpdateEntity.class);
                System.out.println("result:"+result+"\n"+update.toString());
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return update;
    }

    // 获取连接返回的数据
    private byte[] readStream(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        byte[] data = baos.toByteArray();
        inputStream.close();
        baos.close();
        return data;
    }

}
