package cinema.cinema_app.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PREMIUM")
@NoArgsConstructor
@Data
public class PremiumCinemaHall extends CinemaHall{

    public static Double price = 15.0;
}
