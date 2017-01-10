package org.wuwz.adbkit.kit;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

/**
 * 震动工具
 * @Author wuwz
 * @TypeName VibratorUtil
 */
public abstract class VibratorKit {

	/**
	 * 开始震动
	 * @param activity
	 * @param milliseconds 震动时间（毫秒）
	 */
	public static void start(final Activity activity, long milliseconds) {
		Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

	/**
	 * 开始震动
	 * @param activity
	 * @param pattern 自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
	 * @param isRepeat 是否反复震动，如果是true，反复震动，如果是false，只震动一次
	 */
	public static void start(final Activity activity, long[] pattern,boolean isRepeat) {
		Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(pattern, isRepeat ? 1 : -1);
	}
}
