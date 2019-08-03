package com.example.yq.pojo;


import java.io.Serializable;

public class PersonCompany implements Serializable {
    private String companyId,userName,personId,companyToPerson,success,message, resumeName,recruitId;
    private String times;

    public PersonCompany() {
    }

    public PersonCompany(String companyId, String personId, String companyToPerson, String message, String resume_name, String recruitId) {
        this.companyId = companyId;
        this.personId = personId;
        this.companyToPerson = companyToPerson;
        this.message = message;
        this.resumeName = resume_name;
        this.recruitId = recruitId;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getCompanyToPerson() {
        return companyToPerson;
    }

    public void setCompanyToPerson(String companyToPerson) {
        this.companyToPerson = companyToPerson;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResumeName() {
        return resumeName;
    }

    public void setResumeName(String resumeName) {
        this.resumeName = resumeName;
    }

    public String getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(String recruitId) {
        this.recruitId = recruitId;
    }
}
