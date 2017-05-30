package myapp.abrahamjohngomez.com.displaycase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * A class to handle transformations to scale and rotate images appropriately
 *
 * Created by ryuhyoko on 5/30/2017.
 */

public class ImageTransformations extends BitmapTransformation {

    public ImageTransformations(Context context) {
        super(context);
    }
    @Override
    public Bitmap transform(BitmapPool pool, Bitmap original, int outWidth, int outHeight) {
        Bitmap transformed = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        if(transformed == null) {
            transformed = Bitmap.createBitmap(outWidth,outHeight, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(transformed);
        Paint paint = new Paint();
        paint.setAlpha(128);
        canvas.drawBitmap(original, 0, 0, paint);

        return transformed;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
