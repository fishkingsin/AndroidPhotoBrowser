package com.creedon.androidphotobrowser.common.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creedon.androidphotobrowser.R;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

import org.json.JSONObject;

import static android.content.Context.INPUT_METHOD_SERVICE;


/*
 * Created by Alexander Krol (troy379) on 29.08.16.
 */
public class ImageOverlayView extends RelativeLayout {
    private static final String TAG = ImageOverlayView.class.getSimpleName();
    private JSONObject data;
    protected MaterialAutoCompleteTextView etDescription;
    private String originalDescription;

    public interface ImageOverlayVieListener {

        void onDownloadButtonPressed(JSONObject data);

        void onTrashButtonPressed(JSONObject data);

        void onCaptionChanged(JSONObject data, String caption);

        void onCloseButtonClicked();

        void onEditButtonClick(JSONObject data);

        void didEndEditing(JSONObject data, String s);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            Log.d(TAG, "CharSequence " + s + " start " + start + " before " + before + " count " + count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            //TODO submit text change

            if(listener != null && originalDescription.equals(s.toString())){
                originalDescription  = s.toString();
                listener.onCaptionChanged(data,s.toString());
                tvDescription.setText(originalDescription);
            }
        }
    };
    private TextView tvDescription;

    private String sharingText;

    public void setListener(ImageOverlayVieListener listener) {
        this.listener = listener;
    }

    protected ImageOverlayVieListener listener = null;

    public ImageOverlayView(Context context) {
        super(context);
        if (context instanceof ImageOverlayVieListener) {
            listener = (ImageOverlayVieListener) context;
        }
        init();

    }

    public ImageOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (context instanceof ImageOverlayVieListener) {
            listener = (ImageOverlayVieListener) context;
        }
        init();

    }

    public ImageOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (context instanceof ImageOverlayVieListener) {
            listener = (ImageOverlayVieListener) context;
        }
        init();

    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (etDescription.hasFocus()) {
//
//                Rect outRect = new Rect();
//
//                etDescription.getFocusedRect(outRect);
//                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
//                    etDescription.clearFocus();
//                    hideKeyboard(etDescription);
//                }
//
//            }
//        }
//        return super.dispatchTouchEvent(event);
//    }

    public void setDescription(String description) {
        if(!description.equals("")) {
            tvDescription.setText(description);
        }else{
            tvDescription.setText(null);
        }
//        etDescription.setText(description);
        originalDescription = description;
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

    protected View init() {
        View view = inflate(getContext(), R.layout.view_image_overlay, this);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        etDescription = (MaterialAutoCompleteTextView) view.findViewById(R.id.etDescription);
        tvDescription.setVisibility(VISIBLE);
        tvDescription.setMaxLines(4);
        etDescription.setVisibility(GONE);
        etDescription.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etDescription.setMaxLines(4);
        etDescription.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    etDescription.setText(originalDescription);
                    etDescription.addTextChangedListener(textWatcher);
                    etDescription.setVisibility(VISIBLE);
                    tvDescription.setVisibility(GONE);
                }else{
                    didEndEditing();

                }
            }
        });
        etDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    didEndEditing();
                    return true;
                }
                return false;
            }
        });
        tvDescription.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tvDescription.getVisibility() == VISIBLE) {
                    tvDescription.setVisibility(GONE);
                    etDescription.setVisibility(VISIBLE);
                    etDescription.setFocusableInTouchMode(true);
                    etDescription.requestFocus();
                    InputMethodManager lManager = (InputMethodManager)  getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    lManager.showSoftInput(etDescription, 0);
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

                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            etDescription.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
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
                if (listener != null) {
                    listener.onTrashButtonPressed(data);
                }
            }
        });
        view.findViewById(R.id.btnEdit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    //TODO dismiss image view
                    listener.onEditButtonClick(data);
                }
            }
        });

        view.findViewById(R.id.btnDownload).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDownloadButtonPressed(data);
                }
            }
        });
        view.findViewById(R.id.btnClose).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    //TODO dismiss image view
                    listener.onCloseButtonClicked();
                }
            }
        });
        return view;
    }

    private void didEndEditing() {

        if (listener != null) {
            listener.didEndEditing(data, etDescription.getText().toString());
        }
//        if(!etDescription.getText().toString().equals("")) {
        tvDescription.setText(etDescription.getText().toString());
//        }
        originalDescription = etDescription.getText().toString();
        etDescription.removeTextChangedListener(textWatcher);
        //TODO fire caption chaged lister
        tvDescription.setVisibility(VISIBLE);
        etDescription.setVisibility(GONE);
        etDescription.setFocusableInTouchMode(false);
        etDescription.clearFocus();
        hideKeyboard(etDescription);
    }

    private void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(INPUT_METHOD_SERVICE);
        if (manager != null)
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
