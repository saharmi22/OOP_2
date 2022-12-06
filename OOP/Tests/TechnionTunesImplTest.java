package OOP.Tests;

import OOP.Provided.Song;
import OOP.Provided.TechnionTunes;
import OOP.Provided.User;
import OOP.Solution.TechnionTunesImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TechnionTunesImplTest {

    @Test
    public void AddingStuff() {
        int errors = 0;
        TechnionTunesImpl tunes = new TechnionTunesImpl();
        try {
            tunes.addUser(1,"a",20);
            tunes.addUser(2,"b",20);
            tunes.addSong(1,"Hello Hello",3,"Danny");
            tunes.addSong(2,"Bye Bye",4,"Yossi");
            tunes.addSong(3,"World",7,"Danny");
            tunes.addSong(4,"Tomorrow",7,"Dan");
        }
        catch (TechnionTunes.UserAlreadyExists  | TechnionTunes.SongAlreadyExists songAlreadyExists) {
            System.out.println("AddingStuff() failed.");
            return;
        }
        try { tunes.addUser(2,"b",20); }
        catch (TechnionTunes.UserAlreadyExists userAlreadyExists) { errors++; }
        try { tunes.addSong(4,"Tomorrow",7,"Dan"); }
        catch (TechnionTunes.SongAlreadyExists songAlreadyExists) { errors++; }
        try { tunes.getUser(3); }
        catch (TechnionTunes.UserDoesntExist userDoesntExist) { errors++; }
        try { tunes.getSong(5); }
        catch (TechnionTunes.SongDoesntExist songDoesntExist) { errors++; }
        Assert.assertEquals(errors, 4);
    }

    @Test
    public void makeFriends() {
        int errors = 0;
        TechnionTunesImpl tunes = new TechnionTunesImpl();
        try {
            tunes.addUser(1,"aa",20);
            tunes.addUser(2,"ab",20);
            tunes.addUser(3,"cc",30);
            tunes.addUser(4,"d",10);
        }
        catch (TechnionTunes.UserAlreadyExists  userAlreadyExists) {
            System.out.println("makeFriends() failed.");
            return;
        }
        try {
            tunes.makeFriends(1, 2);
            try {
                tunes.getUser(1).AddFriend(tunes.getUser(2));
            } catch (User.AlreadyFriends alreadyFriends) {
                errors++;
            }
            try {
                tunes.getUser(2).AddFriend(tunes.getUser(1));
            } catch (User.AlreadyFriends alreadyFriends) {
                errors++;
            }
            try {
                tunes.getUser(1).AddFriend(tunes.getUser(1));
            } catch (User.SamePerson samPerson) {
                errors++;
            }

            tunes.makeFriends(1, 3);
            tunes.makeFriends(1, 4);
            try {
                tunes.getUser(1).AddFriend(tunes.getUser(3));
            } catch (User.AlreadyFriends alreadyFriends) {
                errors++;
            }

            Assert.assertEquals(tunes.getUser(2).getFriends()
                    .keySet().contains(tunes.getUser(3)), false); // not transitive (1)
            Assert.assertEquals(tunes.getUser(2).getFriends()
                    .keySet().contains(tunes.getUser(4)), false); // not transitive (2)

            try {
                tunes.makeFriends(1, 0);
            } catch (TechnionTunes.UserDoesntExist userDoesntExist) {
                errors++;
            }
        } catch (TechnionTunes.UserDoesntExist | User.SamePerson | User.AlreadyFriends a) {
            System.out.println("makeFriends() failed.");
            return;
        }
        Assert.assertEquals(errors, 5);
    }

    @Test
    public void songGames()  {
        TechnionTunesImpl tunes = new TechnionTunesImpl();
        try { // setup for tests
            tunes.addUser(1,"aa",20);
            tunes.addUser(2,"ab",20);
            tunes.addUser(3,"cc",30);
            tunes.addUser(4,"d",10);

            Assert.assertEquals(tunes.getHighestRatedSongs(10).size(),0); // no songs

            tunes.addSong(1,"d",10,"a");
            tunes.addSong(2,"b",5,"a");
            tunes.addSong(3,"a",10,"a");
            tunes.addSong(4,"c",20,"a");

            List<Integer> expected = Arrays.asList(4, 1, 3, 2); // all songs with rating = 0
            int i = 0;
            for(Song s : tunes.getHighestRatedSongs(4)) {
                Assert.assertEquals((int)expected.get(i), s.getID());
                i++;
            }

            tunes.rateSong(1, 1, 3);
            tunes.rateSong(1, 2, 7);
            tunes.rateSong(1, 4, 3);
            tunes.rateSong(2, 1, 3);
            tunes.rateSong(2, 4, 7);
            tunes.rateSong(3, 2, 3);
            tunes.rateSong(4, 3, 8);

            expected = Arrays.asList(3, 4, 2, 1); // expected order of output
            i = 0;
            for(Song s : tunes.getHighestRatedSongs(4)) {
                Assert.assertEquals((int)expected.get(i), s.getID());
                i++;
            }
            Assert.assertEquals(tunes.getHighestRatedSongs(10).size(), 4);
            Assert.assertEquals(tunes.getHighestRatedSongs(0).size(), 0);
            i = 0;
            expected = Arrays.asList(2, 1, 4, 3); // expected order of output
            System.out.println(Arrays.toString(tunes.getMostRatedSongs(4).stream().mapToInt(Song::getID).toArray()));
            for(Song s : tunes.getMostRatedSongs(4)) {
                Assert.assertEquals((int)expected.get(i), s.getID());
                i++;
            }
            Assert.assertEquals(tunes.getMostRatedSongs(10).size(), 4);
        }
        catch (User.IllegalRateValue | TechnionTunes.UserDoesntExist | TechnionTunes.SongDoesntExist | User.SongAlreadyRated
                | TechnionTunes.UserAlreadyExists | TechnionTunes.SongAlreadyExists  userAlreadyExists) {
            System.out.println("songGames() failed.");
        }
    }

    @Test
    public void topLikers() {
        TechnionTunesImpl tunes = new TechnionTunesImpl();
        try { // setup for tests
            tunes.addUser(1, "b", 20);
            tunes.addUser(2, "a", 20);
            tunes.addUser(3, "c", 30);
            tunes.addUser(4, "d", 10);
            tunes.addUser(5, "e", 5);


            int i = 0;
            Assert.assertEquals(tunes.getTopLikers(7).size(), 5);
            List<Integer> expected = Arrays.asList(3, 1, 2, 4, 5); // no songs yet
            for(User u : tunes.getTopLikers(7)) {
                Assert.assertEquals((int)expected.get(i), u.getID());
                i++;
            }

            tunes.addSong(1,"d",10,"a");
            tunes.addSong(2,"b",5,"a");
            tunes.addSong(3,"a",10,"a");
            tunes.addSong(4,"c",20,"a");


            tunes.rateSong(1, 1, 3);
            tunes.rateSong(1, 2, 7);
            tunes.rateSong(1, 4, 3);
            tunes.rateSong(2, 1, 3);
            tunes.rateSong(2, 4, 7);
            tunes.rateSong(3, 2, 3);
            tunes.rateSong(4, 3, 8);

            i = 0;
            expected = Arrays.asList( 4, 2, 1, 3, 5);
            for(User u : tunes.getTopLikers(3)) {
                Assert.assertEquals(expected.get(i).equals(u.getID()), true);
                i++;
            }

            tunes.rateSong(3, 3, 7);
            tunes.rateSong(5, 4, 6);
            tunes.rateSong(1, 3, 7);

            i = 0;
            expected = Arrays.asList( 4, 5, 3, 1, 2);
            for(User u : tunes.getTopLikers(7)) {
                Assert.assertEquals(expected.get(i).equals(u.getID()), true);
                i++;
            }

        } catch (TechnionTunes.SongAlreadyExists | TechnionTunes.UserAlreadyExists | User.SongAlreadyRated | TechnionTunes.UserDoesntExist
                | TechnionTunes.SongDoesntExist | User.IllegalRateValue illegalRateValue) {
            System.out.println("topLikers() failed.");
        }
    }

    @Test
    public void canGetAlong() {
        try {
            TechnionTunesImpl tunes = new TechnionTunesImpl();

            for(int i = 1; i <= 4; i++) {
                tunes.addUser(i, Integer.toString(i), i);
                tunes.addSong(i,Integer.toString(i),20,Integer.toString(i));
            }
            Assert.assertEquals(tunes.canGetAlong(1, 1), true);

            tunes.rateSong(1,1,10);
            tunes.rateSong(2,1,10);
            tunes.rateSong(3,1,10);
            tunes.rateSong(4,1,10);

            tunes.makeFriends(1,2);
            tunes.makeFriends(1,3);

            Assert.assertEquals(tunes.canGetAlong(1,1), true); // reflexive
            Assert.assertEquals(tunes.canGetAlong(2,1), true); // friends && songID: 1
            Assert.assertEquals(tunes.canGetAlong(2,3), true); // route: 2>1>3
            Assert.assertEquals(tunes.canGetAlong(1,4), false); // no route

            tunes.makeFriends(4,3);
            Assert.assertEquals(tunes.canGetAlong(2,4), true); // route: 2>1>3>4
            Assert.assertEquals(tunes.canGetAlong(4,2), true); // reversed route

        } catch (TechnionTunes.SongAlreadyExists | TechnionTunes.SongDoesntExist | User.IllegalRateValue | User.SongAlreadyRated | TechnionTunes.UserDoesntExist
                | User.AlreadyFriends | User.SamePerson | TechnionTunes.UserAlreadyExists userAlreadyExists) {
            System.out.println("canGetAlong() failed.");
        }
    }

    @Test
    public void intersectSongs() throws TechnionTunes.SongDoesntExist, TechnionTunes.UserDoesntExist, User.IllegalRateValue, User.SongAlreadyRated, TechnionTunes.UserAlreadyExists, TechnionTunes.SongAlreadyExists, User.SamePerson, User.AlreadyFriends {
        // generate test for getIntersection in TechnionTunesImpl
        TechnionTunesImpl tunes = new TechnionTunesImpl();
        for (int i = 1; i <= 4; i++) {
            tunes.addUser(i, Integer.toString(i), i);
            tunes.addSong(i, Integer.toString(i), 20, Integer.toString(i));
        }
        tunes.rateSong(1, 1, 10);
        tunes.rateSong(2, 1, 10);
        tunes.rateSong(3, 1, 10);
        tunes.rateSong(4, 1, 10);

        tunes.makeFriends(1, 2);
        tunes.makeFriends(1, 3);

        tunes.rateSong(1, 2, 10);
        tunes.rateSong(2, 2, 10);
        tunes.rateSong(3, 2, 10);
        tunes.rateSong(4, 2, 10);

        tunes.rateSong(1, 3, 10);
        tunes.rateSong(2, 3, 10);
        tunes.rateSong(3, 3, 10);
        tunes.rateSong(4, 3, 10);

        tunes.rateSong(1, 4, 10);
        tunes.rateSong(3, 4, 10);
        tunes.rateSong(4, 4, 10);

        List<Integer> expected = Arrays.asList(1, 2, 3, 4);
        Set<Integer> actual = tunes.getIntersection(new int[]{1, 1}).stream().map(Song::getID).collect(Collectors.toSet());
        for(Integer s : expected) Assert.assertTrue(actual.contains(s));
        for(Integer s : actual) Assert.assertTrue(expected.contains(s));

        expected = Arrays.asList(1, 2, 3);
        actual = tunes.getIntersection(new int[]{1, 2}).stream().map(Song::getID).collect(Collectors.toSet());
        for(Integer s : expected) Assert.assertTrue(actual.contains(s));
        for(Integer s : actual) Assert.assertTrue(expected.contains(s));

        expected = Arrays.asList(1, 2, 3);
        actual = tunes.getIntersection(new int[]{2, 1}).stream().map(Song::getID).collect(Collectors.toSet());
        for(Integer s : expected) Assert.assertTrue(actual.contains(s));
        for(Integer s : actual) Assert.assertTrue(expected.contains(s));

        expected = Arrays.asList(1, 2, 3, 4);
        actual = tunes.getIntersection(new int[]{1, 3}).stream().map(Song::getID).collect(Collectors.toSet());
        for(Integer s : expected) Assert.assertTrue(actual.contains(s));
        for(Integer s : actual) Assert.assertTrue(expected.contains(s));

    }

//    public static void main(String[] args) {
//        new TechnionTunesImplTest().AddingStuff();
//        new TechnionTunesImplTest().makeFriends();
//        new TechnionTunesImplTest().songGames();
//        new TechnionTunesImplTest().topLikers();
//        new TechnionTunesImplTest().canGetAlong();
//    }
}
