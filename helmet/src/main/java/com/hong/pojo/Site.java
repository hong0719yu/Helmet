package com.hong.pojo;

public class Site {
    private Integer imageId; //大图片
    private Integer imageLocationId; //位置图标
    private String siteCode; //工地代码
    private String location; //所处位置
   //构造方法，在后面会用到，会用构造方法里给属性赋值
    public Site(Integer imageId, Integer imageLocationId, String siteCode, String location) {
        this.imageId = imageId;
        this.imageLocationId = imageLocationId;
        this.siteCode = siteCode;
        this.location = location;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public String getLocation() {
        return location;
    }

    public Integer getImageId() {
        return imageId;
    }

    public Integer getImageLocationId() {
        return imageLocationId;
    }

}
