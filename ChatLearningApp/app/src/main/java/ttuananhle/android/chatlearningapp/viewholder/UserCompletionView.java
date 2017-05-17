package ttuananhle.android.chatlearningapp.viewholder;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokenautocomplete.TokenCompleteTextView;

import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.model.User;

/**
 * Created by leanh on 5/16/2017.
 */

public class UserCompletionView extends TokenCompleteTextView<User> {

    public UserCompletionView(Context context) {
        super(context);
    }

    public UserCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(User user) {
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TokenTextView token = (TokenTextView) l.inflate(R.layout.user_tonken, (ViewGroup) getParent(), false);
        token.setText(user.getEmail());
        return token;
    }

    @Override
    protected User defaultObject(String completionText) {
        int index = completionText.indexOf('@');
        if (index == -1) {
            return new User(completionText, completionText.replace(" ", "") + "@example.com");
        } else {
            return new User(completionText.substring(0, index), completionText);
        }
    }
}
