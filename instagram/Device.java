package xunhu.instagram;

/**
 * Created by hu on 11/10/2015.
 */
//create a class for found device through bluetooth
public class Device {
    private String device_name ="";
    private String mac="";
    public Device(String device_name,String mac){
        this.device_name=device_name;
        this.mac=mac;
    }
    public String getDevice_name(){
        return this.device_name;
    }
    public String getMac(){
        return this.getMac();
    }
}
