package ms.kotlin.battleships.web.controllers

import ms.kotlin.battleships.app.services.UserService
import ms.kotlin.battleships.web.models.request.RegistryModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@CrossOrigin
@Validated
@RestController
class RegistryController {

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/register")
    fun register(@RequestBody registryModel: RegistryModel) {
        userService.createUser(registryModel)
    }

}