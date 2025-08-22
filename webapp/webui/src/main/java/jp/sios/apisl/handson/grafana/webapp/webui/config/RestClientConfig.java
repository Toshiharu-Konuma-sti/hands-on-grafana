package jp.sios.apisl.handson.grafana.webapp.webui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig
{

    @Bean
    // {{{ public RestClient restClient(RestClient.Builder restClientBuilder)
    public RestClient restClient(RestClient.Builder restClientBuilder)
    {
        RestClient restClient = restClientBuilder.build();
        return restClient;
    }
    // }}}

}
