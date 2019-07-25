package com.example.demo01.leetcode;

public class Simple3 {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public static void main(String[] args) {
    }

    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if ((p == null && q != null) || (q == null && p != null)) {
            return false;
        }
        if (p.val != q.val) {
            return false;
        }
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }



    private boolean fuzhu(TreeNode root, int left, int right) {
        if (root == null) {
            return Math.abs(left - right) <= 1;
        }
        if(root.left!=null){
            left++;
        }
        if(root.right!=null){
            right++;
        }
        return fuzhu(root.left, left, right) &&fuzhu(root.right, left, right);
    }
}
