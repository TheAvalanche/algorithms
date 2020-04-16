package org.symhodia.search;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test2 {

	public static void main(String[] args) {
		List<Figure> figures = new ArrayList<>();

		figures.stream()
				.filter(it -> it.getColor() == Color.GREEN)
				.collect(Collectors.toList());

		figures.stream()
				.map(it -> it.getShape())
				.collect(Collectors.toList());
	}

	public class Figure {
		private Color color;
		private Shape shape;
		private int size;

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public Shape getShape() {
			return shape;
		}

		public void setShape(Shape shape) {
			this.shape = shape;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public boolean isGreen() {
			return false;
		}
	}

	enum Color {
		ORANGE, GREEN, RED
	}

	enum Shape {
		CIRCLE, SQUARE, TRIANGLE
	}

}
