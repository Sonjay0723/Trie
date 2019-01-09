package trie;

import java.util.ArrayList;

import javax.xml.ws.Holder;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		if (allWords.length == 0)
			return new TrieNode(null, null, null);
		
		TrieNode first = new TrieNode(new Indexes(0, (short)0, (short)(allWords[0].length() - 1)), null, null);
		TrieNode start = new TrieNode(null, first, null);
		
		for (int i = 1; i < allWords.length; i++) {
			TrieNode temp = first;
			TrieNode temp2 = first;
			
			short length = 0;
			short begin = -1, end = -1;
			
			while (temp != null) {
				
				if (temp.substr.startIndex > allWords[i].length()) {
					temp2 = temp;
					temp = temp.sibling;
					continue;
				}
				
				begin = temp.substr.startIndex;
				end = temp.substr.endIndex;
				
				String phrase = "";
				String word1 = allWords[i].substring(begin);
				String word2 = allWords[temp.substr.wordIndex].substring(begin, end + 1);
				for (int j = 0; j < word1.length(); j++) {
					if (j < word2.length() && word1.charAt(j) == word2.charAt(j))
						phrase += word1.charAt(j);
					else
						break;
				}
				
				length = (short)phrase.length();
				//System.out.println("phrase: " + phrase);
				
				if (length > 0) {
					if (length + begin - 1 < end) {
						temp2 = temp;
						break;
					}
					else if (length + begin - 1 == end) {
						temp2 = temp;
						temp = temp.firstChild;
					}
				}
				else {
					temp2 = temp;
					temp = temp.sibling;
				}
				
				phrase = "";
			}
			
			if (temp == null)
				temp2.sibling = new TrieNode(new Indexes(i, (short)begin, (short)(allWords[i].length() - 1)), null, null);
			else {
				TrieNode child = temp2.firstChild;
				
				Indexes parent = new Indexes(temp2.substr.wordIndex, (short)(length + begin), temp2.substr.endIndex);
				temp2.substr.endIndex = (short)(length + begin - 1);
				/*System.out.println("temp1 start: " + temp.substr.startIndex);
				System.out.println("temp1 end: " + temp.substr.endIndex);
				System.out.println("temp2 start: " + temp2.substr.startIndex);
				System.out.println("temp2 end: " + temp2.substr.endIndex);*/
				
				temp2.firstChild = new TrieNode(parent, null, null);
				temp2.firstChild.firstChild = child;
				Indexes index = new Indexes((short)i, (short)(length + begin), (short)(allWords[i].length() - 1));
				temp2.firstChild.sibling = new TrieNode(index, null, null);
			}
		}
		return start;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		if (root == null)
			return new ArrayList<TrieNode>();
		
		TrieNode temp = root;
		//TrieNode temp2 = root.firstChild;
		ArrayList<TrieNode> list = new ArrayList<TrieNode>();
		
		try {
			//System.out.println(temp.substr.toString());
			/*if (temp == null)
			return null;*/
		} catch (NullPointerException e) {}
		
		while (temp != null) {
			
			if (temp.substr == null)
				temp = temp.firstChild;
			
			//System.out.println(temp.toString());
			//int begin = temp.substr.startIndex;
			int end = temp.substr.endIndex;
			String phrase = allWords[temp.substr.wordIndex].substring(0, end + 1);
			//int length = prefix.length();
			
			boolean included = allWords[temp.substr.wordIndex].startsWith(prefix) || prefix.startsWith(phrase);
			
			if (included && temp.firstChild == null) {
				list.add(temp);
				//System.out.println(temp.toString());
			}
			else if (included) {
				list.addAll(completionList(temp.firstChild, allWords, prefix));
				//System.out.println(temp.toString());
			}
			
			temp = temp.sibling;
		}
		
		return list;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }