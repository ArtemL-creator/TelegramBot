package MyBot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;

public class Parser {
    public Map<String, String> parseFromHabr(String Url) throws IOException {
        Document doc = Jsoup.connect("https://habr.com/ru/search/?q=" +
                        Url + "&target_type=posts&order=relevance")
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("https://www.google.com")
                .get();

        String document = doc.html();
        String[] html = document.split("\\n");

        Map<String, String> parsedString = new HashMap<>();

        for (String str : html) {
            String value;
            String ref;

            if (str.contains("<h2")) {
                String[] name = str.split("<span>|</span>");
                String[] refs = str.split("<h2 class=\"tm-article-snippet__title " +
                        "tm-article-snippet__title_h2\"><a href=\"|\" data-article-link=");

                if (name.length > 1 && refs.length > 1) {
                    if (name[1].contains("<em")) {
                        String[] elem = name[1].split("<em class=\"searched-item\">|</em>");
                        value = String.join("", elem);
                    } else {
                        value = name[1];
                    }

                    ref = refs[1];
                    parsedString.put("https://habr.com"+ref, value);
                }
            }
        }

        return parsedString;
    }

    public Map<String, Double> parseSynonym(String Url) throws IOException {
        Document doc = Jsoup.connect("https://sinonim.org/s/"+ Url +"#f")
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("https://www.google.com")
                .get();

        String document = doc.html();
        String[] html = document.split("\\n|,");

        List<String> words = new LinkedList<>();
        List<Double> values = new LinkedList<>();
        Map<String, Double> map = new HashMap<>();

        boolean flag = false;

        for (String str : html) {
            if (flag) {
                String[] value = str.split("<td>|</td>");

                if (value.length > 1) {
                    if(value[1].equals("-")) {
                        values.add(0.0);
                    } else {
                        values.add(Double.parseDouble(value[1]));
                    }
                }

                flag = false;
            }

            if (str.contains("<td class=\"nach\">")) {
                flag = true;
            }

            if (str.contains("<a id=\"as") && !str.contains("<span id=\"tr")) {
                String[] results = str.split("<td>|</a>");
                byte[] result = results[1].getBytes();

                int i = 0;
                while (result[i] != '>') {
                    i++;
                }
                i++;

                words.add(results[1].substring(i));
            }
        }


        for (int i = 0; i < words.size(); i++) {
            if (i > values.size() - 1) {
                map.put(words.get(i), 0.0);
            } else {
                map.put(words.get(i), values.get(i));
            }
        }

        return map;
    }
}