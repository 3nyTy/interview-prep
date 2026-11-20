/*
 * ============================================================================
 *  Chat Bot System Refactoring  —  OpenAI Staff-SWE coding interview question.
 * ============================================================================
 *
 *  A legacy chat service supports several slash-command bots (AwayBot, MeetBot,
 *  TacoBot). The original code crams every bot into one function over global
 *  state. We refactor it, in stages, into clean, extensible, testable Java.
 *
 *  This SINGLE file contains FIVE independently-runnable parts, one per stage:
 *
 *    Part 1 — Legacy port: a faithful Java translation of the original coupled
 *             `sendMessage` (one method, two maps). Reproduces the canonical
 *             13-line transcript exactly.
 *    Part 2 — Initial refactor: an `IBotService` interface + one class per bot
 *             + a `ChatRoom` registry. Cross-bot communication via DEPENDENCY
 *             INJECTION (MeetBot holds an AwayBot reference).
 *    Part 3 — Event-driven architecture: dependency injection replaced by a
 *             publish/subscribe `EventBus`. Bots no longer reference each other.
 *    Part 4 — Test cases: a dependency-free (no JUnit) assertion suite of unit,
 *             integration and edge-case tests over the Part-3 event-driven bots.
 *    Part 5 — Extensions, folded into one cohesive design: command validation,
 *             bot priority/ordering, a middleware pipeline, and an async
 *             (CompletableFuture) event bus.
 *
 *  HOW TO COMPILE AND RUN (the file contains the 🌮 emoji, so UTF-8 matters):
 *
 *      javac -encoding UTF-8 "A - Chatbot.java"
 *      java Chatbot            # runs ALL parts, prints "ALL PARTS PASSED"
 *      java Chatbot part3      # runs only Part 3 (part1 .. part5)
 *
 *  SELF-CONTAINMENT CONTRACT:
 *      Each PartN is a self-contained `static` nested class with its OWN
 *      `public static void main(String[])` and its OWN nested helper types
 *      (interface / ChatRoom / bots / EventBus / ...). No PartN references any
 *      other PartN or the enclosing `Chatbot` class. Consequently the shared
 *      types are DELIBERATELY DUPLICATED across parts: copying any single PartN
 *      class into its own `.java` (promoted to a public top-level class, inner
 *      helpers kept) compiles and runs unchanged.
 *
 *  NOTE ON ASSERTIONS:
 *      Tests use a small `check(...)` helper that throws `AssertionError` on
 *      failure rather than the `assert` keyword, so plain `java Chatbot`
 *      genuinely validates every embedded example WITHOUT needing the `-ea`
 *      flag (a regression throws and exits non-zero).
 * ============================================================================
 */

import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;

// Package-private top-level class (NOT public): a file named "A - Chatbot.java"
// cannot host a public class of a different name, but a package-private one is
// fine. Target: Java 21.
class Chatbot {

