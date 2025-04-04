package org.simsimbooks.couponserver.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "user_name", length = 50)
    private String name;

    private String email;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;


}
