package br.com.devengine.tests;

import br.com.devengine.Valueless;
import br.com.devengine.entities.UserTest;
import br.com.devengine.entities.UserTestV2;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ValuelessTest {

    private final UserTest userTest = new UserTest(1L, "root", "123");
    private final UserTestV2 userTestV2 = new UserTestV2(1L, "root", "123", Arrays.asList("USER", "ADMIN"));

    @Test
    public void notEmptyOrNullTest() {
        assertTrue(Valueless.notEmptyOrNull(userTestV2));
        UserTestV2 clone = null;
        assertFalse(Valueless.notEmptyOrNull(clone));
    }

    @Test
    public void notEmptyOrNullMoreThenOneTest() {
        assertTrue(Valueless.notEmptyOrNull(userTest, userTestV2));
        UserTestV2 clone = null;
        assertFalse(Valueless.notEmptyOrNull(clone, userTest));
    }

    @Test
    public void notEmptyOrNullExecuteTest() {
        String username = Valueless.<UserTestV2, String>notEmptyOrNullExecute(userTestV2, "getUsername");
        assertEquals("root", username);
    }

}
