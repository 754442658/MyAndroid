

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneTools {

	/**
	 * 匹配是不是手机号
	 * 
	 * @param mobiles
	 *            手机号
	 * @return
	 */
	public static boolean isMobileNum(String mobiles) {
		Pattern p = Pattern
				.compile("^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 判断传过来的用户名是不是手机号，是手机号的话就加密，否则直接返回
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
