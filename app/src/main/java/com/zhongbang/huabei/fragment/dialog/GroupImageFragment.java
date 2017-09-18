package com.zhongbang.huabei.fragment.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.zhongbang.huabei.R;
import com.zhongbang.huabei.app.GroupPhotoActivity;

import java.io.File;
import java.io.IOException;

public class GroupImageFragment extends DialogFragment implements View.OnClickListener {

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
        inflate.findViewById(R.id.btn_ok).setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(inflate);
        return builder.show();
    }

    @Override
    public void onClick(View v) {
        // 指定存储照片的路径
        Uri imageUri = Uri.fromFile(GroupPhotoActivity.getTempImage());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        getActivity().startActivityForResult(intent, GroupPhotoActivity.TAKE_PHOTO_REQUEST_CODE);
        dismiss();
    }
}
