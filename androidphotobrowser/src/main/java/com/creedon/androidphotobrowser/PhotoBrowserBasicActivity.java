package com.creedon.androidphotobrowser;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.creedon.androidphotobrowser.common.data.models.CustomImage;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by troy379 on 06.03.17.
 */
public abstract class PhotoBrowserBasicActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {

    protected List<String> posters;
    protected List<String> descriptions;
    protected CoordinatorLayout coordinatorLayout;

    public interface PhotoBrowserListener{
        List<String> photoBrowserPhotos(PhotoBrowserBasicActivity activity );
        List<String> photoBrowserVideos(PhotoBrowserBasicActivity activity );
        List<String> photoBrowserThumbnails(PhotoBrowserBasicActivity activity );
        String photoBrowserPhotoAtIndex(PhotoBrowserBasicActivity activity , int index);
        List<String> photoBrowserPhotoCaptions(PhotoBrowserBasicActivity photoBrowserBasicActivity);
        String getActionBarTitle();
        String getSubtitle();
        List<CustomImage> getCustomImages(PhotoBrowserActivity photoBrowserActivity);
    }
    public PhotoBrowserListener listener;
    private static final String TAG = PhotoBrowserBasicActivity.class.getSimpleName();
    protected RecyclerView recyclerView;
    protected GridLayoutManager lLayout;


    public RecyclerViewAdapter getRcAdapter() {
        return rcAdapter;
    }

    protected RecyclerViewAdapter rcAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photobrowser);


    }



    protected void showPicker(int startPosition) {

    }

    protected void init() {

        lLayout = new GridLayoutManager(PhotoBrowserBasicActivity.this, 3);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lLayout);
        //TODO add listener
        List<String> thumbnails = listener.photoBrowserThumbnails(this);
        posters = listener.photoBrowserPhotos(this);
        descriptions = listener.photoBrowserPhotoCaptions(this);

        List<String> videoUrls = listener.photoBrowserVideos(this);
        List<String> mediaUrls = new ArrayList<>();

        if (posters != null) {
            for (int i = 0; i < posters.size(); i++) {
                if (!videoUrls.get(i).isEmpty()) {
                    mediaUrls.add(videoUrls.get(i));
                } else {
                    mediaUrls.add(posters.get(i));
                }
            }
        }

        rcAdapter = new RecyclerViewAdapter(this, thumbnails, mediaUrls);
        recyclerView.setAdapter(rcAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(PhotoBrowserBasicActivity.this, recyclerView, this));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 1, true, 0));


    }

    @Override
    public void onItemClick(View view, int position) {
        showPicker(position);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        // ...
    }


    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;
        private int headerNum;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge, int headerNum) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
            this.headerNum = headerNum;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view) - headerNum; // item position

            if (position >= 0) {
                int column = position % spanCount; // item column

                if (includeEdge) {
                    outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                    if (position < spanCount) { // top edge
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing; // item bottom
                } else {
                    outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                    outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                    if (position >= spanCount) {
                        outRect.top = spacing; // item top
                    }
                }
            } else {
                outRect.left = 0;
                outRect.right = 0;
                outRect.top = 0;
                outRect.bottom = 0;
            }
        }
    }

}
