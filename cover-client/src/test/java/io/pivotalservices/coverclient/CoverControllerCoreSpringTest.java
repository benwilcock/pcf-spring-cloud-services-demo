package io.pivotalservices.coverclient;

import com.netflix.governator.annotations.binding.Primary;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
@ActiveProfiles(value = {"test"})
public class CoverControllerCoreSpringTest {

    @Autowired
    private RestTemplate mockRestTemplate;

    @Autowired
    CoverClientController coverClientController;

    @Test
    public void testGetMyCovers(){
        // Arrange
        given(mockRestTemplate.getForObject(any(URI.class), any())).willReturn("Simulated Cover");

        // Act
        //String answer = restTemplate.getForObject("/mycovers", String.class);
        String answer = coverClientController.myCovers();

        // Assert
        Assert.assertEquals("Simulated Cover", answer);
    }

    @Configuration
    static class Config {

        @Bean
        @Primary
        public RestTemplate restTemplate(){
            return Mockito.mock(RestTemplate.class);
        }

        @Bean
        public CoverClientController coverClientController(){
            return new CoverClientController();
        }

        @Bean
        public CoverService coverService(RestTemplate restTemplate){
            return new CoverServiceImpl(restTemplate);
        }

    }
}
