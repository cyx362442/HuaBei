package com.zhongbang.huabei.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongbang.huabei.R;
import com.zhongbang.huabei.fragment.dialog.ConfirmDialogFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragmentTop extends Fragment {
    Unbinder unbinder;

    public MainFragmentTop() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_main_fragment_top, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_top1, R.id.ll_top2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_top1:
                ConfirmDialogFragment dialogFragment = ConfirmDialogFragment.newInstance(getString(R.string.dialogMsg));
                dialogFragment.show(getFragmentManager(),getString(R.string.dialog));
                break;
            case R.id.ll_top2:
                break;
        }
    }
}
