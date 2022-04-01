package de.com.bruns.sample.sampleapp.helloworld;

import org.springframework.data.mongodb.repository.MongoRepository;

interface HelloWorldRepository extends MongoRepository<HelloWorld, String> {

}
