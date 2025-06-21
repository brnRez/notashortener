package com.brunao.notashortener.links;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_links")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Links {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalUrl;
    private String shortenedUrl;
    private String qrCodePath;
    private LocalDateTime initialDate;


}
