package de.chat2u.persistence.chats;

import de.chat2u.model.Message;
import de.chat2u.model.chats.Channel;
import de.chat2u.model.chats.Chat;
import de.chat2u.model.chats.Group;
import de.chat2u.model.users.User;
import org.sql2o.Connection;
import org.sql2o.ResultSetHandler;
import org.sql2o.Sql2o;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Created OnlineChatContainer in de.chat2u.persistence.chats
 * by ARSTULKE on 16.12.2016.
 */
public class OnlineChatContainer implements ChatContainer {
    private final Connection connection;

    private final ResultSetHandler<Chat> chatResultSetHandler;
    private final ResultSetHandler<Message> messageResultSetHandler;

    public OnlineChatContainer(Sql2o sql) {
        connection = sql.open();

        messageResultSetHandler = resultSet -> new Message(resultSet.getString("sender"), resultSet.getString("text"), resultSet.getString("chatID"), resultSet.getDate("timestamp"));
        chatResultSetHandler = resultSet -> {
            String query = "SELECT `user`.`name` " +
                    "FROM `chat` " +
                    "JOIN `group_user` " +
                    "ON `chat`.`id` = `group_user`.`groupid` " +
                    "JOIN `user` " +
                    "ON `group_user`.`username` = `user`.`name` " +
                    "WHERE `chat`.`id` = :chatID";

            Set<String> userList = new HashSet<>(connection
                    .createQuery(query)
                    .addParameter("chatID", resultSet.getString("id"))
                    .executeAndFetch((ResultSetHandler<String>) resultSet1 -> resultSet1.getString("name")));

            if (resultSet.getInt("channel") == 1) {
                return new Channel(resultSet.getString("name"), userList);
            } else {
                query = "SELECT `message`.`sender`, `message`.`text`, `message`.`chatid`, `message`.`timestamp`" +
                        "FROM `chat` " +
                        "JOIN `message` " +
                        "ON `chat`.`id` = `message`.`chatID` " +
                        "WHERE `chat`.`id` = :chatID";

                List<Message> messages = new ArrayList<>(connection
                        .createQuery(query)
                        .addParameter("chatID", resultSet.getString("id"))
                        .executeAndFetch(messageResultSetHandler));

                messages.sort(Message::compareTo);

                return new Group(resultSet.getString("name"), messages, userList);
            }
        };
    }

    @Override
    public String createGroup(String name, Set<User> users) {
        Set<String> userList = users.stream()
                .collect(
                        HashSet::new,
                        (strings, user) -> strings.add(user.getUsername()),
                        (BiConsumer<Set<String>, Set<String>>) Set::addAll);

        Group groupChat = new Group(name, new ArrayList<>(), userList);
        if (insertChat(groupChat))
            return groupChat.getID();
        else
            return null;
    }

    @Override
    public void createNewChannel(String name) {
        Chat chat = new Channel(name);
        insertChat(chat);
    }

    private boolean insertChat(Chat chat) {
        String query = "SELECT `name` FROM `chat` WHERE `id` = :chatID";
        List<String> result = connection
                .createQuery(query)
                .addParameter("chatID", chat.getID())
                .executeAndFetch((ResultSetHandler<String>) resultSet -> resultSet.getString("name"));
        if (result.size() == 0) {
            query = "INSERT INTO `chat` (`id`, `name`, `channel`) VALUES (:id, :name, :channel);";
            connection.createQuery(query)
                    .addParameter("id", chat.getID())
                    .addParameter("name", chat.getName())
                    .addParameter("channel", chat instanceof Channel ? 1 : 0)
                    .executeUpdate();

            chat.getUsers().forEach(username -> addUserToChat(chat.getID(), username));
            return true;
        }
        return false;
    }

    @Override
    public Chat getChatByID(String id) {
        String query = "SELECT `id`, `name`, `channel` FROM `chat` WHERE `id`=:chatID;";
        List<Chat> result = new ArrayList<>(connection
                .createQuery(query)
                .addParameter("chatID", id)
                .executeAndFetch(chatResultSetHandler));

        if (result.size() > 1) {
            throw new AssertionError("Datenbankfehler. Bitte kontaktiere einen Admin.");
        } else if (result.size() < 1) {
            return null;
        }

        return result.get(0);
    }

    @Override
    public Set<Channel> getChannels() {
        String query = "SELECT `id`, `name`, `channel` FROM `chat` WHERE `channel`='1';";

        return new HashSet<>(connection
                .createQuery(query)
                .executeAndFetch(chatResultSetHandler)).stream()
                .collect(
                        HashSet::new,
                        (channels, chat) -> channels.add((Channel) chat),
                        (BiConsumer<Set<Channel>, Set<Channel>>) Set::addAll);
    }

    @Override
    public void addUserToChat(String id, String username) {
        String query = "SELECT `groupid`, `username` FROM `group_user` WHERE `groupid` = :chatID AND `username` = :username";
        List<String> result = connection
                .createQuery(query)
                .addParameter("chatID", id)
                .addParameter("username", username)
                .executeAndFetch((ResultSetHandler<String>) resultSet -> resultSet.getString("groupid") + " | " + resultSet.getString("username"));

        if (result.size() == 0) {
            query = "INSERT INTO `group_user` (`groupid`, `username`) VALUES (:groupid, :username);";
            connection
                    .createQuery(query)
                    .addParameter("groupid", id)
                    .addParameter("username", username)
                    .executeUpdate();
        }
    }

    @Override
    public void removeUserFromChat(String id, String username) {
        String query = "DELETE FROM `group_user` WHERE `group_user`.`groupid` = :groupid AND `group_user`.`username` = :username;";

        connection
                .createQuery(query)
                .addParameter("groupid", id)
                .addParameter("username", username)
                .executeUpdate();
    }

    @Override
    public List<Message> addMessageToHistory(Message message) {
        String query = "INSERT INTO `message` (`sender`, `text`, `chatid`, `timestamp`) VALUES (:sender, :text, :chatID, :timestamp);";
        connection
                .createQuery(query)
                .addParameter("sender", message.getSender())
                .addParameter("text", message.getText())
                .addParameter("chatID", message.getChatID())
                .addParameter("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(message.getTimestamp()))
                .executeUpdate();

        return ((Group) getChatByID(message.getChatID())).getHistory();
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
