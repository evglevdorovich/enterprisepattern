package com.learning.enterprisepatterns.unitofwork.repository;

import com.learning.enterprisepatterns.unitofwork.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
