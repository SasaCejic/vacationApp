package backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class Employee {

    private int employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private String contractType;
    private Date startDate;
    private long passHash;

    public Employee() {
    }

    public Employee(int employeeId, String firstName, String lastName, String position, String contractType, Date startDate, long passHash) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.contractType = contractType;
        this.startDate = startDate;
        this.passHash = passHash;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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

            JSONArray ja = (JSONArray) jo.get("employees");

            Iterator itr = ja.iterator();

            while (itr.hasNext())
            {
                JSONObject jo1 = (JSONObject) itr.next();

                if((int)(long)jo1.get("employeeId") == id){

                    setEmployeeId(id);
                    setFirstName((String)jo1.get("firstName"));
                    setLastName((String)jo1.get("lastName"));
                    setPosition((String) jo1.get("position"));
                    setContractType((String) jo1.get("contractType"));

                    String stringDate = ((String) jo1.get("startDate"));
                    Date date= null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                    setStartDate(date);
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
