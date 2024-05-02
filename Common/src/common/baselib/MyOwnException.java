package common.baselib;

@SuppressWarnings("serial")
public class MyOwnException extends AssertionError {

	public MyOwnException(String msg, String imgfile) {
		super(msg + " " + imgfile);

	}

}
