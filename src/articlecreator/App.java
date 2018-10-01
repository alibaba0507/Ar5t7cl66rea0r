/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package articlecreator;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;  
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 *
 * @author alibaba0507
 */
public class App {
//We need a real browser user agent or Google will block our request with a 403 - Forbidden
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
 public static void appSearch(String args) throws Exception {
        String google = "http://www.google.com/search?q=";
    String search = args;//"stackoverflow";
    String charset = "UTF-8";
    String userAgent = "ExampleBot 1.0 (+http://example.com/bot)"; // Change this to your company's name and bot homepage!
    Elements links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get().select(".g>.r>a");

for (Element link : links) {
    String title = link.text();
    String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
    url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

    if (!url.startsWith("http")) {
        continue; // Ads/news/etc.
    }

    System.out.println("Title: " + title);
    System.out.println("URL: " + url);
}
    
/*
 String q = google + URLEncoder.encode(search, charset);
        //Fetch the page
        final Document doc = Jsoup.connect(q).userAgent(USER_AGENT).get();
        
         //System.out.println(doc.);
        //Traverse the results
        for (Element result : doc.select("div a href ")){

            final String title = result.text();
            final String url = result.attr("href");

            //Now do something with the results (maybe something more useful than just printing to console)

            System.out.println(title);
        }
 */
}
}

