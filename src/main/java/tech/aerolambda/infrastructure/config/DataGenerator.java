package tech.aerolambda.infrastructure.config;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
public class DataGenerator {

    private final Random random = new Random(42);

    private static final String[] FIRST_NAMES = {
        "James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda",
        "William", "Elizabeth", "David", "Barbara", "Richard", "Susan", "Joseph", "Jessica",
        "Thomas", "Sarah", "Charles", "Karen", "Christopher", "Nancy", "Daniel", "Lisa",
        "Matthew", "Betty", "Anthony", "Margaret", "Mark", "Sandra", "Donald", "Ashley",
        "Steven", "Kimberly", "Paul", "Emily", "Andrew", "Donna", "Joshua", "Michelle",
        "Kenneth", "Dorothy", "Kevin", "Carol", "Brian", "Amanda", "George", "Melissa",
        "Edward", "Deborah", "Ronald", "Stephanie", "Timothy", "Rebecca", "Jason", "Sharon",
        "Jeffrey", "Laura", "Ryan", "Cynthia", "Jacob", "Kathleen", "Gary", "Amy"
    };

    private static final String[] LAST_NAMES = {
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
        "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson",
        "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson",
        "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker",
        "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores",
        "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell",
        "Carter", "Roberts", "Gomez", "Phillips", "Evans", "Turner", "Diaz", "Parker"
    };

    private static final String[] STORE_ADJECTIVES = {
        "Cozy", "Grand", "Classic", "Modern", "Vintage", "Premier", "Elite", "Golden",
        "Silver", "Royal", "Noble", "Bright", "Sunny", "Happy", "Lucky", "Magic",
        "Wonder", "Dream", "Star", "Moon", "Ocean", "Mountain", "Valley", "River",
        "Forest", "Garden", "Meadow", "Harbor", "Lighthouse", "Cornerstone", "Heritage"
    };

    private static final String[] STORE_NOUNS = {
        "Book", "Page", "Chapter", "Story", "Tale", "Novel", "Library", "Bookshelf",
        "Reader", "Writer", "Author", "Ink", "Quill", "Scroll", "Tome", "Volume"
    };

    private static final String[] STORE_SUFFIXES = {
        "Books", "Bookstore", "Book Shop", "Book Emporium", "Book Haven", "Book Nook",
        "Book Corner", "Book House", "Book Palace", "Book World", "Book Depot", "Books & More"
    };

    private static final String[] STREET_NAMES = {
        "Main", "Oak", "Maple", "Cedar", "Pine", "Elm", "Washington", "Lincoln",
        "Park", "Lake", "River", "Hill", "Valley", "Forest", "Garden", "Meadow",
        "Spring", "Summer", "Autumn", "Winter", "Sunrise", "Sunset", "Harbor", "Bay"
    };

    private static final String[] STREET_TYPES = {
        "Street", "Avenue", "Boulevard", "Drive", "Lane", "Road", "Way", "Place", "Court"
    };

    private static final String[] CITIES = {
        "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia",
        "San Antonio", "San Diego", "Dallas", "San Jose", "Austin", "Jacksonville",
        "Fort Worth", "Columbus", "San Francisco", "Charlotte", "Indianapolis", "Seattle",
        "Denver", "Boston", "Portland", "Las Vegas", "Detroit", "Nashville", "Memphis",
        "Baltimore", "Milwaukee", "Albuquerque", "Tucson", "Fresno", "Sacramento", "Miami"
    };

    private static final String[] STATES = {
        "NY", "CA", "IL", "TX", "AZ", "PA", "FL", "OH", "NC", "MI", "WA", "CO", "MA",
        "OR", "NV", "TN", "GA", "MD", "WI", "NM"
    };

    private static final String[] BOOK_ADJECTIVES = {
        "Lost", "Hidden", "Secret", "Forgotten", "Ancient", "Eternal", "Silent", "Dark",
        "Bright", "Golden", "Silver", "Crimson", "Midnight", "Broken", "Burning", "Frozen",
        "Whispered", "Stolen", "Cursed", "Blessed", "Wild", "Gentle", "Fierce", "Lonely",
        "Endless", "Fleeting", "Haunted", "Sacred", "Wicked", "Noble", "Twisted", "Shattered"
    };

