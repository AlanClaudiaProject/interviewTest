import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
            System.out.println("2. Start program and unit test");
            userInput = grabInputwithExpectedOutcome1or2();
            if(userInput==2) {
                unitTest = true;
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

    private static void exitCall(String[] userInput) {
        role = START;
    }

    private static void cancelCall(String[] userInput) {
        if(role.equalsIgnoreCase(BUYER)){
            if (userInput.length != 3) {
                System.out.println("command have wrong amount of input!");
            }
            else {
                cancel(userInput[1],userInput[2]);
            }
        }
        else {
            System.out.println("incorrect role");
        }
    }

    private static void cancel(String bookingId, String PhoneNumber) {
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
                            System.out.println("booking cancelled!");
                        }
                        else {
                            System.out.println("booking cancellation time exceeded!");
                        }
                    }
                }
            }
        }

        if(!exist) {
            System.out.println("booking ID not found!");
        }
    }

    private static void bookCall(String[] userInput) {
        if(role.equalsIgnoreCase(BUYER)){
            if (userInput.length != 4) {
                System.out.println("command have wrong amount of input!");
            }
            else {
                book(userInput[1],userInput[2],userInput[3]);
            }
        }
        else {
            System.out.println("incorrect role");
        }
    }

    private static void book(String showId, String phoneNumber, String seatsInput) {
        boolean exist = false;
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
            System.out.println("ShowId not found!");
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
                        System.out.println("seat(" + seat + ") taken!");
                    } else {
                        selectedShowBooking[row][seatNum] = new Booking(phoneNumber, showId, seat, BOOKINGDELIMITER);
                        System.out.println("seat(" + seat + ") booked! booking number: " + selectedShowBooking[row][seatNum].getBookingId());
                        try {
                            TimeUnit.MILLISECONDS.sleep(10);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private static void avaCall(String[] userInput) {
        if(role.equalsIgnoreCase(BUYER)){
            if (userInput.length != 2) {
                System.out.println("command have wrong amount of input!");
            }
            else {
                ava(userInput[1]);
            }
        }
        else {
            System.out.println("incorrect role");
        }
    }

    private static void ava(String showId) {
        boolean exist = false;
        Booking[][] selectedShowBooking = null;
        for (Show ea : shows) {
            if(ea.getShowId().equalsIgnoreCase(showId)){
                exist = true;
                selectedShowBooking = ea.getBookings();
            }
        }
        if(!exist) {
            System.out.println("ShowId not found!");
        }
        else {
            for(int a=0;a<selectedShowBooking.length;a++){
                for(int b=0;b<selectedShowBooking[a].length;b++){
                    char seatAphla = (char) (a+65);
                    if(selectedShowBooking[a][b] != null) {
                    }
                    else {
                        System.out.print("The seat "+seatAphla+(b+1)+" ");
                        System.out.println("is empty");
                    }
                }
            }
        }
    }

    private static void viewCall(String[] userInput) {
        if(role.equalsIgnoreCase(ADMIN)) {
            if (userInput.length != 2) {
                System.out.println("Setup command have wrong amount of input!");
            }
            else {
                view(userInput[1]);
            }
        }
        else {
            System.out.println("incorrect role");
        }
    }

    private static void setupCall(String[] userInput) {
        if(role.equalsIgnoreCase(ADMIN)) {
            if (userInput.length != 5) {
                System.out.println("Setup command have wrong amount of input!");
            }
            if(isNumber(userInput[2]) && isNumber(userInput[3]) && isNumber(userInput[4])) {
                int rowNum = Integer.parseInt(userInput[2]);
                int seatNum = Integer.parseInt(userInput[3]);
                int cancelTime = Integer.parseInt(userInput[4]);
                setup( userInput[1], rowNum, seatNum, cancelTime);
            }
        }
        else {
            System.out.println("incorrect role");
        }
    }

    private static void view(String showId) {
        boolean exist = false;
        Booking[][] selectedShowBooking = null;
        for (Show ea : shows) {
            if(ea.getShowId().equalsIgnoreCase(showId)){
                exist = true;
                selectedShowBooking = ea.getBookings();
            }
        }
        if(!exist) {
            System.out.println("ShowId not found!");
        }
        else {
            for(int a=0;a<selectedShowBooking.length;a++){
                for(int b=0;b<selectedShowBooking[a].length;b++){
                    char seatAphla = (char) (a+65);
                    System.out.print("information for seat "+seatAphla+(b+1)+" ");
                    if(selectedShowBooking[a][b] != null) {
                        String ticketId = selectedShowBooking[a][b].getBookingId();
                        String phoneNumber = selectedShowBooking[a][b].getPhoneNumber();
                        System.out.println("is bought. TicketId:"+ticketId+" Buyer phone number:"+phoneNumber);
                    }
                    else {
                        System.out.println("is empty");
                    }
                }
            }
        }
    }

    private static void setup(String showId, int rowMax, int seatMax, int cancelTime) {
        boolean exist = false;
        for (Show ea : shows) {
            if(ea.getShowId().equalsIgnoreCase(showId)){
                exist = true;
                System.out.println("Show Id exists");
            }
        }
        if(!exist){
            if(rowMax > MaxRowPerHall){
                System.out.println("Exceeded max Number of row("+MaxRowPerHall+") allowed!");
            }
            else if (seatMax > MaxSeatPerRow){
                System.out.println("Exceeded max Number of seat per row("+MaxSeatPerRow+") allowed!");
            }
            else {
                Show current = new Show(showId, rowMax, seatMax, cancelTime);
                shows.add(current);
                System.out.println("Show added to system!");
            }
        }
    }

    private static boolean isNumber(String s) {
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
                System.out.println(input);
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
        String name = reader.readLine();

        // Printing the read line
        System.out.println(name);
        return name;
    }

}
