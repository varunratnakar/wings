package edu.isi.wings.portal.controllers;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

import com.google.gson.Gson;

import edu.isi.wings.catalog.resource.ResourceFactory;
import edu.isi.wings.catalog.resource.api.ResourceAPI;
import edu.isi.wings.catalog.resource.classes.Machine;
import edu.isi.wings.catalog.resource.classes.Software;
import edu.isi.wings.catalog.resource.classes.SoftwareVersion;
import edu.isi.wings.portal.classes.Config;
import edu.isi.wings.portal.classes.JsonHandler;
import edu.isi.wings.portal.classes.html.CSSLoader;
import edu.isi.wings.portal.classes.html.JSLoader;

public class ResourceController {
  private int guid;
  private String rns;
  private String libns;

  private ResourceAPI api;
  private boolean isSandboxed;
  private Config config;
  private Properties props;
  private Gson json;

  public ResourceController(int guid, Config config) {
    this.guid = guid;
    this.config = config;
    this.isSandboxed = config.isSandboxed();
    json = JsonHandler.createPrettyGson();
    this.props = config.getProperties();

    api = ResourceFactory.getAPI(props);

    this.rns = (String) props.get("ont.resource.url") + "#";
    this.libns = (String) props.get("lib.resource.url") + "#";
  }

  public void show(PrintWriter out) {
    // Get Hierarchy
    try {
      ArrayList<String> machineIds = api.getMachineIds();
      ArrayList<String> softwareIds = api.getSoftwareIds();
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Describe Resources</title>");
      JSLoader.setContextInformation(out, config);
      CSSLoader.loadResourceViewer(out, config.getContextRootPath());
      JSLoader.loadResourceViewer(out, config.getContextRootPath());
      out.println("</head>");

      out.println("<script>");
      out.println("var resViewer_" + guid + ";");
      out.println("Ext.onReady(function() {"
          + "resViewer_" + guid + " = new ResourceViewer('"+ guid + "', { " 
          + "machines: " + json.toJson(machineIds) + ", "
          + "softwares: " + json.toJson(softwareIds) 
          + " }, "
          + "'" + config.getScriptPath() + "', " 
          + "'" + this.rns + "', "
          + "'" + this.libns + "', " 
          + !isSandboxed 
          + ");"
          + "resViewer_" + guid + ".initialize();\n"
          + "});\n"
          );
      out.println("</script>");
      
      out.println("</html>");
    } finally {
      api.end();
    }
  }

  // Query
  public String getAllSoftwareVersions() {
    try {
      return json.toJson(this.api.getAllSoftwareVersions());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      api.end();
    }
  }
  
  public String getAllSoftwareEnvironment() {
    try {
      return json.toJson(this.api.getAllSoftwareEnvironment());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      api.end();
    }
  }
  
  public String getMachineJSON(String resid) {
    try {
      return json.toJson(this.api.getMachine(resid));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      api.end();
    }
  }

  public String getSoftwareJSON(String resid) {
    try {
      return json.toJson(this.api.getSoftware(resid));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      api.end();
    }
  }

  public String getSoftwareVersionJSON(String resid) {
    try {
      return json.toJson(this.api.getSoftwareVersion(resid));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      api.end();
    }
  }
  
  public boolean checkMachine(String resid) {
    try {
      Machine machine = this.api.getMachine(resid);
      return machine.checkConnection();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      api.end();
    }
  }


  // Add
  public boolean addMachine(String resid) {
    try {
      return this.api.addMachine(resid) && this.api.save();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      api.end();
    }
  }

  public boolean addSoftware(String resid) {
    try {
      return this.api.addSoftware(resid) && this.api.save();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      api.end();
    }
  }

  public boolean addSoftwareVersion(String resid, String softwareid) {
    try {
      return this.api.addSoftwareVersion(resid, softwareid) && this.api.save();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      api.end();
    }
  }

  // Save updates
  public boolean saveMachineJSON(String resid, String resvals_json) {
    try {
      Machine machine = json.fromJson(resvals_json, Machine.class);
      return this.api.saveMachine(machine) && this.api.save();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      api.end();
    }
  }

  public boolean saveSoftwareJSON(String resid, String resvals_json) {
    try {
      Software software = json.fromJson(resvals_json, Software.class);
      return this.api.saveSoftware(software) && this.api.save();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      api.end();
    }
  }

  public boolean saveSoftwareVersionJSON(String resid, String resvals_json) {
    try {
      SoftwareVersion version = json.fromJson(resvals_json, SoftwareVersion.class);
      return this.api.saveSoftwareVersion(version) && this.api.save();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      api.end();
    }
  }

  // Remove
  public boolean removeMachine(String resid) {
    try {
      return this.api.removeMachine(resid) && this.api.save();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      api.end();
    }
  }

  public boolean removeSoftware(String resid) {
    try {
      return this.api.removeSoftware(resid) && this.api.save();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      api.end();
    }
  }

  public boolean removeSoftwareVersion(String resid) {
    try {
      return this.api.removeSoftwareVersion(resid) && this.api.save();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      api.end();
    }
  }
}