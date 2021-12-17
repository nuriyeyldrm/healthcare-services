package com.backend.healthcare_services.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "confirmation_tokens")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime expiredDate;

    private LocalDateTime confirmedDate;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User users;

    public ConfirmationToken(String token, LocalDateTime createdDate, LocalDateTime expiredDate, User users) {
        this.token = token;
        this.createdDate = createdDate;
        this.expiredDate = expiredDate;
        this.users = users;
    }

    public ConfirmationToken(Long id, String token, LocalDateTime createdDate, LocalDateTime expiredDate, User users) {
        this.id = id;
        this.token = token;
        this.createdDate = createdDate;
        this.expiredDate = expiredDate;
        this.users = users;
    }
}
