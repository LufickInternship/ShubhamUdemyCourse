package com.shubhamkumarwinner.udemycourse.fast_adapter.swipe_list;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter_extensions.drag.IDraggable;
import com.mikepenz.fastadapter_extensions.swipe.ISwipeable;
import com.mikepenz.materialize.holder.StringHolder;
import com.shubhamkumarwinner.udemycourse.R;

import java.util.List;

public class SwipeableItem extends AbstractItem<SwipeableItem, SwipeableItem.SwipableViewHolder>
        implements ISwipeable<SwipeableItem, IItem>, IDraggable<SwipeableItem, IItem> {
    public StringHolder name;
    public StringHolder description;


    public int swipedDirection;
    private Runnable swipedAction;
    public boolean swipeable = true;
    public boolean draggable = true;

    public SwipeableItem withName(String Name) {
        this.name = new StringHolder(Name);
        return this;
    }

    public SwipeableItem withName(@StringRes int NameRes) {
        this.name = new StringHolder(NameRes);
        return this;
    }

    public SwipeableItem withDescription(String description) {
        this.description = new StringHolder(description);
        return this;
    }

    public SwipeableItem withDescription(@StringRes int descriptionRes) {
        this.description = new StringHolder(descriptionRes);
        return this;
    }


    @NonNull
    @Override
    public SwipableViewHolder getViewHolder(@NonNull View v) {
        return new SwipableViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.fastadapter_image_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.swipable_item;
    }

    @Override
    public void bindView(@NonNull SwipableViewHolder viewHolder, @NonNull List<Object> payloads) {
        super.bindView(viewHolder, payloads);
        //set the text for the name
        StringHolder.applyTo(name, viewHolder.name);
        //set the text for the description or hide
        StringHolder.applyToOrHide(description, viewHolder.description);

        viewHolder.swipeResultContent.setVisibility(swipedDirection != 0 ? View.VISIBLE : View.GONE);
        viewHolder.itemContent.setVisibility(swipedDirection != 0 ? View.GONE : View.VISIBLE);

        CharSequence swipedAction = null;
        CharSequence swipedText = null;
        if (swipedDirection != 0) {
            swipedAction = viewHolder.itemView.getContext().getString(R.string.action_undo);
            swipedText = swipedDirection == ItemTouchHelper.LEFT ? "Removed" : "Archived";
            viewHolder.swipeResultContent.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), swipedDirection == ItemTouchHelper.LEFT ? R.color.md_red_900 : R.color.md_blue_900));
        }
        viewHolder.swipedAction.setText(swipedAction == null ? "" : swipedAction);
        viewHolder.swipedText.setText(swipedText == null ? "" : swipedText);
        viewHolder.swipedActionRunnable = this.swipedAction;
    }

    @Override
    public void unbindView(@NonNull SwipableViewHolder holder) {
        super.unbindView(holder);
        holder.name.setText(null);
        holder.description.setText(null);
        holder.swipedAction.setText(null);
        holder.swipedText.setText(null);
        holder.swipedActionRunnable = null;
    }

    @Override
    public boolean isDraggable() {
        return draggable;
    }

    @Override
    public SwipeableItem withIsDraggable(boolean draggable) {
        this.draggable = draggable;
        return this;
    }

    @Override
    public boolean isSwipeable() {
        return swipeable;
    }

    @Override
    public SwipeableItem withIsSwipeable(boolean swipeable) {
        this.swipeable = swipeable;
        return this;
    }

    public void setSwipedDirection(int swipedDirection) {
        this.swipedDirection = swipedDirection;
    }

    public void setSwipedAction(Runnable action) {
        this.swipedAction = action;
    }

    static class SwipableViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView description;
        View swipeResultContent;
        View itemContent;
        TextView swipedText;
        TextView swipedAction;

        public Runnable swipedActionRunnable;


        public SwipableViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.material_drawer_name);
            description = itemView.findViewById(R.id.material_drawer_description);
            swipeResultContent = itemView.findViewById(R.id.swipe_result_content);
            itemContent = itemView.findViewById(R.id.item_content);
            swipedText = itemView.findViewById(R.id.swiped_text);
            swipedAction = itemView.findViewById(R.id.swiped_action);

            swipedAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (swipedActionRunnable != null) {
                        swipedActionRunnable.run();
                    }
                }
            });
        }
    }
}
