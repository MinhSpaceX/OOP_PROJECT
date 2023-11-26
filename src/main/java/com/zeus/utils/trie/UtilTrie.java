package com.zeus.utils.trie;

import com.zeus.utils.DictionaryUtil.Word;
import com.zeus.utils.log.Logger;
import org.apache.commons.logging.Log;

import java.util.*;

/**
 * Class to increase search efficiency.
 */
class UtilTrie {
    protected Node root;

    /**
     * Constructor, initialize root node.
     */
    UtilTrie() {
        root = new Node();
    }

    /**
     * Insert all word targets in the list into the trie.
     *
     * @param words List of {@link Word}.
     */
    UtilTrie(List<Word> words) {
        root = new Node();
        words.forEach(w -> this.insert(w.getWordTarget()));
    }

    /**
     * Insert a string into trie.
     *
     * @param word String to insert,
     * @return true if success<p>false if failed</p>
     */
    public boolean insert(String word) {
        if (root == null) {
            Logger.warn("Root is null.");
            return false;
        }
        if (word == null) return false;
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
            return false;
        }
        iterator.setEndOfWord(true);
        return true;
    }

    /**
     * Search a String in the trie.
     *
     * @param word String to search for.
     * @return true if the string is in trie.
     * <p>false if not in trie.</p>
     */
    public boolean search(String word) {
        if (isEmpty()) {
            Logger.warn("UtilTrie is empty.");
            return false;
        }
        word = word.toLowerCase();
        int index;
        Node iterator = root;
        for (int character = 0; character < word.length(); character++) {
            index = word.charAt(character);
            if (iterator.getChildren().get(index) == null) {
                return false;
            }
            iterator = iterator.next(index);
        }
        if (iterator.isEndOfWord()) {
            Logger.info("Found word: " + word);
        } else {
            Logger.info("Did not found the word: " + word);
        }
        return iterator.isEndOfWord();
    }

    /**
     * Reset trie, clear all data but root node.
     */
    public void reset() {
        root.children.clear();
    }

    /**
     * Check to see if tree is empty.
     *
     * @return true if root node has children
     * <p>false if root node doesn't</p>
     */
    public boolean isEmpty() {
        return root.getChildren().isEmpty();
    }

    /**
     * Auto fill method to find all the string start with given string.
     *
     * @param word          String to look for.
     * @param numberOfWords Number of string return.
     * @param wordLength    Minimum length of the string.
     * @return List of string found.
     */
    public List<String> autoFill(String word, int numberOfWords, int wordLength) {
        List<String> result = new ArrayList<>();
        Node babyGroot = goTo(word);
        if (babyGroot == null) return result;
        if (babyGroot.isEndOfWord()) result.add(word.toLowerCase());
        getWords(babyGroot, word.toLowerCase(), result, numberOfWords, wordLength);
        return result;
    }

    /**
     * Delete string from trie, using recursive method
     * (if return true then delete the node).
     *
     * @param node  Current node.
     * @param word  String deleting.
     * @param depth Depth of the node.
     * @return true if can delete
     * <p>false if not</p>
     */
    protected boolean delete(Node node, String word, int depth) {
        if (node == null) {
            Logger.warn("Delete word cancelled. Word does not exist! (Null)");
            return false;
        }
        if (word.length() == depth) {
            if (!node.isEndOfWord()) {
                Logger.warn("Delete word cancelled. Word does not exist!");
                return false;
            }
            return node.getChildren().isEmpty();
        }
        Set<Integer> keys = node.getChildren().keySet();
        for (Integer i : keys) {
            //System.out.println("Contain " + node.getChildren().get(i));
            if (i.compareTo((int) word.charAt(depth)) == 0) {
                if (delete(node.getChildren().get(i), word, depth + 1)) {
                    node.getChildren().remove(i);
                }
                break;
            }
        }
        return false;
    }

    /**
     * Using recursive method to print contents to console.
     *
     * @param node Node processing.
     * @param word The string accumulated through recur.
     */
    protected void print(Node node, String word) {
        //System.out.println("Print: " + node);
        if (node.isEndOfWord()) {
            System.out.println(node);
            System.out.println(word);
        }
        Set<Integer> keys = node.getChildren().keySet();
        for (Integer character : keys) {
            String temp = word + (char) ((int) character);
            print(node.getChildren().get(character), temp);
        }
    }

    /**
     * Go to the node with given string,
     *
     * @param word String to go to.
     * @return The {@link Node}.
     */
    private Node goTo(String word) {
        Node node = root;
        if (isEmpty()) {
            Logger.warn("UtilTrie is empty.");
            return null;
        }
        word = word.toLowerCase();
        int index;
        for (int character = 0; character < word.length(); character++) {
            index = word.charAt(character);
            if (node != null) {
                node = node.next(index);
            }
        }
        if (node == null) {
            Logger.warn("No word to auto fill.");
        }
        return node;
    }

    /**
     * Get words method, used to make auto fill suggestions, add string
     * to list if the node is endOfWord..
     *
     * @param node          Node processing.
     * @param word          The string accumulated.
     * @param list          List to store the string.
     * @param numberOfWords Number of string to get.
     * @param wordLength    Minimum string length.
     */
    private void getWords(Node node, String word, List<String> list, int numberOfWords, int wordLength) {
        if (list.size() >= numberOfWords && numberOfWords != -1) return;
        if (node.isEndOfWord() && word.length() >= wordLength) {
            list.add(word);
        }
        Set<Integer> keys = node.getChildren().keySet();
        for (Integer character : keys) {
            String temp = word + (char) ((int) character);
            getWords(node.getChildren().get(character), temp, list, numberOfWords, wordLength);
        }
    }

    /**
     * Node class to store data.
     */
    protected static class Node {
        private Map<Integer, Node> children = new HashMap<>();
        private boolean endOfWord = false;

        /**
         * Constructor.
         */
        public Node() {
        }

        /**
         * Constructor.
         *
         * @param endOfWord Mark the end of word.
         */
        public Node(boolean endOfWord) {
            this.endOfWord = endOfWord;
        }

        /**
         * Get the children node based on given character.
         *
         * @param character character based on ASCII.
         * @return Children node.
         */
        public Node next(int character) {
            return children.get(character);
        }

        /**
         * Add children node.
         *
         * @param character Character to act as key in children map.
         */
        public void add(int character) {
            children.put(character, new Node());
        }

        /**
         * Get children node map.
         *
         * @return Map of children with character in ASCII as key.
         */
        public Map<Integer, Node> getChildren() {
            return children;
        }

        /**
         * Set children map.
         *
         * @param children Children map.
         */
        public void setChildren(Map<Integer, Node> children) {
            this.children = children;
        }

        /**
         * Check to see if the node is end of a string.
         *
         * @return true if it is end of string (or a word)
         * <p>false if not</p>
         */
        public boolean isEndOfWord() {
            return endOfWord;
        }

        /**
         * Set end of word.
         *
         * @param endOfWord Set true if is end of word and false otherwise.
         */
        public void setEndOfWord(boolean endOfWord) {
            this.endOfWord = endOfWord;
        }
    }
}
