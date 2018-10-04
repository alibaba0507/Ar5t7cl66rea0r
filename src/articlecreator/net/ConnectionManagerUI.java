/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.net;

import articlecreator.gui.components.LinksObject;
import articlecreator.gui.components.SearchObject;
import articlecreator.gui.components.ui.ProjectsUI;
import articlecreator.gui.components.ui.PropertiesUI;
import articlecreator.gui.run.ArticleManagmentMain;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
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

    /*private final static String DUCKDUCKGO_SEARCH_URL = "https://duckduckgo.com/html/?q=";
    private final static String GOOGLE_SEARCH_URL = "http://www.google.com/search?q=";
    private final static String GOOGLE_REGEX_SEARCH = "h3.r a";
    private final static String DUCKGO_REGEX_SEARCH = "#links .results_links";
     */
    public void processArticle(LinksObject link, String dir) throws Exception {
        Document doc = crawl(link.getLink(), null);
        // remove script and hidden shit 
        doc.select("script,.hidden,form").remove();
        URL url = new URL(link.getLink());
        // base URI will be used within the loop below
        String baseUri = (new StringBuilder())
                .append(url.getProtocol())
                .append("://")
                .append(url.getHost())
                .toString();
        // select all article tags
        Elements posts = doc.select("article");
        if (posts.size() == 0) {
            posts = doc.select("#content");
        } else if (posts.size() == 0) {
            posts = doc.select(".content");
        } else if (posts.size() == 0) {
            posts = doc.select(".mainContent");
        } else if (posts.size() == 0) {
            posts = doc.select("#mainContent");
        } else if (posts.size() == 0) {
            posts = doc.select(".blog-posts.hfeed");
        } else if (posts.size() == 0) {
            posts = doc.select(".blog-posts");
        } else if (posts.size() == 0) {
            posts = doc.select(".standard-body");
        } else if (posts.size() == 0) {
            return;
        }
        // #content or .content #mainContent or .mainContent
        // .blog-posts hfeed
        String txtToSave = posts.text();

        String title = doc.title();
        if (title.length() > 20) {
            title = title.substring(0, 20);
        }
        // make it unique
        title += "_" + title.hashCode();

        int wordCnt = txtToSave.split(" ").length;
        link.setWordCount(Integer.toString(wordCnt));
        title = title.replaceAll("[\\\\/:*?\"<>|]", "_");

        final File f = new File(dir + ArticleManagmentMain.FILE_SEPARATOR + title + ".html");
        PrintWriter writer = new PrintWriter(f, "UTF-8");
        writer.write(posts.outerHtml());
        writer.flush();
        writer.close();
        // Write text only
        final File fTXT = new File(dir + ArticleManagmentMain.FILE_SEPARATOR + title + ".txt");
        PrintWriter writerTXT = new PrintWriter(fTXT, "UTF-8");
        writerTXT.write(txtToSave);
        writerTXT.flush();
        writerTXT.close();
        //FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");

    }

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
            ProjectsUI.console.append(" Reirect [" + redirectUrl + "]\r\n");
            ProjectsUI.console.append(" Coocies [" + cookies + "]\r\n");
            return crawl(redirectUrl, cookies);
        }
        String body = response.body();
        /*    ProjectsUI.console.append("BODY ================================\r\n");
        ProjectsUI.console.append(body + "\r\n");
        ProjectsUI.console.append("END BODY ================================\r\n");
         */
        return Jsoup.parse(body);

    }

    /**
     * Do the search like browser
     *
     * @param prop
     * @param keyWord
     * @param searchEngine can be NULL if null will pick first default engine
     * @throws Exception
     */
    public ArrayList searchForLinks(String keyWord, int index) throws Exception {
        // we start with google search
        //String urlSearch = "http://www.google.com/search?q=";
        String jsonResult = "";
        String searchEngine = "";
        String regex = "";
        ArrayList tbl = new ArrayList();

        ArrayList userList = (ArrayList) PropertiesUI.getInstance().getDefaultProps().get("USER_SEARCH");
        if (userList == null) {
            ProjectsUI.console.append(" >>> NO VALID SEARCH ENGINE SELECTED .... \r\n");
            return new ArrayList();
        } else if (index < userList.size()) {
            SearchObject o = (SearchObject) userList.get(index);
            searchEngine = o.getSearchEngine();
            regex = o.getLinksRegex();
        }
        // regex = GOOGLE_REGEX_SEARCH;

        if (searchEngine == null || searchEngine == "") {
            ProjectsUI.console.append(">>>>> NO SEARCH ENGINE WE WILL EXIT SEARCH ......");
            return tbl;
        }
        String search = keyWord;//"stackoverflow";
        String charset = "UTF-8";
        String urlEncode = searchEngine + URLEncoder.encode(search, charset);
        ProjectsUI.console.append(" Constracting URL [" + urlEncode + "] \r\n");
        // 3 seconds to not be rejected by search engine

        Document doc = crawl(urlEncode, "");
        if (doc != null) {

            //Elements links = doc.body().select(".g>.r>a");
            //Elements links = doc.body().select("h3.r a"); // this is google
            //Elements links = doc.select("#links .results_links"); // this is duckduckgo
            Elements links = doc.select(regex);
            // Elements links = doc.getElementById("links").getElementsByClass("results_links");
            if (links.size() < 2) { // we have a problem something went wrong 
                // we must try to change the engine
                // if (searchEngine == GOOGLE_SEARCH_URL) {
                Thread.sleep(3000);
                return searchForLinks(keyWord, index + 1);
                // }
                //else if (searchEngine == DUCKDUCKGO_SEARCH_URL)
            } else {
                //JSONObject mainObj = new JSONObject();
                // JSONArray arrJSON = new JSONArray();

                for (org.jsoup.nodes.Element link : links) {
                    final String title = link.text();
                    //  final String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
                    final String result = link.attr("href");
                    //final String url = URLDecoder.decode(link.absUrl("href").substring(link.absUrl("href").indexOf('=') + 1, link.absUrl("href").indexOf('&')), "UTF-8");
                    // final String url = URLDecoder.decode(link.absUrl("href").substring(link.absUrl("href").indexOf('=') + 1, link.absUrl("href").indexOf('&')), "UTF-8");
                    final String url = URLDecoder.decode(result, "UTF-8");
                    if (!url.startsWith("http")) {
                        continue; // Ads/news/etc.
                    }
                    //JSONObject o = new JSONObject();
                    //o.put("title", title);
                    //o.put("URL", url);
                    //arrJSON.add(o);
                    LinksObject lnk = new LinksObject();
                    lnk.setKeyWord(keyWord);
                    lnk.setLink(url);
                    lnk.setTitle(title);
                    tbl.add(lnk);
                    ProjectsUI.console.append(" Link -> Title[" + title + "] \r\n");
                    ProjectsUI.console.append(" Link -> URL[" + url + "] \r\n");
                } // end for
                // mainObj.put(keyWord, arrJSON);
                // JSONParser parser  =new JSONParser();
                //  jsonResult = arrJSON.toJSONString();
                // prop.put(keyWord, arrJSON.toJSONString());
            }
        } else {
            Thread.sleep(3000);
            return searchForLinks(keyWord, index + 1);
        }
        return tbl;
    }
}
