/**
 * This is a game about decking of cards.
 * According to the rules,try to get a higher score by putting the cards in different place.
 * From Java How to Program Deitel and Deitel, and modified by Zhijiao LIU.
 * @author Deitel and Deitel
 * @author Vindya Wijeratne
 * @author Zhijiao LIU
 * @version Last modified 17/05/2011
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
* Open the game interface, and set all buttons and labels.
* @author Deitel and Deitel
* @author Vindya Wijeratne
* @author Zhijiao LIU
* @version Last modified 17/05/2011
*/
public class DeckOfCards extends JFrame {
	private Card deck[];
	private int currentCard,number,noOfSelected=0;
	private JButton shuffleButton, skipButton, nButton[];	
	private JLabel status;
	private Container c; 
	private int hand1,hand2,hand3;
	String faces[] = { "Ace", "Deuce", "Three", "Four", "Five", "Six"};
	String suits[] = { "Hearts", "Diamonds", "Clubs", "Spades" };
	
	void setNumber(int number) {this.number = number;}
	int getNumber() {return number;}
//  Define Parameters
	
	/**
	* Open the game interface, and set all buttons and labels.
	*/
	public DeckOfCards() {
		super("Card Grid game");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		Toolkit tk = this.getToolkit();
		int w = 560;
		int h = 480;
		Dimension dm = tk.getScreenSize();
		this.setLocation((int)(dm.getWidth()-w)/2,(int)(dm.getHeight()-h)/2);

		deck = new Card[24];
		currentCard = -1;
		setNumber(1);
		for (int i = 0; i < deck.length; i++)
			deck[i] = new Card(faces[i % 6], suits[i / 6]);

		c = getContentPane();
		JPanel p = new JPanel(new FlowLayout());
		c.add(p,BorderLayout.NORTH);
		JPanel q = new JPanel(new GridLayout(3,3));
		c.add(q,BorderLayout.CENTER);

		shuffleButton = new JButton("Shuffle cards");
		shuffleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent shuffle) {
				status.setText("SHUFFLING ...");
				shuffle();
				status.setText("DECK IS SHUFFLED");
				for(int n=0;n<9;n++){
					String j = String.valueOf(n);
					nButton[n].setEnabled(true);
					nButton[n].setText(j);	
				}
				dealCard();			
				shuffleButton.setEnabled(false);
				skipButton.setEnabled(true);
			}
		});
		p.add(shuffleButton);// set the shuffle button

		skipButton = new JButton("Skip (once)");
		skipButton.setEnabled(false);
		skipButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent skip) {
				dealCard();	
				skipButton.setEnabled(false);
			}
		});
		p.add(skipButton);// set the skip button
		
		status = new JLabel("SHUFFLE CARDS TO BEGIN");
		p.add(status);
		
		nButton = new JButton[9];// set the 9 game buttons
		int m=0;
		do{	
			nButton[m] = new JButton();
			nButton[m].setEnabled(false);
			nButton[m].addActionListener(new GridListener());
			q.add(nButton[m]);
			m++;
		}while(m<9);

		setSize(w,h); // set the window size
		setVisible(true); // show the window
	}
	
	/**
	* Define what will be done after click the current button.
	*/
	public class GridListener implements ActionListener{
		public void actionPerformed(ActionEvent grid) {
			JButton m = (JButton)grid.getSource();
			m.setText(deck[currentCard].toString());
			m.setEnabled(false);
			dealCard();
			noOfSelected++;
			if(skipButton.isSelected()){
				noOfSelected-=1;
			}
			if(noOfSelected==9){				
				WindowOfScore(); // call the window of final score	
			}
		}
	}
	
	/**
	* Swap the cards.
	*/
	public void shuffle() {
		currentCard = -1;
        setNumber(1);
		for (int i = 0; i < deck.length; i++) {
			int j = (int) (Math.random() * 24);
			Card temp = deck[i]; // swap
			deck[i] = deck[j]; // the
			deck[j] = temp; // cards
		}
	}

	public Card dealCard() {
		/**
		* Return the information of current card. 
		* @return deck[currentCard] Face and suit of current card.
		*/
		if (currentCard++ < deck.length){
			status.setText(deck[currentCard].toString());
			return deck[currentCard];}
		else {
			return null;
		}
	}
	
	/**
	* Calculate the final score.
	*/	
	public void calculateScore(){
		hand1=0;
		hand2=0;
		hand3=0;
		int position[][] = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8}};
		for(int compareP[]:position){
			for(String compareS:suits){// find same suit in column or row
				if(nButton[compareP[0]].getText().indexOf(compareS)>=0
				&& nButton[compareP[1]].getText().indexOf(compareS)>=0
				&& nButton[compareP[2]].getText().indexOf(compareS)>=0){
					hand2+=1;
					for(int compareAce:compareP){// find Ace to classify the hands
						if(nButton[compareAce].getText().indexOf(faces[0])>=0){
							hand1+=1;
							hand2-=1;
							break;
						}
					}
					break;
				}
			}
			for(String compareF:faces){// find two same faces at least
				if((nButton[compareP[0]].getText().indexOf(compareF)>=0 && nButton[compareP[1]].getText().indexOf(compareF)>=0 && nButton[compareP[2]].getText().indexOf(compareF)<0)||
				   (nButton[compareP[0]].getText().indexOf(compareF)>=0 && nButton[compareP[2]].getText().indexOf(compareF)>=0 && nButton[compareP[1]].getText().indexOf(compareF)<0)||
				   (nButton[compareP[1]].getText().indexOf(compareF)>=0 && nButton[compareP[2]].getText().indexOf(compareF)>=0 && nButton[compareP[0]].getText().indexOf(compareF)<0)){
					hand3+=1;
				}
			}
		}

	}
	

		/**
		* Open the score interface, and set all buttons and labels.
		*/
		public void WindowOfScore() {

			calculateScore();
			JOptionPane.showMessageDialog(null,"Total score: "+(hand1*1000+hand2*500+hand3*100)+" points \n " +
					                           "Category 1: Ace Flush (1000 points): "+hand1+" hands \n " + 
					                           "Cateogry 2: Flush (500 points): "+hand2+" hands \n " + 
					                           "Category 3: Pair (100 points): "+hand3+" hands ");
			noOfSelected=0;
			skipButton.setEnabled(false);
			status.setText("SHUFFLE CARDS TO BEGIN");
			shuffleButton.setEnabled(true);
					
		}
	
	
    /**
     * Main now only creates a new instance of my 
     * program and calls the program's method.
     * @param args This program does not use this parameter.
     */
	public static void main(String args[]) {
		DeckOfCards app = new DeckOfCards();
	}

}
