/**
 * Class Name: Connect4.java
 * By: Smit B. and Mithu S.
 * Date: January 19, 2018
 * School : Lester B. Pearson Collegiate Institute
 * Course : ICS3U1 - 12
 * 
 * Welcome to Connect 4! This game is playable with only 2 players, no more, no less. Before each player can make a move, the program
 * will ask the user to place a piece or pop a piece. If the user chose place a piece, the user must enter a piece between column 1 to 7.
 * However, the user cannot enter a piece if the column is full.
 * If the user chose to pop a piece, the user can only pop from column 1 to 7. Not to mention, each user only has three pops. If the user ran 
 * out of pops, the user will have to place a piece. If the user used a pop in a blank space, the user will still lose a pop.
 * 
 * How a user wins :
 * To win Connect Four you must be the first player to get four of your piece in a row either horizontally, vertically or diagonally. Person who wins most of the 
 * 3 games is declared the winner
 * 
 * How the users tie:
 * For the users to tie, at least one of the following must occur. Either the entire board must be filled with the users pieces, or, both users win at the same time
 * Also, if both players won equally amount of times. then an overall tie occurred
 * 
 * 
 */
import java.util.Scanner;

public class Connect4 {
	// This is the 2D array that will be used to first off, to build/make the board, and the coordinates (6 and 7) 
	// will be changed later on for win conditions, dropping pieces, popping pieces, etc.
	static String design[][] = new String[6][7];
	// This is the Scanner that will be used for any method as it is static
	static Scanner input = new Scanner(System.in);
	// This variable keeps track how many times p1 has won so far 
	static int p1Wins = 0;
	// This variable keeps track how many times p2 has won so far 
	static int p2Wins = 0;
	// This variable keeps track how many times a tie has occurred so far
	static int ties = 0;
	//These will keep track on the amount of pieces placed in each column
	static int column1 = 0,column2 = 0,column3 = 0,column4 = 0,column5 = 0,column6 = 0,column7 = 0; 
	// This boolean value tells us if player 1 has won or not, primarily set to false as no one has won yet
	static boolean p1WinningCondition = false;
	// This boolean value tells us if player 2 has won or not, primarily set to false as no one has won yet
	static boolean p2WinningCondition = false;

