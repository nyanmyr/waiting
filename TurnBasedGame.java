package turnbasedgame;

import java.util.*;

public class TurnBasedGame {

    public static Random random = new Random();

    public static void main(String[] args) {

        // stores the possible names of the monster
        LinkedList<String> nameList = new LinkedList<>();
        // <editor-fold desc="add the spooky, scary, freaky monster names">
        nameList.add("Lilith ");
        nameList.add("Morrigan");
        nameList.add("Hecate");
        nameList.add("Lamia");
        nameList.add("Jezebeth");
        nameList.add("Agrat");
        nameList.add("Bellatrix");
        nameList.add("Circe");
        nameList.add("Drusilla");
        nameList.add("Euryale");
        nameList.add("Champakali");
        nameList.add("Keres");
        nameList.add("Lilura");
        nameList.add("Raven");
        nameList.add("Persephone");
        nameList.add("Tiamat");
        nameList.add("Succubus");
        nameList.add("Morgana");
        nameList.add("Mara");
        nameList.add("Lamashtu");
        nameList.add("Lusca");
        nameList.add("Makara");
        nameList.add("Niamh");
        nameList.add("Nessie");
        nameList.add("Saturna");
        nameList.add("Graeae");
        nameList.add("Medusa");
        nameList.add("Chimera");
        nameList.add("Aurora");
        nameList.add("Cailleach");
        nameList.add("Akumu");
        nameList.add("Yami");
        nameList.add("Akuhei");
        nameList.add("Scylla");
        nameList.add("Amaru");
        nameList.add("Muspell");
        nameList.add("Banshee");
        nameList.add("Cruella");
        nameList.add("Putana");
        nameList.add("Diti");
        nameList.add("Erebus");
        nameList.add("Loki");
        nameList.add("Baphomet");
        nameList.add("Asmodeus");
        nameList.add("Dracula");
        nameList.add("Lucifer");
        nameList.add("Satan");
        nameList.add("Diablo");
        nameList.add("Ravana");
        nameList.add("Mephistopheles");
        // </editor-fold>

        Random random = new Random();
        int[] namesIndex = new int[2];
        
        // loops until it generates a name that does not have a same last and first name
        while (true) {

            namesIndex[0] = random.nextInt(nameList.size());
            namesIndex[1] = random.nextInt(nameList.size());
            if (namesIndex[0] != namesIndex[1]) {
                break;
            }

        }
        
        // puts the first and last name together
        String monsterName = nameList.remove(namesIndex[0]) + " " + nameList.remove(namesIndex[1]);

        Scanner s = new Scanner(System.in);

        // generates the pronouns of the monster (male or female only)
        String genderPronoun = random.nextInt(2) == 1 ? "his" : "her";
        System.out.printf("You have encountered a monster %s name is %s \n", genderPronoun, monsterName);

        // prompts the user to enter a name for their game character
        System.out.print("Enter your character name: ");
        String playerName = s.nextLine();

        // HP    Name   Max  Min  Passive Ability
        Character player = new Character(100, playerName, 10, 1, "TurnTechnique");
        Character bot = new Character(100, monsterName, 10, 1, "Heal", "UnoReverse");

        int gameTime = 1;

        while (true) {

            if (gameTime % 2 != 0) {

                System.out.println("""
                               
                               %s HP : %s HP
                               %s HP : %s HP
                               
                               Actions :
                               `>> type `attack`
                               `>> type `stun`
                               `>> type `skip`
                               """.formatted(playerName, player.playerHP, monsterName, bot.playerHP));

                System.out.print("Enter Action : ");
                String actionStringInput = s.nextLine().trim().toLowerCase();

                System.out.println("\n----------- Player at Play! ------------");
                inputAction(player, bot, actionStringInput);
                System.out.println("------------------------------------------");

                if (bot.playerHP <= 0) {
                    System.out.println("You Won");
                    break;
                }

            } else {

                String randomBotChoice = switch (random.nextInt(3) + 1) {
                    case 1 ->
                        "attack";
                    case 2 ->
                        "stun";
                    case 3 ->
                        "skip";
                    default ->
                        "ran";
                };
                System.out.println("\n----- Bot at Play! (Random Choice) -----");
                if (bot.passive.containsKey("Heal")) {
                    bot.passive.get("Heal").passiveAbility(bot, player);
                }
                if (player.actionHistoryStack.peek().equals("attack")) {
                    bot.passive.get("UnoReverse").passiveAbility(bot, player);
                }
                inputAction(bot, player, randomBotChoice);
                if (randomBotChoice.equals(randomBotChoice)) {
                    parry(player);
                }
                System.out.println("------------------------------------------");

                if (player.playerHP <= 0) {
                    System.out.println("You Lost");
                    break;
                }
            }
            gameTime++;
        }
    }

    static void inputAction(Character character, Character enemy, String stringInput) {

        character.actionHistoryStack.push(stringInput);

        if (character.stunned != 0) {
            System.out.printf("You are Stunned By %d turns left %n", character.stunned--);
            return;
        }

        if (character.burned != 0) {
            System.out.printf("%s Has been burned to %dHP turns left%n", character.playerName,
                    character.playerHP -= 3,
                    character.burned--);
        }

        switch (stringInput) {
            case "attack" -> {
                character.attack(enemy);
            }
            case "stun" -> {
                character.stun(enemy);
            }
            case "skip" -> {
                System.out.println("Skipped Turn");
            }
            default -> {
                System.out.println("That is not a valid Action!!");
            }
        }
    }

