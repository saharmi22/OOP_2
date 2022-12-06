package OOP.Solution;

import OOP.Provided.User;
import OOP.Provided.Song;
import java.util.*;
public class SongImpl implements Song{
    private final int song_id;
    private final int length;
    private final String songName;
    private final String singerName;
    private final Map<User, Integer> userRateMap;

    public SongImpl(int songID, String songName, int length, String singerName){
        this.song_id = songID;
        this.length = length;
        this.songName = songName; //strings in java are immutable so it's safe
        this.singerName = singerName;
        this.userRateMap = new HashMap<>();
    }

    public SongImpl(Song song){
        this.song_id = song.getID();
        this.length = song.getLength();
        this.songName = song.getName(); //strings in java are immutable so it's safe
        this.singerName = song.getSingerName();
        this.userRateMap = new HashMap<>();
    }

    public int getID(){
        return this.song_id;
    }


    public String getName(){
        return this.songName;
    }



    public int getLength(){
        return this.length;
    }


    public String getSingerName(){
        return this.singerName;
    }

    public void rateSong(User user, int rate) throws User.IllegalRateValue,User.SongAlreadyRated{
        if (rate < 0 || rate > 10)
            throw new User.IllegalRateValue();
        if (userRateMap.containsKey(user))
            throw new User.SongAlreadyRated();
        userRateMap.put(user, rate);
    }


    public Collection<User> getRaters(){

        LinkedList<User> sorted_users_rating = new LinkedList<>(); // list of all users
        Map<Integer, Set<User>> ratings_map = getRatings(); //// maps every rate to the set of users that gave that rate
        for (int rate = 10; rate >= 0; rate--) {
            if (ratings_map.containsKey(rate)){
                Set<User> set_rate = ratings_map.get(rate);
                LinkedList<User> sorted_rated_users_rate = new LinkedList<>(set_rate);
                sorted_rated_users_rate.sort((user1, user2) -> {
                    int length_diff = user1.getAge() - user2.getAge();
                    if (length_diff != 0) {
                        return length_diff;
                    }
                    int id_diff = user1.getID() - user2.getID();
                    return -id_diff;
                });
                sorted_users_rating.addAll(sorted_rated_users_rate);
            }
        }
        return sorted_users_rating;

    }


    public Map<Integer,Set<User>> getRatings(){
        Map<Integer, Set<User>> ratings_map = new HashMap<>();
        for (Map.Entry<User, Integer> entry : userRateMap.entrySet()){
            if (ratings_map.containsKey(entry.getValue())){
                ratings_map.get(entry.getValue()).add(entry.getKey());
            }
            else{
                HashSet<User> new_users_set = new HashSet<>();
                new_users_set.add(entry.getKey());
                ratings_map.put(entry.getValue(), new_users_set);
            }
        }
        return ratings_map;
    }


    public double getAverageRating(){
        int count_raters = userRateMap.size();
        int rate_sum = 0;/////////problem?
        for (Map.Entry<User, Integer> entry : userRateMap.entrySet()) {
            rate_sum += entry.getValue();
        }
        return ((double) rate_sum)/((double) count_raters);
    }



    @Override
    public int compareTo(Song song){ return this.song_id-song.getID();}

    @Override
    public boolean equals (Object o){
       if(o instanceof Song){
           return ((Song) o).getID() == this.song_id;
       }
       return false;
    }


}
