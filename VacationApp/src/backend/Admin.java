package backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class Admin {

    private int adminId;
    private String firstName;
    private String lastName;
    private long passHash;

    public Admin() {
    }

    public Admin(int adminId, String firstName, String lastName, long passHash) {
        this.adminId = adminId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passHash = passHash;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getPassHash() {
        return passHash;
    }

    public void setPassHash(long passHash) {
        this.passHash = passHash;
    }

   public void readData(int id){
       try {
           Object obj = new JSONParser().parse(new FileReader("C:\\Users\\Cejic\\IdeaProjects\\VacationApp\\src\\data\\users"));

           JSONObject jo = (JSONObject) obj;

           JSONArray ja = (JSONArray) jo.get("admins");

           Iterator itr = ja.iterator();

           while (itr.hasNext())
           {
               JSONObject jo1 = (JSONObject) itr.next();

               if((int)(long)jo1.get("adminId") == id){

                   setAdminId(id);
                   setFirstName((String)jo1.get("firstName"));
                   setLastName((String)jo1.get("lastName"));
                   setPassHash((long)jo1.get("passHash"));

                   break;
               }

           }

       } catch (IOException e) {
           e.printStackTrace();
       } catch (ParseException e) {
           e.printStackTrace();
       }
   }
}
