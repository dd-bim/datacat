package de.bentrm.datacat.domain;

import org.junit.jupiter.api.Test;

import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class XtdNameTests {

    @Test
    void comaratorIsConsistentWithEquals() {
        var a = new XtdName();
        var b = new XtdName();

        assertEquals(a, b);
        assertEquals(0, a.compareTo(b));
        assertEquals(a.compareTo(b) == 0, a.equals(b));
        assertEquals(b.compareTo(a) == 0, b.equals(a));

        a.setSortOrder(1);
        assertEquals(1, a.compareTo(b));
        assertEquals(a.compareTo(b) == 0, a.equals(b));
        assertEquals(b.compareTo(a) == 0, b.equals(a));

        b.setSortOrder(2);
        assertEquals(-1, a.compareTo(b));
        assertEquals(a.compareTo(b) == 0, a.equals(b));
        assertEquals(b.compareTo(a) == 0, b.equals(a));
    }

    @Test
    void equalityIsDetected() {
        XtdName a = new XtdName();
        a.setId("A");
        a.setLanguageName("de");
        a.setValue("Test");

        XtdName b = new XtdName();
        b.setId("B");
        b.setLanguageName("de");
        b.setValue("Test");

        assertEquals(a, b);

        b.setValue("Not Test");
        assertNotEquals(a, b);

        b.setValue("Test");
        assertEquals(a, b);

        b.setLanguageName("es");
        assertNotEquals(a, b);


        assertNotEquals(a, b);
    }

    @Test
    void worksWithTreeSets() {
        SortedSet<XtdName> names = new TreeSet<>();

        var a = new XtdName();
        a.setLanguageName("de");
        a.setValue("a");
        names.add(a);

        assertTrue(names.contains(a));

        var b = new XtdName();
        b.setLanguageName("de");
        b.setValue("a");

        assertEquals(a, b);
        assertTrue(names.contains(a));
        assertTrue(names.contains(b));

        b.setValue("b");
        assertNotEquals(a, b);
        assertFalse(names.add(b));
        assertFalse(names.contains(b));

        b.setValue("a");
        assertEquals(a, b);
        assertTrue(names.contains(b));

        b.setLanguageName("es");
        assertNotEquals(a, b);
        assertFalse(names.contains(b));

    }
}
