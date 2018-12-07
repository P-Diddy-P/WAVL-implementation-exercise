/**
 *
 * WAVLTree
 *
 * An implementation of a WAVL Tree. (Haupler, Sen & Tarajan ‘15)
 *
 */

public class WAVLTree {

	private WAVLNode root;

	/**
	 * @param node
	 * TODO - set node parent as null?
	 */
	public void setRoot(WAVLNode node) {
		this.root = node;
	}

	/**
	 * static external leaf (rank -1)
	 */
	public static final WAVLNode EXT = new WAVLNode(-1, null, 0, -1);

	/**
	 * constructor of an empty tree with EXT as root
	 * TODO parent of root(EXT) == null?
	 * some functions (update size up to root, exm.)
	 * stop when they reach null.
	 * need to to keep parent of root as null at all times 
	 */
	public WAVLTree() {
		this.setRoot(EXT);
	}

	/**
	 * public boolean empty()
	 *
	 * @return: true iff the tree is empty
	 *
	 */
	public boolean empty() {
		return (this.root == EXT);
	}

	/**
	 * public String search(int k)
	 *
	 * returns the info of an item with key k if it exists in the tree
	 * otherwise, returns null
	 * 
	 * use searchNode function, return null if EXT found or WAVLNode with a
	 * different key meaning that node is not in tree
	 */
	public String search(int k) {
		WAVLNode ret = searchNode(k);
		if (ret == EXT || ret.getKey() != k) {
			return null;
		} else {
			return ret.getValue();
		}
	}

	/**
	 * public int searchNode
	 * 
	 * iterative function
	 * 
	 * @pre: root!=null
	 * @post: @return: the leaf that will be parent of inserted key, or node
	 *        with key in tree, if exists
	 */
	public WAVLNode searchNode(int k) {
		WAVLNode current = root;
		if (current == EXT) {
			return EXT;
		}
		while (true) // loop will stop at some point
		{// while current is not an external leaf
			if (current.getKey() == k) {// if key exists in tree, return it
				return current;
			}

			// if k bigger then current key and right child exists, go right

			if (k > current.getKey() && current.getRight() != EXT) {
				current = current.getRight();
			}

			//if k bigger then current key, and right is EXT, return current			
			else if (k > current.getKey() && current.getRight() == EXT) {
				return current;
			}

			// same with left

			else if (k < current.getKey() && current.getLeft() != EXT) {
				current = current.getLeft();
			}

			else {
				return current;
			}
		}
	}

	/**
	 * 
	 * updates sizes of all nodes from input node, up to root, based on sizes of
	 * both children (size = left child size+ right child size +1)
	 */
	public void updateSizeToRoot(WAVLNode node) {
		while (node != null) {
			node.size = 1 + node.getLeft().getSubtreeSize() 
					+ node.getRight().getSubtreeSize();
			node = node.getParent();

		}
	}

