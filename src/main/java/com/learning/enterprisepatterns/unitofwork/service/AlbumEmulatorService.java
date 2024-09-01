package com.learning.enterprisepatterns.unitofwork.service;

import com.learning.enterprisepatterns.unitofwork.UnitOfWork;
import com.learning.enterprisepatterns.unitofwork.model.Album;
import com.learning.enterprisepatterns.unitofwork.registry.RepositoryRegistry;
import com.learning.enterprisepatterns.unitofwork.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlbumEmulatorService {
    private final RepositoryRegistry registry;
    private final AlbumRepository albumRepository;

    public void doSomethingWithAlbum(Long album1Id, Long albumToRemoveId) {
        try (var unitOfWork = UnitOfWork.startCurrent(registry)) {
            var album = albumRepository.findById(album1Id).get();
            album.setTitle("new title");

            var albumToRemove = albumRepository.findById(albumToRemoveId).get();
            albumToRemove.remove();

            var newAlbum = new Album("new album");

            album.setTitle("titleNew");

            unitOfWork.commit();
        }
    }
}
