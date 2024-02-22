package com.shoeStore.Kha.shoeStore.Contronller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class imgUploadContronller {
    @RequestMapping(value ="/getImg/{imgFile}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getImafe(@PathVariable("imgFile") String img_url) throws IOException {
        if (!img_url.equals("") || img_url != null) {
            try {
                Path filename = Paths.get("uploads/images", img_url);
                byte[] buffer = Files.readAllBytes(filename);
                ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
                return ResponseEntity.ok()
                        .contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("images/jpg"))
                        .body(byteArrayResource);
            } catch (Exception e) {

            }
        }
        return ResponseEntity.badRequest().build();
    }
}
