package com.example.yq.pojo;


import java.io.Serializable;

public class Experience implements Serializable {
    private String expId,userId,resumeName,companyName,job,jobDescribe,yearsStart,yearsEnd ;


    public String getExpId() {
        return expId;
    }

    public void setExpId(String expId) {
        this.expId = expId;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJobDescribe() {
        return jobDescribe;
    }

    public void setJobDescribe(String jobDescribe) {
        this.jobDescribe = jobDescribe;
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
}
