package butter.droid.fragments.hosts;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;


public class JMegaDriveStreamN {
    private String jmegaurl = null;
    private String HTML=null;
    private String jlinkgen = null;

    //Set and get method
    public void setUrl(String gurl){this.jmegaurl=gurl;}
    public String getUrl(){return this.jmegaurl;}
    public void setHTML(String HTML){this.HTML=HTML;}
    public String getHTML(){return this.HTML;}
    public void setJLinkGen(String jlinkgen){this.jlinkgen=jlinkgen;}
    public String getJLinkGen(){return this.jlinkgen;}


    public JMegaDriveStreamN() {
    }
    public JMegaDriveStreamN(String jmegaurl){
        this.jmegaurl=jmegaurl;
    }

    //Fetch google stream link
    public String toString(){
        return this.jlinkgen;
    }

    public String buildN(){
        if(this.jlinkgen==null) {
            this.jlinkgen = this.jLinkGen(this.jmegaurl);
        }
        return this.jlinkgen;
    }

    public String jLinkGen(String gurl) {

        if(this.jlinkgen==null){
            new MDriveTask(gurl,this).execute();
            int i = 0;
            while( !this.isJMegaSourceUpOrDown(this, 1) ) {
                i++;
            }
        }
        return this.jlinkgen;
    }

    public boolean isJMegaSourceUpOrDown(JMegaDriveStreamN jMegaDriveIdObject, int ini) {
        boolean jgsup = false;
        if (ini == 0) {
            if (!(jMegaDriveIdObject.HTML == null)) {
                jgsup = true;
            } else {
                jgsup = false;
            }
        } else if (ini == 1) {
            if (!(jMegaDriveIdObject.jlinkgen == null)) {
                jgsup = true;
            } else {
                jgsup = false;
            }
        }
        return jgsup;
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
}
class MDriveTask extends AsyncTask<Void, Void, Void> {
    private String textResult=null;
    private String gurl;
    private JMegaDriveStreamN JMegaDriveObject;
    public MDriveTask(String gurl, JMegaDriveStreamN JMegaDriveObject){
        this.gurl=gurl;
        this.JMegaDriveObject=JMegaDriveObject;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params){

        if(JMegaDriveObject.getHTML()==null){
            URL url=null;
            HttpURLConnection urlConn = null;
            BufferedReader in;

            try {
                url=new URL(gurl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                urlConn = (HttpURLConnection)url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Get Html page
            String html = null;
            try {
                html = JMegaDriveObject.GetHTML(urlConn);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JMegaDriveObject.setHTML(html);
        }
        //Get link
        String jlinkgen = "", ini = "";
        int start = -1, len = -1, end = -1, prestart = -1, prelen = -1;

        ini = "source data-fluid-hd src=";
        len = ini.length();
        start = (JMegaDriveObject.getHTML()).indexOf(ini);
        prestart = start + len;
        end = (JMegaDriveObject.getHTML()).length() - 1;

        jlinkgen = jlinkgen + (JMegaDriveObject.getHTML()).substring(prestart + 1, end);
        jlinkgen = jlinkgen.substring(0, jlinkgen.indexOf("'"));
        JMegaDriveObject.setJLinkGen(jlinkgen);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}