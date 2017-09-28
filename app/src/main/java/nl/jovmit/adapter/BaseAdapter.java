package nl.jovmit.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import nl.jovmit.gendapter.annotations.RecyclerAdapter;

@RecyclerAdapter(itemType = RecyclerItem.class)
public class BaseAdapter extends BaseGendapter {

    @Override
    protected int layoutResource() {
        return R.layout.recycler_item_view_one;
    }

    @Override
    protected BaseAdapterViewHolder createViewHolder(@NonNull View view) {
        return new ViewHolder(view);
    }
}