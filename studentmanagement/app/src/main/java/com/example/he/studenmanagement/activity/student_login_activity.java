package com.example.he.studenmanagement.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.he.studenmanagement.R;
import com.example.he.studenmanagement.tools.myDatabaseHelper;

/**
 * 学生登录界面
 */
public class student_login_activity extends Activity {
    private EditText name;
    private EditText password;
    private Button login;
    private myDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.student_login_layout);

        name = (EditText) findViewById(R.id.student_login_activity_name_input);
        password = (EditText) findViewById(R.id.student_login_activity_password_input);
        login = (Button) findViewById(R.id.student_login_activity_login);

        dbHelper = myDatabaseHelper.getInstance(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String studentId = name.getText().toString();
                String studentPassword = password.getText().toString();
                if (!TextUtils.isEmpty(studentId) && !TextUtils.isEmpty(studentPassword)) {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.rawQuery("select password from student where id=?", new String[]{studentId});
                    if (cursor.moveToNext()) {
                        String password = cursor.getString(cursor.getColumnIndex("password"));
                        if (password.equals(studentPassword)) {
                            //启动学生登录后的界面并将学生的账户（id）传过去
                            Intent intent = new Intent(student_login_activity.this, student_activity.class);
                            intent.putExtra("id", name.getText().toString());
                            startActivity(intent);
                        } else {
                            Toast.makeText(student_login_activity.this, "密码错误请重新输入", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(student_login_activity.this, "该学号未注册，请联系班主任", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(student_login_activity.this, "帐户，密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
