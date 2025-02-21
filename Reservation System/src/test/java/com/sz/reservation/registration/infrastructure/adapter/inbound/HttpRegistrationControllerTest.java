package com.sz.reservation.registration.infrastructure.adapter.inbound;

import com.sz.reservation.configuration.RootConfig;
import com.sz.reservation.registration.infrastructure.dto.AccountCreationRequest;
import org.apache.http.protocol.HTTP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RootConfig.class})
@WebAppConfiguration
class HttpRegistrationControllerTest {

    @Autowired
    WebApplicationContext wac;

    WebTestClient client;

    @BeforeEach
    void setUp() {
        client = MockMvcWebTestClient.bindToApplicationContext(this.wac).build();
    }

    @Test
    //@Transactional
    public void Should_ReturnCreated_When_ValidRequest() throws IOException {
        //arrange
        String path = "src/test/resources/bird.jpg";
        byte[] imageLogo = Files.readAllBytes(Path.of(path));
        MockMultipartFile multipartFile = new MockMultipartFile("file","bird.jpg",MediaType.IMAGE_JPEG_VALUE ,imageLogo);
        AccountCreationRequest request = new AccountCreationRequest(
                "supermike",
                "mike",
                "kawasaki",
                "notRealEmail@not.gmail",
                "+54",
                "1144001478",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("username","eloytejero");
        builder.part("name","eloy");
        builder.part("surname","eloy");
        builder.part("email","zanone.santinoet36@gmail.com");
        builder.part("countryCode","+54");
        builder.part("phoneNumber","1144001478");
        builder.part("birthDate","2010-04-04");
        builder.part("nationality","argentina");
        builder.part("profilePicture",multipartFile.getResource());
        builder.part("password","eightcharacterlong");

        client.post().uri("/api/v1/register")
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    @Disabled
    @Transactional
    public void Should_ReturnError_When_RepeatingUsername() throws IOException {
        //arrange
        String path = "src/test/resources/bird.jpg";
        byte[] imageLogo = Files.readAllBytes(Path.of(path));
        MockMultipartFile multipartFile = new MockMultipartFile("file","bird.jpg",MediaType.IMAGE_JPEG_VALUE ,imageLogo);

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("username","eloytejero");
        builder.part("name","eloy");
        builder.part("surname","eloy");
        builder.part("email","zanone.santinoet36@gmail.com");
        builder.part("countryCode","+54");
        builder.part("phoneNumber","1144001478");
        builder.part("birthDate","2010-04-04");
        builder.part("nationality","argentina");
        builder.part("profilePicture",multipartFile.getResource());
        builder.part("password","eightcharacterlong");

        //first insert
        client.post().uri("/api/v1/register")
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange();

        //second insert
        client.post().uri("/api/v1/register")
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isBadRequest();
    }

}