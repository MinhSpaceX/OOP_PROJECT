package GameManager;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import DictionaryManager.Dictionary;
import DictionaryManager.DictionaryID;
import DictionaryManager.Word;

public class GameManagement {
	
	private int rightAnswer; // the index in the dictionary of right answer
	private int[] wrongAnswer = new int[3]; // the index in the dictionary of wrong answer
	private DictionaryID id; 
	private boolean gameState = true;
	private char correct;
	private Scanner sc;
	private ArrayList<Word> dictionary;
	
	public GameManagement(Dictionary d, Scanner sc) {
		this.dictionary = d.getDictionary();
		this.sc = sc;
	}
	
	public void StartGame() {
		randomAnswer();
		generateQuestion();
		while(gameState) {
			System.out.println("Enter your answer: [UPPER CASE]");
			String input = sc.nextLine();
			char char_input = input.charAt(0);
			if(char_input == correct) {
				System.out.println("Correct!");
				gameState = false;
			}
			else {
				System.out.println("Wrong!");
			}
		}
	}
		
	/**
	 * Generate game
	 * @param d
	 * @param id
	 */
	public void generateQuestion() {
		int[] answers = new int[4];
		answers = randomAnswer();
		char answer_index = 'A';
		System.out.println("What is the meaning of: " + dictionary.get(rightAnswer).GetWordTarget());
		for(int i = 0; i < 4; i++) {
			System.out.println(answer_index + "." + dictionary.get(answers[i]).GetWordExplain());
			if(answers[i] == rightAnswer) {
				correct = answer_index;
			}
			answer_index++;
		}
	}
	
	/**
	 * 
	 * @return an array that contain answers in random order
	 */
	private int[] randomAnswer() {
		
		
		int index = 0; //use for array wrongAnswer to debug duplicating answer
		int index_answers = 1; //use as the index for answers array
		int[] answers = new int[4];
		Random ran = new Random();
		Random ran2 = new Random();
		
		//get random 4 answers to create question
		rightAnswer = ran.nextInt((dictionary.size() - 1) + 1);
		answers[0] = rightAnswer;
		for(int i = 1; i < 4; i++) {
			wrongAnswer[index] = ran.nextInt((dictionary.size() - 1) + 1);
			if(wrongAnswer[index] != rightAnswer) {
				//debug duplicating answer
				//your code here:
				
				answers[i] = wrongAnswer[index];
				index++;
			}
		}
		
		//shuffle the array to create random question format
		int length = 4;
		for(int i = length - 1; i > 0; i--) {
			int randomIndex = ran2.nextInt(i + 1);
			
			int temp = answers[i];
			answers[i] = answers[randomIndex];
			answers[randomIndex] = temp; 
		}
		
		return answers;
	}
}