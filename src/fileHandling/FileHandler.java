package fileHandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.DefaultListModel;


public class FileHandler {
	public static final int ARM9TABLELEN = 9610;
	public static final int ARM9TABLESTART = 0x48BA0;
	private static File arm9BinFHandle;
	private static File outPckFHandle;
	private static int[] offsetTable;
	private static AssetFile[] outPckContents;
	private static DefaultListModel<Transaction> changes;
	private static DefaultListModel<ReplacementFile> importedReplacements;
	
	
	public static File getArm9BinFHandle() {
		return arm9BinFHandle;
	}
	public static void setArm9BinFHandle(File arm9BinFHandle) {
		FileHandler.arm9BinFHandle = arm9BinFHandle;
	}
	public static File getOutPckFHandle() {
		return outPckFHandle;
	}
	public static void setOutPckFHandle(File outPckFHandle) {
		FileHandler.outPckFHandle = outPckFHandle;
	}
	public static AssetFile[] getOutPckContents() {
		return outPckContents;
	}
	public static DefaultListModel<ReplacementFile> getReplacementList() {
		return importedReplacements;
	}
	public static DefaultListModel<Transaction> getChanges() {
		return changes;
	}
	
	public static void initOffsets() {
		byte[][] arm9RawTable = new byte[ARM9TABLELEN][4];
		RandomAccessFile arm9BinHandle;
		try {
			arm9BinHandle = new RandomAccessFile(arm9BinFHandle, "r");
		} catch(FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
			return;
		}
		
		
		//Try to seek appropriate position in arm9 binary
		try {
			arm9BinHandle.seek(ARM9TABLESTART);
		} catch (IOException e) {
			System.out.println("Error seeking table in arm9bin");
			e.printStackTrace();
			try {
				arm9BinHandle.close();
			} catch (IOException e2) {
				System.out.println("Error closing arm9 file");
				e2.printStackTrace();
				
			}
			return;
		}
		
		//Try to read 5 values from arm9
		try {
			for (int i = 0; i < arm9RawTable.length; i++) {
				arm9BinHandle.read(arm9RawTable[i]);
			}
		} catch(IOException e) {
			System.out.println("Error reading from table");
			e.printStackTrace();
			try {
				arm9BinHandle.close();
			} catch (IOException e2) {
				System.out.println("Error closing arm9 file");
				e2.printStackTrace();
				return;
			}
			return;
		}
		
		
		//Try to close arm 9
		try {
			arm9BinHandle.close();
		} catch (IOException e) {
			System.out.println("Error closing arm9 file");
			e.printStackTrace();
			return;
		}
		offsetTable = convertOffTable(arm9RawTable);
		changes = new DefaultListModel<Transaction>();
		importedReplacements = new DefaultListModel<ReplacementFile>();
	}
	
