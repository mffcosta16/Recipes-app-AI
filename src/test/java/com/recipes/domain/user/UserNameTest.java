package com.recipes.domain.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserNameTest {

    @Test
    void constructValidUserName() {
        //Arrange + Act + Assert
        new UserName("TestUserName123");

    }

    @Test
    void constructorShouldThrowForInvalidUserName() {
        //Arrange
        String name = "Testing123InvalidUsername";

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new UserName(name));
    }

    @Test
    void constructorShouldThrowWhenUserNameIsNull() {
        //Arrange
        String name = null;

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new UserName(name));
    }

    @Test
    void constructorShouldThrowWhenUserNameIsBlank() {
        //Arrange
        String name = "";

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new UserName(name));
    }

    @Test
    void constructorShouldNormalizeUserNameToLowerCase() {
        //Arrange
        String name = " TESTINGUSERNAME  ";
        String name1 = "TeSt123HelLo";

        //Act
        UserName username = new UserName(name);
        UserName userName1 = new UserName(name1);

        //Assert
        assertEquals("testingusername", username.name());
        assertEquals("test123hello", userName1.name());
    }

}
