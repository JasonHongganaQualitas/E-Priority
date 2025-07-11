package id.co.qualitas.epriority.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WSMessage implements Serializable {
    @SerializedName("idMessage")
    int idMessage;
    @SerializedName("message")
    String message;
    @SerializedName("result")
    Object result;

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}