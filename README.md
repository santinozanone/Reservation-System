# Reservation-System
## Purpose
The intention behind this project is to put together everything i have learnt and make this application as professional as possible, in order to be hired as a jr Java developer.

# Steps of development
## 1. Analysis and design
   * In this step we will gather as much information as we can in order to understand the domain and the system we are gonna build.  
   We will accomplish this by working closely with the **stakeholders** and then will:
     * Define the requirements
     * Identify the actors
     * Define the use cases or user stories (still in debate)
     * Create a DFD for describing the flow of the program.
     * Use UML for modelling the java classes.
    
## 2. Define the Stack and architecture to be used. 
   * In this case we will use Spring (**not Boot**) and Hexagonal Architecture (Ports and adapters)
   * The Spring modules we will work with are:
       * Spring MVC
       * Spring Data Access (JDBC)
       * Spring Data Access (Transaction Management)

## 3. Implementation
* After the design phase is completed, we will begin coding the system by following the defined requirements and architecture.

## 4. Testing
   * Will make Unit tests at the beginning

## 5. Deployment
   * Yet to define

&nbsp; 

# Development Log
### 20/01/2025
This was the first day of development.
#### **What was done today:** 
   * Created this Readme
   * Created the Github repo
   * Did some investigation about which git workflows to use.

   **Challenges:**
   * None 
---
### 21/01/2025
#### **What was done today:** 
   * Investigate about the domain, looking at examples like booking.com or Airbnb
   * Created the jira project
      * Added the Epics
      * Added the User stories
      * Added some tasks

   * Researched about the best way to store images in a DB, concluded that a **indirect   
   approach** will be used (the path of the image will be stored in the db, not the blob)   
   due to its better performance.

   * Researched about whether to resize the images that will be uploaded or not (**still in discussion**)

   * Read and understood Spring Security Documentation

   * Read about session and cookie storage, and its impact in API Restfullness

   ---
