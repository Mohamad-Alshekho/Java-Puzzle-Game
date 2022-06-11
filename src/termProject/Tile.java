package termProject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Tile {
	String id;
	String type;
	String property;

	Tile(){
		id = "0";
	}
	public Tile(String id, String type, String property) {
		super();
		this.id = id;
		this.type = type;
		this.property = property;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	@SuppressWarnings("exports") // ???
	public ImageView returnImage() {
		switch (type) {
		case "Starter":
			if (property.equals("Vertical")) {
				return new ImageView("v-start.png");
			} else if (property.equals("Horizontal")) {
				return new ImageView("h-start.png");
			}
			break;
		case "End":
			if (property.equals("Vertical")) {
				return new ImageView("v-end.png");
			} else if (property.equals("Horizontal")) {
				return new ImageView("h-end.png");
			}
			break;
		case "Empty":
			if (property.equals("none")) {
				return new ImageView("empty.png");
			} else if (property.equals("Free")) {
				return new ImageView("empty-free.png");
			}
			break;
		case "Pipe":
			if (property.equals("00"))
				return new ImageView("00.png");
			else if (property.equals("01"))
				return new ImageView("01.png");
			else if (property.equals("10"))
				return new ImageView("10.png");
			else if (property.equals("11"))
				return new ImageView("11.png");
			else if (property.equals("Horizontal"))
				return new ImageView("h-pipe.png");
			else if (property.equals("Vertical"))
				return new ImageView("v-pipe.png");
			break;
		case "PipeStatic":
			if(property.equals("Vertical")) {
				return new ImageView("v-pipestatic.png");
			}
			else if(property.equals("Horizontal")) {
				return new ImageView("h-pipestatic.png");
			}
			else if(property.equals("01")) {
				return new ImageView("01-pipestatic.png");
			}
			break;

		}
		return null;
	}
}
