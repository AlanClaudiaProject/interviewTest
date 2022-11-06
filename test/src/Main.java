import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class Main {

    private static final String ADMIN = "ADMIN";
    private static final String BUYER = "BUYER";
    private static final String START = "START";
    private static final String END = "END";

    private static final String ADMINACTIONSETUP = "SETUP";
    private static final String ADMINACTIONVIEW = "VIEW";
    //    private static final String BUYERACTIONAVAILABILITY = "AVAILABILITY";
    private static final String BUYERACTIONAVAILABILITY = "AVA";
    private static final String BUYERACTIONBOOK = "BOOK";
    private static final String BUYERACTIONCANCEL = "CANCEL";

    private static final String ROLEACTIONEXIT = "EXIT";

    private static final String COMMANDDELIMITER = " ";
    private static final String SEATDELIMITER = ",";
    private static final String BOOKINGDELIMITER = "-";

    private static ArrayList<Show> shows = new ArrayList<Show>();

    private static final int MaxSeatPerRow = 10;
    private static final int MaxRowPerHall = 26;

    private static boolean debug = false;
    private static boolean unitTest = false;
    private static String role = START;

    public static void main(String[] args) {
        System.out.println("harlow");
        while(!role.equalsIgnoreCase(ROLEACTIONEXIT)) {
            startUserInteraction();
        }
    }

    private static void startUserInteraction() {
        startUserInteraction(role);
    }

    private static void startUserInteraction(String currentState) {
        if(currentState.equalsIgnoreCase(START)){
            int userInput = 0;
            System.out.println("Please select 1 of the below(1 or 2):");
            System.out.println("1. Start program");
            System.out.println("2. Start program and unit Test");
            userInput = grabInputwithExpectedOutcome1or2();
            if(userInput==2) {
                unitTest = true;
                startUnitTest();
                return;
            }


            System.out.println("Please select 1 of the below role(1 or 2):");
            System.out.println("1. Admin");
            System.out.println("2. Buyer");
            userInput = grabInputwithExpectedOutcome1or2();
            if(userInput==1) {
                role = ADMIN;
            }
            else if(userInput==2) {
                role = BUYER;
            }
        }
        if(currentState.equalsIgnoreCase(ADMIN) || currentState.equalsIgnoreCase(BUYER)){
            String[] userInput = grabRoleInput();
            if(userInput[0].equalsIgnoreCase(ADMINACTIONSETUP)){
                setupCall(userInput);
            }
            else if(userInput[0].equalsIgnoreCase(ADMINACTIONVIEW)){
                viewCall(userInput);
            }
            else if(userInput[0].equalsIgnoreCase(BUYERACTIONAVAILABILITY)){
                avaCall(userInput);
            }
            else if(userInput[0].equalsIgnoreCase(BUYERACTIONBOOK)){
                bookCall(userInput);
            }
            else if(userInput[0].equalsIgnoreCase(BUYERACTIONCANCEL)){
                cancelCall(userInput);
            }
            else if(userInput[0].equalsIgnoreCase(ROLEACTIONEXIT)){
                exitCall(userInput);
            }
        }
        System.out.println("Please Enter Command!");
    }

    @Test
    private static void startUnitTest() {
        System.out.println("Starting unit test");
        System.out.println("Admin role");
        role = ADMIN;

        String[] input = new String[]{"setup","mov1","26","10","10"};
        System.out.print("setup mov1 26 10 10 result in true: ");
        assertTrue(setupCall(input));
        System.out.println("success");

        input = new String[]{"setup","mov1","26","10","10"};
        System.out.print("setup mov1 26 10 10 result in false: ");
        assertFalse(setupCall(input));
        System.out.println("success");

        input = new String[]{"setup","mov1","27","10","10"};
        System.out.print("setup mov2 27 10 10 result in false: ");
        assertFalse(setupCall(input));
        System.out.println("success");

        input = new String[]{"setup","mov1","0","10","10"};
        System.out.print("setup mov2 0 10 10 result in false: ");
        assertFalse(setupCall(input));
        System.out.println("success");

        input = new String[]{"setup","mov1","26","11","10"};
        System.out.print("setup mov2 26 11 10 result in false: ");
        assertFalse(setupCall(input));
        System.out.println("success");

        input = new String[]{"setup","mov1","26","0","10"};
        System.out.print("setup mov2 26 0 10 result in false: ");
        assertFalse(setupCall(input));
        System.out.println("success");

        input = new String[]{"setup","mov1"};
        System.out.print("setup mov1 result in false: ");
        assertFalse(setupCall(input));
        System.out.println("success");

        input = new String[]{"view","mov1"};
        System.out.print("View mov1 result in true: ");
        assertTrue(viewCall(input));
        System.out.println("success");

        input = new String[]{"view","mov2"};
        System.out.print("View mov2 result in False: ");
        assertFalse(viewCall(input));
        System.out.println("success");

        input = new String[]{"view"};
        System.out.print("View result in False: ");
        assertFalse(viewCall(input));
        System.out.println("success");

        input = new String[]{"ava","mov1"};
        System.out.print("Ava mov1 result in False: ");
        assertFalse(avaCall(input));
        System.out.println("success");

        input = new String[]{"book","mov1","87741464","A1,A3"};
        System.out.print("book mov1 87741464 a1,A3 result in False: ");
        assertFalse(bookCall(input));
        System.out.println("success");

        input = new String[]{"cancel","mov1-a1-230298301283","87741464"};
        System.out.print("book mov1 87741464 a1,A3 result in False: ");
        assertFalse(bookCall(input));
        System.out.println("success");


        System.out.println();
        System.out.println("Buyer role");
        role = BUYER;

        input = new String[]{"ava","mov1"};
        System.out.print("Ava mov1 result in True: ");
        assertTrue(avaCall(input));
        System.out.println("success");

        input = new String[]{"ava","mov2"};
        System.out.print("Ava mov2 result in False: ");
        assertFalse(avaCall(input));
        System.out.println("success");

        input = new String[]{"ava"};
        System.out.print("Ava result in False: ");
        assertFalse(avaCall(input));
        System.out.println("success");

        input = new String[]{"book","mov1","87741464","A1,A3"};
        System.out.print("book mov1 87741464 a1,A3 result in True: ");
        assertTrue(bookCall(input));
        System.out.println("success");

        input = new String[]{"book","mov1","87741464","A1,A3"};
        System.out.print("book mov1 87741464 a1,A3 result in False: ");
        assertFalse(bookCall(input));
        System.out.println("success");

        input = new String[]{"book","mov1","87741464"};
        System.out.print("book mov1 87741464 result in False: ");
        assertFalse(bookCall(input));
        System.out.println("success");

        input = new String[]{"cancel",shows.get(0).getBookings()[0][0].getBookingId(),"87741464"};
        System.out.print("cancel <ticketID> 87741464 result in False: ");
        assertTrue(cancelCall(input));
        System.out.println("success");

        input = new String[]{"cancel","mov1-a1-230298301283","87741464"};
        System.out.print("cancel mov1-a1-230298301283 87741464 result in False: ");
        assertFalse(cancelCall(input));
        System.out.println("success");

        input = new String[]{"setup","mov2","26","10","10"};
        System.out.print("setup mov2 26 10 10 result in false: ");
        assertFalse(setupCall(input));
        System.out.println("success");

        input = new String[]{"view","mov1"};
        System.out.print("View mov1 result in False: ");
        assertFalse(viewCall(input));
        System.out.println("success");

        role = START;
//        System.exit(1);
        shows = new ArrayList<Show>();
    }

    private static void exitCall(String[] userInput) {
        role = START;
    }

    private static boolean cancelCall(String[] userInput) {
        if(debug){
            System.out.println("cancelCall("+ Arrays.toString(userInput)+")");
        }
        if(role.equalsIgnoreCase(BUYER)){
            if (userInput.length != 3) {
                if(!unitTest) System.out.println("command have wrong amount of input!");
            }
            else {
                return cancel(userInput[1],userInput[2]);
            }
        }
        else {
            if(!unitTest) System.out.println("incorrect role");
        }
        return false;
    }

    private static boolean cancel(String bookingId, String PhoneNumber) {
        if(debug){
            System.out.println("cancel("+bookingId+", "+PhoneNumber+")");
        }
        boolean exist = false;
        Show targetShow = null;
        Booking[][] selectedShowBooking = null;
        String[] bookingdetail = bookingId.split(BOOKINGDELIMITER);
        for (Show ea : shows) {
            if(ea.getShowId().equalsIgnoreCase(bookingdetail[0])){
                selectedShowBooking = ea.getBookings();
                targetShow = ea;
                char rowAlpha = bookingdetail[1].charAt(0);
                int row = rowAlpha;
                row = row - 65;
                int seatNum = Integer.parseInt(bookingdetail[1].substring(1));
                seatNum = seatNum - 1;
                if(row >=0 && seatNum >=0 && row < targetShow.getRows() && seatNum < targetShow.getSeatsPerRow()) {
                    if(selectedShowBooking[row][seatNum].getBookingId().equalsIgnoreCase(bookingId) && selectedShowBooking[row][seatNum].getPhoneNumber().equalsIgnoreCase(PhoneNumber)){
                        exist = true;
                        if(selectedShowBooking[row][seatNum].getBookingTime().plusMinutes(targetShow.getCancellationTime()).isAfter(LocalDateTime.now())) {
                            ea.getBookings()[row][seatNum] = null;
                            if(!unitTest) System.out.println("booking cancelled!");
                        }
                        else {
                            if(!unitTest) System.out.println("booking cancellation time exceeded!");
                        }
                    }
                }
            }
        }

        if(!exist) {
            if(!unitTest) System.out.println("booking ID not found!");
            return false;
        }
        return true;
    }

    private static boolean bookCall(String[] userInput) {
        if(debug){
            System.out.println("bookCall("+ Arrays.toString(userInput)+")");
        }
        if(role.equalsIgnoreCase(BUYER)){
            if (userInput.length != 4) {
                if(!unitTest)System.out.println("command have wrong amount of input!");
            }
            else {
                return book(userInput[1],userInput[2],userInput[3]);
            }
        }
        else {
            if(!unitTest)System.out.println("incorrect role");
        }
        return false;
    }

    private static boolean book(String showId, String phoneNumber, String seatsInput) {
        if(debug){
            System.out.println("book("+showId+", "+phoneNumber+", "+seatsInput+")");
        }
        boolean exist = false;
        boolean seatFound = false;
        Show targetShow = null;
        Booking[][] selectedShowBooking = null;
        for (Show ea : shows) {
            if(ea.getShowId().equalsIgnoreCase(showId)){
                exist = true;
                selectedShowBooking = ea.getBookings();
                targetShow = ea;
            }
        }
        if(!exist) {
            if(!unitTest) System.out.println("ShowId not found!");
            return false;
        }
        else {
            String[] seats = seatsInput.toUpperCase(Locale.ROOT).split(SEATDELIMITER);
            for (String seat : seats) {
                char rowAlpha = seat.charAt(0);
                int row = rowAlpha;
                row = row - 65;
                int seatNum = Integer.parseInt(seat.substring(1));
                seatNum = seatNum - 1;
                if(row >=0 && seatNum >=0 && row < targetShow.getRows() && seatNum < targetShow.getSeatsPerRow()) {
                    if (selectedShowBooking[row][seatNum] != null) {
                        if(!unitTest) System.out.println("seat(" + seat + ") taken!");
                    } else {
                        selectedShowBooking[row][seatNum] = new Booking(phoneNumber, showId, seat, BOOKINGDELIMITER);
                        if(!unitTest) System.out.println("seat(" + seat + ") booked! booking number: " + selectedShowBooking[row][seatNum].getBookingId());
                        seatFound = true;
                        try {
                            TimeUnit.MILLISECONDS.sleep(10);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        return seatFound;
    }

    private static boolean avaCall(String[] userInput) {
        if(debug){
            System.out.println("avaCall("+ Arrays.toString(userInput)+")");
        }
        if(role.equalsIgnoreCase(BUYER)){
            if (userInput.length != 2) {
                if(!unitTest)System.out.println("command have wrong amount of input!");
            }
            else {
                return ava(userInput[1]);
            }
        }
        else {
            if(!unitTest)System.out.println("incorrect role");
        }
        return false;
    }

    private static boolean ava(String showId) {
        if(debug){
            System.out.println("ava("+showId+")");
        }
        boolean exist = false;
        boolean seatFound = false;
        Booking[][] selectedShowBooking = null;
        for (Show ea : shows) {
            if(ea.getShowId().equalsIgnoreCase(showId)){
                exist = true;
                selectedShowBooking = ea.getBookings();
            }
        }
        if(!exist) {
            if(!unitTest)System.out.println("ShowId not found!");
            return false;
        }
        else {
            for(int a=0;a<selectedShowBooking.length;a++){
                for(int b=0;b<selectedShowBooking[a].length;b++){
                    char seatAphla = (char) (a+65);
                    if(selectedShowBooking[a][b] != null) {
                    }
                    else {
                        if(!unitTest)System.out.println("The seat "+seatAphla+(b+1)+" is empty");
                        seatFound = true;
                    }
                }
            }
        }
        if(!seatFound)System.out.println("No empty seat for show!");
        return seatFound;
    }

    private static boolean viewCall(String[] userInput) {
        if(debug){
            System.out.println("viewCall("+ Arrays.toString(userInput)+")");
        }
        if(role.equalsIgnoreCase(ADMIN)) {
            if (userInput.length != 2) {
                if(!unitTest)System.out.println("Setup command have wrong amount of input!");
            }
            else {
                return view(userInput[1]);
            }
        }
        else {
            if(!unitTest)System.out.println("incorrect role");
        }
        return false;
    }

    private static boolean view(String showId) {
        if(debug){
            System.out.println("showId("+showId+")");
        }
        boolean exist = false;
        Booking[][] selectedShowBooking = null;
        for (Show ea : shows) {
            if(ea.getShowId().equalsIgnoreCase(showId)){
                exist = true;
                selectedShowBooking = ea.getBookings();
            }
        }
        if(!exist) {
            if(!unitTest)System.out.println("ShowId not found!");
            return false;
        }
        else {
            for(int a=0;a<selectedShowBooking.length;a++){
                for(int b=0;b<selectedShowBooking[a].length;b++){
                    char seatAphla = (char) (a+65);
                    if(!unitTest)System.out.print("information for seat "+seatAphla+(b+1)+" ");
                    if(selectedShowBooking[a][b] != null) {
                        String ticketId = selectedShowBooking[a][b].getBookingId();
                        String phoneNumber = selectedShowBooking[a][b].getPhoneNumber();
                        if(!unitTest)System.out.println("is bought. TicketId:"+ticketId+" Buyer phone number:"+phoneNumber);
                    }
                    else {
                        if(!unitTest)System.out.println("is empty");
                    }
                }
            }
        }
        return true;
    }

    private static boolean setupCall(String[] userInput) {
        if(debug){
            System.out.println("setupCall("+ Arrays.toString(userInput)+")");
        }
        if(role.equalsIgnoreCase(ADMIN)) {
            if (userInput.length != 5) {
                if(!unitTest)System.out.println("Setup command have wrong amount of input!");
                return false;
            }
            if(isNumber(userInput[2]) && isNumber(userInput[3]) && isNumber(userInput[4])) {
                int rowNum = Integer.parseInt(userInput[2]);
                int seatNum = Integer.parseInt(userInput[3]);
                int cancelTime = Integer.parseInt(userInput[4]);
                return setup( userInput[1], rowNum, seatNum, cancelTime);
            }
        }
        else {
            if(!unitTest)System.out.println("incorrect role");
        }
        return false;
    }

    private static boolean setup(String showId, int rowMax, int seatMax, int cancelTime) {
        if(debug){
            System.out.println("setup("+showId+", "+rowMax+", "+seatMax+", "+cancelTime+")");
        }
        boolean exist = false;
        for (Show ea : shows) {
            if(ea.getShowId().equalsIgnoreCase(showId)){
                exist = true;
                if(!unitTest)System.out.println("Show Id exists");
            }
        }
        if(!exist){
            if(rowMax > MaxRowPerHall && rowMax > 0){
                if(!unitTest)System.out.println("Exceeded max Number of row("+MaxRowPerHall+") allowed!");
            }
            else if (seatMax > MaxSeatPerRow && seatMax > 0){
                if(!unitTest)System.out.println("Exceeded max Number of seat per row("+MaxSeatPerRow+") allowed!");
            }
            else {
                Show current = new Show(showId, rowMax, seatMax, cancelTime);
                shows.add(current);
                if(!unitTest)System.out.println("Show added to system!");
                return true;
            }
        }
        return false;
    }

    private static boolean isNumber(String s) {
        if(debug){
            System.out.println("isNumber("+s+")");
        }
        try{
            int test = Integer.parseInt(s);
        }
        catch(Exception ex){
            return false;
        }
        return true;
    }

    private static String[] grabRoleInput() {
        String[] output = null;
        String input = null;
        do {
            try {
                System.out.print("command: ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                // Reading data using readLine

                input = reader.readLine();

                // Printing the read line
//                System.out.println(input);
                output = input.split(COMMANDDELIMITER);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }while(output == null);
        return output;
    }

    private static int grabInputwithExpectedOutcome1or2(){
        String userInput = null;
        boolean oneFound = false;
        boolean twoFound = false;
        boolean retry = false;
        do {
            try {
                userInput = grabInput(retry);
                if(userInput != null){
                    if(userInput.equalsIgnoreCase("1")){
                        oneFound = true;
                    }
                    else if(userInput.equalsIgnoreCase("2")){
                        twoFound = true;
                    }
                }
                retry = true;
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }while(!oneFound && !twoFound );
        if(oneFound){
            return 1;
        }
        else {
            return 2;
        }
    }

    private static String grabInput(boolean retry) throws IOException {
        if(retry){
            System.out.println("incorrect Answer!");

        }
        System.out.print("Answer: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Reading data using readLine
        String input = reader.readLine();

        // Printing the read line
        if(debug){
            System.out.println("input: "+input);
        }
        return input;
    }

}