    private static final String[] BOOK_NOUNS = {
        "Kingdom", "Empire", "World", "Dream", "Shadow", "Light", "Heart", "Soul",
        "Memory", "Promise", "Secret", "Truth", "Lie", "Love", "War", "Peace",
        "Storm", "Fire", "Ice", "Wind", "Mountain", "Ocean", "Forest", "Desert",
        "City", "Castle", "Tower", "Bridge", "Door", "Key", "Crown", "Throne",
        "Sword", "Shield", "Rose", "Thorn", "Star", "Moon", "Sun", "Night"
    };

    private static final String[] BOOK_VERBS = {
        "Finding", "Seeking", "Chasing", "Breaking", "Building", "Crossing", "Falling",
        "Rising", "Burning", "Freezing", "Healing", "Hunting", "Saving", "Destroying",
        "Awakening", "Dreaming", "Remembering", "Forgetting", "Discovering", "Creating"
    };

    private static final String[] GENRES = {
        "fiction", "mystery", "thriller", "romance", "science fiction", "fantasy",
        "historical fiction", "horror", "literary fiction", "contemporary fiction"
    };

    private static final String[] BIO_TEMPLATES = {
        "{author} is an acclaimed author known for their captivating {genre} novels. With over {works} published works, they have earned recognition from readers worldwide.",
        "Born in {city}, {author} discovered their passion for writing at an early age. Their works often explore themes of {theme} and have been translated into {languages} languages.",
        "{author} is a bestselling author whose {genre} novels have captivated millions of readers. They have received numerous awards including the {award} Prize.",
        "After a career in {career}, {author} turned to writing full-time. Their debut novel became an instant bestseller, launching a successful literary career spanning {years} years.",
        "{author} writes {genre} fiction that blends action with deep character development. Their work has been praised for its authentic portrayal of human emotions."
    };

    private static final String[] AWARDS = {
        "National Book", "Pulitzer", "Booker", "Hugo", "Nebula", "Edgar", "Arthur C. Clarke"
    };

    private static final String[] CAREERS = {
        "journalism", "teaching", "law", "medicine", "engineering", "psychology", "art"
    };

    private static final String[] DESCRIPTION_TEMPLATES = {
        "In this gripping %s, %s takes readers on an unforgettable journey through %s. When %s discovers %s, everything changes. A masterful tale of %s that will keep you reading late into the night.",
        "Set in %s, this %s follows the story of %s who must %s. With rich prose and compelling characters, this book explores themes of %s and %s.",
        "%s delivers another stunning %s in this tale of %s. As %s unfolds, readers will find themselves drawn into a world where %s. A must-read for fans of %s fiction.",
        "When %s, %s finds themselves caught in %s. This %s weaves together elements of %s and %s, creating a narrative that is both thrilling and thought-provoking.",
        "This %s tells the story of %s, a %s who must navigate %s. %s has crafted a tale that is equal parts %s and %s, with an ending that will leave readers breathless."
    };

    private static final String[] SETTINGS = {
        "a small coastal town", "the bustling streets of New York", "a remote mountain village",
        "Victorian England", "modern-day Tokyo", "a dystopian future", "medieval Europe",
        "the American frontier", "a mysterious island", "wartime France"
    };

    private static final String[] CHARACTER_TYPES = {
        "a young detective", "an aging professor", "a reluctant hero", "a mysterious stranger",
        "a brilliant scientist", "a struggling artist", "a reformed criminal", "a brave journalist",
        "an orphaned child", "a world-weary soldier"
    };

    private static final String[] PLOT_ELEMENTS = {
        "uncover an ancient conspiracy", "solve a series of mysterious disappearances",
        "protect a dangerous secret", "find their way back home", "confront their past",
        "save their family from ruin", "discover the truth about their origins",
        "stop a catastrophic event", "rebuild their shattered life"
    };

    private static final String[] THEMES = {
        "love and loss", "redemption and forgiveness", "courage and sacrifice",
        "identity and belonging", "power and corruption", "hope and despair",
        "family and loyalty", "truth and deception", "freedom and oppression"
    };

    public String generateAuthorName() {
        return pickRandom(FIRST_NAMES) + " " + pickRandom(LAST_NAMES);
    }

    public String generateAuthorBio(String authorName) {
        String template = pickRandom(BIO_TEMPLATES);
        String genre = pickRandom(GENRES);
        String city = pickRandom(CITIES);
        String award = pickRandom(AWARDS);
        String career = pickRandom(CAREERS);
        String theme = pickRandom(THEMES);
        int years = random.nextInt(30) + 5;
        int languages = random.nextInt(20) + 5;
        int works = random.nextInt(30) + 3;

        return template
                .replace("{author}", authorName)
                .replace("{genre}", genre)
                .replace("{city}", city)
                .replace("{award}", award)
                .replace("{career}", career)
                .replace("{theme}", theme)
                .replace("{years}", String.valueOf(years))
                .replace("{languages}", String.valueOf(languages))
                .replace("{works}", String.valueOf(works));
    }

