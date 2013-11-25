package com.view.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 发送邮件测试
 *
 */
public class SendEmailActivity extends Activity implements View.OnClickListener{
	private Button mSendBtn;
	private Button mSendToBtn;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_mail);
        mSendBtn=(Button)findViewById(R.id.send_btn);
        mSendToBtn=(Button)findViewById(R.id.send_to_btn);
        mSendBtn.setOnClickListener(this);
        mSendToBtn.setOnClickListener(this);
    }
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.send_btn:
			startSendEmailIntent();
			break;
		case R.id.send_to_btn:
			startSendToEmailIntent();
			break;
		}
	}
	private void startSendEmailIntent(){
		Intent data=new Intent(Intent.ACTION_SENDTO);
		data.setData(Uri.parse("mailto:10000@qq.com"));
		data.putExtra(Intent.EXTRA_EMAIL, new String[]{"10000@qq.com"});
//		data.putExtra(Intent.EXTRA_CC, new String[]{"ls8709@163.com"});
//		data.putExtra(Intent.EXTRA_BCC, new String[]{"ls810@163.com"});
		data.putExtra(Intent.EXTRA_SUBJECT, "这是标题Android send Email");
		data.putExtra(Intent.EXTRA_TEXT, "这是内容Android send Email");
		startActivity(data);
	}
	private void startSendToEmailIntent(){
		//Intent data=new Intent(Intent.ACTION_SEND);
		Intent data=new Intent(Intent.ACTION_SEND_MULTIPLE);
		data.putExtra(Intent.EXTRA_EMAIL, new String[]{"10000@qq.com"});
		data.putExtra(Intent.EXTRA_SUBJECT, "这是标题Android send Email");
		data.putExtra(Intent.EXTRA_TEXT, "这是内容Android send Email");
		ArrayList<Uri> fileUris = new ArrayList<Uri>();  
		fileUris.add(Uri.parse("file://"+ android.os.Environment.getExternalStorageDirectory() + "/mapdata/map/image1.jpg"));  
		fileUris.add(Uri.parse("file://"+ android.os.Environment.getExternalStorageDirectory() + "/mapdata/map/image2.jpg")); 
		fileUris.add(Uri.parse("file://"+ android.os.Environment.getExternalStorageDirectory() + "/mapdata/map/image1point.xml")); 
		fileUris.add(Uri.parse("file://"+ android.os.Environment.getExternalStorageDirectory() + "/mapdata/map/image2point.xml")); 
		data.putParcelableArrayListExtra(Intent.EXTRA_STREAM, fileUris); 
		data.setType("image/*");  
		data.setType("message/rfc882"); 
		//data.setType("text/plain");
		startActivity(data);
	}
}
