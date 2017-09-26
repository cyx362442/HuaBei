package com.zhongbang.huabei.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.app.main_center.TxDetailActivity;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.VolleyResultListener;
import com.zhongbang.huabei.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillFragment extends Fragment {
    String pager_num = "0";
    private HashMap<String, String> HashMap_post = new HashMap<String, String>();
    private ArrayList<Zhangdan> zhangdan_hold = new ArrayList<Zhangdan>();
    private String urlPost = "http://chinaqmf.cn:8088/IHB/POST";
    private LVAdapter lvAdapter;
    private String username;
    private ListView listView;
    private int num = 1;
    public BillFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_bill, container, false);
        initUI(inflate);
        return inflate;
    }
    public void setposition(String position, String username) {
        pager_num = position;
        this.username = username;

    }
    class Zhangdan {
        String total_number2;
        String page_number;
        String order_number;
        String type;
        String real_otain;
        String time1;
        String state;
        String Id;
    }
    private void initUI(View inflate) {
        listView = (ListView) inflate.findViewById(R.id.listView_bill);
        lvAdapter = new LVAdapter();
        listView.setAdapter(lvAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(getActivity(),
                        TxDetailActivity.class);
                intent.putExtra("dingdanhao", zhangdan_hold.get(arg2).Id);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.come_in,
                        R.anim.exit);
            }
        });
    }
    public void onStart() {
        super.onStart();
        HashMap_post.clear();// 清空
        inntdata();
    }
    class LVAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return zhangdan_hold.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = getLayoutInflater(getArguments()).inflate(
                    R.layout.relative_bill_lv, null);
            TextView tixian = (TextView) inflate.findViewById(R.id.tixian);
            TextView jine = (TextView) inflate.findViewById(R.id.jine);
            TextView time = (TextView) inflate.findViewById(R.id.time);
            TextView stata = (TextView) inflate.findViewById(R.id.stata);
            tixian.setText(zhangdan_hold.get(position).type);
            jine.setText(zhangdan_hold.get(position).real_otain);
            time.setText(zhangdan_hold.get(position).time1);
            stata.setText(zhangdan_hold.get(position).state);
            if (position == num * 12 - 1) {
                num++;
                inntdata();
            }
            return inflate;
        }
    }
    void inntdata() {
        zhangdan_hold.clear();//初始化之前记得清空容器
        HashMap_post.put("type_all", "wdzd");
        HashMap_post.put("num", "10");
        HashMap_post.put("pag", "" + num);
        HashMap_post.put("username", username);// 此时这里就是要传的网页上对应的参数name的值
        HashMap_post.put("type", pager_num);// 此时这里就是要传的网页上对应的参数String的值
        http();
    }
    private void http() {
		/* 使用POST请求 */
        DownHTTP.postVolley(urlPost, HashMap_post, new VolleyResultListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                ToastUtil.showShort(getActivity(),"网络异");
            }

            @Override
            public void onResponse(String arg0) {
                // TsUtils.show(getActivity().getApplicationContext(), arg0);//
                // 用这个方法弹出提示
                Zhangdan zhangdan;
                try {
                    JSONArray array = new JSONArray(arg0);
                    for (int i = 0; i < array.length(); i++) {
                        zhangdan = new Zhangdan();
                        zhangdan.total_number2 = array.getJSONObject(i)
                                .getString("total_number2").trim();
                        zhangdan.page_number = array.getJSONObject(i)
                                .getString("page_number").trim();
                        zhangdan.order_number = array.getJSONObject(i)
                                .getString("order_number").trim();
                        zhangdan.type = array.getJSONObject(i)
                                .getString("type").trim();
                        zhangdan.real_otain = array.getJSONObject(i)
                                .getString("real_otain").trim();
                        zhangdan.time1 = array.getJSONObject(i)
                                .getString("time1").trim();
                        zhangdan.state = array.getJSONObject(i)
                                .getString("state").trim();
                        zhangdan.Id = array.getJSONObject(i).getString("Id")
                                .trim();
                        zhangdan_hold.add(i + num * 10 - 10, zhangdan);
                    }
                    lvAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
}
