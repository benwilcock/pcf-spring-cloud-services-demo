package io.pivotalservices.coverclient;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = {"test"})
public class CoversConsumerApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetMyCovers(){
        // Arrange

        // Act
        String answer = restTemplate.getForObject("/mycovers", String.class);

        // Assert
        Assert.assertEquals("Failsafe Cover", answer);
    }
}
