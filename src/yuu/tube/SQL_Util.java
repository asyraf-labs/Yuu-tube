package yuu.tube;

import java.sql.*;
import java.util.ArrayList;

public class SQL_Util {
    public static final String credentials = "jdbc:mysql://localhost:3306/YoutubeDB?user=root&password=tropika17&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static Connection connection = null;

    public static void initConnection() {
        if (connection != null){
            System.out.println("[WARN] Connection has already been established.");
            return;
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(credentials);
//            System.out.println("Yey, connected to YoutubeDB");
        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    public static void addUser(String username, String email, String password){
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO user(username, email, password) VALUES(?,?,?)");
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3,password);
            ps.execute();
            ps.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void addVideo(String youtubeChannelName, String fileName, String title){
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO video(title, fileName, youtubeName) VALUES(?,?,?)");
            ps.setString(1, title);
            ps.setString(2, fileName);
            ps.setString(3, youtubeChannelName);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addVideoToUser(int uid, int vid){
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO VideoUser(uid, vid) VALUES(?,?)");
            ps.setInt(1, uid);
            ps.setInt(2, vid);
            ps.execute();
            ps.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static int getUid(String username){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM user WHERE username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                int UID = rs.getInt("uid");
                ps.close();
                rs.close();
//                System.out.println("UID for " + username + " : " + UID);
                return UID;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public static int getVid(String title){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM video WHERE title=?");
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                int VID = rs.getInt("vid");
                ps.close();
                rs.close();
//                System.out.println("VID for " + title + " : " + VID);
                return VID;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public static void isSignedUp(String email, String password){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT uid FROM user WHERE email=? AND password=?");
            ps.setString(1,email);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int isExist = rs.getInt("uid");
                String stringIsExist = Integer.toString(isExist);
//                System.out.println(stringIsExist);
                if (!stringIsExist.equals("null")) {
                    System.out.println("Welcome back 👋");
                } else {
                    System.out.println("you need to Sign Up first 😊");
                }
                ps.close();
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int returnUserVideo(int videoId){
       try {
           PreparedStatement ps = connection.prepareStatement("SELECT * FROM VideoUser Where uid=?");
           ps.setInt(1, videoId);
           ResultSet rs = ps.executeQuery();
           ArrayList<Integer> list= new ArrayList<Integer>();
           while (rs.next()){
               list.add(rs.getInt("vid"));
           }
           Integer[] result = new Integer[list.size()];
           result = list.toArray(result);

           for(int i = 0; i < result.length ; i++){
               displayVideoList(result[i]);
//               System.out.println(i + " " + result[i]);
           }
           ps.close();
           rs.close();
       } catch (SQLException e){
           e.printStackTrace();
       }
       return -1;
    }

    public static void displayVideoList(int vidId){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT vid, title FROM video WHERE vid=?");
            ps.setInt(1, vidId);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> list = new ArrayList<String>();
            while (rs.next()){
                list.add(rs.getString("title"));
            }
            String[] result = new String[list.size()];
            result = list.toArray(result);

            for(int i = 0; i < result.length ; i++){
                System.out.println("👉 " + result[i]);
            }

            ps.close();
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static String findFileForThatVideo(String videoTitle){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM video WHERE title=?");
            ps.setString(1, videoTitle);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String videoFile = rs.getString("fileName");
                ps.close();
                rs.close();
                return videoFile;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return "";
    }

    public static void userDetails(String username){
        try {
            PreparedStatement ps = connection.prepareStatement("select  * from YoutubeDB.user where username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String name = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");

                System.out.println(" ");
                System.out.println("Username : " + name);
                System.out.println("Email : " + email);
                System.out.println("Password : " + password);
                ps.close();
                rs.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void editUsername(String oldUsername, String newUsername){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE user SET username=? WHERE username=?");
            ps.setString(1, newUsername);
            ps.setString(2, oldUsername);
            ps.execute();
            ps.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void editEmail(String oldEmail, String newEmail){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE user SET email=? WHERE email=?");
            ps.setString(1, newEmail);
            ps.setString(2, oldEmail);
            ps.execute();
            ps.close();
        } catch (SQLException e){

        }
    }

    public static void editPassword(String oldPass, String newPass){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE user SET password=? WHERE password=?");
            ps.setString(1, newPass);
            ps.setString(2, oldPass);
            ps.execute();
            ps.close();
        } catch (SQLException e){

        }
    }

    public static String getEmail(String username){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT email FROM user WHERE username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String email = rs.getString("email");
                ps.close();
                rs.close();
                return email;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return "";
    }

    public static String getPassword(String username){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT password FROM user WHERE username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String password = rs.getString("password");
                ps.close();
                rs.close();
                return password;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return "";
    }

    public static String displayYoutubeChannelName(){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT youtubeName FROM video");
            ResultSet rs = ps.executeQuery();
            ArrayList<String> list = new ArrayList<String>();
            while (rs.next()){
                list.add(rs.getString("youtubeName"));
            }
            String[] result = new String[list.size()];
            result = list.toArray(result);

            for(int i = 0; i < result.length ; i++){
                System.out.println("👉 " + result[i]);
            }

            ps.close();
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return "";
    }

    public static void displayVideoListFromYoutubeChannel(String youtubeChannelName){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT vid, title FROM video WHERE youtubeName=?");
            ps.setString(1, youtubeChannelName);
            ResultSet rs = ps.executeQuery();
            ArrayList<String> list = new ArrayList<String>();
            while (rs.next()){
                list.add(rs.getString("title"));
            }
            String[] result = new String[list.size()];
            result = list.toArray(result);

            for(int i = 0; i < result.length ; i++){
                System.out.println("▶️ " + result[i]);
            }

            ps.close();
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}
