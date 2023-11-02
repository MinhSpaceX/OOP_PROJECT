package com.zeus.utils.trie;

import com.zeus.DictionaryManager.Word;
import com.zeus.utils.log.Logger;

import java.util.*;


public class Trie {
    private static final int ALPHABET_SIZE = 26;
    private Node root = null;
    public Trie() {
        root = new Node();
    }

    Trie(List<Word> words) {
        root = new Node();
        words.forEach(w -> this.insert(w.getWordTarget()));
    }
    public void insert(String word) {
        if (root == null) {
            Logger.warn("Root is null.");
        }
        if (word == null) return;
        word = word.toLowerCase();
        Node iterator = root;
        for (int character = 0; character < word.length(); character++) {
            int index = word.charAt(character);
            if (iterator.getChildren().get(index) == null) {
                iterator.add(index);
            }
            iterator = iterator.next(index);
        }
        if (iterator.isEndOfWord()) {
            Logger.warn("Insert action cancelled. Word existed!");
            return;
        }
        iterator.setEndOfWord(true);
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
        } else {
            Logger.info("Did not found the word: " + word);
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
    public void print() {
        print(root, "");
    }
    public void reset() {
        root.children.clear();
    }
    public boolean isEmpty() {
        return root.getChildren().isEmpty() || root == null;
    }

    public List<String> autoFill(String word, int numberOfWords, int wordLength) {
        List<String> result = new ArrayList<>();
        Node babyGroot = goTo(word);
        if (babyGroot == null) return result;
        if (babyGroot.isEndOfWord()) result.add(word.toLowerCase());
        getWords(babyGroot, word.toLowerCase(), result, numberOfWords, wordLength);
        return result;
    }
    private Node goTo(String word) {
        Node node = root;
        if (isEmpty()) {
            Logger.warn("Trie is empty.");
            return null;
        }
        word = word.toLowerCase();
        int index = 0;
        for (int character = 0; character < word.length(); character++) {
            index = word.charAt(character);
            if(node != null) {
                node = node.next(index);
            }
        }
        if (node == null) {
            Logger.warn("No word to auto fill.");
        }
        return node;
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

    private void getWords(Node node, String word, List<String> list, int numberOfWords, int wordLength) {
        if (list.size() >= numberOfWords && numberOfWords != -1) return;
        if (node.isEndOfWord() && word.length() >= wordLength) {
            if (word.matches("[a-z]+")) list.add(word);
        }
        Set<Integer> keys = node.getChildren().keySet();
        for (Integer character : keys) {
                String temp = word + (char) ((int) character);
                getWords(node.getChildren().get(character), temp, list, numberOfWords, wordLength);
            }
    }

    private void print(Node node, String word) {
        if (node.isEndOfWord()) {
            System.out.println(word);
        }
        Set<Integer> keys = node.getChildren().keySet();
        for (Integer character : keys) {
            String temp = word + (char) ((int) character);
            print(node.getChildren().get(character), temp);
        }
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
