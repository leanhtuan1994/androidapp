package ttuananhle.android.chatlearningapp.viewholder;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.adapter.RecyclerSetingAdapter;
import ttuananhle.android.chatlearningapp.model.Setting;

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

    public void setImgItem(Bitmap bitmap){
        if ( bitmap != null){
            this.imgItem.setImageBitmap(bitmap);
        }
    }
    public void setTxtItem(String text){
        this.txtItem.setText(text);
    }

    public void bind(final Setting setting, final RecyclerSetingAdapter.OnItemClickListener listener){
        this.setImgItem(setting.getImageUrl());
        this.setTxtItem(setting.getName());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(setting);
            }
        });
    }
}
