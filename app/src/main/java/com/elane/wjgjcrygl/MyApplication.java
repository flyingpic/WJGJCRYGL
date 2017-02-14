package com.elane.wjgjcrygl;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.elane.wjgjcrygl.util.RestartDevice;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Created by zgbf on 2016/10/12.
 */
public class MyApplication extends Application {

    private Context mContext;

    public static String oracle_ip = "192.168.4.235";//oracle数据库ip  192.168.2.229
    public static String oracle_port = "1521";//oracle数据库port  1521
    public static String oracle_sid = "fxgl";//oracle数据库sid  ecmis
    public static String oracle_username = "ECMIS_IIT";//oracle数据库用户名  ECMIS_IIT
    public static String oracle_password = "ecmis_iit";//oracle数据库密码

    public static String VERIFY_PASSWORD_BASE_URL = "http://192.168.4.45:8888/web.iit/servlet/";
    public static String FINGER_COMPARE_BASE_URL = "http://192.168.4.236:8732/XMLFingerprintMatch/FingerprintMatch/";

    public static Logger gLogger;//日志文件

    public static LogConfigurator logConfigurator;

    public void configLog(){
        logConfigurator = new LogConfigurator();

        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator +"ABDOOR_App_Log"+File.separator+"RunTimeLog"+File.separator+ getApplicationContext().getPackageName()+ File.separator + getNowDate() +".txt");
        // Set the root log level
        logConfigurator.setRootLevel(Level.DEBUG);
        // Set log level of a specific logger
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.configure();

        //gLogger = Logger.getLogger(this.getClass());
        gLogger = Logger.getLogger("CrifanLiLog4jTest");
    }
    public String getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd_HH-mm-ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        configLog();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e("test", "程序处于低内存状态，需要关闭重启");
        System.gc();
        new RestartDevice();
//        System.exit(0);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
