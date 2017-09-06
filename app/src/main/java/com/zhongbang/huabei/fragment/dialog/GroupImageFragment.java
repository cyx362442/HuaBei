package com.zhongbang.huabei.fragment.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.zhongbang.huabei.R;

public class GroupImageFragment extends DialogFragment {
    public static GroupImageFragment newInstance() {
        Bundle args = new Bundle();
        GroupImageFragment fragment = new GroupImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View inflate = layoutInflater.inflate(R.layout.fragment_group_image, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(inflate);
        return builder.show();
    }
}
