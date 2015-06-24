public class BinaryTree {
    int data;
    BinaryTree left;
    BinaryTree right;
    BinaryTree(int data) {
        this.data = data;
        left = null;
        right = null;
    }

//插入节点
    public void insertTree2(BinaryTree root ,int data){
        if(data >= root.data){
            if(root.right == null){
                root.right = new BinaryTree(data);
            }else{
                insertTree2(root.right,data);
            }
        }else{
            if(root.left == null){
                root.left = new BinaryTree(data);
            }else{
                insertTree2(root.left,data);
            }
        }
    }

    public int height2(){
        int heightOfTree;
        if (this == null) return -1;
        int leftHeight = (left == null ? 0 : left.height2());
        int rightHeight = (right == null ? 0 : right.height2());
        return (leftHeight < rightHeight ? rightHeight:leftHeight ) +1;
    }

    public void preOrder2(BinaryTree parent){
        if(parent == null){
            return;
        }
        System.out.print(parent.data + " ");
        preOrder2(parent.left);
        preOrder2(parent.right);
    }

    // 中序遍历二叉树
    public void inOrder2(BinaryTree parent) {
        if (parent == null)
            return;
        inOrder2(parent.left);
        System.out.print(parent.data + " ");
        inOrder2(parent.right);
    }

    // 后序遍历二叉树
    public void postOrder2(BinaryTree parent) {
        if (parent == null)
            return;
        postOrder2(parent.left);
        postOrder2(parent.right);
        System.out.print(parent.data + " ");

    }


    //查找
    public boolean searchkey2(BinaryTree root, int key) {
        boolean bl = false;
        if (root == null) {
            bl = false;
            return bl;
        } else if (root.data == key) {
            bl = true;
            return bl;
        } else if (key >= root.data) {
            return searchkey2(root.right, key);
        }
        return searchkey2(root.left, key);
    }
//测试类:

    public static void main(String args[]) {
        int data[] = {12, 11, 34, 45, 67, 89, 56, 43, 22, 98};
        BinaryTree root = new BinaryTree(data[0]);
        for (int i = 1; i < data.length; i++) {
            root.insertTree2(root, data[i]);
        }
        System.out.println();
        root.postOrder2(root);
        System.out.println();
        root.preOrder2(root);
        System.out.println();
        root.inOrder2(root);
        System.out.println(data[data.length - 1]);
        int key = Integer.parseInt("34");
        if (root.searchkey2(root, key)) {
            System.out.println("找到了:" + key);
        } else {
            System.out.println("没有找到：" + key);
        }
    }
}