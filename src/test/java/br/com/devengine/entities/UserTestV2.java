package br.com.devengine.entities;

import br.com.devengine.ObjectManipulation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTestV2 implements ObjectManipulation<UserTestV2> {

    private Long id;
    private String username;
    private String password;
    private List<String> authorities;

}
