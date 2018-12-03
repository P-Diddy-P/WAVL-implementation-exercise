/**
 *
 * WAVLTree
 *
 * An implementation of a WAVL Tree. (Haupler, Sen & Tarajan ‘15)
 *
 */

public class WAVLTree {

	WAVLNode root;
	public static final WAVLNode EXT = new WAVLNode(-1, null);

	public WAVLTree() {
		this.root = EXT;
	}

	/**
	 * public boolean empty()
	 *
	 * returns true if and only if the tree is empty
	 *
	 */
	public boolean empty() {
		return (this.root == EXT);
	}

	/**
	 * public String search(int k)
	 *
	 * returns the info of an item with key k if it exists in the tree otherwise,
	 * returns null
	 */
	public String search(int k) {
		return "42"; // to be replaced by student code
	}

	/**
	 * public int insert(int k, String i)
	 *
	 * inserts an item with key k and info i to the WAVL tree. the tree must remain
	 * valid (keep its invariants). returns the number of rebalancing operations, or
	 * 0 if no rebalancing operations were necessary. returns -1 if an item with key
	 * k already exists in the tree.
	 */
	public int insert(int k, String i) {
		return 42; // to be replaced by student code
	}

	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there; the tree
	 * must remain valid (keep its invariants). returns the number of rebalancing
	 * operations, or 0 if no rebalancing operations were needed. returns -1 if an
	 * item with key k was not found in the tree.
	 */
	public int delete(int k) {
		return 42; // to be replaced by student code
	}

	/**
	 * public String min()
	 *
	 * Returns the info of the item with the smallest key in the tree, or null if
	 * the tree is empty
	 */
	public String min() {
		return "42"; // to be replaced by student code
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree, or null if the
	 * tree is empty
	 */
	public String max() {
		return "42"; // to be replaced by student code
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree, or an empty array
	 * if the tree is empty.
	 */
	public int[] keysToArray() {
		int[] arr = new int[42]; // to be replaced by student code
		return arr; // to be replaced by student code
	}

	/**
	 * public String[] infoToArray()
	 *
	 * Returns an array which contains all info in the tree, sorted by their
	 * respective keys, or an empty array if the tree is empty.
	 */
	public String[] infoToArray() {
		String[] arr = new String[42]; // to be replaced by student code
		return arr; // to be replaced by student code
	}

	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 *
	 */
	public int size() {
		if (this.root == EXT) {
			return 0;
		} else {
			return this.root.getSubtreeSize();
		}
	}

	/**
	 * @return $ret == null => tree is empty
	 */
	public WAVLNode getRoot() {
		if (this.root == EXT) {
			return null;
		} else {
			return this.root;
		}
	}

	/**
	 * public int select(int i)
	 *
	 * Returns the value of the i'th smallest key (return -1 if tree is empty)
	 * Example 1: select(1) returns the value of the node with minimal key Example
	 * 2: select(size()) returns the value of the node with maximal key Example 3:
	 * select(2) returns the value 2nd smallest minimal node, i.e the value of the
	 * node minimal node's successor
	 *
	 */
	public String select(int i) {
		return null;
	}

	/**
	 * public class WAVLNode
	 */
	public static class WAVLNode {
		private int key;
		private int size;
		private int rank;
		private String value;

		private WAVLNode parent;
		private WAVLNode left;
		private WAVLNode right;

		/**
		 * @pre @param.key >= 0
		 * @pre @param.value != null
		 * @post this.parent == null && this.right == EXT && this.left == EXT
		 */
		public WAVLNode(int key, String value) {
			this.key = key;
			this.value = value;
			this.setRight(EXT);
			this.setLeft(EXT);
		}

		public int getKey() {
			return this.key;
		}

		public String getValue() {
			return this.value;
		}

		public WAVLNode getLeft() {
			if (this.left == EXT) {
				return null;
			} else {
				return this.left;
			}
		}

		public void setLeft(WAVLNode newLeft) {
			this.left = newLeft;
		}

		public WAVLNode getRight() {
			if (this.right == EXT) {
				return null;
			} else {
				return this.right;
			}
		}

		public void setRight(WAVLNode newRight) {
			this.left = newRight;
		}
		
		/**
		 * @return $ret == null => this == root
		 */
		public WAVLNode getParent() {
			return this.parent;
		}
		
		public void setParent(WAVLNode newParent) {
			this.parent = newParent;
		}

		public boolean isInnerNode() {
			if (this.getLeft() == EXT && this.getRight() == EXT) {
				return false;
			} else {
				return true;
			}
		}

		public int getSubtreeSize() {
			return this.size;
		}
	}
}
