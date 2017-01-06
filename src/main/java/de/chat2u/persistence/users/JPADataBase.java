package de.chat2u.persistence.users;

import de.chat2u.model.User;
import de.chat2u.persistence.chats.JPAChatContainer;

import java.util.List;

/**
 * Created JPADataBase in de.chat2u.persistence.users
 * by ARSTULKE on 19.12.2016.
 */
public class JPADataBase implements DataBase {
    @Override
    public User authenticate(String username, String password) {
        return ((JPAChatContainer.PersistentOperation<User>) entityManager -> {
            User user = entityManager.find(User.class, username);
            if (user == null) {
                return null;
            } else {
                List result = entityManager
                        .createNativeQuery("SELECT * FROM `user` WHERE `password` = :password AND `name` = :username ;")
                        .setParameter("password", password)
                        .setParameter("username", username).getResultList();

                if (result.size() > 1) {
                    throw new AssertionError("Datenbankfehler: authenticate - mit der Anfrage: "+username+" "+password);
                } else if (result.size() < 1) {
                    return null;
                }
                return user;
            }
        }).execute();
    }

    @Override
    public void addUser(User user, String password) {
        ((JPAChatContainer.PersistentOperation<Void>) entityManager -> {
            User u = entityManager.find(User.class, user.getUsername());
            if (u == null) {
                entityManager
                        .createNativeQuery("INSERT INTO `user` (`password`, `user`.`name`) VALUES (:password, :username) ;")
                        .setParameter("password", password)
                        .setParameter("username", user.getUsername())
                        .executeUpdate();
            }
            return null;
        }).execute();
    }

    @Override
    public User getByUsername(String username) {
        return ((JPAChatContainer.PersistentOperation<User>) entityManager -> entityManager.find(User.class, username)).execute();
    }

    @Override
    public void removeUser(User user) {
        ((JPAChatContainer.PersistentOperation<Void>) entityManager -> {
            User u = entityManager.find(User.class, user.getUsername());
            entityManager.remove(u);
            return null;
        }).execute();
    }

    @Override
    public void close() throws Exception {
    }
}
