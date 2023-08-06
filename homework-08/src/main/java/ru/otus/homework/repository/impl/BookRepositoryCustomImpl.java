package ru.otus.homework.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.repository.BookRepositoryCustom;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;


@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

	private final MongoTemplate template;

	@Override
	public Optional<Comment> findCommentById(String commentId) {
		Aggregation aggregation = newAggregation(
			match(Criteria.where("comments._id").is(commentId)),
			unwind("comments"),
			project()
				.and("comments._id").as("_id")
				.and("comments.text").as("text"),
			match(Criteria.where("_id").is(commentId))
		);
		AggregationResults<Comment> results = template.aggregate(aggregation, Book.class, Comment.class);
		return Optional.ofNullable(results.getUniqueMappedResult());
	}

	@Override
	public List<Comment> findCommentsByBookId(String bookId) {
		Aggregation aggregation = newAggregation(
			match(Criteria.where("id").is(bookId)),
			unwind("comments"),
			project().andExclude("_id")
				.and("comments._id").as("_id")
				.and("comments.text").as("text")
		);
		return template.aggregate(aggregation, Book.class, Comment.class).getMappedResults();
	}

	@Override
	public List<Comment> findAllComments() {
		Aggregation aggregation = newAggregation(
			unwind("comments"),
			project().andExclude("_id")
				.and("comments.id").as("_id")
				.and("comments.text").as("text")
		);
		return template.aggregate(aggregation, Book.class, Comment.class).getMappedResults();
	}

	@Override
	public void deleteCommentById(String commentId) {
		Query query = new Query(Criteria.where("comments.id").is(commentId));
		Update update = new Update().pull("comments", Query.query(Criteria.where("id").is(commentId)));
		template.updateFirst(query, update, Book.class);
	}

	public Comment saveComment(Comment comment, Book book) {
		if (comment.getId() == null) {
			comment.setId(UUID.randomUUID().toString());
		}

		if (book.getComments() == null) {
			book.setComments(Collections.singletonList(comment));
		} else {
			book.getComments().add(comment);
		}

		template.save(book);
		return comment;
	}

	@Override
	public Optional<Book> findBookByCommentId(String commentId) {
		Query query = Query.query(Criteria.where("comments")
			.elemMatch(Criteria.where("_id").is(commentId)));
		return Optional.ofNullable(template.findOne(query, Book.class));
	}
}
