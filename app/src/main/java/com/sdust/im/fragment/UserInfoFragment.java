package com.sdust.im.fragment;

import com.sdust.im.R;
import com.sdust.im.bean.ApplicationData;
import com.sdust.im.view.CircleImageView;
import com.sdust.im.view.TitleBarView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.BitmapCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfoFragment extends Fragment{
	private Context mContext;
	private View mBaseView;
	private CircleImageView userBitMap;
	private TextView userName;
	private Bitmap bitmap;
	private String user_name;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_userinfo, null);
		initView();
		return mBaseView;
	}
	private void initView(){
	    bitmap = ApplicationData.getInstance().getUserPhoto();
		user_name = ApplicationData.getInstance().getUserName();
		userBitMap = (CircleImageView)mBaseView.findViewById(R.id.picture);
		userBitMap.setImageBitmap(bitmap);
		userName = (TextView)mBaseView.findViewById(R.id.userName);
		userName.setText(user_name);
	}

}

