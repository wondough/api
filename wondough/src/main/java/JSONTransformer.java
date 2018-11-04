package wondough;

import spark.*;
import com.google.gson.Gson;

/**
* A ResponseTransformer for JSON data.
*/
public class JSONTransformer implements ResponseTransformer {
    private Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

}
