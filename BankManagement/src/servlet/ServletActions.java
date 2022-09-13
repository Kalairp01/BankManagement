//$Id$
package servlet;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ServletActions {
	public static JsonObject getjson(HttpServletRequest req) {
		StringBuffer jb = new StringBuffer();
		  String line = null;
		  try {
		    BufferedReader reader = req.getReader();
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		  } catch (Exception e) { /*report an error*/ }
		 JsonElement jelement = new JsonParser().parse(jb.toString());
		    JsonObject  jobject = jelement.getAsJsonObject();
		    return jobject;
	}
}
