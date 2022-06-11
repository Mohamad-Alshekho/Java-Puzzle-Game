package termProject;

import java.applet.AudioClip;
import java.applet.Applet;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;



public class Project extends Application implements IMethods {
	public static String[] levels = { "level1.txt", "level2.txt", "level3.txt", "level4.txt", "level5.txt","level6.txt" };
	public static int levelIndex = 0;
	public static String currentLevel = levels[levelIndex];
	ArrayList<ArrayList<String>> myFile = new ArrayList<ArrayList<String>>();
	GridPane grid = new GridPane();
	File file = new File(currentLevel);
	Scene scene;
	Stage stage;
	Pane pp = new Pane();

	//Circle circle = new Circle(50, 50, 11);;

//	Circle circle = new Circle(50, 50, 11);
	ImageView circle = new ImageView("ball.png");

	int moves=0;
	int levelNum=1;
	ArrayList<String> path = new ArrayList<String>();


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws FileNotFoundException {
		
		ArrayList<Tile> tiles = new ArrayList<Tile>(); // stores the tile objects
		ArrayList<ImageView> imgs = new ArrayList<ImageView>(); // stores the image view objects

		setMyFile();
		grid.setAlignment(Pos.CENTER);
		adjustGridPane(grid, myFile, tiles, imgs);
		pp.getChildren().add(grid);
//		circle.setFill(Color.RED);
		circle.setFitHeight(27);
		circle.setFitWidth(27);
		
		int[] position = new int[2];
		position[0] = getPosition(getStarterTile(tiles).id)[0];
		position[1] = getPosition(getStarterTile(tiles).id)[1];
		circle.setX(position[0]*100 +37);
		circle.setY(position[1]*100 +37);
		
		pp.getChildren().add(circle);
		scene = new Scene(pp, 400, 400);
		primaryStage.setTitle("Puzzle         "+"Level "+levelNum+"         Moves "+moves);
		primaryStage.setScene(scene);
		primaryStage.show();

		stage = primaryStage;
		int x = 0;
		for (int i = 0; i < 16; i++) {
			x = i;
			imgs.get(i).setOnMouseClicked(e -> {
				System.out.println("x: " + e.getSceneX() + ", y: " + e.getSceneY());
			});
		}

		setListeners(imgs, tiles, scene);
	}

