package com.sz.reservation.accountManagement.infrastructure.adapter.inbound;

import com.sz.reservation.accountManagement.application.useCase.AccountVerificationUseCase;
import com.sz.reservation.accountManagement.configuration.AccountConfig;
import com.sz.reservation.accountManagement.configuration.exceptionHandler.AccountExceptionHandler;
import com.sz.reservation.globalConfiguration.RootConfig;
import com.sz.reservation.globalConfiguration.globalExceptionHandler.GlobalHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("unit test HttpEmailVerificationControllerTest")

class HttpEmailVerificationControllerTest {

    @MockitoBean
    private  AccountVerificationUseCase accountVerificationUseCase;

    private  HttpEmailVerificationController controller;

    private  WebTestClient client;

    private String VERIFICATION_PATH = "/account/verification";


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new HttpEmailVerificationController(accountVerificationUseCase);
        client = MockMvcWebTestClient
                .bindToController(controller).
                controllerAdvice(new GlobalHandler())
                .controllerAdvice(new AccountExceptionHandler())
                .build();
    }

    @Test
    public void Should_ReturnStatusOk_When_ValidInput(){
        //arrange
        String token = "01954f09-742d-7d86-a3da-b0127c8facc4"; // 36 characters token
        //act and assert
        client.post().uri(uriBuilder -> uriBuilder
                .path(VERIFICATION_PATH).queryParam("token",token).build()).exchange().expectStatus().isOk();
    }

    @Test
    public void Should_ReturnBadRequest_When_TokenIsBiggerThan36Char(){

        //arrange
        String token = "01954f09-742d-7d86-a3da-b0127c8facc4qwer"; // 40 characters token

        //act and assert
        client.post().uri(uriBuilder -> uriBuilder
                .path(VERIFICATION_PATH).queryParam("token",token).build()).exchange().expectStatus().isBadRequest();
    }

    @Test
    public void Should_ReturnBadRequest_When_TokenIsSmallerThan36Char(){
        //arrange
        String token = "01954f09-742d-7d86-a3da-b0127c8facc"; // 35 characters token

        //act and assert
        client.post().uri(uriBuilder -> uriBuilder
                .path(VERIFICATION_PATH).queryParam("token",token).build()).exchange().expectStatus().isBadRequest();
    }

    @Test
    public void Should_ReturnBadRequest_When_TokenIsNull(){
        //act and assert
        client.post().uri(uriBuilder -> uriBuilder
                .path(VERIFICATION_PATH).queryParam("token", (Object) null).build()).exchange().expectStatus().isBadRequest();
    }


    @Test
    public void Should_ReturnBadRequest_When_TokenIsBlank(){
        //act and assert
        client.post().uri(uriBuilder -> uriBuilder
                .path(VERIFICATION_PATH).queryParam("token", "").build()).exchange().expectStatus().isBadRequest();
    }

    @Test
    public void Should_ReturnBadRequest_When_TokenHasWhitespaces(){
        //arrange
        String token = "01954f09-742d-7d86-a3da-b0127c8 facc4"; // has a whitespace

        //act and assert
        client.post().uri(uriBuilder -> uriBuilder
                .path(VERIFICATION_PATH).queryParam("token", token).build()).exchange().expectStatus().isBadRequest();
    }

    @Test
    public void Should_ReturnBadRequest_When_TokenHasInvalidFormat(){
        //arrange
        String token = "01954f09-742d-7d86-a3dab-0127c8facc4"; // invalidFormat

        //act and assert
        client.post().uri(uriBuilder -> uriBuilder
                .path(VERIFICATION_PATH).queryParam("token", token).build()).exchange().expectStatus().isBadRequest();
    }






}