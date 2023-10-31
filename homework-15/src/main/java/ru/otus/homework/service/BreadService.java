package ru.otus.homework.service;

import ru.otus.homework.domain.Bread;
import ru.otus.homework.domain.Dough;

public interface BreadService {

    Bread toBread(Dough dough);

}
