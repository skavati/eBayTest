package common.utilities;

import java.io.IOException;
import java.io.InputStream;


import org.apache.log4j.*;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import common.toolbox.appium.TestAppium;
import common.toolbox.selenium.Waits;

public class JSchExampleSSHConnection {
	private static final Logger LOGGER = LogManager.getLogger(FileUtils.class);
	
	public static void main(String[] args) {
		String host = "10.252.131.227";
		String user = "anirudh";
		String command1 = "bash appium_4724.sh";
		// to kill all ports
		try {
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, 22);
			session.setPassword("qazxcvbnm");
			session.setConfig(config);
			session.connect();			
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command1);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);
			InputStream in = channel.getInputStream();
			channel.connect();
			// TestAppium.Trust_Dev_profile();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;					
				}
				if (channel.isClosed()) {				
					break;
				}
				try {
					Waits.waitInSeconds(1);
				} catch (Exception ee) {
				}
			}

			channel.disconnect();
			session.disconnect();
			
		} catch (Exception e) {
			LOGGER.info(e);
		}

	}

}
