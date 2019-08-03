package com.example.yq.pojo;


import java.io.Serializable;


public class Education implements Serializable {
    private String eduId, userId, resumeName,school, major,yearsStart, yearsEnd;


    public String getEduId() {
        return eduId;
    }

    public void setEduId(String eduId) {
        this.eduId = eduId;
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

    public String getYearsStart() {
        return yearsStart;
    }

    public void setYearsStart(String yearsStart) {
        this.yearsStart = yearsStart;
    }

    public String getYearsEnd() {
        return yearsEnd;
    }

    public void setYearsEnd(String yearsEnd) {
        this.yearsEnd = yearsEnd;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
