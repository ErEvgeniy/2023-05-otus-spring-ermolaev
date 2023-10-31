package ru.otus.homework.service;

import ru.otus.homework.domain.Dough;
import ru.otus.homework.domain.Flour;

public interface DoughService {

    Dough toDough(Flour flour);

}
