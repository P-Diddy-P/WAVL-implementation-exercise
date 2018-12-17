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
	 * static external leaf (rank -1).
	 * EXT's parent is null at all times.
	 */
	public static final WAVLNode EXT = new WAVLNode(-1, null, 0, -1);
	/**
	 * @post: node is this's root, and it's parent is null.
	 * 
	 */
	public void setRoot(WAVLNode node) {
		this.root = node;
		node.setParent(null);
	}

	/**
	 * constructor of an empty tree with EXT as root
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
	 * @return info of an item with key k ,if it exists in the tree
	 * otherwise, returns null
	 * 
	 * use searchNode function, return null if EXT found or WAVLNode with a
	 * different key meaning that node is not in tree
	 */
	public String search(int k) {
		WAVLNode ret = searchNode(k);
		if (ret == null || ret.getKey() != k) {
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
	 * @return: the leaf that will be parent of inserted key, or node
	 *        with key in tree, if exists
	 * 
	 * the function use an iterative binary search on the tree
	 * 
	 */
	public WAVLNode searchNode(int k) {
		WAVLNode current = root;
		if (current == EXT) {
			return EXT;
		}
		while (true)
		{// while current is not an external leaf
			if (current.getKey() == k) {// if key exists in tree, return it
				return current;
			}

			// if k bigger then current key and right child exists, go right

			if (k > current.getKey()){
				if (current.hasRight()){
					current = current.getRight();
				}
				else {
					return current;
				}
			}
			
			else {
				if (current.hasLeft()){
					current = current.getLeft();
				}
				else {
					return current;
				}
			}

		}
	}

	/**
	 * public void updateSizeToRoot
	 * 
	 * @post: for every node, node.size is: 1+node.left.size+node.right.size
	 * 
	 * updates sizes of all nodes from input node, up to root, based on sizes of
	 * both children (size = left child size+ right child size +1)
	 * 
	 * TODO - EXTnull
	 */
	public void updateSizeToRoot(WAVLNode node) {
		while (node != null) {
			node.updateSize();
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
				//TODO - maybe create setRoot(WAVLNode) func
				this.setRoot(EXT);
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

			to_balance.updateSize();

		}

		// 2. rebalancing and resizing phase
		/*
		 * in WAVL tree we have 4 rebalancing operations after deletion: special
		 * case: first check is a (2,2) leaf - **and after every rotation(?)
		 * case 1: (3,2)/(2,3) node =>demote node 
		 * case 2: (3,1)&(2,2) left
		 * child/(1,3)&(2,2) right child => double demote case 1 and case 2 can
		 * bubble up, require a while loop
		 * 
		 * case 3: (3,1)&(1/2,1)right child=> rotate left right child
		 * (1,3)&(1,1/2) left child=> rotate right left child 
		 * case 4:
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
			to_balance.updateSize(); // decrease size by 1
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
			ops_counter += 7; // 2 rotations + 5 pro/dem
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
		WAVLNode minNode = this.getRoot();
		
		while (minNode.hasLeft()) {
			minNode = minNode.getLeft();
		}
		return minNode.getValue();
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree, or null if
	 * the tree is empty
	 */
	public String max() {
		WAVLNode maxNode = this.getRoot();
		
		while (maxNode.hasRight()) {
			maxNode = maxNode.getRight();
		}
		return maxNode.getValue();
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree, or an empty
	 * array if the tree is empty.
	 */
	public int[] keysToArray() {
		int[] keyArray = new int[this.size()];
		
		keysToArrayRec(this.getRoot(), keyArray, 0);
		
		return keyArray;
	}
	
	private int keysToArrayRec(WAVLNode node, int[] resultArray, int writeIndex) {
		if (node.hasLeft()) {
			writeIndex = keysToArrayRec(node.getLeft(), resultArray, writeIndex);
		}
		resultArray[writeIndex] = node.getKey();
		writeIndex++;
		if (node.hasRight()) {
			writeIndex = keysToArrayRec(node.getRight(), resultArray, writeIndex);
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
		if (node.hasLeft()) {
			writeIndex = infoToArrayRec(node.getLeft(), resultArray, writeIndex);
		}
		resultArray[writeIndex] = node.getValue();
		writeIndex++;
		if (node.hasRight()) {
			writeIndex = infoToArrayRec(node.getRight(), resultArray, writeIndex);
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
		return this.root.getSubtreeSize();
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
	 * Returns the value of the i'th smallest key (return null if tree is empty)
	 * Example 1: select(1) returns the value of the node with minimal key Example
	 * 2: select(size()) returns the value of the node with maximal key Example 3:
	 * select(2) returns the value 2nd smallest minimal node, i.e the value of the
	 * node minimal node's successor
	 */
	public String select(int i) {
		if (i > this.getRoot().getSubtreeSize()) {
			return null;
		}
		
		WAVLNode selectedNode = this.getRoot();
		int nodeIndex = selectedNode.getLeft().getSubtreeSize() + 1;
		while (nodeIndex != i) {
			if (i > nodeIndex) {
				selectedNode = selectedNode.getRight();
				nodeIndex += (selectedNode.getLeft().getSubtreeSize() + 1);
			} else if (i < nodeIndex) {
				selectedNode = selectedNode.getLeft();
				nodeIndex -= (selectedNode.getRight().getSubtreeSize() + 1);
			}
		}
		return selectedNode.getValue();
	}
	
	/**
	 * @param rotNode.parent != null
	 * @param rotNode != EXT
	 */
	private static void rotate(WAVLNode rotNode) {
		WAVLNode tempPar = rotNode.getParent();
		
		if (rotNode.isRight()) { // rotNode is a right child
			WAVLNode tempChild = rotNode.getLeft();
			rotNode.setParent(rotNode.getParent().getParent());
			rotNode.setLeft(tempPar);
			rotNode.getLeft().setRight(tempChild);
			
		} else { // rotNode is a left child
			WAVLNode tempChild = rotNode.getRight();
			rotNode.setParent(rotNode.getParent().getParent());
			rotNode.setRight(tempPar);
			rotNode.getRight().setLeft(tempChild);
		}
	}
	
	/**
	 * @param node.right != EXT
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
			this(key, value, 1, 0);
		}
		
		/**
		 * overloading of WAVLNode constructor, for the special case of
		 * EXT creation in WAVLTree 
		 */
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
			if (this.hasLeft()){
				return (this.getRank() - this.getLeft().getRank());}
			else {
				return (this.getRank() + 1);
			}
		}
		
		/**
		 * 
		 * @return: this.getRank-rank.getRight.getRank()
		 */
		public int rankDiffRight() {
			if (this.hasRight()){
				return (this.getRank() - this.getRight().getRank());}
			else {
				return (this.getRank() + 1);
			}
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
					&& this.getLeft().rankDiffLeft() == 1 //left right child, cannot be EXT
					&& this.getLeft().rankDiffRight() == 2)
					|| (this.rankDiffLeft() == 2 && this.rankDiffRight() == 0 
					&& this.getRight().rankDiffLeft() == 2
							&& this.getLeft().rankDiffRight() == 1);
		}

		public void insertUpdateCase2() {
			if (this.rankDiffLeft() == 0) {
				rotateLeftChild(this); // rotate right the left child
			} else {
				rotateRightChild(this); // rotate left the right child
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
					&& this.getRight().rankDiffRight() == 1) //left or right child, CANNOT be EXT
					|| (this.rankDiffLeft() == 2 && this.rankDiffRight() == 0 
					&& this.getLeft().rankDiffLeft() == 1
							&& this.getRight().rankDiffRight() == 2);
		}

		public void insertUpdateCase3() {
			if (this.rankDiffLeft() == 0) { // if case 3-left (i.e, 0 edge on left)
				doubleRotateLeftChild(this);
			}

			else // same on right
			{
				doubleRotateRightChild(this);
			}
			//set current working node is this parent
			WAVLNode this_parent = this.getParent();//TODO check if not problematic

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
				this.getParent().setLeft(EXT); 
			} else {
				this.getParent().setRight(EXT);
			}
		}

		/**
		 * updates size of single node (no iteration). does nothing if node is
		 * EXT.
		 */
		public void updateSize() {
			if (this != EXT) {
				int leftSize = 0;
				int rightSize = 0;
				if (this.hasLeft()){
					leftSize = this.getLeft().getSubtreeSize();
				}
				if (this.hasRight()){
					rightSize = this.getRight().getSubtreeSize();
				}
				this.size = 1 + leftSize + rightSize;
			}
		}

		/**
		 * @return true iff this has exactly one child
		 */
		public boolean isUnary() {
			if ( (this.hasLeft() & !this.hasRight()) ||
				 (!this.hasLeft() & this.hasRight()) ){
				return true;
			}
			else {
				return false;
			}

		}

		/**
		 * @pre: this is a unary node (has exactly one child)
		 * @post: no node has this as child
		 */
		public void deleteUnary() {
			if (this.hasLeft()) // if this has left child
			{
				if (this.isLeft()) {// if this is a left child
					this.getParent().setLeft(this.getLeft());
				} else {
					this.getParent().setRight(this.getLeft());
				}
			} else if (this.hasRight())
			{
				if (this.isLeft()) {
					this.getParent().setLeft(this.getRight());
				} else {
					this.getParent().setRight(this.getRight());
				}
			}
		}

		// DELETE Helper functions

		/**
		 * @return: true iff this is a 2,2 leaf
		 */
		public boolean deleteIs22Leaf() {
			return ((!this.isInnerNode()) &&
					this.rankDiffLeft() == 2 &&
					this.rankDiffRight() == 2);
		}

		public void deleteUpdate22Leaf() {
			this.rank--;
		}

		/**
		 * @return: true iff this is a case 1 node, i.e, (3,2) or (2,3) node
		 */
		public boolean deleteIsCase1() {
			return (this.rankDiffLeft() == 3 & this.rankDiffRight() == 2) 
					|| (this.rankDiffLeft() == 2 & this.rankDiffRight() == 3);
		}

		public void deleteUpdateCase1() {
			this.rank--;
		}

		/**
		 * @return: true iff this is a case 2 node, i.e, (3,1) node with (2,2)
		 *          right child, or (1,3) node with (2,2) left child
		 */
		public boolean deleteIsCase2() {
			return (this.rankDiffLeft() == 3 & this.rankDiffRight() == 1 
					& this.getRight().rankDiffLeft() == 2
					& this.getRight().rankDiffRight() == 2)
					|| (this.rankDiffRight() == 3 & this.rankDiffLeft() == 1 
					& this.getLeft().rankDiffLeft() == 2
							& this.getLeft().rankDiffRight() == 2);

		}

		public void deleteUpdateCase2() {
			this.rank--;
			if (this.rankDiffRight() == 0) {
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
			return ((this.rankDiffLeft() == 3 && this.rankDiffRight() == 1)
					&& (this.getRight().rankDiffLeft() == 1))
					|| ((this.rankDiffRight() == 3 && this.rankDiffLeft() == 1)
							&& (this.getLeft().rankDiffRight() == 1));
		}

		public int deleteUpdateCase3() {
			int ops = 0;
			if (this.rankDiffLeft() == 3) {
				rotateRightChild(this);
			} else {
				rotateLeftChild(this);
			}
			this.rank--;//demote z
			this.getParent().rank++;//promote y
			ops += 3;
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
			return ((this.rankDiffLeft() == 3 
					&& this.rankDiffRight() == 1) 
					&& (this.getRight().rankDiffRight() == 2))
					|| ((this.rankDiffLeft() == 1 && this.rankDiffRight() == 3) 
					&& (this.getLeft().rankDiffLeft() == 2));
		}
		
		/**
		 * update node according to case 4 delete. rotate right/left grandchild,
		 * then rotate left/right new child, and conduct promotions/demotions
		 * update size locally of this, parent of this and "brother" of this,
		 * (subsequent resizing called in WAVLTree.delete).
		 */
		public void deleteUpdateCase4() {
			if (this.rankDiffLeft() == 3) {
				doubleRotateRightChild(this);
				this.getParent().getRight().rank--;
				this.getParent().getRight().updateSize();
			} else {
				doubleRotateLeftChild(this);
				this.getParent().getLeft().rank--;
				this.getParent().getLeft().updateSize();
			}
			this.rank--;
			this.updateSize();
			this.getParent().rank += 2;
			this.getParent().updateSize();

		}


		/**
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
		 * if EXT, return null
		 */
		public WAVLNode getLeft() {

			if (this.hasLeft()) {
				return this.left;
			} else {
				return null;
			}
			else {
				return null;
			}
		}

		/**
		 * @post: newLeft is left child of this, and this is parent of newLeft
		 */
		public void setLeft(WAVLNode newLeft) {
			this.left = newLeft;
			if (newLeft!=EXT){
				this.getLeft().setParent(this);
			}
		}



		public WAVLNode getRight() {
			if (this.hasRight()) {
				return this.right;
			} else {
				return null;
			}
		}

		public void setRight(WAVLNode newRight) {
			this.right = newRight;
			if (newRight!=EXT){
				this.getLeft().setParent(this);

			}
		}

		/**
		 * @return $ret == null => this == root
		 */
		public WAVLNode getParent() {
			return this.parent;
		}

		/**
		 * @param newParent != EXT
		 * @param newParent.key != this.key
		 */
		public void setParent(WAVLNode newParent) {
			this.parent = newParent;
			if (this.getKey() > newParent.getKey()) {
				newParent.setRight(this);
			} else {
				newParent.setLeft(this);
			}
		}
		
		public int getRank() {
			return this.rank;
		}
		
		public void setRank(int newRank) {
			this.rank = newRank;
		}
		
		/**
		 * @return: true iff both this' children are EXT
		 * 
		 */
		public boolean isInnerNode() {
			return (this.hasLeft() || this.hasRight());
		}

		public int getSubtreeSize() {
			return this.size;
		}
		
		public boolean hasLeft() {
			if (this.left==EXT){
				return false;
			}
			else {
				return true;
			}
		}
		
		public boolean hasRight() {
			if (this.right==EXT){
				return false;
			}
			else {
				return true;
			}
		}

	
}
  
		/**
		 * @return $ret min node such that $ret.key > $this.key
		 * @post ($ret.rank < $this.rank) => $ret.left == EXT
		 * @post ($ret == $this) => ($tree.max() == $this)
		 */
		public WAVLNode successor() {
			WAVLNode successorNode;
			
			// if $this has a right subtree, successor is in it
			if (this.hasRight()) {
				successorNode = this.getRight();
			
				while (successorNode.hasLeft()) {
					successorNode = successorNode.getLeft();
				}
			
			// $this has no right subtree, successor is first right parent
			} else {
				successorNode = this;
				
				while ((successorNode.getParent() != null) && successorNode.isRight()) {
					successorNode = successorNode.getParent();
				}
			}
			
			return successorNode;
		}
		
		private boolean isRight() {
			return (this.getParent().getRight() == this);
		}
		
		private boolean isLeft() {
			return (this.getParent().getLeft() == this);
		}
		
		
		/**
		 * @Description: replaces all pointers from $this siblings
		 * to @param.replacement and points from @param.replacement
		 * to $this siblings. @imp Does not change rank and size.
		 */
		private void replace(WAVLNode replacement) {
			replacement.setParent(this.getParent());
			replacement.setLeft(this.getLeft());
			replacement.setRight(this.getRight());
		}
		
		/**
		 * Description: collapses $this node to it's parent's position.
		 * 
		 * @imp this method assumes $this node is the right child of
		 * the parent and has no left child, so it does not treat all cases.
		 * This method is specifically built for a private case of
		 * transplant.
		 */
		private void collapse() {
			WAVLNode tempParent = this.getParent();
			this.setParent(tempParent.getParent());
			this.setLeft(tempParent.getLeft());
		}
		
		/**
		 * @pre this.isInnerNode()
		 * 
		 * @imp NOTE: this function does not update node sizes,
		 * rather it returns the lowest node requiring update and
		 * trusts later actions to update the size.
		 */
		public WAVLNode transplant() {
			WAVLNode donor = this.successor();
			
			if (donor.getParent() == this) {
				// transplant node has a leaf as a right child
				donor.collapse();
				return donor;
				
			} else if (donor == this) {
				// transplant node has no successor, and therefore
				// has no right child
				WAVLNode newOrigin = donor.getParent();
				if (this.hasLeft()) {
					this.getLeft().setParent(this.getParent());
				} else {
					donor.getParent().setRight(EXT);
				}
				return newOrigin;
				
			} else {
				// since we know donor is a successor, we can safely
				// assume that it is both a left child and that it has
				// no left child.
				WAVLNode newOrigin = donor.getParent();
				newOrigin.setLeft(donor.getRight());
				this.replace(donor);
				donor.setRank(this.getRank());
				
				return newOrigin;
			}
		}
	}
}
