package com.daphnis.zmap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

/**
 * Created by Daphnis on 2016/10/26.
 */
public class Zutils {
    //切换Activity
    public static void showActivity(Context packageContext, Class<?> cla){
        Intent itt=new Intent();
        itt.setClass(packageContext,cla);
        packageContext.startActivity(itt);
    }

    //弹出消息窗口
    public static void showMessage(Context packageContext,String msg){
        new AlertDialog.Builder(packageContext)
                .setTitle("重要消息")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}
