package de.chat2u.persistence.users;

import de.chat2u.ChatServer;
import de.chat2u.model.User;
import org.sql2o.Connection;
import org.sql2o.ResultSetHandler;
import org.sql2o.Sql2o;

import java.util.List;

/**
 * Created OnlineDataBase in sql
 * by ARSTULKE on 15.12.2016.
 */
public class OnlineDataBase implements DataBase, AutoCloseable {

    private final Connection connection;
    private final ResultSetHandler<User> userResultSetHandler = resultSet -> {
        String user = resultSet.getString("name");
        return new User(user);
    };

    public OnlineDataBase(Sql2o sql) {
        connection = sql.open();
    }

    @Override
    public User authenticate(String username, String password) {
        String query = "SELECT `name` FROM `user` WHERE `name` = :username AND `password`= :password";
        List<User> result = connection
                .createQuery(query)
                .addParameter("username", username)
                .addParameter("password", password)
                .executeAndFetch(userResultSetHandler);
        if (result.size() > 1) {
            throw new AssertionError("Datenbankfehler. Bitte kontaktiere einen Admin.");
        } else if (result.size() < 1) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public void addUser(User user, String password) {
        String query = "INSERT INTO `user` (`name`, `password`) VALUES (:username, :password);";
        connection.createQuery(query)
                .addParameter("username", user.getUsername())
                .addParameter("password", password)
                .executeUpdate();

        query = "DELETE FROM `chat_user` WHERE `chat_user`.`username` = :username;";
        connection.createQuery(query)
                .addParameter("username", user.getUsername())
                .executeUpdate();

        ChatServer.chats.getGroupsFrom(user.getUsername()).forEach(group -> {
            String secQuery = "INSERT INTO `chat_user` (`chat_id`, `username`) VALUES (:chatID, :username);";
            connection.createQuery(secQuery)
                    .addParameter("chatID", group.getId())
                    .addParameter("username", user.getUsername())
                    .executeUpdate();
        });

        //.addParameter("groups", Groups.toJSON(user.getGroups()))
    }

    @Override
    public void removeUser(User user) {
        String query = "DELETE FROM `user` WHERE `user`.`name` = :username;";
        connection.createQuery(query)
                .addParameter("username", user.getUsername())
                .executeUpdate();

        query = "DELETE FROM `chat_user` WHERE `chat_user`.`username` = :username;";
        connection.createQuery(query)
                .addParameter("username", user.getUsername())
                .executeUpdate();
    }

    @Override
    public User getByUsername(String username) {
        String query = "SELECT `name` FROM `user` WHERE `name`=:username";
        List<User> result = connection
                .createQuery(query)
                .addParameter("username", username)
                .executeAndFetch(userResultSetHandler);

        if (result.size() > 1) {
            throw new AssertionError("Datenbankfehler. Bitte kontaktiere einen Admin.");
        } else if (result.size() < 1) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
