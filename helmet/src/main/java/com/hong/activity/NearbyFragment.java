package com.hong.activity;

import android.view.View;

import com.baidu.mapapi.map.MapView;
import com.hong.helmet.R;
import com.hong.util.BasePageTitleFragment;

/**
 * 附近
 */

public class NearbyFragment extends BasePageTitleFragment {

    @Override
    public View initView() {
        setTitleIcon("附近",true);
        View nearbyFragment = View.inflate(getContext(), R.layout.activity_nearby, null);
        //mMapviewNearby = (MapView)nearbyFragment.findViewById(R.id.map_view_nearby);
        return nearbyFragment;
    }

    @Override
    public void initData() {

    }
}
