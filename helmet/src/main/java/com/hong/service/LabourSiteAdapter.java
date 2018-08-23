package com.hong.service;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.hong.activity.LabourSiteActivity;
import com.hong.activity.MapViewActivity;
import com.hong.activity.NewSiteMapActivity;
import com.hong.activity.SiteFindWorkActivity;
import com.hong.helmet.R;
import com.hong.pojo.LabourSite;
import com.hong.util.ToastUtils;

import java.util.List;

public class LabourSiteAdapter extends RecyclerView.Adapter<LabourSiteAdapter.ViewHolder> {

    private List<LabourSite> mLabourSiteList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView LabourSiteProjectName;
        TextView LabourSiteProjectActualNum;
        TextView LabourSiteProjectRealNum;
        TextView LabourSiteProjectDistanceNum;
        ImageView LabourSiteProjectLocationIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            LabourSiteProjectName = (TextView)itemView.findViewById(R.id.labour_site_project_name);
            LabourSiteProjectActualNum = (TextView)itemView.findViewById(R.id.labour_site_project_actual_number);
            LabourSiteProjectRealNum = (TextView)itemView.findViewById(R.id.labour_site_project_real_number);
            LabourSiteProjectDistanceNum = (TextView)itemView.findViewById(R.id.labour_site_project_distance_number);
            LabourSiteProjectLocationIcon = (ImageView)itemView.findViewById(R.id.labour_site_project_location_icon);

        }
    }

    public LabourSiteAdapter(List<LabourSite> mLabourSiteList) {
        this.mLabourSiteList = mLabourSiteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.labour_site,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.LabourSiteProjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), SiteFindWorkActivity.class));
            }
        });
        viewHolder.LabourSiteProjectLocationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), MapViewActivity.class));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        LabourSite labourSite = mLabourSiteList.get(position);
        viewHolder.LabourSiteProjectName.setText(labourSite.getProjectName());
        viewHolder.LabourSiteProjectActualNum.setText(labourSite.getActualNum());
        viewHolder.LabourSiteProjectRealNum.setText(labourSite.getRealNum());
        viewHolder.LabourSiteProjectDistanceNum.setText(labourSite.getDistanceNum());
    }

    @Override
    public int getItemCount() {
        return mLabourSiteList.size();
    }


}
