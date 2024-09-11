package com.learning.enterprisepatterns.lazyload.virtualProxy;

import com.learning.enterprisepatterns.activerecord.db.DB;
import com.learning.enterprisepatterns.lazyload.model.Movie;
import com.learning.enterprisepatterns.lazyload.model.Producer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
@RequiredArgsConstructor
public class MovieMapper implements MovieFinder {
    private final ProducerMapper producerMapper;

    private static final String COLUMNS = "id, name, producer_id";
    private static final String FIND_STATEMENT = """
            SELECT %s FROM movies
            WHERE ID = ?
            """.formatted(COLUMNS);

    @Override
    @SneakyThrows
    public Movie findById(int id) {
        val rowSet = DB.queryForRowSet(FIND_STATEMENT, id);
        rowSet.next();
        return load(rowSet);
    }

    @SneakyThrows
    private Movie load(SqlRowSet rowSet) {
        val result = new Movie();
        result.setId(rowSet.getInt("id"));
        result.setName(rowSet.getString("name"));
        val producerId = rowSet.getInt("producer_id");

        // could be done via metadata mapping, done for the explicit right now
        val virtualProxyProducer = createVirtualProxy();

        setId(virtualProxyProducer, producerId);
        result.setProducer(virtualProxyProducer);

        return result;
    }

    @SneakyThrows
    private Producer createVirtualProxy() {
        return new ByteBuddy()
                .subclass(Producer.class)
                .defineField("source", Producer.class, Visibility.PRIVATE)
                .defineField("id", Integer.class, Visibility.PRIVATE)
                .method(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class)))
                .intercept(MethodDelegation.to(new LazyLoadInterceptor(producerMapper)))
                .make()
                .load(Producer.class.getClassLoader())
                .getLoaded()
                .newInstance();
    }

    @SneakyThrows
    private static void setId(Producer virtualProxyProducer, int producerId) {
        val idField = ReflectionUtils.findField(virtualProxyProducer.getClass(), "id");
        idField.setAccessible(true);
        idField.set(virtualProxyProducer, producerId);
    }
}
