package br.com.devengine.entities;

import br.com.devengine.ObjectManipulation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTest implements ObjectManipulation<UserTest> {

    private Long id;
    private String username;
    private String password;

}
