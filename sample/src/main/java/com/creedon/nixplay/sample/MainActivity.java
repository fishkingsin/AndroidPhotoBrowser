package com.creedon.nixplay.sample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.creedon.androidphotobrowser.PhotoBrowserActivity;
import com.creedon.androidphotobrowser.PhotoBrowserBasicActivity;
import com.creedon.androidphotobrowser.common.data.models.CustomImage;
import com.creedon.androidphotobrowser.common.views.ImageOverlayView;
import com.creedon.nixplay.sample.common.data.Demo;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends PhotoBrowserActivity implements PhotoBrowserBasicActivity.PhotoBrowserListener {

    private ArrayList<String> _previewUrls;
    private ArrayList<String> _thumbnailUrls;
    private ArrayList<String> _captions;

    @Override
    public List<CustomImage> getImages() {
        return super.getImages();
    }

    @Override
    public ImageOverlayView getOverlayView() {
        return super.getOverlayView();
    }

    @Override
    public int getCurrentPosition() {
        return super.getCurrentPosition();
    }

    @Override
    public void setCurrentPosition(int currentPosition) {
        super.setCurrentPosition(currentPosition);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listener = this;



        String jsonString = Demo.getFlickrs();
        try {

            JSONArray array = new JSONArray(jsonString);
            _previewUrls = new ArrayList();
            _thumbnailUrls = new ArrayList();
            for (int i = 0; i < array.length(); i++) {
                _previewUrls.add(array.getJSONObject(i).getString("previewUrl"));
                _thumbnailUrls.add(array.getJSONObject(i).getString("thumbnailUrl"));
            }
//            rowListItem = thumbnailUrls;
//            posters = previewUrls.toArray(new String[0]);
            _captions = new ArrayList<>(Arrays.asList(Demo.getDescriptions()));

        } catch (JSONException e) {
            e.printStackTrace();

        }
        super.onCreate(savedInstanceState);




    }

    @Override
    protected ImageViewer.OnImageChangeListener getImageChangeListener() {
        return super.getImageChangeListener();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void showPicker(int startPosition) {
        super.showPicker(startPosition);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    protected void setupToolBar() {
        super.setupToolBar();
    }

    @Override
    protected void setupSelectionMode(boolean b) {
        super.setupSelectionMode(b);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        super.onItemLongClick(view, position);
    }

    @Override
    public boolean isPhotoSelected(int position) {
        return super.isPhotoSelected(position);
    }

    @Override
    public boolean isPhotoSelectionMode() {
        return super.isPhotoSelectionMode();
    }

    @Override
    public void refreshCustomImage() {
        super.refreshCustomImage();
    }

    @Override
    public List<String> photoBrowserPhotos(PhotoBrowserBasicActivity activity) {
        return _previewUrls;
    }

    @Override
    public List<String> photoBrowserVideos(PhotoBrowserBasicActivity activity) {
        return _previewUrls;
    }

    @Override
    public List<String> photoBrowserThumbnails(PhotoBrowserBasicActivity activity) {
        return _thumbnailUrls;
    }

    @Override
    public String photoBrowserPhotoAtIndex(PhotoBrowserBasicActivity activity, int index) {
        return null;
    }

    @Override
    public List<String> photoBrowserPhotoCaptions(PhotoBrowserBasicActivity photoBrowserBasicActivity) {
        return _captions;
    }
    @Override
    public String getActionBarTitle() {
        return "Album";
    }
    @Override
    public String getSubtitle() {
        return "Subtitle";
    }

    @Override
    public List<CustomImage> getCustomImages(PhotoBrowserActivity photoBrowserActivity) {
        try {
            List<CustomImage> images = new ArrayList<CustomImage>();
//            ArrayList<String> previewUrls = (ArrayList<String>) listener.photoBrowserPhotos(this);
//
//            ArrayList<String> captions = (ArrayList<String>) listener.photoBrowserPhotoCaptions(this);


            try {
                for (int i = 0; i < _previewUrls.size(); i++) {
                    if(_captions.size()-1 < i){
                        images.add(new CustomImage(_previewUrls.get(i),""));
                    }else {
                        images.add(new CustomImage(_previewUrls.get(i), _captions.get(i)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return images;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
