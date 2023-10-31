package ru.otus.homework.service;

import ru.otus.homework.domain.Flour;
import ru.otus.homework.domain.Grain;

public interface FlourService {

    Flour toFlour(Grain grain);

}
