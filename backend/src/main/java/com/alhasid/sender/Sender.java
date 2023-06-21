package com.alhasid.sender;

import com.alhasid.taker.Taker;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "sender", uniqueConstraints = {
        @UniqueConstraint(name = "sender_email_unique", columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
public class Sender {
    @Id
    @SequenceGenerator(name = "sender_id_seq", sequenceName = "sender_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sender_id_seq")
    private Long id;
    @Column(nullable = false)
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Taker> takers;

    public Sender(long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Sender(String email) {
        this.email = email;
    }

    public Sender(String email, List<Taker> takers) {
        this.email = email;
        this.takers = takers;
    }

    public Sender(Long id, String email, List<Taker> takers) {
        this.id = id;
        this.email = email;
        this.takers = takers;
    }

    @Override
    public String toString() {
        return "Sender{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", takers=" + takers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sender sender = (Sender) o;

        if (!id.equals(sender.id)) return false;
        if (!email.equals(sender.email)) return false;
        return Objects.equals(takers, sender.takers);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + (takers != null ? takers.hashCode() : 0);
        return result;
    }
}
