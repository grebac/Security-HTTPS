package hepl.grebac.hepl_security.Web;

import hepl.grebac.hepl_security.DB.DBHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HttpsServerController {
    final DBHandler dbHandler;

    public HttpsServerController(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }


    // Welcome page : You can authenticate
    @GetMapping("/")
    public String authentication(Model model, @RequestParam(name = "isError", defaultValue = "false")boolean isError) {
        model.addAttribute("client", new client());
        model.addAttribute("isError", isError);
        return "authentication";
    }

    // Handle authentaication request : checks database then redirects :
    // - to /payement if the username's password matches
    // - to /?isError=true if the username's password doesn't match
    @PostMapping(value = "/authentication")
    public String login(@ModelAttribute client c, Model model) {
        if(dbHandler.handleConnexionRequest(c.getUsername(), c.getPassword())) {
            return "redirect:/payement?username=" + c.getUsername();
        }
        return "redirect:/?isError=true";
    }

    @GetMapping("/payement")
    public String payement(Model model, @RequestParam(name = "username", defaultValue = "client")String username) {
        model.addAttribute("username", username);
        return "payement";
    }

    @PostMapping(value = "/processPayement")
    public String processPayement(Model model) {
        return "redirect:/payement";
    }
}
