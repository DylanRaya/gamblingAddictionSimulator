import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Random;
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
    private static int numDecks;
    private static double payoutRatio;
    private static final double WINNING_BALANCE = 1_000_000;
    private static final double LOSING_BALANCE = 0;
    private static int consecutiveMaxBets = 0;
    private static double addiction = 0;

    public static void main(String[] args) {
        playSound("src/Jams/AceofSpades.wav");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int difficultyChoice = askForDifficulty(scanner);
            setGameParameters(difficultyChoice);
            displayGameOptions(scanner, difficultyChoice);

            if (checkLossCondition(scanner, difficultyChoice)) {
                System.out.println("Do you want to play again? (1 for Yes, 2 for No)");
                int playAgainChoice = scanner.nextInt();
                if (playAgainChoice != 1) {
                    System.out.println("Thank you for playing!");
                    break;
                }
                continue;
            }

            System.out.println("Do you want to play again? (1 for Yes, 2 for No)");
            int playAgainChoice = scanner.nextInt();
            if (playAgainChoice != 1) {
                System.out.println("Thank you for playing!");
                break;
            }
        }
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
                leaveProbability = 0.8;
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
                story = "Yeah, you're just kinda screwed. I mean, I'm really not sure you can get out of this one and I don't know how you're even still alive. You're in so much debt with the the Société des Bains de Mer (the owners of the Casino de Monte Carlo) and Crown Resorts Limited (the owners of most casinos in Australia) that you've had to sell one of your eyes, a kidney, a lung, and part of your liver. On top of that, Australia's The Honoured Society crime family, Serbian mafia, and Albanian mafia are all after you because you ran out on their loans. This is pretty much it, you've got no possible chance to pay them back even if you were the richest man in the world, the only thing you can do now is run and run far. Tahiti is where you're headed, to become a rancher and mango farmer, but to run and make a new life will take money, and years of experience at the tables and knowledge of the highest payouts means gambling is your only chance...";
                startingBalance = 1000;
                leaveProbability = 0.2;
                break;
            default:
                System.out.println("Invalid choice. Setting to medium difficulty.");
                numDecks = 4;
                payoutRatio = 1.2;
                difficulty = "Medium";
                story = "You've chosen a moderate level of difficulty. The casino floor stretches out before you, beckoning with promises of excitement and fortune. What game will you choose?";
                startingBalance = 7500;
                leaveProbability = 0.5;
        }
        System.out.println("You've chosen the difficulty level: " + difficulty);
        System.out.println(story);
    }

    public static void displayGameOptions(Scanner scanner, int difficultyChoice) {
        while (true) {
            System.out.println("What game would you like to play?");
            System.out.println("1. Blackjack");
            System.out.println("2. Slots");
            System.out.println("3. Roulette");
            System.out.println("4. Info");
            System.out.print("Enter your choice (1-4): ");

            int gameChoice = scanner.nextInt();
            switch (gameChoice) {
                case 1:
                    playBlackjack(scanner, difficultyChoice);
                    break;
                case 2:
                    playSlots(scanner, difficultyChoice);
                    break;
                case 3:
                    playRoulette(scanner, difficultyChoice);
                    break;
                case 4:
                    displayGameInfo();
                    break;
                default:
                    System.out.println("Invalid choice. Exiting the game.");
                    return;
            }
        }
    }

    public static void playBlackjack(Scanner scanner, int difficultyChoice) {
        final int maxBet = 5000;

        System.out.println("Playing Blackjack...");
        System.out.println("Your starting balance: $" + startingBalance);

        while (true) {
            while (true) {
                System.out.print("Enter your bet amount (max bet: $" + maxBet + "): $");
                int bet = scanner.nextInt();

                if (bet <= 0 || bet > maxBet || bet > startingBalance) {
                    System.out.println("Invalid bet amount. Setting bet to $10.");
                    bet = 10;
                }

                System.out.println("You've placed a bet of $" + bet);

                startingBalance -= bet;
                addiction += 0.1;

                Deck deck = new Deck(numDecks);
                deck.shuffle();

                Hand playerHand = new Hand();
                Hand dealerHand = new Hand();
                playerHand.addCard(deck.drawCard());
                playerHand.addCard(deck.drawCard());
                dealerHand.addCard(deck.drawCard());
                dealerHand.addCard(deck.drawCard());

                System.out.println("Your hand: " + playerHand.toString());
                System.out.println("Dealer's hand: " + dealerHand.getCard(0).toString() + " [Hidden]");

                while (true) {
                    System.out.println("Your current hand total: " + playerHand.getHandValue());

                    System.out.println("Do you want to (1) Hit, (2) Stay, or (3) Double Down?");
                    int choice = scanner.nextInt();
                    if (choice == 1) {
                        playerHand.addCard(deck.drawCard());
                        System.out.println("You draw: " + playerHand.getLastCard().toString());
                        if (playerHand.getHandValue() > 21) {
                            System.out.println("Bust! You lose!");
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
                        break;
                    } else {
                        System.out.println("Invalid choice. Please choose (1) Hit, (2) Stay, or (3) Double Down.");
                    }
                }

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
                        startingBalance += bet * payoutRatio;
                    } else if (playerValue == dealerValue) {
                        System.out.println("Push! It's a tie!");
                        startingBalance += bet;
                    } else {
                        System.out.println("Dealer wins!");
                    }
                }

                System.out.println("Your new balance: $" + startingBalance);

                if (checkLossCondition(scanner, difficultyChoice)) {
                    break;
                }

                if (checkWinCondition(scanner, difficultyChoice)) {
                    break;
                }

                System.out.println("Do you want to play another hand? (yes/no)");
                String playAgain = scanner.next();
                if (playAgain.equalsIgnoreCase("no")) {
                    if (attemptToLeaveTable(scanner)) {
                        System.out.println("You successfully left the table.");
                        displayGameOptions(scanner, difficultyChoice);
                        break;
                    } else {
                        System.out.println("You decided to stay at the table.");
                    }
                }
            }
        }
    }

    public static void playSlots(Scanner scanner, int difficultyChoice) {
        final String[] symbols = {"Cherry", "Lemon", "Orange", "Plum", "Bell", "Bar"};
        final int[] betOptions = {100, 1000, 2500};

        System.out.println("Welcome to Slots!");
        System.out.println("Bet options: $100, $1000, $2500");
        System.out.println("Payouts:");
        System.out.println("Cherry - 10x");
        System.out.println("Lemon  - 20x");
        System.out.println("Orange - 30x");
        System.out.println("Plum   - 40x");
        System.out.println("Bell   - 50x");
        System.out.println("Bar    - 100x");
        System.out.println("Your starting balance: $" + startingBalance);

        while (true) {
            int betIndex;
            while (true) {
                System.out.println("Select your bet amount:");
                System.out.println("1. $100");
                System.out.println("2. $1000");
                System.out.println("3. $2500");
                betIndex = scanner.nextInt();
                if (betIndex >= 1 && betIndex <= 3) {
                    break;
                } else {
                    System.out.println("Invalid option. Please select 1, 2, or 3.");
                }
            }
            int bet = betOptions[betIndex - 1];

            if (startingBalance < bet) {
                System.out.println("You don't have enough balance to bet $" + bet + ". Please choose another option.");
                continue;
            }

            startingBalance -= bet;
            addiction += 0.1;

            String[] result = new String[3];
            for (int i = 0; i < 3; i++) {
                result[i] = symbols[new Random().nextInt(symbols.length)];
            }

            System.out.println("Spinning...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            System.out.println("Result: " + String.join(" | ", result));

            int payoutMultiplier = 0;
            for (String symbol : symbols) {
                int count = 0;
                for (String slot : result) {
                    if (symbol.equals(slot)) {
                        count++;
                    }
                }
                if (count >= 2) {
                    payoutMultiplier += count == 3 ? 3 : 2;
                }
            }

            int payout = bet * payoutMultiplier;
            startingBalance += payout;


            System.out.println("Payout: $" + payout);
            System.out.println("Your new balance: $" + startingBalance);

            if (checkLossCondition(scanner, difficultyChoice)) {
                break;
            }

            if (checkWinCondition(scanner, difficultyChoice)) {
                break;
            }
        }
    }

    public static void playRoulette(Scanner scanner, int difficultyChoice) {
        final int minBet = 100;
        final int maxBet = 5000;

        System.out.println("Welcome to Roulette!");
        System.out.println("Minimum bet: $" + minBet);
        System.out.println("Maximum bet: $" + maxBet);
        System.out.println("Your starting balance: $" + startingBalance);

        while (true) {
            int betType;
            while (true) {
                System.out.println("Select the type of bet:");
                System.out.println("1. Single Number");
                System.out.println("2. Combination of Numbers");
                System.out.println("3. Odd/Even");
                System.out.println("4. Black/Red");
                System.out.println("5. Leave the table"); // Added option to leave
                betType = scanner.nextInt();
                if (betType >= 1 && betType <= 5) { // Updated condition to accommodate leaving
                    break;
                } else {
                    System.out.println("Invalid option. Please select 1, 2, 3, 4, or 5.");
                }
            }

            if (betType == 5) { // Check if the player chose to leave
                if (attemptToLeaveTable(scanner)) {
                    System.out.println("You successfully left the roulette table.");
                    displayGameOptions(scanner, difficultyChoice);
                    return; // Return to the main game menu
                } else {
                    System.out.println("You decided to stay at the roulette table.");
                    continue; // Continue playing roulette
                }
            }

            int betAmount;
            while (true) {
                System.out.print("Enter your bet amount (between $" + minBet + " and $" + maxBet + "): $");
                betAmount = scanner.nextInt();
                if (betAmount >= minBet && betAmount <= maxBet && betAmount <= startingBalance) {
                    break;
                } else {
                    System.out.println("Invalid bet amount. Please enter an amount between $" + minBet + " and $" + maxBet + " and make sure you have enough balance.");
                }
            }

            startingBalance -= betAmount;
            addiction += 0.1;

            switch (betType) {
                case 1:
                    playSingleNumberBet(scanner, betAmount);
                    break;
                case 2:
                    playCombinationBet(scanner, betAmount);
                    break;
                case 3:
                    playOddEvenBet(scanner, betAmount);
                    break;
                case 4:
                    playColorBet(scanner, betAmount);
                    break;
            }

            if (checkLossCondition(scanner, difficultyChoice) || checkWinCondition(scanner, difficultyChoice)) {
                break;
            }
        }
    }


    public static void playSingleNumberBet(Scanner scanner, int betAmount) {
        System.out.print("Enter the number you want to bet on (0-36): ");
        int chosenNumber = scanner.nextInt();
        int rouletteResult = spinRouletteWheel();

        System.out.println("Roulette result: " + rouletteResult);

        if (chosenNumber == rouletteResult) {
            int payout = betAmount * 35;
            startingBalance += payout;
            System.out.println("Congratulations! You win $" + payout);
        } else {
            System.out.println("Sorry, you lose!");
        }
    }

    public static void playCombinationBet(Scanner scanner, int betAmount) {
        System.out.print("Enter the numbers you want to bet on (separated by spaces): ");
        scanner.nextLine();
        String[] chosenNumbersStr = scanner.nextLine().split(" ");
        int[] chosenNumbers = new int[chosenNumbersStr.length];
        for (int i = 0; i < chosenNumbersStr.length; i++) {
            chosenNumbers[i] = Integer.parseInt(chosenNumbersStr[i]);
        }
        int rouletteResult = spinRouletteWheel();

        System.out.println("Roulette result: " + rouletteResult);

        for (int number : chosenNumbers) {
            if (number == rouletteResult) {
                int payout = betAmount * 35 / chosenNumbers.length;
                startingBalance += payout;
                System.out.println("Congratulations! You win $" + payout + " on number " + number);
                return;
            }
        }
        System.out.println("Sorry, you lose!");
    }

    public static void playOddEvenBet(Scanner scanner, int betAmount) {
        System.out.print("Do you want to bet on (1) Odd or (2) Even: ");
        int choice = scanner.nextInt();
        int rouletteResult = spinRouletteWheel();

        System.out.println("Roulette result: " + rouletteResult);

        if ((choice == 1 && rouletteResult % 2 != 0) || (choice == 2 && rouletteResult % 2 == 0)) {
            int payout = betAmount * 2;
            startingBalance += payout;
            System.out.println("Congratulations! You win $" + payout);
        } else {
            System.out.println("Sorry, you lose!");
        }
    }

    public static void playColorBet(Scanner scanner, int betAmount) {
        System.out.print("Do you want to bet on (1) Red or (2) Black: ");
        int choice = scanner.nextInt();
        int rouletteResult = spinRouletteWheel();

        System.out.println("Roulette result: " + rouletteResult);

        if ((choice == 1 && isRed(rouletteResult)) || (choice == 2 && !isRed(rouletteResult))) {
            int payout = betAmount * 2;
            startingBalance += payout;
            System.out.println("Congratulations! You win $" + payout);
        } else {
            System.out.println("Sorry, you lose!");
        }
    }

    public static int spinRouletteWheel() {
        return new Random().nextInt(37);
    }

    public static boolean isRed(int number) {
        return (number >= 1 && number <= 10) || (number >= 19 && number <= 28);
    }

    public static boolean checkLossCondition(Scanner scanner, int difficultyChoice) {
        if (startingBalance <= LOSING_BALANCE) {
            switch (difficultyChoice) {
                case 1:
                    System.out.println("You've run out of money! Your father's inheritance is lost, and you've failed to prove yourself.");
                    break;
                case 2:
                    System.out.println("You've hit rock bottom, old-timer. It's time to accept that the slots have taken everything from you.");
                    break;
                case 3:
                    System.out.println("The casino has drained you dry, leaving you with nothing but debt and regrets. There's no way out now.");
                    break;
                case 4:
                    System.out.println("Your luck has run out, and so has your life. The debts you've accumulated have finally caught up with you.");
                    break;
                default:
                    System.out.println("You've run out of money! Game over.");
            }
            return true;
        }
        // Check if addiction surpasses leaveProbability
        else if (addiction >= leaveProbability) {
            System.out.println("Your gambling addiction has consumed you completely. Game over.");
            return true;
        }
        if (attemptToLeaveTable(scanner)) {
            System.out.println("You've become too addicted to leave the table. Your gambling addiction has consumed you.");
            return true;
        }
        return false;
    }


    public static boolean checkWinCondition(Scanner scanner, int difficultyChoice) {
        if (startingBalance >= WINNING_BALANCE) {
            switch (difficultyChoice) {
                case 1:
                    System.out.println("Congratulations! You've reached your goal of $1,000,000! Your father is proud, and you've proven yourself worthy of the family fortune.");
                    break;
                case 2:
                    System.out.println("You've hit the jackpot, old-timer! A million dollars richer, you've proven that even the humblest of slot scoundrels can strike it big.");
                    break;
                case 3:
                    System.out.println("The casino's coffers are yours, and you've risen from the ranks of the downtrodden to claim your prize. A million dollars richer, you walk away, leaving behind your debts and regrets.");
                    break;
                case 4:
                    System.out.println("Unbelievable! Against all odds, you've made it. With a million dollars in your pocket, you can leave your troubles behind and start anew.");
                    break;
                default:
                    System.out.println("Congratulations! You've reached your goal of $1,000,000! You win!");
            }
            displayGameOptions(scanner, difficultyChoice);
            return true;
        }
        return false;
    }

    public static boolean attemptToLeaveTable(Scanner scanner) {
        double leaveProbabilityWithAddiction = leaveProbability - addiction; // Adjusted leave probability with addiction
        System.out.println("Current leave probability: " + leaveProbabilityWithAddiction);
        Random random = new Random();
        return random.nextDouble() > leaveProbabilityWithAddiction;
    }

    public static void displayGameInfo() {
        System.out.println("Game Information:");
        System.out.println("Blackjack:");
        System.out.println("- Rules: Try to get as close to 21 as possible without going over. Beat the dealer's hand to win.");
        System.out.println("- Number of decks: " + numDecks);
        System.out.println("- Payout ratio: " + payoutRatio);
        System.out.println();
        System.out.println("Slots:");
        System.out.println("- Rules: Spin the slots and match symbols to win payouts.");
        System.out.println("- Cost per spin: $100");
        System.out.println("- Payouts:");
        System.out.println("  - Cherry - 10x");
        System.out.println("  - Lemon  - 20x");
        System.out.println("  - Orange - 30x");
        System.out.println("  - Plum   - 40x");
        System.out.println("  - Bell   - 50x");
        System.out.println("  - Bar    - 100x");
        System.out.println();
        System.out.println("Roulette:");
        System.out.println("- Rules: Bet on the outcome of a spinning roulette wheel.");
        System.out.println("- Bet options: Odd/Even");
        System.out.println("- Minimum bet: $100");
        System.out.println("- Maximum bet: $5000");
        System.out.println();
    }

    public static void playSound(String soundFilePath) {
        File soundFile = new File(soundFilePath);
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

class Card {
    private final String suit;
    private final String rank;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    public int getValue() {
        switch (rank) {
            case "Ace":
                return 11;
            case "King":
            case "Queen":
            case "Jack":
            case "10":
                return 10;
            default:
                return Integer.parseInt(rank);
        }
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

class Hand {
    private final List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int getHandValue() {
        int value = 0;
        int numAces = 0;

        for (Card card : cards) {
            value += card.getValue();
            if (card.getValue() == 11) {
                numAces++;
            }
        }

        while (value > 21 && numAces > 0) {
            value -= 10;
            numAces--;
        }

        return value;
    }

    public Card getCard(int index) {
        if (index < 0 || index >= cards.size()) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
        return cards.get(index);
    }

    public Card getLastCard() {
        return cards.get(cards.size() - 1);
    }

    @Override
    public String toString() {
        StringBuilder handString = new StringBuilder();
        for (Card card : cards) {
            handString.append(card.toString()).append(", ");
        }
        return handString.substring(0, handString.length() - 2);
    }
}

class Deck {
    private final List<Card> cards;

    public Deck(int numDecks) {
        cards = new ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

        for (int i = 0; i < numDecks; i++) {
            for (String suit : suits) {
                for (String rank : ranks) {
                    cards.add(new Card(suit, rank));
                }
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        return cards.remove(cards.size() - 1);
    }

}