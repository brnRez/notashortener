package com.brunao.notashortener.links;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinksRepository extends JpaRepository<Links, Long>{



    Links findByShortenedUrl(String shortenedUrl);
}
