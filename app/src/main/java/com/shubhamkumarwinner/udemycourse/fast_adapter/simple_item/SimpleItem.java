package com.shubhamkumarwinner.udemycourse.fast_adapter.simple_item;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.shubhamkumarwinner.udemycourse.R;

import java.util.List;

public class SimpleItem extends AbstractItem<SimpleItem, SimpleItem.SimpleViewHolder> {
    public String name;

    public SimpleItem withName(String name) {
        this.name = name;
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

        TextView text;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            text = itemView.findViewById(R.id.fast_name);
        }

        @Override
        public void bindView(@NonNull SimpleItem item, @NonNull List<Object> payloads) {
            text.setText(item.name);
        }

        @Override
        public void unbindView(@NonNull SimpleItem item) {
            text.setText(null);
        }
    }
}
