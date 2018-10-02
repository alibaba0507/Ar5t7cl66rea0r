/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.net;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author alibaba0507
 */
public class ConnectionManagerUI {

    private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";
    private final static String DUCKDUCKGO_SEARCH_URL = "https://duckduckgo.com/html/?q=";
    private final static String GOOGLE_SEARCH_URL = "http://www.google.com/search?q=";

    private Document crawl(String url, String cookies) throws IOException {
        if (cookies == null) {
            cookies = "";
        }
        Connection.Response response;

        response = Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .header("Accept-Language", "en-US")
                .header("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Cookie", cookies)
                .ignoreContentType(true) // This is used because Jsoup "approved" content-types parsing is enabled by default by Jsoup
                .followRedirects(false)
                .execute();

        System.out.println(response.statusCode() + " : " + url);

        if (response.hasHeader("location")) {
            String redirectUrl = response.header("location");
            Map<String, String> cookiesMap = response.cookies();
            cookies = "";
            Iterator it = cookiesMap.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                String value = (String) cookiesMap.get(key);
                if (cookies != "") {
                    cookies += ";";
                }
                cookies += key + "=" + value;
            }
            crawl(redirectUrl, cookies);
        }

        return Jsoup.parse(response.body());

    }

    /**
     * Do the search like browser
     *
     * @param prop
     * @param keyWord
     * @param searchEngine can be NULL if null will pick first default engine
     * @throws Exception
     */
    public void searchForLinks(Hashtable prop, String keyWord, String searchEngine) throws Exception {
        // we start with google search
        //String urlSearch = "http://www.google.com/search?q=";
        if (searchEngine == null || searchEngine.equals("")) {
            searchEngine = GOOGLE_SEARCH_URL;
        }
        String search = keyWord;//"stackoverflow";
        String charset = "UTF-8";
        String urlEncode = searchEngine + URLEncoder.encode(search, charset);
        Document doc = crawl(urlEncode, "");
        if (doc != null) {
            Elements links = doc.body().select(".g>.r>a");
            if (links.size() < 2) { // we have a problem something went wrong 
                // we must try to change the engine
                if (searchEngine == GOOGLE_SEARCH_URL) {
                    searchForLinks(prop, keyWord, DUCKDUCKGO_SEARCH_URL);
                }
                //else if (searchEngine == DUCKDUCKGO_SEARCH_URL)
            } else {
                JSONObject mainObj = new JSONObject();
                JSONArray arrJSON = new JSONArray();
                for (org.jsoup.nodes.Element link : links) {
                    final String title = link.text();
                    //  final String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
                    final String url = URLDecoder.decode(link.absUrl("href").substring(link.absUrl("href").indexOf('=') + 1, link.absUrl("href").indexOf('&')), "UTF-8");

                    if (!url.startsWith("http")) {
                        continue; // Ads/news/etc.
                    }
                    JSONObject o = new JSONObject();
                    o.put("title", title);
                    o.put("URL", url);
                    arrJSON.add(o);
                }
                mainObj.put(keyWord, arrJSON);
            // JSONParser parser  =new JSONParser();
            prop.put(keyWord, arrJSON.toJSONString());
            }
        }
    }
}