	/**
	 * public int insert(int k, String i)
	 *
	 * inserts an item with key k and info i to the WAVL tree. the tree must
	 * remain valid (keep its invariants). returns the number of rebalancing
	 * operations, or 0 if no rebalancing operations were necessary. returns -1
	 * if an item with key k already exists in the tree.
	 */
	public int insert(int k, String i) {
		// 1.==============Insert new node

		int ops_counter = 0; // init balancing operations counter
		WAVLNode new_node = new WAVLNode(k, i); // init new node with info

		if (empty()) { // if tree is empty
			this.setRoot(new_node); // set new_node as root
			//TODO - set new_node parent to null?/parent is null by default? define at set root
			return ops_counter;
		}

		WAVLNode insert_to = searchNode(k); // find father

		if (insert_to.key == new_node.key) { // if key in tree return -1
			return -1;
		}

		if (k > insert_to.getKey()) // set new node as right or left child
		{
			insert_to.setRight(new_node);
		}

		else {
			insert_to.setLeft(new_node);

		}

		// 2.===============Balancing and update information

		// start case identification with parent of new node
		WAVLNode toBalance = insert_to;

		/*
		 * CASE 1 first check if there's a bubbling up case 1 problem with
		 * iteration. in WAVL/AVL, case 1 in insertion is the only one that can
		 * create a bubbling problem.
		 */

		while (toBalance.insertIsCase1()) {
			toBalance.insertUpdateCase1();
			ops_counter++; // increment ops counter
			toBalance = toBalance.getParent(); // check parent

		}

		/*
		 * if case 2, call insertUpdateCase2
		 */
		if (toBalance.insertIsCase2()) {
			toBalance.insertUpdateCase2();
			ops_counter += 2;

			/*
			 * start resizing from current node (now right/left child after
			 * single rotation)
			 */
			updateSizeToRoot(toBalance);
		}

		/*
		 * insertUpdateCase3() 5 ops - 2 rotations, 3 pro/dem rank
		 */

		if (toBalance.insertIsCase3()) {
			toBalance.insertUpdateCase3();
			ops_counter += 5;

		}
		//update size up to root
		updateSizeToRoot(toBalance);

		// else - no problem, parent is a valid WAVL NODE
		return ops_counter;
	}

	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there; the tree
	 * must remain valid (keep its invariants). returns the number of
	 * rebalancing operations, or 0 if no rebalancing operations were needed.
	 * returns -1 if an item with key k was not found in the tree.
	 * 
	 * start rebalancing and resizing from current, after deletion phase
	 * 
	 */
	public int delete(int k) {
		int ops_counter = 0;
		WAVLNode to_delete = searchNode(k); // find node to be deleted
		WAVLNode to_balance; // node to start balancing from

		if (to_delete.getKey() != k) {
			return -1; // if not in tree, return -1
		}

		// 1. Node deletion and transplant

		// a. to_delete is a leaf - start balancing from to_delete parent
		if (!to_delete.isInnerNode()) // if leaf nullify
		{	
			/*
			 * start rebalancing from to_delete parent,
			 * delete the leaf
			 */
			if (to_balance.getParent()!=null){ //if not root, set to_balance 
				to_balance = to_delete.getParent();
				to_delete.deleteLeaf();
			}
			else { //to_delete is the root - parent is null
				// set root as EXT
				this.root=EXT;
				return 0;
			}

		}

		// b. to_delete is unary node (has exactly one child) -

		else if (to_delete.isUnary()) {

			to_delete.deleteUnary();

			to_balance = to_delete.getParent(); // start balancing from
												// to_delete parent
		}

		// c. if not a leaf or unary, to_delete is a binary node - has both
		// nodes

		else {
			/*
			 * transplant delete to_delete and replace it with it's successor
			 * (as described in Introduction To Algorithms, p.296). transplant
			 * return the parent of successor of to_delete, or to_delete parent,
			 * if successor is right child of to_delete.
			 * 
			 * TODO: transplant should give donor both the rank AND THE SIZE of
			 * deleted node; size (and rank, if necessary) will be updated later
			 * on during resizing of path from successor original parent
			 * 
			 */
			to_balance = transplant(to_delete);
			/*
			 * is this resizing really needed? just in case. possible to remove.
			 * TODO
			 */
			to_balance.updateSize();

		}

		// 2. rebalancing and resizing phase
		/*
		 * in WAVL tree we have 4 rebalancing operations after deletion: special
		 * case: first check is a (2,2) leaf - **and after every rotation(?)
		 * case 1: (3,2)/(2,3) node =>demote node case 2: (3,1)&(2,2) left
		 * child/(1,3)&(2,2) right child => double demote case 1 and case 2 can
		 * bubble up, require a while loop
		 * 
		 * case 3: (3,1)&(1/2,1)right child=> rotate left right child
		 * (1,3)&(1,1/2) left child=> rotate right left child case 4:
		 * (3,1)&(1,2)right child=>rotate right left child of right child,
		 * rotate left (1,3)&(2,1)left child=>rotate left right child of left
		 * child, rotate right case 3 and case 4 are terminal(?)
		 * 
		 */

		/*
		 * 2,2 leaf case: if parent of deleted node is a 2,2 leaf, demote it,
		 * and roll up possible problem
		 * 
		 */
		if (to_balance.deleteIs22Leaf()) {
			to_balance.deleteUpdate22Leaf();
			ops_counter++;
			to_balance.size--; // decrease size by 1
			to_balance = to_balance.getParent();
		}

		/*
		 * in wavl, cases 1 and 2 can bubble upwards. we can update sizes as we
		 * go along, similar to implementation of insert case 1 bubbling
		 */
		while (to_balance != null && (to_balance.deleteIsCase1() 
				|| to_balance.deleteIsCase2())) {

			if (to_balance.deleteIsCase1()) {
				to_balance.deleteUpdateCase1();
				ops_counter++;
			} else {
				to_balance.deleteUpdateCase2();
				ops_counter += 2;
			}
			to_balance.size--; // decrease size
			to_balance = to_balance.getParent();
		}

		/*
		 * if there are no more case 1 or 2, and balance is not null, (in case
		 * we demoted the root), it's a case 3 or 4 (or valid node)
		 */

		if (to_balance != null && to_balance.deleteIsCase3()) {
			/*
			 * ops_counter is incremented by the number of operations conducted
			 * by deleteUpdateCase3(). this number is not constant because
			 * there's an optional demotion if after the rotation, the
			 * problematic node turns into 2,2 leaf, so there's an extra
			 * demotion
			 */
			ops_counter += to_balance.deleteUpdateCase3();
			updateSizeToRoot(to_balance.getParent());

		}
		if (to_balance != null && to_balance.deleteIsCase4()) {
			to_balance.deleteUpdateCase4();
			ops_counter += 6; // 2 rotations + 4 pro/dem
			updateSizeToRoot(to_balance.getParent());
		}

		/*
		 * else - balance is a valid wavl node
		 */
		return ops_counter;
	}

