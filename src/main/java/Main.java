import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                            .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                            .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                            .build())
                    .build();

            HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");
            CloseableHttpResponse response = httpClient.execute(request);

            //Преобразуем json в список java объектов
            List<FactsAboutCats> facts = mapper.readValue(response.getEntity().getContent(),
                    new TypeReference<List<FactsAboutCats>>() {
                    });

            // отфильтруем факты, за которые никто не проголосовал
            List<FactsAboutCats> filteredListOfFacts = facts.stream()
                    .filter(value -> value.getUpvotes() != null && value.getUpvotes() > 0)
                    .collect(Collectors.toList());

            //выведем на экран
            for (int i = 0; i < filteredListOfFacts.size(); i++) {
                System.out.println("№" + (i + 1));
                System.out.println(filteredListOfFacts.get(i));
                System.out.println("_________________");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
