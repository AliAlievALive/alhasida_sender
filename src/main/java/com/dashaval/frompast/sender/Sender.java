package com.dashaval.frompast.sender;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "sender", uniqueConstraints = {
        @UniqueConstraint(name = "sender_email_unique", columnNames = "email")
})
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Sender {
    @Id
    @SequenceGenerator(name = "sender_id_seq", sequenceName = "sender_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sender_id_seq")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    private Integer age;

    public Sender(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Sender(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
}
