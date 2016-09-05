package com.example.think.waterworksapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.think.waterworksapp.adapter.DividerItemDecoration;
import com.example.think.waterworksapp.adapter.EquipmentSubmitAdapter;
import com.example.think.waterworksapp.adapter.ImgUpLoadAdapter;
import com.example.think.waterworksapp.base_intface.EquipmentMsgSubmitView;
import com.example.think.waterworksapp.base_intface.GetInspectionItemView;
import com.example.think.waterworksapp.base_intface.GetMonitoringPointDataView;
import com.example.think.waterworksapp.bean.InspectionItemBean;
import com.example.think.waterworksapp.bean.MonitoringPointDataBean;
import com.example.think.waterworksapp.custom_view.UpperActivity;
import com.example.think.waterworksapp.dialog.DialogManager;
import com.example.think.waterworksapp.dialog.SimperDialog;
import com.example.think.waterworksapp.model.EquipmentMsgSubmitModel;
import com.example.think.waterworksapp.model.GetInspectionItemModel;
import com.example.think.waterworksapp.model.GetMonitoringPointDataModel;
import com.example.think.waterworksapp.popup_window.AddImagePopupWindow;
import com.example.think.waterworksapp.utils.NfcUtils;
import com.example.think.waterworksapp.utils.SetListViewAndGridViewHeightUtils;
import com.example.think.waterworksapp.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class EquipmentInformationActivity extends UpperActivity
        implements ImgUpLoadAdapter.AddImageViewBtnClickListener,AdapterView.OnItemClickListener
                    ,AddImagePopupWindow.AddImageItemClickListener ,GetInspectionItemView,GetMonitoringPointDataView
                        ,View.OnClickListener,EquipmentMsgSubmitView,NfcUtils.GetDeviceIdListener,DialogInterface.OnDismissListener{

    public static final String CURRENT_IMAGE_LIST_LENGTH = "current image list size";

    //进入相机请求码
    private final int CAPTURECODE = 111;
    //进入相册请求码
    private final int ALBUMCODE = 222;
    //进入查看大图的请求码
    private final int IMGPRE = 333;

    private RecyclerView equipment_run_state_submit_recycler;
    private TextView equipmentRunStateTxt;
    private LineChartView line_chart_view;
    private EditText errorMsgSubmitEdit;
    private GridView errorImgSubmitGrid;
    private Button submitBtn;
    private View popupWindowBottom;
//    private DialogManager dialogManager;
    private SimperDialog dialog;

    private EquipmentSubmitAdapter equipmentSubmitAdapter;
    private ArrayAdapter<String> imgUpLoadAdapter;
    private AddImagePopupWindow addImagePopupWindow;
    private ArrayList<String> imgUrls = new ArrayList<>();
    private ArrayList<String> submitTitle;
    private HashMap<String,Object> bodyMap;
    private NfcUtils nfcUtils;


    private final boolean hasLabelsOnlyForSelected = true;
    private final boolean HasLabels = false;
    private boolean ifFinsh = false;
    private boolean ifGetDevice = false;
    private boolean ifReadDeviceIdSuccess = false;

    String[] date = {"1", "2", "3", "4", "5", "6", "7"};//X轴的标注
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<PointValue> mPointValues2 = new ArrayList<PointValue>();
    private List<PointValue> mPointValues3 = new ArrayList<PointValue>();
    private List<PointValue> mPointValues4 = new ArrayList<PointValue>();
    private List<PointValue> mPointValues5 = new ArrayList<PointValue>();
    private List<PointValue> mPointValues6 = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcUtils = new NfcUtils(this,this);
        if (dialog == null)
            dialog = new SimperDialog(this);
        dialog.setTitle("正在获取设备ID").showDialog();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            nfcUtils.processNfcConnectedIntent(intent);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        ifFinsh = true;
        if (dialog!=null)
            dialog.hideDialog();
        super.onDestroy();
    }

    private void setValue(){
        submitTitle = new ArrayList<>();
        submitTitle.add("设备运行无异常、异味。");
        submitTitle.add("闸刀、开关在正确位置。");
        submitTitle.add("仪表读数在正常范围内，无大波动");
        bodyMap = new HashMap<>();
        Intent intent = getIntent();
        bodyMap.put(GetMonitoringPointDataModel.NODE_IDS_KEY,intent.getStringArrayExtra(GetMonitoringPointDataModel.NODE_IDS_KEY));
        bodyMap.put(GetMonitoringPointDataModel.KPI_CODES_key,intent.getStringArrayExtra(GetMonitoringPointDataModel.KPI_CODES_key));
        bodyMap.put(GetMonitoringPointDataModel.IS_REALTIMEDATA_key,intent.getStringArrayExtra(GetMonitoringPointDataModel.IS_REALTIMEDATA_key));
        bodyMap.put(GetMonitoringPointDataModel.TIME_PERIOD_key,intent.getStringArrayExtra(GetMonitoringPointDataModel.TIME_PERIOD_key));
        getData();
    }

    private void getData(){
        GetInspectionItemModel getInspectionItemModel = new GetInspectionItemModel(this,this);
//        getInspectionItemModel.getInspectionItem();

        GetMonitoringPointDataModel getMonitoringPointDataModel = new GetMonitoringPointDataModel(this,this);
//        getMonitoringPointDataModel.getMonitoringPointData(bodyMap);
    }



    @Override
    protected void initView() {
//        initRecycler();
//        initChart();
        initImgUpLoad();
        initPopupWindow();
        errorMsgSubmitEdit = findView(R.id.equipment_error_msg_submit_edit);
        equipmentRunStateTxt = findView(R.id.equipment_run_state_value);
        submitBtn = findView(R.id.equipment_information_submit_btn);
        popupWindowBottom = findView(R.id.equipment_information_popupWindow_bottom);
    }

    private void initPopupWindow() {
        addImagePopupWindow = new AddImagePopupWindow(this,this);
        addImagePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //当popupwindow消失的时候，设置屏幕蒙版消失
                lightOn();
            }
        });
    }


    //下面的菜单去掉的时候，界面变亮
    private void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    //下面的菜单弹出来的时候，界面变暗
    private void lightOff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    private void showPopupWindow(){
        addImagePopupWindow.setAnimationStyle(R.style.dir_popupwindow_anim);
        addImagePopupWindow.showAsDropDown(popupWindowBottom,0,0);
        lightOff();
    }

    /**
     * 初始化图片加载
     */
    private void initImgUpLoad() {
        errorImgSubmitGrid = findView(R.id.equipment_error_img_submit_grid);
        imgUpLoadAdapter = new ImgUpLoadAdapter(this,imgUrls,this);
        errorImgSubmitGrid.setAdapter(imgUpLoadAdapter);
        SetListViewAndGridViewHeightUtils.setGridViewHeight(errorImgSubmitGrid);
    }

    /**
     * 初始化图表
     */
    private void initChart() {
        line_chart_view = findView(R.id.line_chart_view);
        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化
    }

    private void initRecycler(){
        setValue();
        equipment_run_state_submit_recycler = findView(R.id.equipment_run_state_submit);
        equipment_run_state_submit_recycler.setLayoutManager(new LinearLayoutManager(this));
        equipment_run_state_submit_recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        equipmentSubmitAdapter = new EquipmentSubmitAdapter(this,submitTitle);
        equipment_run_state_submit_recycler.setAdapter(equipmentSubmitAdapter);
    }



    @Override
    protected void initEvent() {
        errorImgSubmitGrid.setOnItemClickListener(this);
        submitBtn.setOnClickListener(this);
    }


    private void initLineChart() {
        Resources resources = getResources();
        Line line = new Line(mPointValues).setColor(resources.getColor(R.color.data_chart_colour_1)).setStrokeWidth(0);  //折线的颜色（绿色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(HasLabels);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(hasLabelsOnlyForSelected);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
//        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line.setPointRadius(1);

        Line line2 = new Line(mPointValues2).setColor(resources.getColor(R.color.data_chart_colour_2)).setStrokeWidth(0);  //折线的颜色（红蛇）
        line2.setShape(ValueShape.DIAMOND);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line2.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line2.setFilled(true);//是否填充曲线的面积
        line2.setHasLabels(HasLabels);//曲线的数据坐标是否加上备注
        line2.setHasLabelsOnlyForSelected(hasLabelsOnlyForSelected);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line2.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
//        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line2.setPointRadius(1);

        Line line3 = new Line(mPointValues3).setColor(resources.getColor(R.color.data_chart_colour_3)).setStrokeWidth(0);
        line3.setShape(ValueShape.DIAMOND);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line3.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line3.setFilled(true);//是否填充曲线的面积
        line3.setHasLabels(HasLabels);//曲线的数据坐标是否加上备注
        line3.setHasLabelsOnlyForSelected(hasLabelsOnlyForSelected);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line3.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
//        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line3.setPointRadius(1);

        Line line4 = new Line(mPointValues4).setColor(resources.getColor(R.color.data_chart_colour_4)).setStrokeWidth(0);
        line4.setShape(ValueShape.DIAMOND);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line4.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line4.setFilled(true);//是否填充曲线的面积
        line4.setHasLabels(HasLabels);//曲线的数据坐标是否加上备注
        line4.setHasLabelsOnlyForSelected(hasLabelsOnlyForSelected);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line4.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
//        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line4.setPointRadius(1);

        Line line5 = new Line(mPointValues5).setColor(Color.WHITE).setStrokeWidth(0);
        line5.setShape(ValueShape.DIAMOND);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line5.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line5.setFilled(false);//是否填充曲线的面积
        line5.setHasLabels(false);//曲线的数据坐标是否加上备注
        line5.setHasLabelsOnlyForSelected(false);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line5.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
//        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line5.setPointRadius(1);

        Line line6 = new Line(mPointValues6).setColor(Color.WHITE).setStrokeWidth(0);
        line6.setShape(ValueShape.DIAMOND);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line6.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line6.setFilled(false);//是否填充曲线的面积
        line6.setHasLabels(false);//曲线的数据坐标是否加上备注
        line6.setHasLabelsOnlyForSelected(false);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line6.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
//        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line6.setPointRadius(1);


        lines.add(line);
        lines.add(line2);
        lines.add(line3);
        lines.add(line4);
        lines.add(line5);
        lines.add(line6);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(6);//设置字体大小

//        axisX.setMaxLabelChars(5);
        axisX.setMaxLabelChars(3); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues); //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