	public void goToNextLevel(ArrayList<Tile> tiles, ArrayList<ImageView> imgs) {
		levelIndex++;
		currentLevel = levels[levelIndex];
		myFile.clear();
		file = new File(currentLevel);
		try {
			setMyFile();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		System.out.println(currentLevel);
		grid = null;
		grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		tiles.clear();
		imgs.clear();
		adjustGridPane(grid, myFile, tiles, imgs);
		pp = new Pane();
		pp.getChildren().add(grid);
		circle = null;
		circle = new ImageView("ball.png");
		circle.setFitHeight(27);
		circle.setFitWidth(27);
		int[] position = new int[2];
		position[0] = getPosition(getStarterTile(tiles).id)[0];
		position[1] = getPosition(getStarterTile(tiles).id)[1];
		circle.setX(position[0]*100 +37);
		circle.setY(position[1]*100 +37);
//		circle.setFill(Color.RED);
		pp.getChildren().add(circle);
		scene = new Scene(pp, 400, 400);
		stage.setScene(scene);
		setListeners(imgs, tiles, scene);
		path.clear();
	}

	public void setMyFile() throws FileNotFoundException {
		Scanner input = new Scanner(file);
		ArrayList<String> text = new ArrayList<String>();

		while (input.hasNext()) {
			text.add(input.nextLine());
		}
		// split the objects and but them in arrays accordingly.
		for (int i = 0; i < text.size(); i++) {
			ArrayList<String> t = new ArrayList<String>();
			for (int j = 0; j < text.get(i).split(",").length; j++) {
				t.add(text.get(i).split(",")[j]);
			}
			myFile.add(t);
		}
	}
	
	int id;
	public void setListeners(ArrayList<ImageView> imgs, ArrayList<Tile> tiles, Scene scene) {
		ArrayList<Double> positionX = new ArrayList<Double>();
		ArrayList<Double> positionY = new ArrayList<Double>();
		
		for (int i = 0; i < 16; i++) {
			int x = i;
			imgs.get(i).setOnMouseDragged((e) -> {
				positionX.add(e.getSceneX());
				positionY.add(e.getSceneY());
//				System.out.println("size : " + positionX.size());
//				System.out.println("Coordinates | x: " + e.getSceneX() + ", y: " + e.getSceneY() + "    size: "
//						+ positionX.size());
			});
			imgs.get(i).setOnMousePressed(e->{
				 id = Integer.parseInt(tiles.get(x).getId());
			});
			
			scene.setOnMouseReleased(e -> {
				if (positionX.size() > 10) {
					double differenceX = positionX.get(positionX.size() - 1) - positionX.get(0);
					double differenceY = positionY.get(positionY.size() - 1) - positionY.get(0);
					System.out.println("x : " + differenceX + ", y : " + differenceY);

					if (Math.abs(differenceX) > Math.abs(differenceY)) {
						// horizontal drag
						if (differenceX < 0) {
							Tile tile = tileById(id, tiles);
							if (canMoveLeft(tile, tiles))
								moveLeft(tile, tiles, imgs, scene);
						} else if (differenceX > 0) {
							Tile tile = tileById(id, tiles);
							if (canMoveRight(tile, tiles))
								moveRight(tile, tiles, imgs, scene);
						}
					} else if (Math.abs(differenceX) < Math.abs(differenceY)) {
						// vertical drag
						if (differenceY < 0) {
							Tile tile = tileById(id, tiles);
							if (canMoveUp(tile, tiles))
								moveUpwards(tile, tiles, imgs, scene);
						} else if (differenceY > 0) {
							Tile tile = tileById(id, tiles);
							if (canMoveDown(tile, tiles)) {
								moveDownwards(tile, tiles, imgs, scene);
							
							}
						}
					}
				}
				positionX.clear();
				positionY.clear();
			});

		}
	}

	public Tile tileById(int id, ArrayList<Tile> tiles) {
		Tile a = null;
		for (Tile x : tiles) {
			if (Integer.parseInt(x.id) == id)
				return x;
		}
		return a;
	}

	public void adjustGridPane(GridPane grid, ArrayList<ArrayList<String>> myFile, ArrayList<Tile> tiles,
			ArrayList<ImageView> imgs) {
		for (int i = 0; i < myFile.size(); i++) {
			switch (Integer.parseInt(myFile.get(i).get(0))) {
			case 1:
				tiles.add(new Tile(myFile.get(i).get(0), myFile.get(i).get(1), myFile.get(i).get(2)));
				ImageView img1 = tiles.get(0).returnImage();
				grid.getChildren().add(img1);
				img1.setFitHeight(100);
				img1.setFitWidth(100);
				GridPane.setConstraints(img1, 0, 0);
				imgs.add(img1);
				break;
			case 2:
				tiles.add(new Tile(myFile.get(i).get(0), myFile.get(i).get(1), myFile.get(i).get(2)));
				ImageView img2 = tiles.get(1).returnImage();
				grid.getChildren().add(img2);
				img2.setFitHeight(100);
				img2.setFitWidth(100);
				GridPane.setConstraints(img2, 1, 0);
				imgs.add(img2);
				break;
			case 3:
				tiles.add(new Tile(myFile.get(i).get(0), myFile.get(i).get(1), myFile.get(i).get(2)));
				ImageView img3 = tiles.get(2).returnImage();
				grid.getChildren().add(img3);
				img3.setFitHeight(100);
				img3.setFitWidth(100);
				GridPane.setConstraints(img3, 2, 0);
				imgs.add(img3);
				break;
			case 4:
				tiles.add(new Tile(myFile.get(i).get(0), myFile.get(i).get(1), myFile.get(i).get(2)));
				ImageView img4 = tiles.get(3).returnImage();
				grid.getChildren().add(img4);
				img4.setFitHeight(100);
				img4.setFitWidth(100);
				GridPane.setConstraints(img4, 3, 0);
				imgs.add(img4);
				break;
			case 5:
				tiles.add(new Tile(myFile.get(i).get(0), myFile.get(i).get(1), myFile.get(i).get(2)));
				ImageView img5 = tiles.get(4).returnImage();
				grid.getChildren().add(img5);
				img5.setFitHeight(100);
				img5.setFitWidth(100);
				GridPane.setConstraints(img5, 0, 1);
				imgs.add(img5);
				break;
			case 6:
				tiles.add(new Tile(myFile.get(i).get(0), myFile.get(i).get(1), myFile.get(i).get(2)));
				ImageView img6 = tiles.get(5).returnImage();
				grid.getChildren().add(img6);
				img6.setFitHeight(100);
				img6.setFitWidth(100);
				GridPane.setConstraints(img6, 1, 1);
				imgs.add(img6);
				break;
			case 7:
				tiles.add(new Tile(myFile.get(i).get(0), myFile.get(i).get(1), myFile.get(i).get(2)));
				ImageView img7 = tiles.get(6).returnImage();
				grid.getChildren().add(img7);
				img7.setFitHeight(100);
				img7.setFitWidth(100);
				GridPane.setConstraints(img7, 2, 1);
				imgs.add(img7);
				break;
			case 8:
				tiles.add(new Tile(myFile.get(i).get(0), myFile.get(i).get(1), myFile.get(i).get(2)));
				ImageView img8 = tiles.get(7).returnImage();
				grid.getChildren().add(img8);
				img8.setFitHeight(100);
				img8.setFitWidth(100);
				GridPane.setConstraints(img8, 3, 1);
				imgs.add(img8);
				break;
			case 9:
				tiles.add(new Tile(myFile.get(i).get(0), myFile.get(i).get(1), myFile.get(i).get(2)));
				ImageView img9 = tiles.get(8).returnImage();
				grid.getChildren().add(img9);
				img9.setFitHeight(100);
				img9.setFitWidth(100);
				GridPane.setConstraints(img9, 0, 2);
				imgs.add(img9);
				break;
			case 10:
				tiles.add(new Tile(myFile.get(i).get(0), myFile.get(i).get(1), myFile.get(i).get(2)));
				ImageView img10 = tiles.get(9).returnImage();
				grid.getChildren().add(img10);
				img10.setFitHeight(100);
				img10.setFitWidth(100);
				GridPane.setConstraints(img10, 1, 2);
				imgs.add(img10);
				break;
			case 11:
				tiles.add(new Tile(myFile.get(i).get(0), myFile.get(i).get(1), myFile.get(i).get(2)));
				ImageView img11 = tiles.get(10).returnImage();
				grid.getChildren().add(img11);
				img11.setFitHeight(100);
				img11.setFitWidth(100);
				GridPane.setConstraints(img11, 2, 2);
				imgs.add(img11);
				break;
			case 12:
				tiles.add(new Tile(myFile.get(i).get(0), myFile.get(i).get(1), myFile.get(i).get(2)));
				ImageView img12 = tiles.get(11).returnImage();
				grid.getChildren().add(img12);
				img12.setFitHeight(100);
				img12.setFitWidth(100);
				GridPane.setConstraints(img12, 3, 2);
				imgs.add(img12);
				break;
			case 13:
				tiles.add(new Tile(myFile.get(i).get(0), myFile.get(i).get(1), myFile.get(i).get(2)));
				ImageView img13 = tiles.get(12).returnImage();
				grid.getChildren().add(img13);
				img13.setFitHeight(100);
				img13.setFitWidth(100);
				GridPane.setConstraints(img13, 0, 3);
				imgs.add(img13);
				break;
			case 14:
				tiles.add(new Tile(myFile.get(i).get(0), myFile.get(i).get(1), myFile.get(i).get(2)));
				ImageView img14 = tiles.get(13).returnImage();
				grid.getChildren().add(img14);
				img14.setFitHeight(100);
				img14.setFitWidth(100);
				GridPane.setConstraints(img14, 1, 3);
				imgs.add(img14);
				break;
			case 15:
				tiles.add(new Tile(myFile.get(i).get(0), myFile.get(i).get(1), myFile.get(i).get(2)));
				ImageView img15 = tiles.get(14).returnImage();
				grid.getChildren().add(img15);
				img15.setFitHeight(100);
				img15.setFitWidth(100);
				GridPane.setConstraints(img15, 2, 3);
				imgs.add(img15);
				break;
			case 16:
				tiles.add(new Tile(myFile.get(i).get(0), myFile.get(i).get(1), myFile.get(i).get(2)));
				ImageView img16 = tiles.get(15).returnImage();
				grid.getChildren().add(img16);
				img16.setFitHeight(100);
				img16.setFitWidth(100);
				GridPane.setConstraints(img16, 3, 3);
				imgs.add(img16);
				break;

			}
		}
	}

	public int[] getPosition(String s) {
		int[] arr = new int[3];
		switch (s) {
		case "1":
			arr[0] = 0;
			arr[1] = 0;
			break;
		case "2":
			arr[0] = 1;
			arr[1] = 0;
			break;
		case "3":
			arr[0] = 2;
			arr[1] = 0;
			break;
		case "4":
			arr[0] = 3;
			arr[1] = 0;
			break;
		case "5":
			arr[0] = 0;
			arr[1] = 1;
			break;
		case "6":
			arr[0] = 1;
			arr[1] = 1;
			break;
		case "7":
			arr[0] = 2;
			arr[1] = 1;
			break;
		case "8":
			arr[0] = 3;
			arr[1] = 1;
			break;
		case "9":
			arr[0] = 0;
			arr[1] = 2;
			break;
		case "10":
			arr[0] = 1;
			arr[1] = 2;
			break;
		case "11":
			arr[0] = 2;
			arr[1] = 2;
			break;
		case "12":
			arr[0] = 3;
			arr[1] = 2;
			break;
		case "13":
			arr[0] = 0;
			arr[1] = 3;
			break;
		case "14":
			arr[0] = 1;
			arr[1] = 3;
			break;
		case "15":
			arr[0] = 2;
			arr[1] = 3;
			break;
		case "16":
			arr[0] = 3;
			arr[1] = 3;
			break;
		}
		return arr;
	}

	public int getIdByPosition(int col, int row) {
		int id = 0;
		switch (col) {
		case 0:
			if (row == 0)
				id = 1;
			if (row == 1)
				id = 5;
			if (row == 2)
				id = 9;
			if (row == 3)
				id = 13;
			break;
		case 1:
			if (row == 0)
				id = 2;
			if (row == 1)
				id = 6;
			if (row == 2)
				id = 10;
			if (row == 3)
				id = 14;
			break;
		case 2:
			if (row == 0)
				id = 3;
			if (row == 1)
				id = 7;
			if (row == 2)
				id = 11;
			if (row == 3)
				id = 15;
			break;
		case 3:
			if (row == 0)
				id = 4;
			if (row == 1)
				id = 8;
			if (row == 2)
				id = 12;
			if (row == 3)
				id = 16;
			break;

		}
		return id;
	}

	public Tile upperTile(Tile tile, ArrayList<Tile> tiles) {
		int[] position = new int[2];
		int upperTileId = 0;

		Tile returnTile = null;

		position[0] = getPosition(tile.id)[0];
		position[1] = getPosition(tile.id)[1];
		System.out.println(position.toString());

		if (position[1] > 0) {
			position[1] -= 1;
			upperTileId = getIdByPosition(position[0], position[1]);

			for (Tile x : tiles) {
				if (Integer.parseInt(x.id) == upperTileId)
					return x;
			}
		}
		return returnTile;
	}

	public Tile lowerTile(Tile tile, ArrayList<Tile> tiles) {
		int[] position = new int[2];
		int upperTileId = 0;

		Tile returnTile = null;

		position[0] = getPosition(tile.id)[0];
		position[1] = getPosition(tile.id)[1];
		System.out.println(position.toString());

		if (position[1] < 3) {
			position[1] += 1;
			upperTileId = getIdByPosition(position[0], position[1]);

			for (Tile x : tiles) {
				if (Integer.parseInt(x.id) == upperTileId)
					return x;
			}
		}
		return returnTile;
	}

	public Tile rightTile(Tile tile, ArrayList<Tile> tiles) {
		int[] position = new int[2];
		int upperTileId = 0;

		Tile returnTile = null;

		position[0] = getPosition(tile.id)[0];
		position[1] = getPosition(tile.id)[1];
		System.out.println(position.toString());

		if (position[0] < 3) {
			position[0] += 1;
			upperTileId = getIdByPosition(position[0], position[1]);

			for (Tile x : tiles) {
				if (Integer.parseInt(x.id) == upperTileId)
					return x;
			}
		}
		return returnTile;
	}

	public Tile leftTile(Tile tile, ArrayList<Tile> tiles) {
		int[] position = new int[2];
		int upperTileId = 0;

		Tile returnTile = null;

		position[0] = getPosition(tile.id)[0];
		position[1] = getPosition(tile.id)[1];
		System.out.println(position.toString());

		if (position[0] > 0) {
			position[0] -= 1;
			upperTileId = getIdByPosition(position[0], position[1]);

			for (Tile x : tiles) {
				if (Integer.parseInt(x.id) == upperTileId)
					return x;
			}
		}
		return returnTile;
	}

	public boolean canMoveUp(Tile tile, ArrayList<Tile> tiles) {
		boolean canMove = false;
		if (upperTile(tile, tiles) == null)
			return false;
		else if (tile.getType().equals("Starter") || tile.getType().equals("End")
				|| tile.getType().equals("PipeStatic"))
			return false;
		else if (tile.getType().equals("Empty") && tile.getType().equals("Free"))
			return false;
		else if (upperTile(tile, tiles).getType().equals("Empty") && upperTile(tile, tiles).getProperty().equals("Free")
				&& tile.getType() != "PipeStatic" && tile.getType() != "Starter" && tile.getType() != "End") {
			canMove = true;
			return canMove;
		}
		return canMove;
	}

	public boolean canMoveDown(Tile tile, ArrayList<Tile> tiles) {
		boolean canMove = false;
		if (lowerTile(tile, tiles) == null)
			return false;
		else if (tile.getType().equals("Starter") || tile.getType().equals("End")
				|| tile.getType().equals("PipeStatic"))
			return false;
		else if (tile.getType().equals("Empty") && tile.getType().equals("Free"))
			return false;
		else if (lowerTile(tile, tiles).getType().equals("Empty") && lowerTile(tile, tiles).getProperty().equals("Free")
				&& tile.getType() != "PipeStatic" && tile.getType() != "Starter" && tile.getType() != "End") {
			canMove = true;
			return canMove;
		}
		return canMove;
	}

	public boolean canMoveRight(Tile tile, ArrayList<Tile> tiles) {
		boolean canMove = false;
		if (rightTile(tile, tiles) == null)
			return false;
		else if (tile.getType().equals("Starter") || tile.getType().equals("End")
				|| tile.getType().equals("PipeStatic"))
			return false;
		else if (tile.getType().equals("Empty") && tile.getType().equals("Free"))
			return false;
		else if (rightTile(tile, tiles).getType().equals("Empty") && rightTile(tile, tiles).getProperty().equals("Free")
				&& tile.getType() != "PipeStatic" && tile.getType() != "Starter" && tile.getType() != "End") {
			canMove = true;
			return canMove;
		}
		return canMove;
	}

	public boolean canMoveLeft(Tile tile, ArrayList<Tile> tiles) {
		boolean canMove = false;
		if (leftTile(tile, tiles) == null)
			return false;
		else if (tile.getType().equals("Starter") || tile.getType().equals("End")
				|| tile.getType().equals("PipeStatic"))
			return false;
		else if (tile.getType().equals("Empty") && tile.getType().equals("Free"))
			return false;
		else if (leftTile(tile, tiles).getType().equals("Empty") && leftTile(tile, tiles).getProperty().equals("Free")
				&& tile.getType() != "PipeStatic" && tile.getType() != "Starter" && tile.getType() != "End") {
			canMove = true;
			return canMove;
		}
		return canMove;
	}

	public void moveUpwards(Tile tile, ArrayList<Tile> tiles, ArrayList<ImageView> imgs, Scene scene) {
		int cId = Integer.parseInt(tile.id); // current tile's id
		int uId = cId - 4; // upper tile's id
		int[] uPosition = new int[2]; // the position of upper tile
		int[] cPosition = new int[2]; // the position of current tile

		cPosition[0] = getPosition("" + cId)[0];
		cPosition[1] = getPosition("" + cId)[1];

		uPosition[0] = cPosition[0];
		uPosition[1] = cPosition[1] - 1;

		Tile upperTile = new Tile();
		for (Tile x : tiles) {
			if (Integer.parseInt(x.id) == uId)
				upperTile = x;
		}

		upperTile.setId(cId + "");
		tile.setId(uId + "");

		GridPane.setConstraints(imgs.get(cId - 1), uPosition[0], uPosition[1]);
		GridPane.setConstraints(imgs.get(uId - 1), cPosition[0], cPosition[1]);

		Collections.swap(imgs, (cId - 1), (uId - 1));
		Collections.swap(tiles, (cId - 1), (uId - 1));
		
		moves++;
		stage.setTitle("Puzzle         "+"Level "+levelNum+"         Moves "+moves);
		
		moveSound();
		setListeners(imgs, tiles, scene);

		if (checkPath(tiles)) {
			Animation(pp, tiles, imgs);
			System.out.println(path);
		}

	}

	public void moveDownwards(Tile tile, ArrayList<Tile> tiles, ArrayList<ImageView> imgs, Scene scene) {
		int cId = Integer.parseInt(tile.id); // current tile's id
		int lId = cId + 4; // lower tile's id
		int[] lPosition = new int[2]; // the position of lower tile
		int[] cPosition = new int[2]; // the position of current tile

		cPosition[0] = getPosition("" + cId)[0];
		cPosition[1] = getPosition("" + cId)[1];

		lPosition[0] = cPosition[0];
		lPosition[1] = cPosition[1] + 1;

		Tile lowerTile = new Tile();
		for (Tile x : tiles) {
			if (Integer.parseInt(x.id) == lId)
				lowerTile = x;
		}

		lowerTile.setId(cId + "");
		tile.setId(lId + "");

		GridPane.setConstraints(imgs.get(cId - 1), lPosition[0], lPosition[1]);
		GridPane.setConstraints(imgs.get(lId - 1), cPosition[0], cPosition[1]);

		Collections.swap(imgs, (cId - 1), (lId - 1));
		Collections.swap(tiles, (cId - 1), (lId - 1));
		moves++;
		stage.setTitle("Puzzle         "+"Level "+levelNum+"         Moves "+moves);
		moveSound();
		setListeners(imgs, tiles, scene);
		if (checkPath(tiles)) {
			Animation(pp, tiles, imgs);
			System.out.println(path);
		}
	}

	public void moveRight(Tile tile, ArrayList<Tile> tiles, ArrayList<ImageView> imgs, Scene scene) {
		int cId = Integer.parseInt(tile.id); // current tile's id
		int rId = cId + 1; // right tile's id
		int[] rPosition = new int[2]; // right tile's position
		int[] cPosition = new int[2]; // current tile's position

		cPosition[0] = getPosition("" + cId)[0];
		cPosition[1] = getPosition("" + cId)[1];

		rPosition[0] = cPosition[0] + 1;
		rPosition[1] = cPosition[1];

		Tile rightTile = new Tile();
		for (Tile x : tiles) {
			if (Integer.parseInt(x.id) == rId)
				rightTile = x;
		}

		rightTile.setId(cId + "");
		tile.setId(rId + "");

		GridPane.setConstraints(imgs.get(cId - 1), rPosition[0], rPosition[1]);
		GridPane.setConstraints(imgs.get(rId - 1), cPosition[0], cPosition[1]);

		Collections.swap(imgs, (cId - 1), (rId - 1));
		Collections.swap(tiles, (cId - 1), (rId - 1));
		
		moves++;
		stage.setTitle("Puzzle         "+"Level "+levelNum+"         Moves "+moves);
		
		moveSound();
		setListeners(imgs, tiles, scene);

		if (checkPath(tiles)) {
			Animation(pp, tiles, imgs);
			System.out.println(path);
		}

	}

	public void moveLeft(Tile tile, ArrayList<Tile> tiles, ArrayList<ImageView> imgs, Scene scene) {
		int cId = Integer.parseInt(tile.id); // current tile's id
		int lId = cId - 1; // left tile's id
		int[] lPosition = new int[2]; // left tile's position
		int[] cPosition = new int[2]; // current tile's position

		cPosition[0] = getPosition("" + cId)[0];
		cPosition[1] = getPosition("" + cId)[1];

		lPosition[0] = cPosition[0] - 1;
		lPosition[1] = cPosition[1];

		Tile leftTile = new Tile();
		for (Tile x : tiles) {
			if (Integer.parseInt(x.id) == lId)
				leftTile = x;
		}

		leftTile.setId(cId + "");
		tile.setId(lId + "");

		GridPane.setConstraints(imgs.get(cId - 1), lPosition[0], lPosition[1]);
		GridPane.setConstraints(imgs.get(lId - 1), cPosition[0], cPosition[1]);

		Collections.swap(imgs, (cId - 1), (lId - 1));
		Collections.swap(tiles, (cId - 1), (lId - 1));
		
		moves++;
		stage.setTitle("Puzzle         "+"Level "+levelNum+"         Moves "+moves);
		
		moveSound();
		setListeners(imgs, tiles, scene);

		if (checkPath(tiles)) {
			Animation(pp, tiles, imgs);
			System.out.println(path);
		}

	}

	public boolean checkPath(ArrayList<Tile> tiles) {
		boolean isComplete = false;
		Tile starter = getStarterTile(tiles);
		Tile currentTile = starter;
		Tile nextTile = new Tile();
		Tile previousTile = new Tile();
		path.add(getStarterTile(tiles).getId());
		while (true) {
			if (currentTile.getProperty().equals("Horizontal")) {
				int cases = 0;
				
				if (Integer.parseInt(previousTile.id) == Integer.parseInt(currentTile.id) - 1) {
					path.add("Horizontal-right");
					if (currentTile.getType().equals("End"))
						return true;
					nextTile = tileById(Integer.parseInt(currentTile.id) + 1, tiles);
					cases = 1;
				} else if (Integer.parseInt(previousTile.id) == Integer.parseInt(currentTile.id) + 1) {
					path.add("Horizontal-left");
					if (currentTile.getType().equals("End"))
						return true;
					nextTile = tileById(Integer.parseInt(currentTile.id) - 1, tiles);
					cases = 2;
				} else { // if there is no previous tile. That is, if it's the first tile.
					path.add("Horizontal-left");
					if (currentTile.getType().equals("End"))
						return true;
					nextTile = tileById(Integer.parseInt(currentTile.id) + 1, tiles);
					cases = 2;
				}

				if (nextTile == null && (!currentTile.getType().equals("End"))) {
					path.clear();
					return false;
				}

				switch (cases) {
				case 1:
					if (nextTile != null) {
						if (!(nextTile.getProperty().equals("Horizontal") || nextTile.getProperty().equals("00")
								|| nextTile.getProperty().equals("10"))) {
							path.clear();
							return false;
						}
					}
					break;
				case 2:
					if (nextTile != null) {
						if (!(nextTile.getProperty().equals("Horizontal") || nextTile.getProperty().equals("01")
								|| nextTile.getProperty().equals("11"))) {
							path.clear();
							return false;
						}
					}
					break;
				default:
					System.out.println("No case detected");
					break;
				}
			} else if (currentTile.getProperty().equals("Vertical")) {
				int cases = 0;
				
				if (Integer.parseInt(previousTile.id) == Integer.parseInt(currentTile.id) - 4) {
					path.add("Vertical-down");
					if (currentTile.getType().equals("End"))
						return true;
					nextTile = tileById(Integer.parseInt(currentTile.id) + 4, tiles);
					cases = 1;
				} else if (Integer.parseInt(previousTile.id) == Integer.parseInt(currentTile.id) + 4) {
					path.add("Vertical-up");
					if (currentTile.getType().equals("End"))
						return true;
					nextTile = tileById(Integer.parseInt(currentTile.id) - 4, tiles);
					cases = 2;
				} else { // if there is no previous tile. That is, if it's the first tile.
					path.add("Vertical-down");
					if (currentTile.getType().equals("End"))
						return true;
					nextTile = tileById(Integer.parseInt(currentTile.id) + 4, tiles);
					cases = 1;
				}

				if (nextTile == null) {
					path.clear();
					return false;
				}

				switch (cases) {
				case 1:
					if (nextTile != null) {
						if (!(nextTile.getProperty().equals("Vertical") || nextTile.getProperty().equals("00")
								|| nextTile.getProperty().equals("01"))) {
							path.clear();
							return false;
						}
					}
					break;
				case 2:
					if (nextTile != null) {
						if (!(nextTile.getProperty().equals("Vertical") || nextTile.getProperty().equals("10")
								|| nextTile.getProperty().equals("11"))) {
							path.clear();
							return false;
						}
					}
					break;

				default:
					System.out.println("No case detected");
					break;
				}
			} else if (currentTile.getProperty().equals("00")) {
				int cases = 0;
				
				if (Integer.parseInt(previousTile.id) == Integer.parseInt(currentTile.id) - 4) {
					path.add("00-left");
					if (currentTile.getType().equals("End"))
						return true;
					nextTile = tileById(Integer.parseInt(currentTile.id) - 1, tiles);
					cases = 1;
				} else if (Integer.parseInt(previousTile.id) == Integer.parseInt(currentTile.id) - 1) {
					path.add("00-up");
					if (currentTile.getType().equals("End"))
						return true;
					nextTile = tileById(Integer.parseInt(currentTile.id) - 4, tiles);
					cases = 2;
				}

				if (nextTile == null) {
					path.clear();
					return false;
				}

				switch (cases) {
				case 1:
					if (nextTile != null) {
						if (!(nextTile.getProperty().equals("Horizontal") || nextTile.getProperty().equals("01")
								|| nextTile.getProperty().equals("11"))) {
							path.clear();
							return false;
						}
					}
					break;
				case 2:
					if (nextTile != null) {
						if (!(nextTile.getProperty().equals("Vertical") || nextTile.getProperty().equals("10")
								|| nextTile.getProperty().equals("11"))) {
							path.clear();
							return false;
						}
					}
					break;
				default:
					System.out.println("No case detected");
					break;
				}
			} else if (currentTile.getProperty().equals("01")) {
				int cases = 0;
				if (Integer.parseInt(previousTile.id) == Integer.parseInt(currentTile.id) - 4) {
					path.add("01-rigt");
					if (currentTile.getType().equals("End"))
						return true;
					nextTile = tileById(Integer.parseInt(currentTile.id) + 1, tiles);
					cases = 1;
				} else if (Integer.parseInt(previousTile.id) == Integer.parseInt(currentTile.id) + 1) {
					path.add("01-up");
					if (currentTile.getType().equals("End"))
						return true;
					nextTile = tileById(Integer.parseInt(currentTile.id) - 4, tiles);
					cases = 2;
				}

				if (nextTile == null) {
					path.clear();
					return false;
				}

				switch (cases) {
				case 1:
					if (nextTile != null) {
						if (!(nextTile.getProperty().equals("Horizontal") || nextTile.getProperty().equals("00")
								|| nextTile.getProperty().equals("10"))) {
							path.clear();
							return false;
						}
					}
					break;
				case 2:
					if (nextTile != null) {
						if (!(nextTile.getProperty().equals("Horizontal") || nextTile.getProperty().equals("10")
								|| nextTile.getProperty().equals("11"))) {
							path.clear();
							return false;
						}
					}
					break;
				default:
					System.out.println("No case detected");
					break;
				}

			} else if (currentTile.getProperty().equals("10")) {
				int cases = 0;
				if (Integer.parseInt(previousTile.id) == Integer.parseInt(currentTile.id) - 1) {
					path.add("10-down");
					if (currentTile.getType().equals("End"))
						return true;
					nextTile = tileById(Integer.parseInt(currentTile.id) + 4, tiles);
					cases = 1;
				} else if (Integer.parseInt(previousTile.id) == Integer.parseInt(currentTile.id) + 4) {
					path.add("10-left");
					if (currentTile.getType().equals("End"))
						return true;
					nextTile = tileById(Integer.parseInt(currentTile.id) - 1, tiles);
					cases = 2;
				}
				if (nextTile == null) {
					path.clear();
					return false;
				}

				switch (cases) {
				case 1:
					if (nextTile != null) {
						if (!(nextTile.getProperty().equals("Vertical") || nextTile.getProperty().equals("00")
								|| nextTile.getProperty().equals("01"))) {
							path.clear();
							return false;
						}
					}
					break;
				case 2:
					if (nextTile != null) {
						if (!(nextTile.getProperty().equals("Horizontal") || nextTile.getProperty().equals("01")
								|| nextTile.getProperty().equals("11"))) {
							path.clear();
							return false;
						}
					}
					break;
				default:
					System.out.println("No case detected");
					break;
				}
			} else if (currentTile.getProperty().equals("11")) {
				int cases = 0;
				if (Integer.parseInt(previousTile.id) == Integer.parseInt(currentTile.id) + 1) {
					path.add("11-down");
					if (currentTile.getType().equals("End"))
						return true;
					nextTile = tileById(Integer.parseInt(currentTile.id) + 4, tiles);
					cases = 1;
				} else if (Integer.parseInt(previousTile.id) == Integer.parseInt(currentTile.id) + 4) {
					path.add("11-right");
					if (currentTile.getType().equals("End"))
						return true;
					nextTile = tileById(Integer.parseInt(currentTile.id) + 1, tiles);
					cases = 2;
				}

				if (nextTile == null) {
					path.clear();
					return false;
				}

				switch (cases) {
				case 1:
					if (nextTile != null) {
						if (!(nextTile.getProperty().equals("Vertical") || nextTile.getProperty().equals("00")
								|| nextTile.getProperty().equals("01"))) {
							path.clear();
							return false;
						}
					}
					break;
				case 2:
					if (nextTile != null) {
						if (!(nextTile.getProperty().equals("Horizontal") || nextTile.getProperty().equals("00")
								|| nextTile.getProperty().equals("10"))) {
							path.clear();
							return false;
						}
					}
					break;
				default:
					System.out.println("No case detected");
					break;
				}
			}

			else { // if there is no pipe at all, return false
				path.clear();
				return false;
			}

			if (currentTile.getType().equals("End"))
				return true;

			previousTile = currentTile;
			currentTile = nextTile;
			System.out.println(path);
		}
	}

	public void Animation(Pane np, ArrayList<Tile> tiles, ArrayList<ImageView> imgs) {
		ImageView circle = this.circle;
		ArrayList<PathTransition> pt = new ArrayList<PathTransition>();

		String starterId = path.get(0);
		int sPosX = 0; // starting X-position of the line to be created
		int sPosY = 0; // starting Y-position of the line to be created
		int ePosX = 0; // ending X-position of the line to be created
		int ePosY = 0; // ending Y-position of the line to be created

		int[] pos = new int[2];
		pos[0] = getPosition(starterId)[0];
		pos[1] = getPosition(starterId)[1];

		sPosX = pos[0] * 100 + 50;
		sPosY = pos[1] * 100 + 50;

		for (int i = 1; i < path.size(); i++) {
			switch (path.get(i)) {
			case "Vertical-down":
				ePosX = sPosX;
				if(i == 1 || i ==path.size()-1)
				ePosY = sPosY + 50;
				else
					ePosY = sPosY + 100;
				
				Line line1 = new Line(sPosX,sPosY,ePosX,ePosY);
				pt.add(new PathTransition(Duration.millis(500),line1,circle));
				
				break;
				
			case "Vertical-up":
				ePosX = sPosX;
				if(i == 1 || i ==path.size()-1)
				ePosY = sPosY - 50;
				else
					ePosY = sPosY - 100;
				
				Line line1up = new Line(sPosX,sPosY,ePosX,ePosY);
				pt.add(new PathTransition(Duration.millis(500),line1up,circle));
				
				break;
			
			case "Horizontal-right":
				if(i ==1 || i ==path.size()-1)
					ePosX = sPosX + 50;
				else
				ePosX = sPosX + 100;
				
				ePosY = sPosY;
				Line line2 = new Line(sPosX,sPosY,ePosX,ePosY);
				pt.add(new PathTransition(Duration.millis(500),line2,circle));
				
				break;
			case "Horizontal-left":
				if(i ==1 || i ==path.size()-1)
					ePosX = sPosX - 50;
				else
				ePosX = sPosX - 100;
				
				ePosY = sPosY;
				Line line2down = new Line(sPosX,sPosY,ePosX,ePosY);
				pt.add(new PathTransition(Duration.millis(500),line2down,circle));
				
				break;
			case "00-left":
				ePosX = sPosX - 50;
				ePosY = sPosY + 50;
				Line line3left = new Line(sPosX,sPosY,ePosX,ePosY);
				pt.add(new PathTransition(Duration.millis(500),line3left,circle));
				break;
			case "00-up":
				ePosX = sPosX + 50;
				ePosY = sPosY - 50;
				Line line3up = new Line(sPosX,sPosY,ePosX,ePosY);
				pt.add(new PathTransition(Duration.millis(500),line3up,circle));
				break;
			case "01-rigt":
				ePosX = sPosX +50;
				ePosY = sPosY + 50;
				Line line4right = new Line(sPosX,sPosY,ePosX,ePosY);
				pt.add(new PathTransition(Duration.millis(500),line4right,circle));
				break;
			case "01-up":
				ePosX = sPosX - 50;
				ePosY = sPosY - 50;
				Line line4up = new Line(sPosX,sPosY,ePosX,ePosY);
				pt.add(new PathTransition(Duration.millis(500),line4up,circle));
				break;
			case "10-down":
				ePosX = sPosX + 50;
				ePosY = sPosY + 50;
				Line line5down = new Line(sPosX,sPosY,ePosX,ePosY);
				pt.add(new PathTransition(Duration.millis(500),line5down,circle));
				break;
			case "10-left":
				ePosX = sPosX - 50;
				ePosY = sPosY - 50;
				Line line5left = new Line(sPosX,sPosY,ePosX,ePosY);
				pt.add(new PathTransition(Duration.millis(500),line5left,circle));
				break;
			case "11-down":
				ePosX = sPosX - 50;
				ePosY = sPosY + 50;
				Line line6down = new Line(sPosX,sPosY,ePosX,ePosY);
				pt.add(new PathTransition(Duration.millis(500),line6down,circle));
				break;
			case "11-right":
				ePosX = sPosX + 50;
				ePosY = sPosY - 50;
				Line line6right = new Line(sPosX,sPosY,ePosX,ePosY);
				pt.add(new PathTransition(Duration.millis(500),line6right,circle));
				break;
			}
			
			sPosX = ePosX;
			sPosY = ePosY;
		}
		int x = 0;
		for(int j = 0; j<pt.size();j++) {
			if(j == 0) {
				pt.get(j).setCycleCount(1);
				pt.get(j).play();
			}else {
				x +=500;
				pt.get(j).setDelay(Duration.millis(x));
				pt.get(j).setCycleCount(1);
			}
		}
		for(PathTransition p : pt) {
			p.play();
		}
		//level up fade transition.
		ImageView imageView = new ImageView("levelUp.png");
	    imageView.setX(113);
	    imageView.setY(140);
	    imageView.setFitHeight(100);
	    imageView.setFitWidth(175);
		FadeTransition ft =  new FadeTransition(Duration.millis(1000), imageView);
		    ft.setFromValue(1.0);
		    ft.setToValue(0.0);
		    ft.setCycleCount(1);
		 //finish image f transition
		    ImageView fImageView = new ImageView("finish.jpg");
		    fImageView.setX(83);
		    fImageView.setY(115);
		    fImageView.setFitHeight(165);
		    fImageView.setFitWidth(235);
			FadeTransition ft2 =  new FadeTransition(Duration.millis(6000), fImageView);
			    ft2.setFromValue(1.0);
			    ft2.setToValue(0.1);
			    ft2.setCycleCount(1);
		 // play fade transition and level up sound after the animation then go to next level.   
		pt.get(pt.size()-1).setOnFinished(e->{
			if(currentLevel != levels[levels.length -1]) {
				np.getChildren().add(imageView);
				ft.play();
				levelUpSound();
				//
				ft.setOnFinished(e1->{
					moves=0;
					levelNum++;
					stage.setTitle("Puzzle         "+"Level "+levelNum+"         Moves "+moves);
					goToNextLevel(tiles, imgs);
				});
			}
			//play the level up sound after last level.
			if(currentLevel.equals(levels[levels.length -1]) ) {
				completedSound();
				np.getChildren().add(fImageView);
				ft2.play();
			}
		});
		
		for(ImageView img : imgs) {
			img.setOnMouseDragged(null);
		}

	}

	public Tile getStarterTile(ArrayList<Tile> tiles) {
		Tile tile = null;
		for (Tile x : tiles) {
			if (x.getType().equals("Starter"))
				tile = x;
		}

		return tile;
	}
	public void moveSound() {
		AudioClip sound;
		URL url = Project.class.getResource("beep-timber.aif");
		sound = Applet.newAudioClip(url);
		sound.play();
	}
	public void levelUpSound() {
		AudioClip sound2;
		URL url2 = Project.class.getResource("levelUpSound.wav");
		sound2 = Applet.newAudioClip(url2);
		sound2.play();
	}
	public void completedSound() {
		AudioClip sound2;
		URL url2 = Project.class.getResource("completed.wav");
		sound2 = Applet.newAudioClip(url2);
		sound2.play();
	}
}
