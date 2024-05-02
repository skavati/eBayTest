package common.utilities;

import atu.testrecorder.ATUTestRecorder;

//http://www.randelshofer.ch/monte/

public class VideoUtils {

	private static ATUTestRecorder recorder;
	

	public ATUTestRecorder getRecorder() {
		return recorder;
	}

	public  static void Vediorecord(ATUTestRecorder recorder) {
		VideoUtils.recorder = recorder;
	}

	public static void  startRecording() throws Exception {
		String dir = System.getProperty("user.home") + "\\ScreenCast";
		// create folder "ScreenCast" if not exists
		FileUtils.CreateDirectory(dir); 
		recorder = new ATUTestRecorder(dir+"\\", StringUtils.getCurrentTimeStamp("ddMMyyHHmm"), false);
		// To start video recording.
		recorder.start();
	}

	public static void stopRecording() throws Exception {
		
		recorder.stop();
	}

	

}