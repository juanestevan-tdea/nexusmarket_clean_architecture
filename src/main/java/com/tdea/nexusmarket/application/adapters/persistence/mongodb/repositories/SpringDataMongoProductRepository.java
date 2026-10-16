package com.tdea.nexusmarket.application.adapters.persistence.mongodb.repositories;

import com.tdea.nexusmarket.application.adapters.persistence.mongodb.documents.ProductDocument;
import com.tdea.nexusmarket.application.domain.models.Product;
import com.tdea.nexusmarket.application.domain.ports.out.ProductRepositoryPort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SpringDataMongoProductRepository implements ProductRepositoryPort {

    private final MongoRepository<ProductDocument, Long> mongoRepository;

    public SpringDataMongoProductRepository(MongoRepository<ProductDocument, Long> mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return mongoRepository.findById(id).map(ProductDocument::toDomain);
    }

    @Override
    public List<Product> findAllActive() {
        return mongoRepository.findAll().stream()
            .filter(doc -> "ACTIVE".equals(doc.getStatus()))
            .map(ProductDocument::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Product save(Product product) {
        ProductDocument document = ProductDocument.fromDomain(product);
        ProductDocument saved = mongoRepository.save(document);
        return saved.toDomain();
    }
}
