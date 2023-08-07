package ru.otus.homework.event;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Author;
import ru.otus.homework.exception.NonConsistentOperation;
import ru.otus.homework.repository.BookRepository;

@Component
@RequiredArgsConstructor
public class MongoAuthorEventsListener extends AbstractMongoEventListener<Author> {

	private final BookRepository bookRepository;

	@Override
	public void onBeforeDelete(BeforeDeleteEvent<Author> event) {
		super.onBeforeDelete(event);
		Document source = event.getSource();
		String authorId = source.get("_id").toString();
		if (bookRepository.existsBookByAuthorId(authorId)) {
			throw new NonConsistentOperation(String.format("Author with id %s is used", authorId));
		}
	}

}
