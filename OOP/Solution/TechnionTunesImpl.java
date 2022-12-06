package OOP.Solution;

import OOP.Provided.Song;
import OOP.Provided.TechnionTunes;
import OOP.Provided.User;
import static java.util.stream.Collectors.*;
import java.util.Set;
import java.lang.Math;



import java.util.*;

public class TechnionTunesImpl {
    private final HashMap<Integer, User> users_map;
    private final HashMap<Integer, Song> songs_map;

    public TechnionTunesImpl(){
        users_map = new HashMap<>();
        songs_map = new HashMap<>();
    }

    void addUser(int userID , String userName , int userAge) throws TechnionTunes.UserAlreadyExists {
        if (users_map.containsKey(userID)){
            throw new TechnionTunes.UserAlreadyExists();
        }
        User user = new UserImpl(userID, userName, userAge);
        users_map.put(userID, user);
    }

    User getUser(int id) throws TechnionTunes.UserDoesntExist{
        if (users_map.containsKey(id)){
            return users_map.get(id);
        }
        throw new TechnionTunes.UserDoesntExist();
    }


    void makeFriends(int id1, int id2) throws TechnionTunes.UserDoesntExist, User.AlreadyFriends, User.SamePerson{
        if (!(users_map.containsKey(id1)))
            throw new TechnionTunes.UserDoesntExist();
        if (!(users_map.containsKey(id2)))
            throw new TechnionTunes.UserDoesntExist();

        User user1 = users_map.get(id1);
        User user2 = users_map.get(id2);
        user1.AddFriend(user2);
        user2.AddFriend(user1);

    }



    void addSong(int songID , String songName , int length ,String SingerName) throws TechnionTunes.SongAlreadyExists{
        if (songs_map.containsKey(songID)){
            throw new TechnionTunes.SongAlreadyExists();
        }
        Song song = new SongImpl(songID, songName, length, SingerName);
        songs_map.put(songID, song);
    }


    Song getSong(int id) throws TechnionTunes.SongDoesntExist{
        if (songs_map.containsKey(id)){
            return songs_map.get(id);
        }
        throw new TechnionTunes.SongDoesntExist();
    }


    void rateSong(int userId, int songId, int rate) throws TechnionTunes.UserDoesntExist, TechnionTunes.SongDoesntExist, User.IllegalRateValue, User.SongAlreadyRated{
        User user;
        Song song;
        if (users_map.containsKey(userId)) {
            user = users_map.get(userId);
        }
        else{
            throw new TechnionTunes.UserDoesntExist();
        }
        if (songs_map.containsKey(songId)){
            song = songs_map.get(songId);
        }
        else{
            throw new TechnionTunes.SongDoesntExist();
        }
        user.rateSong(song, rate);
        song.rateSong(user,rate);
    }

    //compute intersection between set_A and set_B and put update set_A to that intersection
    private void Intersection(Set<Song> set_A, Set<Song> set_B){
        for (Song song : set_A){
            if (! set_B.contains(song)){
                set_A.remove(song);
            }
        }
    }


    Set<Song> getIntersection(int IDs[]) throws TechnionTunes.UserDoesntExist{
        Set<Song> inter_song = new TreeSet<>();
        User user1, user2;
        int len = IDs.length;
        if (len == 0)
            return inter_song;
        if (users_map.containsKey(IDs[0])){
            user1 = users_map.get(IDs[0]);
        }
        else {
            throw new TechnionTunes.UserDoesntExist();
        }
        inter_song.addAll(user1.getRatedSongs());
        Set<Song> set_B = new TreeSet<>();
        for (int i=1; i<len; i++){
            if (users_map.containsKey(IDs[i])){
                user2 = users_map.get(IDs[i]);
            }
            else {
                throw new TechnionTunes.UserDoesntExist();
            }
            set_B.addAll(user2.getRatedSongs());
            Intersection(inter_song, set_B);
            set_B.clear();
        }
        return inter_song;
    }



    Collection<Song> sortSongs(Comparator<Song> comp){
        List<Song> sort_song_list = new ArrayList<Song>(songs_map.values());
        sort_song_list.sort(comp);
        return sort_song_list;
    }


    private int compByLenHLIdLH(Song song1, Song song2) {
        int len1 = song1.getLength();
        int len2 = song2.getLength();
        if (len1 != len2)
            return len2-len1;

        int id1 = song1.getID();
        int id2 = song2.getID();
        return id1 - id2;

    }
    Collection<Song> getHighestRatedSongs(int num){
        List<Song> highest_rated_songs = new ArrayList<>(songs_map.values());
        highest_rated_songs.sort(new Comparator<Song>() {
            @Override
            public int compare(Song song1, Song song2) {
                double avg1 = song1.getAverageRating();
                double avg2 = song2.getAverageRating();
                if (avg1 > avg2)
                    return 1;
                if (avg1 < avg2)
                    return -1;

                return compByLenHLIdLH(song1, song2);
            }
        });
        int min_len = Math.min(num, highest_rated_songs.size()) - 1;
        return highest_rated_songs.subList(0, min_len);
    }


    Collection<Song> getMostRatedSongs(int num){
        List<Song> most_rated_songs = new ArrayList<>(songs_map.values());
        most_rated_songs.sort(new Comparator<Song>() {
            @Override
            public int compare(Song song1, Song song2) {
                int num_rate1 = song1.getRaters().size();
                int num_rate2 = song2.getRaters().size();
                if (num_rate1 != num_rate2)
                    return num_rate1 - num_rate2;

                return compByLenHLIdLH(song1, song2);
            }
        });
        int min_len = Math.min(num, most_rated_songs.size()) - 1;
        return most_rated_songs.subList(0, min_len);
    }



    Collection<User> getTopLikers(int num){
        List<User> top_likers = new ArrayList<>(users_map.values());
        top_likers.sort(new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                double avg_rate1 = user1.getAverageRating();
                double avg_rate2 = user2.getAverageRating();
                if (avg_rate1 > avg_rate2)
                    return 1;
                if (avg_rate1 < avg_rate2)
                    return -1;
                int age1 = user1.getAge();
                int age2 = user2.getAge();
                if (age1 != age2)
                    return age1-age2;
                int id1 = user1.getID();
                int id2 = user2.getID();
                return id2 - id1;
            }
        });
        int min_len = Math.min(num, top_likers.size()) - 1;
        return top_likers.subList(0, min_len);
    }


    boolean canGetAlong(int userId1, int userId2) throws TechnionTunes.UserDoesntExist{
        User user1, user2;
        if (users_map.containsKey(userId1) && users_map.containsKey(userId2)){
            user1 = users_map.get(userId1);
            user2 = users_map.get(userId2);

            LinkedList<User> open_nodes = new LinkedList<>();
            LinkedList<User> closed_nodes = new LinkedList<>();

            open_nodes.addLast(user1);
            User curr_node;

            if (user1 == user2){
                return true;
            }

            while (!open_nodes.isEmpty()){
                curr_node = open_nodes.getFirst();
                open_nodes.remove(curr_node);
                closed_nodes.add(curr_node);
                for (User friend:curr_node.getFriends().keySet()){
                    if (curr_node.favoriteSongInCommon(friend)) {
                        if (!open_nodes.contains(friend) && !closed_nodes.contains(friend)) {
                            if (friend == user2)
                                return true;
                            open_nodes.addLast(friend);
                        }
                    }
                }
            }
        }
        else{
            throw new TechnionTunes.UserDoesntExist();
        }
        return false;
    }



}
