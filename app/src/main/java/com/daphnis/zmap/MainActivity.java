package com.daphnis.zmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //界面加载完成
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        if(hasFocus){
            useConfig();
        }
    }

    //登录按钮
    protected void btnLoginClick(View v){
        String uname=((EditText)findViewById(R.id.editTextName)).getText().toString();
        String pw=((EditText)findViewById(R.id.editTextPw)).getText().toString();
        String accounts=getApplicationContext().getFilesDir().getAbsolutePath()+
                "/"+this.getString(R.string.accounts);
        try{
            BufferedReader read=new BufferedReader(new FileReader(accounts));
            String acc;
            while((acc=read.readLine())!=null){
                String[] strs=acc.split(",");
                if(strs[0].equals(uname)&&strs[1].equals(pw)){
                    saveConfig(uname,pw);
                    finish();
                    Zutils.showActivity(MainActivity.this,MapActivity.class);
                    return;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Zutils.showMessage(MainActivity.this,"登录失败！！");
    }
    //注册按钮
    protected void btnRegisterClick(View v){
        Zutils.showActivity(MainActivity.this,RegisterActivity.class);
    }
    //保存配置信息
    private void saveConfig(String uname,String pw){
        CheckBox crp=(CheckBox)findViewById(R.id.checkBoxRp),
                cal=(CheckBox)findViewById(R.id.checkBoxAl);
        char rp=crp.isChecked()? '1':'0',
                al=cal.isChecked()? '1':'0';
        try{
            FileOutputStream out=this.openFileOutput(this.getString(R.string.config),this.MODE_WORLD_READABLE);
            out.write(String.format("%c,%c,%s,%s",rp,al,uname,pw).getBytes());
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //使用配置信息
    private void useConfig(){
        String cfg=getApplicationContext().getFilesDir().getAbsolutePath()+
                "/"+this.getString(R.string.config);
        File f=new File(cfg);
        try{
            if(f.exists()){
                BufferedReader read=new BufferedReader(new FileReader(f));
                String[] cfgs=read.readLine().split(",");
                if(cfgs[0].equals("1")){
                    ((CheckBox)findViewById(R.id.checkBoxRp)).setChecked(true);
                    ((EditText)findViewById(R.id.editTextName)).setText(cfgs[2]);
                    ((EditText)findViewById(R.id.editTextPw)).setText(cfgs[3]);
                }
                if(cfgs[1].equals("1")){
                    ((CheckBox)findViewById(R.id.checkBoxAl)).setChecked(true);
                    Toast.makeText(this,"正在登陆..",Toast.LENGTH_LONG).show();
                    TimerTask tt=new TimerTask() {
                        @Override
                        public void run() {
                            btnLoginClick(null);
                        }
                    };
                    new Timer().schedule(tt,2000);
                }
                read.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}