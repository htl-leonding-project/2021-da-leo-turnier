package at.htl.LeoTurnier.dto;


public class CompetitorDto {

    private Long id;

    private int wins;

    private int points;

    private int tieBreakerWins;

    public CompetitorDto() {
        this(-1L);
    }

    public CompetitorDto(Long id) {
        this.id = id;
    }

    public CompetitorDto(int wins, int points, int tieBreakerWins) {
        this.wins = wins;
        this.points = points;
        this.tieBreakerWins = tieBreakerWins;
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

    public void subtractWin() {
        this.wins--;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getTieBreakerWins() {
        return tieBreakerWins;
    }

    public void addTieBreakerWin() {
        this.tieBreakerWins++;
    }

    public void subtractTieBreakerWin() {
        this.tieBreakerWins--;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != CompetitorDto.class) {
            return false;
        }
        CompetitorDto c = (CompetitorDto) obj;
        return this.getWins() == c.getWins() &&
                this.getPoints() == c.getPoints() &&
                this.getTieBreakerWins() == c.getTieBreakerWins();
    }
}
