import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
    private String phoneNumber;
    private String bookingId;
    private LocalDateTime bookingTime;

    public Booking(String phoneNumber, String showId, String seat, String delimiter) {
        this.phoneNumber = phoneNumber;
        this.bookingTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSSS");
        this.bookingId =  showId+delimiter+seat+delimiter+bookingTime.format(formatter);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

}