### 22/01/2025
#### **What was done today:** 
   * Started developing the schema for the user entity and realized that storing the phone  
   number isnt as easy as it looks, so i did more research in the topic and found about the [**E.164 Standard**](https://www.twilio.com/docs/glossary/what-e164),   
   which defines a general format for storing international phone numbers. Also found a google library called   
   [**libphonenumber**](https://github.com/google/libphonenumber) that allows  parsing, formatting, and validating international phone numbers.  
   However this wasnt finished yet because in my country (Argentina), besides having to conform with the standard ,   
   its also necessary to append a " 9 " before the phone number if you are calling from another country.  
   So an Argentinian number like this:   
   **11 1234 5678** will be formatted to :  
   **+54 9 11 1234 5678**  
   Even Whatsapp has a link talking about it: https://faq.whatsapp.com/1294841057948784/?cms_id=1294841057948784&draft=false  
   
   * With this in mind the following schema was created:
      * #### **Still need to redefine the primary keys**
   * ![](image.png)
   

   * After having the diagram i finished the use case for the registration functionality.

   * Then realized that the diagram was incomplete so i modified it
   ![alt text](image-1.png)

   * Spent a lot of time reading through java email api and how to use it for sending emails.
   * Decided that will use the [**Gmail Api**](https://developers.google.com/gmail/api/guides/sending), which is free and allows me to send the verification email
   * Continue investigating about Hexagonal architecture and how to organize the package of the application  
   >A lot of the examples i read about hexagonal architecture were using a layered-like approach for organizing the packages,
   where they would have this global packages, like infrastructure,core,etc and put everything in there, i wanted to do something different
   , so i found about package by feature and tried to mix it with the concept of hexagonal.  
   What i will have is: \
   1 - A configuration package containing all the spring configuration.  
   2 - A package for every feature to implement, like login,registration,reservation,etc.
   And inside those packages will be the core, infrastructure and adapters packages.  
   3 - A common or util package for common functionality,
   
Using the prior package structure,i finally started:  
>com.sz.reservation
com.sz.reservation.configuration  -> spring dispatcherServlet and config classes are here.  
com.sz.reservation.registration ->  the registration feature package, contains the core, infrastructure and adapters
com.sz.reservation.registration.core -> contains the PhoneNumber and User classes.
    
   - That is everything i got by now, didnt have time for making the uml so i will paste an screenshot of the package structure and classes here.
      
      ![alt text](image-5.png)
      ![alt text](image-2.png)
      ![alt text](image-3.png)
      

---


### 27/01/2025
In the last days i have been researching and trying to implement the registration feature,  
and found a lot of things to implement. here is a summary of what i did,
* Created the ProfilePicture class
* Learnt about multipart in http
* Configured a multiPartResolver in order to be able to handle this type of file
* Implemented a basic controller for receiving a multipart file
* Realized that i have to verify the real data type of the multipart, because cannot trust
  the client
* Upon researching, found about [Apache Kita](https://tika.apache.org/), which is a content analysis toolkit, and could help me  
  implement what i needed.
* Read the documentation with some examples online, and implemented a FileValidator, which is in a util package  
  at the root level, given that it will be used for different features of the app.
* I didnt like the domain model and how the request data would be binded, so i created a dto called UserRequest,  
  which has all the parameters necessary for creating an user.
* Added validation annotations to the UserRequest.
* I continued reading and trying to adhere to hex architecture as much as possible , so , after modifying the package structure 3 times, it looks like this now: It still can and will be improved as time goes by
![alt text](image-4.png)
* Added a custom exception for handling multipart types that are not allowed,  with a global exception handler
* Implemented the registration use case which has dependencies on the outbound adapters
* Implemented the outbound adapters


#### **What was done today:** 
Today i spent all day learning about testing, more precisely, Unit testing, although i still read about integration testing.
but my focus was on, learn how to use JUnit5 with Spring mvc.
* I read the documentation
* Researched on the internet
* Learned about mocks and [Mockito](https://site.mockito.org/)
* Installed the necessary dependencies using maven
* Did some basic tests and then tried to test my FileValidator class
* At first i wanted to mock my file in order to pass the inputStream to the validator, however,  
  that would be too complex and cannot guarantee that the content inside it is correct.
* So what i did was, use the @BeforeEach and @AfterEach annotations , and a global attribute of type File.
  This assures that before each test is ran, a new file will be created, and after, will be deleted.
* The tests worked perfectly, and did the first commit (i had forgotten to do so) hehe.
* Here is the test:
```java
@ExtendWith(MockitoExtension.class)
class TikaFileValidatorTest {
    private File outputFile;

    @BeforeEach
    public void createPngFile(){
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, 0);
        outputFile = new File("saved.png");
        try {
            Assertions.assertTrue(ImageIO.write(image, "png", outputFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void removePngFile(){
        Assertions.assertTrue(outputFile.delete());
    }

    @Test
    public void Should_ReturnRealMediaType_When_ValidInputStream() throws IOException {
        Path tempPath = Path.of(outputFile.getAbsolutePath());
        FileValidator fileValidator = new TikaFileValidator();
        MediaType type = fileValidator.getRealFileType(Files.newInputStream(tempPath, StandardOpenOption.READ));
        Assertions.assertEquals(MediaType.IMAGE_PNG, type);
    }

    @Test
    public void Should_ReturnRealMediaType_When_ExtensionOfFileIsChanged() throws IOException {

        File newFile = new File("saved2.jpeg");
        Assertions.assertTrue(outputFile.renameTo(newFile));
        Path tempPath = Path.of(newFile.getAbsolutePath());
        FileValidator fileValidator = new TikaFileValidator();
        MediaType type  = fileValidator.getRealFileType(Files.newInputStream(tempPath, StandardOpenOption.READ));
        Assertions.assertEquals(MediaType.IMAGE_PNG, type);
        outputFile = newFile;

    }
}
```
---
### 29/01/2025
#### **What was done today:** 
* Wrote more tests for the tika file validator
* Used spring test dependency for mocking a multipart file
* Refactored the Registration Use case by transfering the input Stream handling to the file validator class
* Refactored the tika validator test to make them clearer.
* Added a @DisplayName annotation into the test class
* Added @TestMethodOrder(MethodOrderer.OrderAnnotation.class), which gave me the capability of selecting the execution order.
* Finished the profile picture validation, the only thing left is check the dimension of the photo and compress it or resize it as necessary
* Created a new Exception (EmptyMediaException)
* Created the test class for the registration use case
* Added the (guava)[]library for obtaining the extensions of Files


---
### 31/01/2025
#### **What was done today:** 
* Deleted the EmptyMediaException class, only one exception towars media will be created, MediaNotSupportedException
* Refactored the infrastructure package, by creating a new subpackage ,adapters,where i have the inbound and outbound.
* Added a dto package inside the infrastructure one
* Added a service package inside the infrastructure package
* Created a service package inside the domain, which will hold the domain services
* Created the userCreationService inside the domain services package, responsible for guaranteeing the creation of the user entity in a valid state
* Created a hashing service interface in the domain service package, which will be used for hashing the passwords.
* Created the hashing implementation inside the infrastructure services using bcrypt library from spring Security
* Moved the profile picture validator from the application package to the domain services package, given that i think its part of domain or bussiness validation rather than application, it still can be argued and will as the project grows.
* Modified the phone number and user request class by instead of using 3 attributes for storing the phone number, using 2, country code, and PhoneNumber(which will include area code)
* Added an Email attribute in UserRequest and in user because i had forgotten.
* Changed the name of RegistrationController to HttpRegistrationController
* Tried to implement the libphonenumber library from google but now i have some doubts about my validation in the domain, because i am having a debate wheter to put the validations, if inside the PhoneNumber VO(value object) by injecting a validator into it or by having a validator which will be used by  my userCreationService class,
  the cons with this approach is that allows the creation of a PhoneNumber in invalid state. 
  And if i inject a dependency into a VO ,supposedly is a bad practice, i could define an interface in the same domain package and implement it in the infrastructure package that way the inversion of control is still there, i will check that tomorrow.
* Modified the UserRegistrationDb interface so instead of returning an user it returns an Optional< User>

---
### 01/02/2025
#### **What was done today:** 
* Added the libphonenumber dependency in the pom.xml
* Created the PhoneNumberValidator interface in domain.service package
* Implemented PhoneNumberValidator using libphonenumber in infrastructure.service package
* Created and implemented test class and methods for libphonenumber
* Moved the port package from application to domain
* Moved Exception package from registration.exception to domain.exception
* Moved the globalExceptionHandler from .exception to root package (reservation)
* Changed the name of userCreationService to UserService
* Modified the UserService so it only depends on interfaces from the same domain, allowing to use dependency inversion principle
* Modified the createUser method by renaming it to create.
* Modified the PhoneNumber class so it implements my bussiness invariant, which is that if a number is argentinian , it must have a "9" after the country code and before the area code
* Spent the rest of the day researching in how to handle some invariants of the code, for example i dont like the solution of handling the uniqueness of my users in the domain service,
  and i think its a concern that maybe should not be there, i have some different approaches, like instead of having that domain service for creating the entities and allowing the domain to
  be created in invalid states, i could enforce to pass a validator instance when creating the user class, that validator would contain an instance to the repository, however this would create an additional coupling
  i dont think should be necessary, and it would be harder to test. Other approach would be or handling all directly in the application service, which i dont want to do because i feel that the app service is doing too much
  and it makes an anemic like domain, or, make the necessary call in the app service and pass the data necesary to the domain service.
  Even though this approaches could work, we are not taking in account the concurrency issues that could happen , assume that two users when retrieving data from the repository get that their email or username has NOT been used,
  that means that the creation of the User object is totally possible because they unique, but user 1 finishes firts, then user2 doesnt know that user1 finished, so know user1 has an entity in an invalid state.
  The solution we can apply are diverse. We could use locking inside a transaction for maximun security, but that would be overkill and could cost a lot of perfomance.
  So what i will do is let the repository handle it, by making the fields in the DB, UNIQUE, so when 2 identical emails or usernames try to insert, the db will throw an exception, which will handled by my application.
  I arrived at this thinking after a long day reading through stackOverflow and different sites, (i have 10 google chrome windows open), and deciding that is not a bussiness rule that the username or email must be unique, or, even if it was,
  the overhead of handling it, isnt worth it, at least to me, and, by now.  
  **Evidence of my chrome windows:**
  ![alt text](image-6.png)

---

### 02/02/2025
#### **What was done today:** 
* Created the resizing image service.
* Added a private getExtension method to the fileValidator.
* Eliminated the domain service for user creation to the application services directly.
* Created the output profilePictureStorage port.
* Implemented the ProfilePictureStorage port in the infrastructure service by  storing files in a folder outside the project directory.
* Created the test class for ProfilePictureStorage.
* Added a Test to ProfilePictureStorage.
* Changed the ProfilePicture profileImage to a Image type.
* Added the necessary dependencies in the registrationUseCase controller.
* Created the necessary beans in the root application config.
* Modified the PhoneNumberValidator to accept the countrycode and phone number as different variables and not encapsulated in the PhoneNumber object
* Deleted the ID attribute from user
* Deleted the id attribute from PhoneNumber
* Modified the profile picture validator so it also validates the extension of the file to the real content of the file.

---

### 03/02/2025
#### **What was done today:** 
* Finished the profile picture storage functionality, now each profile picture is stored in a common folder in the file system.
* Added tests to the profile picture storage.
* Modified the root configuration, so now beans inside infrastructure package will be annotated with @Component,@Service,etc.
* Modified the generateProfilePicture method, so now it also appends a random uuid to the date.
* Added an application.properties file.
* Added a localpfpstorage.location attribute to the properties file, containing the path where profile pictures will be stored.
* Refactored the LocalSystemProfilePictureStorage by creating smaller private methods.
* Added the @WebAppConfiguration and @ContextConfiguration to the LocalSystemProfilePictureStorageTest so i can access the application context.
* Finally, read a bit about transaction handling and if should i use declarative or programming, i will test declarative first because its "coupled" and doesnt add so much boilerplate code.

---

### 06/02/2025
#### **What was done today:** 
* in the last days i have modified the domain, now there is an account class which contains the data and behaviour of an account as change password,pfp,username, etc.
it also contains fields not related to the behaviour of the methods like nationality which doesnt affect anything, however after a lot of thinking and exploring the possibilities and trade offs 
i opted for having all account related data in the account class and not separate it , at least by now.
because it would involve more complexity in the object creation and in the repository.
with only account aggregate, everything becomes easier, at the cost of loading 3 or more VO, which should cause a performance bottleneck.
and if it does, well, we can refactor that later and maybe split into account and profile info.
Then we have accountCreationData which contains the data necessary for creating an account, except the profile picture, which will be passed as parameter to the domain service.

* Today implemented and added unit test to the email sending functionality, at first i start reading about google gmail api, but i found it more complex than necessary and i needed something simpler.
because gmail api uses OAuth, and that is because its made for an application that wants to act in behalf an user,
so its gives YOUR app the possibility to send emails.  
What people do is, they log in with the account they want to send mails with, so now the gmail api thinks your app can send mails like its you.
The problem with this is that the token expires and you have to renovate it, i didnt understand if it expires in one week or one hour, but anyways, you have to renovate it.
So then i found Twillio SendGrid which gives me an API and a java library for sending emails.  
it also gives me templates so i can send pretty emails.
After i created the api key and authenticate my gmail mail direction then i could send emails from my gmail account.  

* Obviously for implementing this, i created the necessary port in the domain and the adapter in the infrastructure, as well as storing the api key in the properties file, which wont go into github.
***This is the template i use for sending emails***

   ![alt text](image-7.png)


### 07/02/2025
#### **What was done today:** 
* Basically all that was done today was, deciding about stateless or statefullness session (will use stateless by now),authentication mechanism(will use base auth by spring security) and researching about the best way to create and handle uuids in mysql, given that just creating a random uuid (V4) and storing it as a char is a total performance threat
 i still need to read about how mysql stores the data, i know that they use b trees, but didnt quite understand some things like why it needs to be ordered and etc.
 But i found the solution, basically will use a V1 UUID, and will re arrange the timestamp part so it can be ordered, and will be stored in binary, not char. this supposedly gives a huge performance boost(which is more noticeable in bigger tables).   
 I will also take a look at sequential uuid generation, and make some benchmarks using ordered vs unordered uuid.  
 i will leave the links here:  
 * https://stitcher.io/blog/optimised-uuids-in-mysql
 * https://web.archive.org/web/20160209174810/http://mysqlserverteam.com/storing-uuid-values-in-mysql-tables/  
 * https://www.percona.com/blog/uuids-are-popular-but-bad-for-performance-lets-discuss/  
 * https://www.percona.com/blog/store-uuid-optimized-way/  
 * https://www.reddit.com/r/PostgreSQL/comments/mi78aq/any_significant_performance_disadvantage_to_using/
 * https://github.com/tvondra/sequential-uuids
 * https://www.pingcap.com/article/mastering-uuid-storage-in-mysql/
 * https://medium.com/@hnasr/postgres-vs-mysql-5fa3c588a94e  
 * https://www.percona.com/blog/illustrating-primary-key-models-in-innodb-and-their-impact-on-disk-usage/
 * https://dev.mysql.com/blog-archive/mysql-8-0-uuid-support/  
 * https://planetscale.com/blog/the-problem-with-using-a-uuid-primary-key-in-mysql
 * https://www.bytebase.com/blog/choose-primary-key-uuid-or-auto-increment/
 * https://stackoverflow.com/questions/40733975/how-does-non-sequential-eg-uuid-guid-data-degrade-index-performance?rq=3  
 * https://mariadb.com/kb/en/guiduuid-performance/
 * https://www.itdo.com/blog/ulid-vs-uuid/
 * https://www.baeldung.com/cs/ulid-vs-uuid
 * https://laracasts.com/discuss/channels/general-discussion/ulid-experiences-opinions?page=1&replyId=862458
 * https://adileo.github.io/awesome-identifiers/



### 10/02/2025 and 11/02/2025
#### **What was done today:** 
 * I think i finally understood how table data is stored in mysql. first of all it uses B+ trees and not B tree, the difference is that in b tree the intermediate nodes contain key and value while in B+ tree only the leaf nodes contain the values while the others contain only the key, and also all the leaf nodes are linked betweem then, like a linked list.  
 Mysql in the default mode stores the table's data in a .ibd file called the ***file per table tablespace*** containing the data and index of the table.
 ![alt text](image-8.png)
 
 the data is stored in a segment, which contains different extents, extends are a group of pages, normally of 1MB, pages default size is 16KB so an extent can contain maximun 64 pages.

 So in this b + tree the internal nodes contain the keys while the leafs contain the actual record.  
 Mysql uses a clustered index which means that the records are ordered using a primary key value, thats why is important and why an auto increment primary key helps to improve performance.
 ***the B+ Tree is the clustered index as i have understood.***

 So in summary, an index is a data structure used for quickly row fetching, mysql uses a clustered index ( index whose order of the rows in the data pages corresponds to the order of the rows in the index) in form of a B+ tree for storing data more efficiently. this data is stored in the .ibd file as explained above.  
 ***There can only be one clustered index in a table.***  
 We also have non clustered index which are the secondary indexes of a table, this are stored as a separate B+ tree in the same **.ibd** file as the PK index, but in this case the leaf contains the value of the column where the index was and the pk.
 Here is an example.  
 * A search is performed on the secondary index B+tree.  
 * The primary keys for matching results are collected.
* These are then used to do additional B+tree lookup(s) on the main table B+tree to then find the actual row data.

**Thats why when using UUID we need to make them "sequential" by changing the datetime order, so then it will not cause page splits and fragmentation, because data rows will be in "order", and mysql would not need to balance the B+ Tree that often, or make page splits that frequently.**

   

### 12/02/2025
#### **What was done today:** 
* I made some benchmarks using UUID V7 (thats it, the timestamp ordered so it generates "sequential" ids), autoIncrement integers and non ordered Random UUID,
  the results were pretty good while using autoincrement and "sequential" V7 UUID, while using random UUID were pretty bad.
  what i did was make a test class and one test method which would insert 1000,10.000,100.000,and 1.000.000 rows of data in the account table.
  Obviously this is not a real case scenario given that it didnt take in account the use of different threads and the use of the connection pool, however it can give me an insight of the performance different between
  the ids.
  I started making the updates inside a for loop but that isnt the best way for making big inserts, so instead i made use of the batchUpdate of the jdbc template.
  i forgot to said that i used HikariCP which is a connection pool for jdbc. supposedly is the most performant one.
  The inserts with the sequential UUID and the autoIncrement Id were almost the same, taking 23 seconds to insert 1 million rows and 5 seconds to insert 100.000 rows, however the random uuid was a disaster, taking 
  28 seconds to insert 10.000 rows.
  If i hadnt used hikariCp the results would had been a lot worse.

* I refactored the domain, deleting the accountCreationService and created a dto in the application package,
  when users send a request to login the application service now is in charge of all validation and then creates the AccountCreationData,it also creates the ids for the entities that require them,
  then it stores the image on disk,store it on db, and sends the email with the UUID asigned for verification.

* Now the account class will be an aggregate containing different behaviour like change name,password,profile Picture, etc.

* Also finished the UserRegistrationDb class  



---

### 13/02/2025
#### **What was done today:** 
* Refactored the domain model and the application module, there is no accountCreationService, there is a class in the app module that creates a AccountCreationData dto which is used in the repository
  for inserting a new account.
* Enable  transaction management by creating a transaction manager bean.
* Created the account verification token class.
* Created the email verification token for the users.
* Modified the db schema by adding a verification_token table.
* Added validation in the domain model entities.
* Created new RuntimeException to be used in the model and application.
* Modified the UserRegistrationDb code to make the inserts in the necessaries tables.
* Fixed a bug in the ProfilePicturePathName generator method (i had forgotten to add "." before the extension)
* Changed java.util.date for LocalDate
* Created the bean for the new AccountCreationData class.
* Modified the Account class so it converts into an aggregate and only contains data used for its behaviour.
* Added regex validation for email in the account class and request class from the http controller.
* Added a test class for the creation of an account.

### 14/02/2025
#### **What was done today:** 
* Fixed a bug in the accountcreationrequest validation
* Added exception handling in the globalExceptionHandler for all exceptions.
* Modified the resize class in order to accept a multipart file
* Fixed a bug in the filetype validator
* Created exception for email already in use
* Implemented exception handling for duplicated username or email
* Created a special annotation for not allowing whitespaces in string variables
* Created an exception package inside the infrastructure package, containing all the exceptions related to infra.
* Created exceptions for, network error, directory creation, SendGrid Email api errors, file reading and file writing errors.
* Changed checked exceptions to unchecked ones, and added handler in the globalExceptionHandler
* Implemented registration Use case test.
