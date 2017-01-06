package de.chat2u.persistence.chats;

import de.chat2u.model.Message;
import de.chat2u.model.User;
import de.chat2u.model.chats.Channel;
import de.chat2u.model.chats.Chat;
import de.chat2u.model.chats.Group;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Created JPAChatContainer in de.chat2u.persistence.chats
 * by ARSTULKE on 20.12.2016.
 */
public class JPAChatContainer implements ChatContainer {

    public interface PersistentOperation<R> {
        EntityManagerFactory jpaTest = Persistence.createEntityManagerFactory("jpaTest");

        /*
        * Usage
        *
        * return ((JPAChatContainer.PersistentOperation<ResultType>) entityManager -> {
            doSomthingWithEntityManager();
            return new ResultType();
        }).execute();
        *
        * */

        default R execute() {
            EntityManager entityManager = null;
            try {
                entityManager = jpaTest.createEntityManager();
                entityManager.getTransaction().begin();

                return doExecute(entityManager);
            } finally {
                if (entityManager != null) {
                    entityManager.getTransaction().commit();
                    entityManager.close();
                }
            }
        }

        R doExecute(EntityManager entityManager);
    }

    @Override
    public String createGroup(String name, Set<User> users) {
        Group g = new Group(name, users.stream().collect(
                HashSet::new,
                (strings, user) -> strings.add(user.getUsername()),
                (BiConsumer<Set<String>, Set<String>>) Set::addAll));

        return insertChat(g) ? g.getId() : null;
    }

    @Override
    public void createNewChannel(String name) {
        insertChat(new Channel(name, new HashSet<>()));
    }

    private boolean insertChat(Chat chat) {
        return ((PersistentOperation<Boolean>) entityManager -> {
            Object o = entityManager.find(Chat.class, chat.getId());
            if (o == null) {
                entityManager.persist(chat);
                o = entityManager.find(Chat.class, chat.getId());
            }

            return o != null;
        }).execute();
    }

    @Override
    public Set<Group> getGroupsFromUsername(String username) {
        return ((PersistentOperation<Set<Group>>) entityManager -> {
            User u = entityManager.find(User.class, username);
            if (u != null) {
                String query = "SELECT `chat`.`id` " +
                        "FROM `chat_user` " +
                        "JOIN `chat` " +
                        "ON `chat_user`.`chat_id` = `chat`.`id` " +
                        "WHERE `chat_user`.`username` = :username AND `chat`.`type` = 'gr';";

                //noinspection unchecked
                Set<String> groupIDs = new HashSet<>(entityManager.createNativeQuery(query)
                        .setParameter("username", username)
                        .getResultList());

                Set<Group> groups = new HashSet<>();
                groupIDs.forEach(groupID -> {
                    Chat chat = entityManager.find(Chat.class, groupID);
                    if (chat instanceof Group)
                        groups.add((Group) chat);
                });
                return groups;
            }
            return null;
        }).execute();
    }

    @Override
    public Chat getChatByID(String id) {
        return ((PersistentOperation<Chat>) entityManager -> entityManager.find(Chat.class, id)).execute();
    }

    @Override
    public Set<Channel> getChannels() {
        return ((PersistentOperation<Set<Channel>>) entityManager -> {
            //noinspection unchecked
            List<Chat> chats = entityManager.createQuery("SELECT chat FROM Chat chat WHERE TYPE(chat) = 'ch'").getResultList();

            Set<Channel> channels = new HashSet<>();
            chats.forEach(chat -> {
                if (chat instanceof Channel)
                    channels.add((Channel) chat);
            });
            return channels;
        }).execute();
    }

    @Override
    public Chat addUserToChat(String id, String username) {
        return ((PersistentOperation<Chat>) entityManager -> {
            Chat chat = entityManager.find(Chat.class, id);
            User user = entityManager.find(User.class, username);

            chat.getUserList().add(user);
            entityManager.merge(chat);

            return chat;
        }).execute();
    }

    @Override
    public void removeUserFromChat(String id, String username) {
        ((PersistentOperation<Void>) entityManager -> {
            Chat chat = entityManager.find(Chat.class, id);
            User user = entityManager.find(User.class, username);

            chat.getUserList().remove(user);
            entityManager.merge(chat);

            return null;
        }).execute();
    }

    @Override
    public void addMessageToHistory(Message message) {
        ((PersistentOperation<Void>) entityManager -> {
            Chat chat = entityManager.find(Chat.class, message.getChatID());
            if (chat instanceof Group) {
                ((Group) chat).addMessageToHistory(message);
                entityManager.merge(chat);
            }
            return null;
        }).execute();
    }

    @Override
    public void close() throws Exception {
    }
}
