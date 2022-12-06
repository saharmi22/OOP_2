package OOP.Solution;

import OOP.Provided.Song;
import OOP.Provided.TechnionTunes;
import OOP.Provided.User;

import java.util.Set;
import java.lang.Math;



import java.util.*;
import java.util.function.Consumer;

public class TechnionTunesImpl implements TechnionTunes{
    private final HashMap<Integer, User> users_map;
    private final HashMap<Integer, Song> songs_map;

    public TechnionTunesImpl(){
        users_map = new HashMap<>();
        songs_map = new HashMap<>();
    }

    public void addUser(int userID , String userName , int userAge) throws TechnionTunes.UserAlreadyExists {
        if (users_map.containsKey(userID)){
            throw new TechnionTunes.UserAlreadyExists();
        }
        User user = new UserImpl(userID, userName, userAge);
        users_map.put(userID, user);
    }

    public User getUser(int id) throws TechnionTunes.UserDoesntExist{
        if (users_map.containsKey(id)){
            return users_map.get(id);
        }
        throw new TechnionTunes.UserDoesntExist();
    }


    public void makeFriends(int id1, int id2) throws TechnionTunes.UserDoesntExist, User.AlreadyFriends, User.SamePerson{
        if (!(users_map.containsKey(id1)))
            throw new TechnionTunes.UserDoesntExist();
        if (!(users_map.containsKey(id2)))
            throw new TechnionTunes.UserDoesntExist();

        //in case of exception, if it will be thrown it's okay
        User user1 = users_map.get(id1);
        User user2 = users_map.get(id2);
        user1.AddFriend(user2);
        user2.AddFriend(user1);

    }



    public void addSong(int songID , String songName , int length ,String SingerName) throws TechnionTunes.SongAlreadyExists{
        if (songs_map.containsKey(songID)){
            throw new TechnionTunes.SongAlreadyExists();
        }
        Song song = new SongImpl(songID, songName, length, SingerName);
        songs_map.put(songID, song);
    }


    public Song getSong(int id) throws TechnionTunes.SongDoesntExist{
        if (songs_map.containsKey(id)){
            return songs_map.get(id);
        }
        throw new TechnionTunes.SongDoesntExist();
    }


    public void rateSong(int userId, int songId, int rate) throws TechnionTunes.UserDoesntExist, TechnionTunes.SongDoesntExist, User.IllegalRateValue, User.SongAlreadyRated{
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
        //in case of exception, if it will be thrown it's okay
        user.rateSong(song, rate);
        song.rateSong(user,rate);
    }

    //compute intersection between set_A and set_B and put update set_A to that intersection
    private void Intersection(Set<Song> set_A, Set<Song> set_B){
        boolean changed = true; //if we changed collection in the middle of foreach should continue checking
        while (changed){
            changed = false;
            for (Song song : set_A){
                if (! set_B.contains(song)){ //if found a song that does not appear in both delete it
                    set_A.remove(song);
                    changed = true;
                    break; //deleted an item so we can't continue foreach
                }
            }
        }
    }


