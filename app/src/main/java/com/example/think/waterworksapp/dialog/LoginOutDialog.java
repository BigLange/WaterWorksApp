package com.example.think.waterworksapp.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.think.waterworksapp.LoginActivity;
import com.example.think.waterworksapp.R;
import com.example.think.waterworksapp.utils.ActivityPreservationUtils;

/**
 * Created by Think on 2016/9/13.
 */

public class LoginOutDialog implements DialogInterface.OnClickListener {

    private AlertDialog dialog;
    private static LoginOutDialog loginOutDialog;
    private Context context;

    private LoginOutDialog(){}

    public static LoginOutDialog getLoginOutDialog(Context context){
        if (loginOutDialog==null)
            loginOutDialog = new LoginOutDialog();
        loginOutDialog.context = context;
        return loginOutDialog;
    }

    private void createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("登出警告")
                .setIcon(R.drawable.login_out_waring_icon)
                .setMessage("您的账号已无权限，请重新登录！")
                .setCancelable(false)
                .setPositiveButton("我知道了",this);
        dialog = builder.create();
    }

    public void showDialog(){
        if (dialog==null)
            createDialog();
        dialog.show();
    }

    private void dismiss(){
        if (dialog==null)
            return;
        dialog.dismiss();
        dialog = null;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        dismiss();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ActivityPreservationUtils.finshAllActivity();
        LoginOutDialog.clearLoginOutDialog();
    }

    public static void clearLoginOutDialog(){
        if (loginOutDialog==null)
            return;
        loginOutDialog.dismiss();
        loginOutDialog = null;
    }
}
