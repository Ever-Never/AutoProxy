package com.mapple;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyAddress {

    @Override
    public String toString() {
        return "ProxyAddress " + ip + ":" + port + " " + type + " " + level + " " + location + " " + time;
    }

    private String ip;
    private int port;
    private String type;
    private String level;
    private String location;
    private String time;
    
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    
    private static boolean isIPAddress(String ipaddr) {
        boolean flag = false;
        if(ipaddr == null)
            return false;
        Pattern pattern = Pattern
                .compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
        Matcher m = pattern.matcher(ipaddr);
        flag = m.matches();
        return flag;
    }
    
    public boolean isValid() {
        if(!isIPAddress(ip)) {
            return false;
        }
        if(port <= 0 || port > 65535) {
            return false;
        }
        return true;
    }
    
    
}
