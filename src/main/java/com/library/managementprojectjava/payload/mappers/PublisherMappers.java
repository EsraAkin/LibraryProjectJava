package com.library.managementprojectjava.payload.mappers;

import com.library.managementprojectjava.entity.businnes.Publisher;
import com.library.managementprojectjava.payload.request.businnes.PublisherRequest;
import com.library.managementprojectjava.payload.response.businnes.PublisherResponse;
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
