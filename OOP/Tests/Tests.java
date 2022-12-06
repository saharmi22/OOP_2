package OOP.Tests;

import OOP.Provided.Song;
import OOP.Provided.TechnionTunes;
import OOP.Provided.User;
import OOP.Solution.SongImpl;
import OOP.Solution.TechnionTunesImpl;
import junit.runner.Version;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class Tests {

    @Test
    public void exampleTest() throws TechnionTunes.UserAlreadyExists, TechnionTunes.UserDoesntExist, User.AlreadyFriends, User.SamePerson, TechnionTunes.SongAlreadyExists, User.IllegalRateValue, TechnionTunes.SongDoesntExist, User.SongAlreadyRated {
        TechnionTunes tt = new TechnionTunesImpl();
        tt.addUser(125,"Rajaie",5);
        tt.addUser(200,"Natan",5);

        Assert.assertEquals("Rajaie",tt.getUser(125).getName());
        Assert.assertEquals("Natan",tt.getUser(200).getName());

        tt.makeFriends(125,200);


        tt.addSong(321,"Despacito",300,"Daddy Yankee");
        Assert.assertEquals("Despacito",tt.getSong(321).getName());
        tt.rateSong(125,321,8);
        tt.rateSong(200,321,10);

        Assert.assertTrue(tt.getUser(125).getRatedSongs().contains(new SongImpl(321,"haha",300,"hehe")));
        for(Song s : tt){
            Assert.assertTrue(tt.getUser(125).getRatedSongs().contains(s));
        }

        Assert.assertTrue(tt.canGetAlong(125,200));

        System.out.println("JUnit version is: " + Version.id());
    }

    @Test
    public void usersTest() throws TechnionTunes.UserAlreadyExists, User.SamePerson, User.AlreadyFriends, TechnionTunes.UserDoesntExist, TechnionTunes.SongAlreadyExists, TechnionTunes.SongDoesntExist, User.IllegalRateValue, User.SongAlreadyRated {
        TechnionTunes tt = new TechnionTunesImpl();
        tt.addUser(0, "0", 10);
        tt.addUser(1, "1", 0);
        tt.addUser(2, "2", 0);
        tt.addUser(3, "3", 0);

        tt.makeFriends(0, 1);
        tt.makeFriends(0, 2);
        tt.makeFriends(0, 3);

        Assert.assertEquals(3, tt.getUser(0).getFriends().keySet().size());
        Assert.assertEquals(1, tt.getUser(1).getFriends().keySet().size());

        tt.addSong(100,"100",300,"Daddy Yankee");
        tt.addSong(101,"101",300,"Daddy Yankee");
        tt.addSong(102,"102",300,"Daddy Yankee");
        tt.addSong(103,"103",300,"Daddy Yankee");

        Assert.assertTrue(tt.canGetAlong(2, 2));
        tt.rateSong(1, 100, 8);
        tt.rateSong(2, 100, 10);
        Assert.assertFalse(tt.canGetAlong(1, 2));
        tt.rateSong(0, 101, 10);
        tt.rateSong(1, 101, 10);
        tt.rateSong(0, 102, 10);
        tt.rateSong(2, 102, 10);
        Assert.assertTrue(tt.canGetAlong(0, 1));
        Assert.assertTrue(tt.canGetAlong(0, 2));
        Assert.assertTrue(tt.canGetAlong(1, 2));
        tt.rateSong(3, 100, 10);
        Assert.assertFalse(tt.canGetAlong(0, 3));
        Assert.assertFalse(tt.canGetAlong(1, 3));
        tt.makeFriends(3, 1);
        Assert.assertTrue(tt.canGetAlong(1, 3));
        tt.addUser(4, "4", 0);
        tt.addUser(5, "5", 0);
        tt.addUser(6, "6", 0);
        tt.makeFriends(3, 4);
        tt.makeFriends(3, 5);
        tt.makeFriends(5, 4);
        tt.makeFriends(5, 6);
        tt.rateSong(3, 101, 10);
        tt.rateSong(4, 101, 10);
        tt.rateSong(5, 101, 10);
        tt.rateSong(6, 101, 10);
        Assert.assertTrue(tt.canGetAlong(3, 6));
        Assert.assertTrue(tt.canGetAlong(1, 6));
        Assert.assertTrue(tt.canGetAlong(0, 5));
        Assert.assertTrue(tt.canGetAlong(0, 6));

        int[] IDs = new int[2];
        IDs[0] = 1;
        IDs[1] = 0;
        Assert.assertEquals(1, tt.getIntersection(IDs).size());
        IDs[1] = 3;
        Assert.assertEquals(2, tt.getIntersection(IDs).size());

        tt.addSong(104,"104",300,"Daddy Yankee");
        tt.addSong(105,"105",299,"Daddy Yankee");
        tt.rateSong(6, 105, 10);
        tt.rateSong(6, 104, 0);
        ArrayList<Song> highestRated = new ArrayList<>(tt.getHighestRatedSongs(7));
        Assert.assertEquals(6, highestRated.size());
        Assert.assertEquals(101, highestRated.get(0).getID());
        Assert.assertEquals(102, highestRated.get(1).getID());
        Assert.assertEquals(105, highestRated.get(2).getID());
        Assert.assertEquals(100, highestRated.get(3).getID());
        Assert.assertEquals(103, highestRated.get(4).getID());
        Assert.assertEquals(104, highestRated.get(5).getID());

        tt.addSong(106,"106",300,"Daddy Yankee");
        ArrayList<Song> mostRated = new ArrayList<>(tt.getMostRatedSongs(7));
        Assert.assertEquals(101, mostRated.get(0).getID());
        Assert.assertEquals(100, mostRated.get(1).getID());
        Assert.assertEquals(102, mostRated.get(2).getID());
        Assert.assertEquals(105, mostRated.get(3).getID());
        Assert.assertEquals(104, mostRated.get(4).getID());
        Assert.assertEquals(106, mostRated.get(5).getID());
        Assert.assertEquals(103, mostRated.get(6).getID());

        tt.addUser(7, "7", 0);
        ArrayList<User> topLikers = new ArrayList<>(tt.getTopLikers(8));
        Assert.assertEquals(0, topLikers.get(0).getID());
        Assert.assertEquals(2, topLikers.get(1).getID());
        Assert.assertEquals(3, topLikers.get(2).getID());
        Assert.assertEquals(4, topLikers.get(3).getID());
        Assert.assertEquals(5, topLikers.get(4).getID());
        Assert.assertEquals(1, topLikers.get(5).getID());
        Assert.assertEquals(6, topLikers.get(6).getID());
        Assert.assertEquals(7, topLikers.get(7).getID());
    }

    @Test
    public void exceptionsTest() throws TechnionTunes.UserAlreadyExists, User.SamePerson, User.AlreadyFriends, TechnionTunes.UserDoesntExist, TechnionTunes.SongAlreadyExists, TechnionTunes.SongDoesntExist, User.IllegalRateValue, User.SongAlreadyRated {
        TechnionTunes tt = new TechnionTunesImpl();

        // user Tests

        try {
            tt.getUser(0);
            throw new RuntimeException("failed to throw UserDoesntExist");
        } catch (TechnionTunes.UserDoesntExist ignored) {
        }

        tt.addUser(0,"0",0);
        tt.addUser(1, "1", 0);

        try {
            tt.addUser(0, "2", 2);
            throw new RuntimeException("failed to throw UserAlreadyExists");
        } catch (TechnionTunes.UserAlreadyExists ignored) {
        }

        // makeFriends tests:

        try {
            tt.makeFriends(1, 2);
            throw new RuntimeException("failed to throw UserDoesntExist");
        } catch (TechnionTunes.UserDoesntExist ignored) {
        }
        try {
            tt.makeFriends(2, 1);
            throw new RuntimeException("failed to throw UserDoesntExist");
        } catch (TechnionTunes.UserDoesntExist ignored) {
        }
        try {
            tt.makeFriends(2, 2);
            throw new RuntimeException("failed to throw UserDoesntExist");
        } catch (TechnionTunes.UserDoesntExist ignored) {
        }
        try {
            tt.makeFriends(1, 1);
            throw new RuntimeException("failed to throw SamePerson");
        } catch (User.SamePerson ignored) {
        }

        tt.makeFriends(0, 1);

        try {
            tt.makeFriends(1, 0);
            throw new RuntimeException("failed to throw AlreadyFriends");
        } catch (User.AlreadyFriends ignored) {
        }

        // songs tests:
        tt.addSong(100,"100",300,"Daddy Yankee");
        tt.addSong(101,"101",300,"Daddy Yankee");

        try {
            tt.addSong(100,"0",0,"0");
            throw new RuntimeException("failed to throw SongAlreadyExists");
        } catch (TechnionTunes.SongAlreadyExists ignored) {
        }

        tt.rateSong(0, 100, 8);
        tt.rateSong(1, 100, 10);

        try {
            tt.rateSong(0, 100, 8);
            throw new RuntimeException("failed to throw SongAlreadyRated");
        } catch (User.SongAlreadyRated ignored) {
        }
        try {
            tt.rateSong(3, 100, 8);
            throw new RuntimeException("failed to throw UserDoesntExist");
        } catch (TechnionTunes.UserDoesntExist ignored) {
        }
        try {
            tt.rateSong(0, 300, 8);
            throw new RuntimeException("failed to throw SongDoesntExist");
        } catch (TechnionTunes.SongDoesntExist ignored) {
        }
        try {
            tt.rateSong(0, 300, -1);
            throw new RuntimeException("failed to throw SongDoesntExist");
        } catch (TechnionTunes.SongDoesntExist ignored) {
        }
        try {
            tt.rateSong(3, 100, -1);
            throw new RuntimeException("failed to throw UserDoesntExist");
        } catch (TechnionTunes.UserDoesntExist ignored) {
        }
        try {
            tt.rateSong(0, 100, -1);
            throw new RuntimeException("failed to throw IllegalRateValue");
        } catch (User.IllegalRateValue ignored) {
        }


        try {
            tt.canGetAlong(0, 2);
            throw new RuntimeException("failed to throw UserDoesntExist");
        } catch (TechnionTunes.UserDoesntExist ignored) {
        }

    }

}
