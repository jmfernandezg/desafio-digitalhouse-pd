import com.jmfg.certs.dh.prodev.service.CustomerService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/customer")
class CustomerController(
    private val customerService: CustomerService
) {
    @PostMapping("/login")
    fun login(@RequestParam username: String, @RequestParam password: String) =
        customerService.login(username, password)
}