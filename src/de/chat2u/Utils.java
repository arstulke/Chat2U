package de.chat2u;

import java.util.HashMap;

public class Utils {
    /**
     * Write the parameters from the URL into a {@link HashMap} with
     * Key and value
     * <p>
     *
     * @param query is the querry String from the URL
     * @return a {@link HashMap} with key and Value of each queryArgument
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
}