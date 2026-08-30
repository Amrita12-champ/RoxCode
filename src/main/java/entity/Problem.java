package entity;

public class Problem {
    private int id;
    private String title;
    private String difficulty;
    private String category;
    private String acceptanceRate;

    public Problem(int id, String title, String difficulty, String category, String acceptanceRate) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.category = category;
        this.acceptanceRate = acceptanceRate;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDifficulty() { return difficulty; }
    public String getCategory() { return category; }
    public String getAcceptanceRate() { return acceptanceRate; }
}