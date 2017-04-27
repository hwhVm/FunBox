package com.beini.ui.fragment.popupwindow;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.beini.R;
import com.beini.base.BaseFragment;
import com.beini.bind.ContentView;
import com.beini.bind.Event;
import com.beini.bind.ViewInject;
import com.beini.ui.fragment.popupwindow.bean.CardBean;
import com.beini.ui.fragment.popupwindow.bean.JsonBean;
import com.beini.ui.fragment.popupwindow.bean.ProvinceBean;
import com.beini.ui.fragment.popupwindow.mode.PopuWindwoModel;
import com.beini.ui.fragment.popupwindow.view.OptionsPickerView;
import com.beini.ui.fragment.popupwindow.view.TimePickerView;
import com.beini.ui.fragment.popupwindow.view.listener.CustomListener;
import com.beini.ui.fragment.popupwindow.view.utils.GetJsonDataUtil;
import com.beini.utils.BLog;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Create by beini 2017/3/21
 * http://www.webpiaoliang.com/xuexi/wysj/biancheng/269177.html
 */
@ContentView(R.layout.fragment_popup_window)
public class PopupWindowFragment extends BaseFragment implements PopupWindow.OnDismissListener, View.OnClickListener {
    @ViewInject(R.id.btn_Options)
    Button  btn_Options;
    @ViewInject(R.id.btn_CustomTime)
    Button btn_CustomTime;
    @ViewInject(R.id.btn_Time)
    Button  btn_Time;
    @ViewInject(R.id.btn_CustomOptions)
    Button  btn_CustomOptions;
    @ViewInject(R.id.btn_no_linkage)
    Button  btn_no_linkage;
    @ViewInject(R.id.btn_GotoJsonData)
    Button  btn_GotoJsonData;

    private PopupWindow popupWindow;
    private int navigationHeight;
    private PopuWindwoModel popuWindwoModel;
    private TimePickerView pvTime, pvCustomTime;
    private OptionsPickerView pvOptions, pvCustomOptions, pvNoLinkOptions;

    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
 /*   private ArrayList<ArrayList<ArrayList<IPickerViewData>>> options3Items = new ArrayList<>();*/
 private ArrayList<CardBean> cardItem = new ArrayList<>();

    private ArrayList<String> food = new ArrayList<>();
    private ArrayList<String> clothes = new ArrayList<>();
    private ArrayList<String> computer = new ArrayList<>();
    @Override
    public void initView() {
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        navigationHeight = getResources().getDimensionPixelSize(resourceId);
        BLog.d("     navigationHeight===" + navigationHeight);
        navigationHeight = 15;
        popuWindwoModel = new PopuWindwoModel(PopupWindowFragment.this);
        //
        //最好等数据加载完毕再初始化并显示，以免数据量大的时候，还未加载完毕就显示，造成APP崩溃。
        initTimePicker();
        initCustomTimePicker();

        initOptionData();
        initOptionPicker();
        initCustomOptionPicker();
        initNoLinkOptionsPicker();
    }


