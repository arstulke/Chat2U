package de.chat2u.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Wenn;
import de.chat2u.ChatServer;
import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.UserRepository;
import de.chat2u.model.AuthenticationUser;
import de.chat2u.model.Message;
import de.chat2u.model.User;
import de.chat2u.utils.MessageBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

/**
 * Created SendMessageSteps in PACKAGE_NAME
 * by ARSTULKE on 15.11.2016.
 */
public class SendMessageSteps {
    private String user1;
    private String user2;
    private String message;

    @Gegebensei("^ein Chat mit den Teilnehmern \"([^\"]*)\" und \"([^\"]*)\"$")
    public void einChatMitDenTeilnehmernUnd(String user1, String user2) throws Throwable {
        this.user1 = user1;
        this.user2 = user2;

        UserRepository<AuthenticationUser> userRepository = new UserRepository<>();
        String password = "Test123";
        userRepository.addUser(new AuthenticationUser(user1, password));
        userRepository.addUser(new AuthenticationUser(user2, password));
        ChatServer.initialize(new AuthenticationService(userRepository));
        ChatServer.login(user1, password, null);
        ChatServer.login(user2, password, null);
    }

    @Wenn("^\"([^\"]*)\" die Nachricht \"([^\"]*)\" sendet$")
    public void dieNachrichtSendet(String username, String message) throws Throwable {
        this.message = MessageBuilder.buildTextMessage(new Message(username, message, ChatServer.GLOBAL)).toString();
        ChatServer.sendTextMessageToChat(username, message, ChatServer.GLOBAL);
    }

    @Dann("^soll diese im Chat angezeigt werden.$")
    public void sollDieseImChatAngezeigtWerden() throws Throwable {
        UserRepository<User> users = ChatServer.getOnlineUsers();
        assertTrue(getStringHistory(users.getByUsername(user1)).contains(message));
        assertTrue(getStringHistory(users.getByUsername(user2)).contains(message));
    }

    private List<String> getStringHistory(User user) {
        return user.getHistory().stream().map((msg) -> MessageBuilder.buildTextMessage(msg).toString()).collect(Collectors.toList());
    }
}
