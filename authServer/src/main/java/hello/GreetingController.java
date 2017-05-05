package hello;

import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    @RequestMapping(value ="/isAuthenticated",method = RequestMethod.POST)
    public String isAuthenticated(@RequestBody String sessId) {
    	DBConnector dbc = new DBConnector();
    	return dbc.auth(sessId);
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody String body) {
    	DBConnector dbc = new DBConnector();
    	return dbc.login(body);
    }
    @RequestMapping(value="/signup", method = RequestMethod.POST)
    public String signup(@RequestBody String body) {
    	DBConnector dbc = new DBConnector();
    	return dbc.signup(body);
    }
}