package com.laundrylangpickcargo.wheelview;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<String> vehicleList;
    private ArrayList<String> driverList;
    private View chooseView;
    private WheelView driverView;
    private DriverChooseWheelListener driverChooseWheelListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testWheelView(View v) {
        initDriverChooseView();
        showDialogFromBottom(this, chooseView);
    }

    /**
     * init司机选取布局
     */
    public void initDriverChooseView() {
        chooseView = LayoutInflater.from(this).inflate(R.layout.layout_choose_driver, null);
        WheelView vehicleView = (WheelView) chooseView.findViewById(R.id.wv_chooseDriver_vehicle);
        driverView = (WheelView) chooseView.findViewById(R.id.wv_chooseDriver_driver);
        vehicleList = new ArrayList<>();
        driverList = new ArrayList<>();
        //选取发运衣物的车辆
        for (int i = 0; i < 10; i++) {
            vehicleList.add("车辆" + i);
        }
        vehicleView.setOffset(1);
        vehicleView.setItems(vehicleList);
        driverChooseWheelListener = new DriverChooseWheelListener(1);
        vehicleView.setOnWheelViewListener(driverChooseWheelListener);
        //车辆选中的时候将对应司机的数据加上
        driverChooseWheelListener.onSelected(1, vehicleList.get(0));
        driverView.setOnWheelViewListener(new DriverChooseWheelListener(2));
    }

    class DriverChooseWheelListener extends WheelView.OnWheelViewListener {
        private int wheelTag;//1:车辆 2：司机

        public DriverChooseWheelListener(int wheelTag) {
            this.wheelTag = wheelTag;
        }

        @Override
        public void onSelected(int selectedIndex, String item) {
            super.onSelected(selectedIndex, item);
            if (wheelTag == 1) {
                //选取发运衣物的司机
                driverList.clear();
                for (int i = 0; i < 20; i++) {
                    driverList.add(item + "-司机" + i);
                }
                driverView.setOffset(1);//设置偏移量
                driverView.setSeletion(0);//选中第一个空标签
                driverView.setItems(driverList);
            } else if (wheelTag == 2) {
            }
        }
    }

    /**
     * 从底部弹出一个dialog
     *
     * @param context 当前Activity对象
     * @param view    需要弹起的视图
     * @return AlertDialog
     */
    public static AlertDialog showDialogFromBottom(Activity context, View view) {
        AlertDialog popupLog = new AlertDialog.Builder(context).create();
        Window win = popupLog.getWindow();
        win.setWindowAnimations(R.style.dialog_style);
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.x = 0;
        wl.y = context.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        popupLog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        popupLog.setCanceledOnTouchOutside(true);
        popupLog.show();
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = popupLog.getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置宽度
        popupLog.getWindow().setAttributes(lp);
        if (view != null) {
            win.setContentView(view);
        }
        return popupLog;
    }
}
