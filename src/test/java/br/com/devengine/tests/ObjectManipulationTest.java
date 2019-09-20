package br.com.devengine.tests;

import br.com.devengine.entities.UserTest;
import br.com.devengine.entities.UserTestV2;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectManipulationTest {

    private final UserTest userTest = new UserTest(1L, "root", "123");

    @Test
    public void clearTest() {
        userTest.clear();
        assertEquals(new UserTest(), userTest);
    }

    @Test
    public void clearSensibleDataTest() {
        userTest.clearSensibleData("username", "password");
        assertNull(userTest.getUsername());
        assertNull(userTest.getPassword());
        assertEquals(1L, userTest.getId().longValue());
    }

    @Test
    public void copiarTest() {
        UserTest userTest2 = new UserTest();
        userTest2.copiar(userTest);
        assertEquals(userTest, userTest2);
    }

    @Test
    public void getInstanceTest() {
        assertNull(userTest.getInstance());
    }

    @Test
    public void convertTest() {
        UserTestV2 userTestV2 = new UserTestV2();
        userTestV2.convert(userTest);
        assertEquals(userTest.getId(), userTestV2.getId());
        assertEquals(userTest.getUsername(), userTestV2.getUsername());
        assertEquals(userTest.getPassword(), userTestV2.getPassword());
    }

    @Test
    public void printValuesTest(){
        userTest.printValues();
    }

    @Test
    public void isNullTest(){
        assertFalse(userTest.isNull());
        userTest.clear();
        assertTrue(userTest.isNull());
    }

    @Test
    public void clonarTest(){
        assertEquals(userTest, userTest.clonar());
    }

}
