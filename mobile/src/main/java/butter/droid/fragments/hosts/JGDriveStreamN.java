package butter.droid.fragments.hosts;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.*;
import java.net.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
//import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butter.droid.R;
import butter.droid.activities.MainActivity;
import butter.droid.fragments.MovieDetailFragment;

public class JGDriveStreamN{
   private String gurl=null;
   private String jgid=null;
   private String HTML=null;
   private String jgcode=null;
   private String jgdlink=null;
   private String jgcache=null;

   //Set and get method
   public String getUrl(){return this.gurl;}
   public void setUrl(String gurl){this.gurl=gurl;}
   public String getJGId(){return this.jgid;}
   public void setJGId(String jgid){this.jgid=jgid;}
   public String getHTML(){ return this.HTML;}
   public void setHTML(String HTML){this.HTML=HTML;}
   public String getJGCode(){
        return this.jgcode;
   }
   public void setJGCode(String jgcode){
        this.jgcode=jgcode;
    }
   public String getJGDLink(){
        return this.jgdlink;
   }
   public void setJGDLink(String jgdlink){this.jgdlink=jgdlink;}
   //Common constructor
   public JGDriveStreamN(){
   }
   //This constructor builds the class
   public JGDriveStreamN(String gurl) throws IOException {
           this.jgid = jDriveId(gurl);
           this.jgcache = jDriveTurn(gurl);
   }
   //Fetch google stream link
   public String toString(){
      return this.jgdlink;
   }
   //Fetch google id, code and stream link
   public String[] toStringArray(){
      String[] tostring = new String[3];
      tostring[0]=this.jgid;
      tostring[1]=this.jgcode;
      tostring[2]=this.jgdlink;
      return tostring;
   }
   //Retrive the md5 of @input
   public static String getMd5(String input) 
   { 
      try {
      	// Static getInstance method is called with hashing MD5 
         MessageDigest md = MessageDigest.getInstance("MD5"); 
      		// digest() method is called to calculate message digest 
      		// of an input digest() return array of byte 
         byte[] messageDigest = md.digest(input.getBytes()); 
      		// Convert byte array into signum representation 
         BigInteger no = new BigInteger(1, messageDigest);
      		// Convert message digest into hex value 
         String hashtext = no.toString(16); 
         while (hashtext.length() < 32) { 
            hashtext = "0" + hashtext; 
         }
         return hashtext; 
      }
      	// For specifying wrong message digest algorithms 
      catch (NoSuchAlgorithmException e) { 
         throw new RuntimeException(e); 
      } 
   }
   //Split @strToExpl into array depends on @separator
   public static String[] explode(String separator, String strToExpl){     
      return  strToExpl.split(separator);
   }
   //Retrive the current/future Date, with @format.
   public static String getDateStr(String format, long delay){
      String timeStamp;
      timeStamp = new SimpleDateFormat(format).format( new Date( (delay)+( Calendar.getInstance().getTime() ).getTime() ) );
      return timeStamp;
   }
   //Fires the Date. If @delay is equals to 0 retrive the current date. @delay - miliseconds
   public static Date getDate(String format, long delay){
      Date timeStamp =  new Date( (delay)+( Calendar.getInstance().getTime() ).getTime() );
      return timeStamp;
   }
   public static Map<String, String> splitQuery(String uri) throws UnsupportedEncodingException, MalformedURLException {
      URL url = new URL(uri);
      Map<String, String> query_pairs = new LinkedHashMap<String, String>();
      String query = url.getQuery();
      String[] pairs = query.split("&");
      for (String pair : pairs) {
         int idx = pair.indexOf("=");
         query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
      }
      return query_pairs;
   }
    //Build class
    public void buildN() throws IOException{
        if(this.jgid==null) {
            String id = jDriveId(this.gurl);
        }
        if(this.jgdlink==null) {
            String linkdown = getlink(this.jgid);
        }
    }
   //Fill all fields of this class 
   public String jDriveTurn(String link) throws IOException{
         String linkdown=null;
         String id = jDriveId(link);
         linkdown = getlink(id);
      return linkdown;
   }
   //Fires the google streaming link
   public String getlink(String id) throws MalformedURLException, IOException{
      if(this.jgdlink==null){
         String gurl = "https://drive.google.com/uc?export=download&id="+id;
          new DriveTask(gurl,this).execute();
          int i = 0;
          while( !JGDriveStreamN.isJGSourceUpOrDown(this.jgdlink) ) {
              i++;
          }
      }
      return this.jgdlink;
   }
   //Set class id
   public String jDriveId(String gurl) throws UnsupportedEncodingException, MalformedURLException {
      if(this.jgid==null){
         String gid, start, end=""; int ini,len; Map<String, String> parts; boolean bool = false;
         int i1 = gurl.indexOf("/edit");
         int i2 = gurl.indexOf("?id=");
         int i3 = gurl.indexOf("/view");
         if( gurl.contains("/edit") ){
            System.out.println("idijaa2--"+i1+"--2--");
            gurl = gurl.replace("/edit","/view");
            bool=true;
         }
         else if( gurl.contains("?id=") ) {
            System.out.println("idijaa3--"+i2+"--3--");
         
            parts = splitQuery(gurl);
            this.jgid =  parts.get("id");
            return parts.get("id");
            //return "dsdsdd2";
         } 
         else if ( gurl.contains("/view") ) {
            System.out.println("idijaa4--"+i3+"--4--");
            gurl = gurl + "/view";
         }
         start  = "file/d/";
         end    = "/view";
         gurl = " " + gurl;
         ini    = gurl.indexOf(start);
         if (ini == 0) {
            return null;
         }
         ini += start.length();
         len = gurl.indexOf(end, ini) - ini;

         gid=gurl.substring(ini,ini+len);
         this.jgid=gid;
      }
      return this.jgid;
   }
   //
    public boolean isGoogleUrl(String gurl){
       if(gurl.contains("google.com")){return true;}
       else{return false;}
    }
   //Set class confirm code
   public String jGConfirmCode(String jgsource, int postlen){
      if(this.jgcode==null){
         String jgcode="",ini="";
         if(jgsource.length()>11){
            int start=-1, len=-1, end=-1, prestart=-1, prelen=-1;
         
            ini="confirm="; len=ini.length(); start = jgsource.indexOf(ini);
            prestart=start+len; end = prestart+postlen;
         
            jgcode = jgcode+jgsource.substring(prestart, end);
            this.jgcode=jgcode;
         }
      }
      else{jgcode=this.jgcode;}
      return jgcode;
   }
   //deprecated
   public static String locheader(Map<String, List<String>> header){
   	//String[] temp = explode("\r\n", page);String location=null;
      Map<String, List<String>> infoheader=header;String[] temp2=null; String location = null;
   	  //To get a map of all the fields of http header 
        //print all the fields along with their value. 
      for (Map.Entry<String, List<String>> mp : infoheader.entrySet()) 
      { 
         System.out.print(mp.getKey() + " : "); 
         System.out.println(mp.getValue().toString()); 
      }
      List<String> loc1 = infoheader.get("Location");
      if(loc1!=null){ 
         location = loc1.get(0);
      }
      else{
            //location = "";
      }
      return location;
   }
   //Retrive url header @element
   public static String getElementList(String element, Map<String, List<String>> mapList){
      String listEle="";
      listEle=(mapList.get(element)).get(0);
      	         
      for(int i=0;i<listEle.length();i++){
         char l=listEle.charAt(i);
         if(l==';'){
            listEle=listEle.substring(0,i+1);
            break;
         }
      }
   
      return listEle;
   }
   //Retrive HTML page
   public String GetHTML(HttpURLConnection urlConn) throws IOException{
       BufferedReader in = null;String stringbuffer = "";
       String HTML="";
       try {
           in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

           while((stringbuffer = in.readLine()) != null){
               HTML += stringbuffer;
           }
       } catch (IOException e) {
           e.printStackTrace();
       }

       return HTML;
   }
    public static boolean isJGSourceUpOrDown(String HTML){
        boolean jgsup=false;
        if(!(HTML==null)) {
            jgsup = true;
        }
        else{
            jgsup = false;
        }

        return jgsup;
    }
}
class DriveTask extends AsyncTask<Void, Void, Void> {
   private String textResult=null;
   private String gurl;
   private JGDriveStreamN JGDriveObject;
   public DriveTask(String gurl, JGDriveStreamN JGDriveObject){
      this.gurl=gurl;
      this.JGDriveObject=JGDriveObject;
   }
   @Override
   protected Void doInBackground(Void... params){
       URL url = null;
       HttpURLConnection urlConn = null;
       BufferedReader in;

           try {
               url = new URL(gurl);
           } catch (MalformedURLException e) {
               e.printStackTrace();
           }
           try {
               urlConn = (HttpURLConnection) url.openConnection();
           } catch (IOException e) {
               e.printStackTrace();
           }
           //Get Html page
           String html = null;
           try {
               html = JGDriveObject.GetHTML(urlConn);
           } catch (IOException e) {
               e.printStackTrace();
           }
           JGDriveObject.setHTML(html);

       //Get Url Headers
       Map<String, List<String>> map = urlConn.getHeaderFields();
       String NID=JGDriveObject.getElementList("Set-Cookie", map);
       //Get Confirm Code
       String jcode=JGDriveObject.jGConfirmCode(JGDriveObject.getHTML(), 4);
       JGDriveObject.setJGCode(jcode);
       //Set Cookie
       String setCookie="download_warning_13058876669334088843_11wtw6iY4rmeoAZzhsrodhFAop8CV-kEY="+jcode+"; "+NID;

       URL jgurlN= null;
       try {
           jgurlN = new URL(gurl+"&confirm="+jcode);
       } catch (MalformedURLException e) {
           e.printStackTrace();
       }
       HttpURLConnection urlConn2= null;
       try {
           urlConn2 = (HttpURLConnection)jgurlN.openConnection();
       } catch (IOException e) {
           e.printStackTrace();
       }
       // Set the cookie value to send

       urlConn2.setRequestProperty("Cookie", setCookie);
       try {
           urlConn2.setRequestMethod("GET");
       } catch (ProtocolException e) {
           e.printStackTrace();
       }
       urlConn2.setInstanceFollowRedirects(false);  //you still need to handle redirect manully.
       HttpURLConnection.setFollowRedirects(false);
       Map<String, List<String>> map2 = urlConn2.getHeaderFields();
       String location=JGDriveObject.getElementList("Location", map2);
       JGDriveObject.setJGDLink(location);

       //JGDriveObject.setHTML(textResult);

       return null;
   }
    @Override
    protected void onPostExecute(Void aVoid){
        JGDriveObject.setHTML(textResult);
        super.onPostExecute(aVoid);
    }
}
