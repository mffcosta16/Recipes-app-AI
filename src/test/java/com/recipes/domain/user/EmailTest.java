package com.recipes.domain.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

    @Test
    void constructorForValidEmail() {
        //Arrange + Act + Assert
        new Email("test@gmail.com");

    }

    @Test
    void constructorForInvalidEmail() {
        //Arrange
        String email1 = "test123.pt";
        String email2 = "@test123.com";
        String email3 = "test123@@com";
        String email4 = "test123@.o/la";
        String email5 = "test/123/@.com";
        String email6 = "test@.";

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Email(email1));
        assertThrows(IllegalArgumentException.class, () -> new Email(email2));
        assertThrows(IllegalArgumentException.class, () -> new Email(email3));
        assertThrows(IllegalArgumentException.class, () -> new Email(email4));
        assertThrows(IllegalArgumentException.class, () -> new Email(email5));
        assertThrows(IllegalArgumentException.class, () -> new Email(email6));
    }

    @Test
    void constructorShouldThrowWhenEmailIsNull() {
        //Arrange
        String email = null;

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Email(email));
    }

    @Test
    void constructorShouldThrowWhenEmailIsBlank() {
        //Arrange
        String email = " ";

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new Email(email));
    }

    @Test
    void constructorShouldNormalizeEmailToLowerCase() {
        //Arrange
        String email1 = "TEST123@GMAIL.COM";
        String email2 = "TesTEmaIL@HoTmAiL.CoM";
        String email3 = " TEST@GMAIL.COM  ";

        //Act
        Email emailTest1 = new Email(email1);
        Email emailTest2 = new Email(email2);
        Email emailTest3 = new Email(email3);

        //Assert
        assertEquals("test123@gmail.com", emailTest1.email());
        assertEquals("testemail@hotmail.com", emailTest2.email());
        assertEquals("test@gmail.com", emailTest3.email());
    }
}
