package ir.torshizi.upload.upload.service;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import javax.annotation.PostConstruct;

import ir.torshizi.upload.upload.FileUploadProperties;
import ir.torshizi.upload.upload.exception.FileNotFoundException;
import ir.torshizi.upload.upload.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileSystemStorageService {
    private final Path dirLocation;

    @Autowired
    public FileSystemStorageService(FileUploadProperties fileUploadProperties) {
        this.dirLocation = Paths.get(fileUploadProperties.getLocation()).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.dirLocation);
        } catch (Exception ex) {
            throw new FileStorageException(null, "Could not create upload dir!");
        }
    }


    public String saveFile(String filedName, MultipartFile file) {
        try {
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            Path dfile = this.dirLocation.resolve(fileName);
            Files.copy(file.getInputStream(), dfile, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            throw new FileStorageException(filedName, "Could not upload file");
        }
    }

    String generateUniqueFileName(String fileName) {
        return (UUID.randomUUID().toString()
                .concat("_")
                .concat(String.valueOf(System.currentTimeMillis()))
                .concat("_")
                .concat(fileName));
    }


    public Resource loadFile(String fieldName, String fileName) {
        try {
            Path file = this.dirLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException(fieldName, "Could not find file");
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException(fieldName, "Could not download file");
        }
    }
}
