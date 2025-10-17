package testcases;

import io.restassured.RestAssured;
import io.restassured.filter.log.*;
import org.testng.annotations.BeforeClass;
import routes.Routes;
import utils.ConfigReader;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BaseClass {

    ConfigReader configReader;

    //For logging
    RequestLoggingFilter requestLoggingFilter;
    ResponseLoggingFilter responseLoggingFilter;

    @BeforeClass
    public void setup() throws FileNotFoundException {
        RestAssured.baseURI= Routes.BASE_URL;

        configReader=new ConfigReader();

        // Setup filters for logging
        FileOutputStream fos = new FileOutputStream(".\\logs\\test_logging.txt");
        PrintStream log = new PrintStream(fos, true);
        requestLoggingFilter = new RequestLoggingFilter(log);
        responseLoggingFilter = new ResponseLoggingFilter(log);
        RestAssured.filters(requestLoggingFilter, responseLoggingFilter);
    }

    //Helper method to check if list is sorted in Descending order

    boolean isSortedDescending(List<Integer> list){
        for (int i = 0; i<list.size()-1; i++) {
            if(list.get(i)<list.get(i+1)){
                return false;
            }
        }
        return true;
    }

    //Helper method to check if list is sorted in Ascending order

    boolean isSortedAscending(List<Integer> list){
        for (int i = 0; i<list.size()-1; i++) {
            if(list.get(i)>list.get(i+1)){
                return false;
            }
        }
        return true;
    }

    //Helper method to check dates fall within the specified range

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public boolean validateCartDatesWithinRange(List<String> cartDates, String startDate, String endDate) {

        LocalDate start = LocalDate.parse(startDate, FORMATTER);
        LocalDate end = LocalDate.parse(endDate, FORMATTER);

        for (String dateTime : cartDates) {
            LocalDate cartDate = LocalDate.parse(dateTime.substring(0, 10), FORMATTER);
            if (cartDate.isBefore(start) || cartDate.isAfter(end)) {
                return false; // Immediately return false if any cart date is out of range
            }
        }
        return true; // All dates are within range
    }
}