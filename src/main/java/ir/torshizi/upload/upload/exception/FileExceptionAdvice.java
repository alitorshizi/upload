package ir.torshizi.upload.upload.exception;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


@ControllerAdvice
public class FileExceptionAdvice {

    private Throwable cause;

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<Violation> handleFileNotFoundException(FileNotFoundException exception) {
        List<Violation> violations = new ArrayList<>();
        violations.add(new Violation(exception.getFieldName(), exception.getMessage()));
        return violations;
    }

    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public List<Violation> handleMaxSizeException(FileStorageException exception) {
        List<Violation> violations = new ArrayList<>();
        violations.add(new Violation(exception.getFieldName(), exception.getMessage()));
        return violations;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public List<Violation> handleMaxSizeException(MaxUploadSizeExceededException exception) {
        List<Violation> violations = new ArrayList<>();
        violations.add(new Violation(((FileSizeLimitExceededException) exception.getCause().getCause()).getFieldName(), exception.getMessage()));
        return violations;
    }
}
