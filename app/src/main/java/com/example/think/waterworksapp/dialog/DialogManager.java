package com.example.think.waterworksapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.think.waterworksapp.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/3/22.
 */
public class DialogManager {
    private Dialog mDialog;

    private TextView title;
    private Timer timer;
    private TimerTask timerTask;

    private Context mContext;

    private DialogShowOverListener listener;

    Handler handler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            dimissDialog();
        }
    };

    public DialogManager(Context context){
        this.mContext = context;
    }
    //显示录音的对话框
    public void showRecordingDialog(String titleValue){
        dimissDialog();
        //这里我们创建dialog，需要传入两个参数，第一个就是我们的上下文context，下一个是一个id
        //这个id是我们设置在style文件中的一个设置属性的一个style 可以过去看看到底设置了什么
        mDialog  = new Dialog(mContext, R.style.Theme_AudioDialog);
        //下面就通过LayoutInflater.from(mContext)获得inflater然后再获得我们开始专门给dialog设置的界面的view对象
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.dialog_moban, null);
        //然后我们就通过setContentView这个方法，吧我们这边View对象传过去
        mDialog.setContentView(v);
        //下面我们再通过这个mDialog来 获取我们个个组件的对象
        title = (TextView) mDialog.findViewById(R.id.dialog_title);
        title.setText(titleValue);
        //最后我们再显示出来dialog
        setDialogPosition();
        mDialog.show();
        startTimer();
        //可能这里有点不明白，为什么不使用谷歌推荐的那种 先产生一个buider对象，然后设置值后再创建dialog
        //因为我们这里就是一个提示的作用，不需要太多的和用户的交互。并且我们这里可能需要频繁的换图片(根据不同的情况)
        //所以这里直接使用这种方式创建dialog会比较好。
    }

    private void setDialogPosition() {
        Window dialogWindow = mDialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        lp.y = 1000;
//        dialogWindow.setAttributes(lp);
    }

    public void setDialogOverListener(DialogShowOverListener listener){
        this.listener = listener;
    }


    //隐藏对话框
    public void dimissDialog(){
        //判断，如果mDialog不为空，并且mDialog是显示的状态的话
        if(mDialog!=null&&mDialog.isShowing()){
            //那么让mDialog取消显示
            mDialog.dismiss();
            //让mDialog指向为空
            mDialog = null;
            stopTimer();
        }
    }

    private void startTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        timer.schedule(timerTask,1000);
    }

    private void stopTimer(){
        if (listener != null){
            listener.dialogShowOver();
        }
        if (timer==null)
            return;
        timer.cancel();
        timer = null;
        timerTask = null;
    }


    public interface DialogShowOverListener{
        void dialogShowOver();
    }
}
