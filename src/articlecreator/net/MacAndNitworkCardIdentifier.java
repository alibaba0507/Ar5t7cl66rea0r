/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package articlecreator.net;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author alibaba0507
 */
public class MacAndNitworkCardIdentifier {

    public static void main(String[] args) throws Exception {
        System.out.println(">>>>> [" + getMacAddressByNetworkInterface()
                + "] MAC[" + getMacAddress()
                + "] [" + getNetworkInterfaceNo() + "] >>>>>>");
    }

    public static String getNetworkInterfaceNo() {
        String code = "";
        try {
            Enumeration<NetworkInterface> is = NetworkInterface.getNetworkInterfaces();
            while (is.hasMoreElements()) {
                NetworkInterface i = is.nextElement();
                byte[] addr = i.getHardwareAddress();
                if (addr != null) {
                    for (byte b : addr) {
                        code = code + (0xff & b);
                    }
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(MacAndNitworkCardIdentifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return code;
    }

    private static String getMacAddressByNetworkInterface() {
        try {
            List<NetworkInterface> nis = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nis) {
                if (!ni.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02x:", b));
                    }
                    return res1.deleteCharAt(res1.length() - 1).toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    public static String getMacAddress() {

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface nic = networkInterfaces.nextElement();
                byte[] hardwareAddress = nic.getHardwareAddress();
                StringBuilder buffer = new StringBuilder();
                if (null != hardwareAddress) {
                    for (byte b : hardwareAddress) {
                        buffer.append(String.format("%02X", b));
                    }
                    return buffer.toString();
                }
            }

            return Long.toHexString(System.currentTimeMillis());
        } catch (SocketException e) {

            return Long.toHexString(System.currentTimeMillis());
        }
    }
}
