/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.gui.components.ui;

import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextArea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author alibaba0507
 */
public class ProjectsUI {
    public static Object selectedProjectItem;
    public static DefaultListModel projectListModel;
    public static JList projectList;
    public static JTextArea console;
    
     public static void reloadProjectTree(String selectedProject) {
        if (ProjectsUI.projectListModel == null) {
            return;
        }

        ProjectsUI.projectListModel.clear();
        String savedProjects = (String) PropertiesUI.getInstance().getDefaultProps().get("PROJECTS");
        JSONObject savedProjJSON = new JSONObject();
        JSONParser parser = new JSONParser();
        try {
            savedProjJSON = (JSONObject) parser.parse(savedProjects);
            JSONArray projectsJSON = (JSONArray) savedProjJSON.get("prj");
            Iterator<JSONObject> objs = projectsJSON.iterator();
            int selectedIndex = 0;
            int cnt = 0;
            while (objs.hasNext()) {

                final JSONObject p = (JSONObject) objs.next();
                if (selectedProject != null && ((String) p.get("name")).equals(selectedProject/*txtProjName.getText()*/)) {
                    selectedIndex = cnt;
                }
                cnt++;
                ProjectsUI.projectListModel.addElement(new ProjectItem() {
                    @Override
                    public String toString() {
                        return (String) p.get("name");
                    }

                    @Override
                    public String getJSONObject() {
                        return p.toJSONString();
                    }
                });
            }// end while
            ProjectsUI.projectList.setSelectedIndex(cnt - 1);
            ProjectsUI.selectedProjectItem = ProjectsUI.projectList.getSelectedValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
