package de.chat2u.persistence.users;

import de.chat2u.model.User;
import de.chat2u.persistence.chats.JPAChatContainer;

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
                entityManager.persist(new User(username));
                entityManager
                        .createNativeQuery("UPDATE `user` SET `password` = :password WHERE `user`.`name` = :username ;")
                        .setParameter("password", password)
                        .setParameter("username", username)
                        .executeUpdate();

                user = entityManager.find(User.class, username);
            }
            return user;
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
