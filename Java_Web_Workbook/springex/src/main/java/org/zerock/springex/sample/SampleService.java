package org.zerock.springex.sample;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@ToString
@Service
@RequiredArgsConstructor//생성자
public class SampleService {

    @Qualifier("normal")
    private final SampleDAO sampleDAO;


//    lombok에서 만들어주는 생성자 @RequiredArgsConstructor를 사용하지 않을 경우 아래와 같이 한다.
//    @RequiredArgsConstructor와 @Qualifier를 동시에 사용하면 인식을 하지 못해서 lombok.config를 만들어주는 것이다.
//    public SampleService(@Qualifier("normal") SampleDAO sampleDAO) {
//        this.sampleDAO = sampleDAO;
//    }
}
