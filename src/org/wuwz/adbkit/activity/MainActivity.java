package org.wuwz.adbkit.activity;


import java.text.MessageFormat;

import org.wuwz.adbkit.R;
import org.wuwz.adbkit.kit.DialogKit;
import org.wuwz.adbkit.kit.ShellKit;
import org.wuwz.adbkit.kit.ShellKit.CommandResult;
import org.wuwz.adbkit.kit.VibratorKit;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private Dialog _progressDialog;
	private ImageView _imageViewTipImg;
	private TextView _textViewTipMsg,_textViewCommandTip; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		startRemoteDebugger();
	}

	private void initViews() {
		_textViewTipMsg = (TextView) findViewById(R.id.tv_tip_msg);
		_textViewCommandTip = (TextView) findViewById(R.id.tv_command_tip);
		_imageViewTipImg = (ImageView) findViewById(R.id.iv_tip_img);
		
		((TextView) findViewById(R.id.tv_about)).setOnClickListener(this);
		((TextView) findViewById(R.id.tv_title)).setOnClickListener(this);
		
		_textViewTipMsg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String text = String.valueOf(_textViewTipMsg.getText());
				if(text.endsWith("重试?")) {
					startRemoteDebugger();
				}
				
			}
		});
	}

	private void startRemoteDebugger() {
		_progressDialog = DialogKit.progressDialog(MainActivity.this, "正在开启ADB远程调试..",false);
		String wifiHostIp = getWifiHostIp();
		
		if(!TextUtils.isEmpty(wifiHostIp)) {
			Toast.makeText(MainActivity.this, "正在执行开启命令.", Toast.LENGTH_SHORT).show();
			String[] commands = {"setprop service.adb.tcp.port 5555", "stop adbd", "start adbd"};
			CommandResult r = ShellKit.execCommand(commands, true, true);
			setStatusView(r.result == 0,wifiHostIp);
		} else {
			setStatusView(false,null);
		}
		
		closeProgressDialog();
	}

	private void setStatusView(boolean isSuccess,String wifiHostIp) {
		if(TextUtils.isEmpty(wifiHostIp)) {
			wifiHostIp = "xxx.xxx.xx(需先开启WIFI)";
		}
		_textViewTipMsg.setText(this.getString(isSuccess ? R.string.tip_msg_ok : R.string.tip_msg_error));
		_textViewTipMsg.setTextColor(isSuccess ? Color.parseColor("#2aa515") : android.graphics.Color.RED);
		_imageViewTipImg.setImageResource(isSuccess ? R.drawable.ic_ok : R.drawable.ic_error);
		_textViewCommandTip.setText(MessageFormat.format(this.getString(R.string.command_tip), wifiHostIp));
		
		if(isSuccess) {
			VibratorKit.start(MainActivity.this, 1500);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopRemoteDebugger();
	}
	
	private void stopRemoteDebugger() {
		String[] commands = {"setprop service.adb.tcp.port -1", "stop adbd", "start adbd"};
		ShellKit.execCommand(commands, true, true);
		
		setStatusView(false,null);
	}
	
	private String getWifiHostIp() {
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		if(ipAddress == 0) {
			new AlertDialog.Builder(MainActivity.this)
				.setTitle("提示")
				.setMessage("无法获取到IP地址\n是否没有连接WIFI?")
				.setCancelable(false)
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {
						
					@Override
					public void onClick(DialogInterface dialog, int whic) {
						Toast.makeText(MainActivity.this, "请先连接好WIFI后再次尝试打开APP\n注意：一定要确保与电脑处于同一个网络环境下!", Toast.LENGTH_LONG).show();;
						startActivity(new Intent("android.settings.WIFI_SETTINGS"));
					}
				}).show();
			return "";
		}
		return ((ipAddress & 0xff)+"."+(ipAddress>>8 & 0xff)+"." +(ipAddress>>16 & 0xff)+"."+(ipAddress>>24 & 0xff));
	}
	
	private void closeProgressDialog() {
		
		// 1.5秒后再关闭
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(_progressDialog != null) {
					
					_progressDialog.dismiss();
					_progressDialog = null;
				}
			}
		}, 1500);
	}

	@Override
	public void onClick(View v) {
		new AlertDialog.Builder(MainActivity.this)
		.setTitle("关于")
		.setMessage("利用Shell实现的ADB远程连接工具,借助此工具可实现免数据线调试APP或者其他操作,需要ROOT权限。\nBy @wuwz.")
		.setPositiveButton("确定", null)
		.setNegativeButton("下载源码", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				Intent intent = new Intent();        
		        intent.setAction("android.intent.action.VIEW");    
		        Uri content_url = Uri.parse("https://github.com/wuwz/RemoteDebug");   
		        intent.setData(content_url);  
		        startActivity(intent);
			}
		})
		.show();
	}
	
	private long mExitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "退出程序后将断开连接！\n再按一次返回键即可退出程序。", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				stopRemoteDebugger();
				System.exit(1);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
