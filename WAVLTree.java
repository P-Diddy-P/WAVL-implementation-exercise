/**
 *
 * WAVLTree
 *
 * An implementation of a WAVL Tree. (Haupler, Sen & Tarajan ‘15)
 *
 */

public class WAVLTree {

	private WAVLNode root;
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
		WAVLNode minNode = this.getRoot();
		
		while (minNode.getLeft() != null) {
			minNode = minNode.getLeft();
		}
		
		return minNode.getValue();
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree, or null if the
	 * tree is empty
	 */
	public String max() {
		WAVLNode maxNode = this.getRoot();
		
		while (maxNode.getRight() != null) {
			maxNode = maxNode.getLeft();
		}
		
		return maxNode.getValue();
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree, or an empty array
	 * if the tree is empty.
	 */
	public int[] keysToArray() {
		int[] keyArray = new int[this.size()];
		
		keysToArrayRec(this.getRoot(), keyArray, 0);
		
		return keyArray;
	}
	
	private int keysToArrayRec(WAVLNode node, int[] resultArray, int writeIndex) {
		if (node.getLeft() != null) {
			writeIndex = keysToArrayRec(node.getLeft(), resultArray, writeIndex);
		}
		resultArray[writeIndex] = node.getKey();
		writeIndex++;
		if (node.getRight() != null) {
			writeIndex = keysToArrayRec(node.getLeft(), resultArray, writeIndex);
		}
		return writeIndex;
	}

	/**
	 * public String[] infoToArray()
	 *
	 * Returns an array which contains all info in the tree, sorted by their
	 * respective keys, or an empty array if the tree is empty.
	 */
	public String[] infoToArray() {
		String[] infoArray = new String[this.size()];

		infoToArrayRec(this.getRoot(), infoArray, 0);
		
		return infoArray;
	}
	
	private int infoToArrayRec(WAVLNode node, String[] resultArray, int writeIndex) {
		if (node.getLeft() != null) {
			writeIndex = infoToArrayRec(node.getLeft(), resultArray, writeIndex);
		}
		resultArray[writeIndex] = node.getValue();
		writeIndex++;
		if (node.getRight() != null) {
			writeIndex = infoToArrayRec(node.getLeft(), resultArray, writeIndex);
		}
		return writeIndex;
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
		// TODO Edge cases: tree is empty and i>this.size
		// TODO both conditions are the same assuming EXT.size < 1
		if (i > this.getRoot().getSubtreeSize()) {
			return null;
		}
		
		WAVLNode selectedNode = this.getRoot();
		int nodeIndex = selectedNode.getLeft().getSubtreeSize() + 1;
		while (nodeIndex != i) {
			if (i > nodeIndex) {
				selectedNode = selectedNode.getRight();
				nodeIndex += selectedNode.getLeft().getSubtreeSize() + 1;
			} else if (i < nodeIndex) {
				selectedNode = selectedNode.getLeft();
				nodeIndex -= 1 + selectedNode.getRight().getSubtreeSize();
			}
		}
		return selectedNode.getValue();
	}
	
	/**
	 * @pre @param.rotNode.parent != null
	 * @pre @param.rotNode != EXT
	 */
	private static void rotate(WAVLNode rotNode) {
		WAVLNode tempPar = rotNode.getParent();
		
		// rotNode is a right child
		if (rotNode.getKey() > rotNode.getParent().getKey()) {
			WAVLNode tempChild = rotNode.getLeft();
			rotNode.setParent(rotNode.getParent().getParent());
			rotNode.setLeft(tempPar);
			rotNode.getLeft().setRight(tempChild);
			
		// rotNode is a left child
		} else {
			WAVLNode tempChild = rotNode.getRight();
			rotNode.setParent(rotNode.getParent().getParent());
			rotNode.setRight(tempPar);
			rotNode.getRight().setLeft(tempChild);
		}
	}
	
	/**
	 * @pre @param.node.right != EXT
	 * @post @param.node.parent == @pre @param.node.right
	 */
	public static void rotateRightChild(WAVLNode node) {
		rotate(node.getRight());
	}
	
	/**
	 * @pre @param.node.left != EXT
	 * @post @param.node.parent == @pre @param.node.left
	 */
	public static void rotateLeftChild(WAVLNode node) {
		rotate(node.getLeft());
	}
	
	/**
	 * @pre @param.node.right.left != EXT
	 */
	public static void doubleRotateRightChild(WAVLNode node) {
		rotate(node.getRight().getLeft());
		rotate(node.getRight());
	}
	
	
	/**
	 * @pre @param.node.left.right != EXT
	 */
	public static void doubleRotateLeftChild(WAVLNode node) {
		rotate(node.getLeft().getRight());
		rotate(node.getLeft());
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
			if (this.left == null) {
				return null;
			} else {
				return this.left;
			}
		}

		public void setLeft(WAVLNode newLeft) {
			this.left = newLeft;
		}

		public WAVLNode getRight() {
			if (this.right == null) {
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
		
		public int getRank() {
			return this.rank;
		}
		
		public void setRank(int newRank) {
			this.rank = newRank;
		}

		public boolean isInnerNode() {
			if (this.getLeft() == null && this.getRight() == null) {
				return false;
			} else {
				return true;
			}
		}

		public int getSubtreeSize() {
			return this.size;
		}
		
		/**
		 * @return $ret min node such that $ret.key > this.key
		 * @post $ret.rank<this.rank => $ret.left == EXT
		 */
		public WAVLNode successor() {
			WAVLNode successorNode;
			
			// if this has a right subtree, successor is in it
			if (this.getRight() != null) {
				successorNode = this.getRight();
			
				while (successorNode.getLeft() != null) {
					successorNode = successorNode.getLeft();
				}
			
			// this has no right subtree, successor is the first right parent
			} else {
				successorNode = this;
				
				while (successorNode.getParent().getLeft() != successorNode) {
					successorNode = successorNode.getParent();
				}
			}
			
			return successorNode;
		}
		
		private boolean isARightChild() {
			return (this.getParent().getRight() == this);
		}
		
		private boolean isALeftChild() {
			return (this.getParent().getRight() == this);
		}
		
		private int calculateSize() {
			return this.getRight().getSubtreeSize() + this.getLeft().getSubtreeSize() + 1;
		}
		
		
		/**
		 * @Description: replaces all pointers from $this siblings
		 * to @param.replacement and points from @param.replacement
		 * to $this siblings.
		 */
		private void replace(WAVLNode replacement) {
			if (this.getParent() != null) {
				replacement.setParent(this.getParent());
				if(this.isALeftChild()) {
					this.getParent().setLeft(replacement);
				} else {
					this.getParent().setRight(replacement);
				}
			} else {
				replacement.setParent(null);
			}
			
			replacement.setLeft(this.getLeft());
			replacement.setRight(this.getRight());
			if(this.getRight() != null) {
				this.getRight().setParent(replacement);
			}
			if(this.getLeft() != null) {
				this.getLeft().setParent(replacement);
			}
		}
		
		/**
		 * Description: collapses $this node to it's parent's position.
		 * 
		 * @imp this method assumes $this node is the right child of
		 * the parent and has no left child, so it is not general.
		 * This method is specifically built for a private case of
		 * transplant.
		 */
		private void collapse() {
			WAVLNode tempParent = this.getParent();
			
			if (this.getParent().getParent() == null) {
				this.setParent(null);
			} else {
				this.setParent(this.getParent().getParent());
			}
			
			this.setLeft(this.getParent().getLeft());
			tempParent.getLeft().setParent(this);
			if(tempParent.isARightChild()) {
				tempParent.getParent().setRight(this);
			} else { // tempParent.isALeftChild()
				tempParent.getParent().setLeft(this);
			}
			this.setRank(tempParent.getRank());
			this.calculateSize();
		}
		
		/**
		 * @pre this.right != EXT && this.left != EXT
		 * 
		 * @imp NOTE: this function does not update node sizes,
		 * rather it returns the lowest node requiring update and
		 * trusts later actions to update the size.
		 */
		public WAVLNode transplant() {
			WAVLNode donor = this.successor();
			
			if (donor.getParent() == this) {
				donor.collapse();
				return donor;
			} else {
				// since we know donor is a successor, we can safely
				// assume that it is both a left child and that it has
				// no left child.
				WAVLNode newOrigin = donor.getParent();
				if (donor.getRight() != null) {
					donor.getRight().setParent(newOrigin);
					newOrigin.setLeft(donor.getRight());
				} else {
					newOrigin.setLeft(EXT);
				}
				this.replace(donor);
				
				return newOrigin;
			}
		}
	}
}
