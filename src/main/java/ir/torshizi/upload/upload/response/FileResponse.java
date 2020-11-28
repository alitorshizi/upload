package ir.torshizi.upload.upload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileResponse {
    private String originalFileName;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String fileUrl;
    private String message;
}