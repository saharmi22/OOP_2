package OOP.Tests;

import OOP.Provided.Song;
import OOP.Provided.User;
import OOP.Solution.SongImpl;
import OOP.Solution.UserImpl;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.HTMLDocument;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class UserImplTest {
    UserImpl user;
    UserImplTest() throws User.IllegalRateValue, User.SongAlreadyRated {
        user = new UserImpl(1, "n1", 12);
        SongImpl s = new SongImpl(11, "s1", 23, "singer1");
        user.rateSong(s, 1);
        s = new SongImpl(12, "s2", 21, "singer1");
        user.rateSong(s, 5);
        s = new SongImpl(13, "s3", 22, "singer2");
        user.rateSong(s, 5);
        s = new SongImpl(14, "s4", 22, "singer3");
        user.rateSong(s, 5);
    }
    @Test
    void getID() {
        assert(user.getID() == 1);
    }

    @Test
    void getName() {
        assert(Objects.equals(user.getName(), "n1"));
    }

    @Test
    void getAge() {
        assertEquals(user.getAge(), 12);
    }

    @Test
    void rateSong() {
    }

    @Test
    void getAverageRating() {
        assertEquals(4, user.getAverageRating());
    }

    @Test
    void getPlaylistLength() {
        System.out.println(user.getPlaylistLength());
        assertEquals(88, user.getPlaylistLength());
    }

    @Test
    void getRatedSongs() {
        //s2 -> s4 -> s3 -> s1
        Collection<Song> songs = user.getRatedSongs();
        Iterator<Song> it = songs.iterator();
        assertEquals(it.next().getName(), "s2");
        assertEquals(it.next().getName(), "s4");
        assertEquals(it.next().getName(), "s3");
        assertEquals(it.next().getName(), "s1");
    }

    @Test
    void getFavoriteSongs() throws User.IllegalRateValue, User.SongAlreadyRated {
        Collection<Song> songs = user.getFavoriteSongs();
        Iterator<Song> it = songs.iterator();
        assert(!it.hasNext());
        Song s5 = new SongImpl(100, "s7", 3, "singer");
        Song s6 = new SongImpl(99, "s8", 5, "singer");
        user.rateSong(s5, 9);
        user.rateSong(s6, 8);
        songs = user.getFavoriteSongs();
        it = songs.iterator();
        assertEquals(it.next(), s6);
        assertEquals(it.next(), s5);
        Song s = new SongImpl(s5);
        assert(s != s5);
        assertEquals(s, s5);
        assertNotEquals(s, s6);
    }

    @Test
    void addFriend() throws User.SamePerson, User.AlreadyFriends, User.IllegalRateValue, User.SongAlreadyRated {
        User u2 = new UserImpl(2, "u2", 13);
        user.AddFriend(u2);
        assert (user.getFriends().containsKey(u2));
    }

    @Test
    void favoriteSongInCommon() throws User.SamePerson, User.AlreadyFriends, User.IllegalRateValue, User.SongAlreadyRated {

        User u2 = new UserImpl(9, "u2", 13);
        user.AddFriend(u2);

        Song s = new SongImpl(2, "song", 3, "singer");
        u2.rateSong(s, 8);

        Song s2 = new SongImpl(101, "s2", 12, "aa");
        u2.rateSong(s2, 1);
        
        assert(!user.favoriteSongInCommon(u2));
        user.rateSong(s, 10);
        assert(user.favoriteSongInCommon(u2));


    }

    @Test
    void getFriends() throws User.SamePerson, User.AlreadyFriends, User.IllegalRateValue, User.SongAlreadyRated {
        User u2 = new UserImpl(9, "u2", 13);
        Song s = new SongImpl(2, "song", 3, "singer");
        u2.rateSong(s, 8);

        Song s2 = new SongImpl(101, "s2", 12, "aa");
        u2.rateSong(s2, 1);

        assert(user.getFriends().isEmpty());
        user.AddFriend(u2);

        assert(user.getFriends().containsKey(u2));

    }

    @Test
    void testEquals() {
        User u2 = new UserImpl(1, "u2", 13);
        assert (user.equals(u2));
    }

    @Test
    void testHashCode() {
    }

    @Test
    void compareTo() {
        User u2 = new UserImpl(2, "u2", 13);
        assert (user.compareTo(u2) == -1);

    }
}