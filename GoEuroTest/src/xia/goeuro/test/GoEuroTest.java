package xia.goeuro.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.io.FileUtils;

/*
 * Implement an API query and transform this data into a csv file 
 * Create a Java command line tool that takes as an input parameter a string
 */
public class GoEuroTest {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub	
		String cvsFilename = "d:\\j2c.cvs";
		String url = "http://api.goeuro.com/api/v2/position/suggest/en/";
		
		if (args.length == 1 && args[0] != null){
			
            String queryStr = url + args[0];
                                
            String respons =  queryEndpoint(queryStr);
          
            String jsonstr = null;
            if(!respons.equals("[]")) 
            {
        	  jsonstr = "{\"objects\":" +respons +"}";
        	  
        	  String nJsonStr = parseJson(jsonstr);
        	   
        	  json2cvs(nJsonStr, cvsFilename);
        	}
            else 
        	  System.out.printf("... sorry! no such a city found.");            
        }
		else 
			System.out.printf("... please input a valid city name.");
	}

	private static String queryEndpoint(String urlParameters)
	{
		String strResult = "";
		URL url;
		try {
			    url = new URL(urlParameters);
			
				BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
				
				String strTemp = "";
				while(null != (strTemp = br.readLine()))
				{
					strResult += strTemp ;					
				}
				
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
        //System.out.println(strResult);
		return strResult;
	}
	
	private static String parseJson(String josnStr)
	{
		  String newJsonStr = "{\"objects\":[";  String reStr = "";
		 try {
	            JSONObject rootObject = new JSONObject(josnStr); 
	            JSONArray objs = rootObject.getJSONArray("objects"); 

	                       
	            for(int i=0; i < objs.length(); i++) 
	            { 
	                JSONObject element = objs.getJSONObject(i); // Get row object
	        	                
	                Integer id = (Integer) element.get("_id"); 
	                String name = (String) element.get("name");                
	                String type = (String) element.get("type"); 
	                JSONObject geoPosition = element.getJSONObject("geo_position"); 
	                Double latitude =(Double) geoPosition.get("latitude");
	                Double longitude = (Double)geoPosition.get("longitude");
	                
	                //create a new json string instead of a class
	                newJsonStr+= "{\"id\":"+ id +",\"name\":"+ name +",\"type\":"+type +",\"latitude\":"+latitude +",\"longitude\":"+longitude+"},";
	              
	            }
	            
	           reStr =  newJsonStr.substring(0, newJsonStr.length()-1) +"]}";	            
	           
	        } catch (JSONException e) {
	            // JSON Parsing error
	            e.printStackTrace();
	        }		
		 
		 return reStr;
		
	}

	private static void json2cvs(String jsonString, String filename)
	{		 
		 JSONObject output;
	      try {
	        	
	        	output = new JSONObject(jsonString);

	            JSONArray docs = output.getJSONArray("objects");

	            File file = new File(filename);
	            System.out.println("... generated a file at " +filename+"\n");
	            
	            String csv = CDL.toString(docs);
	            
	            FileUtils.writeStringToFile(file, csv);
	            
	            System.out.println(csv);
	            
	            
	        } catch (JSONException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }        
	    }

}

