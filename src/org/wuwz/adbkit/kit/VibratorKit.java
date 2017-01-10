package org.wuwz.adbkit.kit;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

/**
 * �𶯹���
 * @Author wuwz
 * @TypeName VibratorUtil
 */
public abstract class VibratorKit {

	/**
	 * ��ʼ��
	 * @param activity
	 * @param milliseconds ��ʱ�䣨���룩
	 */
	public static void start(final Activity activity, long milliseconds) {
		Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

	/**
	 * ��ʼ��
	 * @param activity
	 * @param pattern �Զ�����ģʽ �����������ֵĺ���������[��ֹʱ������ʱ������ֹʱ������ʱ��������]ʱ���ĵ�λ�Ǻ���
	 * @param isRepeat �Ƿ񷴸��𶯣������true�������𶯣������false��ֻ��һ��
	 */
	public static void start(final Activity activity, long[] pattern,boolean isRepeat) {
		Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(pattern, isRepeat ? 1 : -1);
	}
}