    public String generateStoreName() {
        int type = random.nextInt(3);
        return switch (type) {
            case 0 -> pickRandom(STORE_ADJECTIVES) + " " + pickRandom(STORE_NOUNS) + " " + pickRandom(STORE_SUFFIXES);
            case 1 -> "The " + pickRandom(STORE_ADJECTIVES) + " " + pickRandom(STORE_NOUNS);
            default -> pickRandom(CITIES) + " " + pickRandom(STORE_SUFFIXES);
        };
    }

    public String generateAddress() {
        int number = random.nextInt(9000) + 100;
        String street = pickRandom(STREET_NAMES);
        String type = pickRandom(STREET_TYPES);
        String city = pickRandom(CITIES);
        String state = pickRandom(STATES);
        int zip = random.nextInt(90000) + 10000;
        return String.format("%d %s %s, %s, %s %d", number, street, type, city, state, zip);
    }

    public String generatePhone() {
        return String.format("(%03d) %03d-%04d",
                random.nextInt(900) + 100,
                random.nextInt(900) + 100,
                random.nextInt(9000) + 1000);
    }

    public String generateEmail(String name) {
        String cleanName = name.toLowerCase().replaceAll("[^a-z0-9]", "");
        cleanName = cleanName.substring(0, Math.min(cleanName.length(), 15));
        String[] domains = {"books.com", "bookstore.com", "reading.com", "library.net", "pages.com"};
        return cleanName + random.nextInt(100) + "@" + pickRandom(domains);
    }

    public String generateBookTitle() {
        int type = random.nextInt(4);
        return switch (type) {
            case 0 -> "The " + pickRandom(BOOK_ADJECTIVES) + " " + pickRandom(BOOK_NOUNS);
            case 1 -> pickRandom(BOOK_NOUNS) + " of " + pickRandom(BOOK_NOUNS);
            case 2 -> pickRandom(BOOK_VERBS) + " the " + pickRandom(BOOK_NOUNS);
            default -> "The " + pickRandom(BOOK_NOUNS) + "'s " + pickRandom(BOOK_NOUNS);
        };
    }

    public String generateIsbn() {
        return String.format("978-%03d-%04d-%03d-%d",
                random.nextInt(900) + 100,
                random.nextInt(9000) + 1000,
                random.nextInt(900) + 100,
                random.nextInt(10));
    }

    public BigDecimal generatePrice() {
        double price = 5.99 + (random.nextDouble() * 194.0);
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
    }

    public Integer generatePublicationYear() {
        return random.nextInt(76) + 1950;
    }

    public String generateBookDescription(String authorName) {
        String template = pickRandom(DESCRIPTION_TEMPLATES);
        String genre = pickRandom(GENRES);
        String setting = pickRandom(SETTINGS);
        String character = pickRandom(CHARACTER_TYPES);
        String plot = pickRandom(PLOT_ELEMENTS);
        String theme1 = pickRandom(THEMES);
        String theme2 = pickRandom(THEMES);

        return template
                .replaceFirst("%s", genre)
                .replaceFirst("%s", authorName)
                .replaceFirst("%s", setting)
                .replaceFirst("%s", character)
                .replaceFirst("%s", plot)
                .replaceFirst("%s", theme1)
                .replaceFirst("%s", theme2)
                .replaceFirst("%s", setting)
                .replaceFirst("%s", genre)
                .replaceFirst("%s", character)
                .replaceFirst("%s", plot)
                .replaceFirst("%s", theme1)
                .replaceFirst("%s", theme2)
                .replaceFirst("%s", authorName)
                .replaceFirst("%s", genre)
                .replaceFirst("%s", theme1);
    }

    public String generateUserName() {
        return pickRandom(FIRST_NAMES) + " " + pickRandom(LAST_NAMES);
    }

    public String generateUserEmail(String name) {
        String cleanName = name.toLowerCase().replaceAll(" ", ".");
        String[] domains = {"email.com", "mail.com", "inbox.com", "webmail.com"};
        return cleanName + "@" + pickRandom(domains);
    }

    private <T> T pickRandom(T[] array) {
        return array[random.nextInt(array.length)];
    }

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
}
