package org.cmcc.service.sftp.utils;

public class SettleLogger implements com.jcraft.jsch.Logger {
    @Override
    public boolean isEnabled(int level) {
        return true;
    }

    @Override
    public void log(int level, String msg) {
        System.out.println(msg);
    }
}
