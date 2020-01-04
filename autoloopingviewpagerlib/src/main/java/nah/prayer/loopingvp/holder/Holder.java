package nah.prayer.loopingvp.holder;

/**
 * Created by Sai on 15/12/14.
 */

import android.content.Context;
import android.view.View;

public interface Holder<T>{
    View createView(Context context);
    void UpdateUI(Context context, int position, T data);
}