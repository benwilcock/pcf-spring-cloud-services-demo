package io.pivotalservices.coverservice;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles(value = {"test"})
@TestPropertySource("/application-test.properties") // The "/" denotes root of classpath.
public class CoverServiceControllerCoreSpringTest {

    @Value("${cover.service.cover-types}")
    String cover;

    @Autowired
    CoverServiceController coverServiceController;

    @Test
    public void testGetMyCovers(){
        // Arrange

        // Act
        String answer = coverServiceController.getCovers();

        // Assert
        Assert.assertEquals(cover, answer);
    }

    @Configuration
    static class Config {

        @Bean
        public CoverServiceController coverServiceController(){
            return new CoverServiceController();
        }

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertiesResolver() {
            return new PropertySourcesPlaceholderConfigurer();
        }


    }
}
