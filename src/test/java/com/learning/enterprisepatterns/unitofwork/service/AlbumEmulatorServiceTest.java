package com.learning.enterprisepatterns.unitofwork.service;

import com.learning.enterprisepatterns.unitofwork.UnitOfWork;
import com.learning.enterprisepatterns.unitofwork.model.Album;
import com.learning.enterprisepatterns.unitofwork.registry.RepositoryRegistry;
import com.learning.enterprisepatterns.unitofwork.repository.AlbumRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@DirtiesContext
class AlbumEmulatorServiceTest {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private RepositoryRegistry registry;

    @Autowired
    private AlbumEmulatorService albumEmulatorService;

    @Test
    void testAlbumEmulatorService() {
        // init

        Album albumToEdit = null;
        Album albumToRemove = null;

        //We could do that proxy will not be generated in test package
        //So we will not need there try with resources
        try (var unitOfWork = UnitOfWork.startCurrent(registry)) {
            albumToEdit = new Album("Album 1");
            albumToRemove = new Album("Album To remove");
            albumToEdit = albumRepository.saveAndFlush(albumToEdit);
            albumToRemove = albumRepository.saveAndFlush(albumToRemove);
        }

        albumEmulatorService.doSomethingWithAlbum(albumToEdit.getId(), albumToRemove.getId());

        var all = albumRepository.findAll().stream().sorted(Comparator.comparing(Album::getId)).toList();
        assertThat(all).hasSize(2);

        var newAlbum = all.get(1);
        var editedAlbum = all.get(0);

        assertThat(newAlbum.getTitle()).isEqualTo("new album");
        assertThat(editedAlbum.getTitle()).isEqualTo("titleNew");

        assertThat(newAlbum.getId()).isEqualTo(3);
        assertThat(editedAlbum.getId()).isEqualTo(albumToEdit.getId());
    }
}
