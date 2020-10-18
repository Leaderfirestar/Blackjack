import java.util.ArrayList;
import java.util.Scanner;
//String response = JOptionPane.showInputDialog(null, "What would ...");

class Card{
    String name;
    String suit;
    String value;
    public Card(String cardName, String cardSuit, String cardValue){
        this.name = cardName;
        this.suit = cardSuit;
        this.value = cardValue;
    }
}

class Player{
    String name;
    ArrayList<Card> hand1 = new ArrayList<Card>();
    ArrayList<Card> hand2 = new ArrayList<Card>();
    Scanner scan;
    public Player(String name){
        this.name = name;
    }
    public Player(String name, Scanner scan){
        this.name = name;
        this.scan = scan;
    }
    public int getScore(int hand){
        int score = 0;
        boolean soft = false;
        if(hand == 1){
            for(int i = 0; i < this.hand1.size(); i++){
                if(this.hand1.get(i).value == "Ace"){
                    if(score < 11){
                        soft = true;
                        score += 11;
                    }else{
                        score += 1;
                    }
                }else{
                    score += Integer.valueOf(this.hand1.get(i).value);
                }
                if(score > 21 && soft){
                    score -= 10;
                    soft = false;
                }
            }
        }else{
            for(int i = 0; i < this.hand2.size(); i++){
                if(this.hand2.get(i).value == "Ace"){
                    if(score < 11){
                        soft = true;
                        score += 11;
                    }else{
                        score += 1;
                    }
                }else{
                    score += Integer.valueOf(this.hand2.get(i).value);
                }
                if(score > 21 && soft){
                    score -= 10;
                    soft = false;
                }
            }
        }
        return score;
    }
    public void briefing(int hand, Deck deck, Player computer){
        if(this.name != "computer"){
            System.out.println("You have the following cards on the table: ");
            for(int i = 0; i < this.hand1.size(); i++){
                System.out.println(this.hand1.get(i).name + " of " + this.hand1.get(i).suit);
            }
            if(this.getScore(1) > 21){
                System.out.println("This hand is a bust!");
            }else if(this.getScore(1) == 21){
                System.out.println("This hand is a blackjack!");
            }else{
                System.out.println("You currently have a score of " + this.getScore(1));
            }
            if(this.hand2.size() != 0){
                System.out.println("Your second hand has the following cards: ");
                for(int j = 0; j < this.hand2.size(); j++){
                    System.out.println(this.hand2.get(j).name + " of " + this.hand2.get(j).suit);
                }
                if(this.getScore(2) > 21){
                    System.out.println("This hand is a bust");
                }else if(this.getScore(2) == 21){
                    System.out.println("This hand is a blackjack!");
                }else{
                    System.out.println("You currently have a score of " + this.getScore(2));
                }
            }
            System.out.println(String.format("The dealer has a %s of %s showing", computer.hand1.get(1).name, computer.hand1.get(1).suit));
            System.out.println(String.format("What would you like to do with your hand(%d)", hand));
        }
        String command;
        if(this.name != "computer"){
            command = this.scan.nextLine();
        }else{
            if(this.getScore(1) < 17){
                command = "hit";
            }else{
                command = "stand";
            }
        }
        if(hand == 0){
            hand = 1;
        }
        this.playBall(command.toLowerCase(), hand, deck, computer);
    }
    public boolean canSplit(int hand){
        if(hand == 1){
            if(this.hand1.size() != 2){
                return false;
            }else if(this.hand1.get(0).name == this.hand1.get(1).name){
                return true;
            }else{
                return false;
            }
        }else{
            if(this.hand2.size() != 2){
                return false;
            }else if(this.hand2.get(0).name == this.hand2.get(1).name){
                return true;
            }else{
                return false;
            }
        }
    }
    public void playBall(String command, int hand, Deck deck, Player computer){
        switch(command){
            case "split":
                if(this.canSplit(hand)){
                    this.hand2.add(this.hand1.get(1));
                    this.hand1.remove(1);
                    int card1 = (int)Math.floor(Math.random() * deck.deck.size());
                    System.out.println(String.format("The %s of %s was added to hand 1", deck.deck.get(card1).name, deck.deck.get(card1).suit));
                    this.hand1.add(deck.deck.get(card1));
                    deck.deck.remove(card1);
                    int card2 = (int)Math.floor(Math.random() * deck.deck.size());
                    System.out.println(String.format("The %s of %s was added to hand 2", deck.deck.get(card2).name, deck.deck.get(card2).suit));
                    this.hand2.add(deck.deck.get(card2));
                    deck.deck.remove(card2);
                    this.briefing(1, deck, computer);
                }else{
                    if(this.hand2.size() != 0){
                        System.out.println("You can only split your hand once");
                        System.out.println("What would you like to do?");
                        String newCommand = this.scan.nextLine();
                        this.playBall(newCommand.toLowerCase(), hand, deck, computer);
                    }else{
                        System.out.println("You cannot split this hand");
                        System.out.println("What would you like to do?");
                        String newCommand = this.scan.nextLine();
                        this.playBall(newCommand.toLowerCase(), hand, deck, computer);
                    }
                }
            case "hit":
                int random = (int)Math.floor(Math.random() * deck.deck.size());
                if(hand == 1){
                    if(this.name != "computer"){
                        System.out.println(String.format("The %s of %s was added to your hand", deck.deck.get(random).name, deck.deck.get(random).suit));
                    }else{
                        System.out.println(String.format("The %s of %s was added to the dealer's hand", deck.deck.get(random).name, deck.deck.get(random).suit));
                    }
                    this.hand1.add(deck.deck.get(random));
                    deck.deck.remove(random);
                    if(this.name == "computer"){
                        System.out.println(String.format("The dealer now has a score of %d", this.getScore(1)));
                    }
                }else{
                    System.out.println(String.format("The %s of %s was added to your hand", deck.deck.get(random).name, deck.deck.get(random).suit));
                    this.hand2.add(deck.deck.get(random));
                    deck.deck.remove(random);
                }
                if(this.getScore(hand) > 21){
                    if(this.name != "computer"){
                        if(this.hand2.size() != 0){
                            hand = 2;
                            this.briefing(hand, deck, computer);
                        }else{
                            deck.returnDeck();
                        }
                    }
                }else{
                    this.briefing(hand, deck, computer);
                }
            case "stand":
                if(hand == 1 && this.hand2.size() != 0){
                    hand += 1;
                    this.briefing(hand, deck, computer);
                }
        }
    }
}

