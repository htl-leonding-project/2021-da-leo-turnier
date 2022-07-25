package at.htl.LeoTurnier.dto;

public class SeedDto {
    private Long id;

    private int seed;

    private double averagePlacement;

    public SeedDto() {
        this(-1L);
    }

    public SeedDto(Long id) {
        this(id, 0);
    }

    public SeedDto(Long id, double averagePlacement) {
        this(id, 0, averagePlacement);
    }

    public SeedDto(Long id, int seed, double averagePlacement) {
        this.id = id;
        this.seed = seed;
        this.averagePlacement = averagePlacement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public double getAveragePlacement() {
        return averagePlacement;
    }

    public void setAveragePlacement(double averagePlacement) {
        this.averagePlacement = averagePlacement;
    }
}
