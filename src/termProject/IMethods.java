package termProject;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public interface IMethods {
	
public void adjustGridPane(GridPane grid, ArrayList<ArrayList<String>> myFile, ArrayList<Tile> tiles,
		ArrayList<ImageView> imgs);
//puts the tiles in order on GridPane

	public void moveDownwards(Tile tile, ArrayList<Tile> tiles, ArrayList<ImageView> imgs, Scene scene);
	public void moveLeft(Tile tile, ArrayList<Tile> tiles, ArrayList<ImageView> imgs, Scene scene);
	public void moveRight(Tile tile, ArrayList<Tile> tiles, ArrayList<ImageView> imgs, Scene scene);
	public void moveUpwards(Tile tile, ArrayList<Tile> tiles, ArrayList<ImageView> imgs, Scene scene);


//	These four methods return a boolean value whether the tile can move in the specified directions
	public boolean canMoveRight(Tile tile, ArrayList<Tile> tiles);
	public boolean canMoveLeft(Tile tile, ArrayList<Tile> tiles);
	public boolean canMoveUp(Tile tile, ArrayList<Tile> tiles);
	public boolean canMoveDown(Tile tile, ArrayList<Tile> tiles);
	
	@SuppressWarnings("exports")
	public Tile rightTile(Tile tile, ArrayList<Tile> tiles); //returns the right tile
	@SuppressWarnings("exports")
	public Tile leftTile(Tile tile, ArrayList<Tile> tiles); //returns the left tile
	@SuppressWarnings("exports")
	public Tile upperTile(Tile tile, ArrayList<Tile> tiles); //returns the upper tile
	@SuppressWarnings("exports")
	public Tile lowerTile(Tile tile, ArrayList<Tile> tiles); //returns the lower tile
	
	public boolean checkPath(ArrayList<Tile> tiles);

}
