package com.learning.enterprisepatterns.config;

import lombok.val;
import org.springframework.boot.autoconfigure.domain.EntityScanner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.convert.MappingContextTypeInformationMapper;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoManagedTypes;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.List;

import static org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper.DEFAULT_TYPE_KEY;

@Configuration
public class AppConfig {

    /**
     * Configures a mapper that includes type information only for classes annotated with {@link org.springframework.data.annotation.TypeAlias}.
     * Regular classes without this annotation will not have the {@code _class} property stored in MongoDB.
     * As a result, only classes with {@link org.springframework.data.annotation.TypeAlias} will have the {@code _class} property
     * in MongoDB, which is used for deserialization.
     * Unlike standard polymorphic (de)serialization using ObjectMapper, this approach stores the polymorphic type as an inner document
     * rather than a simple string. This allows us to perform operations such as finding by property and incrementing values within the document.
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory, MongoCustomConversions conversions, MongoMappingContext context) {
        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        mappingConverter.setCustomConversions(conversions);
        mappingConverter.setCodecRegistryProvider(factory);
        mappingConverter.setMapKeyDotReplacement("-dot-");
        mappingConverter.afterPropertiesSet();
        val mapper = new MappingContextTypeInformationMapper(mappingConverter.getMappingContext());
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(DEFAULT_TYPE_KEY, List.of(mapper)));
        return mappingConverter;
    }

    /**
     * Ensures that Spring Boot scans both {@link org.springframework.data.mongodb.core.mapping.Document} classes
     * and {@link org.springframework.data.annotation.TypeAlias} classes.
     * It's needed for manage TypeAlias for proper type mapping
     */
    @Bean
    public MongoManagedTypes mongoManagedTypes(ApplicationContext context) throws ClassNotFoundException {
        return MongoManagedTypes.fromIterable(new EntityScanner(context).scan(Persistent.class));
    }
}
