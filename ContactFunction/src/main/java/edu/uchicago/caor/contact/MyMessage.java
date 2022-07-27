package edu.uchicago.caor.contact;

import com.amazonaws.services.simpleemail.model.Message;

public class MyMessage {

    private String emailFrom;
    private String subject;
    private String body;

    public MyMessage() {
    }

    public MyMessage(String emailFrom, String subject, String body) {
        this.emailFrom = emailFrom;
        this.subject = subject;
        this.body = body;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "emailFrom='" + emailFrom + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
