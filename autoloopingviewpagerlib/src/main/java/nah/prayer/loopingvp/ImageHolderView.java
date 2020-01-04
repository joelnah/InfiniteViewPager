package nah.prayer.loopingvp;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import nah.prayer.loopingvp.holder.Holder;


/**
 * 필요한 경우 별도로 커스텀해서 사용.
 */
public class ImageHolderView implements Holder<String> {
    private ImageView imageView;

    private ImageView.ScaleType type = ImageView.ScaleType.CENTER_CROP;

    public ImageHolderView(){}
    public ImageHolderView(ImageView.ScaleType type){
        this.type = type;
    }

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        RequestOptions options = new RequestOptions()
                //.format(DecodeFormat.PREFER_ARGB_8888)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .timeout(5 * 1000)
                .fitCenter();

        imageView.setScaleType(type);

        Glide.with(context)
                .load(data)
                .apply(options)
                .into(imageView);
    }

}
