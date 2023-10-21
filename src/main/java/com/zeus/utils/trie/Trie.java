package com.zeus.utils.trie;

import com.zeus.utils.log.Logger;
import javafx.util.Pair;

import java.util.*;

public class Trie {
    private static final int ALPHABET_SIZE = 26;
    private Node root = null;
    public Trie() {
        root = new Node();
    }
    public boolean insert(String word) {
        if (root == null) {
            Logger.warn("Root is null.");
        }
        if (word == null) return false;
        word = word.toLowerCase();
        int index = 0;
        Node iterator = root;
        for (int character = 0; character < word.length(); character++) {
            index = word.charAt(character);
            if (iterator.getChildren().get(index) == null) {
                iterator.add(index);
            }
            iterator = iterator.next(index);
        }
        if (iterator.isEndOfWord()) {
            Logger.warn("Insert action cancelled. Word existed!");
            return false;
        }
        iterator.setEndOfWord(true);
        return true;
    }

    public boolean search(String word) {
        if (isEmpty()) {
            Logger.warn("Trie is empty.");
            return false;
        }
        word = word.toLowerCase();
        int index = 0;
        Node iterator = root;
        for (int character = 0; character < word.length(); character++) {
            index = word.charAt(character);
            if (iterator.getChildren().get(index) == null) {
                return false;
            }
            iterator = iterator.next(index);
        }
        if (iterator.isEndOfWord()) {
            Logger.info("Found word: "+ word);
        }
        return iterator.isEndOfWord();
    }

    public boolean delete(String word) {
        if (isEmpty()) {
            Logger.warn("Trie is empty.");
            return false;
        }
        if (delete(root, word, 0)) {
            Logger.info("Delete successfully.");
            return true;
        }
        return false;
    }

    private boolean delete(Node node, String word, int depth) {
        if (node == null) {
            Logger.warn("Delete word cancelled. Word does not exist!");
            return false;
        }
        if (word.length()-1 == depth) {
            if (!node.isEndOfWord()) {
                Logger.warn("Delete word cancelled. Word does not exist!");
                return false;
            }
            return node.getChildren().isEmpty();
        }
        if (delete(node.getChildren().get((int)word.charAt(depth)), word, depth+1)) {
            node.getChildren().remove((int)word.charAt(depth));
        }
        return false;
    }

    public void printAll() {
        printAll(root, "");
    }

    public List<String> getAllWords() {
        List<String> result = new ArrayList<>();
        getAllWords(root, "", result);
        return result;
    }

    private void getAllWords(Node node, String word, List<String> list) {
        if (node.isEndOfWord()) {
            System.out.println(word);
        }
        else {
            for (Integer character : node.getChildren().keySet()) {
                StringBuilder temp = new StringBuilder();
                temp.append(word);
                temp.append((char) ((int) character));
                getAllWords(node.getChildren().get(character), temp.toString(), list);
            }
        }
    }


    private void printAll(Node node, String word) {
        if (node.isEndOfWord()) {
            System.out.println(word);
        }
        else {
            for (Integer character : node.getChildren().keySet()) {
                StringBuilder temp = new StringBuilder();
                temp.append(word);
                temp.append((char) ((int) character));
                printAll(node.getChildren().get(character), temp.toString());
            }
        }
    }

    public void reset() {
        root.children.clear();
    }

    public boolean isEmpty() {
        return root.getChildren().isEmpty() || root == null;
    }
    static class Node {
        private Map<Integer, Node> children = new HashMap<>();
        private boolean endOfWord = false;

        public Node() {}
        public Node next(int character) {
            return children.get(character);
        }

        public void add(int character) {
            children.put(character, new Node());
        }
        public Node(boolean endOfWord) {
            this.endOfWord = endOfWord;
        }

        public Map<Integer, Node> getChildren() {
            return children;
        }

        public void setChildren(Map<Integer, Node> children) {
            this.children = children;
        }

        public boolean isEndOfWord() {
            return endOfWord;
        }

        public void setEndOfWord(boolean endOfWord) {
            this.endOfWord = endOfWord;
        }
    }
}