	/**
	 * This is the main method in which the other methods will be called. Here, whether which player won,
	 * tied, or lost, is all gathered here, and other methods such as p1/p2ValidTurn's are called
	 * to validate certain parts of the code.
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Connect4\n");
		// This for makes sure that 3 matches will be played in total
		for (int i = 1; i <= 3; i++) {
			System.out.println("Round " + i + "         P1W: " + p1Wins + "  P2W: "+p2Wins+"  T: "+ties);
			// Plays the starter board, aka the blank board, that never changes
			starterBoard();
			// These are the number of pops for P1 and P2, three in total, if all wasted, then no more pops for that player
			int numofPopsP1 = 3;
			int numofPopsP2 = 3;
			while (true) { 
				int choice;
				do {
					do {
						// Gives P1 options to choose from
						System.out.println("\nP1: Do you want to... (" + numofPopsP1 + " pops remaining) ");
						System.out.println("(1)Place a piece");
						System.out.println("(2)Pop a Piece");
						choice = input.nextInt();
						// If the choice number entered by player one is out of range, error method is output
						if (choice >= 3 || choice <= 0) {
							System.out.println("Invalid choice ");
							// Otherwise, if proper/valid option chosen, then it will break out of this current do-while that will go until valid entered
						} else {
							break;
						}
					} while ((choice <= 3 || choice >= 0));
					// If P1 chose to drop a piece, then it will exit the do-while and go the options later on
					if (choice == 1) {
						break;
					}
					// If P1 chose to pop, but they have none left, an error message will be output, saying they ran out of pops and need to go again
					if (numofPopsP1 <= 0) {
						System.out.println("You ran out of pops");
					}
				} while (numofPopsP1 < 1);
				// If P1 chose to pop a piece, then ...
				if (choice == 2) {
					//First, their total number of pops will drop one
					numofPopsP1--;
					// Then, these methods are called, p1ValidTurn checks if the place they entered is valid, and popPieceDesign does the "popping"
					popPieceDesign(p1ValidTurn());
					// If everything is valid and design is well, then it will output what the board currently looks like
					ModifierBoard();

				} else {
					// But, if they instead chose to drop a piece ...	
					boolean redoP1Turn = true;
					do {
						// Then, these methods are called, p1ValidTurn checks if the place they entered is valid, and p1PieceDropDesign "drops the piece",
						// But it only drops if the return value is true, so if the current spot is empty and the column number is valid
						boolean moveP1 = p1PieceDropDesign(p1ValidTurn());
						if (moveP1) {
							// If valid in all ways, then this method will output what the current board looks like, with the piece on it
							ModifierBoard();
							redoP1Turn = false;
							// Otherwise, if the current column is not empty, then error message displayed
						} else {
							System.out.println("Move not made");
						}
					} while (redoP1Turn == true);
				}

				// These variables check to see if P1 or P2 have won in various different instances. If one or more are true, then the player wins
				p1WinningCondition = (p1HorizontalWin() == true || p1VerticalWin() == true || p1DiagonalBackward() == true	|| p1DiagonalForward() == true);  
				p2WinningCondition = (p2HorizontalWin() == true || p2VerticalWin() == true || p2DiagonalBackward() == true	|| p2DiagonalForward() == true);  
				// However, if they both won, then the game counts it as a tie 
				if (p1WinningCondition && p2WinningCondition) {
					System.out.println("\nRound ended as a draw\n");
					// Number of ties goes up one because a since both players won, then a tie has just occurred
					ties++;
				}
				// But, if only P1 won, then ...
				else if (p1WinningCondition) {
					System.out.println("\nPlayer 1 wins!\n");
					// Number of times P1 won goes up because they have just won
					p1Wins++;
					break;
					// But, if only P2 won, then ...
				}else if (p2WinningCondition) {
					System.out.println("\nPlayer 2 Wins!\n");
					// Number of times P1 won goes up because they have just won
					p2Wins ++;
					break;
					//checks if the entire board is full or not. If so, then...
				} else if (fullBoardCheck() == true) {
					System.out.println("\n Round ended as a draw\n");
					// Number of ties goes up one because since the board is full, a tie has just occurred
					ties++;
					break;
				}

				do {
					do {
						// Gives P2 options to choose from...
						System.out.println("\nP2: Do you want to... (" + numofPopsP2 + " pops remaining) ");
						System.out.println("(1)Place a piece");
						System.out.println("(2)Pop a Piece");
						choice = input.nextInt();
						// Choice can only be 1 or 2, anything else would be invalid
						if (choice >= 3 || choice <= 0) {
							System.out.println("Invalid choice ");
						} else {
							break;
						}
					} while ((choice <= 3 || choice >= 0));
					// Once the user chose 1, the do while breaks, and the drop will begin
					if (choice == 1) {
						break;
					}
					//User will have to choose another choice if ran out of pops, and must choose to drop a piece
					if (numofPopsP2 <= 0) {
						System.out.println("You ran out of pops");
					}
				} while (numofPopsP2 < 1);
				//If P2 chose to pop a piece, then ...
				if (choice == 2) {
					numofPopsP2--;
					// Then, these methods are called, p1ValidTurn checks if the place they entered is valid, and popPieceDesign does the "popping"
					popPieceDesign(p2ValidTurn());
					// If everything is valid and design is well, then it will output what the board currently looks like
					ModifierBoard();
					// But, if they instead chose to drop a piece ...	
				} else {

					boolean redoP2Turn = true;
					do {
						// Then, these methods are called, p2ValidTurn checks if the place they entered is valid, and p2PieceDropDesign "drops the piece",
						// But it only drops if the return value is true, so it is empty and the column number is valid

						boolean P2move = p2PieceDropDesign(p2ValidTurn());
						if (P2move) {
							// If valid in all ways, then this method will output what the current board looks like
							ModifierBoard();
							redoP2Turn = false;
						} else {
							System.out.println("Move not made");

						}
					} while (redoP2Turn == true);



				}

				// These variables check to see if P1 or P2 have won in various different instances. If one or more are true, then the player wins
				p1WinningCondition = (p1HorizontalWin() == true || p1VerticalWin() == true || p1DiagonalBackward() == true	|| p1DiagonalForward()== true);  
				p2WinningCondition = (p2HorizontalWin() == true || p2VerticalWin() == true || p2DiagonalBackward() == true	|| p2DiagonalForward()== true);  
				// However, if they both won, then the game counts it as a tie 
				if (p1WinningCondition && p2WinningCondition) {
					System.out.println("\nRound ended as a draw\n");
					ties++;
				}
				//If player one wins, game is ended
				else if (p1WinningCondition) {
					System.out.println("\nPlayer 1 wins!\n");
					// Number of times P1 won goes up once
					p1Wins++;
					break;
					//If player two wins, game is ended
				}else if (p2WinningCondition) {
					System.out.println("\nPlayer 2 Wins!\n");	
					// Number of times P2 won goes up once
					p2Wins ++;
					break;
					//If the board is full, the game count it as a tie
				} else if (fullBoardCheck() == true) {
					System.out.println("\n Round ended as a draw\n");
					// Number of ties goes up once
					ties++;
					break;
				}


			}
		}
		//Outputs statistics when all 3 matches are over
		statistics ();
	}
	/**
	 * This method is not used much, a maximum of 3 times. Basically, this method displays an empty board at the beginning of each round
	 * and not the modifier which changes over time
	 */
	public static void starterBoard() {
		for (int i = 5; i > -1; i--) {
			for (int j = 0; j < 7; j++) {
				design[i][j] = " O ";
				System.out.print(" O ");
			}
			System.out.println();

		}
		System.out.println("********************");
		System.out.println(" 1  2  3  4  5  6  7");
	}
	/**
	 * This method validates that whatever value P1 enters, that it is within range, and if it's continuously out of range, then
	 * it will keep asking P1 to input until valid entered
	 */
	public static int p1ValidTurn() {

		int p1Column = -1;

		do {

			System.out.println("\nP1: Enter Column  ");
			p1Column = input.nextInt();
			// The column can only be within this certain range
			if (p1Column <= 0 || p1Column >= 8) {
				System.out.print("Invalid Column...");
			}

		} while (p1Column <= 0 || p1Column >= 8);
		// These if's count how many pieces are entered in each column by both players
		if (p1Column == 1) {
			column1++;	
		}
		if (p1Column == 2) {
			column2++;	
		}
		if (p1Column == 3) {
			column3++;	
		}
		if (p1Column == 4) {
			column4++;	
		}
		if (p1Column == 5) {
			column5++;	
		}
		if (p1Column == 6) {
			column6++;	
		}
		if (p1Column == 7) {
			column7++;	
		}		
		return p1Column;

	}
	/**
	 * This method is the design for dropping P1's pieces on the board. If there is an empty space, then the player piece will be substituted instead 
	 */
	public static boolean p1PieceDropDesign(int p1Piece) {

		for (int i = design.length - 1; i > -1; i--) {
			if (design[i][p1Piece - 1].equals(" O ")) {
				design[i][p1Piece - 1] = " 1 ";
				return true;
			}

		}
		return false;

	}
	/**
	 * This method modifies every single time a piece is dropped or popped. It will always output after some sort of variation
	 */
	public static void ModifierBoard() {
		//Nested for loops help create a rectangle
		for (int i = 0; i < design.length; i++) {
			for (int j = 0; j < design[0].length; j++) {

				System.out.print(design[i][j]);
			}
			System.out.println();

		}
		System.out.println("********************");
		System.out.println(" 1  2  3  4  5  6  7");
	}
	/**
	 * This method validates that whatever value P2 enters, that it is within range, and if is it continuously out of range, then
	 * it will keep asking P2 to input until valid option entered
	 */
	public static int p2ValidTurn() {

		int p2Column = -1;

		do {

			System.out.println("\nP2: Enter Column  ");
			p2Column = input.nextInt();
			// The column can only be within this certain range
			if (p2Column <= 0 || p2Column >= 8) {
				System.out.print("Invalid Column...");
			}

		} while (p2Column <= 0 || p2Column >= 8);
		// These if's count how many pieces are entered in each column by any player
		if (p2Column == 1) {
			column1++;	
		}
		if (p2Column == 2) {
			column2++;	
		}
		if (p2Column == 3) {
			column3++;	
		}
		if (p2Column == 4) {
			column4++;	
		}
		if (p2Column == 5) {
			column5++;	
		}
		if (p2Column == 6) {
			column6++;	
		}
		if (p2Column == 7) {
			column7++;	
		}		
		return p2Column;

	}
	/**
	 * This method is the design for dropping P2's pieces on the board. If there is an empty space, then the piece will be substituted instead of the empty space
	 */
	public static boolean p2PieceDropDesign(int p2Piece) {

		for (int i = design.length - 1; i > -1; i--) {

			if (design[i][p2Piece - 1].equals(" O ")) {
				design[i][p2Piece - 1] = " 2 ";
				return true;
			}

		}
		return false;

	}
	/**
	 * This method is the design for popping. Basically, this method makes sure that when a user
	 * wants to pop a piece, that all the previous pieces get bumped down one, and that the piece on the top is now a blank space
	 */
	public static void popPieceDesign(int pop) {

		for (int i = design.length - 1; i > -1; i--) {
			//If statement checks whether there is a piece located at the spot where user wants
			// to pop a piece.
			if (design[i][pop - 1].equals(" 1 ") || design[i][pop - 1].equals(" 2 ")) {
				//This statement re assigns the values by taking values above it
				design[i][pop - 1] = design[i - 1][pop - 1];
			} else {
				design[i][pop - 1] = " O ";
			}
			//Help avoid an error at the top, if the popping is occurring in a filled column
			if (i == 1) {
				design[0][pop - 1] = " O ";
			}
		}
	}
	/**
	 * This is a method that checks if the entire top row is full or not. If the top row is filled,
	 * then a boolean value is returned, if true, than a tie occurred, else, continues as empty are present, and a tie has not yet occurred 
	 */
	public static boolean fullBoardCheck() {
		//Fill board counter variable is used to keep track the amount of columns full
		int fillBoardCounter = 0;
		//for loop from 0 to 6, to check if top is filled up
		for (int i = 0; i < design[0].length; i++) {
			//If a column is full, the board counter is increased by 1.
			if (design[0][i].equals(" 1 ") || design[0][i].equals(" 2 ")) {
				fillBoardCounter++;
			}
		}
		//if the fill board counter is 7, if statement returns true, otherwise statement
		//returns false
		if (fillBoardCounter == design[0].length) {
			return true;
		} else {
			return false;
		}

	}
	/**
	 * This method is called to verify that P1 hasn't won horizontally yet. A boolean value 
	 * is given back, true if P1 won horizontally, else, false if no win is seen yet horizontally 
	 */
	public static boolean p1HorizontalWin() {
		//Variable p1HorizontalCounter is used to keep track the amount of pieces i a row 
		// in a horizontal
		int p1HorizontalCounter = 0;
		//Reason why row is the first loop because the program can check pieces horizontally
		for (int i = 0; i < design.length; i++) {
			for (int j = 0; j < design[0].length; j++) {
				//checks whether if the current piece consists of a Player one piece
				//if true, vertical counter is increased by one, else vertical counter is assigned
				//back to zero
				if (design[i][j].equals(" 1 ")) {
					p1HorizontalCounter++;
				} else {
					p1HorizontalCounter = 0;
				}
				//if there is a four in a row or more, returns true
				if (p1HorizontalCounter >= 4) {
					return true;
				}
			}
		}
		//If no horizontal is found, returns false
		return false;
	}
	/**
	 * This method is called to verify that P1 hasn't won vertically yet. A boolean value 
	 * is given back, true if P1 won vertically, else, false if no win is seen yet vertically
	 */
	public static boolean p1VerticalWin() {
		//Variable p1VerticalCounter is used to keep track the amount of pieces i a row 
		// in a vertical
		int p1VerticalCounter = 0;
		//Reason why column is the first loop because the program can check pieces vertically
		for (int j = 0; j < design[0].length; j++) {
			for (int i = 0; i < design.length; i++) {
				//checks whether if the current piece consists of a Player one piece
				//if true, vertical counter is increased by one, else vertical counter is assigned
				//back to zero
				if (design[i][j].equals(" 1 ")) {
					p1VerticalCounter++;
				} else {
					p1VerticalCounter = 0;
				}
				//if there is a four in a row or more, returns true
				if (p1VerticalCounter >= 4) {
					return true;
				}
			}

		}
		//If no vertical is found, returns false
		return false;
	}
	/**
	 * This method is called to verify that P1 hasn't won diagonally backwards yet. A boolean value 
	 * is given back, true if P1 won diagonally backwards, else, false if no win 
	 * is seen yet diagonally backwards 
	 */
	public static boolean p1DiagonalBackward() {
		for (int i = 0; i <= 2; i++) {
			for (int j = 3 ; j <= 6; j++) {
				//This if statement will check if P1's pieces are all diagonally backwards the same. If so, returned true, else, false
				if (design[i][j] == " 1 "&&design[i+1][j-1] == " 1 "&&design[i+2][j-2] == " 1 "&&design[i+3][j-3] == " 1 ") {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * This method is called to verify that P1 hasn't won diagonally forward yet. A boolean value 
	 * is given back, true if P1 won diagonally forward, else, false if no win 
	 * is seen yet diagonally forward
	 */
	public static boolean p1DiagonalForward() {
		for (int i = 0; i <= 2; i++) {
			for (int j = design.length - 1 ; j >= 3; j--) {
				//This if statement will check if P1's pieces are all diagonally forwards the same. If so, returned true, else, false
				if (design[i][j] == " 1 "&&design[i+1][j-1] == " 1 "&&design[i+2][j-2] == " 1 "&&design[i+3][j-3] == " 1 ") {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This method is called to verify that P2 hasn't won horizontally yet. A boolean value 
	 * is given back, true if P2 won horizontally, else, false if no win is seen yet horizontally 
	 */
	public static boolean p2HorizontalWin() {
		//Variable p2HorizontalCounter is used to keep track the amount of pieces i a row 
		// in a horizontal
		int p2HorizontalCounter = 0;
		//Reason why row is the first loop because the program can check pieces horizontally
		for (int i = 0; i < design.length; i++) {
			for (int j = 0; j < design[0].length; j++) {
				//checks whether if the current piece consists of a Player two piece
				//if true, vertical counter is increased by one, else vertical counter is assigned
				//back to zero
				if (design[i][j].equals(" 2 ")) {
					p2HorizontalCounter++;
				} else {
					p2HorizontalCounter = 0;
				}
				//if there is a four in a row or more, returns true
				if (p2HorizontalCounter >= 4) {
					return true;
				}
			}
		}
		//If no horizontal is found, returns false
		return false;
	}
	/**
	 * This method is called to verify that P2 hasn't won vertically yet. A boolean value 
	 * is given back, true if P2 won vertically, else, false if no win is seen yet vertically
	 */
	public static boolean p2VerticalWin() {
		//Variable p2VerticalCounter is used to keep track the amount of pieces i a row 
		// in a vertical
		int p2VerticalCounter = 0;
		//Reason why column is the first loop because the program can check pieces vertically
		for (int j = 0; j < design[0].length; j++) {
			for (int i = 0; i < design.length; i++) {
				//checks whether if the current piece consists of a Player two piece
				//if true, vertical counter is increased by one, else vertical counter is assigned
				//back to zero
				if (design[i][j].equals(" 2 ")) {
					p2VerticalCounter++;
				} else {
					p2VerticalCounter = 0;
				}
				//if there is a four in a row or more, returns true
				if (p2VerticalCounter >= 4) {
					return true;
				}
			}

		}
		//If no vertical is found, returns false
		return false;
	}
	/**
	 * This method is called to verify that P2 hasn't won diagonally backwards yet. A boolean value 
	 * is given back, true if P2 won diagonally backwards, else, false if no win 
	 * is seen yet diagonally backwards 
	 */
	public static boolean p2DiagonalBackward() {
		for (int i = 0; i <= 2; i++) {
			for (int j = 3 ; j <= 6; j++) {
				//This if statement will check if P2's pieces are all diagonally backwards the same. If so, returned true, else, false
				if (design[i][j] == " 2 "&&design[i+1][j-1] == " 2 "&&design[i+2][j-2] == " 2 "&&design[i+3][j-3] == " 2 ") {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * This method is called to verify that P2 hasn't won diagonally forward yet. A boolean value 
	 * is given back, true if P2 won diagonally forward, else, false if no win 
	 * is seen yet diagonally forward
	 */
	public static boolean p2DiagonalForward() {
		for (int i = 0; i <= 2; i++) {
			for (int j = design.length - 1 ; j >= 3; j--) {
				//This if statement will check if P2's pieces are all diagonally forwards the same. If so, returned true, else, false
				if (design[i][j] == " 2 "&&design[i+1][j-1] == " 2 "&&design[i+2][j-2] == " 2 "&&design[i+3][j-3] == " 2 ") {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * This method output's the statistics for the entire game session. This includes how many ties,
	 * How many pieces were in each column, and others. 
	 */
	public static void statistics () {
		//These two variables are used to find maximum and minimum for the columns
		//Initial value of Max is -999 because it is impossible for a column to have a negative value 
		//Initial value of Minimum is 999 because it is impossible for a column to have a total of 999 pieces
		//popped and placed in a column
		int max = -999;
		int min = 999;
		System.out.println("\n*************************************************************");

		//Block of if-statements print out the overall status of the game session.
		if	(p1Wins == p2Wins) {
			System.out.println ("                      Status: Tie Game");	
		}
		else if (p1Wins > p2Wins) {
			System.out.println ("                      Status: P1 Wins");
		}
		else if (p2Wins > p1Wins) {
			System.out.println ("                      Status: P2 Wins");
		}

		//Block of code prints the amount of piece place and popped in each column and
		//prints Number of P1 Wins, Number of P2 Wins, Number Ties
		System.out.println("\nNumber of P1 Wins: "+ p1Wins);
		System.out.println("Number of P2 Wins: "+ p2Wins);
		System.out.println("Number of Ties: "+ ties);
		System.out.println ("\nNumber of pieces and pops occur in Column 1 out of all the matches: " + column1);
		System.out.println ("Number of pieces and pops occur in Column 2 out of all the matches: " + column2);
		System.out.println ("Number of pieces and pops occur in Column 3 out of all the matches:  " + column3);
		System.out.println ("Number of pieces and pops occur in Column 4 out of all the matches:  " + column4);
		System.out.println ("Number of pieces and pops occur in Column 5 out of all the matches:  "  + column5 );
		System.out.println ("Number of pieces and pops occur in Column 6 out of all the matches:  " + column6);
		System.out.println ("Number of pieces and pops occur in Column 7 out of all the matches:  " + column7);

		//Block of if statements will determine the largest 
		//pieces place and pop in a column all together
		if (max < column1) {
			max = column1;
		}
		if (max < column2) {
			max = column2;
		}
		if (max < column3) {
			max = column3;
		}

		if (max < column4) {
			max = column4;
		}

		if (max < column5) {
			max = column5;
		}

		if (max < column6) {
			max = column6;
		}

		if (max < column7) {
			max = column7;
		}

		//Block of if statements will determine the least 
		//pieces place and pop in a column all together
		if (min > column1) {
			min = column1;
		}
		if (min > column2) {
			min = column2;
		}
		if (min > column3) {
			min= column3;
		}
		if (min > column4) {
			min = column4;
		}
		if (min > column5) {
			min = column5;
		}
		if (min > column6) {
			min = column6;
		}
		if (min > column7) {
			min = column7;
		}

		// Outputs the statistics of the entire game session
		System.out.println("\nMost pieces place and pop in a column all together is "+ max);
		System.out.println("Least pieces place and pop in a column all together is "+ min);
		System.out.println("\n\nThank You for Playing Connect 4!");


	}
}