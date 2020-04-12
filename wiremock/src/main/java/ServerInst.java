import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class ServerInst {


    public static void main(String[] args) throws IOException {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();
        
        WireMock.configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("/v1/wireMockHome"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("Welcome to wire mock tutorial")));
    }
}
