package shixiu.mitao.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BitmapToBase64 {
	/**
	 * bitmapתBase64
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = "";
		ByteArrayOutputStream bos = null;
		try {
			if (null != bitmap) {
				bos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//30 是压缩率，表示压缩70%; 如果不压缩是100，表示压缩率为0  
				bos.flush();// ˢ��ͼƬ
				bos.close();
				byte[] bitmapByte = bos.toByteArray();
				result = Base64.encodeToString(bitmapByte, Base64.DEFAULT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
}
