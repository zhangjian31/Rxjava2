package com.example.zhangjian.rxjava2.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhangjian.rxjava2.R;
import com.example.zhangjian.rxjava2.bean.InterestBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangjian on 2018/6/1.
 */

public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.ViewHolder> {

    private List<InterestBean> mList = new ArrayList<>();

    public void setData(List<InterestBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(InterestBean bean) {
        if (bean == null) {
            return;
        }
        mList.add(0, bean);
        notifyItemInserted(0);
//        notifyDataSetChanged();
    }

    public List<InterestBean> getData() {
        return mList;
    }

    public InterestBean removeData(int position) {
        InterestBean bean = null;
        if (position >= 0 && position < mList.size()) {
            bean = mList.get(position);
            removeData(bean);
        }
        return bean;
    }

    public void removeData(InterestBean bean) {
        if (bean == null) {
            return;
        }
        Iterator<InterestBean> it = mList.iterator();
        int position = -1;
        while (it.hasNext()) {
            position++;
            InterestBean temp = it.next();
            if (temp != null && temp.getId() == bean.getId()) {
                it.remove();
                break;
            }
        }
        notifyItemRemoved(position);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top, null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position==0){
            holder.itemView.setVisibility(View.INVISIBLE);
            holder.itemView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.itemView.setVisibility(View.VISIBLE);
                }
            },500);
        }
        show(holder.imageView, mList.get(position).getUrl());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
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


    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.avatar);
        }
    }

    private SelectedAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(SelectedAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }


}
