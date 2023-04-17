package tiw.fsa.worker;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class ProbeController {
    @GetMapping("/liveness")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public boolean isAlive() {
        return true;
    }
}
