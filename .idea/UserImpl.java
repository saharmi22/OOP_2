import java.util.SortedSet;
import java.util.HashMap;
import Song;
import java.util.Comparator;

public class UserImpl implements User {
    private int user_id;
    private String user_name;
    private int user_age;
    private HashMap<int, SortedSet<Song>> rated_songs;
    private List<User> friends;

    public UserImpl(int userId, String userName, int userAge) {
        user_id = userId;
        user_name = userName;
        user_age = userAge;
        rated_songs = new HashMap<int, SortedSet<Song>>();
    }

    int getID() {
        return user_id;
    }

    String getName() {
        return user_name;
    }

    int getAge() {
        return user_age;
    }

    User rateSong(final Song song, int rate) throws IllegalRateValue, SongAlreadyRated {
        if (rate < 0 || rate > 10) {
            throw new IllegalRateValue();
        }
        boolean song_exists = false;
        for (int i = 0; (i <= 10 && !song_exists); i++) {
            SortedSet<Song> songs_rated_i = rated_songs.get(i);
            if (songs_rated_i != NULL) {
                if (songs_rated_i.Contains(song))
                    song_exists = true;
            }
        }
        if (song_exists) {
            throw new SongAlreadyRated();
        }
        SortedSet<Song> songs_rate = rated_songs.get(rate);
        if (songs_rate != NULL) {
            songs_rate.add(Song);
        } else {
            SortedSet<Song> new_song_set = new SortedSet<Song>();
            new_song_set.add(Song);
            songs_rate.put(rate, new_song_set)
        }
        return User(this);
    }

    double getAverageRating() {
        int sum = 0;
        int cnt = 0;
        for (int rate = 0; rate <= 10; rate++) {
            SortedSet<Song> set_rate = rated_songs.get(Rate);
            if (set_rate != NULL) {
                sum += rate * set_rate.size();
                cnt += set_rate.size();
            }
        }
        if (cnt != 0) {
            return sum / cnt;
        }
        return 0;
    }

    int getPlaylistLength() {
        int sum_length = 0;
        for (int rate = 0; rate <= 10; rate++) {
            SortedSet<Song> set_rate = rated_songs.get(Rate);
            if (set_rate != NULL) {
                Iterator<Song> it = set_rate.iterator();
                while (it.hasNext()) {
                    Song song = it.next();
                    sum_length += song.getLength();
                }
            }
        }
        return sum_length;
    }

    Collection<Song> getRatedSongs() {
        List<Song> sorted_rated_songs = new List<Song>();
        for (int rate = 10; rate >= 0; rate--) {
            SortedSet<Song> set_rate = rated_songs.get(Rate);
            if (set_rate != NULL) {
                List<Song> sorted_rated_songs_rate = new List<Song>();
                Iterator<Song> it = set_rate.iterator();
                while (it.hasNext()) {
                    Song song = it.next();
                    sorted_rated_songs_rate.add(song);
                }
                Collections.sort(sorted_rated_songs_rate, new Comperator<Song>() {
                    public int compare(Song song1, Song song2) {
                        int length_diff = song1.getLength() - song2.getLength();
                        if (length_diff != 0) {
                            return length_diff;
                        }
                        int id_diff = song1.getID() - song2.getID();
                        return -id_diff
                    }
                });
                Iterator<Song> it = sorted_rated_songs_rate.iterator();
                while (it.hasNext()) {
                    Song song = it.next();
                    sorted_rated_songs.add(song);
                }
            }
        }
        return sorted_rated_songs;
    }

    Collection<Song> getFavoriteSongs() {
        SortedList<Song> favorite_songs = new List<Song>();
        for (int i=8; i<=10; i++){
            SortedSet<Song> set_rate = rated_songs.get(Rate);
            Iterator<Song> it = set_rate.iterator();
            while (it.hasNext()) {
                Song song = it.next();
                favorite_songs.add(song);
            }
        }
        return favorite_songs;
    }

    User AddFriend(User friend) throws AlreadyFriends , SamePerson{
        if (friends.contains(friend)){
            throw new AlreadyFriends();
        }
        if (friend == this){
            throw new SamePerson();
        }
        friends.add(friend);
    }

    boolean favoriteSongInCommon(User user){
        Collection<Song> favorite_songs = this.getFavoriteSongs();
        Collection<Song> favorite_songs_user = user.getFavoriteSongs();
        Iterator<Song> it = favorite_songs.iterator();
        while (it.hasNext()) {
            Song song = it.next();
            if (favorite_songs_user.contains(song))
                return true;
        }
        return false;
    }

    Map<User,Integer> getFriends(){
        Map<User,Integer> friends_map = new Map<User, Integer>();
        Iterator<User> it = friends.iterator();
        while (it.hasNext()) {
            User friend = it.next();
            Collection<Song> rated_songs_friend = friend.getRatedSongs();
            friends_map.put(friend, rated_songs_friend.size());
        }
    }

}