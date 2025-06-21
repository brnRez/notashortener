package com.brunao.notashortener.links;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
public class LinksService {

    private final LinksRepository linksRepository;

    public LinksService(LinksRepository linksRepository){
        this.linksRepository = linksRepository;
    }

    public String generateRandomUrl(){
        return RandomStringUtils.randomAlphanumeric(5,10);
    }
    public String checksHttp(String originalUrl){
        if (!originalUrl.matches("^(http|https)://.*")){
            return "https://" + originalUrl;
        }
        return originalUrl;
    }

    public String generateQrCodeFilePath(String shortenedUrl) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String qrCodedUrl = "http://localhost:8080/r/" + shortenedUrl;

        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodedUrl, BarcodeFormat.QR_CODE, 200, 200);
        String filePath = "qr-codes/" + shortenedUrl + ".png";

        Path path = FileSystems.getDefault().getPath("src/main/resources/static/" + filePath);
        MatrixToImageWriter.writeToPath(bitMatrix,"PNG", path);

        return "http://localhost:8080/qr-codes/" + shortenedUrl;
    }

    public Links shortenUrl(String originalUrl) throws IOException, WriterException {
        Links link = new Links();
        link.setOriginalUrl(checksHttp(originalUrl));
        link.setShortenedUrl(generateRandomUrl());
        link.setInitialDate(LocalDateTime.now());
        link.setQrCodePath(generateQrCodeFilePath(link.getShortenedUrl()));

        return linksRepository.save(link);

    }

    public Links obtainOriginalUrl(String shortenedUrl){

        try {
            return linksRepository.findByShortenedUrl(shortenedUrl);
        }catch (Exception error){
            throw new RuntimeException("Url Not Found in our database");
        }


    }

}