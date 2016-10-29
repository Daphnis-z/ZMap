package com.daphnis.zmap;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class RegisterActivity extends AppCompatActivity {
    private View.OnClickListener btnSubmitClick =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String uname=((EditText)findViewById(R.id.editTextName)).getText().toString();
            String pw=((EditText)findViewById(R.id.editTextPw)).getText().toString();
            String mail=((EditText)findViewById(R.id.editTextMail)).getText().toString();
            if(uname.equals("")||pw.equals("")||mail.equals("")){
                Zutils.showMessage(RegisterActivity.this,"注册信息不完整！！");
                return;
            }

            saveAccount(uname,pw,mail);//保存账户信息
            new AlertDialog.Builder(RegisterActivity.this)
                    .setMessage("注册成功！！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            Zutils.showActivity(RegisterActivity.this,MainActivity.class);
                        }
                    })
                    .show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //为控件注册事件
        findViewById(R.id.btnSubmit).setOnClickListener(btnSubmitClick);
    }

    //保存账户信息
    private void saveAccount(String name,String pw,String mail){
        String accounts=getApplicationContext().getFilesDir().getAbsolutePath()+
                "/"+this.getString(R.string.accounts);
        File f=new File(accounts);
        try{
            if(f.exists()){
                FileWriter write=new FileWriter(f,true);
                write.write(String.format("%s,%s,%s\n",name,pw,mail));
                write.close();
            }else{
                FileOutputStream out=this.openFileOutput(this.getString(R.string.accounts),this.MODE_WORLD_READABLE);
                out.write(String.format("%s,%s,%s\n",name,pw,mail).getBytes());
                out.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
