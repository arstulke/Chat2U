package de.chat2u;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    /**
     * Get Query params from Request/Query String
     *
     * @param query Querystring from Request
     * */
    static HashMap<String, String> getParams(String query) {
        HashMap<String, String> parameters = new HashMap<>();
        String[] params = query.split("&");

        for (String param : params) {
            if (param.contains("=")) {
                if (param.split("=").length > 1) {
                    parameters.put(param.split("=")[0], param.split("=")[1]);
                } else {
                    parameters.put(param, "");
                }
            } else {
                parameters.put(param, "");
            }
        }
        return parameters;
    }

    /**
     * Generates a user token that isn't existing.
     * */
    public static String generateUniqueToken() {
        List<String> tokens = getUserTokens();
        String token = RandomStringUtils.random(32, true, true);
        while (tokens.contains(token))
            token = RandomStringUtils.random(32, true, true);
        return token;
    }

    /**
     * Returns a list of existing User Tokens.
     */
    public static List<String> getUserTokens() {
        return Chat.users.values().stream().map(User::getToken).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns a list of existing Usernames.
     */
    public static List<String> getUsernameList() {
        return Chat.users.values().stream().map(User::getUsername).collect(Collectors.toCollection(ArrayList::new));
    }
}