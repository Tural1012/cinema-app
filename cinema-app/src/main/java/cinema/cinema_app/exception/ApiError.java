package cinema.cinema_app.exception;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


import java.time.Instant;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiError {
    Instant instant =  Instant.now();
    int status;
    String message;

    public ApiError( int status, String message) {
        this.status = status;
        this.message = message;
    }

}
