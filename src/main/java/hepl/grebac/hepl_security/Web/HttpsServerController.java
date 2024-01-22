package hepl.grebac.hepl_security.Web;

import hepl.grebac.hepl_security.DB.DBHandler;
import hepl.grebac.hepl_security.TLS.requestToACQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class HttpsServerController {
    final DBHandler dbHandler;
    final requestToACQ requestToACQ;

    @Autowired
    public HttpsServerController(DBHandler dbHandler, requestToACQ requestToACQ) {
        this.dbHandler = dbHandler;
        this.requestToACQ = requestToACQ;
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
    public String payement(Model model, @RequestParam(name = "username", defaultValue = "client")String username, @RequestParam(name = "payementError", defaultValue = "false")Boolean payementError) {
        model.addAttribute("username", username);
        model.addAttribute("payementError", payementError);
        return "payement";
    }

    @PostMapping(value = "/processPayement")
    public String processPayement(Model model) {
        try {
            var payementValid = requestToACQ.requestToACS();

            if(payementValid) {
                return "payementValid";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/payement?payementError=true";
    }
}
