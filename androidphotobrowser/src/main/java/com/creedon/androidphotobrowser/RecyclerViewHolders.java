package com.creedon.androidphotobrowser;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.facebook.drawee.view.SimpleDraweeView;

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    SimpleDraweeView simpleDraweeView;
    CheckBox checkBox;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        simpleDraweeView = (SimpleDraweeView) itemView.findViewById(com.creedon.nixplay.androidphotobrowser.R.id.image);
        checkBox = (CheckBox) itemView.findViewById(com.creedon.nixplay.androidphotobrowser.R.id.checkBox);
        checkBox.setVisibility(View.INVISIBLE);
        checkBox.setClickable(false);
    }

    @Override
    public void onClick(View view) {
//            Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}