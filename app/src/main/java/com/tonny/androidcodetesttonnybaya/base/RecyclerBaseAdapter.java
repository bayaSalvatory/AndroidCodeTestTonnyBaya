package com.tonny.androidcodetesttonnybaya.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tonny.androidcodetesttonnybaya.BR;

/**
 * Created by tbaya on 9/18/17.
 */
public abstract class RecyclerBaseAdapter extends RecyclerView.Adapter<RecyclerBaseAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, viewType,
                parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Object obj = getObjectForPosition(position);
        holder.bind(obj);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    public abstract int getLayoutIdForPosition(int position);

    public abstract Object getObjectForPosition(int position);

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding mBinding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        void bind(Object object) {
            mBinding.setVariable(BR.viewmodel, object);
            mBinding.executePendingBindings();
        }
    }
}
