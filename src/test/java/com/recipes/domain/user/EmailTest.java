package com.recipes.domain.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

    @Test
    void constructorShouldSucceedForValidEmail() {
        //Arrange + Act
        Email email = new Email("test@gmail.com");

        //Assert
        assertEquals("test@gmail.com", email.email());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "test123.pt",
            "@test123.com",
            "test123@@com",
            "test123@.o/la",
            "test/123/@.com",
            "test@.",
            "test 123@gmail.com",
            "test123@gmail .com",
    })
    void constructorShouldThrowForInvalidEmailFormat(String invalidEmail) {
        //Act + Assert
        assertThrows(UserException.class, () -> new Email(invalidEmail));
    }

    @Test
    void constructorShouldThrowWhenEmailIsNull() {
        //Arrange
        String email = null;

        //Act + Assert
        assertThrows(UserException.class, () -> new Email(email));
    }

    @Test
    void constructorShouldThrowWhenEmailIsBlank() {
        //Arrange
        String email = " ";

        // Act + Assert
        assertThrows(UserException.class, () -> new Email(email));
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

    @Test
    void equalsAndHashCodeShouldBeBasedOnNormalizedValue() {
        //Arrange
        Email email1 = new Email("Test@Gmail.com");
        Email email2 = new Email(" TEST@GMAIL.COM ");
        Email email3 = new Email("other@gmail.com");

        //Act + Assert
        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
        assertNotEquals(email1, email3);
        assertNotEquals(email1.hashCode(), email3.hashCode());
    }

    @Test
    void toStringShouldContainTheEmailValue() {
        //Arrange
        Email email = new Email("test@gmail.com");

        //Act + Assert
        assertEquals("Email[email=test@gmail.com]", email.toString());
    }
}
