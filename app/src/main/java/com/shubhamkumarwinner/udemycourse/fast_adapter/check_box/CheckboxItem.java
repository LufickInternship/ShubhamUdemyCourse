package com.shubhamkumarwinner.udemycourse.fast_adapter.check_box;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.materialize.holder.StringHolder;
import com.shubhamkumarwinner.udemycourse.R;

import java.util.List;

public class CheckboxItem extends AbstractItem<CheckboxItem, CheckboxItem.ViewHolder> {

    public String header;
    public StringHolder name;
    public StringHolder description;

    public CheckboxItem withHeader(String header) {
        this.header = header;
        return this;
    }

    public CheckboxItem withName(String Name) {
        this.name = new StringHolder(Name);
        return this;
    }

    public CheckboxItem withName(@StringRes int NameRes) {
        this.name = new StringHolder(NameRes);
        return this;
    }

    public CheckboxItem withDescription(String description) {
        this.description = new StringHolder(description);
        return this;
    }

    public CheckboxItem withDescription(@StringRes int descriptionRes) {
        this.description = new StringHolder(descriptionRes);
        return this;
    }

    /**
     * defines the type defining this item. must be unique. preferably an id
     *
     * @return the type
     */
    @Override
    public int getType() {
        return R.id.checkbox_parent;
    }

    /**
     * defines the layout which will be used for this item in the list
     *
     * @return the layout for this item
     */
    @Override
    public int getLayoutRes() {
        return R.layout.check_box_item;
    }

    /**
     * binds the data of this item onto the viewHolder
     *
     * @param viewHolder the viewHolder of this item
     */
    @Override
    public void bindView(@NonNull ViewHolder viewHolder, @NonNull List<Object> payloads) {
        super.bindView(viewHolder, payloads);

        viewHolder.checkBox.setChecked(isSelected());

        //set the text for the name
        StringHolder.applyTo(name, viewHolder.name);
        //set the text for the description or hide
        StringHolder.applyToOrHide(description, viewHolder.description);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.name.setText(null);
        holder.description.setText(null);
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    /**
     * our ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        public CheckBox checkBox;
        TextView name;
        TextView description;

        public ViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkbox);
            name = view.findViewById(R.id.checkbox_name);
            description = view.findViewById(R.id.checkbox_description);
            this.view = view;
        }
    }

    //Todo ask question about this kind of method
    public static class CheckBoxClickEvent extends ClickEventHook<CheckboxItem> {
        @Override
        public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof CheckboxItem.ViewHolder) {
                return ((CheckboxItem.ViewHolder) viewHolder).checkBox;
            }
            return null;
        }

        @Override
        public void onClick(View v, int position, FastAdapter<CheckboxItem> fastAdapter, CheckboxItem item) {
            fastAdapter.toggleSelection(position);
        }
    }
}
