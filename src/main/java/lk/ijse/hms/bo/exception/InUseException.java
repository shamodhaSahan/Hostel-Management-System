package lk.ijse.hms.bo.exception;

/**
 * Created By shamodha_s_rathnamalala
 * Date : 4/6/2023
 * Time :1:52 PM
 */

public class InUseException extends RuntimeException{
    public InUseException() {
    }

    public InUseException(String message) {
        super(message);
    }

    public InUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public InUseException(Throwable cause) {
        super(cause);
    }

    public InUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
