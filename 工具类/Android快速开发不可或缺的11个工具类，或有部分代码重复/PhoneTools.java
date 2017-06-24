

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneTools {

	/**
	 * ƥ���ǲ����ֻ���
	 * 
	 * @param mobiles
	 *            �ֻ���
	 * @return
	 */
	public static boolean isMobileNum(String mobiles) {
		Pattern p = Pattern
				.compile("^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * �жϴ��������û����ǲ����ֻ��ţ����ֻ��ŵĻ��ͼ��ܣ�����ֱ�ӷ���
	 * 
	 * @param username
	 * @return
	 */
	public static String disPhoneUserName(String username) {
		if (isMobileNum(username)) {
			return username.substring(0, 3) + "****"
					+ username.substring(7, 11);
		} else {
			return username;
		}
	}
}
