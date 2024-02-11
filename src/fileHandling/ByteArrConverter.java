package fileHandling;

public class ByteArrConverter {
	// Will likely break on systems where a byte isn't 8 bits or an int isn't 4 bytes
	public static int byteArrToInt(byte[] arr) {
		int outDat = 0;
		outDat |= (arr[0] & 0xFF);
		outDat |= (arr[1] & 0xFF) << 8;
		outDat |= (arr[2] & 0xFF) << 16;
		outDat |= (arr[3] & 0xFF) << 24;
		return outDat;
	}

	public static byte[] intToByteArr(int dat) {
		byte[] outDat = new byte[4];
		outDat[0] = (byte) dat;
		outDat[1] = (byte) (dat >> 8);
		outDat[2] = (byte) (dat >> 16);
		outDat[3] = (byte) (dat >> 24);
		return outDat;
	}
}
