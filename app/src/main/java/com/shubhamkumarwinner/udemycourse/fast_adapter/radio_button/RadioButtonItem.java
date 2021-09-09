package com.shubhamkumarwinner.udemycourse.fast_adapter.radio_button;

import android.view.View;
import android.widget.RadioButton;
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
import java.util.Set;

public class RadioButtonItem extends AbstractItem<RadioButtonItem, RadioButtonItem.ViewHolder> {

    public String header;
    public StringHolder name;
    public StringHolder description;

    public RadioButtonItem withHeader(String header) {
        this.header = header;
        return this;
    }

    public RadioButtonItem withName(String Name) {
        this.name = new StringHolder(Name);
        return this;
    }

    public RadioButtonItem withName(@StringRes int NameRes) {
        this.name = new StringHolder(NameRes);
        return this;
    }

    public RadioButtonItem withDescription(String description) {
        this.description = new StringHolder(description);
        return this;
    }

    public RadioButtonItem withDescription(@StringRes int descriptionRes) {
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
        return R.id.radio_parent;
    }

    /**
     * defines the layout which will be used for this item in the list
     *
     * @return the layout for this item
     */
    @Override
    public int getLayoutRes() {
        return R.layout.radio_button_item;
    }

    /**
     * binds the data of this item onto the viewHolder
     *
     * @param viewHolder the viewHolder of this item
     */
    @Override
    public void bindView(@NonNull ViewHolder viewHolder, @NonNull List<Object> payloads) {
        super.bindView(viewHolder, payloads);

        viewHolder.radioButton.setChecked(isSelected());

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

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    /**
     * our ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        public RadioButton radioButton;
        TextView name;
        TextView description;

        public ViewHolder(View view) {
            super(view);
            radioButton = view.findViewById(R.id.radiobutton);
            name = view.findViewById(R.id.radio_name);
            description = view.findViewById(R.id.radio_description);
            this.view = view;
        }
    }

    public static class RadioButtonClickEvent extends ClickEventHook<RadioButtonItem> {
        @Override
        public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof RadioButtonItem.ViewHolder) {
                return ((ViewHolder) viewHolder).radioButton;
            }
            return null;
        }

        @Override
        public void onClick(View v, int position, FastAdapter<RadioButtonItem> fastAdapter, RadioButtonItem item) {
            if (!item.isSelected()) {
                Set<Integer> selections = fastAdapter.getSelections();
                if (!selections.isEmpty()) {
                    int selectedPosition = selections.iterator().next();
                    fastAdapter.deselect();
                    fastAdapter.notifyItemChanged(selectedPosition);
                }
                fastAdapter.select(position);
            }
        }
    }
}
