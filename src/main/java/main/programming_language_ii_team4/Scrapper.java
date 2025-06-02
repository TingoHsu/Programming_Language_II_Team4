package main.programming_language_ii_team4;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scrapper {

    public List<String> getImage(String input) {
        input = input.replaceAll(" ", "%20");
        String url = "https://www.bing.com/images/search?q=" + input + "&first=1";

        try {
            Document doc = Jsoup.connect(url).get();
            Elements images = doc.select("img");

            List<String> imageUrls = new ArrayList<>();

            for (int i = 10; i < images.size(); i++) {
                if (images.get(i).attr("abs:src").isEmpty()) break;
                imageUrls.add(images.get(i).attr("abs:src"));
                i++;
            }

            imageUrls.removeIf(String::isBlank);
            Collections.shuffle(imageUrls);

            return imageUrls.subList(0, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}