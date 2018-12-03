/**
 *
 * WAVLTree
 *
 * An implementation of a WAVL Tree. (Haupler, Sen & Tarajan ‘15)
 *
 */

public class WAVLTree {

	/*
	 * TODO existing (STATIC?) helper fucntions: Rotate Left, Rotate Right,
	 * Double Rotate, InOrder traversal (what it does? how?)
	 */

	// FIELDS:

	// TODO root (WAVLNode) - root of the tree - default constructor shold be
	// TODO root=null?
	WAVLNode root;

	/**
	 * public boolean empty()
	 *
	 * returns true if and only if the tree is empty
	 *
	 */
	public boolean empty() {
		return false; // to be replaced by student code
	}

	/**
	 * public String search(int k)
	 *
	 * returns the info of an item with key k if it exists in the tree
	 * otherwise, returns null
	 * 
	 * iterative function - replace current acording to key, return info if
	 * current.key==k, null if current.key<k or current.key>k an current is a
	 * leaf
	 */
	public String search(int k) {
		WAVLNode current = root;
		while (true) {
			if (current.getKey() == k) {
				return current.getValue();
			} else if (k > current.getKey() && current.getRight().getRank() != -1) {
				current = current.getRight();
			} else if (current.getKey() > k && current.getRight().getRank() == -1) {
				return null;
			} else if (k < current.getKey() && current.getLeft().getRank() != -1) {
				current = current.getLeft();
			} else {
				return null;
			}

		}
	}

	// Insert Helper
	/**
	 * public int search_node
	 * 
	 * iterative function
	 * 
	 * @pre: root!=null
	 * @post returns the leaf that will be father of inserted key, null if
	 *       searched key exists
	 * 
	 *       TODO: is it possible to use the same name as function public String
	 *       search(int k)?
	 * 
	 */
	public WAVLNode search_node_insert(int k) {
		WAVLNode current = root;
		while (true) // loop will stop at some point
		{// while current is not an external leaf
			if (current.getKey() == k) {// if key exists in tree, return null
				return null;
			}

			// if k bigger then current key and right child exists, go right

			if (k > current.getKey() && current.getRight().getRank() != -1) {// TODO
																				// maybe
																				// substitute
																				// with
																				// isInner?
				current = current.getRight();
			}

			// if key bigger than current, and no right child,
			// then key need to be inserted as right child to current
			else if (k > current.getKey() && current.getRight().getRank() == -1) {
				return current;
			}

			// same with left

			else if (k < current.getKey() && current.getLeft().getRank() != -1) {
				current = current.getLeft();
			}

			else {
				return current;
			}
		}
	}

	/**
	 * 
	 * a function that takes in a node, and increments the size of all of the
	 * nodes in the path from input node to the root of the tree, to be used
	 * after insertion case 2 or case 3
	 */
	public void inc_size_upto_root(WAVLNode node) {
		while (node.getParent() != null) {
			node.size++;
			node = node.getParent();
		}
	}

	/**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the WAVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, 
   * or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) {
	   //1.==============Insert new node
	   
	   int ops_counter = 0; //init balacing opreations counter
	   WAVLNode new_node; // TODO init new node with info

	   
	   if (empty()){ //if tree is empty
		   root = new_node; //TODO tree init?
		   return ops_counter;
	   }
	   
	   WAVLNode insert_to = search_node_insert(k); // find father
	   
	   if (insert_to==null){ // if key in tree return -1
		   return -1;
	   }
	   
	   
	   if (k > insert_to.getKey()) //set new node as right or left child
	//TODO - change if changing placing method (function?)
	   {
		   insert_to.right = new_node;
	   }
	   
	   else
	   {
		   insert_to.left = new_node;

	   }
	   
	   //2.===============Balancing and update information
	   
	   /*
	    * TODO consider - encapsulation of insert cases functions
	    * TODO consider - general case 2/ case 3 function, 
	    * that receive a left/right parameter
	    * TODO consider - does destruction of (2,2) nodes have an effect?
	    * TODO ROOT CASES - Rotate function should consider case 
	    * that rotation is on root
	    */
	   
	   // start case identification with parent of new node
	   WAVLNode current = insert_to; 
	   
	   /*CASE 1
	    * first check if there's a bubbling up case 1 
	    * problem with iteration. in WAVL/AVL, case 1 in insertion is 
	    * the only one that can create a bubbling problem.
	    */
	   
	   
	   while (current.insert_is_case1())
	   {
		   current.rank++; // increment rank
		   current.size++; //increment size
		   ops_counter++; // increment ops counter
		   current = current.getParent(); //check parent
		   
	   }
	   
