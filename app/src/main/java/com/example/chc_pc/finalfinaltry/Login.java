package com.example.chc_pc.finalfinaltry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

    private String Sname,Spass,name,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);




        Sname = "chc";
        Spass = "123456";

        findViewById(R.id.denglu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editName,editPass;

                editName = (EditText) findViewById(R.id.yonghuming);
                editPass = (EditText) findViewById(R.id.mima);
                name = editName.getText().toString();
                pass = editPass.getText().toString();
                if(!name.equals(Sname)) {
                    if(name.length()==0)
                        Toast.makeText(Login.this,"用户名不能为空！",Toast.LENGTH_SHORT).show();
                    else
                    Toast.makeText(Login.this,"用户名不存在！",Toast.LENGTH_SHORT).show();
                } else if(!pass.equals(Spass)) {
                    Toast.makeText(Login.this,"密码错误！",Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Login.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
