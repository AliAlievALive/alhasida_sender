package com.alhasid.sender;

import com.alhasid.taker.Taker;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "sender", uniqueConstraints = {
        @UniqueConstraint(name = "sender_email_unique", columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
public class Sender implements UserDetails {
    @Id
    @SequenceGenerator(name = "sender_id_seq", sequenceName = "sender_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sender_id_seq")
    private Long id;
    @Column(nullable = false)
    private String email;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "sender", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Taker> takers;

    @Column(nullable = false)
    private String password;

    public Sender(long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Sender(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Sender(String email, String password, List<Taker> takers) {
        this.email = email;
        this.password = password;
        this.takers = takers;
    }

    public Sender(Long id, String email, String password, List<Taker> takers) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.takers = takers;
    }

    @Override
    public String toString() {
        return "Sender{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", takers=" + takers +
                ", password='" + password + '\'' +
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
