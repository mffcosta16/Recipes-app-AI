package com.recipes.domain.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {

    @Test
    void constructorShouldSucceedForValidEmail() {
        //Arrange
        var email = new Email("test123@gmail.com");

        //Act
        var userId = new UserId(email);

        //Assert
        assertEquals(email, userId.email());
    }

    @Test
    void constructorShouldThrowWhenIdIsNull() {
        //Act + Assert
        assertThrows(UserException.class, () -> new UserId(null));

    }

    @Test
    void equalsAndHashCodeShouldBeBasedOnEmail() {
        //Arrange
        UserId id1 = new UserId(new Email("test@gmail.com"));
        UserId id2 = new UserId(new Email(" TEST@GMAIL.COM "));
        UserId id3 = new UserId(new Email("other@gmail.com"));

        //Act + Assert
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }

    @Test
    void toStringShouldContainTheEmail() {
        //Arrange
        UserId userId = new UserId(new Email("test@gmail.com"));

        //Act + Assert
        assertEquals("UserId[email=Email[email=test@gmail.com]]", userId.toString());
    }

}
