package com.alhasid.taker;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Entity
@Table(name = "taker", uniqueConstraints = {
        @UniqueConstraint(name = "taker_email_unique", columnNames = "email")
})
@Getter
@Setter
@ToString
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
    public Taker(Long id, String name, String email, Integer age, Gender gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
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

        if (!Objects.equals(id, taker.id)) return false;
        if (!name.equals(taker.name)) return false;
        if (!email.equals(taker.email)) return false;
        if (!Objects.equals(age, taker.age)) return false;
        return gender == taker.gender;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + gender.hashCode();
        return result;
    }
}
