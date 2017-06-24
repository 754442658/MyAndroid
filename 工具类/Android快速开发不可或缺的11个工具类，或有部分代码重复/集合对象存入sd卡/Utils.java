import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import shixiutv.mitao.state.AppStore;
import shixiutv.mitao.state.Constant;
import shixiutv.mitao.websocket.ChatMsg;


public class Utils {

    // Ӧ�û����ļ�
    private static final File CACHE_FILE = new File(Constant.CACHE_FILE.getAbsolutePath() + "/�����¼/" + AppStore.user.getUID());

    /**
     * �������
     *
     * @param ser
     * @param fileName
     * @throws IOException
     */
    public static boolean saveObjectList(ArrayList<ChatMsg> ser, String fileName) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            if (!Constant.CACHE_FILE.exists()) {
                CACHE_FILE.mkdirs();
            }
            File file = new File(CACHE_FILE + "/" + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            oos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * ��ȡ����
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static ArrayList<ChatMsg> readObjectList(String fileName) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        if (!CACHE_FILE.exists()) {
            CACHE_FILE.mkdirs();
        }
        File file = new File(CACHE_FILE + "/" + fileName);
        L.e(file.getAbsolutePath());
        try {
            if (!file.exists()) {
                // û�������¼���ؿռ���
                return new ArrayList<>();
            }
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            return (ArrayList<ChatMsg>) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            //�����л�ʧ�� - ɾ�������ļ�
            if (e instanceof InvalidClassException) {
                file.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

}