    public Set<Song> getIntersection(int IDs[]) throws TechnionTunes.UserDoesntExist{
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
        //intersection until now
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
            //update intersection with user's songs
            Intersection(inter_song, set_B);
            //clearing for next user
            set_B.clear();
        }
        return inter_song;
    }



    public Collection<Song> sortSongs(Comparator<Song> comp){
        List<Song> sort_song_list = new ArrayList<Song>(songs_map.values());
        sort_song_list.sort(comp);
        return sort_song_list;
    }



    public Collection<Song> getHighestRatedSongs(int num){
        List<Song> highest_rated_songs = new ArrayList<>(songs_map.values()); // all the songs are in the list
        //we want to sort them, so we use a comperator class that we define in the parameters that compares according to
        //avg, len, id
        highest_rated_songs.sort(new Comparator<Song>() {
            @Override
            public int compare(Song song1, Song song2) {
                double avg1 = song1.getAverageRating();
                double avg2 = song2.getAverageRating();
                if (avg1 < avg2)
                    return 1;
                if (avg1 > avg2)
                    return -1;

                int len1 = song1.getLength();
                int len2 = song2.getLength();
                if (len1 != len2)
                    return len2-len1;

                int id1 = song1.getID();
                int id2 = song2.getID();
                return id1 - id2;
            }
        });
        //if the number of songs is smaller than num, then we have to take it instead of num in order to not access a
        //unidentified memory area
        int min_len = Math.min(num, highest_rated_songs.size());
        return highest_rated_songs.subList(0, min_len);
    }


    public Collection<Song> getMostRatedSongs(int num){
        List<Song> most_rated_songs = new ArrayList<>(songs_map.values());

        most_rated_songs.sort(new Comparator<Song>() {
            @Override
            public int compare(Song song1, Song song2) {
                //rate by num of raters
                int num_rate1 = song1.getRaters().size();
                int num_rate2 = song2.getRaters().size();
                if (num_rate1 != num_rate2)
                    return num_rate2 - num_rate1;

                //then by length
                int len1 = song1.getLength();
                int len2 = song2.getLength();
                if (len1 != len2)
                    return len1-len2;

                //then by id
                int id1 = song1.getID();
                int id2 = song2.getID();
                return id2 - id1;
            }
        });
        //check if there's enough items and if not take less
        int min_len = Math.min(num, most_rated_songs.size());
        return most_rated_songs.subList(0, min_len);
    }



    public Collection<User> getTopLikers(int num){
        List<User> top_likers = new ArrayList<>(users_map.values());
        top_likers.sort(new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                //sort by user's avarage rating
                double avg_rate1 = user1.getAverageRating();
                double avg_rate2 = user2.getAverageRating();
                if (avg_rate1 < avg_rate2)
                    return 1;
                if (avg_rate1 > avg_rate2)
                    return -1;
                //then by age
                int age1 = user1.getAge();
                int age2 = user2.getAge();
                if (age1 != age2)
                    return age2-age1;
                //then by id
                int id1 = user1.getID();
                int id2 = user2.getID();
                return id1 - id2;
            }
        });
        //check if there's enough items and if not take less
        int min_len = Math.min(num, top_likers.size());
        return top_likers.subList(0, min_len);
    }


    public boolean canGetAlong(int userId1, int userId2) throws TechnionTunes.UserDoesntExist{
        //running BFS on the graph as taught in Algorithms 1
        User user1, user2;
        if (users_map.containsKey(userId1) && users_map.containsKey(userId2)){
            user1 = users_map.get(userId1);
            user2 = users_map.get(userId2);

            //creating open, closed queues
            LinkedList<User> open_nodes = new LinkedList<>();
            LinkedList<User> closed_nodes = new LinkedList<>();

            //adding starting node fo search - user1
            open_nodes.addLast(user1);
            User curr_node;

            if (user1 == user2){
                return true;
            }

            while (!open_nodes.isEmpty()){
                //get next one from open queue to expand
                curr_node = open_nodes.getFirst();
                open_nodes.remove(curr_node);
                //adding to close because we are expanding
                closed_nodes.add(curr_node);
                for (User friend:curr_node.getFriends().keySet()){
                    //edge exists if they have a favorite song in common
                    if (curr_node.favoriteSongInCommon(friend)) {
                        //did we already discover it? if we did skip so that we won't get into an infinite loop
                        if (!open_nodes.contains(friend) && !closed_nodes.contains(friend)) {
                            //if we got to goal - user 2 - return true
                            if (friend == user2)
                                return true;
                            //add friend to open in order to expand it
                            open_nodes.addLast(friend);
                        }
                    }
                }
            }
        }
        else{
            throw new TechnionTunes.UserDoesntExist();
        }
        //did not find path
        return false;
    }


    @Override
    public Iterator<Song> iterator() {
        List<Song> songs = new ArrayList<>(songs_map.values());
        songs.sort(new Comparator<Song>() {

            @Override
            public int compare(Song song1, Song song2) {

                int len1 = ((Song)song1).getLength();
                int len2 = ((Song)song2).getLength();
                if (len1 != len2)
                    return len1 - len2;

                int id1 = ((Song)song1).getID();
                int id2 = ((Song)song2).getID();
                return id1 - id2;
            }
        });
        return songs_map.values().iterator();
    }

    //@Override
    /*public void forEach(Consumer<? super Song> action) {
        TechnionTunes.super.forEach(action);
    }*/

}

