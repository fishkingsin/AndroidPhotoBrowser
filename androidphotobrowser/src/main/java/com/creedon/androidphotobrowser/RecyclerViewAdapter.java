package com.creedon.androidphotobrowser;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
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
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    public interface RecyclerViewAdapterListener {
        boolean isPhotoSelected(int position);

        boolean isPhotoSelectionMode();
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
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {

        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {

            @Override
            public void onSubmit(String id, Object callerContext) {
                super.onSubmit(id, callerContext);
            }

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                if (imageInfo == null) {
                    return;
                }
                QualityInfo qualityInfo = imageInfo.getQualityInfo();
                Log.d(TAG, "Final image received! " +
                        String.format("Size %d x %d ", imageInfo.getWidth(),
                                imageInfo.getHeight()) +
                        String.format("Quality level %d, good enough: %s, full quality: %s", qualityInfo.getQuality(),
                                qualityInfo.isOfGoodEnoughQuality(),
                                qualityInfo.isOfFullQuality())

                );
                super.onFinalImageSet(id, imageInfo, animatable);
            }

            @Override
            public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                super.onIntermediateImageSet(id, imageInfo);
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                super.onIntermediateImageFailed(id, throwable);
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
            }

            @Override
            public void onRelease(String id) {
                super.onRelease(id);
            }
        };

        ImageRequest imageRequest = ImageRequestBuilder

                .newBuilderWithSource(Uri.parse(itemList.get(position)))
//                .setMediaVariationsForMediaId("partial")
//                .setBytesRange(BytesRange.toMax(30000))
                .setRequestListener(new RequestListener() {
                    @Override
                    public void onRequestStart(ImageRequest request, Object callerContext, String requestId, boolean isPrefetch) {

                    }

                    @Override
                    public void onRequestSuccess(ImageRequest request, String requestId, boolean isPrefetch) {

                    }

                    @Override
                    public void onRequestFailure(ImageRequest request, String requestId, Throwable throwable, boolean isPrefetch) {

                    }

                    @Override
                    public void onRequestCancellation(String requestId) {

                    }

                    @Override
                    public void onProducerStart(String requestId, String producerName) {

                    }

                    @Override
                    public void onProducerEvent(String requestId, String producerName, String eventName) {

                    }

                    @Override
                    public void onProducerFinishWithSuccess(String requestId, String producerName, Map<String, String> extraMap) {
                        Log.d("success", "requestId " + requestId + " producerName " + producerName + " extraMap " + extraMap);
                    }

                    @Override
                    public void onProducerFinishWithFailure(String requestId, String producerName, Throwable t, Map<String, String> extraMap) {

                    }

                    @Override
                    public void onProducerFinishWithCancellation(String requestId, String producerName, Map<String, String> extraMap) {

                    }

                    @Override
                    public void onUltimateProducerReached(String requestId, String producerName, boolean successful) {

                    }

                    @Override
                    public boolean requiresExtraMap(String requestId) {
                        return false;
                    }
                })
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setTapToRetryEnabled(true)
                .setOldController(holder.simpleDraweeView.getController())
                .setControllerListener(controllerListener)
                .setImageRequest(imageRequest)
                .build();

        final ProgressBarDrawable progressBarDrawable = new ProgressBarDrawable();
        progressBarDrawable.setColor(context.getResources().getColor(R.color.colorAccent));
        progressBarDrawable.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        progressBarDrawable
                .setRadius(5);
        final Drawable failureDrawable = context.getResources().getDrawable(R.drawable.missing);
        DrawableCompat.setTint(failureDrawable, Color.RED);
        final Drawable placeholderDrawable = context.getResources().getDrawable(R.drawable.loading);
        holder.simpleDraweeView.getHierarchy().setPlaceholderImage(placeholderDrawable, ScalingUtils.ScaleType.CENTER_INSIDE);
        holder.simpleDraweeView.getHierarchy().setFailureImage(failureDrawable, ScalingUtils.ScaleType.CENTER_INSIDE);
        holder.simpleDraweeView.getHierarchy().setProgressBarImage(progressBarDrawable, ScalingUtils.ScaleType.CENTER_INSIDE);

        holder.simpleDraweeView.setController(controller);
//        holder.simpleDraweeView.setImageURI(itemList.get(position));
        if (listener != null) {
            boolean selection = listener.isPhotoSelected(position);
            boolean selectionMode = listener.isPhotoSelectionMode();
            CheckBox checkBox = holder.checkBox;
            checkBox.setVisibility(selectionMode ? View.VISIBLE : View.INVISIBLE);
            checkBox.setChecked(selectionMode && selection);
        }

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
