package OOP.Solution;

import OOP.Provided.User;
import OOP.Provided.Song;
import java.util.*;

public class UserImpl implements User {
    private final int user_id;
    private final String user_name;
    private final int user_age;
    private final HashMap<Integer, TreeSet<Song>> rated_songs; //key is rate and value is set of songs rated that num
    private final LinkedList<User> friends;

    //constructor
    public UserImpl(int userId, String userName, int userAge) {
        user_id = userId;
        user_name = userName;
        user_age = userAge;
        rated_songs = new HashMap<>();
        friends = new LinkedList<>();
    }

    @Override public int getID() {
        return user_id;
    }

    public String getName() {
        return user_name;
    }

    public int getAge() {
        return user_age;
    }

    public User rateSong(final Song song, int rate) throws IllegalRateValue, SongAlreadyRated {
        if (rate < 0 || rate > 10) {
            throw new IllegalRateValue();
        }
        boolean song_exists = false;
        //check if song is rated by checking sets for all rates
        for (int i = 0; (i <= 10 && !song_exists); i++) {
            if (rated_songs.containsKey(i)){
                TreeSet<Song> songs_rated_i = rated_songs.get(i);
                if (songs_rated_i.contains(song))
                    song_exists = true;
            }
        }
        if (song_exists) {
            throw new SongAlreadyRated();
        }
        if (rated_songs.containsKey(rate)){
            TreeSet<Song> songs_rate = rated_songs.get(rate);
            songs_rate.add(song);
        } else { //first song rated (rate)
            //create new set for key rat in hashmap
            TreeSet<Song> new_song_set = new TreeSet<>();
            new_song_set.add(song);
            rated_songs.put(rate, new_song_set);
        }
        return this;
    }

    public double getAverageRating() {
        int sum = 0;
        int cnt = 0;
        //go over all items in map and calc sum rates and num of songs overall
        for (int rate = 0; rate <= 10; rate++) {
            if (rated_songs.containsKey(rate)){
                TreeSet<Song> set_rate = rated_songs.get(rate);
                //in set_rate all songs are rated (rate)
                sum += rate * set_rate.size();
                cnt += set_rate.size();
            }
        }
        if (cnt != 0) {
            return (double)sum / (double)cnt;
        }
        return 0;
    }

    public int getPlaylistLength() {
        int sum_length = 0;
        //summing song lengths from all rate-sets
        for (int rate = 0; rate <= 10; rate++) {
            if (rated_songs.containsKey(rate)){
                TreeSet<Song> set_rate = rated_songs.get(rate);
                for (Song song : set_rate) {
                    sum_length += song.getLength();
                }
            }
        }
        return sum_length;
    }

    public Collection<Song> getRatedSongs() {
        LinkedList<Song> sorted_rated_songs = new LinkedList<>();
        for (int rate = 10; rate >= 0; rate--) {
            if (rated_songs.containsKey(rate)){
                TreeSet<Song> set_rate = rated_songs.get(rate);
                LinkedList<Song> sorted_rated_songs_rate = new LinkedList<>(set_rate);
                sorted_rated_songs_rate.sort((song1, song2) -> {
                    int length_diff = song1.getLength() - song2.getLength();
                    if (length_diff != 0) {
                        return length_diff;
                    }
                    int id_diff = song1.getID() - song2.getID();
                    return -id_diff;
                });
                //adding one rate at a time so that order by rate will be saved as instructed
                sorted_rated_songs.addAll(sorted_rated_songs_rate);
            }
        }
        return sorted_rated_songs;
    }

    public Collection<Song> getFavoriteSongs() {
        LinkedList<Song> favorite_songs = new LinkedList<>();
        //adding all favorite songs to one list
        for (int i=8; i<=10; i++){
            if (rated_songs.containsKey(i)){
                SortedSet<Song> set_rate = rated_songs.get(i);
                favorite_songs.addAll(set_rate);
            }

        }
        //sorting by id as requested
        favorite_songs.sort(Comparator.comparingInt(Song::getID));
        return favorite_songs;
    }

    public User AddFriend(User friend) throws AlreadyFriends , SamePerson{
        if (friends.contains(friend)){
            throw new AlreadyFriends();
        }
        if (friend == this){
            throw new SamePerson();
        }
        friends.add(friend);
        return this;
    }

    public boolean favoriteSongInCommon(User user){
        if (user.getFriends().containsKey(this) && friends.contains(user)){
            Collection<Song> favorite_songs = this.getFavoriteSongs();
            Collection<Song> favorite_songs_user = user.getFavoriteSongs();
            //check if there is a favorite song of this that is in user's favorites
            for (Song song : favorite_songs) {
                if (favorite_songs_user.contains(song))
                    return true;
            }
        }
        return false;
    }

    public Map<User,Integer> getFriends(){
        Map<User,Integer> friends_map = new HashMap<>();
        //adding to the empty map all friend and how many songs they rated
        for (User friend : friends) {
            Collection<Song> rated_songs_friend = friend.getRatedSongs();
            friends_map.put(friend, rated_songs_friend.size());
        }
        return friends_map;
    }

    @Override
    public int compareTo(User user) {
        return this.user_id - user.getID();
    }

    @Override
    public boolean equals (Object o){
        if(o instanceof User){
            return ((User) o).getID() == this.user_id;
        }
        return false;
    }
}
