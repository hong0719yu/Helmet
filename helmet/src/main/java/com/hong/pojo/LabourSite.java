package com.hong.pojo;

public class LabourSite {

    private String ProjectName; // 项目名称
    private String actualNum; // 实际人数
    private String realNum; // 总共人数
    private String distanceNum; // 距离数

    public LabourSite(String projectName, String actualNum, String realNum, String distanceNum) {
        ProjectName = projectName;
        this.actualNum = actualNum;
        this.realNum = realNum;
        this.distanceNum = distanceNum;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setActualNum(String actualNum) {
        this.actualNum = actualNum;
    }

    public String getActualNum() {
        return actualNum;
    }

    public void setRealNum(String realNum) {
        this.realNum = realNum;
    }

    public String getRealNum() {
        return realNum;
    }

    public void setDistanceNum(String distanceNum) {
        this.distanceNum = distanceNum;
    }

    public String getDistanceNum() {
        return distanceNum;
    }

}
