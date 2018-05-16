package idv.haojun.helpers;


import android.os.Handler;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MarqueeHelper {
    public static final long UPDATE_TIME = 2000L;
    private int indexOfCurrent;
    private Handler handler;
    private List<Integer> imageResources;
    private ImageView imageView;
    private boolean running;

    public MarqueeHelper(List<Integer> imageResources, ImageView imageView) {
        this.indexOfCurrent = 0;
        this.handler = new Handler();
        this.imageResources = imageResources;
        this.imageView = imageView;
        this.running = false;
    }

    public void start() {
        if (!running && !imageResources.isEmpty()) {
            running = true;
            handler.post(runnable);
        }
    }

    public void stop() {
        running = false;
        handler.removeCallbacks(runnable);
    }

    private int getNextImageResource() {
        int id = imageResources.get(indexOfCurrent);
        indexOfCurrent = indexOfCurrent + 1 >= imageResources.size() ? 0 : indexOfCurrent + 1;
        return id;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (running) {
                Picasso.with(imageView.getContext())
                        .load(getNextImageResource())
                        .into(imageView);
                handler.postDelayed(runnable, UPDATE_TIME);
            }
        }
    };
}
