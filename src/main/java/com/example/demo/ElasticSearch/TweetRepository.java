package com.example.demo.ElasticSearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TweetRepository extends ElasticsearchRepository<Tweet, Long> {
    List<Tweet> findAllByText(String text);
    List<Tweet> findByPublicationDateBetween(Long date1, Long date2);

}
