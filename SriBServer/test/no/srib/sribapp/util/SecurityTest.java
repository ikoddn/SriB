package no.srib.sribapp.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class SecurityTest {
    
    @Test
    public void testToSHA512() {
        String input = "Testpw123";
        String username = "testuser";
        
        String expected = "4746004308fc4e46010e3069056f56f82083a6bc0a8f1e676cc1e0110bb4c123b3ac895d19cd77979d5734c88e8aefead96cbdbe436403f4dbacf49d84ea8c46";
        String actual = Security.toSHA512(input, username);
        assertEquals(expected, actual);
    }
}