    // ----------------------------------- Parry by Mark Vincent Palencia ----------------------------------------
    // ===========================================================================================================
    static void parry(Character player) {
        //  0 1 2 3
        int chance = random.nextInt(8);
        if (chance != 1) {
            return;
        }

        int chance2 = random.nextInt(5);
        double array[] = {0.10, 0.15, 0.20, 0.25, 0.30};
        double boost = array[chance2];

        System.out.println("*Parry Technique* Player Has Parried the Attack " + (boost * 100) + "% to HP and DMG");

        player.playerHP = (int) (player.playerHP + (player.playerHP * boost));
        player.playerMinDMG = (int) (player.playerMinDMG + (player.playerMinDMG * boost));
        player.playerMaxDMG = (int) (player.playerMaxDMG + (player.playerMaxDMG * boost));
    }// ===========================================================================================================
    // ------------------------------------------------------------------------------------------------------------
}

class Character {

    public static Random random = new Random();

    Stack<Integer> playerHPStack = new Stack<>();
    Stack<Integer> damageInflicted = new Stack<>();
    Stack<String> actionHistoryStack = new Stack<>();

    public String playerName;
    public int playerHP;
    public int playerDMG;
    public int playerMaxDMG;
    public int playerMinDMG;

    //effects
    public int stunned;
    public int burned;

    HashMap<String, Passive> passive;

    public Character(int playerHP, String playerName, int playerMaxDMG, int playerMinDMG, String... passive) {
        this.playerName = playerName;
        this.playerHP = playerHP;
        this.playerMaxDMG = playerMaxDMG;
        this.playerMinDMG = playerMinDMG;
        this.passive = new HashMap<>();
        for (String pas : passive) {
            this.passive.put(pas, Passive.assignPassive(pas));
        }
        playerHPStack.push(playerHP);
    }

    public void attack(Character enemy) {

        playerDMG = random.nextInt(playerMaxDMG) + playerMinDMG;
        if (passive.containsKey("TurnTechnique")) {
            TurnTechniquePassive turnPassive = (TurnTechniquePassive) this.passive.get("TurnTechnique");
            turnPassive.passiveAbility(this, enemy);
        }
        System.out.print("""
                        %s has dealt %d Damage
                        %s has now %d HP
                         """.formatted(playerName,
                playerDMG,
                enemy.playerName,
                enemy.damageAttack(playerDMG)));
        damageInflicted.push(playerDMG);
        enemy.playerHPStack.push(enemy.playerHP);
    }

    public void stun(Character enemy) {

        if (new Random().nextInt(4) != 0) {
            System.out.println("Tried to Stun But Failed");
            return;
        }

        int stunAmount = random.nextInt(3) + 1;
        System.out.printf("%s have Stun %s by %d Turn!%n".formatted(playerName,
                enemy.playerName,
                enemy.stunned = stunAmount));
    }

    public void passive(Character enemy) {
        if (!passive.isEmpty()) {
            for (var pas : passive.entrySet()) {
                if (pas.getKey().equals("TurnTechnique")) {
                    continue;
                }
                Passive curPas = Passive.assignPassive(pas.getKey());
                if (passive != null) {
                    curPas.passiveAbility(this, enemy);
                }
            }
        }
    }

    public int damageAttack(int damageDealth) {
        if (playerHP - damageDealth <= 0) {
            playerHP = 0;
        } else {
            playerHP -= damageDealth;
        }
        return playerHP;
    }

}

abstract class Passive {

    public abstract void passiveAbility(Character character, Character enemy);

    public static Passive assignPassive(String passive) {
        return switch (passive) {
            case "Heal" ->
                new HealPassive();
            case "TurnTechnique" ->
                new TurnTechniquePassive();
            case "UnoReverse" ->
                new UnoReversePassive();
            default ->
                null;
        };
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }
}

class HealPassive extends Passive {

    @Override
    public void passiveAbility(Character character, Character enemy) {

        if (character.playerHPStack.size() <= 1 || new Random().nextInt(4) + 1 != 4) {
            return;
        }
        character.playerHPStack.pop();
        character.playerHP = character.playerHPStack.peek();
        System.out.printf("%s's *Passive Healing Ability* has healed itself back to %s%n", character.playerName, character.playerHP);
    }
}

class TurnTechniquePassive extends Passive {

    Queue<Integer> attackStack = new LinkedList<>();

    @Override
    public void passiveAbility(Character character, Character enemy) {
        attackStack.add(character.playerDMG);
        if (attackStack.size() % 4 == 0) {
            int abilityChoice = new Random().nextInt(2);
            if (abilityChoice == 0) {
                System.out.println("*Turn Technique Passive/Jingu Mastery* has been activiated -> double damage");
                character.playerDMG = character.playerDMG * 2;
            } else {
                System.out.println("*Turn Technique Passive/Jingu Mastery* has been activiated -> enemy has been burned for 3 turns");
                enemy.burned = 3;
            }
        }
    }
}

class UnoReversePassive extends Passive {

    @Override
    public void passiveAbility(Character character, Character enemy) {

        Stack<Integer> damageInflictedStack = enemy.damageInflicted;

        if (damageInflictedStack.isEmpty()) {
            damageInflictedStack.add(character.playerDMG);
        }

        if (new Random().nextInt(5) == 0) {
            if (!damageInflictedStack.isEmpty()) {
                System.out.printf("%s has used *Uno Reversed Technique*. %s healed, %s damage returned \n",
                        character.playerName,
                        damageInflictedStack.peek(),
                        damageInflictedStack.peek());
                character.playerHP += damageInflictedStack.peek();
                enemy.playerDMG -= damageInflictedStack.pop();
            } else {
                System.out.println("UNO reverse card failed.");
            }

        }
    }
}
