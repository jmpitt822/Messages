import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

import static spark.Spark.post;

/**
 * Created by jeremypitt on 9/20/16.
 */
public class Main {
    static User user;
    static Message message;
    static ArrayList<Message> messageList = new ArrayList<>();
    static HashMap<String, String> users = new HashMap<>();

    public static void main(String[] args) {
        users.put("Jeremy Pitt", "password");
        Spark.init();

        Spark.get("/",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    if (user == null) {
                        return new ModelAndView(m, "index.html");
                    } else {

                        m.put("name", user.name);
                        m.put("messageList", messageList);
                        return new ModelAndView(m, "messages.html");
                    }
                }),
                new MustacheTemplateEngine()
        );

        post(
                "create-user",
                ((request, response) -> {
                    String name = request.queryParams("name");
                    String passwordInput = request.queryParams("password");
                    if (!users.containsKey(name)){
                        if (!passwordInput.equals(users.get(name))){
                            user = new User(name, passwordInput);
                            response.redirect("/");
                            return "";
                        }
                        else{
                            response.redirect("/");
                            return "";
                        }

                    }
                    else {
                        user = new User(name, passwordInput);
                        users.put(user.name, user.password);
                        response.redirect("/");
                        return "";
                    }
                })
        );

        post(
                "create-message",
                ((request, response) -> {
                    String newMessage = request.queryParams("message");
                    message = new Message(newMessage);
                    messageList.add(message);
                    response.redirect("/");

                    return "";
                })
        );
    }
}
