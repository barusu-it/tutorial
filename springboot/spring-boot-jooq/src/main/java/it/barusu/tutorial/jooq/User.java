package it.barusu.tutorial.jooq;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "p_user")
@ToString
@AllArgsConstructor
@NoArgsConstructor
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

    @Column
    private Long age;


}
