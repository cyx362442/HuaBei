package com.zhongbang.huabei.fragment;

import android.app.Dialog;
import android.app.usage.UsageEvents;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.volley.VolleyError;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.event.Dismiss;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.VolleyResultListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeDialog extends DialogFragment implements View.OnClickListener {
    private final String url="http://chinaqmf.cn:8088/ihuabei/app/system/announcements.app";
    private final String noticeUrl="http://chinaqmf.cn:8088/ihuabei/attached/announcement/";
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View inflate = inflater.inflate(R.layout.fragment_notice_dialog, null);
        final WebView wv = (WebView) inflate.findViewById(R.id.wv);
        inflate.findViewById(R.id.tv).setOnClickListener(this);
        WebSettings webSettings = wv .getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        DownHTTP.getVolley(url, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String html = jsonObject.getString("html");
                    wv.loadUrl(noticeUrl+html);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        this.setCancelable(false);
        builder.setView(inflate);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        EventBus.getDefault().post(new Dismiss());
        dismiss();
    }
}
