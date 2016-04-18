package com.example.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class MainActivity extends AppCompatActivity implements OnClickListener{

    // Bmob应用创建建时获取的Application id,根据自己创建的应用来写入
    private static final String BMOB_APPLICATION_ID = "be783fdda4eac8a781a9f80596e98fe0";
    private Button registerBtn, loginBtn;
    private EditText loginEt, passwordEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //初始化
        Bmob.initialize(this, "f16acca85445783472c87d72732b9b3c");
        initView();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //初始化控件

    private void initView() {
        registerBtn = (Button) this.findViewById(R.id.id_register_btn);
        loginBtn = (Button) this.findViewById(R.id.id_login_btn);
        loginEt = (EditText) this.findViewById(R.id.id_login_name_et);
        passwordEt = (EditText) this.findViewById(R.id.id_password_et);

        registerBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

    }

    //注册

    private void register() {
        String loginId = loginEt.getText().toString();
        String password = passwordEt.getText().toString();
        if (loginId.isEmpty() || password.isEmpty()) {
            ToastUtils.toast(this, "密码或账号不为空!");
            return;
        }

        final UserBean userBean = new UserBean();
        userBean.setLoginId(loginId);
        userBean.setPassword(password);
        userBean.setUserName("Bmob");
        /**
         * 保存数据到Bmob服务器
         */
        userBean.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                ToastUtils.toast(MainActivity.this, userBean.toString()
                        + " 注册成功");
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                ToastUtils.toast(MainActivity.this, arg0 + "," + arg1 + " 注册失败");
            }
        });

    }
    //登陆

    private void login() {
        String loginId = loginEt.getText().toString();
        String password = passwordEt.getText().toString();
        if (loginId.isEmpty() || password.isEmpty()) {
            ToastUtils.toast(this, "密码或账号不为空!");
            return;
        }

        BmobQuery<UserBean> userQuery = new BmobQuery<UserBean>();

        // 查询条件
        userQuery.addWhereEqualTo("loginId", loginId);
        userQuery.addWhereEqualTo("password", password);

        userQuery.findObjects(this, new FindListener<UserBean>() {

            @Override
            public void onError(int arg0, String arg1) {
                ToastUtils.toast(MainActivity.this, arg0 + "," + arg1 + " 登陆失败");
            }

            @Override
            public void onSuccess(List<UserBean> userList) {
                if (userList != null && userList.size() > 0)
                    ToastUtils.toast(MainActivity.this, " 登陆成功");
                else {
                    ToastUtils.toast(MainActivity.this, " 登陆失败");
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_register_btn:
                register();
                break;
            case R.id.id_login_btn:
                login();
                break;
        }
    }

    public class UserBean extends BmobObject{

        private static final long serialVersionUID = 1L;
        private String loginId;
        private String userName;
        private String password;
        public String getLoginId() {
            return loginId;
        }
        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }
        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        @Override
        public String toString() {
            return "UserBean [loginId=" + loginId + ", userName=" + userName
                    + ", password=" + password + "]";
        }
    }

    public static class ToastUtils {

        public static void toast(Context context,String msg){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }

        public static void toast(Context context,int msgId){
            Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
