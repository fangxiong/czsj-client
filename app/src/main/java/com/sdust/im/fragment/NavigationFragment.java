package com.sdust.im.fragment;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.sdust.im.R;
import com.sdust.im.util.MyOrientationListener;
import com.sdust.im.view.TitleBarView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaControllerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class NavigationFragment extends Fragment{
	private Context mContext;
	private MapView mMapView;
	private RelativeLayout mMakerLy;
	private View mBaseView;
	private ImageView btMapLocal;
	private BaiduMap mBaiduMap;
	// 自定义定位图标
	private BitmapDescriptor mIconLocation;
	private MyOrientationListener myOrientationListener;
	private float mCurrentX;
	private MyLocationConfiguration.LocationMode mLocationMode;

	//定位相关
	private boolean isFirstIn = true;
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private double mLatitude;
	private double mLongitude;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_nearby, null);
		initView();
		initLocation();
		initListener();
		return mBaseView;
	}
	/**
	 * 初始化组件
	 */
private void initView(){
	mMapView =(MapView)mBaseView.findViewById(R.id.id_bmapView);
	mBaiduMap = mMapView.getMap();
	btMapLocal = (ImageView)mBaseView.findViewById(R.id.iv_map_local);
}

	/**
	 * 初始化事件
	 */
private void initListener(){
	btMapLocal.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			centerToMyLocation();
		}
	});
}
	/**
	 * 定位到我的位置
	 */
	private void centerToMyLocation()
	{
		LatLng latLng = new LatLng(mLatitude, mLongitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}
	/**
	 * 初始化位置
	 */
private void initLocation(){
	mBaiduMap.setMyLocationEnabled(true);//开启定位图层
	mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
	mLocationClient = new LocationClient(getActivity());
	mLocationListener = new MyLocationListener();
	mLocationClient.registerLocationListener(mLocationListener);

	LocationClientOption option = new LocationClientOption();
	option.setCoorType("bd09ll");
	option.setIsNeedAddress(true);
	option.setOpenGps(true);
	option.setScanSpan(1000);
	mLocationClient.setLocOption(option);
	// 初始化图标
	mIconLocation = BitmapDescriptorFactory
			.fromResource(R.drawable.navi_map_gps_locked);
	myOrientationListener = new MyOrientationListener(mContext);

	myOrientationListener
			.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
				@Override
				public void onOrientationChanged(float x) {
					mCurrentX = x;
				}
			});
	mLocationClient.start();
}
	private class MyLocationListener implements BDLocationListener
	{
		@Override
		public void onReceiveLocation(BDLocation location)
		{

			MyLocationData data = new MyLocationData.Builder()//
					.direction(mCurrentX)//
					.accuracy(location.getRadius())//
					.latitude(location.getLatitude())//
					.longitude(location.getLongitude())//
					.build();
			mBaiduMap.setMyLocationData(data);
			// 设置自定义图标
			MyLocationConfiguration config = new MyLocationConfiguration(
					mLocationMode, true, mIconLocation);
			mBaiduMap.setMyLocationConfigeration(config);

			// 更新经纬度
			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
			System.out.println("经度:"+mLatitude+"纬度:"+mLongitude);

			if (isFirstIn)
			{
				LatLng latLng = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				isFirstIn = false;

				Toast.makeText(mContext, location.getAddrStr(),
						Toast.LENGTH_SHORT).show();
			}

		}
	}
	@Override
	public void onStop(){
		super.onStop();
		//停止定位
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
	}
	@Override
	public void onResume(){
		super.onResume();
		mMapView.onResume();
	}
	@Override
	public void onPause(){
		super.onPause();
		mMapView.onPause();
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}

