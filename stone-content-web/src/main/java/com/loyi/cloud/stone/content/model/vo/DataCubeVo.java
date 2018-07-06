package com.loyi.cloud.stone.content.model.vo;

/**
 * Created by fq on 2018/6/26.
 */
public class DataCubeVo {
    private Integer newUserCount;
    private Integer summerUserCount;

    public Integer getNewUserCount() {
        return newUserCount;
    }

    public void setNewUserCount(Integer newUserCount) {
        this.newUserCount = newUserCount;
    }

    public Integer getSummerUserCount() {
        return summerUserCount;
    }

    public void setSummerUserCount(Integer summerUserCount) {
        this.summerUserCount = summerUserCount;
    }

    @Override
    public String toString() {
        return "DataCubeVo{" +
                "newUserCount=" + newUserCount +
                ", summerUserCount=" + summerUserCount +
                '}';
    }
}
