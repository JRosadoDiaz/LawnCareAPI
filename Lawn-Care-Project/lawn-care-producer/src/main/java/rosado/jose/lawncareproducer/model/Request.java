package rosado.jose.lawncareproducer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

//@Data
@Entity(name = "Request")
@Table(name = "Request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;



    @Column(name = "TimeScheduled")
    private LocalDate timeScheduled;

    @Column(name = "RequestStatus")
    private String requestStatus;

    @JsonIgnore
    @ManyToOne
    private User usersWithRequest;
}
