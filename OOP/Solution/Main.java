package OOP.Solution;

import OOP.Provided.User;

import java.util.Map;

public class Main{
    public static void main(String[] args){
        User user = new UserImpl(1, "sahar", 20);
        int user_id = user.getID();
        int user_age = user.getAge();
        String user_name = user.getName();
        User user2 = new UserImpl(2, "noa", 18);
        User user3 = new UserImpl(3, "elinor", 19);
        try {
            user.AddFriend(user2);
        }
        catch (Exception ignored){

        }
        try {
            user.AddFriend(user3);
        }
        catch (Exception ignored){

        }
        Map<User, Integer> map = user.getFriends();
    }
}