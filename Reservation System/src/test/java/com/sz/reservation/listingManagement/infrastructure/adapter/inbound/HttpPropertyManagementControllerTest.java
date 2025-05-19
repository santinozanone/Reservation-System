package com.sz.reservation.listingManagement.infrastructure.adapter.inbound;

import com.sz.reservation.globalConfiguration.globalExceptionHandler.GlobalHandler;
import com.sz.reservation.globalConfiguration.security.userDetails.CustomUserDetails;
import com.sz.reservation.listingManagement.application.useCase.listing.ListingImageState;
import com.sz.reservation.listingManagement.application.useCase.listing.ListingPropertyUseCase;
import com.sz.reservation.listingManagement.configuration.exceptionHandler.PropertyExceptionHandler;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit testing HttpPropertyController")
class HttpPropertyManagementControllerTest {

    private MockMvc mvc;

    @Mock
    ListingPropertyUseCase listingPropertyUseCase;

    @BeforeEach
    public void setup() throws IOException {
        CustomUserDetails customUserDetails = new CustomUserDetails("01854f09-742d-7d86-a3da-b0127c8facc4", "juan", "juan2", true,true, "email");
        mvc = MockMvcBuilders
                .standaloneSetup(new HttpPropertyManagementController(listingPropertyUseCase))
                .setControllerAdvice(new PropertyExceptionHandler(), new GlobalHandler())
                .build();
        Mockito.lenient().when(listingPropertyUseCase.uploadListingImages(
                ArgumentMatchers.any(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any(),
                ArgumentMatchers.any())
        ).thenReturn(ListingImageState.CORRECT);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.lenient().when(authentication.getPrincipal()).thenReturn(customUserDetails);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


    }


    @Test
    public void Should_ReturnStatusOK_When_Upload_Valid_ListingImages() throws Exception {
        //arrange
        MockMultipartHttpServletRequest mockRequest = new MockMultipartHttpServletRequest();
        String boundary = "q1w2e3r4t5y6u7i8o9";
        mockRequest.setContentType("multipart/form-data; boundary=" + boundary);

        File file = new File("src/test/resources/bird.jpg");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HttpEntity multipartEntity = MultipartEntityBuilder.create()
                .addTextBody("listingId", "01854f09-742d-7d86-a3da-b0127c8facc4")
                .addPart("file", new FileBody(file))
                .setBoundary(boundary)
                .build();

        multipartEntity.writeTo(outputStream); // or getContent() to get content stream
        byte[] content = outputStream.toByteArray(); // serialize the content to bytes

        //act and assert
        MvcResult r = mvc.perform(multipart("/api/v1/host/listing/images-upload")
                        .content(content)
                        .servletPath("/api/v1/host")
                        .contentType("multipart/form-data; boundary=" + boundary)
                )
                .andExpect(status().isOk()).andReturn();
        System.out.println(r.getResponse().getContentAsString());
    }

    @Test
    public void Should_ReturnBadRequest_When_NotMultipartRequest() throws Exception {
        String boundary = "q1w2e3r4t5y6u7i8o9";
        //act and assert
        mvc.perform(post("/api/v1/host/listing/images-upload")
                .servletPath("/api/v1/host")
                .contentType("form-data; boundary=" + boundary)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void Should_ReturnBadRequest_When_FirstParameter_IsNotNamed_ListingId() throws Exception {
        //arrange
        MockMultipartHttpServletRequest mockRequest = new MockMultipartHttpServletRequest();
        String boundary = "q1w2e3r4t5y6u7i8o9";
        mockRequest.setContentType("multipart/form-data; boundary=" + boundary);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HttpEntity multipartEntity = MultipartEntityBuilder.create()
                .addTextBody("id", "01854f09-742d-7d86-a3da-b0127c8facc4")
                .setBoundary(boundary)
                .build();

        multipartEntity.writeTo(outputStream); // or getContent() to get content stream
        byte[] content = outputStream.toByteArray(); // serialize the content to bytes

        //act and assert
        mvc.perform(multipart("/api/v1/host/listing/images-upload")
                        .content(content)
                        .servletPath("/api/v1/host")
                        .contentType("multipart/form-data; boundary=" + boundary)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void Should_ReturnBadRequest_When_ListingId_IsEmpty() throws Exception {
        //arrange
        MockMultipartHttpServletRequest mockRequest = new MockMultipartHttpServletRequest();
        String boundary = "q1w2e3r4t5y6u7i8o9";
        mockRequest.setContentType("multipart/form-data; boundary=" + boundary);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HttpEntity multipartEntity = MultipartEntityBuilder.create()
                .addTextBody("listingId", "")
                .setBoundary(boundary)
                .build();

        multipartEntity.writeTo(outputStream); // or getContent() to get content stream
        byte[] content = outputStream.toByteArray(); // serialize the content to bytes

        //act and assert
        mvc.perform(multipart("/api/v1/host/listing/images-upload")
                        .content(content)
                        .servletPath("/api/v1/host")
                        .contentType("multipart/form-data; boundary=" + boundary)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void Should_ReturnBadRequest_When_ListingId_IsNotValid_UUID() throws Exception {
        //arrange
        MockMultipartHttpServletRequest mockRequest = new MockMultipartHttpServletRequest();
        String boundary = "q1w2e3r4t5y6u7i8o9";
        mockRequest.setContentType("multipart/form-data; boundary=" + boundary);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HttpEntity multipartEntity = MultipartEntityBuilder.create()
                .addTextBody("listingId", "asfdafafdsfs-fasfa")
                .setBoundary(boundary)
                .build();

        multipartEntity.writeTo(outputStream); // or getContent() to get content stream
        byte[] content = outputStream.toByteArray(); // serialize the content to bytes

        //act and assert
        mvc.perform(multipart("/api/v1/host/listing/images-upload")
                        .content(content)
                        .servletPath("/api/v1/host")
                        .contentType("multipart/form-data; boundary=" + boundary)
                )
                .andExpect(status().isBadRequest());
    }


    @Test
    public void Should_Return_NoFilesDetected_InRequestBody_When_FileParameter_IsFormField() throws Exception {
        //arrange
        MockMultipartHttpServletRequest mockRequest = new MockMultipartHttpServletRequest();
        String boundary = "q1w2e3r4t5y6u7i8o9";
        mockRequest.setContentType("multipart/form-data; boundary=" + boundary);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HttpEntity multipartEntity = MultipartEntityBuilder.create()
                .addTextBody("listingId", "01854f09-742d-7d86-a3da-b0127c8facc4")
                .addTextBody("file", "file")
                .setBoundary(boundary)
                .build();

        multipartEntity.writeTo(outputStream); // or getContent() to get content stream
        byte[] content = outputStream.toByteArray(); // serialize the content to bytes

        //act and assert
       MvcResult r = mvc.perform(multipart("/api/v1/host/listing/images-upload")
                        .content(content)
                        .servletPath("/api/v1/host")
                        .contentType("multipart/form-data; boundary=" + boundary)
                ).andExpect(status().isBadRequest()).andReturn();
       assertTrue(r.getResponse().getContentAsString().contains(ListingImageUploadResponseStatus.NO_FILES_DETECTED.name()));
    }
}