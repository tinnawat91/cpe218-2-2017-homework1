import javax.swing.*;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.net.URL;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Stack;

public class Homework1 extends JPanel
		implements TreeSelectionListener {
	private JEditorPane htmlPane;
	private JTree jTree;

	//Optionally play with line styles.  Possible values are
	//"Angled" (the default), "Horizontal", and "None".
	private static boolean playWithLineStyle = false;
	private static String lineStyle = "Horizontal";

	//Optionally set the look and feel.
	private static boolean useSystemLookAndFeel = false;

	public static Stack<Character> stack1=new Stack<Character>();
	public static Node root;
	public static String Screen;

	public Homework1() {
		super(new GridLayout(1,0));

		//Create the nodes.
		DefaultMutableTreeNode top =
				new DefaultMutableTreeNode(root);
		CreateNode(top,root);

		//Create a tree that allows one selection at a time.
		jTree = new JTree(top);
		jTree.getSelectionModel().setSelectionMode
				(TreeSelectionModel.SINGLE_TREE_SELECTION);

		//Listen for when the selection changes.
		jTree.addTreeSelectionListener(this);

		if (playWithLineStyle) {
			System.out.println("line style = " + lineStyle);
			jTree.putClientProperty("JTree.lineStyle", lineStyle);
		}

		//Create the scroll pane and add the tree to it.
		JScrollPane treeView = new JScrollPane(jTree);

		//Create the HTML viewing pane.
		htmlPane = new JEditorPane();
		htmlPane.setEditable(false);
		JScrollPane htmlView = new JScrollPane(htmlPane);

		//Add the scroll panes to a split pane.
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(htmlView);

		Dimension minimumSize = new Dimension(100, 50);
		htmlView.setMinimumSize(minimumSize);
		treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(100);
		splitPane.setPreferredSize(new Dimension(500, 300));

		ImageIcon leafIcon = createImageIcon("middle.gif");
		if (leafIcon != null) {
			DefaultTreeCellRenderer renderer =
					new DefaultTreeCellRenderer();
			renderer.setClosedIcon(leafIcon);
			renderer.setOpenIcon(leafIcon);
			jTree.setCellRenderer(renderer);
		}

		//Add the split pane to this panel.
		add(splitPane);
	}

	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Homework1.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	public static boolean IsLeaf=false;
	/** Required by TreeSelectionListener interface. */
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
				jTree.getLastSelectedPathComponent();

		if (node == null) return;
		IsLeaf=node.isLeaf();
		Object nodeInfo = node.getUserObject();
		DisplayNode((Node)nodeInfo);

	}

	public void DisplayNode(Node n)
	{
		inorderRoot(n);
		if(!IsLeaf)
		{
			Screen=Screen+"="+calculate(n);
		}
		htmlPane.setText(Screen);
	}
	public void  CreateNode(DefaultMutableTreeNode top,Node n)
	{
		if(n.left!=null)
		{
			DefaultMutableTreeNode left=new DefaultMutableTreeNode(n.left);
			top.add(left);
			CreateNode(left,n.left);
		}
		if(n.right!=null)
		{
			DefaultMutableTreeNode Right=new DefaultMutableTreeNode(n.right);
			top.add(Right);
			CreateNode(Right,n.right);
		}
	}

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event dispatch thread.
	 */
	private static void createAndShowGUI() {
		if (useSystemLookAndFeel) {
			try {
				UIManager.setLookAndFeel(
						UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				System.err.println("Couldn't use system look and feel.");
			}
		}

		//Create and set up the window.
		JFrame frame = new JFrame("Calculator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add content to the window.
		frame.add(new Homework1());

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI.
		String input="";
		if (args.length > 0) {
			input = args[0];
		}
		for (int i = 0; i <input.length(); i++) {
			stack1.push(input.charAt(i));
		}
		root= infix(root,stack1.pop());
		inorderRoot(root);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static Node infix(Node n,Character s) {
		n = new Node(s);
		if (n.check == '+' || n.check == '-' || n.check == '*' || n.check == '/') {//check operand
			n.right = infix(n.right,stack1.pop());//create right node
			//infix(n.right);
			n.left = infix(n.left,stack1.pop());//create left node
			//infix(n.left);
		}
		return n;
	}
	public static int calculate(Node c)
	{
		if(c.check =='+'){
			return calculate(c.left) + calculate(c.right);
		}
		if(c.check =='-'){
			return calculate(c.left) - calculate(c.right);
		}
		if(c.check =='*'){
			return calculate(c.left) * calculate(c.right);
		}
		if(c.check =='/'){
			return calculate(c.left) / calculate(c.right);
		}
		else return Integer.parseInt(c.check.toString());
	}
	public static void inorder(Node d)
	{
		if(d.check!='+'&&d.check!='-'&&d.check!='*'&&d.check!='/'){
			Screen+=d.check;
			System.out.print(d.check);
		}
		if(d.check == '+'){//print node.left operand node.right
			if(d!=root){
				Screen+="(";
				System.out.print("(");
			}
			inorder(d.left);
			Screen+="+";
			System.out.print("+");
			inorder(d.right);
			if(d!=root){
				Screen+=")";
				System.out.print(")");
			}
		}
		else if(d.check == '-'){
			if(d!=root){
				Screen+="(";
				System.out.print("(");
			}
			inorder(d.left);
			Screen+="-";
			System.out.print("-");
			inorder(d.right);
			if(d!=root){
				Screen+=")";
				System.out.print(")");
			}
		}else if(d.check == '*'){
			if(d!=root){
				Screen+="(";
				System.out.print("(");
			}
			inorder(d.left);
			Screen+="*";
			System.out.print("*");
			inorder(d.right);
			if(d!=root){
				Screen+=")";
				System.out.print(")");
			}
		}else if(d.check == '/'){
			if(d!=root){
				Screen+="(";
				System.out.print("(");
			}
			inorder(d.left);
			Screen+="/";
			System.out.print("/");
			inorder(d.right);
			if(d!=root){
				Screen+=")";
				System.out.print(")");
			}
		}

	}
	public static void inorderRoot(Node d)
	{
		Screen="";
		if(d.check!='+'&&d.check!='-'&&d.check!='*'&&d.check!='/'){
			Screen+=d.check;
			System.out.print(d.check);
		}
		else if(d.check=='+')
		{
			inorder(d.left);
			Screen+='+';
			System.out.print(d.check);
			inorder(d.right);
		}
		else if(d.check=='-')
		{
			inorder(d.left);
			Screen+='-';
			System.out.print(d.check);
			inorder(d.right);
		}
		else if(d.check=='*')
		{
			inorder(d.left);
			Screen+='*';
			System.out.print(d.check);
			inorder(d.right);
		}
		else if(d.check=='/')
		{
			inorder(d.left);
			Screen+='/';
			System.out.print(d.check);
			inorder(d.right);
		}
		System.out.println("="+calculate(d));
	}


}