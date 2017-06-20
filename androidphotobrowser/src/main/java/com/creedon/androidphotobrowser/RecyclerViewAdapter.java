package com.creedon.androidphotobrowser;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
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

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    public void onBindViewHolder(final RecyclerViewHolders holder, final int position) {


        prefetchHeader(itemList.get(position), new HeaderResponse() {
            @Override
            public void onErrorLoaded(String s) {
                buildImage(0);
            }

            @Override
            public void onResponseLoaded(int orientation) {
                buildImage(orientation);
            }

            private void buildImage(int orientation) {
                Uri uri = Uri.parse(itemList.get(position));
                ImageRequest imageRequest = ImageRequestBuilder
                        .newBuilderWithSource(uri)
                        .setRotationOptions(RotationOptions.forceRotation(orientation))
                        .build();
                final DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setTapToRetryEnabled(true)
                        .setOldController(holder.simpleDraweeView.getController())

                        .setImageRequest(imageRequest)
                        .build();
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
//                        holder.simpleDraweeView.setImageURI(itemList.get(position));
                        if (listener != null) {
                            boolean selection = listener.isPhotoSelected(position);
                            boolean selectionMode = listener.isPhotoSelectionMode();
                            CheckBox checkBox = holder.checkBox;
                            checkBox.setVisibility(selectionMode ? View.VISIBLE : View.INVISIBLE);
                            checkBox.setChecked(selectionMode && selection);
                        }
                    }});
            }
        });


    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }


    public void prefetchHeader(String url, final HeaderResponse serverResponse) {


        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                .head()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("response", call.request().body().toString());
                serverResponse.onErrorLoaded(call.request().body().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Headers header = response.headers();
                String headerKey = "x-amz-meta-orientation";
                int rotation = 0;
                if(header.names().contains(headerKey)) {
                    String orientationValue = response.headers().get(headerKey);
                    int orientation = Integer.valueOf(orientationValue);


                    try {
                        rotation = exifToDegrees(orientation);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
                serverResponse.onResponseLoaded(rotation);

            }


        });
    }

    private int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        } else {
            return 0;
        }
    }

    private interface HeaderResponse {
        void onErrorLoaded(String s);

        void onResponseLoaded(int rotate);
    }
}