class Deck{
    ArrayList<Card> deck = new ArrayList<Card>();
    public Deck(int number){
        String[] names = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "10", "10", "10", "Ace"};
        for(int d = 0; d < number; d++){
            for(int i = 0; i < names.length; i++){
                for(int j = 0; j < suits.length; j++){
                    Card card = new Card(names[i], suits[j], values[i]);
                    this.deck.add(card);
                }
            }
        }
    }
    public void dealCards(Player me, Player computer){
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 2; j ++){
                int random = (int)Math.floor(Math.random() * this.deck.size());
                if(j == 0){
                    me.hand1.add(this.deck.get(random));
                    this.deck.remove(random);
                }else{
                    computer.hand1.add(this.deck.get(random));
                    this.deck.remove(random);
                }
            }
        }
    }
    public Deck returnDeck(){
        return this;
    }
}

class myClass{
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        Player me = new Player("Eric", scan);
        Player computer = new Player("computer");
        Deck deck = new Deck(3);
        deck.dealCards(me, computer);
        me.briefing(1, deck, computer);
        scan.close();
        System.out.println(String.format("The dealer reveals a %s of %s to compliments his %s of %s", computer.hand1.get(0).name, computer.hand1.get(0).suit, computer.hand1.get(1).name, computer.hand1.get(1).suit));
        while(computer.getScore(1) < 17){
            computer.playBall("hit", 1, deck, computer);
        }
        System.out.println("Your hand has a score of " + me.getScore(1));
        if(me.getScore(1) > 21){
            if(me.hand2.size() != 0){
                if(me.getScore(2) == 21){
                    if(computer.getScore(1) == 21){
                        System.out.println("Your first hand busted, Your second hand is a blackjack, but so was the dealer's. Push!");
                    }else if(computer.getScore(1) < 21){
                        System.out.println("Your first hand busted, Your second hand is a blackjack, and the dealer lost to it!");
                    }else{
                        System.out.println("Your first had busted, but your second pushed with the dealer's blackjack!");
                    }
                }else if(me.getScore(2) > 21){
                    if(computer.getScore(1) > 21){
                        System.out.println("All hands busted");
                    }else{
                        System.out.println("Your hands busted. Dealer wins");
                    }
                }
            }else if(computer.getScore(1) == 21){
                System.out.println("You busted, dealer has a blackjack!");
            }else{
                System.out.println("You busted, dealer wins!");
            }
        }else if(me.getScore(1) == 21){
            if(me.hand2.size() != 0){
                if(me.getScore(2) > 21){
                    if(computer.getScore(1) > 21){
                        System.out.println("Your first hand is a blackjack, but your second hand and the dealer busted!");
                    }else if(computer.getScore(1) == 21){
                        System.out.println("Your blackjack pushed with the dealer, but your second hand busted!");
                    }else{
                        System.out.println("Your blackjack won, but your second hand busted!");
                    }
                }else if(me.getScore(2) == 21){
                    if(computer.getScore(1) > 21){
                        System.out.println("You have 2 blackjacks, and the dealer busted!");
                    }else if(computer.getScore(1) == 21){
                        System.out.println("Your blackjacks pushed with the dealer!");
                    }else{
                        System.out.println("You have 2 blackjacks! both hands win!");
                    }
                }
            }else if(computer.getScore(1) == 21){
                System.out.println("Your blackjack pushed with the dealer");
            }else{
                System.out.println("Your blackjack won!");
            }
        }else{
            if(me.hand2.size() != 0){
                System.out.println("Your other hand has a score of " + me.getScore(2));
            }
            System.out.println("The computer has a score of " + computer.getScore(1));
            if(me.getScore(1) > computer.getScore(1)){
                if(me.hand2.size() != 0){
                    if(me.getScore(2) > computer.getScore(1)){
                        System.out.println("Both of your hands win!");
                    }else if(me.getScore(2) < computer.getScore(1)){
                        System.out.println("Your first hand wins, but your second hand loses!");
                    }else{
                        System.out.println("Your first hand wins, but your second hand pushes!");
                    }
                }else{
                    System.out.println("You won!");
                }
            }else if(me.getScore(1) < computer.getScore(1)){
                if(me.hand2.size() != 0){
                    if(me.getScore(2) > computer.getScore(1)){
                        System.out.println("Your first hand loses, but your second hand wins!");
                    }else if(me.getScore(2) < computer.getScore(1)){
                        if(computer.getScore(1) > 21){
                            System.out.println("The dealer busted. You win!");
                        }else{
                            System.out.println("Both hands lose!");
                        }
                    }else{
                        System.out.println("Your first hand loses, and your second hand pushes");
                    }
                }else if(computer.getScore(1) > 21){
                    System.out.println("The dealer busted. You win!");
                }else if(computer.getScore(1) == 21){
                    System.out.println("You lost to the dealer's blackjack!");
                }else{
                    System.out.println("You lost to the dealer!");
                }
            }else{
                if(me.hand2.size() != 0){
                    if(me.getScore(2) > computer.getScore(1)){
                        System.out.println("Your first hand pushes, but your second hand wins!");
                    }else if(me.getScore(2) < computer.getScore(1)){
                        System.out.println("Your first and pushes, and your second hand loses!");
                    }else{
                        System.out.println("Both hands push!");
                    }
                }else{
                    System.out.println("You pushed with the dealer");
                }
            }
        }
    }
}