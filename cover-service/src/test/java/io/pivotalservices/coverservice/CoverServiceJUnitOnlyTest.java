package io.pivotalservices.coverservice;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by benwilcock on 30/11/2016.
 */

public class CoverServiceJUnitOnlyTest {

    @Test
    public void testGetCovers(){
        //Arrange
        CoverServiceController controller = new CoverServiceController();

        //Act
        String answer = controller.getCovers();

        //Assert
        Assert.assertEquals(null, answer);
    }
}
