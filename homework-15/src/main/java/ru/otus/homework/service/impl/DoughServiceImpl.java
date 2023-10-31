package ru.otus.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Dough;
import ru.otus.homework.domain.Flour;
import ru.otus.homework.service.DoughService;

@Service
public class DoughServiceImpl implements DoughService {

    @Override
    public Dough toDough(Flour flour) {
        if (flour != null) {
            return new Dough();
        }
        return null;
    }

}
