package com.creedon.androidphotobrowser.common.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.creedon.androidphotobrowser.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import static android.content.Context.INPUT_METHOD_SERVICE;


/*
 * Created by Alexander Krol (troy379) on 29.08.16.
 */
public class ImageOverlayView extends RelativeLayout {
    private static final String TAG = ImageOverlayView.class.getSimpleName();
    private JSONObject data;
    private MaterialEditText etDescription;

    public interface ImageOverlayVieListener{

        void onDownloadButtonPressed(JSONObject data);

        void onTrashButtonPressed(JSONObject data);
        void onCaptionchnaged(JSONObject data, String caption);
    }
//    private TextView tvDescription;

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
        if(context instanceof ImageOverlayVieListener){
            listener = (ImageOverlayVieListener) context;
        }
    }

    public ImageOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        if(context instanceof ImageOverlayVieListener){
            listener = (ImageOverlayVieListener) context;
        }
    }

    public void setDescription(String description) {
//        tvDescription.setText(description);
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
//        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        etDescription = (MaterialEditText) view.findViewById(R.id.etDescription);

        etDescription.setVisibility(VISIBLE);
//        tvDescription.setVisibility(INVISIBLE);
        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG,"CharSequence "+s +" start "+ start + " before "+before + " count "+count );
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TODO submit text change
                if(listener != null){
                    listener.onCaptionchnaged(data,s.toString());
                }
            }
        });
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (etDescription.isFocused()) {
                        Rect outRect = new Rect();
                        etDescription.getGlobalVisibleRect(outRect);

                        if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                            etDescription.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
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
//        view.findViewById(R.id.btnEdit).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
                //TODO edit caption , pop confirmation , fire data
//                if(tvDescription.getVisibility() == VISIBLE) {
//                    tvDescription.setVisibility(INVISIBLE);
//                    etDescription.setVisibility(VISIBLE);
//
//                }
//                }else{
//                    tvDescription.setVisibility(VISIBLE);
//                    etDescription.setVisibility(INVISIBLE);
//                }
//            }
//        });
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
