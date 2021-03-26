package main;


import backend.Admin;
import backend.Employee;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Main {

    public static Object currentUser = null;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        login(s);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        System.out.println(Calendar.getInstance().get(Calendar.MONTH));
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

            currentUser = (Admin)createUser(firstName,lastName,password,type);

        }else if(type.equalsIgnoreCase("employee")){
            currentUser = (Employee)createUser(firstName,lastName,password,type);
        }

        if(currentUser != null){
            System.out.println("Login successfull");
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
}
