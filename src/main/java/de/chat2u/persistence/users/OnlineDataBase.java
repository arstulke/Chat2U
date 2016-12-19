package de.chat2u.persistence.users;

import de.chat2u.model.users.User;
import org.sql2o.Connection;
import org.sql2o.ResultSetHandler;
import org.sql2o.Sql2o;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created OnlineDataBase in sql
 * by ARSTULKE on 15.12.2016.
 */
public class OnlineDataBase implements DataBase, AutoCloseable {

    private final Connection connection;
    private final ResultSetHandler<User> userResultSetHandler;

    public OnlineDataBase(Sql2o sql) {
        connection = sql.open();
        userResultSetHandler = resultSet -> {
            String user = resultSet.getString("name");

            String query1 = "SELECT `chat`.`name`, `chat`.`id` " +
                    "FROM `user` " +
                    "JOIN `group_user` " +
                    "ON `user`.`name` = `group_user`.`username` " +
                    "JOIN `chat` " +
                    "ON `group_user`.`groupid` = `chat`.`id` " +
                    "WHERE `user`.`name` = :username AND `chat`.`channel` = '0';";

            Set<String> groups = new HashSet<>(connection
                    .createQuery(query1)
                    .addParameter("username", user)
                    .executeAndFetch((ResultSetHandler<String>) resultSet1 -> resultSet1.getString("id")));
            return new User(user, groups);
        };
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

        query = "DELETE FROM `group_user` WHERE `group_user`.`name` = :username;";
        connection.createQuery(query)
                .addParameter("username", user.getUsername())
                .executeUpdate();

        user.getGroups().forEach(groupID -> {
            String secQuery = "INSERT INTO `group_user` (`groupid`, `name`) VALUES (:groupID, :username);";
            connection.createQuery(secQuery)
                    .addParameter("groupID", groupID)
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

        query = "DELETE FROM `group_user` WHERE `group_user`.`name` = :username;";
        connection.createQuery(query)
                .addParameter("username", user.getUsername())
                .executeUpdate();
    }

    @Override
    public boolean contains(String username) {
        return getByUsername(username) != null;
    }

    @Override
    public User getByUsername(String username) {
        String query = "SELECT `name` FROM user WHERE name=:username";
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
