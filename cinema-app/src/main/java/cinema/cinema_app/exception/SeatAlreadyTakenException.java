package cinema.cinema_app.exception;

/**
 * Thrown when a seat is booked concurrently and the DB unique constraint
 * (uk_ticket_seat) rejects the second insert. Mapped to HTTP 409 Conflict.
 */
public class SeatAlreadyTakenException extends RuntimeException {
    public SeatAlreadyTakenException(String message) {
        super(message);
    }
}