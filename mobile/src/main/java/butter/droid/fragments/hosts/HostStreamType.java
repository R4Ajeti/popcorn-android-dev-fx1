package butter.droid.fragments.hosts;

import java.io.IOException;

public class HostStreamType {
    private String hurl=null;
    private String host=null;
    //hosts url
    private String gurl=null;
    private String murl=null;
    //hosts name
    private String[] HOSTTYPE={"google.com","megadrive.co"};
    private String[] urltype=new String[HOSTTYPE.length];
    private int indent = -1;
    //HostObject
    JGDriveStreamN JGDriveObject = null;
    JMegaDriveStreamN JMegaDriveObject = null;
    //Java host drive link
    private String jhdlink = null;
    //Get set methods
    public void setHurl(String hurl){this.hurl=hurl;}
    public String getHurl(){ return this.hurl;}
    public void setHost(String host){this.host=host;}
    public String getHost(){ return this.host;}
    public void setGurl(String gurl){this.gurl=gurl;}
    public String getGurl(){ return this.gurl;}
    public void setMurl(String murl){this.murl=murl;}
    public String getMurl(){ return this.murl;}
    //Get and set method - array
    public void setUrlType(String[] urltype){this.urltype=urltype;}
    public void setUrlType(String urltype, int i){this.urltype[i]=urltype;}
    public String[] getUrlType(){ return this.urltype;}
    public String getUrlType(int i){ return this.urltype[i];}
    public int getIndent(){ return this.indent;}
    //Common constructor
    public HostStreamType(String hurl){
        this.hurl=hurl;
    }
    public String toString() {
        return this.jhdlink;
    }
    public void isHostDriveUrlBuild() {
        int i = 0;
        while ( (i<(HOSTTYPE.length)) ){
            if ((this.hurl).contains(this.HOSTTYPE[i])) {
                this.urltype[i] = hurl;
                this.indent=i;
                break;
            }
            i++;
        }
    }
    public void buildN(){
        if(this.indent!=-1&&this.hurl!=null){
            if(indent==0){
                this.isGoogleDriveUrl(this.hurl);
            }
            if(indent==1){
                this.isMegaDriveUrl(this.hurl);
            }
        }
    }
    public void isGoogleDriveUrl(String hurl){
        JGDriveObject = new JGDriveStreamN();
        JGDriveObject.setUrl(hurl);
        try {
            JGDriveObject.buildN();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.jhdlink = JGDriveObject.toString();
    }
    public void isMegaDriveUrl(String hurl){
        JMegaDriveObject = new JMegaDriveStreamN();
        JMegaDriveObject.setUrl(hurl);
        JMegaDriveObject.buildN();
        this.jhdlink = JMegaDriveObject.toString();
    }

}