	/**
	 * Attempts to read all files from out.pck.
	 * 
	 * @return	true if it successfully reads all files from out.pck, false otherwise.
	 */
	public static boolean readAllAssets() {
		// store current offset
		int currOffset;
		// store length of file
		int currLen;
		byte[] currData;
		RandomAccessFile outPckHandle;
		outPckContents = new AssetFile[ARM9TABLELEN-1];
		
		try {
			outPckHandle = new RandomAccessFile(outPckFHandle, "r");
		} catch(FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
			return false;
		}
		
		
		// Iterate through all indices in the table that correspond to a file
		// The last index of the table is the length of the 
		for(int i = 0; i < ARM9TABLELEN - 1; i++) {
			currOffset = offsetTable[i];
			currLen = offsetTable[i+1] - currOffset;
			currData = new byte[currLen];
			
			try {
				outPckHandle.readFully(currData);
			} catch (IOException e1) {
				System.out.println("Error reading from out.pck");
				e1.printStackTrace();
				try {
					outPckHandle.close();
				} catch (IOException e2) {
					System.out.println("Error closing _out.pck");
					e2.printStackTrace();
				}
				return false;
			}
			
			outPckContents[i] = new AssetFile(currData, i, currOffset);
		}
		
		try {
			outPckHandle.close();
		} catch(IOException e) {
			System.out.println("Error closing file");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public static boolean importAsset(File newAsset) {
		int fileLen = (int) newAsset.length();
		byte[] assetData = new byte[fileLen];
		RandomAccessFile assetHandle;
		// Try to open new file
		try {
			assetHandle = new RandomAccessFile(newAsset, "r");
		} catch (FileNotFoundException e1) {
			System.out.println("asset not found");
			return false;
		}
		
		// Try to read data
		try {
			assetHandle.read(assetData);
		} catch(IOException e1) {
			System.out.println("error reading file");
			e1.printStackTrace();
			try {
				assetHandle.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		// Try to close file
		try {
			assetHandle.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		ReplacementFile properAsset = new ReplacementFile(assetData, newAsset.getName());
		importedReplacements.addElement(properAsset);
		
		
		return true;
	}
	
	public static boolean registerTransaction(int targetId, ReplacementFile replacement) {
		AssetFile target = outPckContents[targetId];
		Transaction newChange = new Transaction(target, replacement);
		//Next, iterate across List to find the index to insert the new Transaction at
		// Four cases:
		// Empty list : insert
		// Index already exists in list : need to replace
		// All entries in the list come before new transactions : append to list
		// There is an entry somewhere within the list that the new transaction comes before
		// : insert in list right before the first transaction with a larger index than the new one
		
		// Use boolean variable to track if the transaction was inserted into the list during the loop
		boolean inserted = false;
		// Iterate through the list
		for(int i = 0; i < changes.size(); i++) {
			// Get the transaction at the current index
			Transaction currChange = changes.get(i);
			// Compare out.pck index to the new change
			if (currChange.getIndex() == targetId) {
				// Replace current change in the list
				changes.set(i, newChange);
				inserted = true;
				break;
				// Case in which we found the place to insert the new change 
			} else if(currChange.getIndex() > targetId) {
				// Insert at current index, shifting everything at index i and higher up one
				changes.add(i, newChange);
				inserted = true;
				// break ensures that this only happens once
				break;
			}
		}
		// Check if we got through the loop without adding change to list
		if (!inserted) {
			changes.addElement(newChange);
		}
		
		return true;
	}
	
	public static boolean removeTransaction(int transactionId) {
		changes.remove(transactionId);
		return true;
	}
	
	public static int numTransactions() {
		return changes.size();
	}
	
	public static void testOpen() {
		
	}
	
	public static void export(File outPckExport, File arm9Export) {
		// variable to track current offset within file.
		int currOffset = 0;
		// byte array to store current offset before writing to file
		byte[] currOffsetBA;
		// store data of file to be written
		byte[] fileDatToWrite;
		
		// Then, open files
		// Need to open three files:
		// The new arm9 and outPck
		// The original arm9
		RandomAccessFile outPckHandle;
		RandomAccessFile arm9Handle;
		RandomAccessFile arm9OrigHandle;
		// Attempt to initialize files,
		// If not, die
		try {
			outPckHandle = new RandomAccessFile(outPckExport, "rws");
			arm9Handle = new RandomAccessFile(arm9Export, "rws");
			arm9OrigHandle = new RandomAccessFile(arm9BinFHandle, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}
		
		//Copy the arm9 binary into new file
		
		// First, read contents of old arm9 binary into memory
		byte[] intermediateArm9 = new byte[(int) arm9BinFHandle.length()];
		try {
			arm9OrigHandle.readFully(intermediateArm9);
		} catch (IOException e1) {
			// Catch read issue
			System.out.println("Error while reading data from original arm 9 binary.");
			e1.printStackTrace();
			try {
				outPckHandle.close();
				arm9Handle.close();
				arm9OrigHandle.close();
			} catch (IOException e) {
				System.out.println("Error occurred while closing files");
				e.printStackTrace();
			}
			System.exit(1);
			return;
		}
		
		//Then, write the old data new file
		
		try {
			arm9Handle.write(intermediateArm9);
		} catch (IOException e1) {
			System.out.println("Error while writing old data to the new arm9 binary");
			e1.printStackTrace();
			try {
				outPckHandle.close();
				arm9Handle.close();
				arm9OrigHandle.close();
			} catch (IOException e2) {
				System.out.println("Error while closing files");
				e2.printStackTrace();
			}
			System.exit(1);
			return;
		}
		
		//Finally, attempt to close the arm9 binary
		
		try {
			arm9OrigHandle.close();
		} catch (IOException e1) {
			System.out.println("Error while closing the original arm9 binary");
			try {
				outPckHandle.close();
				arm9Handle.close();
			} catch (IOException e2) {
				System.out.println("Error while closing new files");
				e2.printStackTrace();
			}
			System.exit(1);
			return;
		}
		
		// Now, we can write the new data to the new files.
		
		//First, seek the table in the new arm9 binary
		
		try {
			arm9Handle.seek(ARM9TABLESTART);
		} catch (IOException e1) {
			System.out.println("Error while seeking table in new arm9 binary");
			e1.printStackTrace();
			try {
				outPckHandle.close();
				arm9Handle.close();
			} catch (IOException e2) {
				System.out.println("Error while closing new files");
				e2.printStackTrace();
			}
			System.exit(1);
			return;
		}
		//
		// Iterate across every file in _out.pck and perform the following:
		// 
		// First, write the current value of currOffset to the arm9 table
		// If there is a transaction for that index, write the replacement file into _out.pck
		// and pop the transaction from the list.  Then peek at the next transaction and 
		// If not, write the original
		// 
		
		for(int i = 0; i < outPckContents.length; i++) {
			currOffsetBA = ByteArrConverter.intToByteArr(currOffset);
			
			// try to write the current offset to the table
			try {
				arm9Handle.write(currOffsetBA);
			} catch (IOException e1) {
				System.out.println("Error while writing to arm9 table");
				e1.printStackTrace();
				try {
					outPckHandle.close();
					arm9Handle.close();
				} catch (IOException e2) {
					System.out.println("Error while closing files");
					e2.printStackTrace();
				}
				System.exit(1);
				return;
			}
			
			// Check if need to replace current file
			if(!changes.isEmpty() &&  changes.get(0).getIndex() == i) {
				// For the first transaction in the list,
				// Get the replacement AssetFile
				// and data array for that AssetFile
				fileDatToWrite = changes.get(0).getReplacement().getData();
				// Pop the first item on the list
				changes.remove(0);
				// On the off chance that two transactions with the same index have 
				// made it on to the list, remove all remaining items on the list with the same index as the current one
				while(!changes.isEmpty() && changes.get(0).getIndex() == i) {
					changes.remove(0);
				}
			} else {
				fileDatToWrite = outPckContents[i].getData();
			}
			
			//Try to write the contents of the file to out.pck
			try {
				outPckHandle.write(fileDatToWrite);
			} catch (IOException e1){
				System.out.println("Error while writing data to out.pck");
				e1.printStackTrace();
				try {
					outPckHandle.close();
					arm9Handle.close();
				} catch (IOException e2) {
					System.out.println("Error while closing files");
					e2.printStackTrace();
				}
				System.exit(1);
				return;
			}
			
			//Finally increment currOffset with the size of the file written
			currOffset += fileDatToWrite.length;
		}
		
		// Finally, some fenceposting
		// Need to write the final offset, the length of the entirety of the new _out.pck
		currOffsetBA = ByteArrConverter.intToByteArr(currOffset);
		// Try to write currOffsetBA to the last thing
		try {
			arm9Handle.write(currOffsetBA);
		} catch (IOException e1) {
			System.out.println("Error while writing to arm9 table");
			e1.printStackTrace();
			try {
				outPckHandle.close();
				arm9Handle.close();
			} catch (IOException e2) {
				System.out.println("Error while closing files");
				e2.printStackTrace();
			}
			System.exit(1);
			return;
		}
		
		
		try {
			outPckHandle.close();
			arm9Handle.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static int[] convertOffTable(byte[][] offTableRaw) {
		int[] offTable = new int[offTableRaw.length];
		for (int i = 0; i < offTable.length; i++) {
			offTable[i] = ByteArrConverter.byteArrToInt(offTableRaw[i]);
		}
		return offTable;
	}
	
	public static boolean verifyOutPck() {
		return ((long) offsetTable[offsetTable.length-1]) == outPckFHandle.length(); 
	}
}
