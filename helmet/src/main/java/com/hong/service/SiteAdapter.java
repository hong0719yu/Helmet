package com.hong.service;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hong.activity.SiteLocationActivity;
import com.hong.helmet.R;
import com.hong.pojo.Site;

import java.util.List;

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.ViewHolder> {

    private List<Site> mSiteList;
    private Activity mActivity;

    /*
    * 内部类ViewHolder要继承自RecyclerView.ViewHolder。
    * 然后这个内部类ViewHolder的构造函数中要传入一个View参数，这个参数是RecyclerView子项
    * 的最外层布局，那么我们就可以通过findViewById()方法来获取到我们自定义布局中的
    * ImageView和TextView实例了；
    * 并且还要在类中定义属性，来接收在内部类的构造函数中获取到的布局中的实例。
    * */
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView siteImage,siteLocationIcon;
        TextView siteNameContent,siteLocationContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            siteImage = (ImageView)itemView.findViewById(R.id.site_pic);
            siteLocationIcon = (ImageView)itemView.findViewById(R.id.site_location_icon);
            siteNameContent = (TextView)itemView.findViewById(R.id.site_title_content);
            siteLocationContent = (TextView)itemView.findViewById(R.id.site_location_content);
        }
    }

    /*
    * 用于把要展示的数据源传进来，并赋值给一个全局变量mSiteList，之后的操作都将在这个
    * 数据源的基础上进行
    * */
    public SiteAdapter(Activity mActivity,List<Site> mSiteList) {
        this.mSiteList = mSiteList;
        this.mActivity = mActivity;
    }

    @NonNull
    /*
     * 用于创建ViewHolder实例
     * 在这个方法中将布局加载进来，然后创建一个ViewHolder实例，并将加载出来的布局传入到构造函数中，
     * 最后将ViewHolder实例返回
     * */
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.site_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.siteLocationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, SiteLocationActivity.class));
            }
        });
        return viewHolder;
    }

    /*
    * 用于对RecyclerView子项的数据进行赋值，会在每个子项回滚到屏幕内的时候执行
    * 在这里我们通过position参数得到当前项的Site实例，然后将数据设置到ViewHolder的
    * ImageView和TextView中即可
    * */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Site site = mSiteList.get(position);
        viewHolder.siteImage.setImageResource(site.getImageId());
        viewHolder.siteLocationIcon.setImageResource(site.getImageLocationId());
        viewHolder.siteNameContent.setText(site.getSiteCode());
        viewHolder.siteLocationContent.setText(site.getLocation());
    }

    /*
    * 用于告诉RecyclerView一共有多少子项，直接返回数据源长度即可
    * */
    @Override
    public int getItemCount() {
        return mSiteList.size();
    }
}
