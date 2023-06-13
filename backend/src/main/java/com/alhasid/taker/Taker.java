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
    public Taker(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }
    public Taker(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Taker taker = (Taker) o;

        if (!Objects.equals(id, taker.id)) return false;
        if (!Objects.equals(name, taker.name)) return false;
        if (!Objects.equals(email, taker.email)) return false;
        return Objects.equals(age, taker.age);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        return result;
    }
}
