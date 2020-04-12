import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;


public class WireMockUsingTestNG {


    public void serverSetUp(String statusCode, String contentType, String URI, String respBody) {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        WireMock.configureFor("localhost", 8080);
        stubFor(get(urlEqualTo(URI))
                .willReturn(aResponse()
                        .withStatus(Integer.parseInt(statusCode))
                        .withHeader("Content-Type", contentType)
                        .withBody(respBody)));
    }


    private static String convertResponseToString(HttpResponse httpResponse) throws IOException {
        InputStream responseStream = httpResponse.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return responseString;
    }


    @Test
    public void testWireMock() throws IOException {
        String statusCode = "200";
        String contentType = "application/json";
        String uri = "/v1/wireMockHome";
        String statusMessage = "Welcome to wire mock tutorial";
        File file = new File("/wiremock/src/main/resources/jsonForWireMock");
        String resp = FileUtils.readFileToString(file);
        resp = resp.replace("${statusMessage}", statusMessage ).replace("${statusCode}", statusCode);



        serverSetUp(statusCode, contentType, uri, resp);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8080/v1/wireMockHome");
        HttpResponse httpResponse = httpClient.execute(request);
        String responseString = convertResponseToString(httpResponse);
        System.out.println("response is ====> "+responseString);
    }


}
