package ttuananhle.android.tokenautocomplete;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by leanh on 5/9/2017.
 */

public class TokenTextView extends TextView {
   public TokenTextView (Context context){
       super(context);
   }

    public TokenTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setCompoundDrawablesWithIntrinsicBounds(0, 0, selected ? R.drawable.ic_clear_black_24px : 0, 0);
    }
}
