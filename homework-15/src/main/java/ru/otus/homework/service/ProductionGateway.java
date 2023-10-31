package ru.otus.homework.service;


import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.homework.domain.Bread;
import ru.otus.homework.domain.Grain;

import java.util.Collection;

@MessagingGateway
public interface ProductionGateway {

	@Gateway(requestChannel = "grainChannel", replyChannel = "breadChannel")
	Collection<Bread> process(Collection<Grain> grains);
}
