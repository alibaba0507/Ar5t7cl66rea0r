/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator;

import java.util.Iterator;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author alibaba0507
 */
public class ParseHTMLArticles {

    private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";

    /**
     * This will try to parse the document and extract article and images and
     * save to HD
     *
     * @param doc
     * @param dir
     * @param index
     */
    public static void parse(org.jsoup.nodes.Document doc, String dir, int index, String baseUri) {
        // select all article tags
        Elements posts = doc.select("article");
        if (posts.size() == 0) {
            posts = doc.getElementsByClass("bg_block_info").not(".pad_10").not(".pad_20");
        }
    }

    public static Document connectAsBrowser(String url) throws Exception {
        Connection.Response response;
        response = Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .header("Accept-Language", "en-US")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .timeout(30000)
                .ignoreContentType(true) // This is used because Jsoup "approved" content-types parsing is enabled by default by Jsoup
                .execute();
        Map<String, String> cookiesMap = response.cookies();
        String cookies = "";
        Iterator it = cookiesMap.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            String value = (String) cookiesMap.get(key);
            if (cookies != "") {
                cookies += ";";
            }
            cookies += key + "=" + value;
        }
        Document doc = Jsoup.connect(url).cookie(url, url)
                .userAgent(USER_AGENT)
                .header("Accept-Language", "en-US")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Connection", "keep-alive")
                .header("Cookie", cookies)
                .get();
        return doc;
          
    }
}
