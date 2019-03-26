package tl.alex.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date timestamp;

    private String userMessage;
    private String internalMessage;
    private String code;

    private ApiError() {
        timestamp = new Date();
    }

    public ApiError(Throwable ex) {
        this();
        this.userMessage = "Unexpected error";
        this.internalMessage = ex.getLocalizedMessage();
    }

    public ApiError(String message, Throwable ex) {
        this();

        this.userMessage = message;
        this.internalMessage = ex.getLocalizedMessage();
    }

    public ApiError(String defaultMessage, String code) {
        this();

        this.userMessage = defaultMessage;
        this.code = code;
    }

    public void setMessage(String message) {
        this.userMessage = message;
    }

    public String getDebugMessage() {
        return internalMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.internalMessage = debugMessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
