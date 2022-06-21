package ms.kotlin.battleships.web.controllers

import ms.kotlin.battleships.app.services.UserService
import ms.kotlin.battleships.web.models.request.RegistryModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@CrossOrigin
@Validated
@RestController
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/register")
    fun register(@RequestBody @Valid registryModel: RegistryModel) {
        userService.createUser(registryModel)
    }

    @DeleteMapping("/delete")
    fun delete(@RequestParam username: String) {
        userService.deleteByUsername(username)
    }

}