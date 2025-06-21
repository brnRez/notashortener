package com.brunao.notashortener.links;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LinksResponse {

     private Long id;
     private String originalUrl;
     private String shortenedUrl;
     private String qrCodePath;
     private LocalDateTime initialDate;



}
