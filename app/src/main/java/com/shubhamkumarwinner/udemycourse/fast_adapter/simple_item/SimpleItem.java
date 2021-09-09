package com.shubhamkumarwinner.udemycourse.fast_adapter.simple_item;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.commons.utils.FastAdapterUIUtils;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialize.holder.StringHolder;
import com.mikepenz.materialize.util.UIUtils;
import com.shubhamkumarwinner.udemycourse.R;

import java.util.List;

public class SimpleItem extends AbstractItem<SimpleItem, SimpleItem.SimpleViewHolder> {
    public StringHolder name;
    public StringHolder description;

    public SimpleItem withName(String name) {
        this.name = new StringHolder(name);
        return this;
    }

    public SimpleItem withDescription(String description) {
        this.description = new StringHolder(description);
        return this;
    }

    @NonNull
    @Override
    public SimpleViewHolder getViewHolder(@NonNull View v) {
        return new SimpleViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.fast_simple_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fast_adapter_simple_item;
    }

    static class SimpleViewHolder extends FastAdapter.ViewHolder<SimpleItem> {
        protected View view;

        TextView name;
        TextView description;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            name = itemView.findViewById(R.id.fast_name);
            description = itemView.findViewById(R.id.fast_description);
        }

        @Override
        public void bindView(@NonNull SimpleItem item, @NonNull List<Object> payloads) {
            StringHolder.applyTo(item.name, name);
            StringHolder.applyTo(item.description, description);
            UIUtils.setBackground(view, FastAdapterUIUtils.getSelectableBackground(itemView.getContext(), Color.GRAY, true));
        }

        @Override
        public void unbindView(@NonNull SimpleItem item) {
            description.setText(null);
        }
    }
}
