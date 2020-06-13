package restopass.client;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class DefaultRestConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRestConnector.class);

    CloseableHttpClient httpClient = HttpClients.createDefault();

    public void doPost(String url, Map<String, String> headers, Object body, Type requestType) {

        try {

            HttpPost request = new HttpPost(url);

            if(body != null) {

                Gson gson = new Gson();
                String json = gson.toJson(body, requestType);
                StringEntity input = new StringEntity(json);
                input.setContentType("application/json");
                request.setEntity(input);
            }

            if(headers != null) {
                for (Map.Entry<String,String> entry : headers.entrySet())
                    request.setHeader(entry.getKey(), entry.getValue());
            }

            LOGGER.info("Sending request {} to url {}", request.toString(), url);

            CloseableHttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() != 200) {
                String msg = "Failed : HTTP error code : "
                        + response.toString();
                LOGGER.error(msg);
                throw new RuntimeException(msg);
            } else if (response.getStatusLine().getStatusCode() == 200) {
                LOGGER.info("response:" + EntityUtils.toString(response.getEntity()));
            }
            HttpEntity entity = response.getEntity();
            response.close();

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
