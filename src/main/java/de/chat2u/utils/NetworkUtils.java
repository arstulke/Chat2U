package de.chat2u.utils;

import java.util.HashMap;

public class NetworkUtils {
    /**
     * Get Query params from Request/Query String
     *
     * @param query Querystring from Request
     * @return a Hashmap with the querxKey as Key and the value as value
     */
    public static HashMap<String, String> getParams(String query) {
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
}