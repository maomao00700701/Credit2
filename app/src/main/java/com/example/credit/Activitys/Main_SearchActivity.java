package com.example.credit.Activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.credit.Adapters.syHisAdapter;
import com.example.credit.Dialogs.WaitDialog;
import com.example.credit.Entitys.DataManager;
import com.example.credit.R;
import com.example.credit.Services.CallServer;
import com.example.credit.Utils.ContainsEmojiEditText;
import com.example.credit.Utils.CreditSharePreferences;
import com.example.credit.Utils.GsonUtil;
import com.example.credit.Utils.MD5;
import com.example.credit.Utils.MyhttpCallBack;
import com.example.credit.Utils.Toast;
import com.example.credit.Utils.URLconstant;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yolanda.nohttp.RequestMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 主界面的【专利】，【商标】搜索界面
 */
public class Main_SearchActivity extends BaseActivity {
    @ViewInject(R.id.search_et1)
    EditText search_et;

    @ViewInject(R.id.arrow_backm)
    LinearLayout arrow_backm;
    String hit;
    public static Handler handler;
    public static WaitDialog wd;
    @ViewInject(R.id.search_bt1)
    ImageView search_bt;
    @ViewInject(R.id.search_et_cc2)
    ImageView search_et_cc;

    CreditSharePreferences csp=CreditSharePreferences.getLifeSharedPreferences();

    @ViewInject(R.id.his_yout)
    ListView his_yout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__search2);
        ViewUtils.inject(this);
        wd = new WaitDialog(this);
        Intent i = getIntent();
        hit = i.getStringExtra("hit");
        search_et.setHint("请输入" + hit + "信息");
        arrow_backm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) search_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(Main_SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //执行方法
                    if (!search_et.getText().toString().trim().equals("")) {
                        search();
                    } else {
                        Toast.show("搜索关键字不能为空!");
                    }

                    return true;
                }
                return false;
            }
        });
        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!search_et.getText().toString().trim().equals("")) {
                    search();
                } else {
                    Toast.show("搜索关键字不能为空!");
                }
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Intent i;
                switch (msg.what) {
                    case 0://商标
                        wd.dismiss();
                        Main_Search_ListActivity.listsb=DataManager.trademarkModelS.data.brandInfo;
                        i = new Intent(Main_SearchActivity.this, Main_Search_ListActivity.class);
                        i.putExtra("Tname", "商标");
                        i.putExtra("ETname", search_et.getText().toString());
                        i.putExtra("CurrentPage",DataManager.trademarkModelS.data.Paging.CurrentPage);
                        i.putExtra("TotalPage", DataManager.trademarkModelS.data.Paging.TotalPage);
                        startActivity(i);
                        break;
                    case 1://专利
                        wd.dismiss();
                        Main_Search_ListActivity.listzl=DataManager.zl_searchS.data.patentInfo;
                        i = new Intent(Main_SearchActivity.this, Main_Search_ListActivity.class);
                        i.putExtra("Tname", "专利");
                        i.putExtra("ETname", search_et.getText().toString());
                        i.putExtra("CurrentPage", DataManager.zl_searchS.data.Paging.CurrentPage);
                        i.putExtra("TotalPage", DataManager.zl_searchS.data.Paging.TotalPage);
                        startActivity(i);
                        break;
                    case 2://失信人
                        wd.dismiss();
                        Main_Search_ListActivity.listsx=DataManager.MyDishonestyS.data.courtcaseInfo;
                        i = new Intent(Main_SearchActivity.this, Main_Search_ListActivity.class);
                        i.putExtra("Tname", "失信人");
                        i.putExtra("ETname", search_et.getText().toString());
                        i.putExtra("CurrentPage", DataManager.MyDishonestyS.data.Paging.CurrentPage);
                        i.putExtra("TotalPage", DataManager.MyDishonestyS.data.Paging.TotalPage);
                        startActivity(i);
                        break;
                    case 500:
                        wd.dismiss();
                        Toast.show("暂无数据!");
                        break;
                }
            }
        };
        if(!hit.equals("招投标")){
            if ( DataManager.getSgHisList.data.searchHistory !=null && DataManager.getSgHisList.data.searchHistory.size()>0) {
                syHisAdapter adapter=new syHisAdapter(Main_SearchActivity.this,DataManager.getSgHisList.data.searchHistory);
                his_yout.setAdapter(adapter);
                his_yout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        search_et.setText(DataManager.getSgHisList.data.searchHistory.get(position).KEYWORDS+"");
                    }
                });
            }
        }
        search_et.addTextChangedListener(new TextWatcher() {//动态判断输入框中的字数并显示隐藏图标
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start >= 0) {
                    search_et_cc.setVisibility(View.VISIBLE);
                } else {
                    search_et_cc.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable t) {
                if (t.length() > 0) {
                    search_et_cc.setVisibility(View.VISIBLE);
                } else {
                    search_et_cc.setVisibility(View.GONE);
                }
            }
        });
        search_et_cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_et.setText("");
            }
        });
    }
    public void search() {
        wd.show();
        String urls = "";
        String nam = "";
        int num = 0;
        switch (hit) {
            case "商标":
                urls = URLconstant.URLINSER + URLconstant.TRADEMARKURL;
                nam = "brandName";
                num = 0x1001;
                break;
            case "专利":
                urls = URLconstant.URLINSER + URLconstant.PATENTURL;
                nam = "patentName";
                num = 0x1002;
                break;
            case "招投标":
//                urls = URLconstant.URLINSER + URLconstant.TBSREACH;
//                nam = "patentName";
                search_et.setHint("请输入招标名称");
                startActivity(new Intent(Main_SearchActivity.this,H5ViewActivity.class).putExtra("msg","7").putExtra("URL",URLconstant.TENDER).putExtra("KeyNo",search_et.getText().toString()));
                overridePendingTransition(R.anim.start_tran_one, R.anim.start_tran_two);
                wd.dismiss();
                break;
            case "失信人":
                urls = URLconstant.URLINSER + URLconstant.SXDETAILS;
                num = 0x1005;
                break;
        }

        if (hit != "招投标"&&!hit.equals("招投标")) {
            GsonUtil ComplaintsRuerst = new GsonUtil(urls, RequestMethod.GET);
            ComplaintsRuerst.add("deviceId", new Build().MODEL);
            if (hit != "失信人"&&!hit.equals("失信人")) {
                ComplaintsRuerst.add(nam, search_et.getText().toString());
                ComplaintsRuerst.add("token", MD5.MD5s("" + new Build().MODEL));
                ComplaintsRuerst.add("KeyNo", "");
            }else{
                ComplaintsRuerst.add("token", MD5.MD5s(search_et.getText().toString() + new Build().MODEL));
                ComplaintsRuerst.add("KeyNo", search_et.getText().toString());
                ComplaintsRuerst.add("pripid","");

            }
            if (hit != "招投标"&&!hit.equals("招投标")) {
                ComplaintsRuerst.add("pageIndex", 1);
                ComplaintsRuerst.add("pageSize", 10);
            }
            CallServer.getInstance().add(Main_SearchActivity.this, ComplaintsRuerst, MyhttpCallBack.getInstance(), num, true, false, true);
        }
    }

}
