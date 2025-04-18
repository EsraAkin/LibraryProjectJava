package com.library.demo.payload.mappers;

import com.library.demo.entity.businnes.Publisher;
import com.library.demo.payload.request.businnes.PublisherRequest;
import com.library.demo.payload.response.businnes.PublisherResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Builder
@RequiredArgsConstructor
public class PublisherMappers {


    public Publisher mapPublisherRequestToPublisher(PublisherRequest publisherRequest){
        return Publisher.builder()
                .name(publisherRequest.getName())
                .builtIn(publisherRequest.getBuiltIn())
                .build();
    }

    public PublisherResponse mapPublisherToPublisherResponse(Publisher publisher){
        return PublisherResponse.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .builtIn(publisher.getBuiltIn())
                .build();

    }


}
