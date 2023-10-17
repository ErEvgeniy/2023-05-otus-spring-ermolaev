package ru.otus.homework.event;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Book;
import ru.otus.homework.repository.CommentRepository;

@Component
@RequiredArgsConstructor
public class MongoBookEventsListener extends AbstractMongoEventListener<Book> {

	private final CommentRepository commentRepository;

	@Override
	public void onAfterDelete(AfterDeleteEvent<Book> event) {
		super.onAfterDelete(event);
		Document source = event.getSource();
		String bookId = source.get("_id").toString();
		commentRepository.deleteAllByBookId(bookId);
	}

}
