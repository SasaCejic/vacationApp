package services;

import backend.Admin;
import backend.Employee;
import main.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Services {

    public static void login(Scanner s){


        String entry = "";

        while (!entry.equalsIgnoreCase("l") && !entry.equalsIgnoreCase("q")) {
            System.out.println("Type 'l' to login or 'q' to exit the application: ");
            entry = s.nextLine().trim();
        }

        if (entry.equalsIgnoreCase("l")) {
            entry = "";

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
            }
            if(type.equalsIgnoreCase("employee")){
                Main.currentUser = (Employee)createUser(firstName,lastName,password,type);
            }

           chooseOptions(s,type);
        }


    }

    public static void chooseOptions(Scanner s, String type){

        String entry = "";

        while (!entry.equalsIgnoreCase("q") && !entry.equalsIgnoreCase("lo")) {
            if(type.equalsIgnoreCase("admin")){



                while(!entry.equalsIgnoreCase("sr") && !entry.equalsIgnoreCase("fv") && !entry.equalsIgnoreCase("se") && !entry.equalsIgnoreCase("lo") && !entry.equalsIgnoreCase("q")){
                    System.out.println("Type \n" + "'sr' to show active requests\n" + "'fv' to show future vacations\n" + "'se' to show employees with their number of days off\n" + "'lo' to logout\n" + "or 'q' to exit\n\n");
                    entry = s.nextLine().trim();
                }
                if (entry.equalsIgnoreCase("sr")) {
                    showActiveRequests(s);
                    while (!entry.equalsIgnoreCase("cs") && !entry.equalsIgnoreCase("lo") && !entry.equalsIgnoreCase("q")) {
                        System.out.println("Type \n" + "'cs' to change status of some of the requests\n" + "'lo' to logout\n" + "or 'q' to exit\n\n");
                        entry = s.nextLine().trim();
                    }
                    if (entry.equalsIgnoreCase("cs")) {
                        System.out.println("Enter employeeId from request you want to change status: ");
                        int employeeId = s.nextInt();
                        changeRequestStatus(s, employeeId);
                    }
                }else if(entry.equalsIgnoreCase("fv")){
                    showFutureVacations(s);
                }else if(entry.equalsIgnoreCase("se")){
                    showEmployees(s);
                }

            }else if(type.equalsIgnoreCase("employee")){

                while (!entry.equalsIgnoreCase("cr") && !entry.equalsIgnoreCase("lo") && !entry.equalsIgnoreCase("q")) {
                    System.out.println("Type \n" + "'cr' to create new request\n" + "'lo' to logout\n" + "or 'q' to exit\n\n");
                    entry = s.nextLine().trim();
                }
                if (entry.equalsIgnoreCase("cr")) {
                    createRequest(s, (Employee)Main.currentUser);
                }
            }

        }
        if(entry.equalsIgnoreCase("lo")){
            logout(s);
        }

    }

    public static Object createUser(String firstName, String lastName, String password, String type){
        try {
            if (type.equalsIgnoreCase("admin")) {
                Object obj = new JSONParser().parse(new FileReader("src\\data\\users"));

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
                Object obj = new JSONParser().parse(new FileReader("src\\data\\users"));

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

        Map<String,String> values = new HashMap<>();
        values.put("firstName",emp.getFirstName());
        values.put("lastName",emp.getLastName());
        values.put("dateFrom",firstDay.toString());
        values.put("dateUntil",lastDay.toString());
        values.put("numberOfDays",String.valueOf((int)((lastDay.getTime()-firstDay.getTime())/(1000*60*60*24))));
        values.put("year",String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));


        String urlString = "https://vacationapi.000webhostapp.com/";

        sendPOSTRequest(urlString,values);

        checkRequest(s, emp, firstDay, lastDay);

        chooseOptions(s,"employee");
    }

    public static void checkRequest(Scanner s, Employee emp, Date firstDay, Date lastDay){
        boolean validRequest = true;


        if(((int)(lastDay.getTime() - firstDay.getTime())/(1000*60*60*24)) > emp.getDaysOff()){
            validRequest = false;
        }

        try {
            Object obj = new JSONParser().parse(new FileReader("src\\data\\requests"));

            JSONObject jo = (JSONObject) obj;

            JSONArray ja = (JSONArray) jo.get("requests");

            Iterator itr = ja.iterator();

            while (itr.hasNext()) {
                JSONObject jo1 = (JSONObject) itr.next();

                String status = (String)jo1.get("status");

                if(emp.getPosition().equalsIgnoreCase(findPositionById((int)(long)jo1.get("employeeId"))) && !(status.equalsIgnoreCase("denied")) && !(status.equalsIgnoreCase("expired"))){

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
            JSONParser jsonParser = new JSONParser();

            Employee user = (Employee) Main.currentUser;

            try {
                Object obj = new JSONParser().parse(new FileReader("src\\data\\requests"));

                JSONObject jo = (JSONObject) obj;

                JSONArray ja = (JSONArray) jo.get("requests");

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String firstDayString = df.format(firstDay);
                String lastDayString = df.format(lastDay);

                JSONObject request = new JSONObject();
                request.put("employeeId", user.getEmployeeId());
                request.put("firstDay", firstDayString);
                request.put("lastDay", lastDayString);
                request.put("status", "processing");

                ja.add(request);

                FileWriter file = new FileWriter("src\\data\\requests");
                file.write( "{ \"requests\":" + ja.toJSONString() + "}");
                file.flush();
                file.close();

            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }

            System.out.println("You created request successfully");
            login(s);
        }else{
            System.out.println("You cannot create request. Try again");
            login(s);
        }
    }

    public static String findPositionById(int id){
        try {
            Object obj = new JSONParser().parse(new FileReader("src\\data\\users"));

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

    public static void showActiveRequests(Scanner s){
        try {
            Object obj = new JSONParser().parse(new FileReader("src\\data\\requests"));

            JSONObject jo = (JSONObject) obj;

            JSONArray ja = (JSONArray) jo.get("requests");

            Iterator itr = ja.iterator();

            while (itr.hasNext()) {
                JSONObject jo1 = (JSONObject) itr.next();

                if (((String)jo1.get("status")).equalsIgnoreCase("processing")){
                    System.out.println("================= ACTIVE REQUEST ===================");
                    System.out.println("First day: " + (String)jo1.get("firstDay"));
                    System.out.println("Last day: " + (String)jo1.get("lastDay"));
                    System.out.println("EmployeeId: " + (int)(long)jo1.get("employeeId"));
                    System.out.println("====================================================");
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        chooseOptions(s,"admin");
    }

    public static void changeRequestStatus(Scanner s, int employeeId){
        try {
            Object obj = new JSONParser().parse(new FileReader("src\\data\\requests"));

            JSONObject jo = (JSONObject) obj;

            JSONArray ja = (JSONArray) jo.get("requests");

            String message = "Error. Try again.";

                    for(Object jsonObject: ja){

                        if((int)(long)(((JSONObject) jsonObject).get("employeeId")) == employeeId){
                            System.out.println("Choose new status");
                            System.out.println("1. approved");
                            System.out.println("2. denied");
                            System.out.println("Choose number: ");
                            int number = s.nextInt();

                            if(number == 1){
                                ((JSONObject) jsonObject).put("status","approved");
                                message = "You approved request successfully.";
                            }
                            if(number == 2){
                                ((JSONObject) jsonObject).put("status","denied");
                                message = "You denied request successfully";
                            }
                            break;

                        }
                    }
            FileWriter file = new FileWriter("src\\data\\requests");
            file.write( "{ \"requests\":" + ja.toJSONString() + "}");
            file.flush();
            file.close();

            System.out.println(message);

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        chooseOptions(s,"admin");
    }

    public static void logout(Scanner s){
        Main.currentUser = null;
        System.out.println("You logged out successfully.");
        login(s);
    }

    public static void sendPOSTRequest(String urlString, Map<String,String> values){
        try {
            URL url = new URL(urlString);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            StringJoiner sj = new StringJoiner("&");
            for(Map.Entry<String,String> entry : values.entrySet())
                sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                        + URLEncoder.encode(entry.getValue(), "UTF-8"));
            byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            http.connect();
            try(OutputStream os = http.getOutputStream()) {
                os.write(out);
            }


            URI uri= new URI(urlString);

            java.awt.Desktop.getDesktop().browse(uri);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void showFutureVacations(Scanner s){
        try {
            Object obj = new JSONParser().parse(new FileReader("src\\data\\requests"));

            JSONObject jo = (JSONObject) obj;

            JSONArray ja = (JSONArray) jo.get("requests");

            Iterator itr = ja.iterator();

            while (itr.hasNext()) {
                JSONObject jo1 = (JSONObject) itr.next();

                if (((String)jo1.get("status")).equalsIgnoreCase("approved")){
                    System.out.println("================= ACTIVE REQUEST ===================");
                    System.out.println("First day: " + (String)jo1.get("firstDay"));
                    System.out.println("Last day: " + (String)jo1.get("lastDay"));
                    System.out.println("EmployeeId: " + (int)(long)jo1.get("employeeId"));
                    System.out.println("====================================================");
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        chooseOptions(s,"admin");
    }

    public static void showEmployees(Scanner s){
        try {
            Object obj = new JSONParser().parse(new FileReader("src\\data\\users"));

            JSONObject jo = (JSONObject) obj;

            JSONArray ja = (JSONArray) jo.get("employees");

            Iterator itr = ja.iterator();

            while (itr.hasNext()) {
                JSONObject jo1 = (JSONObject) itr.next();

                Employee emp = new Employee();

                emp.setEmployeeId((int)(long)jo1.get("employeeId"));
                emp.setFirstName((String)jo1.get("firstName"));
                emp.setLastName((String)jo1.get("lastName"));
                emp.setPosition((String) jo1.get("position"));
                emp.setContractType((String) jo1.get("contractType"));

                String stringDate = ((String) jo1.get("startDate"));
                Date date= null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                emp.setStartDate(date);
                emp.setPassHash((long)jo1.get("passHash"));


                Date deadline = null;
                String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                try {
                    deadline = new SimpleDateFormat("yyyy-MM-dd").parse(currentYear + "-06-30");
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = new Date();
                Date today = null;
                try {
                    today = new SimpleDateFormat("yyyy-MM-dd").parse(formatter.format(date1));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }


                int difference = (int)(today.getTime() - deadline.getTime());

                if(difference < 0){
                    emp.setPastYearDays((int)(long)jo1.get("pastYearDays"));
                }else{
                    emp.setPastYearDays(0);
                }

                emp.evaluateDaysOff();

                System.out.println("================= EMPLOYEE ====================");
                System.out.println("Name: " + emp.getFirstName() + " " + emp.getLastName());
                System.out.println("Position:" + emp.getPosition());
                System.out.println("Contract type: " + emp.getContractType());
                System.out.println("Start date:" + emp.getStartDate());
                System.out.println("Days off: " + emp.getDaysOff());
                System.out.println("===============================================");

            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        chooseOptions(s,"admin");
    }
}
