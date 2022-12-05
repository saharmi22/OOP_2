package OOP.Solution;

import OOP.Provided.User;
import OOP.Provided.Song;
import java.util.Map;
import java.util.*;

public class Main{
    public static void main(String[] args){
        User user = new UserImpl(1, "sahar", 20);
        int user_id = user.getID();
        int user_age = user.getAge();
        String user_name = user.getName();
        User user2 = new UserImpl(1, "noa", 30);
        User user3 = new UserImpl(3, "elinor", 20);
        /*try {
            user.AddFriend(user2);
        }
        catch (Exception ignored){
        }*/
        try {
            user.AddFriend(user3);
        }
        catch (Exception ignored){

        }
        Map<User, Integer> map = user.getFriends();

        Song wap = new SongImpl(44, "WAP", 252, "Cardi&Megan");
        Song oops = new SongImpl(44, "OOPS", 252, "Britney");
        Song toxic = new SongImpl(71, "TOXIC", 199, "Britney");

        int songID = wap.getID();
        int songLength = wap.getLength();
        String songName = wap.getName();
        String singerName = wap.getSingerName();

        System.out.println(songID + " " + songLength + " " + songName + " " + singerName);

        try{
            wap.rateSong(user2, 10);
        }
        catch (Exception ignored){
        }

        try{
            wap.rateSong(user, 4);
        }
        catch (Exception ignored){
        }
        try{
            wap.rateSong(user3, 2);
        }
        catch (Exception ignored){
        }

        Collection<User> users_by_rate =  wap.getRaters();

        Map<Integer, Set<User>> ratings_map = wap.getRatings();

        /*for (Map.Entry<Integer, Set<User>> entry : ratings_map.entrySet()){
            System.out.println(entry.getKey());
            for(User u : entry.getValue())
                System.out.println(u.getName());

        }

        double avg = wap.getAverageRating();
        System.out.println("avg:" + avg);*/

        System.out.println("wap =? oops : " + wap.equals(oops));

        System.out.println("wap =? toxic : " + wap.compareTo(toxic));


        try{
            user.rateSong(wap, 8);;
        }
        catch (Exception ignored){
        }
        try{
            user2.rateSong(wap, 8);;
        }
        catch (Exception ignored){
        }
        try{
            user.rateSong(oops, 8);;
        }
        catch (Exception ignored){
        }
        try{
            user.rateSong(toxic, 8);;
        }
        catch (Exception ignored){
        }

        double avg = user.getAverageRating();
        System.out.println("avg:" + avg);

        double tot_len = user.getPlaylistLength();
        System.out.println("tot len:" + tot_len);

        Collection<Song> rated_songs = user.getFavoriteSongs();
        for(Song s : rated_songs)
            System.out.println(s.getName());

        System.out.println("noa and sahar are friends and have a favorite song in common");
        System.out.println(user.favoriteSongInCommon(user2));

        System.out.println("sahar =? noa : " + user.equals(user2));

        System.out.println("sahar =? elinor : " + user.compareTo(user3));

    }
}