package io.pivotalservices.coversconsumer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by benwilcock on 30/11/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CoversServiceJUnitOnlyTest {

    @Mock
    RestTemplate mockRestTemplate;

    @Test
    public void testGetCovers(){
        //Arrange
        given(mockRestTemplate.getForObject(any(URI.class), any())).willReturn("Blah");
        CoverService service = new CoverServiceImpl();

        //Act
        //String answer = service.getCovers();

        //Assert
        //verify(mockRestTemplate).getForObject(any(URI.class), any());
        //Assert.assertEquals("Blah", answer);
    }
}
