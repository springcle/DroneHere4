/*
package com.santamaria.dronehere;

import android.os.Binder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

*/
/**
 * Created by gta2v on 2017-01-14.
 *//*


public class LogWrapper {
    private static final String TAG = "LogWrapper";
    private static final int LOG_FILE_SIZE_LIMIT = 512 * 1024;
    private static final int LOG_FILE_MAX_COUNT = 2;
    private static final String LOG_FILE_NAME = "DroneHere_FIleLog_%g.txt"; // %g는 파일 이름 뒤에 붙을 숫자
    private static final SimpleDateFormat formatter =
            new SimpleDateFormat("MM-dd HH:mm:ss.SSS:", Locale.getDefault());
    private static final Date date = new Date();
    private static Logger logger;
    private static FileHandler fileHandler;

    static {


        try {
            fileHandler = new FileHandler(Environment.
                    getExternalStorageDirectory() +
                    File.separator + LOG_FILE_NAME, LOG_FILE_SIZE_LIMIT, LOG_FILE_MAX_COUNT, true);

            logger = Logger.getLogger(LogWrapper.class.getName());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
            Log.d(TAG, "init Success");


            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord logRecord) {
                    date.setTime(System.currentTimeMillis());
                    StringBuilder ret = new StringBuilder();
                    ret.append(formatter.format(date));
                    ret.append(logRecord.getMessage());

                    return ret.toString();
                }
            });

        } catch (IOException e) {
            Log.d(TAG, "init Failure");
            e.printStackTrace();
        }
    }


    public static void v(String tag, String msg) {


        if (logger != null) {
            logger.log(Level.INFO, String.format("V/%s(%d) : %s\n",
                    tag, Binder.getCallingPid(), msg));
        }
        Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {


        if (logger != null) {
            logger.log(Level.INFO, String.format("V/%s(%d) : %s\n",
                    tag, Binder.getCallingPid(), msg));
        }
        Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {

        if (logger != null) {
            logger.log(Level.INFO, String.format("V/%s(%d) : %s\n",
                    tag, Binder.getCallingPid(), msg));
        }
        Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {

        if (logger != null) {
            logger.log(Level.INFO, String.format("V/%s(%d) : %s\n",
                    tag, Binder.getCallingPid(), msg));
        }
        Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {


        if (logger != null) {
            logger.log(Level.INFO, String.format("V/%s(%d) : %s\n",
                    tag, Binder.getCallingPid(), msg));
        }
        Log.e(tag, msg);


    }
}
*/
