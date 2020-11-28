package ir.torshizi.upload.upload.controller;

import ir.torshizi.upload.upload.response.FileResponse;
import ir.torshizi.upload.upload.service.FileSystemStorageService;
import org.hibernate.id.GUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileSystemStorageService fileSystemStorageService;

    @PostMapping("/uploadFile")
    public ResponseEntity<FileResponse> uploadSingleFile(@RequestParam("file") MultipartFile file, @RequestParam("userId") Integer UserId) {
        String uploadedFileName = fileSystemStorageService.saveFile("file", file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/download/")
                .path(uploadedFileName)
                .toUriString();

        return ResponseEntity.status(HttpStatus.OK).body(new FileResponse(file.getOriginalFilename(), uploadedFileName, file.getContentType(), file.getSize(), fileDownloadUri, "File uploaded with success!"));
    }


    @PostMapping("/uploadFiles")
    public ResponseEntity<List<FileResponse>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        List<FileResponse> responses = Arrays.
                asList(files).
                stream().
                map(
                        file -> {
                            String upfile = fileSystemStorageService.saveFile("file", file);
                            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                    .path("/api/download/")
                                    .path(upfile)
                                    .toUriString();
                            return new FileResponse(file.getOriginalFilename(), upfile, file.getContentType(), file.getSize(), fileDownloadUri, "File uploaded with success!");
                        }
                )
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }


    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Resource resource = fileSystemStorageService.loadFile("file", filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
