package com.moger.demo.entities;

import com.moger.demo.dataConstants.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Pattern(regexp = "^[A-Za-z0-9]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email format")
    private String email;

    private String password;

    @Column(name = "first_name")
    private String firstName;

    private String lastName;

    @Column(name = "gender_id")
    private Gender gender;

    @Column(name = "user_type_id")
    private String userType;

    private Date created_date;

}
