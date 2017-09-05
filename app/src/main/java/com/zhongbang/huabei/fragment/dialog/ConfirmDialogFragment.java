package com.zhongbang.huabei.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhongbang.huabei.R;
import com.zhongbang.huabei.app.RenZhengActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmDialogFragment extends DialogFragment implements View.OnClickListener {
    public static ConfirmDialogFragment newInstance(String message) {
        Bundle args = new Bundle();
        args.putString("message",message);
        ConfirmDialogFragment fragment = new ConfirmDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = getArguments().getString("message");
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View inflate = layoutInflater.inflate(R.layout.fragment_confirm_dialog, null);
        TextView tvMsg = (TextView) inflate.findViewById(R.id.tv_msg);
        tvMsg.setText(message);
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(this);
        inflate.findViewById(R.id.btn_confirm).setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setView(inflate).show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_cancel){
            dismiss();
        }else{
            Intent intent = new Intent(getActivity(), RenZhengActivity.class);
            startActivity(intent);
            dismiss();
        }
    }
}
