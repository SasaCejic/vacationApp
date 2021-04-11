package main;

import services.Services;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static services.Services.*;


public class Main {

    public static Object currentUser = null;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        login(s);

    }

}
