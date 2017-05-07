package ttuananhle.android.chatlearningapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URI;
import java.util.List;

import ttuananhle.android.chatlearningapp.R;
import ttuananhle.android.chatlearningapp.model.Setting;
import ttuananhle.android.chatlearningapp.model.User;
import ttuananhle.android.chatlearningapp.viewholder.RecyclerContactItemViewHolder;
import ttuananhle.android.chatlearningapp.viewholder.RecyclerSettingItemViewHolder;

/**
 * Created by leanh on 5/7/2017.
 */

public class RecyclerSetingAdapter extends RecyclerView.Adapter<RecyclerSettingItemViewHolder>{
    private List<Setting> settingList;
    private Context context;
    private LayoutInflater layoutInflater;

    public RecyclerSetingAdapter(Context context, List<Setting> list){
        this.context = context;
        this.settingList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerSettingItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.recycler_item_settings, parent, false);

        return RecyclerSettingItemViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerSettingItemViewHolder holder, int position) {
        Setting setting = settingList.get(position);


    }

    @Override
    public int getItemCount() {
        return settingList == null ? 0 : settingList.size();
    }
}
