package id.co.qualitas.epriority.model;

public class Notification {
    public String title;
    public String body;
    public String description;
    public String timestamp;
    public boolean isUnread;

    public Notification(String title, String description, String timestamp, boolean isUnread) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.isUnread = isUnread;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isUnread() {
        return isUnread;
    }

    public void setUnread(boolean unread) {
        isUnread = unread;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
