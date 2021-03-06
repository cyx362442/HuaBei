package com.zhongbang.huabei.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongbang.huabei.R;
import com.zhongbang.huabei.app.CollectionActivity;
import com.zhongbang.huabei.app.ShareGoodsActivity;
import com.zhongbang.huabei.contract.Config;
import com.zhongbang.huabei.fragment.dialog.ConfirmDialogFragment;
import com.zhongbang.huabei.utils.ShapreUtis;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragmentTop extends Fragment {
    Unbinder unbinder;
    private Intent mIntent;

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
        if (checkAudit()) return;
        switch (view.getId()) {
            case R.id.ll_top1:
                mIntent = new Intent(getActivity(), CollectionActivity.class);
                startActivity(mIntent);
                break;
            case R.id.ll_top2:
                mIntent = new Intent(getActivity(), ShareGoodsActivity.class);
                startActivity(mIntent);
                break;
        }
    }

    private boolean checkAudit() {
        if(!getString(R.string.audited).equals(Config.audit)){
            ConfirmDialogFragment fragment = ConfirmDialogFragment.newInstance(getString(R.string.dialogMsg),false);
            fragment.show(getFragmentManager(),getString(R.string.dialog));
            return true;
        }
        return false;
    }
}
