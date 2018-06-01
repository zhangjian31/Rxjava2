package com.example.zhangjian.rxjava2.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import com.example.zhangjian.rxjava2.bean.InterestBean;
import com.example.zhangjian.rxjava2.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangjian on 2018/5/31.
 */

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.ViewHolder> {

    private List<InterestBean> data = new ArrayList<>();
    private boolean isNeedShowAnimal = false;
    private Map<Integer, InterestBean> selectMap = new HashMap<>();

    public void setData(List<InterestBean> data, Map<Integer, InterestBean> selectMap) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.clear();
        this.data.addAll(data);
        this.selectMap = selectMap;
        isNeedShowAnimal = true;
        notifyDataSetChanged();
    }

    public boolean isNeedShowAnimal() {
        return isNeedShowAnimal;
    }

    public void setNeedShowAnimal(boolean needShowAnimal) {
        isNeedShowAnimal = needShowAnimal;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interest, null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        InterestBean bean = data.get(position);
        if (selectMap.containsKey(bean.getId())) {
            holder.selectView.setBackgroundResource(R.drawable.bg_circle_ffe24b);
        } else {
            holder.selectView.setBackgroundResource(R.drawable.bg_circle_ffffff);
        }
        holder.nickname.setText(bean.getTitle());
        show(holder.avatar, bean.getUrl());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            }
        });
        if (isNeedShowAnimal) {
            Animation animation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(100);
            animation.setStartOffset(100 * position);
            holder.itemView.startAnimation(animation);
        }
    }

    public List<InterestBean> getData() {
        return data;
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView avatar;
        View selectView;
        TextView nickname;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = (SimpleDraweeView) itemView.findViewById(R.id.avatar);
            selectView = itemView.findViewById(R.id.select_view);
            nickname = itemView.findViewById(R.id.nickname);
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

   public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    private void show(DraweeView targetView, String url) {
        Uri uri = Uri.parse(url);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(300, 300))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(targetView.getController())
                .setImageRequest(request)
                .build();
        targetView.setController(controller);
    }

}
