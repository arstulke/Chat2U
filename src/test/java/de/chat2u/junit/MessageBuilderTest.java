package de.chat2u.junit;

import de.chat2u.ChatServer;
import de.chat2u.model.Message;
import de.chat2u.utils.MessageBuilder;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created MessageBuilderTest in de.chat2u.junit
 * by ARSTULKE on 18.11.2016.
 */
public class MessageBuilderTest {

    @Test
    public void buildsTextmessage() {
        //given
        String msg = "Hallo, wie geht es dir ?";
        String sender = "User1";
        Date date = new Date();

        //when
        String builtMessage = MessageBuilder.buildTextMessage(new Message(sender, msg, ChatServer.LobbyID)).toString();

        //then
        String expectedMessage = "{\"secondData\":[],\"type\":\"textMessage\",\"primeData\":{\"chatID\":\"" + ChatServer.LobbyID + "\",\"message\":\"<article><b>User1<\\/b> Hallo, wie geht es dir ?<p><small class=\\\"text-muted\\\">" + MessageBuilder.getTimestamp(date) + "<\\/small><\\/p><\\/article>\"}}";
        assertThat(builtMessage, is(expectedMessage));
    }
}
