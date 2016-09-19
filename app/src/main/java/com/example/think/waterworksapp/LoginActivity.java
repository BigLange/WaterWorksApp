package com.example.think.waterworksapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.think.waterworksapp.base_intface.LoginView;
import com.example.think.waterworksapp.custom_view.UpperActivity;
import com.example.think.waterworksapp.dialog.DialogManager;
import com.example.think.waterworksapp.model.LoginModel;
import com.example.think.waterworksapp.utils.ToastUtils;

public class LoginActivity extends UpperActivity implements View.OnFocusChangeListener,View.OnClickListener,LoginView{

    private EditText userName_edt;
    private EditText passWord_edt;
    private Button login_btn;

    private Drawable userNameIcon_blue;
    private Drawable userNameIcon_gray;
    private Drawable passWordIcon_blue;
    private Drawable passWordIcon_gray;

    private LoginModel loginModel;
//    private DialogManager dm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginModel = new LoginModel(this,this);
    }


    @Override
    protected void initView(){
        userName_edt = findView(R.id.login_username);
        passWord_edt = findView(R.id.login_edtPwd);
        login_btn = findView(R.id.login_btnLogin);
        setEditTextIcon();
    }

    @Override
    protected void initEvent() {
        userName_edt.setOnFocusChangeListener(this);
        passWord_edt.setOnFocusChangeListener(this);
        login_btn.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View view) {
        loginModel.loginInspect();
//        jumpIntent();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        int id = view.getId();
        if (id == R.id.login_username)
            setUserNameIcon(b) ;
        else if (id == R.id.login_edtPwd)
            setPassWordIcon(b);
    }

    /**
     * 判断用户名输入框是否获得焦点，焦点的改变，对应图标的改变，设置图标改变
     * @param b   获得焦点，则传入true
     */
    private void setUserNameIcon(boolean b){
        if(b)
            userName_edt.setCompoundDrawables(userNameIcon_blue,null,null,null);
        else
            userName_edt.setCompoundDrawables(userNameIcon_gray,null,null,null);
    }

    /**
     * 判断密码输入框是否获得焦点，焦点的改变，对应图标的改变，设置图标改变
     * @param b   获得焦点，则传入true
     */
    private void setPassWordIcon(boolean b){
        if(b)
            passWord_edt.setCompoundDrawables(passWordIcon_blue,null,null,null);
        else
            passWord_edt.setCompoundDrawables(passWordIcon_gray,null,null,null);
    }

    private void setEditTextIcon(){
        //控制登录用户名图标大小
        userNameIcon_gray = getResources().getDrawable(R.drawable.user_name_login_not_click);
        userNameIcon_gray.setBounds(0,0,60,60);//第一0是距左边距离，第二0是距上边距离，40分别是长宽

        passWordIcon_gray = getResources().getDrawable(R.drawable.pass_word_not_click);
        passWordIcon_gray.setBounds(0,0,60,60);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        passWord_edt.setCompoundDrawables(passWordIcon_gray,null,null,null);//只放左边

        userNameIcon_blue = getResources().getDrawable(R.drawable.user_name_logo_blue_click);
        userNameIcon_blue.setBounds(0,0,60,60);
        userName_edt.setCompoundDrawables(userNameIcon_blue,null,null,null);//只放左边

        passWordIcon_blue = getResources().getDrawable(R.drawable.pass_word_blue_click);
        passWordIcon_blue.setBounds(0,0,60,60);
    }

    @Override
    public String getUserNameValue() {
        return userName_edt.getText().toString();
    }

    @Override
    public String getPassWordValues() {
        return passWord_edt.getText().toString();
    }

    @Override
    public void jumpIntent() {
        Intent intent = new Intent(this,SelectOperationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginError(String errorMsg) {
//        if (dm==null)
//            dm = new DialogManager(this);
//        dm.showRecordingDialog(errorMsg);
        ToastUtils.showToast(this,errorMsg);
    }
}
