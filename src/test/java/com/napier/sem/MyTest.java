
package com.napier.sem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class MyTest {

    @Test
    void unitTest() {
        Assertions.assertEquals(5, 5);
    }

    @Test
    void unitTest3() {
        Assertions.assertEquals(5, 5, "Messages are equal");
    }

    @Test
    void unitTest4() {
        Assertions.assertEquals(5.0, 5.01, 0.02);
    }

    @Test
    void unitTest5() {
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};

        Assertions.assertArrayEquals(a, b);
    }

    @Test
    void unitTest6() {
        Assertions.assertTrue(true);
    }

    @Test
    void unitTest7() {
        Assertions.assertFalse(false);
    }

    @Test
    void unitTest8() {
        Assertions.assertNull(null);
    }

    @Test
    void unitTest9() {
        Assertions.assertNotNull("Hello");
    }

    @Test
    void unitTest10() {
        Assertions.assertThrows(
                NullPointerException.class,
                this::throwsException
        );
    }

    void throwsException() {
        throw new NullPointerException();
    }
}
