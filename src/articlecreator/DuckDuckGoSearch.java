/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package articlecreator;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 *
 * @author alibaba0507
 */
public class DuckDuckGoSearch {
    
private final static String DUCKDUCKGO_SEARCH_URL = "https://duckduckgo.com/html/?q=";
public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";

 public static void getSearchResults(String query){
  
     Document doc = null;
 
  try {
    query = query.replace(" ", "%20");
   doc = Jsoup.connect(DUCKDUCKGO_SEARCH_URL + query).userAgent(USER_AGENT).get();
   Elements results = doc.getElementById("links").getElementsByClass("results_links");
 
   for(Element result: results){
    
    Element title = result.getElementsByClass("links_main").first().getElementsByTag("a").first();
    System.out.println("\nURL:" + title.attr("href"));
    System.out.println("Title:" + title.text());
    System.out.println("Snippet:" + result.getElementsByClass("result__snippet").first().text());
   }
  } catch (IOException e) {
   e.printStackTrace(); 
  }
}
}
