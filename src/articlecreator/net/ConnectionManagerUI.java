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
import articlecreator.spin.syntax.Engine;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
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
    public Document openFile(String url) {
        Document doc = null;
        if (!new File(url).exists())
            return doc;
        try {
            doc = Jsoup.parse(new File(url), "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return doc;
        }
    }

    public Document cleanFile(String url) {
        Document doc = null;
        try {
            Engine spinEngine = new Engine();
            doc = Jsoup.parse(new File(url), "UTF-8");
            for (Element element : doc.select("*")) {
                if (!element.hasText() && element.isBlock()) {
                    element.remove();
                }
            }
            // Overide the document
            final File f = new File(url);
            PrintWriter writer = new PrintWriter(f, "UTF-8");
            writer.write(doc.outerHtml());
            writer.flush();
            writer.close();
            /*Elements els = doc.body().getAllElements();
            for (Element e : els) {
                List<TextNode> tnList = e.textNodes();

                for (TextNode tn : tnList) {
                    String orig = tn.text();
                    if (orig == null || orig.equals("")) {
                        tnList.remove(tn);
                    }
                    //String newPhrase = spinEngine.update(orig, false);
                    // ProjectsUI.console.append("\r\n" + orig.trim() + " --- " + newPhrase.trim() + " ---- ");
                    //  tn.text(orig.replaceAll("changeme", "<changed>changeme</changed>"));
                }
            }// end for
             */

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return doc;
        }
    }

    public Document spinFile(String url) {
        Document doc = null;
        try {
            Engine spinEngine = new Engine();
            doc = Jsoup.parse(new File(url), "UTF-8");
            Elements els = doc.body().getAllElements();
            for (Element e : els) {
                List<TextNode> tnList = e.textNodes();
                for (TextNode tn : tnList) {
                    String orig = tn.text();
                    String newPhrase = spinEngine.update(orig, false);
                    if (!orig.equalsIgnoreCase(newPhrase)) {
                        TextNode newTn = new TextNode(newPhrase);
                      //  newTn.
                        tn.replaceWith(newTn); // tn.text(orig.replaceAll(orig, newPhrase));
                        //ProjectsUI.console.append("\r\n" + orig.trim() + " --- " + tn.text() + " ---- \r\n\r\n");
                    }
                    // ProjectsUI.console.append("\r\n" + orig.trim() + " --- " + newPhrase.trim() + " ---- ");
                    //  tn.text(orig.replaceAll("changeme", "<changed>changeme</changed>"));
                }
            }// end for

            File f = new File(url);
            
            String fileDirectory = f.getParent();
           if (!fileDirectory.endsWith(ArticleManagmentMain.FILE_SEPARATOR + "spin"))
                fileDirectory = f.getParent() + ArticleManagmentMain.FILE_SEPARATOR + "spin";
            File copyDir = new File(fileDirectory);
            if (!copyDir.exists()
                    || !copyDir.isDirectory()) {
                copyDir.mkdir();
            }

            f = new File(fileDirectory + ArticleManagmentMain.FILE_SEPARATOR + f.getName());
            if (!f.exists()) {
                f.createNewFile();
            }

            PrintWriter writer = new PrintWriter(f, "UTF-8");
            writer.write(doc.toString());
            writer.flush();
            writer.close();

            //html = doc.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return doc;
        }
    }

    public void processArticle(LinksObject link, String dir) throws Exception {
        Document doc = crawl(link.getLink(), null,true);
        // remove script and hidden shit 
        doc.select("script,.hidden,form,a,footer,button").remove();
        URL url = new URL(link.getLink());
        // base URI will be used within the loop below
        String baseUri = (new StringBuilder())
                .append(url.getProtocol())
                .append("://")
                .append(url.getHost())
                .toString();
        // select all article tags
        Elements posts = doc.select("article");
        ArrayList listParser = (ArrayList) PropertiesUI.getInstance().getDefaultProps().get("USER_PARSER");
        if (posts.size() == 0) {
            if (listParser != null) {
                Iterator it = listParser.iterator();
                while (it.hasNext()) {
                    posts = doc.select((String) it.next());
                    if (posts.size() > 0) {
                        break;
                    }
                }// end while
            }// end if
            if (posts.size() == 0) {
                listParser = (ArrayList) PropertiesUI.getInstance().getDefaultProps().get("SYSTEM_PARSER");
                Iterator it = listParser.iterator();
                while (it.hasNext()) {
                    posts = doc.select((String) it.next());
                    if (posts.size() > 0) {
                        break;
                    }
                }// end while

            }// end if
        }

        if (posts.size() == 0) {
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
        link.setLocalHTMLFile(f.toString());
        // Write text only
        final File fTXT = new File(dir + ArticleManagmentMain.FILE_SEPARATOR + title + ".txt");
        PrintWriter writerTXT = new PrintWriter(fTXT, "UTF-8");
        writerTXT.write(txtToSave);
        writerTXT.flush();
        writerTXT.close();
        link.setLocalTXTFile(fTXT.toString());
        //FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");

    }

    public Document crawl(String url, String cookies, boolean isFollowRedirect) throws IOException {
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

        if (response.hasHeader("location") && isFollowRedirect) {
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
            return crawl(redirectUrl, cookies, isFollowRedirect);
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
        String redirect = (String) PropertiesUI.getInstance().getDefaultProps().get("SEARCH_REDIRECT");
        boolean followRedirect = false;
        if (redirect != null && !redirect.equals("") && Integer.parseInt(redirect) > 0) {
            followRedirect = true;
        }
       Document doc = crawl(urlEncode, "", true);
      // final Document doc = Jsoup.connect("https://google.com/search?q="+URLEncoder.encode(search, charset)).userAgent(USER_AGENT).get();
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
                ProjectsUI.console.append(">>>> CALLING NEXT ENGINE [" + (index + 1) + "]\r\n");
                return searchForLinks(keyWord, ++index);
                // }
                //else if (searchEngine == DUCKDUCKGO_SEARCH_URL)
            } else {
                //JSONObject mainObj = new JSONObject();
                // JSONArray arrJSON = new JSONArray();
                ArrayList duplicateList = new ArrayList();
                for (org.jsoup.nodes.Element link : links) {
                    final String title = link.text();
                    //  final String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
                    final String result = link.attr("href");
                    //final String url = URLDecoder.decode(link.absUrl("href").substring(link.absUrl("href").indexOf('=') + 1, link.absUrl("href").indexOf('&')), "UTF-8");
                    // final String url = URLDecoder.decode(link.absUrl("href").substring(link.absUrl("href").indexOf('=') + 1, link.absUrl("href").indexOf('&')), "UTF-8");
                    final String url = URLDecoder.decode(result, "UTF-8");
                    if (!url.startsWith("http")) {
                        continue; // Ads/news/etc.
                    } else if (title == null || title.equals("") || title.equalsIgnoreCase("ad")) {
                        continue;
                    } else if (duplicateList.contains(url)) {
                        continue;
                    }
                    duplicateList.add(url);
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
