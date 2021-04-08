package org.cmcc.service.sftp.utils;

import org.cmcc.service.sftp.config.SftpConfig;
import org.cmcc.service.sftp.exception.CustomerMsgException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import static org.cmcc.service.sftp.utils.TimeUtils.getDateBefore;

/**
 * 文件上传到ftp服务器工具类
 */
@Slf4j
@Component
@SuppressWarnings("unchecked")
public class SFTPUtils {

    private static SftpConfig sftpConfig;
    private ChannelSftp sftp = null;
    private Session sshSession = null;

    public static Vector<ChannelSftp.LsEntry> checkSftp() throws CustomerMsgException {
        SFTPUtils sftp = null;
        Vector<ChannelSftp.LsEntry> vector = null;
        try {
            sftp = new SFTPUtils();
            sftp.connect();
            vector = sftp.dirFiles();
        } catch (Exception e) {
            log.error("异常:" + e);
            throw new CustomerMsgException("500","Sftp文件服务器异常");
        } finally {
            assert sftp != null;
            sftp.disconnect();
        }
        return vector;
    }

    public static boolean lsSftp() throws CustomerMsgException {
        SFTPUtils sftp = null;
        boolean isDir = false;
        try {
            sftp = new SFTPUtils();
            sftp.connect();
            isDir = sftp.isDirExist(sftpConfig.getModDir() + getDateBefore(new Date(), -1));
        } catch (Exception e) {
            log.error("异常:" + e);
            throw new CustomerMsgException("500","Sftp文件服务器异常");
        } finally {
            assert sftp != null;
            sftp.disconnect();
        }
        return isDir;
    }

    public static SftpConfig getSftpConfig() {
        return SFTPUtils.sftpConfig;
    }

    @Autowired
    public void init(SftpConfig sftpConfig) {
        SFTPUtils.sftpConfig = sftpConfig;
    }

    /**
     * 通过SFTP连接服务器
     */
    private void connect() throws CustomerMsgException {
        try {
            com.jcraft.jsch.Logger logger = new SettleLogger();
            JSch.setLogger(logger);
            JSch jsch = new JSch();
            sshSession = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getHost(), Integer.parseInt(sftpConfig.getPort()));
            log.info("Session created.");
            sshSession.setPassword(sftpConfig.getPassword());
            sshSession.setConfig("StrictHostKeyChecking", "no");
            sshSession.setConfig("userauth.gssapi-with-mic", "no");
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect(1500);
            log.info("Session connected.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            log.info("Opening Channel.");
            sftp = (ChannelSftp) channel;
            log.info("Connected to " + sftpConfig.getHost() + ".");
        } catch (Exception e) {
            log.error("异常:" + e);
            throw new CustomerMsgException("500","Sftp文件服务器异常");
        }
    }

    /**
     * 关闭连接
     */
    private void disconnect() {
        if (this.sftp != null) {
            if (this.sftp.isConnected()) {
                this.sftp.disconnect();
                log.info("sftp is closed already");
            }
        }
        if (this.sshSession != null) {
            if (this.sshSession.isConnected()) {
                this.sshSession.disconnect();
                log.info("sshSession is closed already");
            }
        }
    }

    /**
     * 判断目录是否存在
     *
     * @param directory 文件夹
     * @return 是否存在
     */
    private boolean isDirExist(String directory) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpattrs = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpattrs.isDir();
        } catch (Exception e) {
            if ("no such file".equals(e.getMessage().toLowerCase())) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    /**
     * 获取指定目录下文件列表
     *
     * @return 是否存在
     */
    private Vector<ChannelSftp.LsEntry> dirFiles() throws CustomerMsgException {
        Vector<ChannelSftp.LsEntry> vector = new Vector<>();
        boolean isDirExistFlag = lsSftp();
        if (isDirExistFlag) {
            try {
                vector = sftp.ls(sftpConfig.getModDir() + getDateBefore(new Date(), -1));
            } catch (SftpException e) {
                log.error(e.getMessage());
                throw new CustomerMsgException("500","Sftp文件服务器异常");
            }
        }
        return vector;
    }
}
