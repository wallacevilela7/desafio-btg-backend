package tech.wvs.desafiobtg.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tech.wvs.desafiobtg.entity.OrderEntity;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {
}
