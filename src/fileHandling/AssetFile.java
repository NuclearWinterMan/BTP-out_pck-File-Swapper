package fileHandling;


public class AssetFile {
	/**
	 * Position of file within the _out.pck file array.
	 * Replacement files are given an index of -1.
	 */
	private int index;
	/** 
	 * Length of the file
	 */
	private int length;
	/**
	 * Location of file within _out.pck.
	 * Replacement files are given an offset of -1.
	 */
	private int offset;
	/**
	 * Array that stores the contents of the file.
	 */
	private byte[] data;
	
	/**
	 * Create an AssetFile given a byte array that corresponds to the contents.
	 * Used to create an AssetFile object for replacement files, so sets offset and 
	 * index to -1.
	 * 
	 * AssetFile object is bound to passed array and does not make a copy.
	 * @param contents 	byte array that corresponds to the contents of the file to be created.
	 */
	public AssetFile(byte[] contents) {
		this.offset = -1;
		this.index = -1;
		this.length = contents.length;
		this.data = contents;
	}
	
	/**
	 * Create an AssetFile given a byte array and its position with the _out.pck data structure.
	 * Used to create an AssetFile for target files, so requires offset and index as parameters.
	 * 
	 * AssetFile object is bound to passed array and does not make a copy.
	 * @param contents	byte array that corresponds to the contents of the file to be created.
	 * @param index		index of file within out.pck file array
	 * @param offset	offset of file within _out.pck
	 */
	public AssetFile(byte[] contents, int index, int offset) {
		this.offset = offset;
		this.index = index;
		this.length = contents.length;
		this.data = contents;
	}

	/**
	 * Returns the length of the asset file.
	 * @return The length of the file
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Returns the offset of the file within out.pck
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Returns the underlying byte array.
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}
	
	/**
	 * Returns the index of the file within _out.pck's file array.  
	 * This corresponds to the entry in arm9's file table.
	 * @return The index of the file
	 */
	public int getIndex() {
		return index;
	}
	
	@Override
	public String toString() {
		String strRep1 = String.format("[%d] ", this.index);
		String strRep2 = String.format("0x%08X", this.offset);
		return strRep1+strRep2;
	}
}
