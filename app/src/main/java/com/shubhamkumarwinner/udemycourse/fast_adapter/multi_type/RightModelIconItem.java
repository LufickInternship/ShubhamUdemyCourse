package com.shubhamkumarwinner.udemycourse.fast_adapter.multi_type;

import com.shubhamkumarwinner.udemycourse.R;

public class RightModelIconItem extends ModelIconItem{
    public RightModelIconItem(IconModel iconModel) {
        super(iconModel);
    }
    @Override
    public int getType() {
        return R.id.fastadapter_right_Model_icon_item_id;
    }

    /**
     * defines the layout which will be used for this item in the list
     *
     * @return the layout for this item
     */
    @Override
    public int getLayoutRes() {
        return R.layout.right_icon_item;
    }
}
