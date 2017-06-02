package com.creedon.androidphotobrowser.common.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creedon.androidphotobrowser.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.KeyEvent.ACTION_DOWN;


/*
 * Created by Alexander Krol (troy379) on 29.08.16.
 */
public class ImageOverlayView extends RelativeLayout {
    private JSONObject data;
    private MaterialEditText etDescription;

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
        etDescription.setText(description);
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
        etDescription = (MaterialEditText) view.findViewById(R.id.etDescription);

        etDescription.setVisibility(INVISIBLE);
        tvDescription.setVisibility(VISIBLE);

        etDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == ACTION_DOWN){
                    hideKeyboard(v);
                    return true;
                }

                return false;
            }
        });


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
                if(tvDescription.getVisibility() == VISIBLE) {
                    tvDescription.setVisibility(INVISIBLE);
                    etDescription.setVisibility(VISIBLE);
                }else{
                    tvDescription.setVisibility(VISIBLE);
                    etDescription.setVisibility(INVISIBLE);
                }
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
        view.findViewById(R.id.btnClose).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    //TODO dismiss image view
                }
            }
        });
    }
    private void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(INPUT_METHOD_SERVICE);
        if (manager != null)
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
