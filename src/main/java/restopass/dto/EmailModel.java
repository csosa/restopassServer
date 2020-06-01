package restopass.dto;

import java.util.HashMap;

public class EmailModel {

    private String emailTo;
    private String subject;
    private HashMap<String, Object> model;
    private String mailTempate;
    private String mailContent;

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public String getMailTempate() {
        return mailTempate;
    }

    public void setMailTempate(String mailTempate) {
        this.mailTempate = mailTempate;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public HashMap<String, Object> getModel() {
        return model;
    }

    public void setModel(HashMap<String, Object> model) {
        this.model = model;
    }
}
