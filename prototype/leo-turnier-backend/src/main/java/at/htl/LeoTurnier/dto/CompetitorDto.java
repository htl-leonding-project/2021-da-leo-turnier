package at.htl.LeoTurnier.dto;

import at.htl.LeoTurnier.entity.Competitor;

public class CompetitorDto {

    private Long id;

    private int wins;

    private int points;

    public CompetitorDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getWins() {
        return wins;
    }

    public void addWin() {
        this.wins++;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }
}
