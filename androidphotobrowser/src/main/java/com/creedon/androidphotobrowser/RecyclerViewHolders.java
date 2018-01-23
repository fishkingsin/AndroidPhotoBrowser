package com.creedon.androidphotobrowser;

import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final long ONE_HOUR = 36000000;
    View mask;
    SimpleDraweeView simpleDraweeView;
    CheckBox checkBox;
    ImageView videoIcon;
    TextView videoDuration;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.image);

        checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        checkBox.setVisibility(View.INVISIBLE);
        checkBox.setClickable(false);

        mask = (View) itemView.findViewById(R.id.mask);

        mask.setVisibility(View.INVISIBLE);
        videoIcon = (ImageView) itemView.findViewById(R.id.iv_videoIcon);
        videoDuration = (TextView) itemView.findViewById(R.id.tv_videoDuration);
    }

    @Override
    public void onClick(View view) {
//            Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }

    public void setVideoIconAndDuration(boolean isVisible, String url){
        videoIcon.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);

        if (isVisible) {
            new MetaDataTask(url).execute();
        } else {
            videoDuration.setVisibility(View.INVISIBLE);
        }
    }

    private String convertMillisToFormat(long millis) {
        String hms;

        if (millis >= ONE_HOUR) {
            hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        } else {
            hms = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        }

        return hms;
    }

    class MetaDataTask extends AsyncTask<Void, Integer, Long> {

        String url;

        MetaDataTask(String url) {
            this.url = url;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(url, new HashMap<String, String>());

                String mVideoDuration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                return Long.parseLong(mVideoDuration);

            } catch (Exception e) {
                e.printStackTrace();
                return -1L;
            }

        }

        @Override
        protected void onPostExecute(Long result) {
            if (result != -1L) {
                videoDuration.setText(convertMillisToFormat(result));
                videoDuration.setVisibility(View.VISIBLE);
            }
        }
    }
}