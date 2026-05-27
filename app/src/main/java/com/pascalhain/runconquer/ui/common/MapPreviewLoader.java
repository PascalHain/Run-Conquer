package com.pascalhain.runconquer.ui.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;

import com.pascalhain.runconquer.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class MapPreviewLoader {

    private static final int CACHE_SIZE_KB = 4 * 1024;
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 360;
    private static final int DEFAULT_ZOOM = 15;
    private static final int DEFAULT_SCALE = 2;

    private static final LruCache<String, Bitmap> CACHE = new LruCache<>(CACHE_SIZE_KB) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value == null ? 0 : value.getByteCount() / 1024;
        }
    };
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2);
    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    private MapPreviewLoader() {
    }

    public static void load(ImageView target, double latitude, double longitude) {
        if (target == null) {
            return;
        }
        if (latitude == 0.0 && longitude == 0.0) {
            target.setImageResource(R.drawable.bg_map_area);
            return;
        }
        String url = buildUrl(latitude, longitude);
        Bitmap cached = CACHE.get(url);
        if (cached != null) {
            target.setImageBitmap(cached);
            return;
        }
        target.setImageResource(R.drawable.bg_map_area);
        EXECUTOR.execute(() -> {
            Bitmap bitmap = download(url);
            if (bitmap == null) {
                return;
            }
            CACHE.put(url, bitmap);
            MAIN.post(() -> target.setImageBitmap(bitmap));
        });
    }

    private static String buildUrl(double lat, double lng) {
        return String.format(Locale.US,
                "https://staticmap.openstreetmap.de/staticmap.php?center=%.6f,%.6f" +
                        "&zoom=%d&size=%dx%d&scale=%d&markers=%.6f,%.6f,red-pushpin",
                lat, lng, DEFAULT_ZOOM, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_SCALE, lat, lng);
    }

    private static Bitmap download(String url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(4000);
            connection.setReadTimeout(4000);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("User-Agent", "RunConquer/1.0");
            int code = connection.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                return null;
            }
            try (InputStream input = connection.getInputStream()) {
                return BitmapFactory.decodeStream(input);
            }
        } catch (Exception ignored) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
