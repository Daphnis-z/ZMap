package com.daphnis.zmap;

import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import java.util.*;

public class MapActivity extends AppCompatActivity {
    MapView mMapView = null;
    BaiduMap bmap;

    private LocationManager locationManager;
    private  String provider=null;

    //地图类型变换
    private Spinner.OnItemSelectedListener spinnerSelectChange =new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position==0){
                bmap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            }else if(position==1){
                bmap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    //点击定位按钮
    private View.OnClickListener btnNowLocClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setLocationManager();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        bmap=mMapView.getMap();

        ((Spinner)findViewById(R.id.spinner)).setOnItemSelectedListener(spinnerSelectChange);
        ((Button)findViewById(R.id.btnNowLoc)).setOnClickListener(btnNowLocClick);

        setLocationManager();
        moveTo(32.0647350000,118.8028910000);//移动到南京
    }

    //移动地图
    private void moveTo(double lat,double lon){
        LatLng ll=new LatLng(lat,lon);
        MapStatusUpdate update= MapStatusUpdateFactory.newLatLng(ll);
        bmap.animateMapStatus(update);
    }

    private void setLocationManager() {
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providerList=locationManager.getProviders(true);
        if(providerList.contains(LocationManager.GPS_PROVIDER)){
            provider=LocationManager.GPS_PROVIDER;
        }else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
            provider=LocationManager.NETWORK_PROVIDER;
        }else {
            //当前没有可用的位置提供器时，弹出Toast提示
            Toast.makeText(this,"请打开GPS再重试",Toast.LENGTH_SHORT).show();
            return;
        }
        Location location=locationManager.getLastKnownLocation(provider);
        if(location==null){
            location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if(location!=null){
            moveTo(location.getLatitude(),location.getLongitude());
            //定义Maker坐标点
            LatLng point = new LatLng(location.getLatitude(),location.getLongitude());
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.flat);
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            bmap.addOverlay(option);
            bmap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

}
