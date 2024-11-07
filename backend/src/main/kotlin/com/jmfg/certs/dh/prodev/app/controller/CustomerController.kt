import com.jmfg.certs.dh.prodev.service.CustomerService
import com.jmfg.certs.dh.prodev.model.dto.LoginRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/v1/customer")
@CrossOrigin(origins = ["http://localhost:3000"])
class CustomerController(
    private val customerService: CustomerService
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest) =
        customerService.login(request)
}