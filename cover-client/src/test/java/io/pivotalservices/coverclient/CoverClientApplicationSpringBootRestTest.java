package io.pivotalservices.coverclient;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = {"test"})
public class CoverClientApplicationSpringBootRestTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    CoverClientController coverClientController;

    @Value("${cover.client.failsafe.cover-types}")
    String failsafeCoverTypesFromApplicationProps;

    @MockBean
    RestTemplate mockRestTemplate;

    @Bean
    public AlwaysSampler defaultSampler() {
        return new AlwaysSampler();
    }

    @Test
    public void testGetMyCover(){
        // Arrange
        String fakedResultForCovertypesCall = "fakedResultForCovertypesCall";
        given(mockRestTemplate.getForObject(any(URI.class), any())).willReturn(fakedResultForCovertypesCall);

        // Act
        String answer = testRestTemplate.getForObject("/mycovers", String.class);

        // Assert

        // You'll get the faked results from the Mock of the RestTemplate
        Assert.assertEquals(fakedResultForCovertypesCall, answer);
    }

    @Test
    public void testCircuitBreakerWorks(){
        // Arrange
        given(mockRestTemplate.getForObject(any(URI.class), any())).willThrow(new RuntimeException("HaHa"));

        // Act
        String answer = testRestTemplate.getForObject("/mycovers", String.class);

        // Assert
        // You'll get the failsafe because the call to the COVER-SERVICE faked an exception.
        Assert.assertEquals(failsafeCoverTypesFromApplicationProps, answer);
    }
}
