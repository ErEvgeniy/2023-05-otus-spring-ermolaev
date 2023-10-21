package ru.otus.homework.event;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exception.NonConsistentOperation;
import ru.otus.homework.repository.BookRepository;

@Component
@RequiredArgsConstructor
public class MongoGenreEventsListener extends AbstractMongoEventListener<Genre> {

	private final BookRepository bookRepository;

	@Override
	public void onBeforeDelete(BeforeDeleteEvent<Genre> event) {
		super.onBeforeDelete(event);
		Document source = event.getSource();
		String genreId = source.get("_id").toString();
		if (bookRepository.existsBookByGenreId(genreId)) {
			throw new NonConsistentOperation(String.format("Genre with id %s is used", genreId));
		}
	}

}
