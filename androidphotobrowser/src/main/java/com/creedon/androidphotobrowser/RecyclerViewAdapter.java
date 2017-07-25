package com.creedon.androidphotobrowser;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    public interface RecyclerViewAdapterListener {
        boolean isPhotoSelected(int position);

        boolean isPhotoSelectionMode();

        int getOrientation(int position);
    }

    RecyclerViewAdapterListener listener;
    private List<String> itemList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<String> itemList) {
        this.itemList = itemList;
        this.context = context;
        try {
            listener = (RecyclerViewAdapterListener) context;
        } catch (Exception e) {
            Log.e(TAG, "RecyclerViewAdapterListener not found");
        }
    }

    //https://stackoverflow.com/questions/30053610/best-way-to-update-data-with-a-recyclerview-adapter
    public void swap(List<String> datas) {

        if (itemList != null) {
            itemList.clear();
            itemList.addAll(datas);
        } else {
            itemList = datas;
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolders holder, final int position) {
        int orientation = 0;
        if (listener != null) {
            orientation = listener.getOrientation(position);
        }
        Uri uri = Uri.parse(itemList.get(position));
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setRotationOptions(RotationOptions.forceRotation(orientation))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setTapToRetryEnabled(true)
                .setOldController(holder.simpleDraweeView.getController())

                .setImageRequest(imageRequest)
                .build();
        ProgressBarDrawable progressBarDrawable = new ProgressBarDrawable();
        progressBarDrawable.setColor(context.getResources().

                getColor(R.color.colorAccent));
        progressBarDrawable.setBackgroundColor(context.getResources().

                getColor(R.color.colorPrimaryDark));
        progressBarDrawable
                .setRadius(5);
        final Drawable failureDrawable = context.getResources().getDrawable(R.drawable.missing);
        DrawableCompat.setTint(failureDrawable, Color.RED);
        final Drawable placeholderDrawable = context.getResources().getDrawable(R.drawable.loading);
        holder.simpleDraweeView.getHierarchy().

                setPlaceholderImage(placeholderDrawable, ScalingUtils.ScaleType.FIT_CENTER);
        holder.simpleDraweeView.getHierarchy().

                setFailureImage(failureDrawable, ScalingUtils.ScaleType.FIT_CENTER);
        holder.simpleDraweeView.getHierarchy().

                setProgressBarImage(progressBarDrawable, ScalingUtils.ScaleType.FIT_CENTER);

        holder.simpleDraweeView.setController(controller);
//                        holder.simpleDraweeView.setImageURI(itemList.get(position));
        if (listener != null)

        {
            boolean selection = listener.isPhotoSelected(position);
            boolean selectionMode = listener.isPhotoSelectionMode();
            CheckBox checkBox = holder.checkBox;
            checkBox.setVisibility(selectionMode ? View.VISIBLE : View.INVISIBLE);
            checkBox.setChecked(selectionMode && selection);

            View mask = holder.mask;
            mask.setVisibility((selectionMode && selection) ? View.VISIBLE : View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        if(this.itemList != null) {
            return this.itemList.size();
        }else{
            return 0;
        }
    }


}
