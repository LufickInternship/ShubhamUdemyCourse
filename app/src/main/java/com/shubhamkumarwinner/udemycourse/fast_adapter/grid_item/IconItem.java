package com.shubhamkumarwinner.udemycourse.fast_adapter.grid_item;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.ISubItem;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.view.IconicsImageView;
import com.shubhamkumarwinner.udemycourse.R;

import java.util.List;

public class IconItem<T extends IItem & IExpandable> extends AbstractItem<IconItem<T>, IconItem.ViewHolder> implements ISubItem<IconItem, T> {

    public IIcon mIcon;
    private T mParent;

    public IconItem withIcon(IIcon icon) {
        this.mIcon = icon;
        return this;
    }

    @Override
    public T getParent() {
        return mParent;
    }

    @Override
    public IconItem withParent(T parent) {
        mParent = parent;
        return this;
    }

    @Override
    public int getType() {
        return R.id.icon_parent;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.icon_item;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);


        viewHolder.image.setIcon(new IconicsDrawable(viewHolder.image.getContext(), mIcon));
        viewHolder.name.setText(mIcon.getName());
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.image.setImageDrawable(null);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        public TextView name;
        public IconicsImageView image;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            image = view.findViewById(R.id.icon);
            this.view = view;
        }
    }
}
