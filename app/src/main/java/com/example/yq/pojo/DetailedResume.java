package com.example.yq.pojo;

import java.io.Serializable;
import java.util.List;

public class DetailedResume implements Serializable {
    private  Resume resumeBean;
    private List<Experience> experienceList;
    private List<Education> educationList;
    private JobIntent jobIntentBean;
    private List<ProjectExperience> projectExprienceList;
    private List<EduJobCondition> eduJobConditionList;

    public Resume getResumeBean() {
        return resumeBean;
    }

    public void setResumeBean(Resume resumeBean) {
        this.resumeBean = resumeBean;
    }

    public List<Experience> getExperienceList() {
        return experienceList;
    }

    public void setExperienceList(List<Experience> experienceList) {
        this.experienceList = experienceList;
    }

    public List<Education> getEducationList() {
        return educationList;
    }

    public void setEducationList(List<Education> educationList) {
        this.educationList = educationList;
    }

    public JobIntent getJobIntentBean() {
        return jobIntentBean;
    }

    public void setJobIntentBean(JobIntent jobIntentBean) {
        this.jobIntentBean = jobIntentBean;
    }

    public List<ProjectExperience> getProjectExprienceList() {
        return projectExprienceList;
    }

    public void setProjectExprienceList(List<ProjectExperience> projectExprienceList) {
        this.projectExprienceList = projectExprienceList;
    }

    public List<EduJobCondition> getEduJobConditionList() {
        return eduJobConditionList;
    }

    public void setEduJobConditionList(List<EduJobCondition> eduJobConditionList) {
        this.eduJobConditionList = eduJobConditionList;
    }
}
