package ajb.domain;

public enum AssetSize {
	RANDOM(300, 300),
    SMALL( 100, 100),
    MEDIUM(200, 200),
    LARGE( 300, 300);

	public int row, col;

	AssetSize(int row, int col) {
		this.row = row;
        this.col = col;
	}
}
