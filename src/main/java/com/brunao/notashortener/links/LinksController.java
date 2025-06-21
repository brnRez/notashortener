package com.brunao.notashortener.links;


import com.google.zxing.WriterException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@AllArgsConstructor

@RestController
public class LinksController {

    private LinksService linksService;



    @PostMapping("/do-not-shorten")
    public ResponseEntity<LinksResponse> generateShortenedUrl(@RequestBody Map<String, String> request) throws IOException, WriterException {
        String originalUrl = request.get("originalUrl");
        Links link = linksService.shortenUrl(originalUrl);

        String generateUserRedirectUrl ="http://localhost:8080/r/" + link.getShortenedUrl();

        LinksResponse response = new LinksResponse(
                link.getId(),
                link.getOriginalUrl(),
                generateUserRedirectUrl,
                link.getQrCodePath(),
                link.getInitialDate()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/r/{shortenedUrl}")
    public void redirectLink(@PathVariable String shortenedUrl, HttpServletResponse response) throws IOException {
        Links link = linksService.obtainOriginalUrl(shortenedUrl);

        if (link !=null){
            try{

            response.sendRedirect(link.getOriginalUrl());

            } catch (IOException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @GetMapping("/qr-codes/{shortenedUrlId}")
    public ResponseEntity <Resource> viewQr(@PathVariable String shortenedUrlId) throws MalformedURLException {
        Path path = Paths.get("src/main/resources/static/qr-codes/" + shortenedUrlId + ".png" );

        if (!Files.exists(path)){
            return ResponseEntity.notFound().build();
        }

        Resource file = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(file);

    }

}
