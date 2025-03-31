package com.library.demo.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users_tbl")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private int score;

    private String address;

    private String phone;

    private LocalDate birthDate;

    private String email;

    private String password;

    private LocalDateTime createDate;

    private String resetPasswordCode;

    private Boolean builtIn;

    @OneToMany(mappedBy = "user")
    private List<Loan> loans;

    @ManyToMany
    @JoinTable(name = "User_Role_tbl",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

}
