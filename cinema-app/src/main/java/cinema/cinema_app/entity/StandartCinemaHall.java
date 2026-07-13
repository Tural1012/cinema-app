package cinema.cinema_app.entity;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("STANDART")
@NoArgsConstructor

public class StandartCinemaHall extends CinemaHall{

    public static Double price = 9.0;
}
