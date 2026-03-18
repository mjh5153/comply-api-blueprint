package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

/**
 * Adventure class representing an adventure movie.
 * Extends the Movie class to inherit common movie properties and behavior.
 * This class demonstrates inheritance and method overriding (polymorphism).
 * Adventure movies are characterized by action, exploration, and thrilling quests.
 */
public class Adventure extends Movie {
    private String adventureType; // e.g., "Quest", "Exploration", "Survival"
    private String mainCharacter;
    private boolean hasActionSequences;

    /**
     * Default constructor for Adventure class.
     */
    public Adventure() {
        super();
    }

    /**
     * Constructor to initialize an Adventure movie with title, director, and release year.
     *
     * @param title The movie title
     * @param director The director's name
     * @param releaseYear The year the movie was released
     */
    public Adventure(String title, String director, int releaseYear) {
        super(title, director, releaseYear);
    }

    /**
     * Constructor to initialize an Adventure movie with all movie details.
     *
     * @param title The movie title
     * @param director The director's name
     * @param releaseYear The year the movie was released
     * @param duration The duration in minutes
     * @param genre The movie genre
     */
    public Adventure(String title, String director, int releaseYear, double duration, String genre) {
        super(title, director, releaseYear, duration, genre);
    }

    /**
     * Full constructor for Adventure with adventure-specific details.
     *
     * @param title The movie title
     * @param director The director's name
     * @param releaseYear The year the movie was released
     * @param duration The duration in minutes
     * @param genre The movie genre
     * @param adventureType The type of adventure
     * @param mainCharacter The name of the main character
     * @param hasActionSequences Whether the movie has action sequences
     */
    public Adventure(String title, String director, int releaseYear, double duration, String genre,
                    String adventureType, String mainCharacter, boolean hasActionSequences) {
        super(title, director, releaseYear, duration, genre);
        this.adventureType = adventureType;
        this.mainCharacter = mainCharacter;
        this.hasActionSequences = hasActionSequences;
    }

    /**
     * Gets the adventure type.
     *
     * @return The adventure type
     */
    public String getAdventureType() {
        return adventureType;
    }

    /**
     * Sets the adventure type.
     *
     * @param adventureType The adventure type to set
     */
    public void setAdventureType(String adventureType) {
        this.adventureType = adventureType;
    }

    /**
     * Gets the main character name.
     *
     * @return The main character name
     */
    public String getMainCharacter() {
        return mainCharacter;
    }

    /**
     * Sets the main character name.
     *
     * @param mainCharacter The main character name to set
     */
    public void setMainCharacter(String mainCharacter) {
        this.mainCharacter = mainCharacter;
    }

    /**
     * Checks if the movie has action sequences.
     *
     * @return True if it has action sequences, false otherwise
     */
    public boolean hasActionSequences() {
        return hasActionSequences;
    }

    /**
     * Sets whether the movie has action sequences.
     *
     * @param hasActionSequences True if it has action sequences, false otherwise
     */
    public void setHasActionSequences(boolean hasActionSequences) {
        this.hasActionSequences = hasActionSequences;
    }

    /**
     * Overrides the play() method from Movie class.
     * Provides adventure-specific behavior for playing the movie.
     * This demonstrates polymorphic behavior - the actual method executed depends on the runtime type.
     */
    @Override
    public void play() {
        System.out.println("🎬 ADVENTURE MOVIE STARTING 🎬");
        System.out.println("Playing adventure: " + getTitle() + " directed by " + getDirector());
        System.out.println("Follow the journey of " + mainCharacter + " in this thrilling " + adventureType + "!");
        if (hasActionSequences) {
            System.out.println("⚡ Get ready for intense action sequences! ⚡");
        }
    }

    /**
     * Overrides the watchMovie() method from Movie class.
     * Provides adventure-specific behavior for watching the movie.
     */
    @Override
    public void watchMovie() {
        super.watchMovie();
        System.out.println("\n🗺️  Watching " + getTitle() +
                          " (" + getGenre() + ") - " + getDuration() + " minutes");
        System.out.println("Instance type: " + this.getClass().getName());
        System.out.println("Adventure Type: " + adventureType);
        System.out.println("Main Character: " + mainCharacter);
        System.out.println("Has Action Sequences: " + (hasActionSequences ? "Yes" : "No"));
        play();
    }

    /**
     * Provides a string representation of the Adventure movie.
     *
     * @return A string representation with adventure-specific details
     */
    @Override
    public String toString() {
        return "Adventure{" +
                "title='" + getTitle() + '\'' +
                ", director='" + getDirector() + '\'' +
                ", releaseYear=" + getReleaseYear() +
                ", duration=" + getDuration() +
                ", genre='" + getGenre() + '\'' +
                ", adventureType='" + adventureType + '\'' +
                ", mainCharacter='" + mainCharacter + '\'' +
                ", hasActionSequences=" + hasActionSequences +
                '}';
    }
}

