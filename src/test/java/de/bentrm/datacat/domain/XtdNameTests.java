package de.bentrm.datacat.domain;

import org.intellij.lang.annotations.Language;
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
        XtdLanguage lang = new XtdLanguage();
        XtdName a = new XtdName();
        a.setUniqueId("A");
        a.setLanguageName(lang);
        a.setName("Test");

        XtdName b = new XtdName();
        b.setUniqueId("B");
        b.setLanguageName(lang);
        b.setName("Test");

        assertEquals(a, b);

        b.setName("Not Test");
        assertNotEquals(a, b);

        b.setName("Test");
        assertEquals(a, b);

        b.setLanguageName(new XtdLanguage());
        assertNotEquals(a, b);


        assertNotEquals(a, b);
    }

    @Test
    void worksWithTreeSets() {
        XtdLanguage lang = new XtdLanguage();
        SortedSet<XtdName> names = new TreeSet<>();

        var a = new XtdName();
        a.setLanguageName(lang);
        a.setName("a");
        names.add(a);

        assertTrue(names.contains(a));

        var b = new XtdName();
        b.setLanguageName(lang);
        b.setName("a");

        assertEquals(a, b);
        assertTrue(names.contains(a));
        assertTrue(names.contains(b));

        b.setName("b");
        assertNotEquals(a, b);
        assertFalse(names.add(b));
        assertFalse(names.contains(b));

        b.setName("a");
        assertEquals(a, b);
        assertTrue(names.contains(b));

        b.setLanguageName(new XtdLanguage());
        assertNotEquals(a, b);
        assertFalse(names.contains(b));

    }
}
