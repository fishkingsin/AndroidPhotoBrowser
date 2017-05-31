package com.creedon.androidphotobrowser.common.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creedon.androidphotobrowser.R;

import org.json.JSONObject;


/*
 * Created by Alexander Krol (troy379) on 29.08.16.
 */
public class ImageOverlayView extends RelativeLayout {
    private JSONObject data;

    public interface ImageOverlayVieListener{

        void onDownloadButtonPressed(JSONObject data);

        void onTrashButtonPressed(JSONObject data);
    }
    private TextView tvDescription;

    private String sharingText;

    public void setListener(ImageOverlayVieListener listener) {
        this.listener = listener;
    }

    ImageOverlayVieListener listener = null;
    public ImageOverlayView(Context context) {
        super(context);
        init();
        if(context instanceof ImageOverlayVieListener){
            listener = (ImageOverlayVieListener) context;
        }
    }

    public ImageOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setDescription(String description) {
        tvDescription.setText(description);
    }

    public void setData(JSONObject jsonObject) {
        this.data = jsonObject;
    }

    public void setShareText(String text) {
        this.sharingText = text;
    }

    private void sendShareIntent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sharingText);
        sendIntent.setType("text/plain");
        getContext().startActivity(sendIntent);
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_image_overlay, this);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        view.findViewById(R.id.btnTrash).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onTrashButtonPressed(data);
                }
            }
        });
        view.findViewById(R.id.btnEdit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO edit caption , pop confirmation , fire data
            }
        });
        view.findViewById(R.id.btnDownload).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onDownloadButtonPressed(data);
                }
            }
        });
    }
}
