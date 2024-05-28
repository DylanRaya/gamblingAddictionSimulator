import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class GamblingAddiction {
    private static double startingBalance = 0;
    private static double leaveProbability;
    private static int numDecks; // Variable to store the number of decks based on difficulty
    private static double payoutRatio; // Variable to store the payout ratio based on difficulty
    private static final double WINNING_BALANCE = 1_000_000;
    private static final double LOSING_BALANCE = 0;

    public static void main(String[] args) {
        playSound("Jams/AceOfSpades.wav");
        Scanner scanner = new Scanner(System.in);

        // Ask for the difficulty level
        int difficultyChoice = askForDifficulty(scanner);

        // Set the game parameters based on difficulty
        setGameParameters(difficultyChoice);

        // Display game options
        displayGameOptions(scanner);
    }

    public static int askForDifficulty(Scanner scanner) {
        System.out.println("Welcome to the Gambling Addiction Simulator, please select the difficulty level:");
        System.out.println("1. Blackjack Beginner");
        System.out.println("2. Slots Scoundrel");
        System.out.println("3. Casino Rat");
        System.out.println("4. Monte Carlo Crackhead");
        System.out.print("Enter your choice (1-4): ");
        return scanner.nextInt();
    }

    public static void setGameParameters(int difficultyChoice) {
        String story;
        String difficulty;

        switch (difficultyChoice) {
            case 1:
                numDecks = 8;
                payoutRatio = 2;
                difficulty = "Blackjack Beginner";
                story = "Here you are in Vegas, a 21 year old generic looking white man here with some friends and about $10,000 of daddy's money in your wallet to play with. You haven't dabbled in the world of gambling before this but what better time to start than now. After all, Papa said he wouldn't allow you any of the family fortune when he dies unless you can prove your success by making a million dollars before his passing and his cancer isn't getting any better...";
                startingBalance = 100000;
                leaveProbability = 0.8; // Higher probability to leave
                break;
            case 2:
                numDecks = 6;
                payoutRatio = 1.5;
                difficulty = "Slots Scoundrel";
                story = "Here you are in The Venetian, Fergus to your left and Edith to your right. How long have you sat in these chairs pressing the buttons, Hours? Days? You can't remember the last time you used the bathroom but sitting in this chair watching the slots spin is the most thrill you've had since the day you landed at Normandy. Now you're 102 years old with shrapnel in your prefrontal cortex and watching the slots spin the only thing that brings you joy. If you had enough money to buy a house with a slot machine in it you'd move away from this land of sin, but with the housing market so expensive the only way to do that would be to make a million dollars and you only have one way to do that...";
                startingBalance = 25000;
                leaveProbability = 0.6;
                break;
            case 3:
                numDecks = 4;
                payoutRatio = 1.2;
                difficulty = "Casino Rat";
                story = "Vegas. Macao. The Morongo Casino and Resort. You've seen just about everything this world has to offer and made and lost thousands. You could totally definitely have stopped anytime you wanted in the last 50 years but you've got a teeny tiny issue now: the IRS. You haven't exactly been paying the tax money you should've been on your winnings or on the money you made from working and after 50 years of that, you've accrued several hundred thousands of dollars in debt and you still need to finish paying off your house and car. All said and done, you'd need about a million dollars to get out of this mess and you can't see yourself making the money any way other than the one way you've been trying all these years...";
                startingBalance = 5000;
                leaveProbability = 0.4;
                break;
            case 4:
                numDecks = 1;
                payoutRatio = 1.0;
                difficulty = "Monte Carlo Crackhead";
                story = "Yeah, you're just kinda fucked. I mean, I'm really not sure you can get out of this one and I don't know how you're even still alive. You're in so much debt with the the Société des Bains de Mer (the owners of the Casino de Monte Carlo) and Crown Resorts Limited (the owners of most casinos in Australia) that you've had to sell one of your eyes, a kidney, a lung, part of your liver, and both testicles. On top of that, Australia's The Honoured Society crime family, Serbian mafia, and Albanian mafia are all after you because you ran out on their loans. This is pretty much it, you've got no possible chance to pay them back even if you were the richest man in the world, the only thing you can do now is run and run far. Tahiti is where you're headed, to become a rancher and mango farmer, but to run and make a new life will take money, and years of experience at the tables and knowledge of the highest payouts means gambling is your only chance...";
                startingBalance = 1000;
                leaveProbability = 0.2; // Lowest probability to leave
                break;
            default:
                System.out.println("Invalid choice. Setting to medium difficulty.");
                numDecks = 4;
                payoutRatio = 1.2;
                difficulty = "Medium";
                story = "You've chosen a moderate level of difficulty. The casino floor stretches out before you, beckoning with promises of excitement and fortune. What game will you choose?";
                startingBalance = 7500;
                leaveProbability = 0.5; // Default medium probability to leave
        }
        System.out.println("You've chosen the difficulty level: " + difficulty);
        System.out.println(story);
    }

    public static void displayGameOptions(Scanner scanner) {
        System.out.println("What game would you like to play?");
        System.out.println("1. Blackjack");
        System.out.println("2. Poker");
        System.out.println("3. Horse Race Betting");
        System.out.println("4. Roulette");
        System.out.println("5. Slots");
        System.out.print("Enter your choice (1-5): ");

        int gameChoice = scanner.nextInt();
        switch (gameChoice) {
            case 1:
                playBlackjack(scanner);
                break;
            case 2:
                //playPoker();
                break;
            case 3:
                //playHorseRaceBetting();
                break;
            case 4:
                //playRoulette();
                break;
            case 5:
                //playSlots();
                break;
            default:
                System.out.println("Invalid choice. Exiting the game.");
        }
    }

    public static void playSound(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }

    public static void playBlackjack(Scanner scanner) {
        final int maxBet = 5000;

        System.out.println("Playing Blackjack...");
        System.out.println("Your starting balance: $" + startingBalance);

        while (true) { // Loop to play multiple hands
            while (true) { // Loop to play multiple hands until the user decides to leave the table
                System.out.print("Enter your bet amount (max bet: $" + maxBet + "): $");
                int bet = scanner.nextInt();

                if (bet <= 0 || bet > maxBet || bet > startingBalance) {
                    System.out.println("Invalid bet amount. Setting bet to $10.");
                    bet = 10;
                }

                System.out.println("You've placed a bet of $" + bet);

                startingBalance -= bet;

                Deck deck = new Deck(numDecks); // Using the number of decks based on difficulty
                deck.shuffle();

                Hand playerHand = new Hand();
                Hand dealerHand = new Hand();
                playerHand.addCard(deck.drawCard());
                playerHand.addCard(deck.drawCard());
                dealerHand.addCard(deck.drawCard());
                dealerHand.addCard(deck.drawCard());

                System.out.println("Your hand: " + playerHand.toString());
                System.out.println("Dealer's hand: " + dealerHand.getCard(0).toString() + " [Hidden]");

                // Player's turn
                while (true) {
                    System.out.println("Your current hand total: " + playerHand.getHandValue());

                    System.out.println("Do you want to (1) Hit, (2) Stay, or (3) Double Down?");
                    int choice = scanner.nextInt();
                    if (choice == 1) {
                        playerHand.addCard(deck.drawCard());
                        System.out.println("You draw: " + playerHand.getLastCard().toString());
                        if (playerHand.getHandValue() > 21) {
                            System.out.println("Bust! You lose!");
                            // Dealer wins if player busts
                            break;
                        }
                    } else if (choice == 2) {
                        break;
                    } else if (choice == 3) {
                        int doubleDownBet = bet * 2;
                        if (doubleDownBet > startingBalance) {
                            System.out.println("You don't have enough balance to double down. Choose another option.");
                            continue;
                        }
                        bet = doubleDownBet;
                        playerHand.addCard(deck.drawCard());
                        System.out.println("You double down and draw: " + playerHand.getLastCard().toString());
                        System.out.println("Your hand: " + playerHand.toString());
                        break; // No more actions allowed after doubling down
                    } else {
                        System.out.println("Invalid choice. Please choose (1) Hit, (2) Stay, or (3) Double Down.");
                    }
                }

                // Dealer's turn
                if (playerHand.getHandValue() <= 21) {
                    System.out.println("Dealer's hand: " + dealerHand.toString());

                    while (dealerHand.getHandValue() < 17) {
                        dealerHand.addCard(deck.drawCard());
                        System.out.println("Dealer draws: " + dealerHand.getLastCard().toString());
                    }

                    System.out.println("Dealer's final hand: " + dealerHand.toString());

                    int playerValue = playerHand.getHandValue();
                    int dealerValue = dealerHand.getHandValue();

                    if (dealerValue > 21 || playerValue > dealerValue) {
                        System.out.println("You win!");
                        startingBalance += bet * payoutRatio; // Use the payoutRatio variable here
                    } else if (playerValue == dealerValue) {
                        System.out.println("Push! It's a tie!");
                        startingBalance += bet; // Return the bet to the player
                    } else {
                        System.out.println("Dealer wins!");
                    }
                }

                System.out.println("Your new balance: $" + startingBalance);

                if (checkLossCondition(scanner)) {
                    break; // Break out of the current game loop and return to game selection
                }

                if (checkWinCondition(scanner)) {
                    break; // Break out of the current game loop and return to game selection
                }

                System.out.println("Do you want to play another hand? (yes/no)");
                String playAgain = scanner.next();
                if (playAgain.equalsIgnoreCase("no")) {
                    if (attemptToLeaveTable(scanner)) {
                        System.out.println("You successfully left the table.");
                        displayGameOptions(scanner);
                        break; // Break out of the current game loop and return to game selection
                    } else {
                        System.out.println("You decided to stay at the table.");
                        // Continue with the current game loop
                    }
                }
            }
        }
    }

    public static boolean attemptToLeaveTable(Scanner scanner) {
        System.out.println("Are you sure you want to leave the table? (yes/no)");
        String leaveDecision = scanner.next();
        return leaveDecision.equalsIgnoreCase("yes");
    }

    public static boolean checkWinCondition(Scanner scanner) {
        if (startingBalance >= WINNING_BALANCE) {
            System.out.println("Congratulations! You have reached a balance of $1,000,000 and won the game!");

            System.out.println("Would you like to walk away with your money? (yes/no)");
            String decision = scanner.next();
            if (decision.equalsIgnoreCase("yes")) {
                System.out.println("You decide to walk away with your winnings. You feel the weight of the money in your pocket as you step out of the casino, knowing that you've secured your future.");
                System.out.println("You are now back at the beginning. Type 'end' to exit the game.");
                String endGame = scanner.next();
                return true;
            } else {
                System.out.println("You decide to continue playing.");
                return false;
            }
        }
        return false;
    }

    public static boolean checkLossCondition(Scanner scanner) {
        if (startingBalance <= LOSING_BALANCE) {
            System.out.println("You have lost all your money and reached a balance of $0. Game over.");
            System.out.println("Would you like to walk away with your remaining balance? (yes/no)");
            String decision = scanner.next();
            if (decision.equalsIgnoreCase("yes")) {
                System.out.println("You decide to walk away with your remaining balance. You leave the casino with a heavy heart, wondering what went wrong.");
                System.out.println("You are now back at the beginning. Type 'end' to exit the game.");
                String endGame = scanner.next();
                return true;
            } else {
                System.out.println("You decide to continue playing.");
                return false;
            }
        }
        return false;
    }

    static class Deck {
        private List<Card> cards;


        public Deck(int numDecks) {
            this.cards = new ArrayList<>();
            for (int i = 0; i < numDecks; i++) {
                for (Suit suit : Suit.values()) {
                    for (Rank rank : Rank.values()) {
                        this.cards.add(new Card(rank, suit));
                    }
                }
            }
        }

        public void shuffle() {
            Collections.shuffle(this.cards);
        }

        public Card drawCard() {
            if (!this.cards.isEmpty()) {
                return this.cards.remove(0);
            } else {
                System.out.println("The deck is empty.");
                return null;
            }
        }
    }

    static class Hand {
        private List<Card> cards;

        public Hand() {
            this.cards = new ArrayList<>();
        }

        public void addCard(Card card) {
            this.cards.add(card);
        }

        public Card getCard(int index) {
            return this.cards.get(index);
        }

        public int getHandValue() {
            int value = 0;
            int aceCount = 0;
            for (Card card : this.cards) {
                if (card.getRank() == Rank.ACE) {
                    aceCount++;
                }
                value += card.getValue();
            }
            while (value > 21 && aceCount > 0) {
                value -= 10;
                aceCount--;
            }
            return value;
        }

        public Card getLastCard() {
            return this.cards.get(this.cards.size() - 1);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Card card : this.cards) {
                sb.append(card.toString()).append(" ");
            }
            return sb.toString();
        }
    }

    enum Suit {
        SPADES, HEARTS, DIAMONDS, CLUBS
    }

    enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10), ACE(11);

        private final int value;

        Rank(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    static class Card {
        private final Rank rank;
        private final Suit suit;

        public Card(Rank rank, Suit suit) {
            this.rank = rank;
            this.suit = suit;
        }

        public Rank getRank() {
            return rank;
        }

        public Suit getSuit() {
            return suit;
        }

        public int getValue() {
            return rank.getValue();
        }

        @Override
        public String toString() {
            return rank + " of " + suit;
        }
    }
}
