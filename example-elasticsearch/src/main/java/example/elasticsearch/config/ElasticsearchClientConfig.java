package example.elasticsearch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.lang.NonNull;

import java.time.Duration;

/**
 * 客户端配置类
 *
 * @author knight
 * @since 2023/12/04
 */
@Configuration
public class ElasticsearchClientConfig extends ElasticsearchConfiguration {

    @NonNull
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder().connectedTo("127.0.0.1:9200")
                .withConnectTimeout(Duration.ofSeconds(5))
                .withSocketTimeout(Duration.ofSeconds(5)).build();
    }

}
