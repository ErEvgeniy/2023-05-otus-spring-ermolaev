package ru.otus.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Flour;
import ru.otus.homework.domain.Grain;
import ru.otus.homework.service.FlourService;

@Service
public class FlourServiceImpl implements FlourService {

    @Override
    public Flour toFlour(Grain grain) {
        if (grain != null) {
            return new Flour();
        }
        return null;
    }
}
