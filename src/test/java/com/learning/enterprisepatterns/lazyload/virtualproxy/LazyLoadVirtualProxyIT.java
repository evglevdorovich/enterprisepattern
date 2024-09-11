package com.learning.enterprisepatterns.lazyload.virtualproxy;

import com.learning.enterprisepatterns.lazyload.virtualProxy.MovieMapper;
import com.learning.enterprisepatterns.lazyload.virtualProxy.utils.LazyLoadUtils;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class LazyLoadVirtualProxyIT {

    @Autowired
    private MovieMapper movieMapper;

    @Test
    void shouldLoadWithLazyObjectProducer() {
        var movie = movieMapper.findById(1);

        var expectedProducerName = "Spielberg";
        var expectedId = 1;

        assertThat(movie.getProducer().getName()).isEqualTo(expectedProducerName);
        assertThat(movie.getProducer().getId()).isEqualTo(expectedId);

        // why do we need it ?
        // because only in that case we could do casts for subclasses
        // the same approach is used in Hibernate.unproxy();
        val producer = LazyLoadUtils.unproxy(movie.getProducer());

        assertThat(producer.getName()).isEqualTo(expectedProducerName);
        assertThat(producer.getId()).isEqualTo(expectedId);
    }
}
