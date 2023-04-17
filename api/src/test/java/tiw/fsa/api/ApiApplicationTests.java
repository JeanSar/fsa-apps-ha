package tiw.fsa.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import tiw.fsa.api.encryption.KeyService;
import tiw.fsa.api.user.*;

import java.util.Base64;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiApplicationTests {
    private static final String ADMIN_LOGIN = "test_admin";
    private static final String ADMIN_PWD = "adminpwd";
    private static final String NORMAL_LOGIN = "test_normal";
    private static final String NORMAL_PWD = "normalpwd";
    private static final String OTHER_LOGIN = "test_other";
    private static final String OTHER_PWD = "test_other_password";
    private static final String NORMAL_KEY1 = "test_normal_key1";
    private static final String NORMAL_KEY2 = "test_normal_key2";

    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private KeyService keyService;

    private TestRestTemplate testRestTemplate;
    private TestRestTemplate testRestTemplateAdmin;
    private TestRestTemplate testRestTemplateNormal;
    @Value(value = "${local.server.port}")
    private int port;
    private String testUrl;
    @Value("${tiw.fsa.api.worker.url}")
    private String workerUrl;

    @BeforeEach
    public void beforeEach() throws UtilisateurIncompletException, UtilisateurNotFoundException {
        testUrl = "http://localhost:" + port + "/";
        utilisateurService.createOrUpdateUtilisateur(
                new UtilisateurDTO(ADMIN_LOGIN, ADMIN_PWD, Utilisateur.ADMIN));
        utilisateurService.createOrUpdateUtilisateur(
                new UtilisateurDTO(NORMAL_LOGIN, NORMAL_PWD, Utilisateur.NORMAL));
        keyService.createKey(NORMAL_LOGIN, NORMAL_KEY1);
        testRestTemplate = new TestRestTemplate();
        testRestTemplateAdmin = new TestRestTemplate(ADMIN_LOGIN, ADMIN_PWD);
        testRestTemplateNormal = new TestRestTemplate(NORMAL_LOGIN, NORMAL_PWD);
    }

    @AfterEach
    public void afterEach() {
        keyService.deleteUserKeys(NORMAL_LOGIN);
        utilisateurService.deleteUtilisateur(ADMIN_LOGIN);
        utilisateurService.deleteUtilisateur(NORMAL_LOGIN);
        utilisateurService.deleteUtilisateur(OTHER_LOGIN);
    }

    private final BooleanSupplier workerIsUp = () -> {
        try {
            var result = testRestTemplate.getForEntity(workerUrl + "/liveness", String.class);
            return result.getStatusCode().is2xxSuccessful();
        } catch (ResourceAccessException e) {
            return false;
        }
    };

    @Test
    void contextLoads() {
    }

    @Test
    public void testUnauthenticatedCantAccess() {
        ResponseEntity<String> result = testRestTemplate.getForEntity(testUrl + "user", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }

    @Test
    public void testAdminCanCreateUser() {
        var newUser = new UtilisateurDTO(OTHER_LOGIN, OTHER_PWD, Utilisateur.NORMAL);
        ResponseEntity<String> result =
                testRestTemplateAdmin.postForEntity(testUrl + "user", newUser, String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        result = testRestTemplateAdmin.getForEntity(testUrl + "user/{login}", String.class, OTHER_LOGIN);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    public void testNormalCanNotCreateUser() {
        var newUser = new UtilisateurDTO(OTHER_LOGIN, OTHER_PWD, Utilisateur.NORMAL);
        ResponseEntity<String> result =
                testRestTemplateNormal.postForEntity(testUrl + "user", newUser, String.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void testNormalCanUpdateOwnPassword() {
        var newPassword = "newpwd";
        var newNormal = new UtilisateurDTO(NORMAL_LOGIN, newPassword, null);
        ResponseEntity<String> result =
                testRestTemplateNormal.postForEntity(testUrl + "user", newNormal, String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        result = testRestTemplateNormal.getForEntity(testUrl + "/user/{login}", String.class, NORMAL_LOGIN);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        var newTpl = new TestRestTemplate(NORMAL_LOGIN, newPassword);
        result = newTpl.getForEntity(testUrl + "/user/{login}", String.class, NORMAL_LOGIN);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testNormalUserCanCreateAndGetKey() {
        var resultCreate = testRestTemplateNormal.postForEntity(testUrl + "/key/{login}/{keyname}", "", String.class, NORMAL_LOGIN, NORMAL_KEY2);
        assertTrue(resultCreate.getStatusCode().is2xxSuccessful());
        var resultGetKeys = testRestTemplateNormal.getForEntity(testUrl + "/key/{login}", String[].class, NORMAL_LOGIN);
        assertTrue(resultGetKeys.getStatusCode().is2xxSuccessful());
        assertNotNull(resultGetKeys.getBody());
        assertEquals(2, resultGetKeys.getBody().length);
    }

    @Test
    public void testEncryptDecrypt() {
        Assumptions.assumeTrue(workerIsUp,"Worker is down");
        String data = Base64.getEncoder().encodeToString("abcdef".getBytes());
        var encRes = testRestTemplateNormal.postForEntity(
                testUrl+"/crypt/{login}/{keyname}/encrypt",data,String.class,NORMAL_LOGIN,NORMAL_KEY1);
        assertTrue(encRes.getStatusCode().is2xxSuccessful());
        var encrypted = encRes.getBody();
        assertNotNull(encrypted);
        var decRes = testRestTemplateNormal.postForEntity(
                testUrl+"/crypt/{login}/{keyname}/decrypt",encrypted,String.class,NORMAL_LOGIN,NORMAL_KEY1);
        assertTrue(decRes.getStatusCode().is2xxSuccessful());
        var decrypted = decRes.getBody();
        assertNotNull(decrypted);
        assertEquals(data,decrypted);
    }
}
