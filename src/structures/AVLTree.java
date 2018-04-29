package structures;

public class AVLTree<K extends Comparable<K>> implements DynamicSet<K> {
	
	public class Node {
    	private K key;
    	private Node left;
    	private Node right;
    	private int height;
    	 
    	protected Node(K key) {
    		this.key = key;
    		this.height = 1;
    	}
    	
    	public K getKey() { return key; }
    	public Node getLeft() { return left; }
    	public Node getRight() { return right; }
    	public int getBalance() { 
    		if(left != null) {
    			if(right != null) return left.height - right.height;
    			else return left.height;
    		}
    		else {
    			if(right != null) return -right.height;
    			else return 0;
    		}
    	}
    	public int getHeight() { return height; }
    	
    	public void setKey(K key) { this.key = key; }
    	public void setLeft(Node left) { this.left = left; }
    	public void setRight(Node right) { this.right = right; }
    	public void updateHeight() {
    		if(left != null) {
    			if(right != null) this.height = 1 + Math.max(left.getHeight(), right.getHeight());
    			else this.height = 1 + left.getHeight();
    		}
    		else {
    			if(right != null) this.height = 1 + right.getHeight();
    			else this.height = 1;
    		}
    	}
    }
	
	private Node root;
    
	/**
	 * The implementation is the same as SimpleBST.
	 */
	@Override
	public K find(K key) {
		Node node = find(root, key);
		if(node != null) return node.getKey();
		else return null;
	}
	
	protected Node find(Node node, K key) {
    	//Node is empty
    	if(node == null) return null;
    	//Search the left subtree
    	else if (key.compareTo(node.getKey()) < 0) return find(node.getLeft(), key);
    	//Search the right subtree
    	else if (key.compareTo(node.getKey()) > 0) return find(node.getRight(), key);
    	//Tree has the key on its root
    	else return node;
    }

	@Override
	public void insert(K key) {
		root = insert(root, key);
	}

    private Node insert(Node node, K key) { 
    	//Node is empty
    	if (node == null) return new Node(key);
    	//Add to the left subtree
    	else if (key.compareTo(node.getKey()) < 0) node.setLeft(insert(node.getLeft(), key));
    	//Add to the right subtree
    	else if (key.compareTo(node.getKey()) > 0) node.setRight(insert(node.getRight(), key));
    	//Tree has the key on its root
    	else return node;
    		
    	node.updateHeight();
    	
    	//Rebalance if needed
    	return rebalance(node);
    }

	@Override
	public void remove(K key) {
		root = remove(root, key);
	}
	
	private Node remove(Node node, K key) {
		//Node is empty
    	if (node == null) return null;
    	//Delete on the left subtree
    	else if (key.compareTo(node.getKey()) < 0) node.setLeft(remove(node.getLeft(), key));
    	//Delete on the right subtree
        else if (key.compareTo(node.getKey()) > 0) node.setRight(remove(node.getRight(), key));
    	//Tree has the key on its root
    	else {
    		//The current node is a leaf (0 children).
    		if (node.getLeft() == null && node.getRight() == null) return null;
    		//The current node only has a right child
    		else if (node.getLeft() == null) {
                return node.getRight();
            }
    		//The current node only has a left child
    		else if (node.getRight() == null) {
                return node.getLeft();
            }
    		//The current node has two children
    		else {
    			Node largestLeftNode = getMax(node.getLeft());
    			node.setKey(largestLeftNode.getKey());
    			node.setLeft(remove(node.getLeft(), largestLeftNode.getKey()));
    		}
        }
    		
    	node.updateHeight();
    	
    	//Rebalance if needed
    	return rebalance(node);
	}
	
	private Node rebalance(Node node) {
		int balance = node.getBalance();
        if (balance > 1) {
        	Node child = node.getLeft();
        	int childBalance = child.getBalance();
        	//Left Left Case
        	if(childBalance >= 0) {
        		return rotateRight(node);
        	}
        	//Left Right Case
        	else {
        		node.setLeft(rotateLeft(node.getLeft()));
                return rotateRight(node);
        	}
        }
        else if (balance < -1) {
        	Node child = node.getRight();
        	int childBalance = child.getBalance();
        	//Right Right Case
        	if(childBalance <= 0) {
        		return rotateLeft(node);
        	}
        	//Right Left Case
        	else {
        		node.setRight(rotateRight(node.getRight()));
                return rotateLeft(node);
        	}
        }
        //Node is balanced
        else return node;
	}

	/**
	 * The implementation is the same as SimpleBST.
	 */
	@Override
	public K getMin() {
		Node node = getMin(root);
		if(node != null) return node.getKey();
		else return null;
	}
	
	protected final Node getMin(Node node) {
    	//Node is empty
    	if(node == null) return null;
    	//Left child is empty
    	else if(node.getLeft() == null) return node;
    	//Left child is not empty
    	else return getMin(node.getLeft());
    }

	/**
	 * The implementation is the same as SimpleBST.
	 */
	@Override
	public K getMax() {
		Node node = getMax(root);
		if(node != null) return node.getKey();
		else return null;
	}
	
	protected final Node getMax(Node node) {
    	//Node is empty
    	if(node == null) return null;
    	//Right child is empty
    	else if(node.getRight() == null) return node;
    	//Right child is not empty
    	else return getMax(node.getRight());
    }
	
	@Override
	public String toString() { 
		return toString(root);
    }
    
    // print the values in this BST in pre-order (to p)
    protected String toString(Node node) {
    	StringBuilder stringBuilder = new StringBuilder();
    	stringBuilder.append("(");
    	//Print the root
    	if(node != null) {
    		stringBuilder.append("[" + node.getHeight() + "]");
    		stringBuilder.append(node.getKey() + ",");
    		//Print the left subtree
    		stringBuilder.append(toString(node.getLeft()));
        	stringBuilder.append(",");
            //Print the right subtree
        	stringBuilder.append(toString(node.getRight()));
    	}
    	stringBuilder.append(")");
        
        return stringBuilder.toString();
    }

	@Override
	public void dump(String filename) {
		// TODO Auto-generated method stub
		
	}
	
	private Node rotateLeft(Node x) {
        Node y = x.getRight();
        if(y == null) return x;
        Node z = y.getLeft();
        
        y.setLeft(x);
        x.setRight(z);
        
        x.updateHeight();
        y.updateHeight();

        return y;
    }
    
	private Node rotateRight(Node x) {
        Node y = x.getLeft();
        if(y == null) return x;
        Node z = y.getRight();
        
        y.setRight(x);
        x.setLeft(z);
        
        x.updateHeight();
        y.updateHeight();

        return y;
    }
}
