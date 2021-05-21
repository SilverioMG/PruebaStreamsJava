package net.atopecode.streamtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.atopecode.streamtest.data.Book;
import net.atopecode.streamtest.data.BookRepository;

public class StreamsUnitTest {

	
	private BookRepository bookRepository;
	private List<Book> bookList;
	
	public StreamsUnitTest() {
		this.bookRepository  = new BookRepository();
		this.bookList = new ArrayList<>();
	}
	
	@BeforeEach
	public void beforeEachTest() {
		//Antes de cada test se refresca la lista de libros:
		bookList = new ArrayList<Book>();
		bookList.addAll(bookRepository.getBooks());
	}
	
	@Test
	public void collect() {
		List<Integer> listCollected = Stream.of(0, 1, 2, 3, 4, 5)
				.collect(Collectors.toList());
		assertTrue(listCollected.size() == 6);
		
		Set<String> setCollected = Stream.of("0", "0", "1", "1", "2", "2", "3", "3", "4", "4", "5", "5")
				.collect(Collectors.toSet());
		assertTrue(setCollected.size() == 6);
	}

	@Test
	public void peek() {
		//A diferencia de 'map()', con 'peek()' hace algo con el objeto pero devuelve el mismo objeto, no otro tipo de dato.
		//Con 'peek()' se realizan operaciones sobre un objeto y se devuelve ese mismo objeto. Con 'map()' se recibe un objeto y se devuelve uno de otro tipo.
		List<Book> books = bookList.stream()
			.peek(book -> book.setAuthor("ATopeCode"))
			.peek(book -> System.out.println(book))
			.collect(Collectors.toList());
	}
	
	@Test
	public void map() {
		List<String> titles = bookList.stream()
				.map(book -> book.getTitle())
				.peek(title -> System.out.println(title))
				.collect(Collectors.toList());
		
		assertEquals(titles.size(), bookList.size());
	}
	
	@Test
	public void forEach() {
		bookList.stream()
			.forEach(book -> {
				double newPrice = book.getPrice();
				newPrice *= 2;
				book.setPrice(newPrice);
				System.out.println(book);
			});
	}
	
	@Test
	public void mapAndForEach() {
		List<String> titles = bookList.stream()
				.map(book -> book.getTitle())
				.peek(title -> System.out.println(title))
				.collect(Collectors.toList());
		
		assertEquals(titles.size(), bookList.size());
		bookList.forEach(
				(book) -> {
					assertTrue(titles.contains(book.getTitle()));
				});
	}
	
	@Test
	public void filter() {
		List<Book> books = bookList.stream()
				.filter(book -> book.getPrice() >= 15)
				.peek(book -> System.out.println(book))
				.collect(Collectors.toList());
		
		assertTrue(books.size() == 2);
	}
	
	@Test
	public void findFirst() {
		Book bookFirst = bookList.stream()
					.filter(book -> book.getPrice() >= 15)
					.peek(System.out::println)
					.findFirst()
					.orElse(null);
		
		assertTrue(bookFirst.getPrice() >= 15);
		
		//Nota.- Hay 2 libros que cumplen la condición del 'filter()' pero como se utiliza evaluación 'lazyEvaluation' al existir el 'findFirst()'
		//ya no se realiza la siguiente iteración sobre el stream. Se observa que pasa esto porque en el 'peek()' solo se visualiza el primer
		//elemento que cumple la condición y que justamente es el resultado de la consulta.
	}
	
	@Test
	public void toArray() {
		//Como se hacía antes de los Streams (2 formas distintas):
		Book[] booksArray = bookList.toArray(Book[]::new);
		booksArray = bookList.toArray(new Book[bookList.size()]);
				
		//Como se hace con Streams (es lo mismo pero son Streams):
		booksArray = bookList.stream().toArray(Book[]::new);
		
		//Convertimos el array en un stream para visualizar su contenido más fácilmente sin tener que utilizar el 'for(Book book: booksArray)' de toda la vida:
		Stream.of(booksArray)
			.peek(System.out::println)
			.collect(Collectors.toList());
	}
	
	@Test
	public void sorted() {
		List<Book> books = bookList.stream()
				.sorted((book1, book2) -> book1.getAuthor().compareToIgnoreCase(book2.getAuthor()))
				.peek(System.out::println)
				.collect(Collectors.toList());
		
		assertTrue(books.get(0).getAuthor().contains("Bartolomé"));
		assertTrue(books.get(1).getAuthor().contains("José"));
		assertTrue(books.get(2).getAuthor().contains("Luís"));
	}
	
	@Test
	public void min() {
		Book book = bookList.stream()
				.min((book1, book2) ->  book1.getPrice().compareTo(book2.getPrice()))
				.orElse(null);
						
		System.out.println(book);
		assertEquals(book.getPrice(), 10);
	}
	
	@Test
	public void max() {
		Book book = bookList.stream()
				.min((book1, book2) -> { 
					return (-1 * book1.getPrice().compareTo(book2.getPrice()));
				})
				.orElse(null);
						
		System.out.println(book);
		assertEquals(book.getPrice(), 50.99);
	}
	
	@Test
	public void distinct() {
		bookList.add(new Book(bookList.get(0).getTitle(), "", 0d));
		List<String> distinctTitles = bookList.stream()
				.map(book -> book.getTitle())
				.distinct()
				.peek(System.out::println)
				.collect(Collectors.toList());
		
		assertEquals(distinctTitles.size(), 3);
	}
	

	@Test
	public void allMatch_anyMatch_noneMatch() {
	    List<Integer> intList = Arrays.asList(2, 4, 5, 6, 8);
	    
	    boolean allEven = intList.stream().allMatch(i -> i % 2 == 0);
	    boolean oneEven = intList.stream().anyMatch(i -> i % 2 == 0);
	    boolean noneMultipleOfThree = intList.stream().noneMatch(i -> i % 3 == 0);
	    
	    assertEquals(allEven, false);
	    assertEquals(oneEven, true);
	    assertEquals(noneMultipleOfThree, false);
	}
	
}