	   /*
	    * if case 2, perform rotate according to case, and update ranks and sizes
	    * size update is performed in the following way
	    */
	   if (current.insert_is_case2())
	   {
		   if () //TODO if case 2-left (i.e, 0 edge is on left)
		   { 
			   rotateRight(current); // rotate
			   current.rank--; //current is now left/right child
			   ops_counter+=2;
			   //TODO possible bug - what if there are no chidren?
			   current.size = 1 + current.getLeft().getSize() +
					   current.getRight().getSize();
			   current.getParent().size = 1 + 
					   current.getParent().getLeft().getSize() +
					   current.getSize();
			// inc size of path from grandfather of rotated node
			   if (current.getParent().getParent()!=null)
			   {
				   inc_size_upto_root(current.getParent().getParent());

			   }			   
		   }
		   else // same on case 2-right 
		   {
			   rotateLeft(current); // rotate
			   current.rank--; //current is now left/right child
			   ops_counter+=2;
			   current.size = 1 + current.getLeft().getSize() + 
					   current.getRight().getSize();
			   current.getParent().size = 1 + 
					   current.getParent().getRight().getSize() +
					   current.getSize();
			   if (current.getParent().getParent()!=null)
			   {
				   inc_size_upto_root(current.getParent().getParent());

			   }
		   }
	   }
	   
	   /*
	    * if case 3, do double rotate (rotate left right child of left child,
	    * then rotate right new left child, or the opposite)
	    * after rotations, update sizes (3 nodes to update) and ranks 
	    */
	   
	   if (current.insert_is_case3())
	   {
		   if () //TODO if case 3-left (i.e, 0 edge on left)
		   {
			rotateLeft(current.getLeft());
			rotateRight(current);
			ops_counter+=2;
			
			current = current.getParent();
			current.rank++;
			current.getLeft().rank--;
			current.getRight().rank--;
			ops_counter+=3; // update ranks
			// TODO -BUG- what if left / right are leafs? if else? try/catch?
			current.getLeft().size = 1 + current.getLeft().getLeft().size +
									 current.getLeft().getRight().size;
			current.getRight().size = 1 + current.getLeft().getLeft().size +
					 current.getLeft().getRight().size;
			current.size = 1 + current.getLeft().size + current.getRight().size;
			// current is father of changed subtree, its size updated, updated the rest
			if (current.getParent()!=null)
			{
				inc_size_upto_root(current.getParent());	
			}
			 
			
		   }
		   
		   else // same on right
		   {
		    rotateRight(current.getRight());
			rotateLeft(current);
			ops_counter+=2;
			
			current = current.getParent(); // update current to parent
			current.rank++;
			current.getLeft().rank--;
			current.getRight().rank--;
			ops_counter+=3; // update ranks
			//update sizes
			current.getLeft().size = 1 + current.getLeft().getLeft().size +
									 current.getLeft().getRight().size;
			current.getRight().size = 1 + current.getLeft().getLeft().size +
					 current.getLeft().getRight().size;
			current.size = 1 + current.getLeft().size + current.getRight().size;
			if (current.getParent()!=null)
			{
				inc_size_upto_root(current.getParent());	
			}		   
			}
	   }
	   
