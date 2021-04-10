package backend;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

public class Employee {

    private int employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private String contractType;
    private Date startDate;
    private long passHash;
    private int pastYearDays;
    private int daysOff;

    public Employee() {
    }

    public Employee(int employeeId, String firstName, String lastName, String position, String contractType, Date startDate, long passHash, int pastYearDays) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.contractType = contractType;
        this.startDate = startDate;
        this.passHash = passHash;
        this.pastYearDays = pastYearDays;
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

    public int getPastYearDays() {
        return pastYearDays;
    }

    public void setPastYearDays(int pastYearDays) {
        this.pastYearDays = pastYearDays;
    }

    public int getDaysOff() {
        return daysOff;
    }

    public void setDaysOff(int daysOff) {
        this.daysOff = daysOff;
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


                    Date deadline = null;
                    String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                    try {
                        deadline = new SimpleDateFormat("yyyy-MM-dd").parse(currentYear + "-05-31");
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

                    if(difference > 0){
                        setPastYearDays((int)(long)jo1.get("pastYearDays"));
                    }else{
                        setPastYearDays(0);
                    }

                    evaluateDaysOff();

                    break;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void evaluateDaysOff(){
        if(this.contractType.equalsIgnoreCase("permanent")){

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            Date today = null;
            try {
                today = new SimpleDateFormat("yyyy-MM-dd").parse(formatter.format(date));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }


            long difference = today.getTime() - startDate.getTime();

            int daysBetween = (int) (difference/(1000*60*60*24));



            if(daysBetween > 365){
                this.daysOff = 20 + pastYearDays;
            }else{
                this.daysOff = 20;
            }
        }

        if(this.contractType.equalsIgnoreCase("temporary")){
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(startDate);

            int startYear = calendar.get(Calendar.YEAR);

            int currentYear = Calendar.getInstance().get(Calendar.YEAR);

            int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

            int fullMonths;

            if(startYear == currentYear){
                int startMonth = calendar.get(Calendar.MONTH);
                int startDay = calendar.get(Calendar.DAY_OF_MONTH);

                if(startDay > 1){
                    daysOff = (20/12) * (currentMonth - startMonth - 1);
                }else{
                    daysOff = (20/12) * (currentMonth - startMonth);
                }
            }else{
                daysOff = (20/12) * currentMonth;
            }
        }
    }
}
