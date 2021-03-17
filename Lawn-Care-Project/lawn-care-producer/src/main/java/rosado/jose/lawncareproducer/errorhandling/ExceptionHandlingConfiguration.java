package rosado.jose.lawncareproducer.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingConfiguration extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotAContractorException.class)
    protected ResponseEntity<ResponseBody> handleUserNotAContractorException(
            UserNotAContractorException ex,
            WebRequest request) {

        ResponseBody responseBody = new ResponseBody(
                "USER_NOT_A_CONTRACTOR",
                "The user is not listed as a contractor"
        );

        ResponseEntity<ResponseBody> response = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        return response;
    }

    @ExceptionHandler(RequestAlreadyClaimedException.class)
    protected ResponseEntity<ResponseBody> handleRequestAlreadyClaimedException(
            RequestAlreadyClaimedException ex,
            WebRequest request) {

        ResponseBody responseBody = new ResponseBody(
                "REQUEST_ALREADY_CLAIMED",
                "The request has already been claimed by another contractor"
        );

        ResponseEntity<ResponseBody> response = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        return response;
    }

    @ExceptionHandler(RequestNotAvailableException.class)
    protected ResponseEntity<ResponseBody> handleRequestNotAvailableException(
            RequestNotAvailableException ex,
            WebRequest request) {

        ResponseBody responseBody = new ResponseBody(
                "REQUEST_NOT_AVAILABLE",
                "The request is not available for this action"
        );

        ResponseEntity<ResponseBody> response = new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        return response;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ResponseBody {
        private String code;
        private String message;
    }

}
