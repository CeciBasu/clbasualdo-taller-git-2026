package py.edu.uc.lp3.clbasualdo.minecraft.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "API Minecraft - clbasualdo - Taller Git LP3 2026";
    }
}
