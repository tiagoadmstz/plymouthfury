package br.com.devengine.tests;

import br.com.devengine.Errors;
import br.com.devengine.Valueless;
import br.com.devengine.entities.UserTest;
import br.com.devengine.entities.UserTestV2;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ErrorsTest {

    private final UserTest userTest = new UserTest(1L, "root", "123");

    @Test
    public void tryCatchDefaultTest() {
        assertTrue(Errors.<UserTest, Boolean>tryCatchDefault(f -> Valueless.notEmptyOrNull(userTest), Boolean.FALSE));
        UserTestV2 userTestV2 = null;
        assertFalse(Errors.<UserTest, Boolean>tryCatchDefault(f -> Valueless.notEmptyOrNull(userTestV2), Boolean.FALSE));
        assertFalse(Errors.<UserTest, Boolean>tryCatchDefault(f -> Valueless.notEmptyOrNull(null), Boolean.FALSE));
    }

}
