package io.pivotalservices.hystrix;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MicroserviceConsumerApplicationTests {


	private static final String EXPECTED = "Test Cover";

    @MockBean
    private CoverService coverService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testGetMyCovers(){
        given(this.coverService.getCovers()).willReturn(EXPECTED);
        String answer = restTemplate.getForObject("/mycovers", String.class);
        Assert.assertEquals(answer, EXPECTED);
    }

}
