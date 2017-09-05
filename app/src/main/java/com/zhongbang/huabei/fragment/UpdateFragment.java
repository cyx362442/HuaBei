package com.zhongbang.huabei.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.zhongbang.huabei.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateFragment extends DialogFragment implements View.OnClickListener {

    private String mUrl;

    public static UpdateFragment newInstance(String url){
        UpdateFragment fragment = new UpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View inflate = inflater.inflate(R.layout.fragment_update, null);
        Bundle bundle = getArguments();
        mUrl = bundle.getString("url", "");
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(this);
        inflate.findViewById(R.id.btn_confirm).setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(inflate);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_cancel){
            dismiss();
        }else if(v.getId()==R.id.btn_confirm){
            if(!TextUtils.isEmpty(mUrl)){
                Uri uri = Uri.parse(mUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }
    }
}