    /** Dispatcher: `java Chatbot` runs all parts; `java Chatbot partN` runs one. */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("===== PART 1 =====");
            Part1.main(new String[0]);
            System.out.println("===== PART 2 =====");
            Part2.main(new String[0]);
            System.out.println("===== PART 3 =====");
            Part3.main(new String[0]);
            System.out.println("===== PART 4 =====");
            Part4.main(new String[0]);
            System.out.println("===== PART 5 =====");
            Part5.main(new String[0]);
            System.out.println("ALL PARTS PASSED");
            return;
        }
        switch (args[0]) {
            case "part1" -> Part1.main(new String[0]);
            case "part2" -> Part2.main(new String[0]);
            case "part3" -> Part3.main(new String[0]);
            case "part4" -> Part4.main(new String[0]);
            case "part5" -> Part5.main(new String[0]);
            default -> System.out.println(
                    "Usage: java Chatbot [part1|part2|part3|part4|part5]   (no arg = run all)");
        }
    }

    // ========================================================================
    //  PART 1 — Faithful port of the legacy coupled `sendMessage`.
    // ========================================================================
    //
    //  The original Python keeps three module-level globals (`aways`, `tacos`,
    //  `messages`) and one big function. We port it line-for-line so the exact
    //  canonical transcript is reproduced, including its quirks:
    //    * the AwayBot mention scan runs FIRST (before MeetBot), which is why
    //      Frank's "/meet David" shows the AwayBot line BEFORE the MeetBot line;
    //    * the taco recipient token "@justin" is printed verbatim after
    //      "gave @", producing the double "@@justin";
    //    * the taco count is the NUMBER OF EMOJI (code points), so "🌮🌮" == 2.
    //
    static final class Part1 {

        // Mirror the legacy globals. LinkedHashMap preserves insertion order,
        // matching Python dict iteration order (relevant when several users are
        // away at once).
        static final LinkedHashMap<String, String> aways = new LinkedHashMap<>();
        static final LinkedHashMap<String, Integer> tacos = new LinkedHashMap<>();
        static final List<String> messages = new ArrayList<>();

        /** Direct translation of the legacy `sendMessage`; ordering is load-bearing. */
        static void sendMessage(String name, String msg) {
            messages.add(name + ": " + msg);

            // (1) AwayBot — notify for each away user mentioned. Runs first, so
            //     away statuses set later in this same call do not self-notify.
            for (Map.Entry<String, String> e : aways.entrySet()) {
                if (msg.contains(e.getKey())) {
                    messages.add("AwayBot: " + e.getKey() + " is away: " + e.getValue());
                }
            }

            // (2) MeetBot — legacy guard was msg[1:5] == "meet"; startsWith("/meet")
            //     is equivalent for the given inputs. Slices are length-guarded so
            //     malformed input never throws.
            if (msg.startsWith("/meet")) {
                String other = msg.length() >= 6 ? msg.substring(6) : "";
                messages.add("MeetBot: Google Meet with @" + name + ", and " + other
                        + " starting at https://meet.google.com/abc-def-123");
                aways.put(name, "@" + name + " may be in a meeting right now");
                String k2 = msg.length() >= 7 ? msg.substring(7) : "";
                aways.put(k2, "@" + k2 + " may be in a meeting right now");
            }

            // (3) TacoBot — legacy guard msg[1:9] == "givetaco".
            if (msg.startsWith("/givetaco")) {
                String[] p = msg.split(" ");
                if (p.length >= 3) {
                    String tacoStr = p[1];
                    String who = p[2];
                    // Count EMOJI (code points), not UTF-16 chars: "🌮🌮" -> 2.
                    int num = tacoStr.codePointCount(0, tacoStr.length());
                    String key = who.startsWith("@") ? who.substring(1) : who; // legacy who[1:]
                    tacos.merge(key, num, Integer::sum);
                    messages.add("TacoBot: @" + name + " gave @" + who + " " + num
                            + " 🌮's. " + who + " now have " + tacos.get(key) + " 🌮s.");
                }
            }

            // (4) Away status set — legacy guard msg[1:5] == "away".
            if (msg.startsWith("/away")) {
                if (msg.length() >= 6) {
                    aways.put(name, msg.substring(6)); // legacy msg[6:]
                }
            }
        }

        public static void main(String[] args) {
            sendMessage("Alice", "Hello");
            sendMessage("Bob", "Hi");
            sendMessage("Alice", "Nice job on your presentations");
            sendMessage("Cindy", "/givetaco 🌮🌮 @justin");
            sendMessage("Alice", "Bob let's meet");
            sendMessage("Bob", "/meet Alice");
            sendMessage("David", "/away out for lunch");
            sendMessage("Emily", "Anyone around?");
            sendMessage("Frank", "/meet David");

            List<String> expected = List.of(
                    "Alice: Hello",
                    "Bob: Hi",
                    "Alice: Nice job on your presentations",
                    "Cindy: /givetaco 🌮🌮 @justin",
                    "TacoBot: @Cindy gave @@justin 2 🌮's. @justin now have 2 🌮s.",
                    "Alice: Bob let's meet",
                    "Bob: /meet Alice",
                    "MeetBot: Google Meet with @Bob, and Alice starting at https://meet.google.com/abc-def-123",
                    "David: /away out for lunch",
                    "Emily: Anyone around?",
                    "Frank: /meet David",
                    "AwayBot: David is away: out for lunch",
                    "MeetBot: Google Meet with @Frank, and David starting at https://meet.google.com/abc-def-123");

            assertEquals(expected, messages, "Part1 canonical 13-line output");
            System.out.println("Part1: PASS (canonical legacy output reproduced)");
        }

        // ---- assertion helpers (no -ea needed) ----
        private static void check(boolean cond, String label) {
            if (!cond) throw new AssertionError(label);
        }
        private static void assertEquals(Object expected, Object actual, String label) {
            check(Objects.equals(expected, actual),
                    label + " | expected=" + expected + " actual=" + actual);
        }
    }

    // ========================================================================
    //  PART 2 — Initial refactor with an IBotService interface + DI.
    // ========================================================================
    //
    //  CHANGES FROM PART 1:
    //    * Replaced the single coupled `sendMessage` + global maps with an
    //      `IBotService` interface and three bot classes, each ENCAPSULATING its
    //      own state (single responsibility).
    //    * Added a `ChatRoom` registry: it appends "name: msg" then iterates the
    //      registered bots, executing those whose `shouldActivate` is true and
    //      collecting their responses (open/closed — add a bot without touching
    //      the others).
    //    * Cross-bot communication via DEPENDENCY INJECTION: MeetBot holds an
    //      AwayBot reference and calls `setAway(...)` for both participants.
    //    * Trigger detection moved from fragile index slicing to
    //      startsWith("/meet ") / ("/givetaco ") / ("/away ") (note trailing
    //      space, so a bare "/meet" never activates).
    //    * Registration order is away, meet, taco so the AwayBot notification
    //      still precedes the MeetBot announcement (canonical ordering).
    //
    static final class Part2 {

        /** Contract every bot implements. */
        interface IBotService {
            boolean shouldActivate(String name, String msg);
            List<String> execute(String name, String msg);
        }

        /** Holds registered bots, the transcript, and the dispatch loop. */
        static final class ChatRoom {
            final List<IBotService> bots = new ArrayList<>();
            final List<String> messages = new ArrayList<>();

            void registerBot(IBotService b) { bots.add(b); }

            void sendMessage(String name, String msg) {
                messages.add(name + ": " + msg);
                for (IBotService b : bots) {
                    if (b.shouldActivate(name, msg)) {
                        messages.addAll(b.execute(name, msg));
                    }
                }
            }
        }

        static final class AwayBot implements IBotService {
            final LinkedHashMap<String, String> awayStatuses = new LinkedHashMap<>();

            /** Public hook so other bots (via DI) can set away status. */
            void setAway(String user, String message) { awayStatuses.put(user, message); }

            public boolean shouldActivate(String name, String msg) {
                if (msg.startsWith("/away ")) return true;
                for (String away : awayStatuses.keySet()) {
                    if (msg.contains(away)) return true;
                }
                return false;
            }

            public List<String> execute(String name, String msg) {
                if (msg.startsWith("/away ")) {
                    setAway(name, msg.substring(6)); // everything after "/away "
                    return List.of();                // setting status produces no message
                }
                List<String> out = new ArrayList<>();
                for (Map.Entry<String, String> e : awayStatuses.entrySet()) {
                    if (msg.contains(e.getKey())) {
                        out.add("AwayBot: " + e.getKey() + " is away: " + e.getValue());
                    }
                }
                return out;
            }
        }

        static final class MeetBot implements IBotService {
            final AwayBot awayBot; // injected dependency

            MeetBot(AwayBot awayBot) { this.awayBot = awayBot; }

            public boolean shouldActivate(String name, String msg) {
                return msg.startsWith("/meet ");
            }

            public List<String> execute(String name, String msg) {
                String[] parts = msg.split(" ", 2);
                if (parts.length < 2 || parts[1].isEmpty()) return List.of(); // empty /meet guard
                String other = parts[1];
                String resp = "MeetBot: Google Meet with @" + name + ", and " + other
                        + " starting at https://meet.google.com/abc-def-123";
                if (awayBot != null) {
                    awayBot.setAway(name, "@" + name + " may be in a meeting right now");
                    awayBot.setAway(other, "@" + other + " may be in a meeting right now");
                }
                return List.of(resp);
            }
        }

        static final class TacoBot implements IBotService {
            final LinkedHashMap<String, Integer> tacoCounts = new LinkedHashMap<>();

            public boolean shouldActivate(String name, String msg) {
                return msg.startsWith("/givetaco ");
            }

            public List<String> execute(String name, String msg) {
                String[] parts = msg.split(" ");
                if (parts.length < 3) return List.of();
                String tacoStr = parts[1];
                String recipient = parts[2];
                int num = tacoStr.codePointCount(0, tacoStr.length()); // count emoji
                String key = recipient.startsWith("@") ? recipient.substring(1) : recipient;
                tacoCounts.merge(key, num, Integer::sum);
                // `recipient` is printed verbatim in both spots; "@" + "@justin" -> "@@justin".
                String resp = "TacoBot: @" + name + " gave @" + recipient + " " + num
                        + " 🌮's. " + recipient + " now have " + tacoCounts.get(key) + " 🌮s.";
                return List.of(resp);
            }
        }

        public static void main(String[] args) {
            AwayBot away = new AwayBot();
            MeetBot meet = new MeetBot(away); // DI: MeetBot -> AwayBot
            TacoBot taco = new TacoBot();

            ChatRoom room = new ChatRoom();
            room.registerBot(away); // order matters: away before meet
            room.registerBot(meet);
            room.registerBot(taco);

            room.sendMessage("Alice", "Hello");
            room.sendMessage("Bob", "Hi");
            room.sendMessage("Alice", "Nice job on your presentations");
            room.sendMessage("Cindy", "/givetaco 🌮🌮 @justin");
            room.sendMessage("Alice", "Bob let's meet");
            room.sendMessage("Bob", "/meet Alice");
            room.sendMessage("David", "/away out for lunch");
            room.sendMessage("Emily", "Anyone around?");
            room.sendMessage("Frank", "/meet David");

            List<String> expected = List.of(
                    "Alice: Hello",
                    "Bob: Hi",
                    "Alice: Nice job on your presentations",
                    "Cindy: /givetaco 🌮🌮 @justin",
                    "TacoBot: @Cindy gave @@justin 2 🌮's. @justin now have 2 🌮s.",
                    "Alice: Bob let's meet",
                    "Bob: /meet Alice",
                    "MeetBot: Google Meet with @Bob, and Alice starting at https://meet.google.com/abc-def-123",
                    "David: /away out for lunch",
                    "Emily: Anyone around?",
                    "Frank: /meet David",
                    "AwayBot: David is away: out for lunch",
                    "MeetBot: Google Meet with @Frank, and David starting at https://meet.google.com/abc-def-123");

            assertEquals(expected, room.messages, "Part2 canonical 13-line output");
            System.out.println("Part2: PASS (interface + DI reproduces canonical output)");
        }

        private static void check(boolean cond, String label) {
            if (!cond) throw new AssertionError(label);
        }
        private static void assertEquals(Object expected, Object actual, String label) {
            check(Objects.equals(expected, actual),
                    label + " | expected=" + expected + " actual=" + actual);
        }
    }

    // ========================================================================
    //  PART 3 — Event-driven architecture (publish/subscribe EventBus).
    // ========================================================================
    //
    //  CHANGES FROM PART 2:
    //    * Removed the direct MeetBot -> AwayBot dependency; bots hold NO
    //      references to each other.
    //    * Added an `Event` value type and an `EventBus` (subscribe/publish);
    //      `ChatRoom` owns the bus.
    //    * `IBotService` is now an abstract class holding the bus plus a
    //      `setupSubscriptions()` lifecycle hook. AwayBot subscribes to
    //      "user_meeting_started" and sets away status in its handler; MeetBot
    //      publishes that event for both participants; TacoBot publishes
    //      "tacos_given" (no subscribers — demonstrates the audit-trail benefit).
    //    * Observable behavior for the canonical conversation is unchanged.
    //
    //  Safety note: `setupSubscriptions()` only REGISTERS handler lambdas. The
    //  handler bodies (which read bot state) execute later, at publish time,
    //  after field initializers have run — so no uninitialized-field hazard.
    //
    static final class Part3 {

        record Event(String type, Map<String, Object> data) {}

        static final class EventBus {
            final Map<String, List<Consumer<Event>>> subscribers = new HashMap<>();

            void subscribe(String type, Consumer<Event> handler) {
                subscribers.computeIfAbsent(type, k -> new ArrayList<>()).add(handler);
            }

            void publish(Event e) {
                List<Consumer<Event>> handlers = subscribers.get(e.type());
                if (handlers != null) {
                    for (Consumer<Event> h : handlers) h.accept(e);
                }
            }
        }

        static abstract class IBotService {
            protected final EventBus eventBus;

            protected IBotService(EventBus eventBus) {
                this.eventBus = eventBus;
                setupSubscriptions();
            }

            /** Override to subscribe to events. Only registers handlers. */
            protected void setupSubscriptions() {}

            abstract boolean shouldActivate(String name, String msg);
            abstract List<String> execute(String name, String msg);
        }

        static final class AwayBot extends IBotService {
            final LinkedHashMap<String, String> awayStatuses = new LinkedHashMap<>();

            AwayBot(EventBus bus) { super(bus); }

            @Override
            protected void setupSubscriptions() {
                eventBus.subscribe("user_meeting_started", this::handleMeetingStarted);
            }

            void handleMeetingStarted(Event e) {
                String username = (String) e.data().get("username");
                awayStatuses.put(username, "@" + username + " may be in a meeting right now");
            }

            @Override
            boolean shouldActivate(String name, String msg) {
                if (msg.startsWith("/away ")) return true;
                for (String away : awayStatuses.keySet()) {
                    if (msg.contains(away)) return true;
                }
                return false;
            }

            @Override
            List<String> execute(String name, String msg) {
                if (msg.startsWith("/away ")) {
                    String awayMessage = msg.substring(6);
                    awayStatuses.put(name, awayMessage);
                    eventBus.publish(new Event("user_away_status_changed",
                            Map.of("username", name, "message", awayMessage)));
                    return List.of();
                }
                List<String> out = new ArrayList<>();
                for (Map.Entry<String, String> e : awayStatuses.entrySet()) {
                    if (msg.contains(e.getKey())) {
                        out.add("AwayBot: " + e.getKey() + " is away: " + e.getValue());
                    }
                }
                return out;
            }
        }

        static final class MeetBot extends IBotService {
            MeetBot(EventBus bus) { super(bus); }

            @Override
            boolean shouldActivate(String name, String msg) { return msg.startsWith("/meet "); }

            @Override
            List<String> execute(String name, String msg) {
                String[] parts = msg.split(" ", 2);
                if (parts.length < 2 || parts[1].isEmpty()) return List.of();
                String other = parts[1];
                // Publish for the giver first, then the other participant.
                eventBus.publish(new Event("user_meeting_started", Map.of("username", name)));
                eventBus.publish(new Event("user_meeting_started", Map.of("username", other)));
                return List.of("MeetBot: Google Meet with @" + name + ", and " + other
                        + " starting at https://meet.google.com/abc-def-123");
            }
        }

        static final class TacoBot extends IBotService {
            final LinkedHashMap<String, Integer> tacoCounts = new LinkedHashMap<>();

            TacoBot(EventBus bus) { super(bus); }

            @Override
            boolean shouldActivate(String name, String msg) { return msg.startsWith("/givetaco "); }

            @Override
            List<String> execute(String name, String msg) {
                String[] parts = msg.split(" ");
                if (parts.length < 3) return List.of();
                String tacoStr = parts[1];
                String recipient = parts[2];
                int num = tacoStr.codePointCount(0, tacoStr.length());
                String key = recipient.startsWith("@") ? recipient.substring(1) : recipient;
                tacoCounts.merge(key, num, Integer::sum);
                eventBus.publish(new Event("tacos_given",
                        Map.of("giver", name, "recipient", key, "count", num, "total", tacoCounts.get(key))));
                return List.of("TacoBot: @" + name + " gave @" + recipient + " " + num
                        + " 🌮's. " + recipient + " now have " + tacoCounts.get(key) + " 🌮s.");
            }
        }

        static final class ChatRoom {
            final EventBus eventBus = new EventBus();
            final List<IBotService> bots = new ArrayList<>();
            final List<String> messages = new ArrayList<>();

            void registerBot(IBotService b) { bots.add(b); }

            void sendMessage(String name, String msg) {
                messages.add(name + ": " + msg);
                for (IBotService b : bots) {
                    if (b.shouldActivate(name, msg)) {
                        messages.addAll(b.execute(name, msg));
                    }
                }
            }
        }

        public static void main(String[] args) {
            ChatRoom room = new ChatRoom();
            AwayBot away = new AwayBot(room.eventBus);
            MeetBot meet = new MeetBot(room.eventBus);
            TacoBot taco = new TacoBot(room.eventBus);
            room.registerBot(away);
            room.registerBot(meet);
            room.registerBot(taco);

            room.sendMessage("Alice", "Hello");
            room.sendMessage("Bob", "Hi");
            room.sendMessage("Alice", "Nice job on your presentations");
            room.sendMessage("Cindy", "/givetaco 🌮🌮 @justin");
            room.sendMessage("Alice", "Bob let's meet");
            room.sendMessage("Bob", "/meet Alice");
            room.sendMessage("David", "/away out for lunch");
            room.sendMessage("Emily", "Anyone around?");
            room.sendMessage("Frank", "/meet David");

            List<String> expected = List.of(
                    "Alice: Hello",
                    "Bob: Hi",
                    "Alice: Nice job on your presentations",
                    "Cindy: /givetaco 🌮🌮 @justin",
                    "TacoBot: @Cindy gave @@justin 2 🌮's. @justin now have 2 🌮s.",
                    "Alice: Bob let's meet",
                    "Bob: /meet Alice",
                    "MeetBot: Google Meet with @Bob, and Alice starting at https://meet.google.com/abc-def-123",
                    "David: /away out for lunch",
                    "Emily: Anyone around?",
                    "Frank: /meet David",
                    "AwayBot: David is away: out for lunch",
                    "MeetBot: Google Meet with @Frank, and David starting at https://meet.google.com/abc-def-123");

            assertEquals(expected, room.messages, "Part3 canonical 13-line output");
            System.out.println("Part3: PASS (event bus reproduces canonical output)");
        }

        private static void check(boolean cond, String label) {
            if (!cond) throw new AssertionError(label);
        }
        private static void assertEquals(Object expected, Object actual, String label) {
            check(Objects.equals(expected, actual),
                    label + " | expected=" + expected + " actual=" + actual);
        }
    }

    // ========================================================================
    //  PART 4 — Test cases (dependency-free; no JUnit).
    // ========================================================================
    //
    //  CHANGES FROM PART 3:
    //    * No production-logic change. This part DUPLICATES the Part-3
    //      event-driven bots verbatim (Event, EventBus, IBotService, AwayBot,
    //      MeetBot, TacoBot, ChatRoom) and adds a tiny assertion harness plus a
    //      suite of unit, integration and edge-case tests — the question's
    //      Part-4 tests translated from Python `unittest` to plain Java.
    //    * AwayBot gains test accessors `isAway` / `awayMessage`.
    //    * Each unit test builds fresh bots + a fresh bus, so no state leaks
    //      between tests.
    //
    static final class Part4 {

        // ---- duplicated production types (same as Part 3) ----
        record Event(String type, Map<String, Object> data) {}

        static final class EventBus {
            final Map<String, List<Consumer<Event>>> subscribers = new HashMap<>();
            void subscribe(String type, Consumer<Event> handler) {
                subscribers.computeIfAbsent(type, k -> new ArrayList<>()).add(handler);
            }
            void publish(Event e) {
                List<Consumer<Event>> handlers = subscribers.get(e.type());
                if (handlers != null) for (Consumer<Event> h : handlers) h.accept(e);
            }
        }

        static abstract class IBotService {
            protected final EventBus eventBus;
            protected IBotService(EventBus eventBus) { this.eventBus = eventBus; setupSubscriptions(); }
            protected void setupSubscriptions() {}
            abstract boolean shouldActivate(String name, String msg);
            abstract List<String> execute(String name, String msg);
        }

        static final class AwayBot extends IBotService {
            final LinkedHashMap<String, String> awayStatuses = new LinkedHashMap<>();
            AwayBot(EventBus bus) { super(bus); }
            @Override protected void setupSubscriptions() {
                eventBus.subscribe("user_meeting_started", this::handleMeetingStarted);
            }
            void handleMeetingStarted(Event e) {
                String u = (String) e.data().get("username");
                awayStatuses.put(u, "@" + u + " may be in a meeting right now");
            }
            // test accessors
            boolean isAway(String u) { return awayStatuses.containsKey(u); }
            String awayMessage(String u) { return awayStatuses.get(u); }

            @Override boolean shouldActivate(String name, String msg) {
                if (msg.startsWith("/away ")) return true;
                for (String away : awayStatuses.keySet()) if (msg.contains(away)) return true;
                return false;
            }
            @Override List<String> execute(String name, String msg) {
                if (msg.startsWith("/away ")) {
                    awayStatuses.put(name, msg.substring(6));
                    return List.of();
                }
                List<String> out = new ArrayList<>();
                for (Map.Entry<String, String> e : awayStatuses.entrySet())
                    if (msg.contains(e.getKey()))
                        out.add("AwayBot: " + e.getKey() + " is away: " + e.getValue());
                return out;
            }
        }

        static final class MeetBot extends IBotService {
            MeetBot(EventBus bus) { super(bus); }
            @Override boolean shouldActivate(String name, String msg) { return msg.startsWith("/meet "); }
            @Override List<String> execute(String name, String msg) {
                String[] parts = msg.split(" ", 2);
                if (parts.length < 2 || parts[1].isEmpty()) return List.of();
                String other = parts[1];
                eventBus.publish(new Event("user_meeting_started", Map.of("username", name)));
                eventBus.publish(new Event("user_meeting_started", Map.of("username", other)));
                return List.of("MeetBot: Google Meet with @" + name + ", and " + other
                        + " starting at https://meet.google.com/abc-def-123");
            }
        }

        static final class TacoBot extends IBotService {
            final LinkedHashMap<String, Integer> tacoCounts = new LinkedHashMap<>();
            TacoBot(EventBus bus) { super(bus); }
            @Override boolean shouldActivate(String name, String msg) { return msg.startsWith("/givetaco "); }
            @Override List<String> execute(String name, String msg) {
                String[] parts = msg.split(" ");
                if (parts.length < 3) return List.of();
                String tacoStr = parts[1], recipient = parts[2];
                int num = tacoStr.codePointCount(0, tacoStr.length());
                String key = recipient.startsWith("@") ? recipient.substring(1) : recipient;
                tacoCounts.merge(key, num, Integer::sum);
                return List.of("TacoBot: @" + name + " gave @" + recipient + " " + num
                        + " 🌮's. " + recipient + " now have " + tacoCounts.get(key) + " 🌮s.");
            }
        }

        static final class ChatRoom {
            final EventBus eventBus = new EventBus();
            final List<IBotService> bots = new ArrayList<>();
            final List<String> messages = new ArrayList<>();
            void registerBot(IBotService b) { bots.add(b); }
            void sendMessage(String name, String msg) {
                messages.add(name + ": " + msg);
                for (IBotService b : bots)
                    if (b.shouldActivate(name, msg)) messages.addAll(b.execute(name, msg));
            }
        }

        // ---- tests ----
        static void testTacoActivate() {
            TacoBot taco = new TacoBot(new EventBus());
            assertTrue(taco.shouldActivate("Alice", "/givetaco 🌮🌮 @bob"), "taco activates on command");
            assertFalse(taco.shouldActivate("Alice", "Hello"), "taco ignores plain text");
        }
        static void testTacoExecute() {
            TacoBot taco = new TacoBot(new EventBus());
            List<String> r = taco.execute("Alice", "/givetaco 🌮🌮🌮 @bob");
            check(r.size() == 1, "taco execute returns one line");
            assertContains(r.get(0), "3 🌮's", "taco counts 3 emoji");
            assertContains(r.get(0), "now have 3 🌮s", "taco total is 3");
        }
        static void testTacoAccumulate() {
            TacoBot taco = new TacoBot(new EventBus());
            taco.execute("Alice", "/givetaco 🌮🌮 @bob");
            List<String> r = taco.execute("Charlie", "/givetaco 🌮 @bob");
            assertContains(r.get(0), "now have 3 🌮s", "taco accumulates to 3");
        }
        static void testMeetLink() {
            MeetBot meet = new MeetBot(new EventBus());
            List<String> r = meet.execute("Alice", "/meet Bob");
            check(r.size() == 1, "meet returns one line");
            assertContains(r.get(0), "Google Meet with @Alice, and Bob", "meet link text");
            assertContains(r.get(0), "https://meet.google.com", "meet link url");
        }
        static void testMeetPublishes() {
            EventBus bus = new EventBus();
            List<Event> captured = new ArrayList<>();
            bus.subscribe("user_meeting_started", captured::add);
            MeetBot meet = new MeetBot(bus);
            meet.execute("Alice", "/meet Bob");
            check(captured.size() == 2, "meet publishes two events");
            assertEquals("Alice", captured.get(0).data().get("username"), "first event = Alice");
            assertEquals("Bob", captured.get(1).data().get("username"), "second event = Bob");
        }
        static void testAwaySet() {
            AwayBot away = new AwayBot(new EventBus());
            List<String> r = away.execute("David", "/away out for lunch");
            check(r.isEmpty(), "/away produces no output");
            assertTrue(away.isAway("David"), "David is recorded as away");
        }
        static void testAwayNotify() {
            AwayBot away = new AwayBot(new EventBus());
            away.execute("David", "/away out for lunch");
            List<String> r = away.execute("Alice", "Hey David, are you around?");
            check(r.size() == 1, "one away notification");
            assertContains(r.get(0), "David is away", "notification mentions David");
        }
        static void testAwayHandlesEvent() {
            AwayBot away = new AwayBot(new EventBus());
            away.handleMeetingStarted(new Event("user_meeting_started", Map.of("username", "Alice")));
            assertTrue(away.isAway("Alice"), "meeting event marks Alice away");
            assertContains(away.awayMessage("Alice"), "may be in a meeting", "meeting away message");
        }
        static void testFullConversationFlow() {
            ChatRoom room = new ChatRoom();
            AwayBot away = new AwayBot(room.eventBus);
            MeetBot meet = new MeetBot(room.eventBus);
            TacoBot taco = new TacoBot(room.eventBus);
            room.registerBot(away);
            room.registerBot(meet);
            room.registerBot(taco);

            room.sendMessage("Alice", "Hello");
            room.sendMessage("Bob", "/meet Alice");
            room.sendMessage("Charlie", "Hey Bob");

            assertTrue(room.messages.contains("Alice: Hello"), "has Alice's message");
            assertTrue(room.messages.contains("Bob: /meet Alice"), "has Bob's message");
            check(room.messages.stream().anyMatch(m -> m.contains("MeetBot") && m.contains("Google Meet")),
                    "MeetBot announced a meeting");
            // Bob's /meet published user_meeting_started for Bob, so AwayBot now
            // considers Bob away; Charlie's "Hey Bob" triggers the notification.
            check(room.messages.stream().anyMatch(m -> m.contains("AwayBot") && m.contains("Bob is away")),
                    "AwayBot notified that Bob is away (in a meeting)");
        }
        static void testEmptyMeet() {
            MeetBot meet = new MeetBot(new EventBus());
            assertFalse(meet.shouldActivate("Alice", "/meet"), "bare /meet does not activate");
            check(meet.execute("Alice", "/meet").isEmpty(), "bare /meet produces no output");
        }
        static void testMultipleDistinctAway() {
            AwayBot away = new AwayBot(new EventBus());
            away.execute("Alice", "/away lunch");
            away.execute("Bob", "/away meeting");
            List<String> r = away.execute("Charlie", "Hey Alice and Bob");
            check(r.size() == 2, "two distinct away users -> two notifications");
        }
        static void testSameAwayUserTwice() {
            AwayBot away = new AwayBot(new EventBus());
            away.execute("Alice", "/away brb");
            List<String> r = away.execute("Bob", "Alice told me Alice is away");
            check(r.size() == 1, "same away user mentioned twice -> one notification");
        }

        public static void main(String[] args) {
            testTacoActivate();
            testTacoExecute();
            testTacoAccumulate();
            testMeetLink();
            testMeetPublishes();
            testAwaySet();
            testAwayNotify();
            testAwayHandlesEvent();
            testFullConversationFlow();
            testEmptyMeet();
            testMultipleDistinctAway();
            testSameAwayUserTwice();
            System.out.println("Part4: PASS (12 unit/integration/edge tests)");
        }

        // ---- assertion harness ----
        private static void check(boolean cond, String label) {
            if (!cond) throw new AssertionError(label);
        }
        private static void assertTrue(boolean cond, String label) { check(cond, label); }
        private static void assertFalse(boolean cond, String label) { check(!cond, label); }
        private static void assertEquals(Object expected, Object actual, String label) {
            check(Objects.equals(expected, actual),
                    label + " | expected=" + expected + " actual=" + actual);
        }
        private static void assertContains(String haystack, String needle, String label) {
            check(haystack != null && haystack.contains(needle),
                    label + " | '" + needle + "' not found in '" + haystack + "'");
        }
    }

    // ========================================================================
    //  PART 5 — Extensions, folded into one cohesive design.
    // ========================================================================
    //
    //  CHANGES FROM PART 4:
    //    Builds on the Part-3 event-driven bots (duplicated again) and adds, in
    //    one coherent pipeline, all four follow-ups:
    //      (1) Command validation via a `Command` value object (graceful errors).
    //      (2) Bot priority/ordering: `IBotService.priority()`; `ChatRoom` sorts
    //          bots ascending by priority before iterating. Priorities are
    //          AwayBot(10) < MeetBot(20) < TacoBot(30) so the canonical ordering
    //          holds even when bots are registered in a scrambled order.
    //      (3) Middleware pipeline: a `Middleware` chain (Logging, RateLimit)
    //          wrapping `sendMessage`.
    //      (4) Async event processing: an `AsyncEventBus` whose `publish` returns
    //          a CompletableFuture completing when all handlers finish; bot
    //          `executeAsync` returns a future. The room `.join()`s for
    //          deterministic output so the canonical transcript is reproducible.
    //
    static final class Part5 {

        /** Value object for a parsed slash command. */
        record Command(String raw, boolean isCommand, String name, String args) {
            static Command parse(String raw) {
                boolean isCmd = raw.startsWith("/");
                if (isCmd) {
                    String[] parts = raw.substring(1).split(" ", 2);
                    return new Command(raw, true, parts[0], parts.length > 1 ? parts[1] : "");
                }
                return new Command(raw, false, null, null);
            }
            boolean validateFormat(int expectedArgCount) {
                if (!isCommand) return false;
                String[] argParts = (args == null || args.isEmpty())
                        ? new String[0] : args.trim().split("\\s+");
                return argParts.length >= expectedArgCount;
            }
        }

        record Event(String type, Map<String, Object> data) {}

        /** Event bus whose publish completes when all (possibly async) handlers do. */
        static final class AsyncEventBus {
            final Map<String, List<Function<Event, CompletableFuture<Void>>>> subscribers = new HashMap<>();

            void subscribe(String type, Function<Event, CompletableFuture<Void>> handler) {
                subscribers.computeIfAbsent(type, k -> new ArrayList<>()).add(handler);
            }
            /** Convenience for synchronous handlers. */
            void subscribeSync(String type, Consumer<Event> handler) {
                subscribe(type, e -> { handler.accept(e); return CompletableFuture.completedFuture(null); });
            }
            CompletableFuture<Void> publish(Event e) {
                List<Function<Event, CompletableFuture<Void>>> handlers = subscribers.get(e.type());
                if (handlers == null || handlers.isEmpty()) return CompletableFuture.completedFuture(null);
                CompletableFuture<?>[] futures = handlers.stream()
                        .map(h -> h.apply(e)).toArray(CompletableFuture[]::new);
                return CompletableFuture.allOf(futures);
            }
        }

        static abstract class IBotService {
            protected final AsyncEventBus eventBus;
            protected IBotService(AsyncEventBus eventBus) { this.eventBus = eventBus; setupSubscriptions(); }
            protected void setupSubscriptions() {}
            abstract int priority();                 // lower runs first
            abstract boolean shouldActivate(String name, String msg);
            abstract CompletableFuture<List<String>> executeAsync(String name, String msg);
        }

        static final class AwayBot extends IBotService {
            final LinkedHashMap<String, String> awayStatuses = new LinkedHashMap<>();
            AwayBot(AsyncEventBus bus) { super(bus); }
            @Override protected void setupSubscriptions() {
                eventBus.subscribeSync("user_meeting_started", e -> {
                    String u = (String) e.data().get("username");
                    awayStatuses.put(u, "@" + u + " may be in a meeting right now");
                });
            }
            @Override int priority() { return 10; }
            @Override boolean shouldActivate(String name, String msg) {
                if (msg.startsWith("/away ")) return true;
                for (String away : awayStatuses.keySet()) if (msg.contains(away)) return true;
                return false;
            }
            @Override CompletableFuture<List<String>> executeAsync(String name, String msg) {
                if (msg.startsWith("/away ")) {
                    awayStatuses.put(name, msg.substring(6));
                    return CompletableFuture.completedFuture(List.of());
                }
                List<String> out = new ArrayList<>();
                for (Map.Entry<String, String> e : awayStatuses.entrySet())
                    if (msg.contains(e.getKey()))
                        out.add("AwayBot: " + e.getKey() + " is away: " + e.getValue());
                return CompletableFuture.completedFuture(out);
            }
        }

        static final class MeetBot extends IBotService {
            MeetBot(AsyncEventBus bus) { super(bus); }
            @Override int priority() { return 20; }
            @Override boolean shouldActivate(String name, String msg) { return msg.startsWith("/meet "); }
            @Override CompletableFuture<List<String>> executeAsync(String name, String msg) {
                String[] parts = msg.split(" ", 2);
                if (parts.length < 2 || parts[1].isEmpty()) return CompletableFuture.completedFuture(List.of());
                String other = parts[1];
                // Publish meeting-started for both; join so away state is set before we return.
                eventBus.publish(new Event("user_meeting_started", Map.of("username", name))).join();
                eventBus.publish(new Event("user_meeting_started", Map.of("username", other))).join();
                return CompletableFuture.completedFuture(List.of(
                        "MeetBot: Google Meet with @" + name + ", and " + other
                                + " starting at https://meet.google.com/abc-def-123"));
            }
        }

        static final class TacoBot extends IBotService {
            final LinkedHashMap<String, Integer> tacoCounts = new LinkedHashMap<>();
            TacoBot(AsyncEventBus bus) { super(bus); }
            @Override int priority() { return 30; }
            @Override boolean shouldActivate(String name, String msg) { return msg.startsWith("/givetaco "); }
            @Override CompletableFuture<List<String>> executeAsync(String name, String msg) {
                Command cmd = Command.parse(msg);
                if (!cmd.validateFormat(2)) {
                    return CompletableFuture.completedFuture(
                            List.of("TacoBot: Invalid command. Usage: /givetaco <tacos> <@username>"));
                }
                String[] parts = msg.split(" ");
                String tacoStr = parts[1], recipient = parts[2];
                int num = tacoStr.codePointCount(0, tacoStr.length());
                String key = recipient.startsWith("@") ? recipient.substring(1) : recipient;
                tacoCounts.merge(key, num, Integer::sum);
                eventBus.publish(new Event("tacos_given",
                        Map.of("giver", name, "recipient", key, "count", num, "total", tacoCounts.get(key))));
                return CompletableFuture.completedFuture(List.of(
                        "TacoBot: @" + name + " gave @" + recipient + " " + num
                                + " 🌮's. " + recipient + " now have " + tacoCounts.get(key) + " 🌮s."));
            }
        }

        /** Request middleware: each may short-circuit or delegate to `next`. */
        interface Middleware {
            CompletableFuture<List<String>> processMessage(
                    String name, String msg,
                    BiFunction<String, String, CompletableFuture<List<String>>> next);
        }

        static final class LoggingMiddleware implements Middleware {
            final List<String> log;
            LoggingMiddleware(List<String> log) { this.log = log; }
            public CompletableFuture<List<String>> processMessage(
                    String name, String msg,
                    BiFunction<String, String, CompletableFuture<List<String>>> next) {
                log.add("[LOG] " + name + ": " + msg);
                return next.apply(name, msg);
            }
        }

        static final class RateLimitMiddleware implements Middleware {
            final int limit;
            final Map<String, Integer> counts = new HashMap<>();
            RateLimitMiddleware(int limit) { this.limit = limit; }
            public CompletableFuture<List<String>> processMessage(
                    String name, String msg,
                    BiFunction<String, String, CompletableFuture<List<String>>> next) {
                if (counts.merge(name, 1, Integer::sum) > limit) {
                    return CompletableFuture.completedFuture(List.of("Rate limit exceeded. Please wait."));
                }
                return next.apply(name, msg);
            }
        }

        static final class ChatRoom {
            final AsyncEventBus eventBus = new AsyncEventBus();
            final List<IBotService> bots = new ArrayList<>();
            final List<Middleware> middleware = new ArrayList<>();
            final List<String> messages = new ArrayList<>();

            void registerBot(IBotService b) { bots.add(b); }
            void use(Middleware m) { middleware.add(m); }

            void sendMessage(String name, String msg) {
                // Terminal handler: builds the user line + priority-ordered bot
                // responses. Returning the whole list (rather than mutating
                // `messages` here) lets a short-circuiting middleware substitute
                // its own response (e.g. a rate-limit notice) cleanly.
                BiFunction<String, String, CompletableFuture<List<String>>> core = (n, m) -> {
                    List<String> out = new ArrayList<>();
                    out.add(n + ": " + m);
                    List<IBotService> sorted = new ArrayList<>(bots);
                    sorted.sort(Comparator.comparingInt(IBotService::priority));
                    for (IBotService b : sorted) {
                        if (b.shouldActivate(n, m)) {
                            out.addAll(b.executeAsync(n, m).join());
                        }
                    }
                    return CompletableFuture.completedFuture(out);
                };
                // Wrap the middleware around `core`, last registered = innermost.
                BiFunction<String, String, CompletableFuture<List<String>>> chain = core;
                for (int i = middleware.size() - 1; i >= 0; i--) {
                    Middleware mw = middleware.get(i);
                    BiFunction<String, String, CompletableFuture<List<String>>> next = chain;
                    chain = (n, m) -> mw.processMessage(n, m, next);
                }
                messages.addAll(chain.apply(name, msg).join());
            }
        }

        public static void main(String[] args) {
            // --- (1) Command validation ---
            TacoBot validationTaco = new TacoBot(new AsyncEventBus());
            assertEquals(
                    List.of("TacoBot: Invalid command. Usage: /givetaco <tacos> <@username>"),
                    validationTaco.executeAsync("Alice", "/givetaco").join(),
                    "malformed /givetaco -> usage error");
            assertTrue(Command.parse("/givetaco 🌮 @bob").validateFormat(2), "valid taco command");
            assertFalse(Command.parse("/givetaco").validateFormat(2), "missing args -> invalid");

            // --- (2) Bot priority/ordering: register in WRONG order, expect canonical output ---
            ChatRoom room = new ChatRoom();
            AwayBot away = new AwayBot(room.eventBus);
            MeetBot meet = new MeetBot(room.eventBus);
            TacoBot taco = new TacoBot(room.eventBus);
            room.registerBot(taco); // scrambled: taco, meet, away
            room.registerBot(meet);
            room.registerBot(away);

            room.sendMessage("Alice", "Hello");
            room.sendMessage("Bob", "Hi");
            room.sendMessage("Alice", "Nice job on your presentations");
            room.sendMessage("Cindy", "/givetaco 🌮🌮 @justin");
            room.sendMessage("Alice", "Bob let's meet");
            room.sendMessage("Bob", "/meet Alice");
            room.sendMessage("David", "/away out for lunch");
            room.sendMessage("Emily", "Anyone around?");
            room.sendMessage("Frank", "/meet David");

            List<String> expected = List.of(
                    "Alice: Hello",
                    "Bob: Hi",
                    "Alice: Nice job on your presentations",
                    "Cindy: /givetaco 🌮🌮 @justin",
                    "TacoBot: @Cindy gave @@justin 2 🌮's. @justin now have 2 🌮s.",
                    "Alice: Bob let's meet",
                    "Bob: /meet Alice",
                    "MeetBot: Google Meet with @Bob, and Alice starting at https://meet.google.com/abc-def-123",
                    "David: /away out for lunch",
                    "Emily: Anyone around?",
                    "Frank: /meet David",
                    "AwayBot: David is away: out for lunch",
                    "MeetBot: Google Meet with @Frank, and David starting at https://meet.google.com/abc-def-123");
            assertEquals(expected, room.messages, "Part5 priority-sorted canonical output");

            // --- (3) Middleware pipeline (separate room so it doesn't perturb the above) ---
            ChatRoom r2 = new ChatRoom();
            List<String> log = new ArrayList<>();
            r2.use(new LoggingMiddleware(log));
            r2.use(new RateLimitMiddleware(2)); // 3rd message from a user is blocked
            r2.sendMessage("Alice", "Hi");
            r2.sendMessage("Alice", "Hello");
            r2.sendMessage("Alice", "Third");
            assertTrue(log.contains("[LOG] Alice: Hi"), "logging middleware recorded the message");
            check(r2.messages.contains("Rate limit exceeded. Please wait."),
                    "rate-limit middleware blocked the 3rd message");

            // --- (4) Async event bus fan-out ---
            AsyncEventBus bus = new AsyncEventBus();
            List<Event> captured = Collections.synchronizedList(new ArrayList<>());
            bus.subscribeSync("user_meeting_started", captured::add);
            MeetBot asyncMeet = new MeetBot(bus);
            asyncMeet.executeAsync("Alice", "/meet Bob").join();
            check(captured.size() == 2, "async bus delivered two events");
            assertEquals("Alice", captured.get(0).data().get("username"), "async first = Alice");
            assertEquals("Bob", captured.get(1).data().get("username"), "async second = Bob");

            System.out.println("Part5: PASS (validation, priority, middleware, async-bus)");
        }

        // ---- assertion harness ----
        private static void check(boolean cond, String label) {
            if (!cond) throw new AssertionError(label);
        }
        private static void assertTrue(boolean cond, String label) { check(cond, label); }
        private static void assertFalse(boolean cond, String label) { check(!cond, label); }
        private static void assertEquals(Object expected, Object actual, String label) {
            check(Objects.equals(expected, actual),
                    label + " | expected=" + expected + " actual=" + actual);
        }
    }
}
