package com.sdust.im.activity;

import com.baidu.mapapi.SDKInitializer;
import com.sdust.im.R;
import com.sdust.im.action.UserAction;
import com.sdust.im.bean.ApplicationData;
import com.sdust.im.fragment.FriendListFragment;
import com.sdust.im.fragment.MessageFragment;



import com.sdust.im.fragment.NavigationFragment;
import com.sdust.im.fragment.UserInfoFragment;

import com.sdust.im.network.NetService;
import com.slidingmenu.lib.SlidingMenu;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends FragmentActivity {

	protected static final String TAG = "MainActivity";
	private long exitTime = 0;
	private Context mContext;
	private ImageButton mNews,mConstact,mDeynaimic,mSetting;
	private View mPopView;
	private View currentButton;
	
	private TextView app_cancle;
	private TextView app_exit;
	private TextView app_change;
	
	private PopupWindow mPopupWindow;
	private LinearLayout buttomBarGroup;

	private SlidingMenu menu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());//在使用SDK各组件之前初始化context信息，context为application
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mContext=this;
		System.out.println("初始化Main");
		findView();
		init();//初始化
		System.out.println("user的头像："+ ApplicationData.getInstance().getUserPhoto());
		initSlidingMenu();
	}


	private void findView(){
		mPopView=LayoutInflater.from(mContext).inflate(R.layout.app_exit, null);
		buttomBarGroup=(LinearLayout) findViewById(R.id.buttom_bar_group);
		mNews=(ImageButton) findViewById(R.id.buttom_news);
		mConstact=(ImageButton) findViewById(R.id.buttom_constact);
		mDeynaimic=(ImageButton) findViewById(R.id.buttom_deynaimic);
		mSetting=(ImageButton) findViewById(R.id.buttom_setting);
		app_cancle=(TextView) mPopView.findViewById(R.id.app_cancle);
		app_change=(TextView) mPopView.findViewById(R.id.app_change_user);
		app_exit=(TextView) mPopView.findViewById(R.id.app_exit);
	}
	
	private void init(){
		mNews.setOnClickListener(newsOnClickListener);
		mConstact.setOnClickListener(constactOnClickListener);
		mDeynaimic.setOnClickListener(deynaimicOnClickListener);
		mSetting.setOnClickListener(settingOnClickListener);
		
		mConstact.performClick();
		
		mPopupWindow=new PopupWindow(mPopView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		
		app_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
		
		app_change.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext, LoginActivity.class);
				startActivity(intent);
				((Activity)mContext).overridePendingTransition(R.anim.activity_up, R.anim.fade_out);
				NetService.getInstance().closeConnection();
				finish();
			}
		});
		
		app_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NetService.getInstance().closeConnection();
				finish();
			}
		});
	}
	private void initSlidingMenu(){
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		View view=LayoutInflater.from(this).inflate(R.layout.menu_item, null);
		menu.setMenu(view);
	}
	private OnClickListener newsOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentManager fm=getSupportFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			MessageFragment messageFragment=new MessageFragment();
			ft.replace(R.id.fl_content, messageFragment,MainActivity.TAG);
			ft.commit();
			setButton(v);
		}
	};
	
	private OnClickListener constactOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentManager fm=getSupportFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			FriendListFragment constactFatherFragment=new FriendListFragment();
			ft.replace(R.id.fl_content, constactFatherFragment,MainActivity.TAG);
			ft.commit();
			setButton(v);
			
		}
	};
	
	private OnClickListener deynaimicOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentManager fm=getSupportFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			NavigationFragment dynamicFragment=new NavigationFragment();
			ft.replace(R.id.fl_content, dynamicFragment,MainActivity.TAG);
			ft.commit();
			setButton(v);
			
		}
	};
	
	private OnClickListener settingOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentManager fm=getSupportFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			UserInfoFragment settingFragment=new UserInfoFragment();
			ft.replace(R.id.fl_content, settingFragment,MainActivity.TAG);
			ft.commit();
			setButton(v);
			
		}
	};
	
	private void setButton(View v){
		if(currentButton!=null&&currentButton.getId()!=v.getId()){
			currentButton.setEnabled(true);
		}
		v.setEnabled(false);
		currentButton=v;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			if((System.currentTimeMillis()-exitTime) > 2000){
				Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			}
			else{
				try {
					UserAction.logout();//下线通知服务器端
				}catch(IOException e){
					e.printStackTrace();
				}
				NetService.getInstance().closeConnection();//关闭客户端与服务器的连接
				finish();
				System.exit(0);

			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
		
	}

}
