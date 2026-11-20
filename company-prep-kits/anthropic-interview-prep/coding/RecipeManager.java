import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Recipe Manager — 4-level OA (CodeSignal style), idiomatic Java translation.
 *
 * This single file is the FINAL, integrated solution (Level 4 state). In the real
 * assessment you implement incrementally; section banners below mark which methods
 * belong to which level, and the few behavioural changes between levels are called out.
 *
 * ---------------------------------------------------------------------------------
 * CORE DESIGN (the whole problem collapses onto three maps + a versioned recipe)
 * ---------------------------------------------------------------------------------
 *  recipes  : id -> Recipe, where a Recipe holds an append-only List<Version>.
 *             The CURRENT state is always versions.get(last).
 *  nameToId : lower-cased CURRENT name -> recipe id.  This is the trick that makes
 *             every uniqueness check O(1) AND makes the rollback-conflict check a
 *             one-liner.  INVARIANT: nameToId contains exactly the set of *current*
 *             recipe names — never historical ones. Old versions do NOT reserve names.
 *  users    : id -> username (Level 3+).
 *
 * KEY CORRECTNESS RULES (the things that fail people):
 *  1. Case-insensitive name uniqueness EVERYWHERE — compare on lower-cased keys
 *     (Locale.ROOT to avoid the Turkish-'I' bug). Original casing is stored verbatim.
 *  2. Validate EVERYTHING before mutating. A rejected op must leave zero side effects
 *     (no partially-applied name map, no half-appended version).
 *  3. rollback APPENDS a copy of an old version as the new newest version. It never
 *     truncates history. Version numbers are 1-based and never reset / reused.
 *  4. rollback conflict == the old version's name is currently owned by a DIFFERENT
 *     recipe. A recipe never conflicts with its own earlier versions (self allowed).
 *
 * LEVEL 4 BEHAVIOURAL CHANGE: in Levels 1–3, update/edit OVERWRITE the recipe; in
 * Level 4 every mutation APPENDS a new version. Both paths funnel through
 * commitVersion(), so the change lives in exactly one place.
 *
 * SOURCE AMBIGUITY (documented, not guessed): the DarkInterview sample leaves
 * update_recipe overwriting in L4 and only versions via edit_recipe; the 1point3acres
 * write-up says BOTH update_recipe and edit_recipe append in L4. I follow the
 * data-preserving 1point3acres reading (both append) — it is the consistent semantic
 * and is strictly safer if a hidden L4 test drives update_recipe. To revert to the
 * DarkInterview reading, make updateRecipe overwrite instead of calling commitVersion.
 */
public class RecipeManager {

    // ============================ internal model ============================

    /** One immutable snapshot of a recipe (a single point in its history). */
    private static final class Version {
        final String name;
        final List<String> ingredients; // stored as an unmodifiable copy (defensive)

        Version(String name, List<String> ingredients) {
            this.name = name;
            this.ingredients = (ingredients == null) ? List.of() : List.copyOf(ingredients);
        }
    }

    /** A recipe is its id plus an append-only list of versions; current == last. */
    private static final class Recipe {
        final String id;
        final List<Version> versions = new ArrayList<>();

        Recipe(String id) { this.id = id; }
        Version current() { return versions.get(versions.size() - 1); }
    }

    // ===================== public, immutable return types ===================
    // (The Python signatures return dicts; these typed records are the Java idiom.)

    public record RecipeView(String id, String name, List<String> ingredients) {
        public RecipeView { ingredients = List.copyOf(ingredients); }
    }

    public record RecipeVersion(int version, String name, List<String> ingredients) {
        public RecipeVersion { ingredients = List.copyOf(ingredients); }
    }

    // ================================ state =================================

    private final Map<String, Recipe> recipes  = new LinkedHashMap<>(); // insertion order -> deterministic sort
    private final Map<String, String> nameToId = new HashMap<>();        // lower(currentName) -> id
    private final Map<String, String> users    = new HashMap<>();        // userId -> username

    // =============================== helpers ================================

    /** Canonical, locale-stable key for case-insensitive name comparison. */
    private static String key(String name) { return name.toLowerCase(Locale.ROOT); }

    /** True iff some OTHER recipe currently owns this name (self is always allowed). */
    private boolean nameOwnedByOther(String name, String selfId) {
        String owner = nameToId.get(key(name));
        return owner != null && !owner.equals(selfId);
    }

