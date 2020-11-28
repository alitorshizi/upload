package ir.torshizi.upload.upload.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileNotFoundException extends RuntimeException {
    private String fieldName;
    private String message;
}