    @Event({R.id.btn_data,R.id.btn_show,R.id.btn_open, R.id.btn_Time, R.id.btn_Options, R.id.btn_CustomOptions, R.id.btn_CustomTime, R.id.btn_no_linkage, R.id.btn_GotoJsonData})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                popuWindwoModel.openPopupWindow(view);
                break;
            case R.id.btn_Time:
                // pvTime.setDate(Calendar.getInstance());
           /* pvTime.show(); //show timePicker*/
                pvTime.show(view);//弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
                break;
            case R.id.btn_Options:
                pvOptions.show(); //弹出条件选择器
                break;
            case R.id.btn_CustomOptions:
                pvCustomOptions.show(); //弹出自定义条件选择器
                break;
            case R.id.btn_CustomTime:
                pvCustomTime.show(); //弹出自定义时间选择器
                break;
            case R.id.btn_no_linkage:
                pvNoLinkOptions.show();
                break;
            case R.id.btn_GotoJsonData:
                break;
            case R.id.btn_data:
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        parseJsonFormAssets();
                    }
                }.start();
                break;
            case R.id.btn_show:
                ShowPickerView();
                break;

        }
    }


    @Override
    public void onDismiss() {
        popuWindwoModel.setBackgroundAlpha(1);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                popuWindwoModel.openPopupWindow(view);
                break;
            case R.id.tv_pick_phone:
                Toast.makeText(baseActivity, "从手机相册选择", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                break;
            case R.id.tv_pick_zone:
                Toast.makeText(baseActivity, "从空间相册选择", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                break;
            case R.id.tv_cancel:
                popupWindow.dismiss();
                break;

        }
    }

    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView  pvOptions = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items2.get(options1).getPickerViewText()+
                        options2Items2.get(options1).get(options2)+
                        options3Items2.get(options1).get(options2).get(options3);

                Toast.makeText(getActivity(),tx,Toast.LENGTH_SHORT).show();
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items2, options2Items2,options3Items2);//三级选择器
        pvOptions.show();
    }

    private void initTimePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2013, 0, 23);

        Calendar endDate = Calendar.getInstance();
        endDate.set(2019, 11, 28);
        //时间选择器
        pvTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null

                /*btn_Time.setText(getTime(date));*/
                Button btn = (Button) v;
                btn.setText(getTime(date));
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setLabel("", "", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.DKGRAY)
                .setContentSize(20)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .build();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
    private void initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014,1,23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027,2,28);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                btn_CustomTime.setText(getTime(date));
            }
        })
                /*.setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
               /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*/
               /*.gravity(Gravity.RIGHT)// default is center*/
                .setDate(selectedDate)
                .setRangDate(startDate,endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(Color.RED)
                .build();

    }

    private void initOptionData() {

        /**
         * 注意：如果是添加JavaBean实体数据，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */

        getCardData();
        getNoLinkData();

        //选项1
        options1Items.add(new ProvinceBean(0,"广东","描述部分","其他数据"));
        options1Items.add(new ProvinceBean(1,"湖南","描述部分","其他数据"));
        options1Items.add(new ProvinceBean(2,"广西","描述部分","其他数据"));

        //选项2
        ArrayList<String> options2Items_01 = new ArrayList<>();
        options2Items_01.add("广州");
        options2Items_01.add("佛山");
        options2Items_01.add("东莞");
        options2Items_01.add("珠海");
        ArrayList<String> options2Items_02 = new ArrayList<>();
        options2Items_02.add("长沙");
        options2Items_02.add("岳阳");
        options2Items_02.add("株洲");
        options2Items_02.add("衡阳");
        ArrayList<String> options2Items_03 = new ArrayList<>();
        options2Items_03.add("桂林");
        options2Items_03.add("玉林");
        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);
        options2Items.add(options2Items_03);

        /*--------数据源添加完毕---------*/
    }
    private void getCardData() {
        for (int i = 0; i < 5; i++) {
            cardItem.add(new CardBean(i, "No.ABC12345 " + i));
        }
    }

    private void getNoLinkData() {
        food.add("KFC");
        food.add("MacDonald");
        food.add("Pizza hut");

        clothes.add("Nike");
        clothes.add("Adidas");
        clothes.add("Anima");

        computer.add("ASUS");
        computer.add("Lenovo");
        computer.add("Apple");
        computer.add("HP");
    }
    private void initOptionPicker() {//条件选择器初始化

        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */

        pvOptions = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(options2)
                       /* + options3Items.get(options1).get(options2).get(options3).getPickerViewText()*/;
                btn_Options.setText(tx);
            }
        })
                .setTitleText("城市选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.GREEN)//设置分割线的颜色
                .setSelectOptions(0,1)//默认选中项
                .setBgColor(Color.BLACK)
                .setTitleBgColor(Color.DKGRAY)
                .setTitleColor(Color.LTGRAY)
                .setCancelColor(Color.YELLOW)
                .setSubmitColor(Color.YELLOW)
                .setTextColorCenter(Color.LTGRAY)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("省","市","区")
                .build();

        //pvOptions.setSelectOptions(1,1);

        /*pvOptions.setPicker(options1Items);//一级选择器*/
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/

    }

    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = cardItem.get(options1).getPickerViewText();
                btn_CustomOptions.setText(tx);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        final TextView tvAdd = (TextView) v.findViewById(R.id.tv_add);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });

                        tvAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getCardData();
                                pvCustomOptions.setPicker(cardItem);
                            }
                        });

                    }
                })
                .isDialog(true)
                .build();

        pvCustomOptions.setPicker(cardItem);//添加数据

    }
    private void initNoLinkOptionsPicker() {// 不联动的多级选项
        pvNoLinkOptions = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                String str = "food:"+food.get(options1)
                        +"\nclothes:"+clothes.get(options2)
                        +"\ncomputer:"+computer.get(options3);

                Toast.makeText(getActivity().getApplicationContext(),str,Toast.LENGTH_SHORT).show();
            }
        }).build();
        pvNoLinkOptions.setNPicker(food,clothes,computer);
    }

    private ArrayList<JsonBean> options1Items2 = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items2 = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items2 = new ArrayList<>();
    public void  parseJsonFormAssets() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "开始解析", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(getActivity(),"province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items2 = jsonBean;

        for (int i=0;i<jsonBean.size();i++){//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c=0; c<jsonBean.get(i).getCityList().size(); c++){//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        ||jsonBean.get(i).getCityList().get(c).getArea().size()==0) {
                    City_AreaList.add("");
                }else {

                    for (int d=0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items2.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items2.add(Province_AreaList);
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "结束解析", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }
    /**
     * get   set
     */
    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    public void setPopupWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }

    public int getNavigationHeight() {
        return navigationHeight;
    }

    public void setNavigationHeight(int navigationHeight) {
        this.navigationHeight = navigationHeight;
    }

}