//        data.setAxisXTop(axisX);  //x 轴在顶部
//        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setMaxLabelChars(7);
        axisY.setTextSize(6);//设置字体大小
        axisY.setTextColor(Color.BLACK);
        axisY.setHasLines(true);
        List<AxisValue> values = new ArrayList<>();
        for (int i = 0; i < 80; i += 10) {
            AxisValue value = new AxisValue(i);
            String label = "" + i;
            value.setLabel(label);
            values.add(value);
        }
        axisY.setValues(values);
//        axisY.setValues(mAxisYValues);
        data.setAxisYLeft(axisY);  //Y轴设置在左边
//        data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        line_chart_view.setInteractive(true);
        line_chart_view.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        line_chart_view.setMaxZoom((float) 2);//最大方法比例
        line_chart_view.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        line_chart_view.setLineChartData(data);

        line_chart_view.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(line_chart_view.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        v.bottom = -30;
        line_chart_view.setCurrentViewport(v);
    }


    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        for (int i=0;i<7;i++){
            mPointValues.add(new PointValue((float) i,(float) Math.random()*40));
            mPointValues2.add(new PointValue((float) i,(float) Math.random()*40));
            mPointValues3.add(new PointValue((float) i,(float) Math.random()*40));
            mPointValues4.add(new PointValue((float) i,(float) Math.random()*40));
            mPointValues5.add(new PointValue((float) i,(float) 40));
            mPointValues6.add(new PointValue((float) i,(float) 0));
        }
    }

    /**
     * X 轴的显示
     */
    private void getAxisXLables() {
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_equipment_information;
    }

    @Override
    public void onAddImgClick() {
        showPopupWindow();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, ImagePreview.class);
        Bundle bundle = new Bundle();
            bundle.putSerializable("dirPaths", imgUrls);
        bundle.putInt("index", i);
        intent.putExtras(bundle);
        startActivityForResult(intent, IMGPRE);
        overridePendingTransition(R.anim.img_pre_start, R.anim.img_pre_close);
    }

    /**
     * 点击拍照的回调
     */
    @Override
    public void toPhotograph() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURECODE);
    }

    /**
     * 点击相册选择的回调
     */
    @Override
    public void toAlbum() {
        Intent intent = new Intent(this, ImageSelectActivity.class);
        intent.putExtra(CURRENT_IMAGE_LIST_LENGTH,imgUrls.size());
        startActivityForResult(intent, ALBUMCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == CAPTURECODE) {
                Bitmap bm = (Bitmap) data.getExtras().get("data");
                setPathToSet(bm);
            } else if (requestCode == ALBUMCODE) {
                Bundle bundle = data.getExtras();
                HashSet<String> albumPath = (HashSet<String>) bundle.getSerializable("dirPaths");
                imgUrls.addAll(albumPath);
            } else if (requestCode == IMGPRE) {
                int index = data.getIntExtra("index", -1);
                imgUrls.remove(imgUrls.remove(index));
//                addimgFragment.setmDatas(mDirPaths);
            }
            imgUpLoadAdapter.notifyDataSetChanged();
            SetListViewAndGridViewHeightUtils.setGridViewHeight(errorImgSubmitGrid);
        }
    }

    public void setPathToSet(Bitmap bm) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(EquipmentInformationActivity.this, "SD卡不能使用", Toast.LENGTH_SHORT).show();
            return;
        }
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root, "WaterWorksErrorImg");
        if (!file.exists()) {
            file.mkdir();
        }
        FileOutputStream fos = null;
        String imgName = System.currentTimeMillis() + ".jpg";
        File imgFile = new File(file, imgName);
        try {
            fos = new FileOutputStream(imgFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imgUrls.add(imgFile.getAbsolutePath());
    }


    @Override
    public void getInspectionItemSuccess(InspectionItemBean data) {
        //做数据处理
    }

    @Override
    public void getInspectionItemFail(String errorMsg) {
//        if (dialogManager==null)
//            dialogManager = new DialogManager(this);
//        dialogManager.showRecordingDialog(errorMsg);
        ToastUtils.showToast(this,errorMsg);
    }

    @Override
    public void getMonitoringPointDataSuccess(ArrayList<MonitoringPointDataBean> data) {
        //做数据处理
        ifGetDevice = true;
    }

    @Override
    public void getMonitoringPointDataFail(String errorMsg) {
//        if (dialogManager==null)
//            dialogManager = new DialogManager(this);
//        dialogManager.showRecordingDialog(errorMsg);
        ToastUtils.showToast(this,errorMsg);
    }

    @Override
    public void onClick(View view) {
        if (!ifGetDevice)
            return;
        EquipmentMsgSubmitModel equipmentMsgSubmitModel = new EquipmentMsgSubmitModel(this,this);
        equipmentMsgSubmitModel.submitMsg();
        if (dialog==null)
            dialog = new SimperDialog(this);
        dialog.setTitle("设备信息提交中...").showDialog();
    }

    @Override
    public Map<Integer, Boolean> getEquipmentInformation() {
        return equipmentSubmitAdapter.getSubmitValue();
    }

    @Override
    public String getEquipmentDetailedInformation() {
        return errorMsgSubmitEdit.getText().toString();
    }

    @Override
    public ArrayList<String> getEquipmentImageInformation() {
        return imgUrls;
    }

    @Override
    public void submitSuccess() {
        if (ifFinsh)
            return;
        dialog.hideDialog();
    }

    @Override
    public void submitFail(String errorMsg) {
        if (ifFinsh)
            return;
        dialog.hideDialog();
//        if (dialogManager==null)
//            dialogManager = new DialogManager(this);
//        dialogManager.showRecordingDialog(errorMsg);
        ToastUtils.showToast(this,errorMsg);
    }

    @Override
    public void getDeviceSuccess(String deviceID) {
//        Log.e("nfcId",deviceID);
//        initRecycler();
//        initChart();
        ifReadDeviceIdSuccess = true;
        dialog.hideDialog();
        ToastUtils.showToast(this, deviceID);
    }

    @Override
    public void getDeviceFail() {
        ToastUtils.showToast(this,"读卡失败");
        finish();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (!ifReadDeviceIdSuccess)
            getDeviceFail();
    }
}