	/**
	 * public String min()
	 *
	 * Returns the info of the item with the smallest key in the tree, or null
	 * if the tree is empty
	 */
	public String min() {
		return "42"; // to be replaced by student code
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree, or null if
	 * the tree is empty
	 */
	public String max() {
		return "42"; // to be replaced by student code
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree, or an empty
	 * array if the tree is empty.
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
	 * Example 1: select(1) returns the value of the node with minimal key
	 * Example 2: select(size()) returns the value of the node with maximal key
	 * Example 3: select(2) returns the value 2nd smallest minimal node, i.e the
	 * value of the node minimal node's successor
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
			this(key, value, 1, 0);
		}

		public WAVLNode(int key, String value, int size, int rank) {
			this.key = key;
			this.value = value;
			this.setRight(EXT);
			this.setLeft(EXT);
			this.size = size;
			this.rank = rank;

		}

		
		
		/**
		 * rankdiff-left and rankdiff-right, retur the rank diff between this
		 * and left or right child
		 * 
		 * @return: this.getRank-rank.getLeft.getRank() 
		 */
		public int rankDiffLeft() {
			return (this.getRank() - this.getLeft().getRank());
		}
		
		/**
		 * 
		 * @return: this.getRank-rank.getRight.getRank()
		 */
		public int rankDiffRight() {
			return (this.getRank() - this.getRight().getRank());
		}

		/**
		 * @return true iff node is an insertion case 1 (0,1)/(1,0)
		 */
		public boolean insertIsCase1() {
			return ((this.rankDiffLeft() == 0 && this.rankDiffRight() == 1)) 
					|| ((this.rankDiffLeft() == 1 && this.rankDiffRight() == 0));
		}
		
		/**
		 * @post: node is updated according to case 1 insert
		 */
		public void insertUpdateCase1() {
			this.rank++; // increment rank
			this.size++; // increment size
		}

		/**
		 * @return true iff node is an insertion case 2 (0,2)/(2,0) with (1,2)
		 *         left child/(2,1) right child
		 */
		public boolean insertIsCase2() {
			return (this.rankDiffLeft() == 0 && this.rankDiffRight() == 2 
					&& this.getLeft().rankDiffLeft() == 1
					&& this.getLeft().rankDiffRight() == 2)
					|| (this.rankDiffLeft() == 2 && this.rankDiffRight() == 0 
					&& this.getRight().rankDiffLeft() == 2
							&& this.getLeft().rankDiffRight() == 1);
		}

		public void insertUpdateCase2() {
			if (this.rankDiffLeft() == 0) {
				rotateRight(this.getLeft()); // rotate right the left child
			} else {
				rotateLeft(this.getRight()); // rotate left the right child
			}
			this.rank--;
		}

		/**
		 * 
		 * @return true iff node is an insertion case 3 (0,2)/(2,0) with (2,1)
		 *         left child/(1,2) right child
		 */
		public boolean insertIsCase3() {
			return (this.rankDiffLeft() == 0 && this.rankDiffRight() == 2 
					&& this.getLeft().rankDiffLeft() == 2
					&& this.getRight().rankDiffRight() == 1)
					|| (this.rankDiffLeft() == 2 && this.rankDiffRight() == 0 
					&& this.getLeft().rankDiffLeft() == 1
							&& this.getRight().rankDiffRight() == 2);
		}

		public void insertUpdateCase3() {
			int ops = 0;
			if (this.rankDiffLeft() == 0) { // if case 3-left (i.e, 0 edge on left)
				rotateLeft(this.getLeft().getRight()); // rotate left the right
														// child of left child
														// of this
				rotateRight(this.getLeft()); // rotate right (newly assigned,
												// prev grandchild of this) left
												// child of this
			}

			else // same on right
			{
				rotateRight(this.getRight().getLeft());
				rotateLeft(this.getRight());
			}
			//set current working node is this parent
			WAVLNode this_parent = this.getParent();

			//update ranks
			this_parent.rank++;
			this_parent.getLeft().rank--;
			this_parent.getRight().rank--;

			// update sizes
			this_parent.getLeft().updateSize();
			this_parent.getRight().updateSize();
			this_parent.updateSize();
		}

		/**
		 * @pre: this is a leaf
		 * @post: no node has this as child
		 */
		public void deleteLeaf() {
			if (this.isLeft()) // if to_del is left son, nullify
			{
				this.getParent().left = EXT; // TODO -BUG: set left sets node as
												// EXT parent - use of setLeft
												// do we care what's in EXT's
												// parent field?
			} else {
				this.getParent().right = EXT;
			}
		}

		/**
		 * updates size of single node (no iteration). does nothing if node is
		 * EXT.
		 */
		public void updateSize() {
			if (this != EXT) {
				this.size = 1 + this.getLeft().getSubtreeSize() + +this.getRight().getSubtreeSize();
			}
		}

		/**
		 * @return true iff this has exactly one child
		 */
		public boolean isUnary() {
			return ((this.getLeft() != EXT && this.getRight() == EXT)
					|| (this.getLeft() == EXT && this.getRight() != EXT));
		}

		/**
		 * @pre: this is a unary node (has exactly one child)
		 * @post: no node has this as child
		 */
		public void deleteUnary() {
			if (this.getLeft() != EXT & this.getRight() == EXT) // if this has
																// left child
			{
				if (this.isLeft()) {// if this is a left child
					this.getParent().setLeft(this.left);
				} else {
					this.getParent().setRight(this.left);
				}
			} else if (this.getLeft() == EXT & this.getRight() != EXT) // if
																			// this
																			// has
																			// right
																			// child
			{
				if (this.isLeft()) {
					this.getParent().setLeft(this.right);
				} else {
					this.getParent().setRight(this.right);
				}
			}
		}

		// DELETE Helper functions

		/**
		 * @return: true iff this is a 2,2 leaf
		 */
		public boolean deleteIs22Leaf() {
			return ((!this.isInnerNode()) && this.rankDiffLeft() == 2 && this.rankDiffRight() == 2);
		}

		public void deleteUpdate22Leaf() {
			this.rank--;
		}

		/**
		 * @return: true iff this is a case 1 node, i.e, (3,2) or (2,3) node
		 */
		public boolean deleteIsCase1() {
			return (this.rankDiffLeft() == 3 && this.rankDiffRight() == 2) || (this.rankDiffLeft() == 2 && this.rankDiffRight() == 3);
		}

		public void deleteUpdateCase1() {
			this.rank--;
		}

		/**
		 * @return: true iff this is a case 2 node, i.e, (3,1) node with (2,2)
		 *          right child, or (1,3) node with (2,2) left child
		 */
		public boolean deleteIsCase2() {
			return (this.rankDiffLeft() == 3 && this.rankDiffRight() == 1 && this.getRight().rankDiffLeft() == 2
					&& this.getRight().rankDiffRight() == 2)
					|| (this.rankDiffRight() == 3 && this.rankDiffLeft() == 1 && this.getLeft().rankDiffLeft() == 2
							&& this.getLeft().rankDiffRight() == 2);

		}

		public void deleteUpdateCase2() {
			this.rank--;
			if (this.rankDiffLeft() == 3) {
				this.getRight().rank--;
			} else {
				this.getLeft().rank--;
			}
		}

		/**
		 * 
		 * @return: true iff this is a case 3 node, i.e. (3,1) node with
		 *          (1,1)/(2,1) right child, or (1,3) node with (1,1)/(1,2) left
		 *          child
		 */
		public boolean deleteIsCase3() {
			return (this.rankDiffLeft() == 3 && this.rankDiffRight() == 1
					&& (this.getRight().rankDiffLeft() == 1 || this.getRight().rankDiffLeft() == 2)
					&& this.getRight().rankDiffRight() == 1)
					|| (this.rankDiffRight() == 3 && this.rankDiffLeft() == 1
							&& (this.getLeft().rankDiffRight() == 1 || this.getLeft().rankDiffRight() == 2)
							&& this.getLeft().rankDiffLeft() == 1);
		}

		public int deleteUpdateCase3() {
			int ops = 0;
			if (this.rankDiffLeft() == 3) {
				rotateRight(this.getLeft());
			} else {
				rotateLeft(this.getRight());
			}
			ops += 2;
			if (this.deleteIs22Leaf()) {
				this.rank--;
				ops++;
			}
			this.updateSize();
			this.getParent().updateSize();
			return ops;
		}

		/**
		 * 
		 * @return: true iff this is (3,1) node with (1,2) right child or (1,3)
		 *          node with (2,1) left child
		 */
		public boolean deleteIsCase4() {
			return (this.rankDiffLeft() == 3 && this.rankDiffRight() == 1 && this.getRight().rankDiffLeft() == 1
					&& this.getRight().rankDiffRight() == 2)
					|| (this.rankDiffLeft() == 1 && this.rankDiffRight() == 3 && this.getLeft().rankDiffLeft() == 2
							&& this.getLeft().rankDiffRight() == 1);
		}
		
		/**
		 * update node according to case 4 delete. rotate right/left grandchild,
		 * then rotate left/right new child, and conduct promotions/demotions
		 * update size locally of this, parent of this and "brother" of this,
		 * (subsequent resizing called in WAVLTree.delete).
		 */
		public void deleteUpdateCase4() {
			if (this.rankDiffLeft() == 3) {
				rotateRight(this.getRight().getLeft());
				rotateLeft(this.getRight());
				this.getParent().getRight().rank--;
				this.getParent().getRight().updateSize();
			} else {
				rotateLeft(this.getLeft().getRight());
				rotateRight(this.getLeft());
				this.getParent().getLeft().rank--;
				this.getParent().getLeft().updateSize();
			}
			this.rank--;
			this.updateSize();
			this.getParent().rank += 2;
			this.getParent().updateSize();

		}


		/**
		 * 
		 * @return rank of node
		 */
		public int getRank() {
			return this.rank;
		}

		
		public int getKey() {
			return this.key;
		}

		public String getValue() {
			return this.value;
		}

		/**
		 * 
		 * @return: left child, can be a regular WAVLNode or EXT
		 */
		public WAVLNode getLeft() {
				return this.left;
		}

		/**
		 * @post: newLeft is left child of this, and this is parent of newLeft
		 */
		public void setLeft(WAVLNode newLeft) {
			this.left = newLeft;
			this.getLeft().setParent(this);
		}

		/**
		 * 
		 * @return: right child, can be a regular WAVLNode or EXT
		 */
		public WAVLNode getRight() {
				return this.right;
		}

		public void setRight(WAVLNode newRight) {
			this.right = newRight;
			this.getRight().setParent(this);
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
		
		/**
		 * 
		 * @return: true iff at least one of this children are EXT
		 */
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

		/**
		 * @return @ret: true iff this is left son of parent
		 */
		public boolean isLeft() {
			if (this.getParent().getLeft() == this) {
				return true;
			} else {
				return false;
			}
		}

	}

	
}
