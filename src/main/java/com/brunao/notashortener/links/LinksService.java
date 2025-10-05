package com.brunao.notashortener.links;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class LinksService {

    private final LinksRepository linksRepository;

    @Autowired
    private S3Client s3Client;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Value("${aws.region}")
    private String awsRegion;

    public LinksService(LinksRepository linksRepository){
        this.linksRepository = linksRepository;
    }

    public String generateRandomUrl(){return RandomStringUtils.randomAlphanumeric(5,10);}

    public String checksHttp(String originalUrl){
        if (!originalUrl.matches("^(http|https)://.*")){
            return "https://" + originalUrl;
        }
        return originalUrl;
    }

    public String generateAndUploadQrCode(String shortenedUrl) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();

        String qrCodedUrl = "http://localhost:8080/r/" + shortenedUrl;

        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodedUrl, BarcodeFormat.QR_CODE, 200, 200);
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngQrCodeData = pngOutputStream.toByteArray();

        //Old - Creates local QR Code Image
        //String filePath = "qr-codes/" + shortenedUrl + ".png";
        //Path path = FileSystems.getDefault().getPath("src/main/resources/static/" + filePath);
        //MatrixToImageWriter.writeToPath(bitMatrix,"PNG", path);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(shortenedUrl)
                .contentType("image/png")
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(pngQrCodeData));


        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName,awsRegion,shortenedUrl);
    }

    public Links shortenUrl(String originalUrl) throws IOException, WriterException {
        Links link = new Links();
        link.setOriginalUrl(checksHttp(originalUrl));
        link.setShortenedUrl(generateRandomUrl());
        link.setInitialDate(LocalDateTime.now());
        link.setQrCodeUrl(generateAndUploadQrCode(link.getShortenedUrl()));

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