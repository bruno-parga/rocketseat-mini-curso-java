package br.com.brunoparga.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Modificadores:
 * public
 * private
 * protected
 */

@RestController
@RequestMapping("/users")
public class UserController {

    //Autowired - aqui eu digo pro SPRING gerenciar todo o ciclo de vida
    @Autowired
    private IUserRepository userRepository;

    /**
     * Tipos de retorno:
     * String - texto
     * Integer - int - numeros inteiros
     * Double - número 0.0000
     * Float - igual o double só suporta mais numeros
     * char ( A C)
     * Date - data
     * void - não tem retorno
     */

    /**
     * Body
     */
    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel){
        var user = this.userRepository.findByUsername(userModel.getUsername());
        if(user != null){
            //Mensagem de erro e/ou status code
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usúario já existe");
        }

        //Faz a criptografia da senha para não ser visivil no BD
        var passwordHashred = BCrypt.withDefaults()
                .hashToString(12, userModel.getPassword().toCharArray());
        //Manda para o banco a senha criptografada
        userModel.setPassword(passwordHashred);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
