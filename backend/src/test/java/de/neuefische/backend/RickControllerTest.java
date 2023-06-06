package de.neuefische.backend;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RickControllerTest {

    @Autowired
    MockMvc mvc;

    private static MockWebServer mockWebServer;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("rick-api-url",() -> mockWebServer.url("/").toString());
    }

    @BeforeAll
    static void startMockServer() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void stopMockServer() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void getRickCharacters() throws Exception {

        mockWebServer.enqueue(new MockResponse()
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody("{\"results\": [{\"id\": \"1\", \"name\": \"Currywurst\", \"status\": \"Alive\", \"species\": \"Human\"}]}"));


        mvc.perform(get("/api/rick-and-morty/characters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value("1"))
                .andExpect(jsonPath("[0].name").value("Currywurst"));

    }

}