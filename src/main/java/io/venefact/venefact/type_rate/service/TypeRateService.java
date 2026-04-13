package io.venefact.venefact.type_rate.service;

import io.venefact.venefact.events.BeforeDeleteTypeRate;
import io.venefact.venefact.type_rate.domain.TypeRate;
import io.venefact.venefact.type_rate.model.TypeRateDTO;
import io.venefact.venefact.type_rate.repos.TypeRateRepository;
import io.venefact.venefact.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TypeRateService {

    private final TypeRateRepository typeRateRepository;
    private final ApplicationEventPublisher publisher;

    public TypeRateService(final TypeRateRepository typeRateRepository,
            final ApplicationEventPublisher publisher) {
        this.typeRateRepository = typeRateRepository;
        this.publisher = publisher;
    }

    public List<TypeRateDTO> findAll() {
        final List<TypeRate> typeRates = typeRateRepository.findAll(Sort.by("id"));
        return typeRates.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public TypeRateDTO getById(final Long id) {
        return typeRateRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(NotFoundException::new);
    }

    public TypeRateDTO create(final TypeRateDTO typeRateDTO) {
        final TypeRate typeRate = new TypeRate();
        typeRate.setName(typeRateDTO.getName());
        typeRateRepository.save(typeRate);
        return mapToDTO(typeRate);
    }

    public TypeRateDTO update(final Long id, final TypeRateDTO typeRateDTO) {
        final TypeRate typeRate = typeRateRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        typeRate.setName(typeRateDTO.getName());
        typeRateRepository.save(typeRate);
        return mapToDTO(typeRate);
    }

    public void delete(final Long id) {
        final TypeRate typeRate = typeRateRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteTypeRate(id));
        typeRateRepository.delete(typeRate);
    }

    private TypeRateDTO mapToDTO(final TypeRate typeRate) {
        TypeRateDTO typeRateDTO = new TypeRateDTO();
        typeRateDTO.setId(typeRate.getId());
        typeRateDTO.setName(typeRate.getName());
        return typeRateDTO;
    }

    public boolean nameExists(final String name) {
        return typeRateRepository.existsByNameIgnoreCase(name);
    }

}
