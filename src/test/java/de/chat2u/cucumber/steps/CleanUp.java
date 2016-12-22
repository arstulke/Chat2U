package de.chat2u.cucumber.steps;

import cucumber.api.java.After;
import de.chat2u.ChatServer;
import org.apache.logging.log4j.LogManager;

/**
 * Created CleanUp in de.chat2u.cucumber.steps
 * by ARSTULKE on 08.12.2016.
 */
public class CleanUp {
    @After("@CleanUp")
    public void cleanUp() {
        ChatServer.getOnlineUsers().forEach(user -> {
            try {
                ChatServer.logout(user.getUsername());
            } catch (Exception e) {
                LogManager.getLogger(getClass()).debug(e);
            }
        });
    }
}
