package fileHandling;

/**
 * Class to record one replacement action within _out.pck.
 * Stores a reference to the target file and the replacement,
 * as well as the index of the target file.
 * 
 * @author NuclearWinterMan
 * @version 0.1.1
 */
public class Transaction implements Comparable<Transaction>{
	
	private int targetIndex;
	
	private AssetFile target;
	private ReplacementFile replacement;
	
	
	/**
	 * Returns the value of target
	 * @return the target
	 */
	public AssetFile getTarget() {
		return target;
	}

	/**
	 * Method that sets the target to the input value.
	 * @param target the target to set
	 */
	public void setTarget(AssetFile target) {
		this.target = target;
	}

	/**
	 * Returns the value of replacement
	 * @return the replacement
	 */
	public ReplacementFile getReplacement() {
		return replacement;
	}

	/**
	 * Method that sets the replacement to the input value.
	 * @param replacement the replacement to set
	 */
	public void setReplacement(ReplacementFile replacement) {
		this.replacement = replacement;
	}

	public Transaction(AssetFile target, ReplacementFile replacement) {
		this.target = target;
		this.replacement = replacement;
		this.targetIndex = target.getIndex();
	}
	
	public int getIndex() {
		return targetIndex;
	}
	
	@Override
	public int compareTo(Transaction otherT) {
		return Integer.compare(targetIndex, otherT.getIndex());
	}
	
	@Override
	public String toString() {
		String targetStr = this.target.toString();
		String replaceStr = this.replacement.toString();
		return targetStr + " -> " + replaceStr;
	}
}
