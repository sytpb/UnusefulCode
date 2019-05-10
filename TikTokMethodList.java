package com.huruwo.xposed.xtask.hook.method;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.huruwo.xposed.R;
import com.huruwo.xposed.ui.MainActivity;
import com.huruwo.xposed.util.Constants;
import com.huruwo.xposed.util.LogXUtils;
import com.huruwo.xposed.xtask.comm.ListViewBaseAdapter;
import com.huruwo.xposed.xtask.comm.bean.ListBean;
import com.huruwo.xposed.xtask.debug.XposedUtils;
import com.huruwo.xposed.xtask.hook.polling.XhsPollingTask;
import com.huruwo.xposed.xtask.task.xhs.userdata.IXMethodData;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static android.widget.LinearLayout.VERTICAL;
import static com.huruwo.xposed.xtask.comm.XSharePrefeUtil.setXIsTest;
import static com.huruwo.xposed.xtask.comm.XSharePrefeUtil.setXTaskType;

/**
 * @author liuwan
 * @date 2019/5/11 0023
 * @action
 **/
public class TikTokMethodList extends HookMethodList {


    private static String C_CODE = "JP";
    private static String C_NAME = "日本";

    public static void initTikTok(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        LogXUtils.e("进入TikTokHOOK生效---");
        XposedBridge.hookAllMethods(TelephonyManager.class, "getSimCountryIso", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                LogXUtils.e("获取当前运营商" + C_CODE);
                param.setResult(C_CODE);
            }
        });
        hookUi(loadPackageParam);
    }

    public static void hookUi(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {


            @Override
            protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                String class_name = param.thisObject.getClass().getSimpleName();
                final Activity activity = (Activity) param.thisObject;
                if (class_name.equals("MainActivity")) {
                    FrameLayout frameLayout = (FrameLayout) activity.getWindow().getDecorView().getRootView();
                    frameLayout.setBackgroundColor(Color.RED);
                    LinearLayout linearLayout = new LinearLayout(activity);
                    LinearLayout.LayoutParams ll_layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    linearLayout.setOrientation(VERTICAL);
                    linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                    linearLayout.setLayoutParams(ll_layoutParams);


                    final TextView button1 = new TextView(activity);
                    button1.setBackgroundColor(Color.GRAY);
                    button1.setText("切换地区 当前" + C_NAME);
                    button1.setGravity(Gravity.CENTER);
                    button1.setTextSize(20);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(4, 20, 4, 4);
                    button1.setLayoutParams(layoutParams);
                    final List<ListBean> listBeans = new ArrayList<>();
                    //AU澳洲 BR巴西 CA加拿大 HK香港 ID印尼 IN印度
                    //JP日本 KP朝鲜 KR韩国 LA柬埔寨 MM缅甸 MO澳门
                    //MX墨西哥 MY马来西亚 NP泥泊尔 PH菲律宾 RU俄罗斯
                    //SG新加坡 TH泰国 TW台湾 US美国 VN越南


                    listBeans.add(new ListBean("AU", "澳洲"));
                    listBeans.add(new ListBean("BR", "巴西"));
                    listBeans.add(new ListBean("CA", "加拿大"));
                    listBeans.add(new ListBean("HK", "香港"));
                    listBeans.add(new ListBean("ID", "印尼"));
                    listBeans.add(new ListBean("IN", "印度"));
                    listBeans.add(new ListBean("JP", "日本"));
                    listBeans.add(new ListBean("KP", "朝鲜"));
                    listBeans.add(new ListBean("KR", "韩国"));
                    listBeans.add(new ListBean("LA", "柬埔寨"));
                    listBeans.add(new ListBean("MM", "缅甸"));
                    listBeans.add(new ListBean("MO", "澳门"));
                    listBeans.add(new ListBean("MX", "墨西哥"));
                    listBeans.add(new ListBean("MY", "马来西亚"));
                    listBeans.add(new ListBean("NP", "泥泊尔"));
                    listBeans.add(new ListBean("PH", "菲律宾"));
                    listBeans.add(new ListBean("RU", "俄罗斯"));
                    listBeans.add(new ListBean("SG", "新加坡"));
                    listBeans.add(new ListBean("TH", "泰国"));
                    listBeans.add(new ListBean("TW", "台湾"));
                    listBeans.add(new ListBean("US", "美国"));
                    listBeans.add(new ListBean("VN", "越南"));

                    final ListView listView = new ListView(activity);
                    LinearLayout.LayoutParams list_layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    listView.setLayoutParams(list_layoutParams);
                    listView.setBackgroundColor(Color.GRAY);
                    ListViewBaseAdapter listViewBaseAdapter = new ListViewBaseAdapter(activity, listBeans);
                    listView.setAdapter(listViewBaseAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ListBean listBean = listBeans.get(position);
                            C_CODE = listBean.getTaskid();
                            C_NAME = listBean.getMsg();
                            ToastUtils.showLong("切换到" + C_NAME);
                            button1.setText("切换地区  当前" + C_NAME);
                            listView.setVisibility(View.GONE);
                        }
                    });

                    linearLayout.addView(button1);
                    linearLayout.addView(listView);
                    frameLayout.addView(linearLayout);

                    listView.setVisibility(View.GONE);

                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

    }


}
