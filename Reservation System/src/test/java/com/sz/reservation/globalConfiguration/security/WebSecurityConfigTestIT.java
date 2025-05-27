package com.sz.reservation.globalConfiguration.security;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.sz.reservation.accountManagement.application.useCase.AccountRegistrationUseCase;
import com.sz.reservation.accountManagement.application.useCase.AccountVerificationUseCase;
import com.sz.reservation.accountManagement.configuration.AccountConfig;
import com.sz.reservation.globalConfiguration.RootConfig;
import com.sz.reservation.globalConfiguration.security.userDetails.CustomUserDetails;
import com.sz.reservation.listingManagement.application.useCase.listing.ListingImageState;
import com.sz.reservation.listingManagement.application.useCase.listing.ListingPropertyUseCase;
import com.sz.reservation.listingManagement.configuration.ListingConfig;
import com.sz.reservation.listingManagement.domain.AmenitiesType;
import com.sz.reservation.listingManagement.domain.HousingType;
import com.sz.reservation.listingManagement.domain.PropertyType;
import com.sz.reservation.listingManagement.domain.ReservationType;
import com.sz.reservation.listingManagement.infrastructure.AddressInfoRequestDto;
import com.sz.reservation.listingManagement.infrastructure.ListingRequestDto;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebSecurityConfig.class, AccountConfig.class ,RootConfig.class , ListingConfig.class})
@WebAppConfiguration
@ActiveProfiles({"test","default"})
@Disabled
class WebSecurityConfigTestIT {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;


    @MockitoBean
    ListingPropertyUseCase listingPropertyUseCase;
    @MockitoBean
    private AccountRegistrationUseCase accountRegistrationUseCase;
    @MockitoBean
    private AccountVerificationUseCase accountVerificationUseCase;




    private ListingRequestDto listingRequestDto;
    private ObjectMapper objectMapper;
    MockMultipartFile profilePicture = new MockMultipartFile("file",new byte[]{0000,111});
    String username;
    String name ;
    String surname ;
    String email ;
    String countryCode ;
    String phoneNumber;
    String birthDate ;
    String nationality ;
    String password ;

    @BeforeEach
    public void setup() throws IOException {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();



        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        // listing request
        setupListingRequestDto();

        //registration request
        setupRegistrationVariables();

        Mockito.lenient().when(listingPropertyUseCase.uploadListingImages(ArgumentMatchers.any(),
                ArgumentMatchers.any(),ArgumentMatchers.any(),ArgumentMatchers.any())).thenReturn(ListingImageState.CORRECT);
        Mockito.lenient().when(listingPropertyUseCase.listProperty(ArgumentMatchers.any(),
                ArgumentMatchers.any())).thenReturn("01854f09-742d-7d86-a3da-b0127c8facc4");

    }

    private void setupListingRequestDto() {
        List<AmenitiesType> amenities = new ArrayList<>();
        amenities.add(AmenitiesType.GYM);
        listingRequestDto = new ListingRequestDto("title",
                "description", new AddressInfoRequestDto("Argentina","9 de julio","A","0000","Buenos Aires","CABA"),
                2, 2, 2, 1, 10,
                PropertyType.HOUSE, HousingType.ENTIRE, ReservationType.AUTOMATIC_APPROVAL, amenities);
    }

    private void setupRegistrationVariables(){
         profilePicture = new MockMultipartFile("file",new byte[]{0000,111});
         username = "mike01";
         name = "mike";
         surname = "kawasaki";
         email = "notRealEmail@not.gmail";
         countryCode = "54";
         phoneNumber = "1111111111";
         birthDate = LocalDate.of(2014, 4, 15).toString();
         nationality = "argentina";
         password = "eightcharacterlong";
    }


    // PROPERTY MANAGEMENT ENDPOINTS --------------------------------
    @Test
    public void Should_AllowEnabledUserTo_ListingImageUploadEndpoint() throws Exception {
        //arrange
        CustomUserDetails customUserDetails = new CustomUserDetails("01854f09-742d-7d86-a3da-b0127c8facc4","juan","juan2",true,true,"email");
        MockMultipartHttpServletRequest mockRequest = new MockMultipartHttpServletRequest();
        String boundary = "q1w2e3r4t5y6u7i8o9";
        mockRequest.setContentType("multipart/form-data; boundary="+boundary);

        File file = new File("src/test/resources/bird.jpg");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HttpEntity multipartEntity = MultipartEntityBuilder.create()
                .addTextBody("listingId","01854f09-742d-7d86-a3da-b0127c8facc4")
                .addPart("file",new FileBody(file))
                .setBoundary(boundary)
                .build();

        multipartEntity.writeTo(outputStream); // or getContent() to get content stream
        byte[] content = outputStream.toByteArray(); // serialize the content to bytes

        //act and assert
        mvc.perform(multipart("/api/v1/host/listing/images-upload")
                        .content(content)
                        .with(user(customUserDetails))
                        .servletPath("/api/v1/host")
                        .contentType("multipart/form-data; boundary="+boundary)
                )
                .andExpect(status().isOk());

    }

