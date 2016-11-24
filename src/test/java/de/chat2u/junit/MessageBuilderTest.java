package de.chat2u.junit;

import de.chat2u.ChatServer;
import de.chat2u.exceptions.AccessDeniedException;
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
        String builtMessage = MessageBuilder.buildMessage(new Message(sender, msg, ChatServer.GLOBAL), "msg").toString();

        //then
        assertThat(builtMessage, is("{\"userMessage\":\"<article><b>User1<\\/b> Hallo, wie geht es dir ?<p><small class=\\\"text-muted\\\">" + MessageBuilder.getTimestamp(date) + "<\\/small><\\/p><\\/article>\",\"userlist\":[],\"chatID\":\"" + ChatServer.GLOBAL + "\",\"sender\":\"User1\",\"noHTMLmsg\":\"Hallo, wie geht es dir ?\",\"type\":\"msg\",\"timestamp\":\"" + MessageBuilder.getTimestamp(date) + "\"}"));
    }

    @Test
    public void buildsExceptionMessage() {
        //given
        Exception e = new AccessDeniedException("Ungültige Zugangsdaten");

        //when
        String builtMessage = MessageBuilder.buildExceptionMessage(e);

        //then
        assertThat(builtMessage, is("{\"exceptionType\":\"AccessDeniedException\",\"msg\":\"<p style=\\\"color:#F70505\\\">Ungültige Zugangsdaten<\\/p>\",\"type\":\"error\",\"exceptionMessage\":\"Ungültige Zugangsdaten\",\"timestamp\":\"" + MessageBuilder.getTimestamp(new Date()) + "\"}"));
    }
}
