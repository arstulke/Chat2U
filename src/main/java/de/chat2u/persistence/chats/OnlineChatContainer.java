package de.chat2u.persistence.chats;

import de.chat2u.ChatServer;
import de.chat2u.model.Message;
import de.chat2u.model.User;
import de.chat2u.model.chats.Channel;
import de.chat2u.model.chats.Chat;
import de.chat2u.model.chats.Group;
import org.sql2o.Connection;
import org.sql2o.ResultSetHandler;
import org.sql2o.Sql2o;

import java.sql.Date;
import java.sql.Timestamp;
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

        messageResultSetHandler = resultSet -> {
            Date datetime = new Date(resultSet.getObject("timestamp", Timestamp.class).getTime());
            return new Message(resultSet.getString("sender"), resultSet.getString("text"), resultSet.getString("chat_id"), datetime);
        };
        chatResultSetHandler = resultSet -> {
            String query = "SELECT `user`.`name` " +
                    "FROM `chat` " +
                    "JOIN `chat_user` " +
                    "ON `chat`.`id` = `chat_user`.`chat_id` " +
                    "JOIN `user` " +
                    "ON `chat_user`.`username` = `user`.`name` " +
                    "WHERE `chat`.`id` = :chat_id";

            Set<String> userList = new HashSet<>(connection
                    .createQuery(query)
                    .addParameter("chat_id", resultSet.getString("id"))
                    .executeAndFetch((ResultSetHandler<String>) resultSet1 -> resultSet1.getString("name")));

            if (resultSet.getString("type").equals("ch")) {
                return new Channel(resultSet.getString("name"), userList);
            } else {
                query = "SELECT *" +
                        "FROM `chat` " +
                        "JOIN `message` " +
                        "ON `chat`.`id` = `message`.`chat_id` " +
                        "WHERE `chat`.`id` = :chat_id";

                List<Message> messages = new ArrayList<>(connection
                        .createQuery(query)
                        .addParameter("chat_id", resultSet.getString("id"))
                        .executeAndFetch(messageResultSetHandler));


                Group group = new Group(resultSet.getString("name"), userList);
                group.setHistory(messages);
                return group;
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

        Group groupChat = new Group(name, userList);
        if (insertChat(groupChat))
            return groupChat.getId();
        else
            return null;
    }

    @Override
    public void createNewChannel(String name) {
        Chat chat = new Channel(name);
        insertChat(chat);
    }

    private boolean insertChat(Chat chat) {
        String query = "SELECT `name` FROM `chat` WHERE `id` = :chat_id";
        List<String> result = connection
                .createQuery(query)
                .addParameter("chat_id", chat.getId())
                .executeAndFetch((ResultSetHandler<String>) resultSet -> resultSet.getString("name"));
        if (result.size() == 0) {
            query = "INSERT INTO `chat` (`id`, `name`, `type`) VALUES (:id, :name, :type);";
            connection.createQuery(query)
                    .addParameter("id", chat.getId())
                    .addParameter("name", chat.getName())
                    .addParameter("type", chat instanceof Channel ? "ch" : "gr")
                    .executeUpdate();

            chat.getUsers().forEach(username -> addUserToChat(chat.getId(), username));
            return true;
        }
        return false;
    }

    @Override
    public Chat getChatByID(String id) {
        String query = "SELECT `id`, `name`, `type` FROM `chat` WHERE `id`=:chat_id;";
        List<Chat> result = new ArrayList<>(connection
                .createQuery(query)
                .addParameter("chat_id", id)
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
        String query = "SELECT `id`, `name`, `type` FROM `chat` WHERE `type`='ch';";

        return new HashSet<>(connection
                .createQuery(query)
                .executeAndFetch(chatResultSetHandler)).stream()
                .collect(
                        HashSet::new,
                        (channelSet, chat) -> channelSet.add((Channel) chat),
                        (BiConsumer<Set<Channel>, Set<Channel>>) Set::addAll);
    }

    @Override
    public Chat addUserToChat(String id, String username) {
        String query = "SELECT `chat_id`, `username` FROM `chat_user` WHERE `chat_id` = :chat_id AND `username` = :username";
        List<String> result = connection
                .createQuery(query)
                .addParameter("chat_id", id)
                .addParameter("username", username)
                .executeAndFetch((ResultSetHandler<String>) resultSet -> resultSet.getString("chat_id") + " | " + resultSet.getString("username"));

        if (result.size() == 0) {
            query = "INSERT INTO `chat_user` (`chat_id`, `username`) VALUES (:groupid, :username);";
            connection
                    .createQuery(query)
                    .addParameter("groupid", id)
                    .addParameter("username", username)
                    .executeUpdate();
        }

        return getChatByID(id);
    }

    @Override
    public void removeUserFromChat(String id, String username) {
        String query = "DELETE FROM `chat_user` WHERE `chat_user`.`chat_id` = :groupid AND `chat_user`.`username` = :username;";

        connection
                .createQuery(query)
                .addParameter("groupid", id)
                .addParameter("username", username)
                .executeUpdate();
    }

    @Override
    public void addMessageToHistory(Message message) {
        String query = "INSERT INTO `message` (`sender`, `text`, `chat_id`, `timestamp`) VALUES (:sender, :text, :chat_id, :timestamp);";
        connection
                .createQuery(query)
                .addParameter("sender", message.getSender())
                .addParameter("text", message.getText())
                .addParameter("chat_id", message.getChatID())
                .addParameter("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(message.getTimestamp()))
                .executeUpdate();
    }


    @Override
    public Set<Group> getGroupsFrom(String username) {
        String query = "SELECT `chat`.`name`, `chat`.`id` " +
                "FROM `user` " +
                "JOIN `chat_user` " +
                "ON `user`.`name` = `chat_user`.`username` " +
                "JOIN `chat` " +
                "ON `chat_user`.`chat_id` = `chat`.`id` " +
                "WHERE `user`.`name` = :username AND `chat`.`type` = 'gr';";

        Set<String> groupIDs = new HashSet<>(connection
                .createQuery(query)
                .addParameter("username", username)
                .executeAndFetch((ResultSetHandler<String>) resultSet1 -> resultSet1.getString("id")));

        Set<Group> groups = new HashSet<>();
        groupIDs.forEach(groupID -> {
            Chat chat = ChatServer.chats.getChatByID(groupID);
            if (chat instanceof Group)
                groups.add((Group) chat);
        });

        return groups;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