    @Test
    public void Should_DenyNOTEnabledUserTo_ListingImageUploadEndpoint() throws Exception {
        //arrange
        MockMultipartHttpServletRequest mockRequest = new MockMultipartHttpServletRequest();
        String boundary = "q1w2e3r4t5y6u7i8o9";
        mockRequest.setContentType("multipart/form-data; boundary="+boundary);

        File file = new File("src/test/resources/bird.jpg");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HttpEntity multipartEntity = MultipartEntityBuilder.create()
                .addTextBody("listingId","01854f09-742d-7d86-a3da-b0127c8facc4")
                .addPart("file",new FileBody(file))
                .setBoundary(boundary)
                .build();

        multipartEntity.writeTo(outputStream); // or getContent() to get content stream
        byte[] content = outputStream.toByteArray(); // serialize the content to bytes

        //act and assert
        mvc.perform(multipart("/api/v1/host/listing/images-upload")
                        .content(content)
                        .with(user("juan").password("juan2"))
                        .servletPath("/api/v1/host")
                        .contentType("multipart/form-data; boundary="+boundary)
                )
                .andExpect(status().isForbidden());

    }

    @Test
    public void Should_DenyNotAuthenticatedUser_To_ListingImageUploadEndpoint() throws Exception {
        //arrange
        MockMultipartHttpServletRequest mockRequest = new MockMultipartHttpServletRequest();
        String boundary = "q1w2e3r4t5y6u7i8o9";
        mockRequest.setContentType("multipart/form-data; boundary="+boundary);

        File file = new File("src/test/resources/bird.jpg");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HttpEntity multipartEntity = MultipartEntityBuilder.create()
                .addTextBody("listingId","01854f09-742d-7d86-a3da-b0127c8facc4")
                .addPart("file",new FileBody(file))
                .setBoundary(boundary)
                .build();

        multipartEntity.writeTo(outputStream); // or getContent() to get content stream
        byte[] content = outputStream.toByteArray(); // serialize the content to bytes

        //act and assert
        mvc.perform(multipart("/api/v1/host/listing/images-upload")
                        .content(content)
                        .servletPath("/api/v1/host")
                        .contentType("multipart/form-data; boundary="+boundary)
                )
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void Should_AllowEnabledUserTo_ListingEndpoint() throws Exception {
        //arrange
        CustomUserDetails customUserDetails = new CustomUserDetails("01854f09-742d-7d86-a3da-b0127c8facc4","juan","juan2",true,true,"email");
        String json = objectMapper.writeValueAsString(listingRequestDto);

        //act and assert
        mvc.perform(post("/api/v1/host/listing")
                        .with(user(customUserDetails))
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .servletPath("/api/v1/host")).andExpect(status().isOk());
    }

    @Test
    public void Should_DenyNotEnabledUserTo_ListingEndpoint() throws Exception {
        //arrange
        String json = objectMapper.writeValueAsString(listingRequestDto);

        //act and assert
        mvc.perform(post("/api/v1/host/listing").contentType(MediaType.APPLICATION_JSON).content(json)
                .with(user("juan")
                        .password("juan2"))
                .servletPath("/api/v1/host")).andExpect(status().isForbidden());
    }

    @Test
    public void Should_DenyNotAuthenticatedUserTo_ListingEndpoint() throws Exception {
        //arrange
        String json = objectMapper.writeValueAsString(listingRequestDto);

        //act and assert
        mvc.perform(post("/api/v1/host/listing").contentType(MediaType.APPLICATION_JSON).content(json)
                .servletPath("/api/v1/host")).andExpect(status().isUnauthorized());
    }


    // ACCOUNT MANAGEMENT ENDPOINTS --------------------------------

    @Test
    public void Should_AllowEnabledUserTo_RegistrationEndpoint() throws Exception {
        //act and assert
        mvc.perform(multipart("/api/v1/account/registration")
                        .file("profilePicture",profilePicture.getBytes())
                        .param("username", username)
                        .param("name", name)
                        .param("surname", surname)
                        .param("email", email)
                        .param("countryCode", countryCode)
                        .param("phoneNumber", phoneNumber)
                        .param("birthDate", birthDate)
                        .param("nationality", nationality)
                        .param("password", password)

                        .with(user("juan")
                        .password("juan2")
                        .authorities(new SimpleGrantedAuthority("ENABLED_VERIFIED_USER")))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .servletPath("/api/v1")).andExpect(status().isCreated());
    }
    @Test
    public void Should_AllowNotEnabledUserTo_RegistrationEndpoint() throws Exception {
        //act and assert
        mvc.perform(multipart("/api/v1/account/registration")
                .file("profilePicture",profilePicture.getBytes())
                .param("username", username)
                .param("name", name)
                .param("surname", surname)
                .param("email", email)
                .param("countryCode", countryCode)
                .param("phoneNumber", phoneNumber)
                .param("birthDate", birthDate)
                .param("nationality", nationality)
                .param("password", password)

                .with(user("juan")
                        .password("juan2"))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .servletPath("/api/v1")).andExpect(status().isCreated());
    }

    @Test
    public void Should_AllowNotRegisteredUserTo_RegistrationEndpoint() throws Exception {
        //act and assert
        mvc.perform(multipart("/api/v1/account/registration")
                .file("profilePicture",profilePicture.getBytes())
                .param("username", username)
                .param("name", name)
                .param("surname", surname)
                .param("email", email)
                .param("countryCode", countryCode)
                .param("phoneNumber", phoneNumber)
                .param("birthDate", birthDate)
                .param("nationality", nationality)
                .param("password", password)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .servletPath("/api/v1")).andExpect(status().isCreated());
    }


    //HTTP EMAIL VERIFICATION
    @Test
    public void Should_AllowNotRegisteredUserTo_EmailVerification() throws Exception {
        //arrange
        String token = "01854f09-742d-7d86-a3da-b0127c8facc4";

        //act and assert
        mvc.perform(post("/api/v1/account/verification").contentType(MediaType.TEXT_PLAIN_VALUE).param("token",token)
                .servletPath("/api/v1")).andExpect(status().isOk());
    }

    @Test
    public void Should_AllowRegisteredUserTo_EmailVerification() throws Exception {
        //arrange
        String token = "01854f09-742d-7d86-a3da-b0127c8facc4";

        //act and assert
        mvc.perform(post("/api/v1/account/verification")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .param("token",token)
                .with(user("juan")
                        .password("juan2"))
                .servletPath("/api/v1")).andExpect(status().isOk());
    }

    @Test
    public void Should_AllowEnabledUserTo_EmailVerification() throws Exception {
        //arrange
        String token = "01854f09-742d-7d86-a3da-b0127c8facc4";

        //act and assert
        mvc.perform(post("/api/v1/account/verification")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .param("token",token)
                .with(user("juan")
                        .password("juan2")
                        .authorities(new SimpleGrantedAuthority("ENABLED_VERIFIED_USER")))
                .servletPath("/api/v1")).andExpect(status().isOk());
    }

    @Test
    public void Should_AllowNotRegisteredUserTo_EmailVerificationResend() throws Exception {
        //arrange
        String token = "01854f09-742d-7d86-a3da-b0127c8facc4";

        //act and assert
        mvc.perform(post("/api/v1/account/verification/resend").contentType(MediaType.TEXT_PLAIN_VALUE).param("token",token)
                .servletPath("/api/v1")).andExpect(status().isOk());
    }

    @Test
    public void Should_AllowRegisteredUserTo_EmailVerificationResend() throws Exception {
        //arrange
        String token = "01854f09-742d-7d86-a3da-b0127c8facc4";

        //act and assert
        mvc.perform(post("/api/v1/account/verification/resend")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .param("token",token)
                .with(user("juan")
                        .password("juan2"))
                .servletPath("/api/v1")).andExpect(status().isOk());
    }

    @Test
    public void Should_AllowEnabledUserTo_EmailVerificationResend() throws Exception {
        //arrange
        String token = "01854f09-742d-7d86-a3da-b0127c8facc4";

        //act and assert
        mvc.perform(post("/api/v1/account/verification/resend")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .param("token",token)
                .with(user("juan")
                        .password("juan2")
                        .authorities(new SimpleGrantedAuthority("ENABLED_VERIFIED_USER")))
                .servletPath("/api/v1")).andExpect(status().isOk());
    }

}