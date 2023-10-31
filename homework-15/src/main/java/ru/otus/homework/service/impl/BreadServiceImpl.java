package ru.otus.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Bread;
import ru.otus.homework.domain.Dough;
import ru.otus.homework.service.BreadService;

@Service
public class BreadServiceImpl implements BreadService {

    @Override
    public Bread toBread(Dough dough) {
        if (dough != null) {
            return new Bread();
        }
        return null;
    }

}
