package com.sz.reservation.accountManagement.infrastructure.adapter.inbound;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.accountManagement.configuration.AccountConfig;
import com.sz.reservation.accountManagement.domain.model.Account;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.globalConfiguration.RootConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RootConfig.class, AccountConfig.class})

@WebAppConfiguration
@ActiveProfiles(value = {"test","default"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpRegistrationControllerTestIT {

    @Autowired
    AccountRepository repository;
    @Autowired
    WebApplicationContext wac;

    WebTestClient client ;

    private final static String REGISTRATION_PATH = "/account/registration";

    private String email;
    private String userId;

    private String username;
    private String name;
    private String surname;

    private String phoneNumber;
    private String countryCode;

    private String birthDate;
    private String nationality;

    private String password;


    @BeforeEach
    public void initializeVariables(){
        userId = UuidCreator.getTimeOrderedEpoch().toString();
        username = "wolfofwallstreet";
        name = "jordan";
        surname = "belfort";
        email = "inventedEmail@miau.com";
        countryCode = "54";
        phoneNumber = "1111448899";
        birthDate = LocalDate.now().minusDays(10).toString();
        nationality = "Argentina";
        password ="ultrasafepassword";
    }

    @BeforeAll
    public void instantiateClient(){
        client  = MockMvcWebTestClient.bindToApplicationContext(wac).build();;
    }


    @Test
    @Transactional("account.transactionManager")
    public void Should_CreateAccount_And_StoreProfilePicture_When_ValidRequest() throws IOException {
        //arrange
        MultipartBodyBuilder builder = createMultipartBodyBuilderRequest();

        //act and assert
        // asserting response status
        client.post().uri(REGISTRATION_PATH)
                .bodyValue(builder.build())
                .exchange()
                .expectStatus().isCreated();


        // asserting account is created
        Optional<Account> account = repository.findAccountByEmail(email);
        assertTrue(account.isPresent());


    }
    @Test
    @Transactional("account.transactionManager")
    public void Should_ReturnError_When_AlreadyInUseUsername() throws IOException {
        //arrange
        MultipartBodyBuilder builder = createMultipartBodyBuilderRequest();
        String unchangedEmail = email;

        email = "otherEmail@gmail.com"; // changing email so it only detects username in use

        //second builder
        MultipartBodyBuilder secondBuilder = createMultipartBodyBuilderRequest();

        //act and assert
        //first insert
        try {
            client.post().uri(REGISTRATION_PATH)
                    .bodyValue(builder.build())
                    .exchange().expectStatus().isCreated(); // first request is fulfilled correctly

            //second insert
            client.post().uri(REGISTRATION_PATH)
                    .bodyValue(secondBuilder.build())
                    .exchange()
                    .expectStatus().isBadRequest(); // second one should fail


            // asserting first account is created
            Optional<Account> account = repository.findAccountByEmail(unchangedEmail);
            assertTrue(account.isPresent());
        }catch (Exception e){
            throw e;
        }
    }

    @Test
    @Transactional("account.transactionManager")
    public void Should_ReturnError_When_AlreadyInUseEmail() throws IOException {
        //arrange
        MultipartBodyBuilder firstBuilder = createMultipartBodyBuilderRequest();
        username = "santino"; // change username so only detects that the email is already in use
        MultipartBodyBuilder secondBuilder = createMultipartBodyBuilderRequest();

        //act and assert
        //first insert
        client.post().uri(REGISTRATION_PATH)
                .bodyValue(firstBuilder.build())
                .exchange().expectStatus().isCreated(); // first request is fulfilled correctly

        //second insert
        client.post().uri(REGISTRATION_PATH)
                .bodyValue(secondBuilder.build())
                .exchange()
                .expectStatus().isBadRequest(); // second one should fail


        // asserting account is created
        Optional<Account> account = repository.findAccountByEmail(email);
        assertTrue(account.isPresent());
    }

    @Test
    public void Should_ReturnBadRequest_When_EmptyInput()  {
        //arrange act and assert
        client.post().uri(REGISTRATION_PATH).exchange().expectStatus().isBadRequest();
    }










    private MultipartBodyBuilder createMultipartBodyBuilderRequest() throws IOException {
        String path = "src/test/resources/bird.jpg";
        byte[] imageLogo = Files.readAllBytes(Path.of(path));
        MockMultipartFile multipartFile = new MockMultipartFile("file","bird.jpg",MediaType.IMAGE_JPEG_VALUE ,imageLogo);

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("username",username);
        builder.part("name",name);
        builder.part("surname",surname);
        builder.part("email",email);
        builder.part("countryCode",countryCode);
        builder.part("phoneNumber",phoneNumber);
        builder.part("birthDate",birthDate);
        builder.part("nationality",nationality);
        builder.part("profilePicture",multipartFile.getResource());
        builder.part("password",password);

        return builder;
    }

}