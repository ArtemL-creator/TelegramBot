package MyBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Responser {
    public List<String> exec(String str) throws IOException {
        List<String> response = new ArrayList<>();

        Parser parser = new Parser();

        Map<String, String> map = parser.parseFromHabr(str);

        int size = map.size();

        if (size == 0) {
            return response;
        }

        int cnt;

        if (size > 10) {
            cnt = 10;
        } else {
            cnt = 1;
        }

        FilterBloom filterBloom = new FilterBloom(size, size / cnt);

        filterBloom.addToFilter(str);

        for (String name : map.keySet()) {
            if (!filterBloom.checkIsNotInFilter(map.get(name))) {
                response.add(name);
            }
        }

        return response;
    }
}