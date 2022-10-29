import java.util.ArrayList;

public class Show {

    private String showId;
    private int rows;
    private int seatsPerRow;
    private int cancellationTime;
    private Booking[][] bookings;

    public Booking[][] getBookings() {
        return bookings;
    }

    public void setBookings(Booking[][] bookings) {
        this.bookings = bookings;
    }

    public Show(String showId, int rows, int seatsPerRow, int cancellationTime) {
        this.showId = showId;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
        this.cancellationTime = cancellationTime;
        bookings = new Booking[rows][seatsPerRow];
    }


    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(int seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }

    public int getCancellationTime() {
        return cancellationTime;
    }

    public void setCancellationTime(int cancellationTime) {
        this.cancellationTime = cancellationTime;
    }
}
