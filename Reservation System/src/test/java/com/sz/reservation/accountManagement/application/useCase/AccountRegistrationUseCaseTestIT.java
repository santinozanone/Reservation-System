package com.sz.reservation.accountManagement.application.useCase;


import com.sz.reservation.accountManagement.configuration.AccountConfig;
import com.sz.reservation.accountManagement.domain.model.Account;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.accountManagement.infrastructure.dto.AccountCreationRequest;
import com.sz.reservation.accountManagement.infrastructure.service.ScaledInstanceMultipartImageResizingService;
import com.sz.reservation.globalConfiguration.RootConfig;
import com.sz.reservation.globalConfiguration.exception.MediaNotSupportedException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RootConfig.class, AccountConfig.class})
@ActiveProfiles(value = {"test", "default"})
@DisplayName("Integration testing Account registration use case")
@TestPropertySource(properties = "account.localpfpstorage.location=${RS_LOCAL_PFP_STORAGE_LOCATION_TEST}")
class AccountRegistrationUseCaseTestIT {

    private static String pictureStoragePath = System.getenv("RS_LOCAL_PFP_STORAGE_LOCATION_TEST");
    @Autowired
    private AccountRegistrationUseCase accountRegistrationUseCase;
    @Autowired
    private AccountRepository repository;

    @BeforeAll
    public static void instantiatingStorage() throws IOException {
        Path storage = Path.of(pictureStoragePath);
        if (!Files.exists(storage)) {
            Files.createDirectories(storage);
        }
    }

    @AfterAll
    public static void deleteStorage() throws IOException {
        Path storage = Path.of(pictureStoragePath);
        if (Files.exists(storage)) {
            Files.delete(storage);
        }
    }

    @Test
    @Transactional
    public void Should_UploadProfilePicture_Correctly() throws FileNotFoundException {
        //arrange
        String accountId = createNotVerifiedAccount();
        String path = "src/test/resources/bird.jpg";
        InputStream inputStream = new FileInputStream(path);

        Assertions.assertTrue(new File(path).exists());

        //act
        accountRegistrationUseCase.uploadProfilePicture(accountId, "bird.jpg", inputStream);

        //assert
        File directory = new File(pictureStoragePath);
        File imageCreated = Objects.requireNonNull(directory.listFiles())[0];
        Assertions.assertTrue(imageCreated.exists());

        //cleaning
        deletePicture(imageCreated.getAbsolutePath());
    }

    @Test
    @Transactional
    public void Should_ThrowMediaNotSupportedException_When_FileExceedsLimit() throws FileNotFoundException {
        //arrange
        String accountId = createNotVerifiedAccount();
        String path = "src/test/resources/18MbImage.png";
        InputStream inputStream = new FileInputStream(path);

        Assertions.assertTrue(new File(path).exists());

        //act
        Assertions.assertThrows(MediaNotSupportedException.class, () ->{
            accountRegistrationUseCase.uploadProfilePicture(accountId, "18MbImage.png", inputStream);
        });
    }


    @Test
    @Transactional
    public void Should_ThrowMediaNotSupportedException_When_FileHasInvalidExtension() throws FileNotFoundException {
        //arrange
        String accountId = createNotVerifiedAccount();
        String path = "src/test/resources/bird.jpg";
        InputStream inputStream = new FileInputStream(path);

        Assertions.assertTrue(new File(path).exists());

        //act
        Assertions.assertThrows(MediaNotSupportedException.class, () ->{
            accountRegistrationUseCase.uploadProfilePicture(accountId, "bird.png", inputStream);
        });
    }

    @Test
    @Transactional
    public void Should_ThrowMediaNotSupportedException_When_FileHasMultipleExtensions() throws IOException {
        //arrange
        String accountId = createNotVerifiedAccount();
        String path = "src/test/resources/bird.jpg";
        InputStream inputStream = new FileInputStream(path);

        Assertions.assertTrue(new File(path).exists());

        //act
        Assertions.assertThrows(MediaNotSupportedException.class, () ->{
            accountRegistrationUseCase.uploadProfilePicture(accountId, "bird.jpg.png", inputStream);
        });
    }

    @Test
    @Transactional
    public void Should_ThrowMediaNotSupportedException_When_FileIsEmpty() {
        //arrange
        String accountId = createNotVerifiedAccount();
        InputStream emptyStream = new ByteArrayInputStream(new byte[0]);

        //act
        Assertions.assertThrows(MediaNotSupportedException.class, () ->{
            accountRegistrationUseCase.uploadProfilePicture(accountId, "bird.jpg.png", emptyStream);
        });
    }




    private void deletePicture(String pictureStoragePath) {
        File file = new File(pictureStoragePath);
        boolean succeeds = file.delete();
        Assertions.assertTrue(succeeds);
    }

    private String createNotVerifiedAccount() {
        String email = "email@email.com";
        accountRegistrationUseCase.registerNotEnabledUser(new AccountCreationRequest(
                "username",
                "name",
                "surname",
                email,
                "54",
                "1150000000",
                LocalDate.now().minusYears(20),
                "argentina",
                "password"));

        Optional<Account> optionalAccount = repository.findAccountByEmail(email);
        Assertions.assertTrue(optionalAccount.isPresent());
        return optionalAccount.get().getId();
    }


}