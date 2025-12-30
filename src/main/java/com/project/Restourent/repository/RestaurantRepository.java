package com.project.Restourent.repository;

import com.project.Restourent.domain.entities.Restaurant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends ElasticsearchRepository<Restaurant,String> {
    //TODO : custom queries
}
