package tech.vedansh.blogapp.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class UploadUtil {
    private  static  final String UPLOAD_DIR = "uploads/";

    public String saveFile(MultipartFile file) {
        try{
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("File Upload failed",e);
        }
    }
}
