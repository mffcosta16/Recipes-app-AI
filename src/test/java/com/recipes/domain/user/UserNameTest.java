package com.recipes.domain.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserNameTest {

    @Test
    void constructorShouldSucceedForValidUserName() {
        //Arrange + Act
        UserName userName = new UserName("TestUserName123");

        //Assert
        assertEquals("testusername123", userName.name());
    }

    @Test
    void constructorShouldSucceedWhenUserNameIsExactlyMaxLength() {
        //Arrange
        String name = "a".repeat(20);

        //Act
        UserName userName = new UserName(name);

        //Assert
        assertEquals(name, userName.name());
    }

    @Test
    void constructorShouldThrowWhenUserNameExceedsMaxLengthByOne() {
        //Arrange
        String name = "a".repeat(21);

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

    @Test
    void equalsAndHashCodeShouldBeBasedOnNormalizedValue() {
        //Arrange
        UserName name1 = new UserName("TestUser");
        UserName name2 = new UserName(" TESTUSER ");
        UserName name3 = new UserName("OtherUser");

        //Act + Assert
        assertEquals(name1, name2);
        assertEquals(name1.hashCode(), name2.hashCode());
        assertNotEquals(name1, name3);
        assertNotEquals(name1.hashCode(), name3.hashCode());
    }

    @Test
    void toStringShouldContainTheUserNameValue() {
        //Arrange
        UserName userName = new UserName("TestUser");

        //Act + Assert
        assertEquals("UserName[name=testuser]", userName.toString());
    }

}
