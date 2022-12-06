package OOP.Tests;

public class Main{
    public static void main(String[] args) {

        try{
            Tests test = new Tests();
            test.exceptionsTest();
            test = new Tests();
            test.usersTest();
            test = new Tests();
            test.exampleTest();
            /*test = new TechnionTunesImplTest();
            test.canGetAlong();
            test = new TechnionTunesImplTest();
            test.songGames();
            test = new TechnionTunesImplTest();
            test.topLikers();
            test = new TechnionTunesImplTest();*/
        }
        catch (Exception e){
            System.out.println(" got exception : " + e);
        }
    }
}