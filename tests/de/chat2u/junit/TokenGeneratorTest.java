package de.chat2u.junit;

import de.chat2u.ChatServer;
import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static de.chat2u.authentication.Permissions.ADMIN;
import static de.chat2u.authentication.Permissions.MOD;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created TokenGeneratorTest in de.chat2u.junit
 * by ARSTULKE on 18.11.2016.
 */
public class TokenGeneratorTest {
    @Before
    public void setup() {
        ChatServer.initialize(new AuthenticationService(new UserRepository<>()));
    }

    @Test
    public void generatesTokenWith32Chars() {
        //when
        String token = ChatServer.generateToken(ADMIN);

        //then
        assertThat(token.length(), is(32));
        assertTrue(token.matches("^[a-zA-Z0-9]*$"));
    }

    @Test
    public void generatesTokenWithAdminPrivileges() {

        //when
        String token = ChatServer.generateToken(ADMIN);
        ChatServer.register("user1", "pw1", token);

        //then
        assertThat(ChatServer.getRegisteredUserByName("user1").getPermissions(), is(ADMIN));
    }

    @Test
    public void generatesTokenWithModeratorPrivileges() {
        //when
        String token = ChatServer.generateToken(MOD);
        ChatServer.register("user1", "pw1", token);

        //then
        assertThat(ChatServer.getRegisteredUserByName("user1").getPermissions(), is(MOD));
    }
}
