package rosado.jose.lawncareproducer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "Users")
@Table(name = "Users")
@Getter
@Setter
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private int id;
    private String userName;
    private String password;
    private String email;
    private String address;
    @JsonProperty
    @JsonSerialize(using=NumericBooleanSerializer.class)
    private boolean isContractor;
    private String authorityString;

    public void setAuthorityString(String auth) {
        this.authorityString = auth;
        authorities.add(auth);
    }

//    @OneToMany(mappedBy = "usersWithRequest")
////    @JsonIgnore
//    private List<Request> previousRequests = new ArrayList<>();

    @Transient
    private List<String> authorities = new ArrayList<>();


    public User() {}

    public User(String username, String password, String email, String address, boolean isContractor, String...authorities) { // The '...' allows for an array to be made by adding additional ','s to method call
        this.userName = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.isContractor = isContractor;
        this.authorities.addAll(Arrays.asList(authorities));
        this.authorityString = getAuthoritiesAsCSV(this.authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities
                .stream()
                .map(s -> new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return s;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
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

    public String getAuthoritiesAsCSV(List<String> auth) {
        return org.apache.tomcat.util.buf.StringUtils.join(auth);
    }

}
