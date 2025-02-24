package com.sz.reservation.registration.infrastructure.adapter.inbound;

import com.sz.reservation.configuration.RootConfig;
import com.sz.reservation.configuration.ServletConfig;
import com.sz.reservation.registration.domain.model.Account;
import com.sz.reservation.registration.domain.port.outbound.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextHierarchy({
        @ContextConfiguration(classes = RootConfig.class),
        @ContextConfiguration(classes = ServletConfig.class)
})
@WebAppConfiguration
class HttpRegistrationControllerTestIT {

    @Autowired
    HttpRegistrationController controller;

    @Autowired
    AccountRepository repository;
    @Autowired
    WebApplicationContext wac;

    WebTestClient client;

    @BeforeEach
    void setUp() {
        client = MockMvcWebTestClient.bindToApplicationContext(this.wac).build();
    }




    private void assertProfilePicStoredAndCleanup(Path profilePicturePath) throws IOException {
        //assert pfp exists
        assertTrue(Files.exists(profilePicturePath));
        //delete pfp stored in the file system
        Files.delete(profilePicturePath);
    }

    @Test
    @Transactional
    public void Should_CreateAccount_And_StoreProfilePicture_When_ValidRequest() throws IOException {
        //arrange
        String email = "zanone.santinoet36@gmail.com";
        String path = "src/test/resources/bird.jpg";
        byte[] imageLogo = Files.readAllBytes(Path.of(path));
        MockMultipartFile multipartFile = new MockMultipartFile("file","bird.jpg",MediaType.IMAGE_JPEG_VALUE ,imageLogo);

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("username","eloytejero");
        builder.part("name","eloy");
        builder.part("surname","eloy");
        builder.part("email",email);
        builder.part("countryCode","+54");
        builder.part("phoneNumber","1144001478");
        builder.part("birthDate","2010-04-04");
        builder.part("nationality","argentina");
        builder.part("profilePicture",multipartFile.getResource());
        builder.part("password","eightcharacterlong");


        //act and assert
        // asserting response status
        client.post().uri("/api/v1/register")
                .bodyValue(builder.build())
                .exchange()
                .expectStatus().isCreated();


        // asserting account is created
        Optional<Account> account = repository.findAccountByEmail(email);
        assertTrue(account.isPresent());

        // asserting photo is stored in file system
        Path pfpPath = Path.of(account.get().getProfilePicture().getImagePath());
        assertProfilePicStoredAndCleanup(pfpPath);

    }

    @Test
    @Transactional
    public void Should_ReturnError_When_AlreadyInUseUsername() throws IOException {
        //arrange
        String email = "zanone.santinoet36@gmail.com";
        String path = "src/test/resources/bird.jpg";
        byte[] imageLogo = Files.readAllBytes(Path.of(path));
        MockMultipartFile multipartFile = new MockMultipartFile("file","bird.jpg",MediaType.IMAGE_JPEG_VALUE ,imageLogo);

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("username","eloytejero");
        builder.part("name","eloy");
        builder.part("surname","eloy");
        builder.part("email",email);
        builder.part("countryCode","+54");
        builder.part("phoneNumber","1144001478");
        builder.part("birthDate","2010-04-04");
        builder.part("nationality","argentina");
        builder.part("profilePicture",multipartFile.getResource());
        builder.part("password","eightcharacterlong");


        //act and assert
        //first insert
        client.post().uri("/api/v1/register")
                .bodyValue(builder.build())
                .exchange().expectStatus().isCreated(); // first request is fulfilled correctly

        //second insert
        client.post().uri("/api/v1/register")
                .bodyValue(builder.build())
                .exchange()
                .expectStatus().isBadRequest(); // second one should fail


        // asserting account is created
        Optional<Account> account = repository.findAccountByEmail(email);
        assertTrue(account.isPresent());

        // asserting photo is stored in file system
        Path pfpPath = Path.of(account.get().getProfilePicture().getImagePath());
        assertProfilePicStoredAndCleanup(pfpPath);

    }

    @Test
    @Transactional
    public void Should_ReturnError_When_AlreadyInUseEmail() throws IOException {
        //arrange
        String email = "zanone.santinoet36@gmail.com";
        String path = "src/test/resources/bird.jpg";
        byte[] imageLogo = Files.readAllBytes(Path.of(path));
        MockMultipartFile multipartFile = new MockMultipartFile("file","bird.jpg",MediaType.IMAGE_JPEG_VALUE ,imageLogo);

        MultipartBodyBuilder firstBuilder = new MultipartBodyBuilder();
        firstBuilder.part("username","eloytejero");
        firstBuilder.part("name","eloy");
        firstBuilder.part("surname","eloy");
        firstBuilder.part("email",email);
        firstBuilder.part("countryCode","+54");
        firstBuilder.part("phoneNumber","1144001478");
        firstBuilder.part("birthDate","2010-04-04");
        firstBuilder.part("nationality","argentina");
        firstBuilder.part("profilePicture",multipartFile.getResource());
        firstBuilder.part("password","eightcharacterlong");

        MultipartBodyBuilder secondBuilder = new MultipartBodyBuilder();
        secondBuilder.part("username","santino");
        secondBuilder.part("name","eloy");
        secondBuilder.part("surname","eloy");
        secondBuilder.part("email","zanone.santinoet36@gmail.com");
        secondBuilder.part("countryCode","+54");
        secondBuilder.part("phoneNumber","1144001478");
        secondBuilder.part("birthDate","2010-04-04");
        secondBuilder.part("nationality","argentina");
        secondBuilder.part("profilePicture",multipartFile.getResource());
        secondBuilder.part("password","eightcharacterlong");

        //act and assert
        //first insert
        client.post().uri("/api/v1/register")
                .bodyValue(firstBuilder.build())
                .exchange().expectStatus().isCreated(); // first request is fulfilled correctly

        //second insert
        client.post().uri("/api/v1/register")
                .bodyValue(secondBuilder.build())
                .exchange()
                .expectStatus().isBadRequest(); // second one should fail


        // asserting account is created
        Optional<Account> account = repository.findAccountByEmail(email);
        assertTrue(account.isPresent());

        // asserting photo is stored in file system
        Path pfpPath = Path.of(account.get().getProfilePicture().getImagePath());
        assertProfilePicStoredAndCleanup(pfpPath);



    }

    @Test
    public void Should_ReturnBadRequest_When_EmptyInput()  {
        //arrange act and assert
        client.post().uri("/api/v1/register").exchange().expectStatus().isBadRequest();
    }



}