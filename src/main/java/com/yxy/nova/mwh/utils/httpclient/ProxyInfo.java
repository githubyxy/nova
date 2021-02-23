package com.yxy.nova.mwh.utils.httpclient;

/**
 * Created by chenchanglong on 2019/7/8.
 */
public class ProxyInfo {

    private String address;

    private Integer port;

    private String user;

    private String password;

    public ProxyInfo(String address, Integer port, String user, String password) {
        this.address = address;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public Integer getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProxyInfo proxyInfo = (ProxyInfo) o;

        return getAddress().equals(proxyInfo.getAddress()) && getPort().equals(proxyInfo.getPort());

    }

    @Override
    public int hashCode() {
        int result = getAddress().hashCode();
        result = 31 * result + getPort().hashCode();
        return result;
    }
}
