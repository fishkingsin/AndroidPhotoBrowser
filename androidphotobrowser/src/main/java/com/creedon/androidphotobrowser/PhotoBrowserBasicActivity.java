package com.creedon.androidphotobrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.creedon.androidphotobrowser.common.data.models.CustomImage;

import java.util.List;

/*
 * Created by troy379 on 06.03.17.
 */
public abstract class PhotoBrowserBasicActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {

    protected List<String> posters;
    protected List<String> descriptions;

    public interface PhotoBrowserListener{
        List<String> photoBrowserPhotos(PhotoBrowserBasicActivity activity );
        List<String> photoBrowserThumbnails(PhotoBrowserBasicActivity activity );
        String photoBrowserPhotoAtIndex(PhotoBrowserBasicActivity activity , int index);

        List<String> photoBrowserPhotoCaptions(PhotoBrowserBasicActivity photoBrowserBasicActivity);

        String getActionBarTitle();

        String getSubtitle();

        List<CustomImage> getCustomImages(PhotoBrowserActivity photoBrowserActivity);
    }
    public PhotoBrowserListener listener;
    private static final String TAG = PhotoBrowserBasicActivity.class.getSimpleName();
    RecyclerView recyclerView;
    GridLayoutManager lLayout;




    protected RecyclerViewAdapter rcAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);


    }



    protected void showPicker(int startPosition) {

    }

    protected void init() {

        lLayout = new GridLayoutManager(PhotoBrowserBasicActivity.this, 3);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lLayout);
        //TODO add listener
        List<String> thumbnails = listener.photoBrowserThumbnails(this);
        posters = listener.photoBrowserPhotos(this);
        descriptions = listener.photoBrowserPhotoCaptions(this);


        rcAdapter = new RecyclerViewAdapter(this, thumbnails);
        recyclerView.setAdapter(rcAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(PhotoBrowserBasicActivity.this, recyclerView, this));

    }

    @Override
    public void onItemClick(View view, int position) {
        showPicker(position);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        // ...
    }


}
