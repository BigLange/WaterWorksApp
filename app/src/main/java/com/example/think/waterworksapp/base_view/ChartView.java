package com.example.think.waterworksapp.base_view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.example.think.waterworksapp.bean.MonitoringPointDataBean;
import com.example.think.waterworksapp.utils.DateFormatUtlis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

/**
 * Created by Think on 2016/9/13.
 */

public class ChartView extends LineChartView {

    private final int X_AXIS_SIZE = 10;
    private float yAxisHeight;


    private List<AxisValue> mAxisXValues = new ArrayList<>();
    private List<AxisValue> mAxisYValues = new ArrayList<>() ;
    private List<Line> lines = new ArrayList<>();
    private LineChartData lineChartData;
    private ArrayList<String> dateArr;

    private  SimpleDateFormat simpleDateFormat;


    public ChartView(Context context) {
        this(context,null);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        simpleDateFormat = new SimpleDateFormat("HH-mm-ss");
        initView();
    }

    private void initView(){
        lineChartData = new LineChartData();
    }

    public void setData(HashMap<Long,String> colorMap, HashMap<Long,ArrayList<MonitoringPointDataBean>> dataMap){
        ArrayList<MonitoringPointDataBean> maxSizeData = null;
        for (long kpiCode:dataMap.keySet()){
            String colorStr = colorMap.get(kpiCode);
            ArrayList<MonitoringPointDataBean> dataArr = dataMap.get(kpiCode);
            setDataLine(dataArr,colorStr);
            if (maxSizeData==null)
                maxSizeData = dataArr;
            if (maxSizeData.size()<dataArr.size())
                maxSizeData = dataArr;
        }
        ArrayList<String> dateArr = dateArrByData(maxSizeData);
        getYAxisLables();
        getXAxisLables(dateArr);
        initAxis();
        initTopAndButton();
        this.setInteractive(true);
        this.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        this.setMaxZoom((float) X_AXIS_SIZE);//最大方法比例
        this.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        this.setLineChartData(lineChartData);
        lineChartData.setLines(lines);
        this.setLineChartData(lineChartData);

        this.setVisibility(View.VISIBLE);

        Viewport v = new Viewport(this.getMaximumViewport());
        v.left = 0;
        v.right = X_AXIS_SIZE;
        v.bottom = -30;
        this.setCurrentViewport(v);
    }

    private ArrayList<String> dateArrByData(ArrayList<MonitoringPointDataBean> maxSizeData) {
        ArrayList<String>  dateArr = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        if (maxSizeData.size()>10){
            for (int i=maxSizeData.size()-10;i<maxSizeData.size();i++){
                String dateStr = maxSizeData.get(i).getInsertTime();
                String simpleFormatDate = DateFormatUtlis.simpleFormat(dateStr,simpleDateFormat);
                dateArr.add(simpleFormatDate);
            }
        }else {
            for (MonitoringPointDataBean dataBean:maxSizeData){
                String dateStr = dataBean.getInsertTime();
                String simpleFormatDate = DateFormatUtlis.simpleFormat(dateStr,simpleDateFormat);
                dateArr.add(simpleFormatDate);
            }
        }
        return dateArr;
    }

    private void setDataLine(ArrayList<MonitoringPointDataBean> data,String colorStr) {
        ArrayList<Float> valueArr = new ArrayList<>();
        if (data.size()>10){
            for (int i = data.size()-10;i<data.size();i++){
                monitorPointToFloat(valueArr,i,data);
            }
        }else {
            for (int i = 0;i<data.size();i++){
                monitorPointToFloat(valueArr,i,data);
            }
        }
        ArrayList<PointValue> pointValues = getPointValues(valueArr);
        Line line = createLine(pointValues,Color.parseColor(colorStr),true,true);
        lines.add(line);
    }


    private void monitorPointToFloat(ArrayList<Float> floats,int position,ArrayList<MonitoringPointDataBean> data){
        MonitoringPointDataBean  monitoringPointDataBean = data.get(position);
        float value = monitoringPointDataBean.getValue();
        floats.add(value);
        if (value>yAxisHeight)
            yAxisHeight = value;
    }


    private void initTopAndButton() {
        Line buttonLine = createLine(getPointValues(getStraightLineValue(0)),Color.WHITE,false,false);
        Line topLine = createLine(getPointValues(getStraightLineValue(yAxisHeight+10)),Color.WHITE,false,false);
        lines.add(buttonLine);
        lines.add(topLine);
    }


    private void initAxis() {
        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setTextSize(6);//设置字体大小
        axisX.setMaxLabelChars(3); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues); //填充X轴的坐标名称
        lineChartData.setAxisXBottom(axisX); //x 轴在底部

        Axis axisY = new Axis();  //Y轴
        axisY.setMaxLabelChars(7);
        axisY.setTextSize(6);//设置字体大小
        axisY.setTextColor(Color.BLACK);
        axisY.setHasLines(true);
        axisY.setValues(mAxisYValues);
        lineChartData.setAxisYLeft(axisY);  //Y轴设置在左边
    }


    /**
     *  Y轴的赋值
     */
    private void getYAxisLables() {
        for (int i = 0; i < yAxisHeight; i += 10) {
            AxisValue value = new AxisValue(i);
            String label = "" + i;
            value.setLabel(label);
            mAxisYValues.add(value);
        }
    }

    private void getXAxisLables(ArrayList<String> xValues) {
        for (int i = 0; i < xValues.size(); i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(xValues.get(i)));
        }
    }



    private ArrayList<PointValue> getPointValues(ArrayList<Float> data){
        ArrayList<PointValue> pointValues = new ArrayList<>();
        for (int i=0;i<data.size();i++){
            pointValues.add(new PointValue(i,data.get(i)));
        }
        return pointValues;
    }

    private ArrayList<Float> getStraightLineValue(float height){
        ArrayList<Float> data = new ArrayList<>();
        for (int i=0;i<7;i++){
            data.add(height);
        }
        return data;
    }

    private Line createLine(ArrayList<PointValue> pointValue,int color,boolean ifFilled,boolean hashLabels){
        Line line = new Line(pointValue).setColor(color).setStrokeWidth(0);
        line.setShape(ValueShape.DIAMOND);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(ifFilled);//是否填充曲线的面积
        line.setHasLabels(false);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(hashLabels);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
//        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line.setPointRadius(1);
        return line;
    }


}
