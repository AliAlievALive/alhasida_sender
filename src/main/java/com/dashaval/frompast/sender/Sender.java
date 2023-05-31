package com.dashaval.frompast.sender;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Sender {
    @Id
    @SequenceGenerator(name = "sender_id_sequence", sequenceName = "sender_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sender_id_sequence")
    private Long id;
    @Column(
            nullable = false
    )
    private String name;
    @Column(
            nullable = false
    )
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