    /**
     * The single mutation primitive: append {@code (name, ingredients)} as the new
     * current version and re-point the name index. Used by update, edit and rollback,
     * so the "append vs overwrite" decision and the index bookkeeping live in ONE spot.
     * Caller must have already validated the name does not collide with another recipe.
     */
    private void commitVersion(Recipe r, String name, List<String> ingredients) {
        nameToId.remove(key(r.current().name)); // drop the outgoing current-name mapping
        r.versions.add(new Version(name, ingredients));
        nameToId.put(key(name), r.id);           // index the new current name
    }

    /** Materialise the current state of a recipe as an immutable view. */
    private RecipeView view(Recipe r) {
        Version v = r.current();
        return new RecipeView(r.id, v.name, v.ingredients);
    }

    // ====================== Level 2 sort comparator =========================
    // Primary: ingredient count ascending. Secondary: id ascending — numeric ids
    // compared as numbers and ordered before non-numeric ids, which compare lexically.

    private static boolean isDigits(String s) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }

    /** Numeric-aware id comparison; BigInteger avoids overflow on very long digit ids. */
    private static int compareIds(String a, String b) {
        boolean an = isDigits(a), bn = isDigits(b);
        if (an && bn) return new BigInteger(a).compareTo(new BigInteger(b));
        if (an) return -1;          // numeric ids sort before non-numeric
        if (bn) return 1;
        return a.compareTo(b);      // lexicographic for non-numeric ids
    }

    private static final Comparator<RecipeView> SORT =
            Comparator.comparingInt((RecipeView r) -> r.ingredients().size())
                      .thenComparing(RecipeView::id, RecipeManager::compareIds);

    // =========================================================================
    // ============================= LEVEL 1: CRUD =============================
    // =========================================================================

    /** add_recipe: false if id taken OR name (case-insensitive) already in use. */
    public boolean addRecipe(String recipeId, String name, List<String> ingredients) {
        if (recipes.containsKey(recipeId)) return false;   // id already exists
        if (nameToId.containsKey(key(name))) return false; // name already exists (case-insensitive)
        Recipe r = new Recipe(recipeId);
        r.versions.add(new Version(name, ingredients));     // this is version 1
        recipes.put(recipeId, r);
        nameToId.put(key(name), recipeId);
        return true;
    }

    /** get_recipe: current view, or null if missing. */
    public RecipeView getRecipe(String recipeId) {
        Recipe r = recipes.get(recipeId);
        return (r == null) ? null : view(r);
    }

    /**
     * update_recipe: false if missing, or if the new name belongs to a DIFFERENT recipe
     * (self may keep its own name, even with different casing).
     * LEVEL 1–3 semantics = overwrite; LEVEL 4 = append (via commitVersion). See header.
     */
    public boolean updateRecipe(String recipeId, String name, List<String> ingredients) {
        Recipe r = recipes.get(recipeId);
        if (r == null) return false;
        if (nameOwnedByOther(name, recipeId)) return false;
        commitVersion(r, name, ingredients); // <-- L4: append. (L1: would overwrite the sole version.)
        return true;
    }

    /** delete_recipe: true if removed; false if missing. */
    public boolean deleteRecipe(String recipeId) {
        Recipe r = recipes.remove(recipeId);
        if (r == null) return false;
        nameToId.remove(key(r.current().name)); // release the current name
        return true;
    }

    // =========================================================================
    // ========================= LEVEL 2: SEARCH & SORT ========================
    // =========================================================================

    /** search_recipes: case-insensitive substring match on the current name, then SORT. */
    public List<RecipeView> searchRecipes(String query) {
        String q = key(query);
        List<RecipeView> out = new ArrayList<>();
        for (Recipe r : recipes.values()) {
            if (key(r.current().name).contains(q)) out.add(view(r));
        }
        out.sort(SORT);
        return out;
    }

    /** get_all_recipes: every recipe's current view, under the same SORT. */
    public List<RecipeView> getAllRecipes() {
        List<RecipeView> out = new ArrayList<>(recipes.size());
        for (Recipe r : recipes.values()) out.add(view(r));
        out.sort(SORT);
        return out;
    }

    // =========================================================================
    // ============================ LEVEL 3: USERS =============================
    // =========================================================================

    /** add_user: false if the user id is already taken. */
    public boolean addUser(String userId, String username) {
        if (users.containsKey(userId)) return false;
        users.put(userId, username);
        return true;
    }

    /**
     * edit_recipe = update_recipe + a user-existence gate (the ONLY addition over
     * updateRecipe in L3). In L4 it also appends a version instead of overwriting.
     */
    public boolean editRecipe(String userId, String recipeId, String name, List<String> ingredients) {
        if (!users.containsKey(userId)) return false;       // <-- L3 addition: user validation
        Recipe r = recipes.get(recipeId);
        if (r == null) return false;
        if (nameOwnedByOther(name, recipeId)) return false;
        commitVersion(r, name, ingredients);                // <-- L4: append (L3: overwrite)
        return true;
    }

    // =========================================================================
    // ========================== LEVEL 4: VERSIONING ==========================
    // =========================================================================

    /** get_recipe_history: every version oldest->newest with its 1-based number, or null. */
    public List<RecipeVersion> getRecipeHistory(String recipeId) {
        Recipe r = recipes.get(recipeId);
        if (r == null) return null;
        List<RecipeVersion> out = new ArrayList<>(r.versions.size());
        for (int i = 0; i < r.versions.size(); i++) {
            Version v = r.versions.get(i);
            out.add(new RecipeVersion(i + 1, v.name, v.ingredients)); // versions are 1-based
        }
        return out;
    }

    /**
     * rollback_recipe: append a COPY of history[version-1] as the new newest version.
     * False if user invalid, recipe missing, version out of range, or the old name is
     * now owned by a different current recipe. All checks run BEFORE any mutation.
     */
    public boolean rollbackRecipe(String userId, String recipeId, int version) {
        if (!users.containsKey(userId)) return false;
        Recipe r = recipes.get(recipeId);
        if (r == null) return false;
        if (version < 1 || version > r.versions.size()) return false; // 1-based bounds
        Version old = r.versions.get(version - 1);
        // Self is fine: if old.name == current name, nameToId maps it back to this recipe.
        if (nameOwnedByOther(old.name, recipeId)) return false;
        commitVersion(r, old.name, old.ingredients); // append copy; history is preserved
        return true;
    }

    // =========================================================================
    // ============================ self-test (demo) ===========================
    // =========================================================================

    private static int passed = 0, failed = 0;

    private static void check(boolean cond, String label) {
        if (cond) { passed++; System.out.println("PASS  " + label); }
        else      { failed++; System.out.println("FAIL  " + label); }
    }

    private static List<String> ids(List<RecipeView> rs) {
        List<String> out = new ArrayList<>();
        for (RecipeView r : rs) out.add(r.id());
        return out;
    }

    public static void main(String[] args) {
        // ---------------------------- Level 1 ----------------------------
        RecipeManager m = new RecipeManager();
        check(m.addRecipe("1", "Spaghetti Bolognese", List.of("pasta", "beef", "tomato")), "L1 add r1");
        check(m.addRecipe("2", "Caesar Salad", List.of("lettuce", "croutons", "parmesan")), "L1 add r2");
        check(!m.addRecipe("1", "Whatever", List.of()), "L1 duplicate id rejected");
        check(!m.addRecipe("3", "spaghetti bolognese", List.of("x")), "L1 duplicate name (case-insensitive) rejected");
        check(m.getRecipe("1").name().equals("Spaghetti Bolognese"), "L1 get returns current name");
        check(m.getRecipe("404") == null, "L1 get missing -> null");
        check(m.updateRecipe("2", "CAESAR SALAD", List.of("romaine")), "L1 update may keep own name (diff case)");
        check(!m.updateRecipe("2", "Spaghetti Bolognese", List.of()), "L1 update to another's name rejected");
        check(!m.updateRecipe("999", "X", List.of()), "L1 update missing rejected");
        check(m.deleteRecipe("1"), "L1 delete ok");
        check(!m.deleteRecipe("1"), "L1 delete again rejected");
        check(m.addRecipe("9", "Spaghetti Bolognese", List.of("a")), "L1 name freed after delete -> reusable");

        // ---------------------------- Level 2 ----------------------------
        RecipeManager s = new RecipeManager();
        s.addRecipe("10", "Alpha", List.of("a"));            // 1 ingredient
        s.addRecipe("2",  "Beta",  List.of("a", "b", "c"));  // 3 ingredients
        s.addRecipe("1",  "Gamma", List.of("a", "b"));       // 2 ingredients
        s.addRecipe("3",  "Alpha Soup", List.of("a"));       // 1 ingredient
        // ingr asc, then numeric id asc: {3,10}(1) , {1}(2) , {2}(3)
        check(ids(s.getAllRecipes()).equals(List.of("3", "10", "1", "2")), "L2 getAll sorted by ingr then numeric id");
        check(ids(s.searchRecipes("alpha")).equals(List.of("3", "10")), "L2 search substring (case-insensitive) + sort");
        check(s.searchRecipes("zzz").isEmpty(), "L2 search no match -> empty");

        RecipeManager mixed = new RecipeManager();
        mixed.addRecipe("r2", "P", List.of("a"));
        mixed.addRecipe("1",  "Q", List.of("a"));
        mixed.addRecipe("r10", "R", List.of("a"));
        // same ingr count: numeric "1" first, then lexicographic "r10" < "r2"
        check(ids(mixed.getAllRecipes()).equals(List.of("1", "r10", "r2")), "L2 numeric-before-lexicographic id ordering");

        // ---------------------------- Level 3 ----------------------------
        RecipeManager u = new RecipeManager();
        u.addRecipe("1", "Gamma", List.of("a", "b"));
        u.addRecipe("2", "Beta",  List.of("a", "b", "c"));
        check(u.addUser("u1", "chef_john"), "L3 add user");
        check(!u.addUser("u1", "again"), "L3 duplicate user rejected");
        check(!u.editRecipe("ghost", "1", "X", List.of()), "L3 edit with invalid user rejected");
        check(!u.editRecipe("u1", "1", "Beta", List.of()), "L3 edit to another's name rejected");
        check(u.editRecipe("u1", "1", "Gamma Deluxe", List.of("a", "b", "c", "d")), "L3 edit ok");
        check(u.getRecipe("1").name().equals("Gamma Deluxe"), "L3 edit applied");

        // ---------------------------- Level 4 ----------------------------
        // Canonical example: Pasta -> Spaghetti -> Carbonara -> rollback(1) => Pasta
        RecipeManager v = new RecipeManager();
        v.addUser("u1", "x");
        v.addRecipe("r1", "Pasta", List.of("egg", "flour"));
        v.editRecipe("u1", "r1", "Spaghetti", List.of("egg", "flour"));
        v.editRecipe("u1", "r1", "Carbonara", List.of("egg", "pork"));
        check(v.rollbackRecipe("u1", "r1", 1), "L4 rollback to v1 ok");
        List<RecipeVersion> h = v.getRecipeHistory("r1");
        check(h.size() == 4, "L4 rollback APPENDS (history len 4, not truncated)");
        check(h.get(0).version() == 1 && h.get(3).version() == 4, "L4 version numbers 1..4");
        List<String> names = new ArrayList<>();
        for (RecipeVersion rv : h) names.add(rv.name());
        check(names.equals(List.of("Pasta", "Spaghetti", "Carbonara", "Pasta")), "L4 history names Pasta/Spaghetti/Carbonara/Pasta");
        check(v.getRecipeHistory("nope") == null, "L4 history of missing -> null");

        // Rollback name-conflict: another recipe now owns the old name.
        RecipeManager c = new RecipeManager();
        c.addUser("u1", "x");
        c.addRecipe("r1", "Pasta", List.of());
        c.editRecipe("u1", "r1", "Spaghetti", List.of()); // r1 current is now "Spaghetti"
        check(c.addRecipe("r2", "Pasta", List.of()), "L4 'Pasta' free again (not a current name) -> addable");
        check(!c.rollbackRecipe("u1", "r1", 1), "L4 rollback rejected: old name owned by another recipe");
        check(c.getRecipeHistory("r1").size() == 2, "L4 failed rollback caused NO partial mutation");
        check(!c.rollbackRecipe("u1", "r1", 99), "L4 rollback out-of-range rejected");
        check(!c.rollbackRecipe("ghost", "r1", 1), "L4 rollback invalid user rejected");
        check(!c.rollbackRecipe("u1", "ZZZ", 1), "L4 rollback missing recipe rejected");
        c.deleteRecipe("r2");                              // free up "Pasta" again
        check(c.rollbackRecipe("u1", "r1", 1), "L4 rollback ok once conflict cleared (dynamic check)");
        check(c.getRecipe("r1").name().equals("Pasta"), "L4 current name is Pasta after rollback");

        System.out.println("\n==== " + passed + " passed, " + failed + " failed ====");
        if (failed > 0) System.exit(1);
    }
}
