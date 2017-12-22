package com.creedon.androidphotobrowser;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    View mask;
    SimpleDraweeView simpleDraweeView;
    CheckBox checkBox;
    ImageView videoIcon;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.image);

        checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        checkBox.setVisibility(View.INVISIBLE);
        checkBox.setClickable(false);

        mask = (View) itemView.findViewById(R.id.mask);

        mask.setVisibility(View.INVISIBLE);
        videoIcon = (ImageView) itemView.findViewById(R.id.iv_videoIcon);
    }

    @Override
    public void onClick(View view) {
//            Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }

    public void setVideoIcon(boolean isVisible){
        videoIcon.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }
}