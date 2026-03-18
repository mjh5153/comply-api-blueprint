package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

/**
 * Movie class representing a generic movie with common properties.
 * This class serves as the base/parent class for different movie types (ActionMovie, ComedyMovie, HorrorMovie).
 * Demonstrates inheritance and polymorphism in object-oriented programming.
 */
public class Movie {
    private String title;
    private String director;
    private int releaseYear;
    private double duration; // in minutes
    private String genre;

    /**
     * Default constructor for Movie class.
     */
    public Movie() {
    }

    /**
     * Constructor to initialize a Movie with title, director, and release year.
     *
     * @param title The movie title
     * @param director The director's name
     * @param releaseYear The year the movie was released
     */
    public Movie(String title, String director, int releaseYear) {
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
    }

    /**
     * Constructor to initialize a Movie with all details.
     *
     * @param title The movie title
     * @param director The director's name
     * @param releaseYear The year the movie was released
     * @param duration The duration in minutes
     * @param genre The movie genre
     */
    public Movie(String title, String director, int releaseYear, double duration, String genre) {
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.duration = duration;
        this.genre = genre;
    }

    /**
     * Gets the title of the movie.
     *
     * @return The movie title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the movie.
     *
     * @param title The movie title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the director of the movie.
     *
     * @return The director's name
     */
    public String getDirector() {
        return director;
    }

    /**
     * Sets the director of the movie.
     *
     * @param director The director's name to set
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * Gets the release year of the movie.
     *
     * @return The release year
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Sets the release year of the movie.
     *
     * @param releaseYear The release year to set
     */
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Gets the duration of the movie.
     *
     * @return The duration in minutes
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the movie.
     *
     * @param duration The duration in minutes to set
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Gets the genre of the movie.
     *
     * @return The genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the genre of the movie.
     *
     * @param genre The genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Plays the movie with a generic message.
     * This method can be overridden by subclasses to provide specific behavior.
     * This demonstrates polymorphic behavior - the actual method executed depends on the runtime type of the object.
     */
    public void play() {
        System.out.println("Playing movie: " + title + " directed by " + director + " (" + releaseYear + ")");
    }

    /**
     * Watches the movie and displays information about the instance type.
     * This method demonstrates runtime type checking and polymorphism.
     * It gets the actual runtime class of the object and displays it.
     */
    public void watchMovie() {
        String instanceType = this.getClass().getSimpleName();
        System.out.println("Watching " + instanceType + ": " + title +
                          " (" + genre + ") - " + duration + " minutes");
        System.out.println("Instance type: " + this.getClass().getName());
        play();
    }

    /**
     * Provides a string representation of the Movie.
     *
     * @return A string representation with movie details
     */
    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", releaseYear=" + releaseYear +
                ", duration=" + duration +
                ", genre='" + genre + '\'' +
                '}';
    }

    /**
     * Static factory method to create Movie instances based on type.
     * This method demonstrates the Factory pattern for polymorphic object creation.
     * Uses a switch statement on the first character of the type parameter to determine
     * which Movie subclass to instantiate.
     *
     * @param type The type of movie to create (Adventure, Action, Comedy, Horror, etc.)
     * @param title The title of the movie
     * @return A Movie instance of the appropriate type (Adventure, Action, etc.)
     */
    public static Movie getMovie(String type, String title) {
        if (type == null || type.isEmpty()) {
            return new Movie(title, "Unknown", 2026);
        }

        char firstChar = Character.toUpperCase(type.charAt(0));

        switch (firstChar) {
            case 'A':
                // Adventure movies
                return new Adventure(title, "Unknown Director", 2026, 120, "Adventure",
                        "Quest", "Hero", true);
            case 'C':
                // Could be Comedy or other C-type movies
                return new Movie(title, "Unknown Director", 2026, 90, "Comedy");
            case 'H':
                // Horror movies
                return new Movie(title, "Unknown Director", 2026, 100, "Horror");
            case 'S':
                // Sci-Fi movies
                return new Movie(title, "Unknown Director", 2026, 130, "Sci-Fi");
            case 'D':
                // Drama movies
                return new Movie(title, "Unknown Director", 2026, 110, "Drama");
            default:
                // Default to generic Movie
                return new Movie(title, "Unknown Director", 2026, 110, type);
        }
    }
}

