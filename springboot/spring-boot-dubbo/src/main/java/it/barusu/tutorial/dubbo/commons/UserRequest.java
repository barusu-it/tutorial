package it.barusu.tutorial.dubbo.commons;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class UserRequest implements Serializable {

    private String name;
    private String statement;
}
