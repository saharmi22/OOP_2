package OOP.Tests;

import OOP.Provided.Song;
import OOP.Provided.User;
import OOP.Solution.SongImpl;
import OOP.Solution.UserImpl;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.HTMLDocument;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import java.util.stream.Collectors;

public class SongImplTest {
    Song s;
    SongImplTest() throws User.IllegalRateValue, User.SongAlreadyRated
    {
        s = new SongImpl(2, "s1", 13, "Vardi");
        User u1 = new UserImpl(11, "Itay", 25);
        User u2 = new UserImpl(9, "Lior", 24);
        User u3 = new UserImpl(10, "Mishel", 24);
        User u4 = new UserImpl(8, "Guy", 23);
        s.rateSong(u1, 3);
        s.rateSong(u2, 6);
        s.rateSong(u3, 6);
        s.rateSong(u4, 6);
    }

    @Test
    void testCompareTo() {
        Song s2 = new SongImpl(2, "Sababa", 15, "Lasch");
        assertEquals(0, s.compareTo(s2));
    }

    @Test
    void testEquals() {
        Song s2 = new SongImpl(2, "Sababa", 15, "Lasch");
        assertEquals(true, s.equals(s2));
    }

    @Test
    void testGetAverageRating() {
        assertEquals(5.25, s.getAverageRating());
    }

    @Test
    void testGetID() {
        assertEquals(2, s.getID());
    }

    @Test
    void testGetLength() {
        assertEquals(13, s.getLength());
    }

    @Test
    void testGetName() {
        assertEquals("s1", s.getName());
    }

    @Test
    void testGetRaters() {
        Collection<User> raters = s.getRaters();
        Iterator<User> it = raters.iterator();
        assertEquals(4, raters.size());
        assertEquals("Guy", it.next().getName());
        assertEquals("Mishel", it.next().getName());
        assertEquals("Lior", it.next().getName());
        assertEquals("Itay", it.next().getName());
    }

    @Test
    void testGetRatings() {
        Map<Integer, Set<User>> map = s.getRatings();
        assertEquals(2, map.size());
        assertEquals(3, map.get(6).size());
        assertEquals(1, map.get(3).size());
        Iterator<User> it = map.get(3).iterator();
        assertEquals("Itay", it.next().getName());
        assertEquals(false, it.hasNext());

        map.put(6, map.get(6).stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new)));
        it = map.get(6).iterator();
        assertEquals("Guy", it.next().getName());
        assertEquals("Lior", it.next().getName());
        assertEquals("Mishel", it.next().getName());
    }

    @Test
    void testGetSingerName() {
        assertEquals("Vardi", s.getSingerName());
    }

    @Test
    void testRateSong() {
        
    }
}
