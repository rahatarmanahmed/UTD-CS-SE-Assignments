import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
// BinarySearchTree class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x
// boolean contains( x )  --> Return true if x is present
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order
// ******************ERRORS********************************
// Throws UnderflowException as appropriate

/**
 * Implements an unbalanced binary search tree.
 * Note that all "matching" is based on the compareTo method.
 * @author Mark Allen Weiss
 */
public class BinarySearchTree<AnyType extends Comparable<? super AnyType>>
{
    /**
     * Construct the tree.
     */
    public BinarySearchTree( )
    {
        root = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     * @param x the item to insert.
     */
    public void insert( AnyType x )
    {
        root = insert( x, root );
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     * @param x the item to remove.
     */
    public void remove( AnyType x )
    {
        root = remove( x, root );
    }

    /**
     * Find the smallest item in the tree.
     * @return smallest item or null if empty.
     */
    public AnyType findMin( )
    {
        if( isEmpty( ) )
            throw new UnderflowException( );
        return findMin( root ).element;
    }

    /**
     * Find the largest item in the tree.
     * @return the largest item of null if empty.
     */
    public AnyType findMax( )
    {
        if( isEmpty( ) )
            throw new UnderflowException( );
        return findMax( root ).element;
    }

    /**
     * Find an item in the tree.
     * @param x the item to search for.
     * @return true if not found.
     */
    public boolean contains( AnyType x )
    {
        return contains( x, root );
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty( )
    {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty( )
    {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree( )
    {
        if( isEmpty( ) )
            System.out.println( "Empty tree" );
        else
            printTree( root );
    }

    /**
     * Internal method to insert into a subtree.
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<AnyType> insert( AnyType x, BinaryNode<AnyType> t )
    {
        if( t == null )
            return new BinaryNode<>( x, null, null );
        
        int compareResult = x.compareTo( t.element );
            
        if( compareResult < 0 )
            t.left = insert( x, t.left );
        else if( compareResult > 0 )
            t.right = insert( x, t.right );
        else
            ;  // Duplicate; do nothing
        return t;
    }

    /**
     * Internal method to remove from a subtree.
     * @param x the item to remove.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<AnyType> remove( AnyType x, BinaryNode<AnyType> t )
    {
        if( t == null )
            return t;   // Item not found; do nothing
            
        int compareResult = x.compareTo( t.element );
            
        if( compareResult < 0 )
            t.left = remove( x, t.left );
        else if( compareResult > 0 )
            t.right = remove( x, t.right );
        else if( t.left != null && t.right != null ) // Two children
        {
            t.element = findMin( t.right ).element;
            t.right = remove( t.element, t.right );
        }
        else
            t = ( t.left != null ) ? t.left : t.right;
        return t;
    }

    /**
     * Internal method to find the smallest item in a subtree.
     * @param t the node that roots the subtree.
     * @return node containing the smallest item.
     */
    private BinaryNode<AnyType> findMin( BinaryNode<AnyType> t )
    {
        if( t == null )
            return null;
        else if( t.left == null )
            return t;
        return findMin( t.left );
    }

    /**
     * Internal method to find the largest item in a subtree.
     * @param t the node that roots the subtree.
     * @return node containing the largest item.
     */
    private BinaryNode<AnyType> findMax( BinaryNode<AnyType> t )
    {
        if( t != null )
            while( t.right != null )
                t = t.right;

        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     * @return node containing the matched item.
     */
    private boolean contains( AnyType x, BinaryNode<AnyType> t )
    {
        if( t == null )
            return false;
            
        int compareResult = x.compareTo( t.element );
            
        if( compareResult < 0 )
            return contains( x, t.left );
        else if( compareResult > 0 )
            return contains( x, t.right );
        else
            return true;    // Match
    }

    /**
     * Internal method to print a subtree in sorted order.
     * @param t the node that roots the subtree.
     */
    private void printTree( BinaryNode<AnyType> t )
    {
        if( t != null )
        {
            printTree( t.left );
            System.out.print(t.element + " ");
            printTree( t.right );
        }
    }

    /**
     * Internal method to compute height of a subtree.
     * @param t the node that roots the subtree.
     */
    private int height( BinaryNode<AnyType> t )
    {
        if( t == null )
            return -1;
        else
            return 1 + Math.max( height( t.left ), height( t.right ) );    
    }
    
    // Basic node stored in unbalanced binary search trees
    private static class BinaryNode<AnyType>
    {
            // Constructors
        BinaryNode( AnyType theElement )
        {
            this( theElement, null, null );
        }

        BinaryNode( AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt )
        {
            element  = theElement;
            left     = lt;
            right    = rt;
        }

        AnyType element;            // The data in the node
        BinaryNode<AnyType> left;   // Left child
        BinaryNode<AnyType> right;  // Right child
    }

    public static class UnderflowException extends RuntimeException
    {
        /**
         * Construct this exception object.
         * @param message the error message.
         */
        public UnderflowException()
        {
            super();
        }
    }


      /** The tree root. */
    private BinaryNode<AnyType> root;


        // Test program
    // public static void main( String [ ] args )
    // {
    //     BinarySearchTree<Integer> t = new BinarySearchTree<>( );
    //     final int NUMS = 4000;
    //     final int GAP  =   37;

    //     System.out.println( "Checking... (no more output means success)" );

    //     for( int i = GAP; i != 0; i = ( i + GAP ) % NUMS )
    //         t.insert( i );

    //     for( int i = 1; i < NUMS; i+= 2 )
    //         t.remove( i );

    //     if( NUMS < 40 )
    //         t.printTree( );
    //     if( t.findMin( ) != 2 || t.findMax( ) != NUMS - 2 )
    //         System.out.println( "FindMin or FindMax error!" );

    //     for( int i = 2; i < NUMS; i+=2 )
    //          if( !t.contains( i ) )
    //              System.out.println( "Find error1!" );

    //     for( int i = 1; i < NUMS; i+=2 )
    //     {
    //         if( t.contains( i ) )
    //             System.out.println( "Find error2!" );
    //     }
    // }

    // CODE BY RAHAT AHMED AFTER HERE:

    public int count()
    {
        return count(root);
    }

    private int count(BinaryNode<AnyType> node)
    {
        if(node == null)
            return 0;
        return count(node.left) + count(node.right) + 1;
    }

    public int countLeaves()
    {
        return countLeaves(root);
    }

    private int countLeaves(BinaryNode<AnyType> node)
    {
        if(node == null)
            return 0;
        if(node.left == null && node.right == null)
            return 1;
        return countLeaves(node.left) + countLeaves(node.right);
    }

    public int countFullNodes()
    {
        return countFullNodes(root);
    }

    private int countFullNodes(BinaryNode<AnyType> node)
    {
        if(node == null)
            return 0;
        if(node.left != null && node.right != null)
            return countFullNodes(node.left) + countFullNodes(node.right) + 1;
        return countFullNodes(node.left) + countFullNodes(node.right);
    }

    public boolean isFull()
    {
        return isFull(root);
    }

    private boolean isFull(BinaryNode<AnyType> node)
    {
        if(node == null)
            return false;
        if(node.left == null && node.right == null)
            return true;
        return isFull(node.left) && isFull(node.right);
    }

    /*
        Returns parent of value. If value is not in the tree, returns null. 
        If value is root, returns null
    */
    public AnyType getParent(AnyType value)
    {
        if(root.element.compareTo(value) == 0)
            return null;
        return getParent(value, root);
    }

    public AnyType getParent(AnyType value, BinaryNode<AnyType> node)
    {
        if(node == null)
            return null;
        if((node.left != null && node.left.element.compareTo(value) == 0) ||
            (node.right != null && node.right.element.compareTo(value) == 0))
            return node.element;
        AnyType leftResult = getParent(value, node.left);
        if(leftResult != null)
            return leftResult;
        return getParent(value, node.right);
    }

    public void addAll(BinarySearchTree<AnyType> other)
    {
        // addAll(other.root);
        List<AnyType> list = other.toList();
        Collections.shuffle(list);
        for(AnyType element : list)
            insert(element);
    }

    // public void addAll(BinaryNode<AnyType> node)
    // {
    //     if(node == null)
    //         return;
    //     insert(node.element);
    //     if(Math.random() < .5) // Adding them randomly, left then right, or right then left
    //         addAll(node.left);
    //     else
    //         addAll(node.right);
    // }

    public List<AnyType> toList()
    {
        List<AnyType> list = new ArrayList<>();
        toList(list, root);
        return list;
    }

    public void toList(List<AnyType> list, BinaryNode<AnyType> node)
    {
        if(node == null)
            return;
        toList(list, node.left);
        list.add(node.element);
        toList(list, node.right);
    }

    public int intpathlen()
    {
        return intpathlen(root, 0);
    }

    public int intpathlen(BinaryNode<AnyType> node, int depth)
    {
        if(node == null)
            return 0;
        return intpathlen(node.left, depth+1) + intpathlen(node.right, depth+1) + depth;
    }

    public void printLevels()
    {
        Queue<BinaryNode<AnyType>> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty())
        {
            BinaryNode<AnyType> node = queue.remove();
            if(node == null)
                continue;
            System.out.print(node.element +" ");
            queue.add(node.left);
            queue.add(node.right);
        }
        System.out.println();
    }

    public boolean compareStructure(BinarySearchTree<AnyType> that)
    {
        return compareStructure(this.root, that.root);
    }

    private boolean compareStructure(BinaryNode<AnyType> thisNode, BinaryNode<AnyType> thatNode)
    {
        boolean leftResult, rightResult;

        if(thisNode.left == null && thatNode.left == null)
            leftResult = true;
        else if(thisNode.left != null && thatNode.left != null )
            leftResult = compareStructure(thisNode.left, thisNode.left);
        else
            leftResult = false;

        if(thisNode.right == null & thatNode.right == null)
            rightResult = true;
        else if(thisNode.right != null && thatNode.right != null)
            rightResult = compareStructure(thisNode.right, thatNode.right);
        else
            rightResult = false;

        return leftResult && rightResult;
    }

    public static void main( String [ ] args )
    {
        BinarySearchTree<Integer> t = new BinarySearchTree<>( );
        BinarySearchTree<Integer> t2 = new BinarySearchTree<>( );
        int NUMS = 40;
        int GAP  =   37;

        System.out.println( "Checking... (no more output means success)" );

        for( int i = GAP; i != 0; i = ( i + GAP ) % NUMS )
        {
            t.insert(i);
            t2.insert(i);
        }

        for( int i = 1; i < NUMS; i+= 2 )
        {
            t.remove(i);
            t2.remove(i);
        }

        // t has even numbers

        BinarySearchTree<Integer> u = new BinarySearchTree<>( );
        GAP = 33;

        for( int i = GAP; i != 0; i = ( i + GAP ) % NUMS )
            u.insert( i );

        for( int i = 0; i < NUMS; i+= 3 )
            u.remove( i );

        System.out.println("Testing assignment code:");
        System.out.print("t: "); t.printTree(); System.out.println();
        System.out.print("t2: "); t2.printTree(); System.out.println();
        System.out.print("u: "); u.printTree(); System.out.println();
        System.out.println("t.count(): " + t.count());
        System.out.println("t.countLeaves(): " + t.countLeaves());
        System.out.println("t.countFullNodes(): " + t.countFullNodes());
        System.out.println("t.isFull(): " + t.isFull());
        System.out.println("t.getParent(22): " + t.getParent(22));
        System.out.println("t.intpathlen(): " + t.intpathlen());
        System.out.println("t.compareStructure(u): " + t.compareStructure(u));
        System.out.println("t.compareStructure(t2)): " + t.compareStructure(t2));

        t.addAll(u);
        System.out.print("t after t.addAll(u): "); t.printTree(); System.out.println();
        System.out.print("t.printLevels(): "); t.printLevels();

    }
}