	   //else - no problem, father is a valid WAVL NODE
       return ops_counter;    // to be replaced by student code
   }

	// Delete Helper

	/**
	 * public WAVLNode search_node_delete(int k)
	 * 
	 * @return WAVLNode with input key, null if not in tree another version of
	 *         search that returns the node itself
	 */
	public WAVLNode search_node_delete(int k) {
		WAVLNode current = root;
		while (true) {// while current is not an external leaf
			if (current.getKey() == k) {// if key exists in tree, return node
										// with key
				return current;
			}

			// if k bigger then current key and right child exists, go right

			if (k > current.getKey() && current.getRight().getRank() != -1) {// TODO
																				// maybe
																				// substitute
																				// with
																				// isInner?
				current = current.getRight();
			}

			// if key bigger than current, and no right child, then node not in
			// tree
			else if (k > current.getKey() && current.getRight().getRank() == -1) {
				return null;
			}

			// same with left

			else if (k < current.getKey() && current.getLeft().getRank() != -1) {
				current = current.getLeft();
			}

			else {
				return null;
			}
		}
	}

	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there; the tree
	 * must remain valid (keep its invariants). returns the number of
	 * rebalancing operations, or 0 if no rebalancing operations were needed.
	 * returns -1 if an item with key k was not found in the tree.
	 */
	public int delete(int k) {
		int ops_counter = 0;
		WAVLNode to_delete = search_node_delete(k); // find node to be deleted

		if (to_delete == null) {
			return -1; // if not in tree, return -1
		}

		// 1. Node deletion and transplant

		// a. to_delete is a leaf
		if (!to_delete.isInnerNode()) // if leaf (real leaf, not external),
										// cancel pointers
		{
			if (to_delete.is_left()) // if to_del is left son, nullify
			{
				to_delete.getParent().left = null; // TODO - or to specify as
													// external(rank -1)?
			} else {
				to_delete.getParent().right = null; // same on right
			}
		}

		// b. to_delete is unary node (has exactly one son)
		else if (to_delete.getLeft() != null & to_delete.getRight() == null) // has
																				// left
																				// son
		{
			if (to_delete.is_left()) {
				to_delete.getParent().left = to_delete.left;
			} else {
				to_delete.getParent().right = to_delete.left;
			}
		} else if (to_delete.getLeft() == null & to_delete.getRight() != null) // has
																				// right
																				// son
		{
			if (to_delete.is_left()) {
				to_delete.getParent().left = to_delete.right;
			} else {
				to_delete.getParent().right = to_delete.right;
			}
		}

		// c. to_delete is a binary node - has both nodes
		/*
		 * TODO assumes implementation of Successor(x) and Transplant(x)
		 * Transplant(x) takes a node, and replace it with it's successor,
		 * according to 2 cases detailed in the book (p.296)
		 */

		else {
			transplant(to_delete);
		}

		return 42; // to be replaced by student code
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
		return 42; // to be replaced by student code
	}

	/**
	 * public WAVLNode getRoot()
	 *
	 * Returns the root WAVL node, or null if the tree is empty
	 *
	 */
	public WAVLNode getRoot() {
		return null;
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
	public class WAVLNode {

		/*
		 * TODO constructor of node (key, info, (is "fake" external?)) set as
		 * default that left and right child are external leafs upon initiation,
		 * set default size (size of sub tree including root) as 1
		 */

		// FIELDS:
		// TODO parent (WAVLNode)

		// TODO left (WAVLNode) - Left Rank?

		// TODO right (WAVLNode) - Right rank?

		// TODO SetRight and SetLeft functions?

		// TODO RANK (int) - according to wavl rules; rank==-1 if external?
		int rank;
		int size; // of subtree whose this is root
		WAVLNode right;
		WAVLNode left;
		WAVLNode parent;

		// TODO RANK DIFF FROM CHILDREN? (int)

		// TODO size of subtree whose root is this (int)

		// METHODS:
		// TODO IsLegal (boolean) - does the node need update? (is (1,1), (1,2),
		// (2,1) or (2,2) node)

		// TODO is external (field? function?) - external nodes have rank=-1
		// TODO make external nodes (rank (-1)) of size 0, to account for leafs

		/**
		 * TODO WAVL node constructor
		 * 
		 * @pre: key = legal int, info
		 */
		public void WAVLNode(int key, String info) {

		}

		// Insert helper functions

		/**
		 * TODO
		 * 
		 * @return true iff node is an insertion case 1 (0,1)/(1,0)
		 */
		public boolean insert_is_case1() {
			return false;
		}

		/**
		 * TODO
		 * 
		 * @return true iff node is an insertion case 2 (0,2)/(2,0) with (1,2)
		 *         left child/(2,1) right child
		 */
		public boolean insert_is_case2() {
			return false;
		}

		/**
		 * TODO
		 * 
		 * @return true iff node is an insertion case 3 (0,2)/(2,0) with (2,1)
		 *         left child/(1,2) right child
		 */
		public boolean insert_is_case3() {
			return false;
		}

		/**
		 * public boolean is_left()
		 * 
		 * @return true if left son of father
		 */
		public boolean is_left() {
			if (this.getParent().getLeft() == this) {
				return true;
			} else {
				return false;
			}
		}

		/**
		 * @return size of subtree
		 */
		public int getSize() {
			return size;
		}

		/**
		 * 
		 * @return parent of node RETURN NULL IF ROOT REACHED
		 */
		public WAVLNode getParent() {
			return null;
		}

		/**
		 * 
		 * @return rank of node
		 */
		public int getRank() {
			return 42;
		}

		public int getKey() {
			return 42; // to be replaced by student code
		}

		public String getValue() {
			return null; // to be replaced by student code
		}

		public WAVLNode getLeft() {
			return null; // to be replaced by student code
		}

		public WAVLNode getRight() {
			return null; // to be replaced by student code
		}

		public boolean isInnerNode() {
			return true; // to be replaced by student code
		}

		public int getSubtreeSize() {
			return 42; // to be replaced by student code
		}
	}

}
