/*
 * Monster Battle System — OpenAI OOD coding question.
 *
 * Two teams of monsters fight in turn-based combat; we produce an event log
 * (list of strings) describing each attack, elimination, and the outcome.
 *
 * ------------------------------------------------------------------------
 * THREE PARTS (each is fully self-contained in its own nested class)
 * ------------------------------------------------------------------------
 * PART 1 — Basic battle. Monster{name,health,attack}; Team{name,monsters}.
 *   Teams alternate starting with Team A. The attacking team's first-alive
 *   monster attacks the defending team's first-alive monster: defender.health
 *   -= attacker.attack. No counter-damage. Eliminated when health <= 0. Battle
 *   ends when one team is fully eliminated. Winner: if B is defeated, A wins,
 *   else B wins (so a both-empty edge case credits A, faithful to reference).
 *
 * PART 2 — Type advantages. Adds MonsterType{FIRE,WATER,GRASS,ELECTRIC} and a
 *   (attacker,defender) -> multiplier chart (2.0 / 0.5; default 1.0).
 *   calculate_damage = int(attack * multiplier). The log gains an effectiveness
 *   suffix placed right after the word "damage": " (Super effective!)" when the
 *   dealt damage exceeds the attacker's raw attack, " (Not very effective...)"
 *   when it is less, and "" when equal.
 *
 * PART 3 — Smart targeting. Defender is STILL the defending team's first-alive
 *   monster, but the attacker is the alive teammate that deals the MAX damage
 *   to that defender; ties go to the earliest in the list (strict ">" update
 *   while scanning in order). Everything else is identical to Part 2.
 *
 * ------------------------------------------------------------------------
 * DAMAGE TRUNCATION
 * ------------------------------------------------------------------------
 * Python's int() truncates toward zero. The only multipliers are {0.5, 1.0,
 * 2.0} and attack is a positive int, so the product is >= 0 and truncation ==
 * floor. We implement this with pure integer arithmetic to avoid any float
 * surprises: 2.0 -> attack*2, 1.0 -> attack, 0.5 -> attack/2 (Java int division
 * truncates toward zero, which for non-negative values equals floor). Health is
 * NOT clamped — it may go negative, but a negative value is never printed
 * because an eliminated monster prints "is eliminated!" instead of its HP.
 *
 * ------------------------------------------------------------------------
 * EMBEDDED CHECKS
 * ------------------------------------------------------------------------
 * Each part's main() builds the canonical example, runs its battle, and
 * verifies the result against hard-coded expectations using explicit if-throw
 * checks (NOT `assert`), so correctness is enforced even when the JVM is run
 * WITHOUT -ea. On success each part prints its full log followed by "Part N: OK".
 *
 * PART-3 TRAP (documented, deliberately NOT asserted): the question's
 * "Complete Example" roster is [FireDragon FIRE, ElectricEel ELECTRIC,
 * GrassGolem GRASS] vs a WATER defender, and the prose claims ElectricEel
 * attacks first. That prose is WRONG: GrassGolem is GRASS and GRASS->WATER is
 * 2.0, so it deals 18*2 = 36 > ElectricEel's 15*2 = 30, meaning GrassGolem is
 * actually selected. We therefore do NOT assert attacker identity on that
 * roster. Part 3 is instead proven by the authoritative reference roster
 * (Fire/Electric vs Water => Electric is chosen) plus an explicit tie-break test.
 *
 * ------------------------------------------------------------------------
 * SELF-CONTAINMENT CONTRACT
 * ------------------------------------------------------------------------
 * Each `static final class PartN` nests its OWN Monster/Team (and, for Part 2/3,
 * MonsterType + type-chart helper), its OWN battle(...), its OWN main(...), and
 * its OWN check helpers. There are ZERO references between parts or to any
 * shared top-level type. Copying any single PartN class into its own .java file
 * (promote `static final class PartN` -> top-level `class PartN`, keep the inner
 * helper classes as `static` nested classes) compiles and runs UNCHANGED.
 *
 * Complexity (each part): O(T * M) time, O(T) space, where T = number of turns
 * (bounded by total HP / min effective damage) and M = team size.
 *
 * Top-level class is package-private (not public) so the file may legally be
 * named "A - Monster Battle System.java"; quote the path when invoking javac.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MonsterBattleSystem {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("===== PART 1 =====");
            Part1.main(args);
            System.out.println();
            System.out.println("===== PART 2 =====");
            Part2.main(args);
            System.out.println();
            System.out.println("===== PART 3 =====");
            Part3.main(args);
            return;
        }
        String which = args[0].toLowerCase();
        switch (which) {
            case "part1" -> Part1.main(args);
            case "part2" -> Part2.main(args);
            case "part3" -> Part3.main(args);
            default -> {
                System.err.println("Usage: MonsterBattleSystem [part1|part2|part3]");
                System.err.println("  (no argument runs all three parts in sequence)");
            }
        }
    }

    // ===== BEGIN PART 1 (self-contained; promote to top-level to run standalone) =====
    static final class Part1 {

        /** A monster with a name, current health, and attack power. */
        static final class Monster {
            final String name;
            int health;
            final int attack;

            Monster(String name, int health, int attack) {
                this.name = name;
                this.health = health;
                this.attack = attack;
            }

            boolean isAlive() {
                return health > 0;
            }

            void takeDamage(int damage) {
                health -= damage; // not clamped; negative HP is never printed
            }
        }

        /** A named team holding an ordered list of monsters. */
        static final class Team {
            final String name;
            final List<Monster> monsters;

            Team(String name, List<Monster> monsters) {
                this.name = name;
                this.monsters = monsters;
            }

            /** First still-alive monster in list order, or null if all eliminated. */
            Monster getFirstAlive() {
                for (Monster m : monsters) {
                    if (m.isAlive()) {
                        return m;
                    }
                }
                return null;
            }

            boolean isDefeated() {
                for (Monster m : monsters) {
                    if (m.isAlive()) {
                        return false;
                    }
                }
                return true;
            }
        }

        /**
         * Simulate a turn-based battle. Team A attacks first; the attacking
         * team's first-alive monster hits the defending team's first-alive
         * monster for attacker.attack damage (no counter-damage). The loop ends
         * when either team is fully eliminated.
         */
        static List<String> battle(Team teamA, Team teamB) {
            List<String> events = new ArrayList<>();
            events.add("Battle begins: " + teamA.name + " vs " + teamB.name);

            Team attackerTeam = teamA;
            Team defenderTeam = teamB;

            while (!teamA.isDefeated() && !teamB.isDefeated()) {
                Monster attacker = attackerTeam.getFirstAlive();
                Monster defender = defenderTeam.getFirstAlive();
                if (attacker == null || defender == null) {
                    break;
                }

                int damage = attacker.attack;
                defender.takeDamage(damage);

                if (defender.isAlive()) {
                    events.add(attacker.name + " attacks " + defender.name + " for " + damage
                            + " damage. " + defender.name + " has " + defender.health + " HP remaining.");
                } else {
                    events.add(attacker.name + " attacks " + defender.name + " for " + damage
                            + " damage. " + defender.name + " is eliminated!");
                }

                // swap roles for the next turn
                Team tmp = attackerTeam;
                attackerTeam = defenderTeam;
                defenderTeam = tmp;
            }

            Team winner = teamB.isDefeated() ? teamA : teamB;
            events.add("Battle ends: " + winner.name + " wins!");
            return events;
        }

        // ---- embedded checks (if-throw; no -ea required) ----

        static void check(boolean cond, String msg) {
            if (!cond) {
                throw new AssertionError(msg);
            }
        }

        static void expectEqualLists(List<String> actual, List<String> expected) {
            int n = Math.min(actual.size(), expected.size());
            for (int i = 0; i < n; i++) {
                if (!actual.get(i).equals(expected.get(i))) {
                    throw new AssertionError("Log mismatch at index " + i
                            + "\n  expected: " + expected.get(i)
                            + "\n  actual:   " + actual.get(i));
                }
            }
            if (actual.size() != expected.size()) {
                throw new AssertionError("Log size mismatch: expected " + expected.size()
                        + " lines, got " + actual.size()
                        + (actual.size() > expected.size()
                            ? "\n  first extra actual line: " + actual.get(n)
                            : "\n  first missing expected line: " + expected.get(n)));
            }
        }

        public static void main(String[] args) {
            Monster dragon = new Monster("Dragon", 100, 25);
            Monster griffin = new Monster("Griffin", 80, 20);
            Team teamA = new Team("Heroes", new ArrayList<>(Arrays.asList(dragon, griffin)));

            Monster goblin = new Monster("Goblin", 30, 10);
            Monster orc = new Monster("Orc", 50, 15);
            Monster troll = new Monster("Troll", 70, 12);
            Team teamB = new Team("Monsters", new ArrayList<>(Arrays.asList(goblin, orc, troll)));

            List<String> log = battle(teamA, teamB);

            List<String> expected = Arrays.asList(
                "Battle begins: Heroes vs Monsters",
                "Dragon attacks Goblin for 25 damage. Goblin has 5 HP remaining.",
                "Goblin attacks Dragon for 10 damage. Dragon has 90 HP remaining.",
                "Dragon attacks Goblin for 25 damage. Goblin is eliminated!",
                "Orc attacks Dragon for 15 damage. Dragon has 75 HP remaining.",
                "Dragon attacks Orc for 25 damage. Orc has 25 HP remaining.",
                "Orc attacks Dragon for 15 damage. Dragon has 60 HP remaining.",
                "Dragon attacks Orc for 25 damage. Orc is eliminated!",
                "Troll attacks Dragon for 12 damage. Dragon has 48 HP remaining.",
                "Dragon attacks Troll for 25 damage. Troll has 45 HP remaining.",
                "Troll attacks Dragon for 12 damage. Dragon has 36 HP remaining.",
                "Dragon attacks Troll for 25 damage. Troll has 20 HP remaining.",
                "Troll attacks Dragon for 12 damage. Dragon has 24 HP remaining.",
                "Dragon attacks Troll for 25 damage. Troll is eliminated!",
                "Battle ends: Heroes wins!"
            );

            expectEqualLists(log, expected);

            for (String line : log) {
                System.out.println(line);
            }
            System.out.println("Part 1: OK");
        }
    }
    // ===== END PART 1 =====

    // ===== BEGIN PART 2 (self-contained; promote to top-level to run standalone) =====
    static final class Part2 {

        /*
         * CHANGES FROM PART 1:
         *   - Monster gains a MonsterType and a calculate_damage(defender) method
         *     that applies the type-effectiveness multiplier.
         *   - battle() now deals attacker.calculateDamage(defender) instead of the
         *     raw attack, and appends an effectiveness suffix to each attack line
         *     (right after the word "damage"): " (Super effective!)" / " (Not very
         *     effective...)" / "".
         */

        enum MonsterType {
            FIRE("Fire"), WATER("Water"), GRASS("Grass"), ELECTRIC("Electric");

            final String label;

            MonsterType(String label) {
                this.label = label;
            }
        }

        /**
         * Type-effectiveness multiplier for attacker->defender, returned as a
         * numerator/2 fraction so we can do truncating integer math:
         *   2.0 -> 4 (attack*4/2 == attack*2)
         *   1.0 -> 2 (attack*2/2 == attack)
         *   0.5 -> 1 (attack*1/2 == attack/2, truncating)
         * Using halves keeps everything in ints while matching Python's int().
         */
        static int multiplierHalves(MonsterType attacker, MonsterType defender) {
            if (attacker == MonsterType.FIRE && defender == MonsterType.GRASS) return 4;
            if (attacker == MonsterType.FIRE && defender == MonsterType.WATER) return 1;
            if (attacker == MonsterType.WATER && defender == MonsterType.FIRE) return 4;
            if (attacker == MonsterType.WATER && defender == MonsterType.GRASS) return 1;
            if (attacker == MonsterType.GRASS && defender == MonsterType.WATER) return 4;
            if (attacker == MonsterType.GRASS && defender == MonsterType.FIRE) return 1;
            if (attacker == MonsterType.ELECTRIC && defender == MonsterType.WATER) return 4;
            return 2; // neutral 1.0x
        }

        static final class Monster {
            final String name;
            int health;
            final int attack;
            final MonsterType type;

            Monster(String name, int health, int attack, MonsterType type) {
                this.name = name;
                this.health = health;
                this.attack = attack;
                this.type = type;
            }

            boolean isAlive() {
                return health > 0;
            }

            void takeDamage(int damage) {
                health -= damage;
            }

            /** int(attack * multiplier) via truncating integer math. */
            int calculateDamage(Monster defender) {
                return attack * multiplierHalves(this.type, defender.type) / 2;
            }
        }

        static final class Team {
            final String name;
            final List<Monster> monsters;

            Team(String name, List<Monster> monsters) {
                this.name = name;
                this.monsters = monsters;
            }

            Monster getFirstAlive() {
                for (Monster m : monsters) {
                    if (m.isAlive()) {
                        return m;
                    }
                }
                return null;
            }

            boolean isDefeated() {
                for (Monster m : monsters) {
                    if (m.isAlive()) {
                        return false;
                    }
                }
                return true;
            }
        }

        static String effectivenessSuffix(int dealt, int baseAttack) {
            if (dealt > baseAttack) return " (Super effective!)";
            if (dealt < baseAttack) return " (Not very effective...)";
            return "";
        }

        static List<String> battle(Team teamA, Team teamB) {
            List<String> events = new ArrayList<>();
            events.add("Battle begins: " + teamA.name + " vs " + teamB.name);

            Team attackerTeam = teamA;
            Team defenderTeam = teamB;

            while (!teamA.isDefeated() && !teamB.isDefeated()) {
                Monster attacker = attackerTeam.getFirstAlive();
                Monster defender = defenderTeam.getFirstAlive();
                if (attacker == null || defender == null) {
                    break;
                }

                int damage = attacker.calculateDamage(defender);
                defender.takeDamage(damage);
                String suffix = effectivenessSuffix(damage, attacker.attack);

                if (defender.isAlive()) {
                    events.add(attacker.name + " attacks " + defender.name + " for " + damage
                            + " damage" + suffix + ". " + defender.name + " has " + defender.health
                            + " HP remaining.");
                } else {
                    events.add(attacker.name + " attacks " + defender.name + " for " + damage
                            + " damage" + suffix + ". " + defender.name + " is eliminated!");
                }

                Team tmp = attackerTeam;
                attackerTeam = defenderTeam;
                defenderTeam = tmp;
            }

            Team winner = teamB.isDefeated() ? teamA : teamB;
            events.add("Battle ends: " + winner.name + " wins!");
            return events;
        }

        // ---- embedded checks (if-throw; no -ea required) ----

        static void check(boolean cond, String msg) {
            if (!cond) {
                throw new AssertionError(msg);
            }
        }

        static void expectEqualLists(List<String> actual, List<String> expected) {
            int n = Math.min(actual.size(), expected.size());
            for (int i = 0; i < n; i++) {
                if (!actual.get(i).equals(expected.get(i))) {
                    throw new AssertionError("Log mismatch at index " + i
                            + "\n  expected: " + expected.get(i)
                            + "\n  actual:   " + actual.get(i));
                }
            }
            if (actual.size() != expected.size()) {
                throw new AssertionError("Log size mismatch: expected " + expected.size()
                        + " lines, got " + actual.size());
            }
        }

        public static void main(String[] args) {
            // (i) Unit checks on calculate_damage with the canonical Fire/Water pair.
            Monster fireDragonUnit = new Monster("FireDragon", 100, 20, MonsterType.FIRE);
            Monster waterSerpentUnit = new Monster("WaterSerpent", 80, 15, MonsterType.WATER);
            check(fireDragonUnit.calculateDamage(waterSerpentUnit) == 10,
                    "Fire->Water should be 20*0.5=10, got "
                            + fireDragonUnit.calculateDamage(waterSerpentUnit));
            check(waterSerpentUnit.calculateDamage(fireDragonUnit) == 30,
                    "Water->Fire should be 15*2.0=30, got "
                            + waterSerpentUnit.calculateDamage(fireDragonUnit));

            // (ii) 1v1 battle: verify the two attack lines carry the right suffixes.
            Monster fireDragon = new Monster("FireDragon", 100, 20, MonsterType.FIRE);
            Monster waterSerpent = new Monster("WaterSerpent", 80, 15, MonsterType.WATER);
            Team teamA = new Team("A", new ArrayList<>(Arrays.asList(fireDragon)));
            Team teamB = new Team("B", new ArrayList<>(Arrays.asList(waterSerpent)));
            List<String> log = battle(teamA, teamB);

            check(log.get(1).equals(
                    "FireDragon attacks WaterSerpent for 10 damage (Not very effective...). "
                            + "WaterSerpent has 70 HP remaining."),
                    "Part2 log[1] mismatch:\n  actual: " + log.get(1));
            check(log.get(2).equals(
                    "WaterSerpent attacks FireDragon for 30 damage (Super effective!). "
                            + "FireDragon has 70 HP remaining."),
                    "Part2 log[2] mismatch:\n  actual: " + log.get(2));

            // (iii) Neutral matchup: WATER vs WATER must use the "" branch.
            Monster w1 = new Monster("Wave", 100, 12, MonsterType.WATER);
            Monster w2 = new Monster("Tide", 100, 12, MonsterType.WATER);
            Team neutralA = new Team("NA", new ArrayList<>(Arrays.asList(w1)));
            Team neutralB = new Team("NB", new ArrayList<>(Arrays.asList(w2)));
            List<String> neutralLog = battle(neutralA, neutralB);
            String firstAttack = neutralLog.get(1);
            check(firstAttack.contains(" damage. "),
                    "Neutral line should read '... damage. ...': " + firstAttack);
            check(!firstAttack.contains("(Super") && !firstAttack.contains("(Not very"),
                    "Neutral line must carry no effectiveness suffix: " + firstAttack);

            for (String line : log) {
                System.out.println(line);
            }
            System.out.println("Part 2: OK");
        }
    }
    // ===== END PART 2 =====

    // ===== BEGIN PART 3 (self-contained; promote to top-level to run standalone) =====
    static final class Part3 {

        /*
         * CHANGES FROM PART 2:
         *   - The defender is unchanged (defending team's first-alive monster),
         *     but the attacker is no longer simply the attacking team's first
         *     alive monster. Instead Team.getBestAttacker(defender) scans all
         *     alive teammates in list order and returns the one dealing the
         *     greatest calculate_damage(defender). The update uses a STRICT ">"
         *     against bestDamage (seeded at -1), so on a tie the earliest monster
         *     in the list is kept.
         *   - Damage calculation, the effectiveness suffix, turn order, and the
         *     winner rule are all identical to Part 2.
         */

        enum MonsterType {
            FIRE("Fire"), WATER("Water"), GRASS("Grass"), ELECTRIC("Electric");

            final String label;

            MonsterType(String label) {
                this.label = label;
            }
        }

        /** See Part 2: halves-encoded multiplier for truncating integer math. */
        static int multiplierHalves(MonsterType attacker, MonsterType defender) {
            if (attacker == MonsterType.FIRE && defender == MonsterType.GRASS) return 4;
            if (attacker == MonsterType.FIRE && defender == MonsterType.WATER) return 1;
            if (attacker == MonsterType.WATER && defender == MonsterType.FIRE) return 4;
            if (attacker == MonsterType.WATER && defender == MonsterType.GRASS) return 1;
            if (attacker == MonsterType.GRASS && defender == MonsterType.WATER) return 4;
            if (attacker == MonsterType.GRASS && defender == MonsterType.FIRE) return 1;
            if (attacker == MonsterType.ELECTRIC && defender == MonsterType.WATER) return 4;
            return 2; // neutral 1.0x
        }

        static final class Monster {
            final String name;
            int health;
            final int attack;
            final MonsterType type;

            Monster(String name, int health, int attack, MonsterType type) {
                this.name = name;
                this.health = health;
                this.attack = attack;
                this.type = type;
            }

            boolean isAlive() {
                return health > 0;
            }

            void takeDamage(int damage) {
                health -= damage;
            }

            int calculateDamage(Monster defender) {
                return attack * multiplierHalves(this.type, defender.type) / 2;
            }
        }

        static final class Team {
            final String name;
            final List<Monster> monsters;

            Team(String name, List<Monster> monsters) {
                this.name = name;
                this.monsters = monsters;
            }

            Monster getFirstAlive() {
                for (Monster m : monsters) {
                    if (m.isAlive()) {
                        return m;
                    }
                }
                return null;
            }

            /**
             * Among alive teammates (scanned in list order), the one dealing the
             * most damage to {@code defender}; ties resolve to the earliest in
             * the list via a strict ">" comparison. Null if none are alive.
             */
            Monster getBestAttacker(Monster defender) {
                Monster best = null;
                int bestDamage = -1;
                for (Monster m : monsters) {
                    if (m.isAlive()) {
                        int d = m.calculateDamage(defender);
                        if (d > bestDamage) {
                            bestDamage = d;
                            best = m;
                        }
                    }
                }
                return best;
            }

            boolean isDefeated() {
                for (Monster m : monsters) {
                    if (m.isAlive()) {
                        return false;
                    }
                }
                return true;
            }
        }

        static String effectivenessSuffix(int dealt, int baseAttack) {
            if (dealt > baseAttack) return " (Super effective!)";
            if (dealt < baseAttack) return " (Not very effective...)";
            return "";
        }

        static List<String> battle(Team teamA, Team teamB) {
            List<String> events = new ArrayList<>();
            events.add("Battle begins: " + teamA.name + " vs " + teamB.name);

            Team attackerTeam = teamA;
            Team defenderTeam = teamB;

            while (!teamA.isDefeated() && !teamB.isDefeated()) {
                Monster defender = defenderTeam.getFirstAlive();
                if (defender == null) {
                    break;
                }
                Monster attacker = attackerTeam.getBestAttacker(defender);
                if (attacker == null) {
                    break;
                }

                int damage = attacker.calculateDamage(defender);
                defender.takeDamage(damage);
                String suffix = effectivenessSuffix(damage, attacker.attack);

                if (defender.isAlive()) {
                    events.add(attacker.name + " attacks " + defender.name + " for " + damage
                            + " damage" + suffix + ". " + defender.name + " has " + defender.health
                            + " HP remaining.");
                } else {
                    events.add(attacker.name + " attacks " + defender.name + " for " + damage
                            + " damage" + suffix + ". " + defender.name + " is eliminated!");
                }

                Team tmp = attackerTeam;
                attackerTeam = defenderTeam;
                defenderTeam = tmp;
            }

            Team winner = teamB.isDefeated() ? teamA : teamB;
            events.add("Battle ends: " + winner.name + " wins!");
            return events;
        }

        // ---- embedded checks (if-throw; no -ea required) ----

        static void check(boolean cond, String msg) {
            if (!cond) {
                throw new AssertionError(msg);
            }
        }

        static void expectEqualLists(List<String> actual, List<String> expected) {
            int n = Math.min(actual.size(), expected.size());
            for (int i = 0; i < n; i++) {
                if (!actual.get(i).equals(expected.get(i))) {
                    throw new AssertionError("Log mismatch at index " + i
                            + "\n  expected: " + expected.get(i)
                            + "\n  actual:   " + actual.get(i));
                }
            }
            if (actual.size() != expected.size()) {
                throw new AssertionError("Log size mismatch: expected " + expected.size()
                        + " lines, got " + actual.size());
            }
        }

        public static void main(String[] args) {
            // PRIMARY GATE — authoritative reference roster from the question's
            // test_smart_targeting: Electric (15*2=30) beats Fire (20*0.5=10).
            Monster fire = new Monster("Fire", 50, 20, MonsterType.FIRE);
            Monster electric = new Monster("Electric", 50, 15, MonsterType.ELECTRIC);
            Monster water = new Monster("Water", 100, 10, MonsterType.WATER);
            Team teamA = new Team("A", new ArrayList<>(Arrays.asList(fire, electric)));
            Team teamB = new Team("B", new ArrayList<>(Arrays.asList(water)));
            List<String> log = battle(teamA, teamB);

            check(log.get(1).contains("Electric attacks Water"),
                    "Part3 expected Electric to be chosen first; got: " + log.get(1));
            check(log.get(1).startsWith("Electric attacks Water for 30 damage (Super effective!)."),
                    "Part3 log[1] should report 30 damage Super effective; got: " + log.get(1));

            // TIE-BREAK — two FIRE monsters each deal 20*2=40 to GRASS; the earliest
            // in the list (FireA) must be chosen.
            Monster fireA = new Monster("FireA", 50, 20, MonsterType.FIRE);
            Monster fireB = new Monster("FireB", 50, 20, MonsterType.FIRE);
            Monster grass = new Monster("Grass", 100, 10, MonsterType.GRASS);
            Team tieA = new Team("A", new ArrayList<>(Arrays.asList(fireA, fireB)));
            Team tieB = new Team("B", new ArrayList<>(Arrays.asList(grass)));
            List<String> tieLog = battle(tieA, tieB);
            check(tieLog.get(1).startsWith("FireA attacks Grass"),
                    "Part3 tie-break should pick the earliest (FireA); got: " + tieLog.get(1));

            // NOTE: we deliberately do NOT assert attacker identity on the question's
            // "Complete Example" roster ([FireDragon FIRE, ElectricEel ELECTRIC,
            // GrassGolem GRASS] vs WATER): GrassGolem deals 18*2=36 > ElectricEel's 30,
            // so GrassGolem is actually chosen first — the prose's "ElectricEel first"
            // claim is wrong. See the top-of-file trap note.

            for (String line : log) {
                System.out.println(line);
            }
            System.out.println("Part 3: OK");
        }
    }
    // ===== END PART 3 =====
}
