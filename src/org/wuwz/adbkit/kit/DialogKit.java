package org.wuwz.adbkit.kit;

import org.wuwz.adbkit.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("InflateParams")
public abstract class DialogKit {
    
    public static Dialog progressDialog(Context ctx, String msg) {
    	return progressDialog(ctx, msg, true);
    }
    
    /**
	 * �Զ���progressDialog
	 * @param ctx
	 * @param msg
	 * @return
	 */
	public static Dialog progressDialog(Context ctx, String msg,boolean cancelable) {

		LayoutInflater inflater = LayoutInflater.from(ctx);
		View v = inflater.inflate(R.layout.dialog_loading, null);// �õ�����view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// ���ز���
		// main.xml�е�ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tv_loading_tip);// ��ʾ����
		// ���ض���
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(ctx, R.anim.anim_dialog_loading);
		// ʹ��ImageView��ʾ����
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// ���ü�����Ϣ

		Dialog dialog = new Dialog(ctx, R.style.dialog_loading);// �����Զ�����ʽdialog

		dialog.setCancelable(cancelable);// �������á����ؼ���ȡ��
		dialog.setContentView(layout, 
			new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT)
			);// ���ò���
		dialog.show();
		return dialog;

	}
}
