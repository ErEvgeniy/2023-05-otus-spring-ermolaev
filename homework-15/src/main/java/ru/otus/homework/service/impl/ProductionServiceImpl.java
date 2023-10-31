package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Bread;
import ru.otus.homework.domain.Grain;
import ru.otus.homework.service.ProductionGateway;
import ru.otus.homework.service.ProductionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductionServiceImpl implements ProductionService {

    private final ProductionGateway productionGateway;

    @Override
    public void start() {
        List<Grain> grainList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            grainList.add(new Grain());
        }
        Collection<Bread> breadCollection = productionGateway.process(grainList);
        log.info("From {} grain produce {} bread", grainList.size(), breadCollection.size());
    }

}
