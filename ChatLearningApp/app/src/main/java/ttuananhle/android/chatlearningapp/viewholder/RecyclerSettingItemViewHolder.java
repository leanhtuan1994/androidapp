package ttuananhle.android.chatlearningapp.viewholder;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ttuananhle.android.chatlearningapp.R;

/**
 * Created by leanh on 5/7/2017.
 */

public class RecyclerSettingItemViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgItem;
    private TextView txtItem;

    public RecyclerSettingItemViewHolder(final View parent, ImageView imgItem, TextView txtItem){
        super(parent);

        this.imgItem = imgItem;
        this.txtItem = txtItem;
    }

    public static RecyclerSettingItemViewHolder newInstance(View parent){
        ImageView img = (ImageView) parent.findViewById(R.id.img_item_setting);
        TextView txt = (TextView) parent.findViewById(R.id.txt_item_setting);

        return new RecyclerSettingItemViewHolder(parent, img, txt);
    }

    public void setImgItem(String imgUrl){

    }

    public void setTxtItem(TextView textView){
        this.txtItem = textView;
    }
}
