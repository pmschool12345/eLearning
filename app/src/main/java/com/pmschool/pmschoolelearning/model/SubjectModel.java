package com.pmschool.pmschoolelearning.model;

public class SubjectModel {
    private String subject;
    private String class_name;
    private String section;

    public SubjectModel(String subject, String class_name, String section) {
        this.subject = subject;
        this.class_name = class_name;
        this.section = section;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
