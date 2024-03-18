package com.george.userService.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class EmailVerificationToken
 {
     @Id
     private String token;
     private Date createdAt;
     @OneToOne
     private User user;


}
