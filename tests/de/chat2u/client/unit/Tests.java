package de.chat2u.client.unit;

import org.junit.Ignore;
import org.junit.Test;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Chat2U:
 * * de.chat2u.client.unit:
 * * * Created by KAABERT on 15.11.2016.
 */
public class Tests {

    @Ignore
    @Test
    public void CopyToClipboard() {
        try {
            String address = InetAddress.getLocalHost().getHostAddress();
            StringSelection selection = new StringSelection(address);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
            System.out.println(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
