package com.shubhamkumarwinner.udemycourse.fast_adapter.multi_type;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.items.ModelAbstractItem;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.shubhamkumarwinner.udemycourse.R;

import java.util.List;

public class ModelIconItem extends ModelAbstractItem<IconModel, ModelIconItem, ModelIconItem.ViewHolder> {

    public ModelIconItem(IconModel iconModel) {
        super(iconModel);
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
    public void bindView(@NonNull ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);

        //define our data for the view
        viewHolder.image.setIcon(new IconicsDrawable(viewHolder.image.getContext(), getModel().icon));
        viewHolder.name.setText(getModel().icon.getName());
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.image.setImageDrawable(null);
        holder.name.setText(null);
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        protected View view;
        public TextView name;
        public IconicsImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.icon);
            this.view = itemView;
        }
    }
}
