package com.example.recyclerview2.appDataBase;

public class ContactClass {
    private String contactEmail;
    private String contactFullName;
    private String contactUserName;
    private String contactStatus;

    public ContactClass(String contactEmail, String contactFullName, String contactUserName, String contactStatus) {
        this.contactEmail = contactEmail;
        this.contactFullName = contactFullName;
        this.contactUserName = contactUserName;
        this.contactStatus = contactStatus;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactFullName() {
        return contactFullName;
    }

    public void setContactFullName(String contactFullName) {
        this.contactFullName = contactFullName;
    }

    public String getContactUserName() {
        return contactUserName;
    }

    public void setContactUserName(String contactUserName) {
        this.contactUserName = contactUserName;
    }

    public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }
}
