package de.chat2u.junit;

import de.chat2u.model.User;
import de.chat2u.persistence.users.DataBase;
import de.chat2u.persistence.users.OnlineDataBase;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.sql2o.GenericDatasource;
import org.sql2o.Sql2o;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created OnlineDataBaseTest in sql
 * by ARSTULKE on 15.12.2016.
 */
public class OnlineDataBaseTest {

    private final Sql2o sql2o = new Sql2o(new GenericDatasource("mysql://localhost:3306/Si", "root", ""));

    @Test
    public void addsAndRemovesUser() {
        try (DataBase dataBase = new OnlineDataBase(sql2o)) {
            String username = RandomStringUtils.random(8, true, false);
            String password = RandomStringUtils.random(8, true, false);

            User user1 = new User(username);
            dataBase.addUser(user1, password);

            User user2 = dataBase.getByUsername(username);
            assertThat(user1, is(user2));

            dataBase.removeUser(user2);
            User user3 = dataBase.getByUsername(username);
            assertThat(user3, is(nullValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void authenticates() {
        try (DataBase dataBase = new OnlineDataBase(sql2o)) {
            String username = "TestUser2";
            String password = "TestPassword2";

            User user1 = new User(username);

            User user2 = dataBase.authenticate(username, password);
            assertThat(user2, is(user1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void contains() {
        try (DataBase dataBase = new OnlineDataBase(sql2o)) {
            String username = "TestUser3";

            boolean contains = dataBase.contains(username);
            Assert.assertTrue("User is not conatained in database", contains);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
