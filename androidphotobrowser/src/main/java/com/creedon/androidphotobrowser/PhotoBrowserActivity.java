package com.creedon.androidphotobrowser;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.creedon.androidphotobrowser.common.data.models.CustomImage;
import com.creedon.androidphotobrowser.common.views.ImageOverlayView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

/*
 * Created by troy379 on 06.03.17.
 * Modified by James Kong on 28.05.2017
 */
public class PhotoBrowserActivity extends PhotoBrowserBasicActivity implements RecyclerViewAdapter.RecyclerViewAdapterListener {

    private static final String KEY_IS_DIALOG_SHOWN = "IS_DIALOG_SHOWN";
    private static final String KEY_CURRENT_POSITION = "CURRENT_POSITION";
    private OkHttpClient globalOkHttpClient3;

    public List<CustomImage> getImages() {
        return images;
    }

    private List<CustomImage> images;
    protected ImageViewer imageViewer;

    public ImageOverlayView getOverlayView() {
        return overlayView;
    }

    protected ImageOverlayView overlayView;

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    private int currentPosition;
    protected boolean isDialogShown;
    protected ArrayList<String> selections;
    protected boolean selectionMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(BuildConfig.DEBUG){
            Fresco.getImagePipeline().clearDiskCaches();
            Fresco.getImagePipeline().clearMemoryCaches();
        }
        super.onCreate(savedInstanceState);
        if(!Fresco.hasBeenInitialized()) {
            Context context = this;
//            globalOkHttpClient3 = new OkHttpClient();
//            ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
//                    .newBuilder(context,globalOkHttpClient3)
//                    .setNetworkFetcher(new OkHttp3NetworkFetcher(globalOkHttpClient3))
//                    .build();
//            Fresco.initialize(context, config);
            Fresco.initialize(this);
        }
        init();
        images = getCustomImages();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(listener.getActionBarTitle());
            actionBar.setSubtitle(listener.getSubtitle());
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_up_white_24dp);
        }

        selections = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            selections.add("0");
        }
    }

    private ImageViewer.Formatter<CustomImage> getCustomFormatter() {
        return new ImageViewer.Formatter<CustomImage>() {
            @Override
            public String format(CustomImage customImage) {
                return customImage.getUrl();
            }
        };
    }

    protected ImageViewer.OnImageChangeListener getImageChangeListener() {
        return new ImageViewer.OnImageChangeListener() {
            @Override
            public void onImageChange(int position) {
                CustomImage image = images.get(position);
                overlayView.setShareText(image.getUrl());
                overlayView.setDescription(image.getDescription());
            }
        };
    }

    protected ImageViewer.OnDismissListener getDismissListener() {
        return new ImageViewer.OnDismissListener() {
            @Override
            public void onDismiss() {
                isDialogShown = false;
            }
        };
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            isDialogShown = savedInstanceState.getBoolean(KEY_IS_DIALOG_SHOWN);
            currentPosition = savedInstanceState.getInt(KEY_CURRENT_POSITION);
        }

        if (isDialogShown) {
            showPicker(currentPosition);
        }
    }

    @Override
    protected void showPicker(int startPosition) {
        isDialogShown = true;
        currentPosition = startPosition;
        overlayView = new ImageOverlayView(this);
        imageViewer = new ImageViewer.Builder<>(this, posters)
                .setOverlayView(overlayView)
                .setStartPosition(startPosition)
                .setImageChangeListener(getImageChangeListener())
                .setOnDismissListener(getDismissListener())
                .setOnOrientationListener(getOrientationListener())
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_IS_DIALOG_SHOWN, isDialogShown);
        outState.putInt(KEY_CURRENT_POSITION, currentPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (!selectionMode) {
            inflater.inflate(R.menu.options_menu, menu);
            setupToolBar();
        } else {
            inflater.inflate(R.menu.menu, menu);
            setupToolBar();
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem actionViewItem = menu.findItem(R.id.miActionButton);
//        // Retrieve the action-view from menu
//        View v = MenuItemCompat.getActionView(actionViewItem);
//        // Find the button within action-view
//        Button b = (Button) v.findViewById(R.id.btnCustomAction);
        // Handle button click here
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (!selectionMode) {
            return true;
        } else {
            setupSelectionMode(false);
            return false;

        }
    }

    protected void setupToolBar() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (selectionMode) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_up_white_24dp);
        }
    }

    protected void setupSelectionMode(boolean b) {
        selectionMode = b;
        if (!selectionMode) {
            for (int i = 0; i < selections.size(); i++) {
                selections.set(i, "0");
            }
        } else {
            //TODO show bottom menu
        }
        rcAdapter.notifyDataSetChanged();

        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int i = item.getItemId();
        if (i == R.id.selectPhoto) {
            setupSelectionMode(!selectionMode);

        } else if (i == R.id.addPhotos) {
        } else if (i == R.id.addAlbumToPlaylist) {
        } else if (i == R.id.editAlbumName) {
        } else if (i == R.id.deleteAlbum) {
        } else if (i == R.id.delete) {
            //TODO delete item
        } else if (i == R.id.send) {
        } else if (i == R.id.download) {
        } else if (i == android.R.id.home) {
            if (!selectionMode) {
                finish();
            }

        } else {
            return super.onOptionsItemSelected(item);
        }
        return false;

    }

    @Override
    public void onItemClick(View view, int position) {
        if (selectionMode) {
            selections.set(position, selections.get(position).equals("1") ? "0" : "1");
            SquareCardView squareCardView = (SquareCardView) view;
            if (squareCardView != null) {
                CheckBox checkBox = (CheckBox) squareCardView.findViewById(R.id.checkBox);
                if (selectionMode) {
                    checkBox.setChecked(selections.get(position).equals("1"));
                }

            }
        } else {
            showPicker(position);
        }

    }

    @Override
    public void onItemLongClick(View view, int position) {
        // ...
        if (!selectionMode) {
            setupSelectionMode(true);
        }
        selections.set(position, selections.get(position).equals("1") ? "0" : "1");
        SquareCardView squareCardView = (SquareCardView) view;
        if (squareCardView != null) {
            CheckBox checkBox = (CheckBox) squareCardView.findViewById(R.id.checkBox);
            if (selectionMode) {
                checkBox.setChecked(selections.get(position).equals("1"));
            }

        }
    }

    @Override
    public boolean isPhotoSelected(int position) {
        return selections.get(position).equals("1");
    }

    @Override
    public boolean isPhotoSelectionMode() {
        return selectionMode;
    }

    @Override
    public int getOrientation(int position) {
        return 0;
    }

    protected List<CustomImage> getCustomImages() {
        if (listener == null) {

        } else {
            String[] posters;

            try {
                List<CustomImage> images = listener.getCustomImages(this);
                if (images == null) {
                    ArrayList<String> previewUrls = (ArrayList<String>) listener.photoBrowserPhotos(this);

                    ArrayList<String> captions = (ArrayList<String>) listener.photoBrowserPhotoCaptions(this);


                    posters = previewUrls.toArray(new String[0]);


                    try {
                        for (int i = 0; i < posters.length; i++) {
                            images.add(new CustomImage(posters[i], captions.get(i)));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return images;
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return null;

    }

    public void refreshCustomImage() {
        images = getCustomImages();
    }

    protected ImageViewer.OnOrientationListener getOrientationListener() {
        return new ImageViewer.OnOrientationListener() {
            @Override
            public int OnOrientaion(int currentPosition) {
                return 0;
            }
        };
    }
}
