package services;

import backend.Admin;
import backend.Employee;
import main.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Services {
    public Services() {
    }

    public static void login(Scanner s){
        System.out.println("Login");
        System.out.println("Type of user (admin/employee): ");
        String type = s.nextLine().trim();

        System.out.println("First name: ");
        String firstName = s.nextLine().trim();

        System.out.println("Last name: ");
        String lastName = s.nextLine().trim();

        System.out.println("Password: ");
        String password = s.nextLine().trim();

        if(type.equalsIgnoreCase("admin")){

            Main.currentUser = (Admin)createUser(firstName,lastName,password,type);

        }else if(type.equalsIgnoreCase("employee")){
            Main.currentUser = (Employee)createUser(firstName,lastName,password,type);
            createRequest(s, (Employee)Main.currentUser);
        }

        if(Main.currentUser != null){
            System.out.println("Login successful");
        }else{
            System.out.println("Try again");
        }
    }

    public static Object createUser(String firstName, String lastName, String password, String type){
        try {
            if (type.equalsIgnoreCase("admin")) {
                Object obj = new JSONParser().parse(new FileReader("C:\\Users\\Cejic\\IdeaProjects\\VacationApp\\src\\data\\users"));

                JSONObject jo = (JSONObject) obj;

                JSONArray ja = (JSONArray) jo.get("admins");

                Iterator itr = ja.iterator();

                while (itr.hasNext())
                {
                    JSONObject jo1 = (JSONObject) itr.next();

                    if(((String) jo1.get("firstName")).equalsIgnoreCase(firstName) && ((String) jo1.get("lastName")).equalsIgnoreCase(lastName) && (long) jo1.get("passHash") == password.hashCode()){

                        Admin a = new Admin();

                        a.readData((int)(long)jo1.get("adminId"));


                        return a;
                    }

                }
            }
            if(type.equalsIgnoreCase("employee")){
                Object obj = new JSONParser().parse(new FileReader("C:\\Users\\Cejic\\IdeaProjects\\VacationApp\\src\\data\\users"));

                JSONObject jo = (JSONObject) obj;

                JSONArray ja = (JSONArray) jo.get("employees");

                Iterator itr = ja.iterator();

                while (itr.hasNext())
                {
                    JSONObject jo1 = (JSONObject) itr.next();


                    if(((String) jo1.get("firstName")).equalsIgnoreCase(firstName) && ((String) jo1.get("lastName")).equalsIgnoreCase(lastName) && (long) jo1.get("passHash") == password.hashCode()){

                        Employee e = new Employee();

                        e.readData((int)(long)jo1.get("employeeId"));

                        return e;
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createRequest(Scanner s, Employee emp){
        System.out.println("When you want to start your vacation? (yyyy-MM-dd)");

        String stringFirstDay = s.nextLine().trim();

        Date firstDay = null;
        try {
            firstDay = new SimpleDateFormat("yyyy-MM-dd").parse(stringFirstDay);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        System.out.println("When you want to end your vacation? (yyyy-MM-dd)");

        String stringLastDay = s.nextLine().trim();

        Date lastDay = null;
        try {
            lastDay = new SimpleDateFormat("yyyy-MM-dd").parse(stringLastDay);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        checkRequest(s, emp, firstDay, lastDay);
    }

    public static void checkRequest(Scanner s, Employee emp, Date firstDay, Date lastDay){
        boolean validRequest = true;

        if((int)(lastDay.getTime() - firstDay.getTime()) > emp.getDaysOff()){
            validRequest = false;
        }

        try {
            Object obj = new JSONParser().parse(new FileReader("C:\\Users\\Cejic\\IdeaProjects\\VacationApp\\src\\data\\requests"));

            JSONObject jo = (JSONObject) obj;

            JSONArray ja = (JSONArray) jo.get("requests");

            Iterator itr = ja.iterator();

            while (itr.hasNext()) {
                JSONObject jo1 = (JSONObject) itr.next();

                String status = (String)jo1.get("status");

                if(emp.getPosition().equalsIgnoreCase(findPositionById((int)(long)jo1.get("employeeId"))) && !(status.equalsIgnoreCase("denied"))){

                    //first and last day from json file
                    Date firstDay1 = new SimpleDateFormat("yyyy-MM-dd").parse((String)jo1.get("firstDay"));
                    Date lastDay1 = new SimpleDateFormat("yyyy-MM-dd").parse((String)jo1.get("lastDay"));

                    //ArrayList with dates between first and last from json file
                    ArrayList<Date> dates1 = new ArrayList<Date>();

                    Date d = firstDay1;

                    while(d.getTime() <= lastDay1.getTime()){
                        dates1.add(d);

//                        we're adding one day
                        Date d1 = new Date(d.getTime() + 1000*60*60*24);

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        d = new SimpleDateFormat("yyyy-MM-dd").parse(formatter.format(d1));
                    }

                    //ArrayList with dates between first and last requested day
                    ArrayList<Date> dates = new ArrayList<Date>();

                    Date dt = firstDay;

                    while(dt.getTime() <= lastDay.getTime()){
                        dates.add(d);

                        Date dt1 = new Date(d.getTime() + 1000*60*60*24);

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        dt = new SimpleDateFormat("yyyy-MM-dd").parse(formatter.format(dt1));
                    }

                    if(compareDateLists(dates,dates1)){
                        validRequest = false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        if(validRequest){
            System.out.println("You created request successfully");
        }else{
            System.out.println("You cannot create request. Try again");
        }
    }

    public static String findPositionById(int id){
        try {
            Object obj = new JSONParser().parse(new FileReader("C:\\Users\\Cejic\\IdeaProjects\\VacationApp\\src\\data\\users"));

            JSONObject jo = (JSONObject) obj;

            JSONArray ja = (JSONArray) jo.get("employees");

            Iterator itr = ja.iterator();

            while (itr.hasNext()) {
                JSONObject jo1 = (JSONObject) itr.next();

                return (String)jo1.get("position");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean compareDateLists(ArrayList<Date> arr, ArrayList<Date> arr1){
        for(Date d : arr){
            for(Date d1 : arr1){
                if(d.getTime() == d1.getTime()){
                    return true;
                }
            }
        }
        return false;
    }
}
