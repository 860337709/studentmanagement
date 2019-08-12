package com.example.he.studenmanagement.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he.studenmanagement.R;
import com.example.he.studenmanagement.tools.myDatabaseHelper;

/**
 * 管理员登录界面
 */
public class admin_login_activity extends Activity {
    private EditText name;//用户名
    private EditText password;//用户密码
    private Button login;//登录按钮
    private TextView register;//注册
    private TextView forgetNum;//忘记密码
    private myDatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.admin_login_layout);

        dbHelper = myDatabaseHelper.getInstance(this);


        name = (EditText) findViewById(R.id.admin_login_activity_name_input);
        password = (EditText) findViewById(R.id.admin_login_activity_password_input);
        login = (Button) findViewById(R.id.admin_login_activity_login);
        register = (TextView) findViewById(R.id.admin_login_activity_register);
        forgetNum = (TextView) findViewById(R.id.admin_login_activity_forgetNum);

        //跳转到登录过的管理员界面
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameInfo = name.getText().toString();
                String passwordInfo = password.getText().toString();
                //从数据库中获取密码并判断是否相同
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.rawQuery("select password from admin where name=?", new String[]{nameInfo});
                String pi = null;
                if (cursor.moveToNext()) {
                    pi = cursor.getString(cursor.getColumnIndex("password"));//获取密码
                }
                //密码正确跳转到登录后的界面
                if (passwordInfo.equals(pi)) {
                    Intent intent = new Intent(admin_login_activity.this, admin_activity.class);
                    startActivity(intent);
                    cursor.close();
                } else {
                    Toast.makeText(admin_login_activity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //自定义AlertDialog用于注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(admin_login_activity.this);
                LayoutInflater factory = LayoutInflater.from(admin_login_activity.this);
                final View textEntryView = factory.inflate(R.layout.register, null);
                builder.setTitle("用户注册");
                builder.setView(textEntryView);
                final EditText name = (EditText) textEntryView.findViewById(R.id.admin_register_name);
                final EditText firstPassword = (EditText) textEntryView.findViewById(R.id.admin_register_first_password);
                final EditText secondPassword = (EditText) textEntryView.findViewById(R.id.admin_register_second_password);


                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            String nameInfo = name.getText().toString();
                            String firstPasswordInfo = firstPassword.getText().toString();
                            String secondPasswordInfo = secondPassword.getText().toString();
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            //检测密码是否为6个数字
                            if (firstPasswordInfo.matches("[0-9]{6}")) {
                                // 两次密码是否相同
                                if (firstPasswordInfo.equals(secondPasswordInfo)) {
                                    Cursor cursor = db.rawQuery("select name from admin where name=? ", new String[]{nameInfo});
                                    //用户是否存在
                                    if (cursor.moveToNext()) {
                                        Toast.makeText(admin_login_activity.this, "该用户已存在", Toast.LENGTH_SHORT).show();
                                    } else {
                                        db.execSQL("insert into admin(name,password)values(?,?)", new String[]{nameInfo, firstPasswordInfo});
                                    }
                                } else {
                                    Toast.makeText(admin_login_activity.this, "两次密码不相同", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(admin_login_activity.this, "密码为6位纯数字", Toast.LENGTH_SHORT).show();
                            }



                    }
                });


                builder.create().show();

            }
        });

        forgetNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(admin_login_activity.this, "此功能暂不支持", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
