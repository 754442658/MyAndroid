

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;


/**
 * δ������쳣����
 *
 * @author w
 *
 */
public class MyUncaughtException implements UncaughtExceptionHandler {

    private Context context;
    //�����洢�豸��Ϣ���쳣��Ϣ  
    private Map<String, String> infos = new HashMap<String, String>();
    //���ڸ�ʽ������,��Ϊ��־�ļ�����һ����  
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    public MyUncaughtException(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    /**
     * ����δ������쳣 ����δ������쳣��������Ӧ�ó���
     * ���豸��Ϣ�ʹ�����Ϣ�洢��sd����־�ļ�
     * @author w
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex, Activity activity) {
        try {
            // ���������־��Ϣ������sd��
            Log.e("Tag","����δ�����쳣��" + ex);
            //�ռ��豸������Ϣ   
            collectDeviceInfo(context);
            //������־�ļ�   
            String logPath = saveCrashInfo2File(ex);
            Log.e("Tag",logPath);
            Intent intent = new Intent(context, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            Log.e("Tag","test");
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }


    /**
     * �ռ��豸������Ϣ
     * @param context2
     */
    private void collectDeviceInfo(Context context2) {
        // TODO Auto-generated method stub
        try {
            PackageManager pm = context2.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context2.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e("Tag", "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d("Tag", field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e("Tag", "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * ���������Ϣ���ļ��� 
     *
     * @param ex
     * @return  �����ļ�����,���ڽ��ļ����͵�������
     */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //���򻺴��ļ��У����Զ���
                File dir = Constant.CACHE_FILE;
                String path = dir.getAbsolutePath();
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path +"/"+ fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e("Tag", "an error occured while writing file...", e);
        }
        return null;
    }

}
