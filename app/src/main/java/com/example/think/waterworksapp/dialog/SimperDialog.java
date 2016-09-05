package com.example.think.waterworksapp.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Administrator on 2016/5/17.
 */
public class SimperDialog {
    private ProgressDialog dialog;

    public SimperDialog(Context context){
        dialog = new ProgressDialog(context);
    }



    public SimperDialog setTitle(String title){
        dialog.setTitle(title);
        return this;
    }

    public SimperDialog setContent(String content){
        dialog.setMessage(content);
        return this;
    }

    public SimperDialog showDialog(){
        dialog.show();
        return this;
    }

    public SimperDialog hideDialog(){
        dialog.dismiss();
        return this;
    }

    public void dialogDismissLitsener(DialogInterface.OnDismissListener listener){
        dialog.setOnDismissListener(listener);
    }


}
