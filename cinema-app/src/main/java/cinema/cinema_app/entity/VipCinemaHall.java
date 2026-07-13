package cinema.cinema_app.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("VIP")
@NoArgsConstructor
public class VipCinemaHall extends CinemaHall{
    public static Double price = 30.0;
}
