package dev.collegues.dto;

public class MessageErreurDto {

    private String message;

    public MessageErreurDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
