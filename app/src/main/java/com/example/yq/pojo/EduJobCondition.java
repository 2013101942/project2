package com.example.yq.pojo;


import java.io.Serializable;
import java.util.Date;

public class EduJobCondition implements Serializable {
    private String eduJobConditionId,userId,resumeName,conDescribe,times;

    public String getEduJobConditionId() {
        return eduJobConditionId;
    }

    public void setEduJobConditionId(String eduJobConditionId) {
        this.eduJobConditionId = eduJobConditionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResumeName() {
        return resumeName;
    }

    public void setResumeName(String resumeName) {
        this.resumeName = resumeName;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getConDescribe() {
        return conDescribe;
    }

    public void setConDescribe(String conDescribe) {
        this.conDescribe = conDescribe;
    }
}
