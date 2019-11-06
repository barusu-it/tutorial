package it.barusu.tutorial.flyway;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "p_user")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    @Column(name = "login_name")
    private String loginName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "real_name")
    private String realName;



}
