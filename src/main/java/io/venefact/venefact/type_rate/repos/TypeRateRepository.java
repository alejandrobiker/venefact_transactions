package io.venefact.venefact.type_rate.repos;

import io.venefact.venefact.type_rate.domain.TypeRate;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TypeRateRepository extends JpaRepository<TypeRate, Long> {

    boolean existsByNameIgnoreCase(String name);

}
