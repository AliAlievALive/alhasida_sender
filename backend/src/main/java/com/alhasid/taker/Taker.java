package com.alhasid.taker;

import com.alhasid.sender.Sender;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "taker")
@Getter
@Setter
@NoArgsConstructor
public class Taker {
    @Id
    @SequenceGenerator(name = "taker_id_seq", sequenceName = "taker_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taker_id_seq")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    private Integer age;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonBackReference
    private Sender sender;
    public Taker(Long id, String name, String email, Integer age, Gender gender, Sender sender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.sender = sender;
    }

    public Taker(Long id, String name, String email, Integer age, Gender gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
    }

    public Taker(String name, String email, Integer age, Gender gender, Sender sender) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.sender = sender;
    }

    public Taker(String name, String email, Integer age, Gender gender) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Taker taker = (Taker) o;

        if (!id.equals(taker.id)) return false;
        if (!name.equals(taker.name)) return false;
        if (!email.equals(taker.email)) return false;
        if (!age.equals(taker.age)) return false;
        if (gender != taker.gender) return false;
        return sender.equals(taker.sender);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + age.hashCode();
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + sender.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Taker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                '}';
    }
}
