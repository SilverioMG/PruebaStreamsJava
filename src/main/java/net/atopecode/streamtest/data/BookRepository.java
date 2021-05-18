package net.atopecode.streamtest.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookRepository {

	private List<Book> books = new ArrayList<>();
	
	public BookRepository() {
		generateDefaultData();
	}
	
	private void generateDefaultData() {
		books = Arrays.asList(
				new Book("Book1", "José Serrano", 10.0),
				new Book("Book2", "Luís Peras", 15.0),
				new Book("Book3", "Bartolomé Rojas", 50.99)
				);
	}
	
	public List<Book> getBooks(){
		return this.books;
	}
}
